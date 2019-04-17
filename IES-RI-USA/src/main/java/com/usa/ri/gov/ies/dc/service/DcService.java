package com.usa.ri.gov.ies.dc.service;

import java.util.List;
import java.util.Map;

import com.usa.ri.gov.ies.ar.model.IesApplication;
import com.usa.ri.gov.ies.dc.model.DcCaseChildDetails;
import com.usa.ri.gov.ies.dc.model.DcCaseEducationDetails;
import com.usa.ri.gov.ies.dc.model.DcCaseIncomeDetails;
import com.usa.ri.gov.ies.dc.model.DcCasePlan;
import com.usa.ri.gov.ies.dc.model.DcCases;

public interface DcService {
	
	public String[] searchAllAppId();

	public Integer createCase(String id);
	
	public IesApplication findByAppId(String appId);

	public DcCasePlan findCaseByCaseId(Integer caseId);

	public Map<Integer,String> fetchAllPlans();

	public boolean saveSelectedPlan(DcCasePlan planModel);
	
	public boolean saveIncomeDetails(DcCaseIncomeDetails incomeDetails);

	public boolean saveEducationDetails(DcCaseEducationDetails eduDetails);

	public List<DcCaseChildDetails> saveChildDetails(DcCaseChildDetails childDetail);

	public Map<String, Object> updateChildDetails(Integer childId);

	DcCaseChildDetails findByChildId(String childId);

	List<DcCaseChildDetails> deleteChildDetails(Integer childId, Integer caseId);

	public DcCases findCaseById(Integer caseNum);
	

}
