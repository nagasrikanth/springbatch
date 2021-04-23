/**
 * 
 */
package com.spring.batch.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.batch.dao.PayloadDataDao;
import com.spring.batch.dto.RequestPayloadData;

/**
 * @author Abdul Karim
 *
 */
@Service
public class DataServices {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataServices.class);

	@Autowired
	PayloadDataDao payloadDataDao;
	
	public List<RequestPayloadData> getRequestPayloadData(){
		LOGGER.info("getRequestPayloadData---- ");
		return payloadDataDao.getRequestPayloadData();
	}
}
