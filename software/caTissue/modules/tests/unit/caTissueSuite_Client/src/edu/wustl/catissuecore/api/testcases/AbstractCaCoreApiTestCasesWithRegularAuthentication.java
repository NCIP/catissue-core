/**
 *
 */
package edu.wustl.catissuecore.api.testcases;

import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import gov.nih.nci.system.applicationservice.ApplicationException;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.List;

/**
 * @author ganesh_naikwade
 * @author Ion C. Olaru
 *
 */
public abstract class AbstractCaCoreApiTestCasesWithRegularAuthentication extends CaCoreApiTestBaseCases {

    protected static Logger log = Logger.getLogger(AbstractCaCoreApiTestCasesWithRegularAuthentication.class);

	public AbstractCaCoreApiTestCasesWithRegularAuthentication() {
		super();
	}

	public void setUp() {
        super.setUp();
		try {
            loginName = PropertiesLoader.getAdminUsername();
            password = PropertiesLoader.getAdminPassword();
            System.out.println(loginName);
            System.out.println(password);
            applicationService = getAppService(loginName, password);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
			System.exit(1);
		}
	}

    protected DistributionProtocol getDistributionProtocolById(Long id) throws ApplicationException {
        DistributionProtocol dp = new DistributionProtocol();
        dp.setId(id);
        List<DistributionProtocol> rs = searchByExample(DistributionProtocol.class, dp);
        DistributionProtocol result = null;
        if (rs != null && !rs.isEmpty()) {
            result = rs.get(0);
        }
        return result;
    }

}
