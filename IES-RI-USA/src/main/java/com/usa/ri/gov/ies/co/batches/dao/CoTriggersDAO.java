package com.usa.ri.gov.ies.co.batches.dao;

import java.util.List;

import com.usa.ri.gov.ies.co.batches.entity.CoTriggersEntity;

public interface CoTriggersDAO {

	public List<CoTriggersEntity> findByTriggerStatus(String status,Integer totalBuckets,Integer currInstance);
}
