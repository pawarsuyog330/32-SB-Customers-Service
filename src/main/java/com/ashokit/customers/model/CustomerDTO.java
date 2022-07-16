package com.ashokit.customers.model;

import java.util.List;

import lombok.Data;

@Data
public class CustomerDTO {
	private  Long  phoneNumber;
	private  String username;
	private  String email;
	private  String planId;
	private  PlanDTO  currentPlan;
	private  List<Long> friendsContactNumbers;
}
