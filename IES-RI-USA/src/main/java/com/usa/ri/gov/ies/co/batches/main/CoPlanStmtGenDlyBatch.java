package com.usa.ri.gov.ies.co.batches.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.usa.ri.gov.ies.admin.constants.AppConstants;
import com.usa.ri.gov.ies.ar.service.IesAppService;
import com.usa.ri.gov.ies.co.batches.model.CoPdfModel;
import com.usa.ri.gov.ies.co.batches.model.CoTriggersModel;
import com.usa.ri.gov.ies.co.batches.service.CoBatchRunService;
import com.usa.ri.gov.ies.dc.model.DcCases;
import com.usa.ri.gov.ies.dc.service.DcService;
import com.usa.ri.gov.ies.ed.model.EligibilityDetailModel;
import com.usa.ri.gov.ies.ed.service.EligibilityDetailService;
import com.usa.ri.gov.ies.properties.AppProperties;
import com.usa.ri.gov.ies.util.MailUtils;

@Service("coPlanStmtBatch")
public class CoPlanStmtGenDlyBatch extends CoAbstractBatch {

	private static final String BATCH_NAME = "CO-PLN-STMT-DLY";
	private static Long SUCCESSFUL_TRG_CNT = 0L;
	private static Long FAILURE_TRG_CNT = 0L;

	// private Logger logger = (Logger) LoggerFactory.getInstance();
	@Autowired
	private CoBatchRunService coBatchService;

	@Autowired
	private EligibilityDetailService edDetailsService;

	@Autowired
	private DcService dcService;

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private IesAppService appService;

	@Autowired
	private MailUtils mailUtils;


	
	/*public String startBatch() {
		CoPlanStmtGenDlyBatch batch = new CoPlanStmtGenDlyBatch();
		batch.init();

		return "Batch Completed";
	}*/

	@Override
	public void init(Integer totalBuckets,Integer currInstance) {
		Integer runSeq = super.preProcess(currInstance,BATCH_NAME);
		start(totalBuckets,currInstance);
		super.postProcess(runSeq,SUCCESSFUL_TRG_CNT,FAILURE_TRG_CNT);
	}

	

	// get All the triggers from CoTriggers table
	@Override
	public void start(Integer totalBuckets,Integer  currInstance) {

		
		
		// find all the pending triggers
		List<CoTriggersModel> triggers = coBatchService.findPendingTriggers(totalBuckets,currInstance);
		
		ExecutorService pool = Executors.newFixedThreadPool(5);
		
	
		
		for (CoTriggersModel trigger : triggers) {

			pool.submit(new Callable<String>() {
			   
				@Override
				public String call() throws Exception {
					process(trigger);
					return null;
				}
			});

			//pool.shutdown();
		
		}
	}

	// process the each trigger
	@Override
	public void process(CoTriggersModel trModel) {
		// using trigger case num read eligibility data

		EligibilityDetailModel edModel = edDetailsService.findByCaseNum(trModel.getCaseNum());

		// generate pdf based on planStatus
		String planStatus = edModel.getPlanStatus();
		Document pdfAttachment=null;
		if (planStatus.equalsIgnoreCase("AP")) {
			// generate pdf for Approved plan
			pdfAttachment = generatePdf(edModel);
		} else {
			// generate pdf form Deniad plan
			pdfAttachment=generatePdf(edModel);
		}
		// get DcCasesDeatils
		//DcCases caseModel = dcService.findCaseById(edModel.getCaseNum());

		// store pdf in db
		savePdf(edModel, pdfAttachment);
		// send pdf to customer email
		//sendEmail(caseModel,, pdfAttachment);

		// if success update trigger as completed
		trModel.setTriggerStatus("C");
		trModel.setUpdatedDate(new Date());
		Boolean isUpdated = coBatchService.updatePendingTrigger(trModel);

		// increment trigger count variable (success|failure)
		if(isUpdated) {
			SUCCESSFUL_TRG_CNT++;
		}
		else {
			FAILURE_TRG_CNT++;
		}
			
	}

