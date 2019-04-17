package com.usa.ri.gov.ies.co.batches.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usa.ri.gov.ies.co.batches.entity.CoBatchRunDetailsEntity;
import com.usa.ri.gov.ies.co.batches.entity.CoBatchSummaryEntity;

public interface CoBatchSummaryRepository extends JpaRepository<CoBatchSummaryEntity,	 Serializable> {

}
