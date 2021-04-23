package com.spring.batch.dao;

import java.util.List;

import com.spring.batch.dto.RequestPayloadData;

public interface PayloadDataDao {
//	@Query(value = "SELECT * FROM RequestPayloadData")
	 public List<RequestPayloadData> getRequestPayloadData();
	 
}
