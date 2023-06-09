package com.project.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.blog.payloads.JWTAuthRequest;
import com.project.blog.payloads.JWTAuthResponse;
import com.project.blog.security.JWTTokenHelper;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
	
	@Autowired
	private JWTTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	

	@PostMapping("/login")
	public ResponseEntity<JWTAuthResponse> createToken(@RequestBody JWTAuthRequest jwtAuthRequest) {
		
		this.authenticate(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword());
UserDetails userDetails =this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername());
		
		String token =this.jwtTokenHelper.generateToken(userDetails);
		
		JWTAuthResponse response  = new JWTAuthResponse();
		response.setToken(token);
		
		return new ResponseEntity<JWTAuthResponse>(response, HttpStatus.OK);
	}
	
	private void authenticate(String username,String password) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(username, password);
		this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		
	}
	
}
