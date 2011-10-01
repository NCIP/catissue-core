/**
 *
 */
package edu.wustl.catissuecore.api.testcases;

import org.apache.log4j.Logger;

/**
 * @author Ion C. Olaru
 *
 */
public abstract class AbstractCaCoreApiTestCasesWithGridAuthentication extends CaCoreApiTestBaseCases {

    private static Logger log = Logger.getLogger(AbstractCaCoreApiTestCasesWithGridAuthentication.class);

	public AbstractCaCoreApiTestCasesWithGridAuthentication() {
		super();
	}

	public void setUp() {
        dorianURL = PropertiesLoader.getGridDorianUrl();
        authenticationServiceURL = PropertiesLoader.getGridAuthenticationServiceUrl();
        loginName = PropertiesLoader.getGridUsername();
        password = PropertiesLoader.getGridPassword();
	}

}
