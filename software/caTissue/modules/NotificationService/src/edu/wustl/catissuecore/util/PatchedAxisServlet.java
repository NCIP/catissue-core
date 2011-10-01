/**
 * 
 */
package edu.wustl.catissuecore.util;

import javax.servlet.ServletException;

/**
 * @author Denis G. Krylov
 * 
 */
public final class PatchedAxisServlet extends
		org.globus.wsrf.container.AxisServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
		fix();
	}

	private void fix() {
		System.setProperty("java.naming.factory.initial",
				"org.jnp.interfaces.NamingContextFactory");
		System.setProperty("java.naming.factory.url.pkgs",
				"org.jboss.naming:org.jnp.interfaces:org.jboss.naming:org.jnp.interfaces");

	}

}
