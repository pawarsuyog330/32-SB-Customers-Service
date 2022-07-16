package com.ashokit.customers.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashokit.customers.entity.CustomerEntity;
import com.ashokit.customers.model.CustomerDTO;
import com.ashokit.customers.model.LoginDTO;
import com.ashokit.customers.model.RegisterDTO;
import com.ashokit.customers.repository.CustomerRepository;
import com.ashokit.customers.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	CustomerRepository repository;

	@Override
	public boolean registerCustomer(RegisterDTO registerDto) {
		
		if(repository.existsById(registerDto.getPhoneNumber())==false) {
			CustomerEntity  customer=new CustomerEntity();
			BeanUtils.copyProperties(registerDto, customer);
			repository.save(customer);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean loginCustomer(LoginDTO loginDto) {
		
		if(repository.checkLogin(loginDto.getPhoneNumber(), loginDto.getPassword())==1) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public CustomerDTO readCustomer(Long phoneNumber) {
		
		CustomerEntity customer = repository.findById(phoneNumber).get();
		CustomerDTO  customerDto=new CustomerDTO();
		BeanUtils.copyProperties(customer, customerDto);
		return customerDto;
		
	}
	
	@Override
	public String fetchCustomerNameByPhoneNumber(Long phoneNumber) {
		return repository.findCustomerNameByPhoneNumber(phoneNumber);
	}

}
