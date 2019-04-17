package com.usa.ri.gov.ies.ar.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.usa.ri.gov.ies.admin.constants.AppConstants;
import com.usa.ri.gov.ies.ar.constants.ARConstants;
import com.usa.ri.gov.ies.ar.model.IesApplication;
import com.usa.ri.gov.ies.ar.service.IesAppService;
import com.usa.ri.gov.ies.properties.AppProperties;

@Controller
public class ApplicationRegistrationController {

	@Autowired(required = true)
	private IesAppService appService;

	@Autowired(required = true)
	private AppProperties appProperties;

	private Logger logger = LoggerFactory.getLogger(ApplicationRegistrationController.class);

	@RequestMapping(value = "/appReg", method = RequestMethod.GET)
	public String showAppForm(Model model) {

		// create CommandClass
		IesApplication appModel = new IesApplication();

		model.addAttribute(ARConstants.APPMODEL, appModel);
		init(model);
		return "appReg";
	}

	/**
	 * This method is used to register applicant with given values
	 * 
	 * @param appAccModel
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/appReg", method = RequestMethod.POST)
	public String appReg(@ModelAttribute("appModel") IesApplication appModel, Model model) {
		try {
			logger.debug("ArController::user account creation started");

			// call Service layer method
			Map<Boolean, String> map = appService.registerApplicant(appModel);

			// Map<String, String> map = appProperties.getProperties();
			if (!map.isEmpty()) {
				// Display success message
				if (map.containsKey(true))
					model.addAttribute(AppConstants.SUCCESS, map.get(true));
				else
					// Display failure message
					model.addAttribute(AppConstants.FAILURE, map.get(false));
			}
			init(model);
			logger.debug("ArController::user account creation ended");
			logger.info("ArController::User Account creation completed successfully");
		} catch (Exception e) {
			logger.error("User Account Creation Failed :: " + e.getMessage());
		}
		return "appReg";
	}

	private void init(Model model) {

		List<String> gendersList = new ArrayList<>();
		gendersList.add("Male");
		gendersList.add("Female");

		model.addAttribute("gendersList", gendersList);
	}

	@RequestMapping(value = "appReg/validateSsn")
	public @ResponseBody String varifySsn(HttpServletRequest req, Model model) {
		logger.debug("SSN Validation started");
		String ssn = req.getParameter("ssn");
		logger.debug("SSN Validation ended");
		return appService.checkDuplicateSsn(ssn);
	}

	/**
	 * This method is used to display all app accounts in table
	 * 
	 * @return String
	 */
	@RequestMapping(value = "/viewApps")
	public String viewApplicants(Model model) {
		logger.debug("viewApplicants() method started");

		// calling service layer method
		List<IesApplication> appModel = appService.findAllApplicants();

		// store accounts in model scope
		model.addAttribute(AppConstants.APP_MODEL, appModel);

		logger.debug("viewApplicants() method ended");
		return "viewApps"; // view name
	}

	@RequestMapping(value = "/editApp")
	public String editApplicationForm(HttpServletRequest req, Model model) {

		String appId = req.getParameter("appId");

		IesApplication appModel = appService.findByAppId(appId);

		model.addAttribute(AppConstants.APP_MODEL, appModel);
		init(model);
		return "editApp";
	}

	/**
	 * This method is used for Updating Account Details
	 * 
	 * @param planDTLS
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/editApp", method = RequestMethod.POST)
	public String editApplication(@ModelAttribute("appModel") IesApplication appModel, Model model) {
		logger.debug("ArController:: editApplication() method started");
		boolean status = false;
		// Invoke Service method
		status = appService.editApplication(appModel);

		if (status) {
			// get SuccessMessage value
			String successMsg = appProperties.getProperties().get(AppConstants.UPDATE_APP_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);
		} else {
			// get Failure Message
			String failureMsg = appProperties.getProperties().get(AppConstants.UPDATE_APP_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);
		}
		// add roles Details
		init(model);
		logger.debug("AdminController:: editAccount() method started");
		logger.info("editAccount  Method completed Successfully");

		return "editApp";
	}

}
