
package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.krishagni.catissueplus.core.auth.domain.Ldap;

public class LdapAuthenticationManager {

	private static final String INITIAL_CONTEXT_FACTORY_VALUE = "com.sun.jndi.ldap.LdapCtxFactory";

	private static final String SIMPLE = "simple";

	public static void authenticateLdap(Ldap ldapSummay) throws NamingException {

		Hashtable<Object, Object> env = new Hashtable<Object, Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY_VALUE);
		env.put(Context.PROVIDER_URL, "ldap://" + ldapSummay.getHost() + ":" + ldapSummay.getPort());
		env.put(Context.SECURITY_AUTHENTICATION, SIMPLE);
		env.put(Context.SECURITY_PRINCIPAL,
				ldapSummay.getIdField() + "=" + ldapSummay.getLoginName() + "," + ldapSummay.getDirectoryContext());
		env.put(Context.SECURITY_CREDENTIALS, ldapSummay.getPassword());

		DirContext ctx = new InitialDirContext(env);
		ctx.close();
	}
}
