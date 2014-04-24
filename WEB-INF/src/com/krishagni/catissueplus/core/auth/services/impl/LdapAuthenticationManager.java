
package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import com.krishagni.catissueplus.core.auth.domain.Ldap;
import com.krishagni.catissueplus.core.auth.events.LoginDetails;

public class LdapAuthenticationManager {

	private static final String INITIAL_CONTEXT_FACTORY_VALUE = "com.sun.jndi.ldap.LdapCtxFactory";

	private static final String SIMPLE = "simple";

	public static void authenticateLdap(Ldap ldap) throws NamingException {
		Hashtable<Object, Object> env = getLdapEnvObj(ldap);
		DirContext ctx = new InitialDirContext(env);
		ctx.close();
	}

	public static void authenticate(LoginDetails loginDetails, Ldap ldap) throws NamingException {
		Hashtable<Object, Object> env = getLdapEnvObj(ldap);
		DirContext ctx = new InitialDirContext(env);
		SearchResult searchResult = searchUser(loginDetails.getLoginId(), ldap, new String[0], ctx);
		if (searchResult != null) {
			env.put(Context.SECURITY_PRINCIPAL, searchResult.getNameInNamespace());
			env.put(Context.SECURITY_CREDENTIALS, loginDetails.getPassword());
			new InitialLdapContext(env, null);
			ctx.close();
		}
	}

	private static SearchResult searchUser(final String userName, Ldap ldap, final String[] returnAttributes,
			DirContext ctx) throws NamingException {

		SearchResult searchResult = null;
		final SearchControls searchCtls = new SearchControls();
		searchCtls.setReturningAttributes(returnAttributes);
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		String searchBase = ldap.getSearchBaseDir();
		String searchFilter = ldap.getFilterString();
		final String replaceStr = "{0}";
		if (searchFilter.indexOf(replaceStr) != -1) {
			searchFilter = searchFilter.replace(replaceStr, userName);
		}

		final NamingEnumeration<SearchResult> result = ctx.search(searchBase, searchFilter, searchCtls);
		while (result.hasMoreElements()) {
			searchResult = result.next();
		}
		return searchResult;
	}

	private static Hashtable<Object, Object> getLdapEnvObj(Ldap ldap) {
		Hashtable<Object, Object> env = new Hashtable<Object, Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY_VALUE);
		env.put(Context.PROVIDER_URL, "ldap://" + ldap.getHost() + ":" + ldap.getPort());
		env.put(Context.SECURITY_AUTHENTICATION, SIMPLE);
		env.put(Context.SECURITY_PRINCIPAL, ldap.getIdField() + "=" + ldap.getBindUser() + "," + ldap.getDirectoryContext());
		env.put(Context.SECURITY_CREDENTIALS, ldap.getBindPassword());
		return env;
	}
}
