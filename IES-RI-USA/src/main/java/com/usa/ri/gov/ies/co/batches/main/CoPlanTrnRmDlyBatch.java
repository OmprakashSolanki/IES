package com.usa.ri.gov.ies.co.batches.main;

import org.springframework.stereotype.Service;

import com.usa.ri.gov.ies.co.batches.model.CoTriggersModel;

@Service("coPlanTrnRmDailyBatch")
public class CoPlanTrnRmDlyBatch extends CoAbstractBatch {

	
	private static final String BATCH_NAME = "CO-PLN-TRN-RM-DLY";
	private static Long SUCCESSFUL_TRG_CNT = 0L;
	private static Long FAILURE_TRG_CNT = 0L;
	
	@Override
	public void init(Integer totalBuckets, Integer currInstance) {
		Integer runSeq = super.preProcess(currInstance,BATCH_NAME);
		start(totalBuckets,currInstance);
		super.postProcess(runSeq,SUCCESSFUL_TRG_CNT,FAILURE_TRG_CNT);
			
	}

	@Override
	public void start(Integer totalBuckets, Integer currInstance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process(CoTriggersModel trModel) {
		// TODO Auto-generated method stub
		
	}

	

}
