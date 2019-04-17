package com.usa.ri.gov.ies.dc.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.usa.ri.gov.ies.dc.entity.DcCasesEntity;


@Repository("dcCasesRepository")
public interface DcCasesRepository extends JpaRepository<DcCasesEntity, Serializable> {

	
}
