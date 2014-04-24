
package com.krishagni.catissueplus.core.services.testdata;

import com.krishagni.catissueplus.core.auth.domain.AuthProvider;
import com.krishagni.catissueplus.core.auth.events.DomainDetails;
import com.krishagni.catissueplus.core.auth.events.LdapDetails;
import com.krishagni.catissueplus.core.auth.events.RegisterDomainEvent;

import edu.wustl.common.beans.SessionDataBean;

public class DomainRegistrationTestData {

	public final static String HOST = "host";

	public final static String PORT = "port";

	public final static String PASSWORD = "password";

	public final static String DIRECTORY_CONTEXT = "directory context";

	public final static String ID_FIELD = "id field";

	public final static String LOGIN_NAME = "login name";

	public final static String LDAP = "ldap";

	public static final Object SEARCH_BASE_DIR = "search base dir";

	public static final Object FILTER_STRING = "filter string";

	public static RegisterDomainEvent getRegisterDomainEventForLdap() {
		RegisterDomainEvent reqEvent = new RegisterDomainEvent(null);
		reqEvent.setSessionDataBean(getSessionDataBean());

		DomainDetails domainDetails = new DomainDetails();
		domainDetails.setName("MyLdap1");
		domainDetails.setAuthType("ldap");
		LdapDetails details = new LdapDetails();
		details.setHost("ldap.testathon.net");
		details.setPort(389l);
		details.setIdField("cn");
		details.setLdapName("Myldap");

		details.setDirectoryContext("OU=users,DC=testathon,DC=net");
		details.setBindUser("john");
		details.setBindPassword("john");

		details.setGivenNameField("givenName");
		details.setSurnameField("sn");
		details.setEmailField("mail");

		details.setSearchBaseDir("OU=users,DC=testathon,DC=net");
		details.setFilterString("(&(objectClass=*)(uid={0}))");
		domainDetails.setLdapDetails(details);
		reqEvent.setDomainDetails(domainDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventForCustom() {
		RegisterDomainEvent reqEvent = new RegisterDomainEvent(null);
		reqEvent.setSessionDataBean(getSessionDataBean());
		DomainDetails domainDetails = new DomainDetails();
		domainDetails.setName("MyCustom");
		domainDetails.setAuthType("custom");
		domainDetails.setImplClass("com.krishagni.catissueplus.core.auth.services.impl.AuthenticationServiceImpl");
		reqEvent.setDomainDetails(domainDetails);
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

	public static RegisterDomainEvent getRegisterDomainEventWithEmptyDomainName() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		dDetails.setName("");
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithEmptyHost() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		LdapDetails ldapDetails = dDetails.getLdapDetails();
		ldapDetails.setHost("");
		dDetails.setLdapDetails(ldapDetails);
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithNullPort() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		LdapDetails ldapDetails = dDetails.getLdapDetails();
		ldapDetails.setPort(null);
		dDetails.setLdapDetails(ldapDetails);
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithEmptyDirectoryContext() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		LdapDetails ldapDetails = dDetails.getLdapDetails();
		ldapDetails.setDirectoryContext("");
		dDetails.setLdapDetails(ldapDetails);
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithEmptyLoginName() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		LdapDetails ldapDetails = dDetails.getLdapDetails();
		ldapDetails.setBindUser("");
		dDetails.setLdapDetails(ldapDetails);
		reqEvent.setDomainDetails(dDetails);

		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithEmptyPassword() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		LdapDetails ldapDetails = dDetails.getLdapDetails();
		ldapDetails.setBindPassword("");
		dDetails.setLdapDetails(ldapDetails);
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithEmptyIdField() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		LdapDetails ldapDetails = dDetails.getLdapDetails();
		ldapDetails.setIdField("");
		dDetails.setLdapDetails(ldapDetails);
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithwrongLdapInfo() {
		RegisterDomainEvent reqEvent = new RegisterDomainEvent(null);
		reqEvent.setSessionDataBean(getSessionDataBean());
		DomainDetails dDetails = new DomainDetails();
		dDetails.setAuthType("ldap");
		dDetails.setName("myldap");

		LdapDetails details = new LdapDetails();
		details.setHost("ldap.testathon.net");
		details.setPort(389l);
		details.setIdField("cn");
		details.setLdapName("Myldap");

		details.setDirectoryContext("OU=users,DC=testathon,DC=net");
		details.setBindUser("john1");
		details.setBindPassword("john1");

		details.setGivenNameField("givenName");
		details.setSurnameField("sn");
		details.setEmailField("mail");

		details.setSearchBaseDir("dc=example,dc=com");
		details.setFilterString("(&(objectClass=*)(uid={0}))");
		dDetails.setLdapDetails(details);

		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithEmptySearchBaseDir() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		LdapDetails ldapDetails = dDetails.getLdapDetails();
		ldapDetails.setSearchBaseDir("");
		dDetails.setLdapDetails(ldapDetails);
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithEmptyFilterString() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		LdapDetails ldapDetails = dDetails.getLdapDetails();
		ldapDetails.setFilterString("");
		dDetails.setLdapDetails(ldapDetails);
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithEmptyDomain() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		dDetails.setName("");
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithNullLdapDetails() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		dDetails.setLdapDetails(null);
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static RegisterDomainEvent getRegisterDomainEventWithEmptyImplClass() {
		RegisterDomainEvent reqEvent = getRegisterDomainEventForLdap();
		DomainDetails dDetails = reqEvent.getDomainDetails();
		dDetails.setAuthType("custom");
		dDetails.setImplClass("");
		reqEvent.setDomainDetails(dDetails);
		return reqEvent;
	}

	public static AuthProvider getAuthProviderForLdap() {
		AuthProvider authProvider = new AuthProvider();
		authProvider.setAuthType("ldap");
		authProvider.setImplClass("com.krishagni.catissueplus.core.auth.services.impl.LdapAuthServiceImpl");
		return authProvider;
	}
}
