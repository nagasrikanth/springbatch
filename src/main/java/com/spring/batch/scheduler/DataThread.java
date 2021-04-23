package com.spring.batch.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DataThread implements Runnable {

	private static final Logger OUT = LoggerFactory.getLogger(DataThread.class);

	private String clientId;

	public DataThread(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			OUT.debug("Execption while processing UnProssed User Data", e);
		}
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}