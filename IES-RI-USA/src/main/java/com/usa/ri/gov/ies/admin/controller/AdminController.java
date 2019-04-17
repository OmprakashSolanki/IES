package com.usa.ri.gov.ies.admin.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.support.SessionStatus;

import com.usa.ri.gov.ies.admin.constants.AppConstants;
import com.usa.ri.gov.ies.admin.model.AppAccount;
import com.usa.ri.gov.ies.admin.model.PlanDetails;
import com.usa.ri.gov.ies.admin.service.AdminService;
import com.usa.ri.gov.ies.properties.AppProperties;

@Controller

public class AdminController {
	private Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired(required = true)
	private AdminService adminService;

	@Autowired
	private AppProperties appProperties;

	public AdminController() {
		System.out.println("AdminController.AdminController()");
	}

	/**
	 * This method is used to display the registration form
	 * 
	 * @Param model
	 */
	@RequestMapping(value = "/accReg", method = RequestMethod.GET)
	public String accRegForm(Model model) {
		System.out.println("AdminController.accRegForm()");

		logger.debug("AdminController ::accRegForm started");
		AppAccount accModel = new AppAccount();

		model.addAttribute(AppConstants.ACCMODEL, accModel);
		initForm(model);

		logger.debug("AdminController ::accRegForm started");

		return "accReg";
	}

	/**
	 * This is used for Registration form submission
	 * 
	 * @param acc
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/accReg", method = RequestMethod.POST)
	public String accRegistration(@ModelAttribute("accModel") AppAccount acc, Model model) {
		logger.debug("AdminController ::accRegistraction started");
		boolean status = false;
		// call service method
		status = adminService.registerAppAccount(acc);

		if (status) {
			initForm(model);

			model.addAttribute(AppConstants.SUCCESS, appProperties.getProperties().get(AppConstants.ACC_SUCCESS));
		} else {
			model.addAttribute(AppConstants.FAILURE, appProperties.getProperties().get(AppConstants.ACC_FAIL));
			logger.debug("AdminController ::accRegForm ended");
			logger.info("Case Worker Registered SuccessFully");
		}
		return "accReg";
	}

	/**
	 * This method is used to load roles for application
	 * 
	 * @param model
	 */
	private void initForm(Model model) {
		List<String> rolesList = new ArrayList<>();
		rolesList.add("Case Worker");
		rolesList.add("Admin");
		model.addAttribute("rolesList", rolesList);

		List<String> gendersList = new ArrayList<>();
		gendersList.add("Male");
		gendersList.add("Fe-Male");
		model.addAttribute("gendersList", gendersList);
	}

