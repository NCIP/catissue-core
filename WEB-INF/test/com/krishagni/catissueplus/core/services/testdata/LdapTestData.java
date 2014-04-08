
package com.krishagni.catissueplus.core.services.testdata;

import com.krishagni.catissueplus.core.auth.events.AddLdapEvent;
import com.krishagni.catissueplus.core.auth.events.LdapDetails;

import edu.wustl.common.beans.SessionDataBean;

public class LdapTestData {

	public final static String HOST = "host";

	public final static String PORT = "port";

	public final static String LOGIN_NAME = "login name";

	public final static String PASSWORD = "password";

	public final static String DIRECTORY_CONTEXT = "directory context";

	public final static String ID_FIELD = "id field";

	public final static String LDAP_NAME = "ldap name";

	public final static String LDAP = "ldap";

	public static final Object SEARCH_BASE_DIR = "search base dir";

	public static final Object FILTER_STRING = "filter string";

	public static AddLdapEvent getAddLdapEvent() {
		AddLdapEvent reqEvent = new AddLdapEvent(null);
		reqEvent.setSessionDataBean(getSessionDataBean());
		LdapDetails details = new LdapDetails();
		details.setHost("ldap.testathon.net");
		details.setPort(389l);
		details.setIdField("cn");
		details.setLdapName("Myldap");

		details.setDirectoryContext("OU=users,DC=testathon,DC=net");
		details.setLoginName("john");
		details.setPassword("john");

		details.setGivenNameField("givenName");
		details.setSurnameField("sn");
		details.setEmailField("mail");
		
		details.setSearchBaseDir("OU=users,DC=testathon,DC=net");
		details.setFilterString("(&(objectClass=*)(uid={0}))");
		reqEvent.setLdapDetails(details);
		return reqEvent;
	}

	private static SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}

	public static AddLdapEvent getAddLdapEventWithEmptyLdapName() {
		AddLdapEvent reqEvent = getAddLdapEvent();
		LdapDetails ldapDetails = reqEvent.getLdapDetails();
		ldapDetails.setLdapName("");
		reqEvent.setLdapDetails(ldapDetails);
		return reqEvent;
	}

	public static AddLdapEvent getAddLdapEventWithEmptyHost() {
		AddLdapEvent reqEvent = getAddLdapEvent();
		LdapDetails ldapDetails = reqEvent.getLdapDetails();
		ldapDetails.setHost("");
		reqEvent.setLdapDetails(ldapDetails);
		return reqEvent;
	}

	public static AddLdapEvent getAddLdapEventWithNullPort() {
		AddLdapEvent reqEvent = getAddLdapEvent();
		LdapDetails ldapDetails = reqEvent.getLdapDetails();
		ldapDetails.setPort(null);
		reqEvent.setLdapDetails(ldapDetails);
		return reqEvent;
	}

	public static AddLdapEvent getAddLdapEventWithEmptyDirectoryContext() {
		AddLdapEvent reqEvent = getAddLdapEvent();
		LdapDetails ldapDetails = reqEvent.getLdapDetails();
		ldapDetails.setDirectoryContext("");
		reqEvent.setLdapDetails(ldapDetails);
		return reqEvent;
	}

	public static AddLdapEvent getAddLdapEventWithEmptyLoginName() {
		AddLdapEvent reqEvent = getAddLdapEvent();
		LdapDetails ldapDetails = reqEvent.getLdapDetails();
		ldapDetails.setLoginName("");
		reqEvent.setLdapDetails(ldapDetails);
		return reqEvent;
	}

	public static AddLdapEvent getAddLdapEventWithEmptyPassword() {
		AddLdapEvent reqEvent = getAddLdapEvent();
		LdapDetails ldapDetails = reqEvent.getLdapDetails();
		ldapDetails.setPassword("");
		reqEvent.setLdapDetails(ldapDetails);
		return reqEvent;
	}

	public static AddLdapEvent getAddLdapEventWithEmptyIdField() {
		AddLdapEvent reqEvent = getAddLdapEvent();
		LdapDetails ldapDetails = reqEvent.getLdapDetails();
		ldapDetails.setIdField("");
		reqEvent.setLdapDetails(ldapDetails);
		return reqEvent;
	}

	public static AddLdapEvent getAddLdapEventWithwrongLdapInfo() {
		AddLdapEvent reqEvent = new AddLdapEvent(null);
		reqEvent.setSessionDataBean(getSessionDataBean());
		LdapDetails details = new LdapDetails();
		details.setHost("ldap.testathon.net");
		details.setPort(389l);
		details.setIdField("cn");
		details.setLdapName("Myldap");

		details.setDirectoryContext("OU=users,DC=testathon,DC=net");
		details.setLoginName("john1");
		details.setPassword("john1");

		details.setGivenNameField("givenName");
		details.setSurnameField("sn");
		details.setEmailField("mail");
		
		details.setSearchBaseDir("dc=example,dc=com");
		details.setFilterString("(&(objectClass=*)(uid={0}))");		
		reqEvent.setLdapDetails(details);
		return reqEvent;
	}

	public static AddLdapEvent getAddLdapEventWithEmptySearchBaseDir() {
		AddLdapEvent reqEvent = getAddLdapEvent();
		LdapDetails ldapDetails = reqEvent.getLdapDetails();
		ldapDetails.setSearchBaseDir("");
		reqEvent.setLdapDetails(ldapDetails);
		return reqEvent;
	}

	public static AddLdapEvent getAddLdapEventWithEmptyFilterString() {
		AddLdapEvent reqEvent = getAddLdapEvent();
		LdapDetails ldapDetails = reqEvent.getLdapDetails();
		ldapDetails.setFilterString("");
		reqEvent.setLdapDetails(ldapDetails);
		return reqEvent;
	}
}
