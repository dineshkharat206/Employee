package com.Employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Employee.Util.JwtUtil;
import com.Employee.model.AuthenticationRequest;
import com.Employee.model.AuthenticationResponse;
import com.Employee.model.Employee;
import com.Employee.service.KafkaSender;
import com.Employee.service.MyUserDetailsService;


@RestController
//@RequestMapping(value = "/javainuse-kafka/")
public class ApacheKafkaWebController {

	@Autowired
	KafkaSender kafkaSender;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;

	@PostMapping(value = "/producer")
	public String producer(@RequestParam("id") int id, @RequestParam("name") String name, @RequestParam("city") String city) {
		Employee e = new Employee();
		e.setId(id);
		e.setFirstname(name);
		e.setCity(city);
		kafkaSender.send(e);

		return "Message sent to the Kafka Topic java_in_use_topic Successfully";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthentictionToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		}catch (BadCredentialsException e) {
			throw new Exception("Incorrect Username or password", e);
			
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}