	/**
	 * This method is used to check the email validate The emailId
	 * 
	 * @param req
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "accReg/validateEmail")
	public @ResponseBody String varifyEmail(HttpServletRequest req, Model model) {
		logger.debug("Email Validation started");
		String email = req.getParameter("email");
		logger.debug("Email Validation ended");
		return adminService.checkDuplicateEmail(email);
	}

	/**
	 * This method is used for displaying all the accounts
	 * 
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/viewAccounts", method = RequestMethod.GET)
	public String findAllAccounts(Model model) {
		logger.debug("AdminController ::searchAllAccounts method started");
		// invoke Service method
		List<AppAccount> modelsList = adminService.findAllAccounts();

		// add data to model scope
		model.addAttribute(AppConstants.APP_ACCOUNTS, modelsList);

		logger.debug("AdminController ::searchAllAccounts method ended");
		logger.info("findAllAccounts Method completed");
		return "viewAccounts";
	}

	/**
	 * This method used to De-Activate the account
	 * 
	 * @param acc_id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String deleteAccount(HttpServletRequest req, Model model) {
		// call service method
		boolean isDelete = adminService.updateActiveSw(Integer.parseInt(req.getParameter("accId")),
				AppConstants.IN_ACTIVE_SW);

		// invoke Service method
		List<AppAccount> modelsList = adminService.findAllAccounts();

		// add data to model scope
		model.addAttribute(AppConstants.APP_ACCOUNTS, modelsList);
		if (isDelete) {
			// add account De-Activation success Message
			String successMsg = appProperties.getProperties().get(AppConstants.ACC_DE_ACTIVATE_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);
			return "viewAccounts";
		} else {
			String failureMsg = appProperties.getProperties().get(AppConstants.ACC_DE_ACTIVATE_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);

			return "viewAccounts";
		}
	}

	/**
	 * This method used to De-Activate the account
	 * 
	 * @param acc_id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/activate", method = RequestMethod.GET)
	public String activateAccount(HttpServletRequest req, Model model) {
		logger.debug("activateAccount method started");
		// call service method
		boolean isActivate = adminService.updateActiveSw(Integer.parseInt(req.getParameter("accId")),
				AppConstants.ACTIVE_SW);
		logger.debug("activateAccount method ended");

		// invoke Service method
		List<AppAccount> modelsList = adminService.findAllAccounts();

		// add data to model scope
		model.addAttribute(AppConstants.APP_ACCOUNTS, modelsList);
		if (isActivate) {
			// add account Activation success Message
			String successMsg = appProperties.getProperties().get(AppConstants.ACC_ACTIVATE_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);
			return "viewAccounts";
		} else {
			// add account activation failure message
			String failureMsg = appProperties.getProperties().get(AppConstants.ACC_ACTIVATE_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);

			return "viewAccounts";
		}
	}

	/**
	 * This method is used for editing account
	 * 
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editAcc")
	public String showEditAccount(HttpServletRequest req, Model model) {
		logger.debug("AdminController::showEditAccount() started");
		String accId = req.getParameter("accId");

		// get account by accId
		AppAccount accModel = adminService.findByAccountId(accId);

		// add model to model scope
		model.addAttribute(AppConstants.ACCMODEL, accModel);

		initForm(model);
		logger.debug("AdminController::showEditAccount() ended");
		logger.info("showEditAccount completed successfully");
		return "editAcc";
	}

	/**
	 * This method is used for Updating Account Details
	 * 
	 * @param planDTLS
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/editAcc", method = RequestMethod.POST)
	public String editAccount(@ModelAttribute("accModel") AppAccount accModel, Model model) {
		logger.debug("AdminController:: editAccount() method started");
		boolean status = false;
		// Invoke Service method
		status = adminService.editAppAccount(accModel);

		if (status) {
			// get SuccessMessage value
			String successMsg = appProperties.getProperties().get(AppConstants.EDIT_ACC_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);
		} else {
			// get Failure Message
			String failureMsg = appProperties.getProperties().get(AppConstants.EDIT_ACC_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);
		}
		// add roles Details
		initForm(model);
		logger.debug("AdminController:: editAccount() method started");
		logger.info("editAccount  Method completed Successfully");

		return "editAcc";
	}

	/**
	 * This method is used for displaying plan registration form
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/planReg", method = RequestMethod.GET)
	public String planRegForm(Model model) {
		logger.debug("AdminController:: planRegForm() method started");
		PlanDetails planModel = new PlanDetails();

		model.addAttribute(AppConstants.PLANMODEL, planModel);
		logger.debug("AdminController:: planRegForm() method started");
		return "planReg";
	}

	/**
	 * This method is used for Plan Registration
	 * 
	 * @param planDTLS
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/planReg", method = RequestMethod.POST)
	public String planCreation(@ModelAttribute("planModel") PlanDetails planDTLS, Model model) {
		logger.debug("AdminController:: planCreation() method started");
		boolean status = false;
		// Invoke Service method
		status = adminService.createPlan(planDTLS);

		if (status) {
			// get SuccessMessage value
			String successMsg = appProperties.getProperties().get(AppConstants.PLAN_REG_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);
		} else {
			// get Failure Message
			String failureMsg = appProperties.getProperties().get(AppConstants.PLAN_REG_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);
		}
		logger.debug("AdminController:: planCreation() method started");
		logger.info("Plan Creation Method executed Successfully");
		return "planReg";
	}

	/**
	 * This method is used to validate the plan
	 * 
	 * @param req
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = { "planReg/validatePlan", "editPlan/validatePlan" })
	public @ResponseBody String varifyPlan(HttpServletRequest req, Model model) {
		logger.debug("Plan Validation started");
		String plan = req.getParameter("plan");
		logger.debug("Plan Validation ended");
		return adminService.checkDuplicatePlan(plan);
	}

	/**
	 * This method is used for displaying all the plans
	 * 
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/viewPlans", method = RequestMethod.GET)
	public String findAllPlans(Model model) {
		logger.debug("AdminController ::findAllPlans() method started");
		// invoke Service method
		List<PlanDetails> modelsList = adminService.findAllPlans();

		// add data to model scope
		model.addAttribute(AppConstants.PlANS, modelsList);

		logger.debug("AdminController ::findAllPlans() method ended");
		logger.info("findAllPlans Method completed");
		return "viewPlans";
	}

	/**
	 * This method used to De-Activate the account
	 * 
	 * @param acc_id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deletePlan", method = RequestMethod.GET)
	public String deletePlan(HttpServletRequest req, Model model) {
		logger.debug("AdminController:: deletePlan(); started");
		// call service method
		int planId = Integer.parseInt(req.getParameter("planId"));
		boolean isDelete = adminService.updatePlanActiveSw(planId, AppConstants.IN_ACTIVE_SW);

		// invoke Service method
		List<PlanDetails> modelsList = adminService.findAllPlans();

		// add data to model scope
		model.addAttribute(AppConstants.PlANS, modelsList);
		if (isDelete) {
			// add account De-Activation success Message
			String successMsg = appProperties.getProperties().get(AppConstants.PLAN_DE_ACTIVATE_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);

			logger.debug("AdminController:: deletePlan(); started");
			logger.info("Plan Deactivated SuccessFully");
			return "viewPlans";
		} else {
			String failureMsg = appProperties.getProperties().get(AppConstants.PLAN_DE_ACTIVATE_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);

			return "viewPlans";
		}
	}

	/**
	 * This method used to De-Activate the account
	 * 
	 * @param acc_id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/activatePlan", method = RequestMethod.GET)
	public String activatePlan(HttpServletRequest req, Model model) {

		logger.debug("AdminController:: activatePlan(); started");

		// call service method
		boolean isDelete = adminService.updatePlanActiveSw(Integer.parseInt(req.getParameter("planId")),
				AppConstants.ACTIVE_SW);

		// invoke Service method
		List<PlanDetails> modelsList = adminService.findAllPlans();

		// add data to model scope
		model.addAttribute(AppConstants.PlANS, modelsList);
		if (isDelete) {
			// add plan De-Activation success Message
			String successMsg = appProperties.getProperties().get(AppConstants.PLAN_ACTIVATE_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);

			logger.debug("AdminController:: activatePlan(); started");
			logger.info("Plan Activated SuccessFully");

			return "viewPlans";
		} else {
			String failureMsg = appProperties.getProperties().get(AppConstants.PLAN_ACTIVATE_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);

			return "viewPlans";
		}
	}

	/**
	 * This method is used to show editPlan with initial Data
	 * 
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editPlan", method = RequestMethod.GET)
	public String showEditPlan(HttpServletRequest req, Model model, SessionStatus status) {
		logger.debug("AdminController::showEditPlan( ); method started");
		
		// read plan id from request scope
		int planId = Integer.parseInt(req.getParameter("planId"));
		status.setComplete();
		
		// call service method to get initial model
		PlanDetails planDetails = adminService.findPlanById(planId);

		if (planDetails != null) {
			// put into model scope
			model.addAttribute(AppConstants.PLANMODEL, planDetails);
		}
		logger.debug("AdminController::showEditPlan( ); method ended");
		logger.info(" showEditPlan form completed Successfully");
		return "editPlan";
	}

	/**
	 * This method is used for Editing Plan Details
	 * 
	 * @param planDTLS
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "/editPlan", method = RequestMethod.POST)
	public String editPlan(@ModelAttribute("planModel") PlanDetails planModel, Model model) {
		logger.debug("AdminController:: editPlan() method started");
		boolean status = false;
		// Invoke Service method
		status = adminService.editPlan(planModel);

		if (status) {
			// get SuccessMessage value
			String successMsg = appProperties.getProperties().get(AppConstants.EDIT_PLAN_SUCCESS_MSG);
			model.addAttribute(AppConstants.SUCCESS, successMsg);
		} else {
			// get Failure Message
			String failureMsg = appProperties.getProperties().get(AppConstants.EDIT_PLAN_ERR_MSG);
			model.addAttribute(AppConstants.FAILURE, failureMsg);
		}
		logger.debug("AdminController:: editPlan() method started");
		logger.info("editPlan  Method completed Successfully");

		return "editPlan";
	}

	/**
	 * This method is used to show account login form
	 * 
	 * @return String 
	 */
	@RequestMapping(value = "/")
	public String loginForm() {

		return "login";
	}

