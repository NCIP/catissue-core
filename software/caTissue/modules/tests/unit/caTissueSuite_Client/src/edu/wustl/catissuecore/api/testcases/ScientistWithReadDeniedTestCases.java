package edu.wustl.catissuecore.api.testcases;

import java.util.List;

import gov.nih.nci.system.applicationservice.ApplicationException;

public class ScientistWithReadDeniedTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {

	public ScientistWithReadDeniedTestCases() {
		loginName = PropertiesLoader.getScientistReadDeniedUsername();
		password = PropertiesLoader.getScientistReadDeniedPassword();
	}

	public void testParticipantsReadDenied() {
		List<Object> result = null;
		try {
			result = getApplicationService().query(
					CqlUtility.getParticipantsForCP(PropertiesLoader
							.getCPTitleForScientistReadDeniedForParticipant()));
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Not able to retrieve Fuild Specimen list using API",
					true);
		}

		assertTrue("Participant list is retrieved using API", result == null
				|| result.size() == 0);
	}

	public void testFluidSpecimenReadDeniedForCP() {
		List<Object> result = null;
		try {
			result = getApplicationService()
					.query(
							CqlUtility
									.getFluidSpecimensWithReviewEventRecordForCP(PropertiesLoader
											.getCPTitleForScientistReadDeniedForFluidSpecimen()));
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Not able to retrieve Fuild Specimen list using API",
					true);
		}

		assertTrue("Participant list is retrieved using API", result == null
				|| result.size() == 0);
	}

}
