package com.usa.ri.gov.ies.dc.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class DcCaseChildDetails{
	
	private Integer childId;

	private Integer caseId;
	
	private String indivName;
	
	private String childName;

	private String childGender;
	
	//@DateTimeFormat(pattern="dd/MM/yyyy")
	private String childDob;
	
	private Long childSSN;

}
