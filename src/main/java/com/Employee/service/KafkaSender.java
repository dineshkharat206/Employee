package com.Employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.Employee.model.Employee;


@Service
public class KafkaSender {
	
	@Autowired
	private KafkaTemplate<String, Employee> kafkaTemplate;
	
	String kafkaTopic = "EmployeeSecurity";
	
	public void send(Employee employee) {
	    
	    kafkaTemplate.send(kafkaTopic, employee);
	}
}
