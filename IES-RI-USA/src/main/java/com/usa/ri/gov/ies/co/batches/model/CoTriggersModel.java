package com.usa.ri.gov.ies.co.batches.model;

import java.util.Date;

import lombok.Data;

@Data
public class CoTriggersModel {

	private Integer triggerId;

	private Integer caseNum;

	private String triggerStatus;

	private Date createdDate;

	private Date updatedDate;

}
