package com.ashokit.customers.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "FRIENDS")
public interface FriendsProxy {
	@GetMapping("/api/friends/{phoneNumber}")
	ResponseEntity<List<Long>> getFriendsContacts(@PathVariable Long phoneNumber, @RequestHeader("myapp-tracing-id")String traceId);

}
