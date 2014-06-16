
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.auth.domain.factory.AuthenticationType;

public class UserInfo {

	private String loginName;

	private String domainName = AuthenticationType.CATISSUE.value();

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

}
