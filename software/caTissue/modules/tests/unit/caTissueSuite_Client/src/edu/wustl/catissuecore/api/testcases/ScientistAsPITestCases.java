package edu.wustl.catissuecore.api.testcases;

import java.util.List;

import edu.wustl.catissuecore.domain.Participant;

import gov.nih.nci.system.applicationservice.ApplicationException;

public class ScientistAsPITestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {

	public ScientistAsPITestCases() {
		loginName = PropertiesLoader.getPIScientistUsername();
		password = PropertiesLoader.getPIScientistPassword();
	}

	public void testGetParticipantForCPAsPI() {
		List<Object> result = null;
		try {
			result = getApplicationService().query(CqlUtility.getParticipantsForCP(PropertiesLoader.getCPTitleForPIScientistForParticipant()));
			for (Object o : result) {
				Participant p = (Participant) o;
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Failed to retrieve Participants", true);
		} catch (ClassCastException e) {
			e.printStackTrace();
			assertFalse("Failed to retrieve Participants", true);
		}

		assertTrue("Participants are retrieved for the given CP", result != null && result.size() > 7);
	}
}
