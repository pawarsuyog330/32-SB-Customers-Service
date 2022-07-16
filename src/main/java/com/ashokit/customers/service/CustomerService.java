package com.ashokit.customers.service;

import com.ashokit.customers.model.CustomerDTO;
import com.ashokit.customers.model.LoginDTO;
import com.ashokit.customers.model.RegisterDTO;


public interface CustomerService {
	
	boolean  registerCustomer(RegisterDTO  registerDto);
	
	boolean  loginCustomer(LoginDTO  loginDto);
	
	CustomerDTO  readCustomer(Long phoneNumber);
	
	String fetchCustomerNameByPhoneNumber(Long phoneNumber);
}
