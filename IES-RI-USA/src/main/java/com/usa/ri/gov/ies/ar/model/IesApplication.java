package com.usa.ri.gov.ies.ar.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class IesApplication {
	
	private String appId;
	
	private String firstName;
	
	private String lastName;
	
	private String dob;
	
	private String gender;
	
	private Long ssn;
	private String phno;
	
	private String email;
	
	private Timestamp createDate;
	
	private Timestamp updateDate;
}
