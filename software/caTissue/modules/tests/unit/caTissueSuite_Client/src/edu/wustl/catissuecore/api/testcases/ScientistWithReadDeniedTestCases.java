/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.api.testcases;

import gov.nih.nci.system.applicationservice.ApplicationException;

import java.util.List;

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
