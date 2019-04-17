package com.usa.ri.gov.ies.ar.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.usa.ri.gov.ies.admin.constants.AppConstants;
import com.usa.ri.gov.ies.ar.bindings.SsnProfile;
import com.usa.ri.gov.ies.ar.entity.IesApplicationEntity;
import com.usa.ri.gov.ies.ar.model.IesApplication;
import com.usa.ri.gov.ies.ar.repository.IesAppRepository;
import com.usa.ri.gov.ies.properties.AppProperties;

@Service("appService")
public class IesAppServiceImpl implements IesAppService {

	@Autowired(required = true)
	private IesAppRepository appRepo;

	@Autowired
	private AppProperties appProperties;

	private Logger logger = LoggerFactory.getLogger(IesAppServiceImpl.class);

	/**
	 * This method is used for registering Applicant Here we are using Restful
	 * service to validate SSN
	 * 
	 * @param appModel
	 * @return Map<Boolean, String>
	 */
	@Override
	public Map<Boolean, String> registerApplicant(IesApplication appModel) {
		logger.debug("***ArService::registerApplicant() method executed***");
		RestTemplate template = new RestTemplate();
		Map<Boolean, String> map = new HashMap<>();
		IesApplicationEntity entity = null;
		try {
			// call findBySSN() method
			entity = appRepo.findBySsn(appModel.getSsn());
			if (entity == null) {
				URI uri = new URI("http://localhost:2526/validateSSN/" + appModel.getSsn());
				ResponseEntity<SsnProfile> responseEntity = template.getForEntity(uri, SsnProfile.class);

				int statusCode = responseEntity.getStatusCodeValue();
				SsnProfile profile = responseEntity.getBody();
				System.out.println(statusCode);
				/*
				 * if (statusCode == 400) { map.put(false, "Invalid SSN provided!!"); return
				 * map; }
				 */
				if (statusCode == 200) {
					if (profile.getState().equalsIgnoreCase("ri")) {
						// covert Model to Entity
						entity = new IesApplicationEntity();
						BeanUtils.copyProperties(appModel, entity);

						entity = appRepo.save(entity);
						if (entity.getAppId() != null) {
							map.put(true, appProperties.getProperties().get(AppConstants.APPLICANT_REG_SUCCESS)
									+ entity.getAppId());
							logger.debug("***ArService::registerApplicant() method ended***");
							logger.info("***ArService::Registration Successful***");
							return map;
						}
					} else {
						map.put(false, appProperties.getProperties().get(AppConstants.UN_AUTHORISED_APPLICANT));
						logger.debug("***ArService::registerApplicant() method ended***");
						logger.info("***ArService::Applicant is UnAuthorised to apply***");
						return map;
					}
				}
			} else {
				map.put(true,
						appProperties.getProperties().get(AppConstants.APPLICANT_ALREADY_REG) + entity.getAppId());
				logger.debug("***ArService::registerApplicant() method ended***");
				logger.info("***ArService::Applicant is Already Registered***");
				return map;
			}
		} catch (Exception e) {

			logger.error("***ArService::registerApplicant() method error:", e.getMessage());
		}
		logger.debug("***ArService::registerApplicant() method ended with error***");
		map.put(false, appProperties.getProperties().get(AppConstants.INVALID_SSN_MSG));
		return map;
	}

	@Override
	public String checkDuplicateSsn(String ssn) {
		// convert to long
		Long ssn2 = Long.parseLong(ssn);
		IesApplicationEntity entity = appRepo.findBySsn(ssn2);

		return (entity == null) ? "Unique" : "Duplicate";
	}

	/**
	 * This method is used to return All accounts details
	 */
	@Override
	public List<IesApplication> findAllApplicants() {
		logger.debug("findAllAppAccounts() method started");
		List<IesApplication> models = new ArrayList<IesApplication>();
		try {
			// call Repository method
			List<IesApplicationEntity> entities = appRepo.findAll();

			if (entities.isEmpty()) {
				logger.warn("***No Accounts found in Application****");
			} else {
				// convert Entities to models
				for (IesApplicationEntity entity : entities) {
					IesApplication model = new IesApplication();
					BeanUtils.copyProperties(entity, model);
					models.add(model);
				}
				logger.info("All Applicants details loaded successfully");
			}
		} catch (Exception e) {
			logger.error("Exception occured in findAllApplicants()::", e);
		}
		logger.debug("findAllApplicants() method ended");
		return models;
	}// method

	@Override
	public IesApplication findByAppId(String appId) {
		IesApplicationEntity entity = appRepo.findById(appId).get();

		IesApplication model = new IesApplication();

		BeanUtils.copyProperties(entity, model);

		return model;
	}

	/**
	 * This method is used edit application details
	 *
	 * @Param accModel
	 * @return boolean
	 */
	@Override
	public boolean editApplication(IesApplication appModel) {
		logger.debug(" editAppAccount() started");
		// create AppAccount Entity object
		IesApplicationEntity entity = new IesApplicationEntity();

		// convert model object to entity
		BeanUtils.copyProperties(appModel, entity);

		// call repository method
		entity = appRepo.save(entity);

		if (entity.getAppId() != null)
			return true;
		logger.debug("editAppAccount() ended");
		logger.info("editAppAccount completed Successfull");
		return false;
}
}
