package com.ashokit.customers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ashokit.customers.entity.CustomerEntity;

@Repository
public interface CustomerRepository  extends JpaRepository<CustomerEntity,Long> {
	
	@Query(value="select  count(*) from  customers_details  where  phone_number=?  and  password=?", nativeQuery=true)
	Integer  checkLogin(Long phoneNumber, String password);
	
	@Query(value="select user_name from customers_details where phone_number=?", nativeQuery=true)
	String findCustomerNameByPhoneNumber(Long phoneNumber);

}
