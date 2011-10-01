/**
 *
 */
package edu.wustl.catissuecore.api.testcases;

import edu.wustl.catissuecore.cacore.CaTissueWritableAppService;
import edu.wustl.common.test.BaseTestCase;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;
import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import java.util.List;

/**
 * @author Ion C. Olaru
 *
 */
public abstract class CaCoreApiTestBaseCases extends BaseTestCase {

    protected CaTissueWritableAppService applicationService;
    protected GlobusCredential gc;
    protected String loginName;
    protected String password;
    protected String dorianURL;
    protected String authenticationServiceURL;

    private static Logger log = Logger.getLogger(CaCoreApiTestBaseCases.class);

	public CaCoreApiTestBaseCases() {
		super();
	}

	public void setUp() {
        super.setUp();
        dorianURL = PropertiesLoader.getGridDorianUrl();
        authenticationServiceURL = PropertiesLoader.getGridAuthenticationServiceUrl();
	}

	final protected CaTissueWritableAppService getApplicationService() {
		return applicationService;
	}

	protected <T> T insert(T object) throws ApplicationException {
		SDKQuery query = new InsertExampleQuery(object);
		SDKQueryResult result = getApplicationService().executeQuery(query);
		return (T) result.getObjectResult();
	}

	protected <T> T update(T object) throws ApplicationException {
		SDKQuery query = new UpdateExampleQuery(object);
		SDKQueryResult result = getApplicationService().executeQuery(query);
		return (T) result.getObjectResult();
	}

	protected <T> List<T> searchByExample(Class<T> klass, T example) throws ApplicationException {
		return applicationService.search(klass, example);
	}

	protected <T> T searchById(Class<T> klass, T example) throws ApplicationException {
		List<T> result =  applicationService.search(klass, example);
        if (result != null && result.size() > 0) return result.get(0);
        else return null;
	}

/*
    final protected GlobusCredential getGlobusCredential() throws Exception{
        log.debug("username: " + loginName);
        log.debug("password: " + password);
        log.debug("Getting SAML from: " + authenticationServiceURL);
        log.debug("Getting GlobusCredential from: " + dorianURL);
        gc = GridAuthenticationClient.authenticate(dorianURL, authenticationServiceURL, loginName, password);
        return gc;
    }

*/
    final protected CaTissueWritableAppService getAppService(GlobusCredential gc) throws Exception {
        return (CaTissueWritableAppService)ApplicationServiceProvider.getApplicationService(gc);
    }

    final protected CaTissueWritableAppService getAppService(String username, String password) throws Exception {
        return (CaTissueWritableAppService)ApplicationServiceProvider.getApplicationService(username, password);
    }

}
