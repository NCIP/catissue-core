
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.auth.domain.Ldap;

public class LdapDetails {

	private Long id;

	/**
	* eg. krishagni-ldap (UNIQUE)
	*/
	private String ldapName;

	/**
	*  eg. localhost, dap.testathon.net etc
	*/
	private String host;

	/**
	*  eg. 10389, 389 etc
	*/
	private String port;

	/**
	* eg. stuart, john etc
	*/
	private String loginName;

	/**
	* eg. OU=users,DC=testathon,DC=net
	*/
	private String directoryContext;

	/**
	* eg. stuart, john
	*/
	private String password;

	/**
	* eg. givenName
	*/
	private String givenNameField;

	/**
	* eg. sn
	*/
	private String surnameField;

	/**
	* eg. mail
	*/
	private String emailField;

	/**
	* eg. cn
	*/
	private String idField;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDirectoryContext() {
		return directoryContext;
	}

	public void setDirectoryContext(String directoryContext) {
		this.directoryContext = directoryContext;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getGivenNameField() {
		return givenNameField;
	}

	public void setGivenNameField(String givenNameField) {
		this.givenNameField = givenNameField;
	}

	public String getSurnameField() {
		return surnameField;
	}

	public void setSurnameField(String surnameField) {
		this.surnameField = surnameField;
	}

	public String getEmailField() {
		return emailField;
	}

	public void setEmailField(String emailField) {
		this.emailField = emailField;
	}

	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public String getLdapName() {
		return ldapName;
	}

	public void setLdapName(String ldapName) {
		this.ldapName = ldapName;
	}

	public static LdapDetails fromDomain(Ldap ldapSummary) {
		LdapDetails ldapDetails = new LdapDetails();
		ldapDetails.setId(ldapSummary.getId());
		ldapDetails.setDirectoryContext(ldapSummary.getDirectoryContext());
		ldapDetails.setLdapName(ldapSummary.getLdapName());
		ldapDetails.setHost(ldapSummary.getHost());
		ldapDetails.setPort(ldapSummary.getPort());
		ldapDetails.setLoginName(ldapSummary.getLoginName());
		ldapDetails.setPassword(ldapSummary.getPassword());

		ldapDetails.setGivenNameField(ldapDetails.getGivenNameField());
		ldapDetails.setIdField(ldapSummary.getIdField());
		ldapDetails.setSurnameField(ldapSummary.getSurnameField());
		ldapDetails.setEmailField(ldapSummary.getEmailField());
		return ldapDetails;
	}
}
