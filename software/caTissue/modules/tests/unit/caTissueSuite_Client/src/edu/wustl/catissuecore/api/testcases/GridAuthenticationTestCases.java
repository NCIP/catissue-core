package edu.wustl.catissuecore.api.testcases;

import org.apache.log4j.Logger;

/**
 * @author Ion C. Olaru
 *
 * */
public class GridAuthenticationTestCases extends AbstractCaCoreApiTestCasesWithGridAuthentication {

    private static Logger log = Logger.getLogger(GridAuthenticationTestCases.class);

	public GridAuthenticationTestCases() {
	}

    public void testTempArithmetic() {
        assertEquals(2, 1 + 1);
    }

/*
    public void testGridAuthentication() {
        try {
            gc = getGlobusCredential();
            assertNotNull(gc);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
*/

/*
    public void testFail() {
        fail();
    }
*/

/*
    public void testApplicationServiceAuthenticationWithGlobusObject() throws Exception {
        gc = getGlobusCredential();
        applicationService = getAppService(gc);
        System.out.println(gc);
        System.out.println(gc.getIdentity());
        System.out.println(gc.getIdentityCertificate());
        assertNotNull(applicationService);
    }
*/

/*
    public void testOperationWithGlobus() throws Exception {
        gc = getGlobusCredential();
        applicationService = getAppService(gc);

        Participant participant = new Participant();
        participant.setLastName("Duck");

        SDKQuery sQuery = new SearchExampleQuery(participant);
        SDKQueryResult result = null;
        try {
            result = ((CaTissueWritableAppService) applicationService).executeQuery(sQuery);
        } catch (ApplicationException e) {
            e.printStackTrace();
            fail();
        }
        result.getObjectResult();
        System.out.println(result.getObjectResult());
    }
*/

}
