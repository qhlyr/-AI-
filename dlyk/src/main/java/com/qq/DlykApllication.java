package com.qq;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.context.ApplicationContext;
import org.springframework.ai.embedding.EmbeddingModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
@EnableMethodSecurity
public class DlykApllication {
	public static final Map<String, List<Object>> dictCacheMap = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(DlykApllication.class, args);
	}

	@Bean
	public CommandLineRunner printEmbeddingModels(ApplicationContext context) {
		return args -> {
			System.out.println("🔍 当前容器中可用的 EmbeddingModel Bean：");
			Map<String, EmbeddingModel> beans = context.getBeansOfType(EmbeddingModel.class);
			beans.forEach((name, bean) -> {
				System.out.println("✅ Bean 名称：" + name + "，类型：" + bean.getClass().getSimpleName());
			});
		};
	}
}