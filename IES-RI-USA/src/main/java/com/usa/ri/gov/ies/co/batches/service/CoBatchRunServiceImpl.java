package com.usa.ri.gov.ies.co.batches.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.usa.ri.gov.ies.co.batches.dao.CoTriggersDAO;
import com.usa.ri.gov.ies.co.batches.entity.CoBatchRunDetailsEntity;
import com.usa.ri.gov.ies.co.batches.entity.CoBatchSummaryEntity;
import com.usa.ri.gov.ies.co.batches.entity.CoPdfEntity;
import com.usa.ri.gov.ies.co.batches.entity.CoTriggersEntity;
import com.usa.ri.gov.ies.co.batches.model.CoBatchRunDetailsModel;
import com.usa.ri.gov.ies.co.batches.model.CoBatchSummaryModel;
import com.usa.ri.gov.ies.co.batches.model.CoPdfModel;
import com.usa.ri.gov.ies.co.batches.model.CoTriggersModel;
import com.usa.ri.gov.ies.co.batches.repository.CoBatchRunDetailsRepository;
import com.usa.ri.gov.ies.co.batches.repository.CoBatchSummaryRepository;
import com.usa.ri.gov.ies.co.batches.repository.CoPdfRepository;
import com.usa.ri.gov.ies.co.batches.repository.CoTriggersRepository;

@Service("coBatchService")
public class CoBatchRunServiceImpl implements CoBatchRunService {

	@Autowired
	CoBatchRunDetailsRepository coBatchRunDetailsRepo;

	@Autowired
	CoTriggersRepository coTriggersRepo;

	@Autowired
	CoBatchSummaryRepository coBatchSummaryRepo;

	@Autowired
	CoPdfRepository coPdfRepository;
	
	@Autowired
	private CoTriggersDAO coTriggersDAO;

	@Override
	public CoBatchRunDetailsModel insertBatchRunDetails(CoBatchRunDetailsModel model) {

		// convert model to entity
		CoBatchRunDetailsEntity entity = new CoBatchRunDetailsEntity();
		BeanUtils.copyProperties(model, entity);
		CoBatchRunDetailsEntity runSeqentity = coBatchRunDetailsRepo.save(entity);

		// convert entity to model
		BeanUtils.copyProperties(runSeqentity, model);
		return model;
	}

	@Override
	public List<CoTriggersModel> findPendingTriggers(Integer totalBuckets,Integer currInstance) {
		// get All the pending triggers
		//List<CoTriggersEntity> triggers = coTriggersRepo.findByTriggerStatus("P",totalBuckets,currInstance);
		List<CoTriggersEntity> triggers = coTriggersDAO.findByTriggerStatus("P",totalBuckets,currInstance);

		List<CoTriggersModel> models = new ArrayList<>();
		triggers.forEach(trigger -> {
			CoTriggersModel model = new CoTriggersModel();
			BeanUtils.copyProperties(trigger, model);
			models.add(model);
		});
		return models;
	}

	@Override
	public CoBatchRunDetailsModel findByBatchRunSeq(Integer batchRunSeq) {

		// find by RunSeq no
		CoBatchRunDetailsEntity entity = coBatchRunDetailsRepo.findById(batchRunSeq).get();
		// convert entity to model
		CoBatchRunDetailsModel model = new CoBatchRunDetailsModel();
		BeanUtils.copyProperties(entity, model);
		return model;
	}

	@Override
	public void updateBatchRunDetails(CoBatchRunDetailsModel model) {

		// convert model to entity
		CoBatchRunDetailsEntity entity = new CoBatchRunDetailsEntity();
		BeanUtils.copyProperties(model, entity);

		// call repo method
		coBatchRunDetailsRepo.save(entity);
	}

	@Override
	public void insertBatchSummary(CoBatchSummaryModel bsModel) {

		// convert model into entity
		CoBatchSummaryEntity entity = new CoBatchSummaryEntity();
		BeanUtils.copyProperties(bsModel, entity);
		// call repo method
		entity = coBatchSummaryRepo.save(entity);
		if (entity != null) {

		}

	}

	@Override
	public CoPdfModel savePdf(CoPdfModel model) {

		// convert model into entity
		CoPdfEntity entity = new CoPdfEntity();
		BeanUtils.copyProperties(model, entity);
		// call repository method
		CoPdfEntity coEntity = coPdfRepository.save(entity);
		// convert entity to model
		BeanUtils.copyProperties(coEntity, model);
		return model;
	}

	@Override
	public boolean updatePendingTrigger(CoTriggersModel model) {

		// convert model to entity
		CoTriggersEntity entity = new CoTriggersEntity();
		BeanUtils.copyProperties(model, entity);

		CoTriggersEntity coEntity = coTriggersRepo.save(entity);
		if (coEntity != null) {
			return true;
		}
		return false;
	}
}
