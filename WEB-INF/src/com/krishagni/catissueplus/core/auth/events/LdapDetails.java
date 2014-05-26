
package com.krishagni.catissueplus.core.auth.events;

import com.krishagni.catissueplus.core.auth.domain.Ldap;

public class LdapDetails {

	private Long domainId;

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
	private Long port;

	/**
	* eg. stuart, john etc
	*/
	private String bindUser;

	/**
	* eg. OU=users,DC=testathon,DC=net
	*/
	private String directoryContext;

	/**
	* eg. OU=users,DC=testathon,DC=net
	*/
	private String searchBaseDir;

	/**
	* eg. (&(objectClass=*)(uid={0}))
	*/
	private String filterString;

	/**
	* eg. stuart, john
	*/
	private String bindPassword;

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

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}

	public String getDirectoryContext() {
		return directoryContext;
	}

	public void setDirectoryContext(String directoryContext) {
		this.directoryContext = directoryContext;
	}

	public String getBindUser() {
		return bindUser;
	}

	public void setBindUser(String bindUser) {
		this.bindUser = bindUser;
	}

	public String getBindPassword() {
		return bindPassword;
	}

	public void setBindPassword(String bindPassword) {
		this.bindPassword = bindPassword;
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

	public String getSearchBaseDir() {
		return searchBaseDir;
	}

	public void setSearchBaseDir(String searchBaseDir) {
		this.searchBaseDir = searchBaseDir;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public static LdapDetails fromDomain(Ldap ldap) {
		LdapDetails ldapDetails = new LdapDetails();
		ldapDetails.setDomainId(ldap.getDomainId());
		ldapDetails.setDirectoryContext(ldap.getDirectoryContext());
		ldapDetails.setHost(ldap.getHost());
		ldapDetails.setPort(ldap.getPort());
		ldapDetails.setBindUser(ldap.getBindUser());
		ldapDetails.setBindPassword(ldap.getBindPassword());

		ldapDetails.setGivenNameField(ldap.getGivenNameField());
		ldapDetails.setIdField(ldap.getIdField());
		ldapDetails.setSurnameField(ldap.getSurnameField());
		ldapDetails.setEmailField(ldap.getEmailField());
		ldapDetails.setSearchBaseDir(ldap.getSearchBaseDir());
		ldapDetails.setFilterString(ldap.getFilterString());
		return ldapDetails;
	}
}
