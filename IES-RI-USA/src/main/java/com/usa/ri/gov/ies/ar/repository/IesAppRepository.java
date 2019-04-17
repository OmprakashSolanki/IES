package com.usa.ri.gov.ies.ar.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.usa.ri.gov.ies.ar.entity.IesApplicationEntity;

@Repository("iesAppRepository")
public interface IesAppRepository  extends JpaRepository<IesApplicationEntity,Serializable>{

	@Query(name="from IesApplicationEntity where ssn=:ssn2")
	public IesApplicationEntity findBySsn(Long ssn2);

}
