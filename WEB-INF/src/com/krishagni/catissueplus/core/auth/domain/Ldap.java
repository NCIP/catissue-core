
package com.krishagni.catissueplus.core.auth.domain;

public class Ldap {

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
	private Long port;

	/**
	* eg. stuart
	*/
	private String loginName;

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
	* eg. stuart
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

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
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

}
