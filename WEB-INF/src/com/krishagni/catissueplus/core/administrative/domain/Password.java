
package com.krishagni.catissueplus.core.administrative.domain;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;

public class Password {

	private Long id;

	private String password;

	private Date updateDate;

	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (isBlank(password) && !isValidPasswordPattern(password)) {
			reportError(UserErrorCode.INVALID_ATTR_VALUE, PASSWORD);
		}
		this.password = password;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	private final static String PASSWORD = "password";

	private final static String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";

	public static boolean isValidPasswordPattern(String password) {
		boolean result = false;
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher mat = pattern.matcher(password);
		result = mat.matches();
		return result;
	}
}
