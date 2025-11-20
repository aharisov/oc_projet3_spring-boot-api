package com.openclassrooms.api.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
	public static String getCurrentUserEmail() {
		// get user email from Spring because it decodes JWT while applying security filters		
	    String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    
	    return email;
	}
}
