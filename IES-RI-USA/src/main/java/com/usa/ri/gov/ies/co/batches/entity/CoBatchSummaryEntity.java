package com.usa.ri.gov.ies.co.batches.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 
 * @author OPS
 *
 */

@Data
@Entity
@Table(name = "BATCH_SUMMARY")
public class CoBatchSummaryEntity {
	@Id()
	@GeneratedValue
	@Column(name = "SUMMARY_ID")
	private Integer summaryId;

	@Column(name = "BATCH_NAME")
	private String batchName;

	@Column(name = "TOTAL_TRIGGER_PROCESSED")
	private Long totalTriggerProcessed;

	@Column(name = "FAILURE_TRIGGER_COUNT")
	private Long failureTriggerCount;

	@Column(name = "SUCCESS_TRIGGER_COUNT")
	private Long successTriggerCount;

}// BatchSummary
