package edu.wustl.catissuecore.api.testcases;

import java.util.List;

import edu.wustl.catissuecore.domain.TissueSpecimen;

import gov.nih.nci.system.applicationservice.ApplicationException;

public class ScientistWithReadAccessTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {

	public ScientistWithReadAccessTestCases() {
		loginName = PropertiesLoader.getScientistReadUsername();
		password = PropertiesLoader.getScientistReadPassword();
	}

	public void testTissueSpecimenReadAccess() {
		List<Object> result = null;
		try {
			result = getApplicationService().query(
					CqlUtility.getTissueSpecimensForCP(PropertiesLoader
							.getCPTitleForScientistReadForTissueSpecimen()));

			for (Object o : result) {
				TissueSpecimen t = (TissueSpecimen) o;
				if (t.getCreatedOn() != null) {
					assertFalse(
							"Failed to retrieve Tissue Specimens having masked data",
							true);
				}
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Not able to retrieve Fuild Specimen list using API",
					true);
		}
	}
}
