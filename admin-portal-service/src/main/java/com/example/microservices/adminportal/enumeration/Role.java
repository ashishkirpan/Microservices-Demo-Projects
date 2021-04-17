package com.example.microservices.adminportal.enumeration;

import static com.example.microservices.adminportal.constant.Authority.*;

public enum Role {

	ROLE_USER(USER_AUTHORITIES), 
	ROLE_HR(HR_AUTHORITIES),
	ROLE_MANAGER(MANAGER_AUTHORITIES),
	ROLE_ADMIN(ADMIN_AUTHORITIES), 
	ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);

	private String[] authorities;

	private Role(String[] authorities) {
		this.authorities = authorities;
	}

	public String[] getAuthorities() {
		return authorities;
	}

}
