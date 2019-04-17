package com.usa.ri.gov.ies.co.batches.main;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.usa.ri.gov.ies.co.batches.model.CoBatchRunDetailsModel;
import com.usa.ri.gov.ies.co.batches.model.CoBatchSummaryModel;
import com.usa.ri.gov.ies.co.batches.model.CoTriggersModel;
import com.usa.ri.gov.ies.co.batches.service.CoBatchRunService;

public abstract class CoAbstractBatch {

	public abstract void init(Integer totalBuckets,Integer currInstance);


	public abstract void start(Integer totalBuckets,Integer  currInstance);
	
	public abstract void process(CoTriggersModel trModel);


	@Autowired
	private CoBatchRunService coBatchService;

	// keeps details of each batch in BatchRunDtls table
	public Integer preProcess(Integer currInstance,String  batchName) {
		// insert batch run details with ST status
		CoBatchRunDetailsModel model = new CoBatchRunDetailsModel();
		model.setBatchName(batchName);
		model.setBatchRunStatus("ST");
		model.setStartDate(new Date());
		model.setRunInstance(currInstance);

		// inserting Run details
		model = coBatchService.insertBatchRunDetails(model);
		model.setRunSeq(model.getRunSeq());
		return model.getRunSeq();
	}
	
	public void postProcess(Integer batchRunSeq,Long SUCCESSFUL_TRG_CNT,Long FAILURE_TRG_CNT) {

		CoBatchRunDetailsModel model = coBatchService.findByBatchRunSeq(batchRunSeq);
		// update batch run details with ED endDate
		model.setBatchRunStatus("EN");
		model.setEndTime(new Date());
		coBatchService.updateBatchRunDetails(model);

		CoBatchSummaryModel bsModel = new CoBatchSummaryModel();
		bsModel.setBatchName(model.getBatchName());
		bsModel.setSuccessTriggerCount(SUCCESSFUL_TRG_CNT);
		bsModel.setFailureTriggerCount(FAILURE_TRG_CNT);
		bsModel.setTotalTriggerProcessed(SUCCESSFUL_TRG_CNT + FAILURE_TRG_CNT);

		// save batch run Summary in BatchSummary table
		coBatchService.insertBatchSummary(bsModel);
	}
}
