package com.ashokit.customers.model;

import lombok.Data;

@Data
public class RegisterDTO {
	
	private Long phoneNumber;
	private String userName;
	private String password;
	private String email;
	private String planId;
	
}