	@RequestMapping(value = "/forgotPwd")
	public String forgotPwdForm() {
		return "forgotPwd";
	}
	/**
	 * This method is Used to login functionality
	 * @param request,model
	 * @return String 
	 * 
	 */
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(HttpServletRequest req, Model model) {
		logger.debug("AdminController::login() started" );
		
		String msg = null;
		
		// read UserName and password
		String uname = req.getParameter("uname");
		String pwd = req.getParameter("pwd");
		
		// call service method
		msg = adminService.appAccountLogin(uname, pwd);

		logger.debug("AdminController::login() ended" );
		logger.info("Login completed" );
		
		if (msg.equals("Admin"))
			return "admin_dashboard";
		else if (msg.equals("CaseWorker"))
			return "caseworker_dashboard";
		else if (msg.equals("De_Activated")) {
			String deActivateMsg = appProperties.getProperties().get(AppConstants.DE_ACTIVATED_ACCOUNT);
			model.addAttribute(AppConstants.FAILURE, deActivateMsg);
			return "login";
		} 
		else if (msg.equals("")) {
			String invalidCrdMsg = appProperties.getProperties().get(AppConstants.IN_VALID_CREDENTIALS);
			model.addAttribute(AppConstants.FAILURE, invalidCrdMsg);
			return "login";
		}
		model.addAttribute(AppConstants.IN_VALID_EMAILID, msg);
		return "login";
	}

}