	private void savePdf(EligibilityDetailModel edModel, Document pdfAttachment) {

		CoPdfModel pdfModel = new CoPdfModel();

		pdfModel.setCaseNumber(edModel.getCaseNum());
		pdfModel.setPlanName(edModel.getPlanName());
		pdfModel.setPlanStatus(edModel.getPlanStatus());
		pdfModel.setPdfDocument(pdfAttachment.toString().getBytes());

		CoPdfModel model = coBatchService.savePdf(pdfModel);
	}

	private void sendEmail(DcCases caseModel, Document pdfAttachment) {

		// get email Template

		String fileName = appProperties.getProperties().get(AppConstants.PLAN_APPROVAL_FILE_NAME);
		String mailSub = appProperties.getProperties().get(AppConstants.PLAN_APPROVAL_SUBJECT);

		try {
			String mailBody = createMailBody(caseModel, fileName);

			// sending confirmation mail

			mailUtils.sendEmail(caseModel.getEmail(), mailSub, mailBody);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// logger.debug(" Plan stmt generate ended");

	}

	public Document generatePdf(EligibilityDetailModel edModel) {
		Document document = new Document();

		//DcCasePlan caseModel = dcService.findCaseByCaseId(edModel.getCaseNum());

		try {

			PdfWriter.getInstance(document, new FileOutputStream(
					new File("E://copdfs/" + edModel.getCaseNum() + "" + edModel.getPlanStatus() + ".pdf")));

			// open
			document.open();

			Paragraph p = new Paragraph();
			p.add("Plan Approval Details");
			p.setAlignment(Element.ALIGN_CENTER);
			Font f = new Font();
			f.setStyle(Font.BOLD);
			f.setSize(8);
			document.add(p);

			/*Paragraph p2 = new Paragraph();
			p2.add("Hi,  " + caseModel.getFirstName() + "  " + caseModel.getLastName() + ","); // no alignment
			p2.setFont(f);
			document.add(p2);*/

			Paragraph p4 = new Paragraph();
			p4.add("The Details Related To plan are mention below");
			p4.setAlignment(Element.ALIGN_LEFT);
			document.add(p4);

			Paragraph p5 = new Paragraph();
			p5.add("CaseId : " + edModel.getCaseNum() + "\n");
			p5.add("Plan Name: " + edModel.getPlanName() + "\n");
			if (edModel.getDenialReason() == null) {
				p5.add("plan Start Date: " + edModel.getPlanStartDate() + "\n");
				p5.add("plan End Date: " + edModel.getPlanEndDate()+ "\n");
				p5.add("plan Start Date: " + edModel.getBenefitAmt() + "\n");
				p5.setFont(f);
			} else {
				p5.add("Plan Denial Reason: " + edModel.getDenialReason() + "\n");
			}
			document.add(p5);

			// close
			document.close();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
 
		
	}

	

	/**
	 * This method is used to create the body of email
	 * 
	 * @param model
	 * @return String
	 * @throws Exception
	 */

	public String createMailBody(DcCases model, String fileName) throws Exception {
		// logger.debug(" Approve Plan Statement batch :: createMailBody started");
		StringBuilder sb = new StringBuilder();

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = null;
		if (br != null) {
			line = br.readLine();
			while (line != null) {
				if (!line.equals("<br/>") && line.length() != 0) {
					if (line.contains("USER_NAME")) {
						line = line.replace("USER_NAME", model.getFirstName() + " " + model.getLastName());
					}

				}
				sb.append(line);
				line = br.readLine();
			}
		}
		br.close();
		// logger.debug("Approve Plan Statement batch:: createMailBody ended");
		// logger.debug("Email body created SuccessFully");
		return sb.toString();
	}// createMailBody

}
