package com.usa.ri.gov.ies.ar.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.UpdateTimestamp;

import com.usa.ri.gov.ies.util.CustomSequenceIdGenerator;

import lombok.Data;

@Data
@Entity
@Table(name="IES_APPLICATION_MASTER")
public class IesApplicationEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_seq")
    @GenericGenerator(
        name = "app_seq", 
        strategy = "com.usa.ri.gov.ies.util.CustomSequenceIdGenerator", 
        parameters = {
            @Parameter(name = CustomSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = CustomSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "AR"),
            @Parameter(name = CustomSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d") })
	@Column(name="APP_ID")
	
	private String appId;
	
	@Column(name="FNAME")
	private String firstName;
	
	@Column(name="LNAME")
	private String lastName;
	
	@Column(name="DOB")
	private String dob;
	
	@Column(name="GENDER")
	private String gender;
	
	@Column(name="SSN")
	private Long ssn;
	
	@Column(name="PHNO")
	private String phno;
	
	@Column(name = "EMAIL", unique = true)
	private String email;
	
	@CreationTimestamp
	private Timestamp createDate;
	
	@UpdateTimestamp
	private Timestamp updateDate;
}
