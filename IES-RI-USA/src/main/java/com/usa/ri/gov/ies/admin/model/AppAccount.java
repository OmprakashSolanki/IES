package com.usa.ri.gov.ies.admin.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AppAccount {

private int accId;
	
	private String firstName;
	
	private String lastName;
	
	private String gender;

	private String phno;
	
	private String email;
	
	private String password;

	//@DateTimeFormat(pattern="MM-dd-yyyy")
	private String dob;
	
	private long ssn;

	private String activeSw;
	
	private Timestamp createDate;
	
	private Timestamp updateDate;

	private String role;

}
