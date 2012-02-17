package edu.wustl.catissuecore.api.testcases;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import gov.nih.nci.system.applicationservice.ApplicationException;

public class ScientistAsNonPIorPCTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {

	public ScientistAsNonPIorPCTestCases() {
		loginName = PropertiesLoader.getNonPIPCScientistUsername();
		password = PropertiesLoader.getNonPIPCScientistPassword();
	}

	public void testBulkFluidSpecimen() {
		DetachedCriteria criteria = DetachedCriteria.forClass(FluidSpecimen.class);
		criteria.add(Property.forName("id").isNotNull());

		List<Object> result = null;
		try {
			result = getApplicationService().query(criteria);

			boolean failed = false;
			for (Object o : result) {
				if (!(o instanceof FluidSpecimen)) {
					failed = true;
					break;
				}
			}

			if (failed || result.size() > 2697) {
				assertFalse(
						"Failed to retrieve Fuild Specimens for condition of id not null",
						true);
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse(
					"Failed to retrieve Fuild Specimens for condition of id not null",
					true);
		}
	}

	public void testGetParticipantForCP() {
		List<Object> result = null;
		try {
			result = getApplicationService().query(
					CqlUtility.getParticipantsForCP(PropertiesLoader
							.getCPTitleOfParticipantForNonPIPCScientist()));

			for (Object o : result) {
				Participant p = (Participant) o;
				if ((p.getFirstName() == null || p.getFirstName().isEmpty())
						&& (p.getLastName() == null || p.getLastName()
								.isEmpty())
						&& (p.getMiddleName() == null || p.getMiddleName()
								.isEmpty())
						&& (p.getSocialSecurityNumber() == null || p
								.getSocialSecurityNumber().isEmpty())
						&& p.getBirthDate() == null && p.getDeathDate() == null) {
				} else {
					assertFalse(
							"Failed to filter the PHI data of Participants",
							true);
					break;
				}
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Failed to retrieve Participants", true);
		} catch (ClassCastException e) {
			assertFalse("Failed to retrieve Participants", true);
		}
	}

	public void testSpecimenMalePPI() {
		List<Object> result = null;
		String ppi = PropertiesLoader.getPPIForSpecimen();
		try {
			result = getApplicationService().query(
					CqlUtility.getSpecimenMalePPI(ppi));

			for (Object o : result) {
				if (o instanceof Specimen) {
					Specimen specimen = (Specimen) o;
					SpecimenCollectionGroup scg = specimen
							.getSpecimenCollectionGroup();
					CollectionProtocolRegistration cpr = scg
							.getCollectionProtocolRegistration();
					if (!ppi.equals(cpr.getProtocolParticipantIdentifier())
							|| specimen.getCreatedOn() != null) {
						assertFalse(
								"Failed to retrieve Specimens of males with CPR having PPI value as "
										+ ppi + " having masked PHI data.",
								true);
						break;
					}
				}
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse(
					"Failed to retrieve Specimens of males with CPR having PPI value as "
							+ ppi + " having masked PHI data.", true);
		}
	}
}
