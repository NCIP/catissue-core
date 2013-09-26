/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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
