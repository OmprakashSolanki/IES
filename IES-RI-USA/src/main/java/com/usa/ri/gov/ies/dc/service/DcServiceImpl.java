package com.usa.ri.gov.ies.dc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usa.ri.gov.ies.admin.entity.PlanDetailsEntity;
import com.usa.ri.gov.ies.admin.repository.PlanDetailsRepository;
import com.usa.ri.gov.ies.ar.entity.IesApplicationEntity;
import com.usa.ri.gov.ies.ar.model.IesApplication;
import com.usa.ri.gov.ies.ar.repository.IesAppRepository;
import com.usa.ri.gov.ies.dc.entity.DcCaseChildDetailsEntity;
import com.usa.ri.gov.ies.dc.entity.DcCaseEducationDetailsEntity;
import com.usa.ri.gov.ies.dc.entity.DcCaseIncomeDetailsEntity;
import com.usa.ri.gov.ies.dc.entity.DcCasePlanEntity;
import com.usa.ri.gov.ies.dc.entity.DcCasesEntity;
import com.usa.ri.gov.ies.dc.model.DcCaseChildDetails;
import com.usa.ri.gov.ies.dc.model.DcCaseEducationDetails;
import com.usa.ri.gov.ies.dc.model.DcCaseIncomeDetails;
import com.usa.ri.gov.ies.dc.model.DcCasePlan;
import com.usa.ri.gov.ies.dc.model.DcCases;
import com.usa.ri.gov.ies.dc.repository.DcCaseChildDetailsRepository;
import com.usa.ri.gov.ies.dc.repository.DcCaseEducationDetailsRepository;
import com.usa.ri.gov.ies.dc.repository.DcCaseIncomeDetailsRepository;
import com.usa.ri.gov.ies.dc.repository.DcCasePlanRepository;
import com.usa.ri.gov.ies.dc.repository.DcCasesRepository;

@Service("dcService")
public class DcServiceImpl implements DcService {

	@Autowired
	private IesAppRepository appRepo;

	@Autowired
	private DcCasesRepository dcCasesRepo;

	@Autowired
	private PlanDetailsRepository planRepo;

	@Autowired
	private DcCasePlanRepository dcCasePlanRepo;

	@Autowired
	private DcCaseIncomeDetailsRepository dcCaseIncomeRepo;
	
	@Autowired
	DcCaseEducationDetailsRepository dcCaseEduDetailsRepo;
	
	@Autowired
	DcCaseChildDetailsRepository dcCaseChildDetailsRepo;

	private Logger logger = Logger.getLogger(DcServiceImpl.class);

	/**
	 * This method is used for searching all appId
	 */
	@Override
	public String[] searchAllAppId() {
		logger.debug("**DcService::searchAllAppId() method called**");
		List<IesApplicationEntity> listEntity = appRepo.findAll();
		String[] appIds = new String[listEntity.size()];
		int i = 0;
		for (IesApplicationEntity entity : listEntity) {
			appIds[i++] = entity.getAppId();
		}
		logger.debug("**DcService::searchAllAppId() method ended**");
		return appIds;
	}

	/**
	 * This method is used for searching by AppID
	 */
	@Override
	public IesApplication findByAppId(String appId) {
		logger.debug("**DcService::findByAppId() method called**");
		IesApplicationEntity entity = appRepo.getOne(appId);
		IesApplication appModel = new IesApplication();
		BeanUtils.copyProperties(entity, appModel);
		logger.debug("**DcService::findByAppId() method ended**");
		return appModel;
	}

	/**
	 * This method is used for creating case number
	 */
	@Override
	public Integer createCase(String appId) {
		logger.debug("**DcService::createCase() method called**");
		IesApplication appModel = findByAppId(appId);
		DcCasesEntity entity = new DcCasesEntity();
		BeanUtils.copyProperties(appModel, entity);
		entity.setUpdateDate(null);
		entity.setCreateDate(null);
		entity = dcCasesRepo.save(entity);
		logger.debug("**DcService::createCase() method ended**");
		return entity.getCaseId();
	}

