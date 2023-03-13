package com.ashokit.customers.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ashokit.customers.feign.FriendsProxy;
import com.ashokit.customers.feign.PlansProxy;
import com.ashokit.customers.model.CustomerDTO;
import com.ashokit.customers.model.LoginDTO;
import com.ashokit.customers.model.PlanDTO;
import com.ashokit.customers.model.RegisterDTO;
import com.ashokit.customers.service.CustomerService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/customer")
@Slf4j
public class CustomerAPI {
	
	@Autowired
	PlansProxy pProxy;
	
	@Autowired
	FriendsProxy fProxy;
		
	@Autowired
	CustomerService  service;
	
	@Autowired
	RestTemplate restTemplate;

//	private static final String PLAN_URL = "http://localhost:1002/api/viewPlans/{planId}";
//	private static final String FRIEND_URL = "http://localhost:1003/api/{phoneNumber}";
	
	private static final String PLAN_URL = "http://PLANS/api/plans/{planId}";
	private static final String FRIEND_URL = "http://FRIENDS/api/friends/{phoneNumber}";
	
	
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
	//@CircuitBreaker(name = "customerCircuitBreaker", fallbackMethod = "showProfileFallback")
	@Retry(name="friends-retry", fallbackMethod = "showProfileFallback")
	public  CustomerDTO  showProfile(@PathVariable Long phoneNumber, @RequestHeader("myapp-tracing-id")String tracingId) {
		
		log.info("myapp-tracing-id is found in Customer-Service : {}", tracingId);
		log.info("showProfile method execution started");
		CustomerDTO  customerDto=service.readCustomer(phoneNumber);
	
//		calling plans microservice
//		PlanDTO planDto = restTemplate.getForObject(PLAN_URL, PlanDTO.class, customerDto.getPlanId());
//		customerDto.setCurrentPlan(planDto);
		
		/*
		RequestEntity<Void> requestEntity = RequestEntity.get(PLAN_URL, customerDto.getPlanId())
				  .accept(MediaType.APPLICATION_JSON).build();
		
		ResponseEntity<PlanDTO> re = restTemplate.exchange(requestEntity, PlanDTO.class);
		customerDto.setCurrentPlan(re.getBody());
		*/
		
		//Here, we are calling Plans-Service thru Feign client
		ResponseEntity<PlanDTO> re = pProxy.getPlans(customerDto.getPlanId(), tracingId);
		customerDto.setCurrentPlan(re.getBody());
		
		
//		calling friends microservice
//		List<Long> friendsContactNumbers = restTemplate.getForObject(FRIEND_URL, List.class, phoneNumber);
//		customerDto.setFriendsContactNumbers(friendsContactNumbers);
		
		/*
		RequestEntity<Void> requestEntity2 = RequestEntity.get(FRIEND_URL, phoneNumber)
				  .accept(MediaType.APPLICATION_JSON).build();
		
		ResponseEntity<List> re2 = restTemplate.exchange(requestEntity2, List.class);
		customerDto.setFriendsContactNumbers(re2.getBody());
		*/
		
		//Here, we are calling Friends-Service thru Feign client
		ResponseEntity<List<Long>> re2= fProxy.getFriendsContacts(phoneNumber, tracingId);
		customerDto.setFriendsContactNumbers(re2.getBody());
		log.info("showProfile method execution completed");
		return customerDto;
				
	}

	public  CustomerDTO  showProfileFallback(@PathVariable Long phoneNumber, @RequestHeader("myapp-tracing-id")String tracingId, Throwable t) {
		CustomerDTO  customerDto=service.readCustomer(phoneNumber);
		ResponseEntity<PlanDTO> re = pProxy.getPlans(customerDto.getPlanId(), tracingId);
		customerDto.setCurrentPlan(re.getBody());
		customerDto.setFriendsContactNumbers(new ArrayList<Long>());
		return customerDto;
	}

}
