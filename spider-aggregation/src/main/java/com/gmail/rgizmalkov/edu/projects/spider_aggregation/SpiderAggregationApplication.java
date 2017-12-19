package com.gmail.rgizmalkov.edu.projects.spider_aggregation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@SpringBootApplication
public class SpiderAggregationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpiderAggregationApplication.class, args);
	}

	@Value("${node.name:#{\"node\"}}")
	private String nodeName;

	@Value("${node.threads.maxPoolSize:#{4}}")
	private Integer threadsPoolSize;

	@Value("${node.threads.corePoolSize:#{2}}")
	private Integer corePoolSize;

	@Value("${node.threads.queueCapacity:#{500}}")
	private Integer queueCapacity;


	@Bean
	public ThreadPoolTaskExecutor executorService() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(threadsPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("spider-");
		executor.initialize();
		return executor;
	}
}
