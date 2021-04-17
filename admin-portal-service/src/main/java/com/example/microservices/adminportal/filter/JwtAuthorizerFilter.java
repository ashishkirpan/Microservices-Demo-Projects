package com.example.microservices.adminportal.filter;

import static com.example.microservices.adminportal.constant.SecurityConstant.OPTIONS_METHOD_HTTP;
import static com.example.microservices.adminportal.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.microservices.adminportal.utility.JWTTokenProvider;

@Component
public class JwtAuthorizerFilter extends OncePerRequestFilter {

	private JWTTokenProvider jwtTokenProvider;
	
	public JwtAuthorizerFilter(JWTTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(request.getMethod().equalsIgnoreCase(OPTIONS_METHOD_HTTP)) {
			response.setStatus(HttpStatus.OK.value());
		} else {
			
			String authorizationHeader = request.getHeader(AUTHORIZATION);
			
			if(authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}
			
			String token = authorizationHeader.substring(TOKEN_PREFIX.length());
			String username = jwtTokenProvider.getSubject(token);
			if(jwtTokenProvider.isTokenValid(username, token)) {
				List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
				Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				SecurityContextHolder.clearContext();
			}			
		}
		
		filterChain.doFilter(request, response);
	}

	
}
