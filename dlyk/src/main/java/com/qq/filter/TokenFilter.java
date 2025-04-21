package com.qq.filter;

import com.qq.model.po.TUser;
import com.qq.model.result.CodeEnum;
import com.qq.model.result.R;
import com.qq.utils.Constants;
import com.qq.utils.JWTUtils;
import com.qq.utils.JsonUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.qq.utils.Constants.LOGIN_OUT_URL;
import static com.qq.utils.Constants.REDIS_TOKEN_KEY;
import static com.qq.utils.JWTUtils.DEFAULT_EXPIRE_TIME;
import static com.qq.utils.JWTUtils.EXPIRE_TIME;

@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {
	private final StringRedisTemplate redisTemplate;
	private final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String uri = request.getRequestURI();

		// ✅ 放行登录接口
		if (uri.contains(Constants.LOGIN_URL)) {
			filterChain.doFilter(request, response);
			return;
		}

		// ✅ 放行 /helloworld 开头的请求
		if (uri.startsWith("/helloworld")) {
			filterChain.doFilter(request, response);
			return;
		}

		response.setContentType("application/json;charset=utf-8");
		String token = request.getHeader("Authorization");
		if (!StringUtils.hasText(token)) {
			String resJson = JsonUtil.toJson(R.FAIL(CodeEnum.TOKEN_IS_EMPTY));
			response.setStatus(401);
			response.getWriter().write(resJson);
			return;
		}
		if (!JWTUtils.verifyJWT(token)) {
			String resJson = JsonUtil.toJson(R.FAIL(CodeEnum.TOKEN_IS_ILLEGAL));
			response.setStatus(401);
			response.getWriter().write(resJson);
			return;
		}
		TUser user = JWTUtils.parseJWTGetUser(token);
		String redisToken = redisTemplate.opsForValue().get(REDIS_TOKEN_KEY + user.getId());
		if (redisToken == null) {
			String resJson = JsonUtil.toJson(R.FAIL(CodeEnum.TOKEN_IS_EXPIRE));
			response.setStatus(401);
			response.getWriter().write(resJson);
			return;
		}
		if (!token.equals(redisToken)) {
			String resJson = JsonUtil.toJson(R.FAIL(CodeEnum.TOKEN_IS_ILLEGAL));
			response.setStatus(401);
			response.getWriter().write(resJson);
			return;
		}
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(user, user.getId(), user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		EXECUTOR_SERVICE.submit(() -> {
			if (uri.contains(LOGIN_OUT_URL)) return;
			boolean rememberMe = Boolean.parseBoolean(request.getHeader("rememberMe"));
			if (rememberMe) {
				redisTemplate.expire(REDIS_TOKEN_KEY + user.getId(), EXPIRE_TIME, TimeUnit.MINUTES);
			} else {
				redisTemplate.expire(REDIS_TOKEN_KEY + user.getId(), DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
			}
		});
		filterChain.doFilter(request, response);
	}
}
