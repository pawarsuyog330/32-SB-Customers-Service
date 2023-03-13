package com.ashokit.customers.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.ashokit.customers.model.PlanDTO;

@FeignClient(name = "PLANS")
public interface PlansProxy {
	
	@GetMapping("/api/plans/viewplans/{planId}")
	ResponseEntity<PlanDTO>  getPlans(@PathVariable String planId, @RequestHeader("myapp-tracing-id") String traceId);

}
