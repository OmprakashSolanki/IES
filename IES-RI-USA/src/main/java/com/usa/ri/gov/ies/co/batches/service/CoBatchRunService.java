package com.usa.ri.gov.ies.co.batches.service;

import java.util.List;

import com.usa.ri.gov.ies.co.batches.model.CoBatchRunDetailsModel;
import com.usa.ri.gov.ies.co.batches.model.CoBatchSummaryModel;
import com.usa.ri.gov.ies.co.batches.model.CoPdfModel;
import com.usa.ri.gov.ies.co.batches.model.CoTriggersModel;

public interface CoBatchRunService {

	public CoBatchRunDetailsModel insertBatchRunDetails(CoBatchRunDetailsModel model);

	public List<CoTriggersModel> findPendingTriggers(Integer totalBuckets,Integer currInstance);
	
	// after trigger is processed insert pdf
	public CoPdfModel savePdf(CoPdfModel model);

	// after processing completed mark trigger as completed
	public boolean updatePendingTrigger(CoTriggersModel model);

	public CoBatchRunDetailsModel findByBatchRunSeq(Integer batchRunSeq);

	public void updateBatchRunDetails(CoBatchRunDetailsModel model);

	public void insertBatchSummary(CoBatchSummaryModel bsModel);
	
	
}
