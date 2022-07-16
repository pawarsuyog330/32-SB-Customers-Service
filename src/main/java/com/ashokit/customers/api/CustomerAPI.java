package com.ashokit.customers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ashokit.customers.model.CustomerDTO;
import com.ashokit.customers.model.LoginDTO;
import com.ashokit.customers.model.PlanDTO;
import com.ashokit.customers.model.RegisterDTO;
import com.ashokit.customers.service.CustomerService;


@RestController
@RequestMapping("/api")
public class CustomerAPI {
		
	@Autowired
	CustomerService  service;
	
	@Autowired
	RestTemplate restTemplate;

	private static final String PLAN_URL = "http://localhost:1002/api/viewPlans/{planId}";
	private static final String FRIEND_URL = "http://localhost:1003/api/{phoneNumber}";
	
	@PostMapping("/register")
	public boolean  addCustomer(@RequestBody RegisterDTO  registerDto) {
		return service.registerCustomer(registerDto);
	}
	
	@PostMapping("/login")
	public  boolean  loginCustomer(@RequestBody  LoginDTO loginDto) {
		return  service.loginCustomer(loginDto);
	}
	
	@GetMapping("/customername/{phoneNumber}")
	public String getCustomerNameByPhoneNumber(@PathVariable Long phoneNumber) {
		return service.fetchCustomerNameByPhoneNumber(phoneNumber);
	}
	
	@GetMapping("/viewProfile/{phoneNumber}")
	public  CustomerDTO  showProfile(@PathVariable Long phoneNumber) {
		
		CustomerDTO  customerDto=service.readCustomer(phoneNumber);
	
//		calling plans microservice
//		PlanDTO planDto = restTemplate.getForObject(PLAN_URL, PlanDTO.class, customerDto.getPlanId());
//		customerDto.setCurrentPlan(planDto);
		
		RequestEntity<Void> requestEntity = RequestEntity.get(PLAN_URL, customerDto.getPlanId())
				  .accept(MediaType.APPLICATION_JSON).build();
		
		ResponseEntity<PlanDTO> re = restTemplate.exchange(requestEntity, PlanDTO.class);
		customerDto.setCurrentPlan(re.getBody());
		
		
		
//		calling friends microservice
//		List<Long> friendsContactNumbers = restTemplate.getForObject(FRIEND_URL, List.class, phoneNumber);
//		customerDto.setFriendsContactNumbers(friendsContactNumbers);
		
		RequestEntity<Void> requestEntity2 = RequestEntity.get(FRIEND_URL, phoneNumber)
				  .accept(MediaType.APPLICATION_JSON).build();
		ResponseEntity<List> re2 = restTemplate.exchange(requestEntity2, List.class);
		customerDto.setFriendsContactNumbers(re2.getBody());
		return customerDto;
				
	}
	
	
	
	
	
	
}
