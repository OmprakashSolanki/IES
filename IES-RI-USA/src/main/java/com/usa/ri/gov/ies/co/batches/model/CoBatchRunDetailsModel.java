/**
 * 
 */
package com.usa.ri.gov.ies.co.batches.model;

import java.util.Date;

import lombok.Data;

/**
 * @author OPS
 *
 */

@Data()
public class CoBatchRunDetailsModel {
	private Integer runSeq;

	
	private String batchName;

	private Date startDate;

	private Date endTime;

	private String batchRunStatus;
	
	private Integer runInstance;

}// CoBatchRunDetailsEntity