	@Override
	public DcCasePlan findCaseByCaseId(Integer caseId) {

		// get DcCase from repository
		DcCasesEntity entity = dcCasesRepo.findById(caseId).get();

		DcCasePlan planModel = new DcCasePlan();
		// convert into model
		planModel.setCaseId(entity.getCaseId());
		planModel.setFirstName(entity.getFirstName());
		planModel.setLastName(entity.getLastName());

		// BeanUtils.copyProperties(entity, planModel);
		return planModel;
	}

	/**
	 * This method is used for fetching plans from PLAN_DTLS table
	 */
	@Override
	public Map<Integer, String> fetchAllPlans() {
		logger.debug("**DcService::fetchAllPlans() method called**");
		List<PlanDetailsEntity> planListEntity = planRepo.findAll();
		Map<Integer, String> planList = new HashMap<>();

		planListEntity.forEach(entity -> {
			if (entity.getActiveSw().equalsIgnoreCase("Y")) {
				planList.put(entity.getPlanId(), entity.getPlanName());
			}
		});
		logger.debug("**DcService::fetchAllPlans() method ended**");
		return planList;
	}

	/**
	 * This method is used to store Selected Plan
	 */
	@Override
	public boolean saveSelectedPlan(DcCasePlan planModel) {

		DcCasePlanEntity entity = new DcCasePlanEntity();
		// convert model to entity
		BeanUtils.copyProperties(planModel, entity);
		try {
			// call repository save method
			DcCasePlanEntity repoEntity = dcCasePlanRepo.save(entity);

			if (repoEntity != null) {
				return true;
			}
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * This method is used for saving income details
	 */
	@Override
	public boolean saveIncomeDetails(DcCaseIncomeDetails incomeDetails) {
		logger.debug("**DcService::saveIncomeDetails() method called**");
		DcCaseIncomeDetailsEntity entity = new DcCaseIncomeDetailsEntity();
		BeanUtils.copyProperties(incomeDetails, entity);
		try {
			entity = dcCaseIncomeRepo.save(entity);
			logger.debug("**DcService::saveIncomeDetails() method ended with success**");
			return true;
		} catch (Exception e) {
			logger.error("**DcService::saveIncomeDetails() method exception:", e);
		}
		logger.debug("**DcService::saveIncomeDetails() method ended with failure**");
		return false;
	}
	
	@Override
	public boolean saveEducationDetails(DcCaseEducationDetails eduDetails) {
		logger.debug("**DcService::saveEducationDetails() method called**");
		DcCaseEducationDetailsEntity entity = new DcCaseEducationDetailsEntity();
		BeanUtils.copyProperties(eduDetails, entity);
		try {
			entity = dcCaseEduDetailsRepo.save(entity);
			logger.debug("**DcService::saveEducationDetails() method ended with success**");
			return true;
		} catch (Exception e) {
			logger.error("**DcService::saveEducationDetails() method exception:", e);
		}
		logger.debug("**DcService::saveEducationDetails() method ended with failure**");
		return false;
	}
	
	/**
	 * This method is used for saving child details
	 */
	@Override
	public List<DcCaseChildDetails> saveChildDetails(DcCaseChildDetails childDetails) {
		logger.debug("**DcService::saveEducationDetails() method called**");
		DcCaseChildDetailsEntity entity = new DcCaseChildDetailsEntity();
		List<DcCaseChildDetails> listModel = new ArrayList<>();
		BeanUtils.copyProperties(childDetails, entity);
		List<DcCaseChildDetailsEntity> listEntity = dcCaseChildDetailsRepo.findByCaseId(childDetails.getCaseId());

		try {
			entity = dcCaseChildDetailsRepo.save(entity);
			logger.info("***DcService:: child details saved into table***");
			listEntity = dcCaseChildDetailsRepo.findByCaseId(childDetails.getCaseId());
			listEntity.forEach(entityChild -> {
				DcCaseChildDetails dcChildModel = new DcCaseChildDetails();
				BeanUtils.copyProperties(entityChild, dcChildModel);
				listModel.add(dcChildModel);
			});
			logger.debug("**DcService::saveEducationDetails() method ended with success**");
			return listModel;
		} catch (Exception e) {
			logger.error("**DcService::saveEducationDetails() method exception:", e);
		}
		logger.debug("**DcService::saveEducationDetails() method ended with failure**");
		return listModel;
	}
	
	/**
	 * This method is used for updatingChildDetails
	 */
	@Override
	public Map<String, Object> updateChildDetails(Integer childId) {
		Map<String, Object> mapResult=new HashMap<>();
		try {
			logger.debug("**DcService::updateChildDetails() method called**");
			//getting child details to update 
			DcCaseChildDetailsEntity entity = dcCaseChildDetailsRepo.findById(childId).get();
			DcCaseChildDetails childModel=new DcCaseChildDetails();
			BeanUtils.copyProperties(entity, childModel);
			mapResult.put("childModel", childModel);
			//getting all child records related to provided caseId
			List<DcCaseChildDetails> listModel = new ArrayList<>();
			List<DcCaseChildDetailsEntity> listEntity = dcCaseChildDetailsRepo.findByCaseId(childModel.getCaseId());
			listEntity.forEach(entityChild -> {
				DcCaseChildDetails dcChildModel = new DcCaseChildDetails();
				BeanUtils.copyProperties(entityChild, dcChildModel);
				listModel.add(dcChildModel);
			});
			mapResult.put("listModel", listModel);
			logger.debug("**DcService::updateChildDetails() method ended with success**");
			return mapResult;
		} catch (Exception e) {
			logger.error("**DcService::updateChildDetails() method exception:", e);
		}
		logger.debug("**DcService::updateChildDetails() method ended with failure**");
		return mapResult;
	}

	@Override
	public List<DcCaseChildDetails> deleteChildDetails(Integer childId, Integer caseId) {
		List<DcCaseChildDetails> listModel = new ArrayList<>();
		try {
			logger.debug("**DcService::deleteChildDetails() method called**");
			//deleting record of passed childId
			dcCaseChildDetailsRepo.deleteById(childId);
			
			//getting all child records based on caseId
			List<DcCaseChildDetailsEntity> listEntity = dcCaseChildDetailsRepo.findByCaseId(caseId);
			listEntity.forEach(entityChild -> {
				DcCaseChildDetails dcChildModel = new DcCaseChildDetails();
				BeanUtils.copyProperties(entityChild, dcChildModel);
				listModel.add(dcChildModel);
			});
			logger.debug("**DcService::deleteChildDetails() method ended with success**");
			return listModel;
		} catch (Exception e) {
			logger.error("**DcService::deleteChildDetails() method exception:", e);
		}
		logger.debug("**DcService::deleteChildDetails() method ended with failure**");
		return listModel;
	}
	
	/** 
	 * this method is used for retrieving records based on ChilId
	 */
	@Override
	public DcCaseChildDetails findByChildId(String childId) {
		Optional<DcCaseChildDetailsEntity> optEntity = dcCaseChildDetailsRepo.findById(childId);
		DcCaseChildDetailsEntity entity=null;
		if(optEntity.isPresent())
			entity=optEntity.get();
		DcCaseChildDetails childModel=new DcCaseChildDetails();
		BeanUtils.copyProperties(entity, childModel);
		return childModel;
	}
	
	@Override
	public DcCases findCaseById(Integer caseNum) {

		//get Case By id 
		DcCasesEntity entity=dcCasesRepo.findById(caseNum).get();
		//convet entity to model
		DcCases model=new DcCases();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

}
