package com.spring.batch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource("classpath:config.properties")
@Component("config")
public class DataConig {

	private static final Logger logger = LoggerFactory.getLogger(DataConig.class);

	@Value("${fixedRate.schedule.string}")
	private String fixedRate;

	@Value("${initialDelay.schedule.string}")
	private String initialDelay;

	@Value("${corePoolSize.schedule.integer}")
	private Integer corePoolSize;

	@Value("${maxPoolSize.schedule.integer}")
	private Integer maxPoolSize;

	@Value("${queueCapacity.schedule.integer}")
	private Integer queueCapacity;
	
	@Value("${spring.schedule.enable}")
	private Boolean enable;

	@Bean(name = "threadPoolTaskExecutor")
	public ThreadPoolTaskExecutor getAsyncExecutor() {
		logger.debug("ThreadPoolTaskExecutor getAsyncExecutor  Start ");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(this.getCorePoolSize());
		executor.setMaxPoolSize(this.getMaxPoolSize());
		executor.setQueueCapacity(this.getQueueCapacity());
		executor.setThreadNamePrefix("THREAD WITH CLINT ID -");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.initialize();
		logger.debug("ThreadPoolTaskExecutor getAsyncExecutor  End ");
		return executor;
	}

	public Integer getFixedRate() {
		logger.debug("ProcessConig fixedRate :{} ",fixedRate);
		return Integer.valueOf(fixedRate);
	}

	public void setFixedRate(String fixedRate) {
		this.fixedRate = fixedRate;
	}

	public String getInitialDelay() {
		logger.debug("ProcessConig initialDelay :{} ",initialDelay);
		return initialDelay;
	}

	public void setInitialDelay(String initialDelay) {
		this.initialDelay = initialDelay;
	}

	public Integer getCorePoolSize() {
		logger.debug("ProcessConig corePoolSize :{} ",corePoolSize);
		return corePoolSize;
	}

	public void setCorePoolSize(Integer corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public Integer getMaxPoolSize() {
		logger.debug("ProcessConig maxPoolSize :{} ",maxPoolSize);
		return maxPoolSize;
	}

	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public Integer getQueueCapacity() {
		logger.debug("ProcessConig queueCapacity :{} ",queueCapacity);
		return queueCapacity;
	}

	public void setQueueCapacity(Integer queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
