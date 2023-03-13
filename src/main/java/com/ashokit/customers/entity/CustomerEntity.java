package com.ashokit.customers.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table( name = "CUSTOMER_DETAILS" )
@Data
public class CustomerEntity {
	@Id
	private  Long  phoneNumber;
	
	@Column(length=20)
	private  String  userName;
	
	@Column(length=20)
	private  String  password;
	
	@Column(length=30)
	private  String  email;
	
	@Column(length=15)
	private  String  planId;

	
}
