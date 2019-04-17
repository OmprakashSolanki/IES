package com.usa.ri.gov.ies.ar.service;

import java.util.List;
import java.util.Map;

import com.usa.ri.gov.ies.ar.model.IesApplication;

public interface IesAppService {

	public Map<Boolean, String> registerApplicant(IesApplication appModel);

	public String checkDuplicateSsn(String ssn);
	
	public IesApplication findByAppId(String appId);
	
	public boolean editApplication(IesApplication appModel);
	
	public List<IesApplication> findAllApplicants();
}
