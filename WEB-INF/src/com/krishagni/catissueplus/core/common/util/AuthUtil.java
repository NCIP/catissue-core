package com.krishagni.catissueplus.core.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class AuthUtil {
	public static Authentication getAuth() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public static User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return null;
		}
		
		return (User)auth.getPrincipal();
	}
	
	public static String getRemoteAddr() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return null;
		}
		
		Object obj = auth.getDetails();
		if (obj instanceof WebAuthenticationDetails) {
			WebAuthenticationDetails details = (WebAuthenticationDetails)obj;
			return details.getRemoteAddress();
		}
		
		return null;
	}
	
	public static boolean isAdmin() {
		return getCurrentUser().isAdmin();
	}
	
	public static String encodeToken(String token) {
		return new String(Base64.encode(token.getBytes()));
	}
	
	public static String decodeToken(String token) {
		return new String(Base64.decode(token.getBytes()));
	}
}