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

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.List;

public class FilterTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {

	public FilterTestCases() {
		loginName = PropertiesLoader.getFilterScientistUsername();
		password = PropertiesLoader.getFilterScientistPassword();
	}

	public void testParticipants() {
		HQLCriteria hqlCriteria = new HQLCriteria(
				"from edu.wustl.catissuecore.domain.Participant as p where p.collectionProtocolRegistrationCollection.collectionProtocol.shortTitle in ('CP1, 'CP2', 'CP3')");
		List<Object> result = null;
		try {
			result = getApplicationService().query(hqlCriteria);

			int cp1Counter = 0, cp2Counter = 0;
			for (Object o : result) {
				Participant p = (Participant) o;
				CollectionProtocolRegistration cpr = p
						.getCollectionProtocolRegistrationCollection()
						.iterator().next();
				CollectionProtocol cp = cpr.getCollectionProtocol();
				if ("CP1".equals(cp.getShortTitle())) {
					cp1Counter++;
				} else if ("CP2".equals(cp.getShortTitle())
						&& (p.getFirstName() == null || p.getFirstName()
								.isEmpty())
						&& (p.getLastName() == null || p.getLastName()
								.isEmpty())
						&& (p.getMiddleName() == null || p.getMiddleName()
								.isEmpty())
						&& (p.getSocialSecurityNumber() == null || p
								.getSocialSecurityNumber().isEmpty())
						&& p.getBirthDate() == null && p.getDeathDate() == null) {
					cp2Counter++;
				}
			}

			if (result.size() > 20 && cp1Counter != 10 && cp2Counter != 10) {
				assertFalse(
						"Failed to retrieve Fuild Specimens for condition of id not null",
						true);
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse(
					"Failed to retrieve Fuild Specimens for condition of id not null",
					true);
		} catch (ClassCastException e) {
			e.printStackTrace();
			assertFalse(
					"Failed to retrieve Fuild Specimens for condition of id not null",
					true);
		}
	}
}
