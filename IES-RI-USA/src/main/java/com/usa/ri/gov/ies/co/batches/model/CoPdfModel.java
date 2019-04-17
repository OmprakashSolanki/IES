/**
 * 
 */
package com.usa.ri.gov.ies.co.batches.model;

import lombok.Data;

/**
 * @author ops
 *
 */
@Data
public class CoPdfModel {
	Integer coPdfId;
	Integer caseNumber;
	byte[] pdfDocument;
	String planName;
	String PlanStatus;

}// CoPdfModel
