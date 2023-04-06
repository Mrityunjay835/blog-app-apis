package com.project.blog.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String requestToken =request.getHeader("Authorization");
		//token are in the form of Bearer 234234523sf
		String username= null;
		String token =null;
		
		if(requestToken!=null && requestToken.startsWith("Bearer")) {
			token = requestToken.substring(7);
			try {
			username = this.jwtTokenHelper.extractUsername(token);
			}catch(IllegalArgumentException e) {
				System.out.println("wrong token");
			}
			catch(ExpiredJwtException e) {
				System.out.println("token expired");
			}
			catch(MalformedJwtException e) {
				System.out.println("invalid jwt");
			}
		}
		else {
			System.out.println("token does't start with Bearer");
		}
//....................................................................................................
//                           validation
//....................................................................................................
		if(username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails =this.userDetailsService.loadUserByUsername(username);
			if(this.jwtTokenHelper.validateToken(token, userDetails)) {
				//now we perform authentication
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			}else {
				System.out.println("invalid jwt token");
			}
			
			
		}else {
			System.out.println("username is null or context is null");
		}
		
		filterChain.doFilter(request, response);
	}

}
