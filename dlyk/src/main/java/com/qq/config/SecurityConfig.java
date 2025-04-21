package com.qq.config;

import com.qq.filter.TokenFilter;
import com.qq.model.po.TUser;
import com.qq.model.result.R;
import com.qq.utils.JWTUtils;
import com.qq.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.concurrent.TimeUnit;

import static com.qq.utils.Constants.*;
import static com.qq.utils.JWTUtils.DEFAULT_EXPIRE_TIME;
import static com.qq.utils.JWTUtils.EXPIRE_TIME;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final StringRedisTemplate redisTemplate;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public CorsConfigurationSource corsConfiguration() {
		CorsConfiguration configuration = new CorsConfiguration();
		// 允许所有来源（建议根据需求修改）
		configuration.addAllowedOriginPattern("*");
		// 允许所有请求方法（GET, POST, PUT, DELETE, etc.）
		configuration.addAllowedMethod("*");
		// 允许所有请求头
		configuration.addAllowedHeader("*");
		// 允许携带凭证（如 Cookies、Authorization 头）
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// 对所有路径应用 CORS 规则
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.cors((cors)->{
					cors.configurationSource(corsConfiguration());
				})
				.formLogin((config) -> {
					config.loginProcessingUrl(LOGIN_URL)
						.usernameParameter("loginAct")
						.passwordParameter("loginPwd")
						.successHandler((request, response, authentication) -> {
							TUser user = (TUser) authentication.getPrincipal();
							String userJson = JsonUtil.toJson(user);

							// 生成jwt，如果勾选了记住我，则保存7天，否则保存30分钟
							String jwt = JWTUtils.createJWT(userJson);
							boolean rememberMe = Boolean.parseBoolean(request.getParameter("rememberMe"));
							redisTemplate.opsForValue().set(REDIS_TOKEN_KEY + user.getId(), jwt,
									rememberMe ? EXPIRE_TIME : DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);

							user.setLoginPwd(null);
							String res = JsonUtil.toJson(R.OK(user, jwt));
							response.setContentType("application/json;charset=utf-8");
							response.getWriter().write(res);
						})
						.failureHandler((request, response, exception) -> {
							String json = JsonUtil.toJson(R.FAIL(exception.getMessage()));
							response.setContentType("application/json;charset=utf-8");
							response.getWriter().write(json);
						});

				})
				.logout((config) -> {
					config.logoutUrl(LOGIN_OUT_URL)
						.logoutSuccessHandler((request, response, authentication) -> {
							TUser user = (TUser) authentication.getPrincipal();
							redisTemplate.delete(REDIS_TOKEN_KEY + user.getId());
							response.setContentType("application/json;charset=utf-8");
							response.getWriter().write(JsonUtil.toJson(R.OK()));
						});
				})
				.authorizeHttpRequests((config) -> {
					config.requestMatchers(LOGIN_URL, "/helloworld/**").permitAll()
						.anyRequest().authenticated();
				})
				// 认证或授权失败时触发（配置了全局异常处理器，优先走全局异常处理器逻辑，走不到此处）
				/*.exceptionHandling((config) -> {
					// 没有权限时触发
					config.accessDeniedHandler((request, response, accessDeniedException) -> {
						String json = JsonUtil.toJson(R.FAIL(ACCESS_DENIED));
						response.getWriter().write(json);
					});
				})*/
				.csrf(AbstractHttpConfigurer::disable)
				// 禁用session，前后端分离不需要用session，用jwt
				.sessionManagement((config) -> {
					config.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				})
				.addFilterBefore(new TokenFilter(redisTemplate), LogoutFilter.class)
				.build();
	}
}
