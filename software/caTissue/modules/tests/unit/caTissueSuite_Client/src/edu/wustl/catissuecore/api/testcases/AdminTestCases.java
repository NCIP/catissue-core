package edu.wustl.catissuecore.api.testcases;

import java.text.ParseException;
import java.util.*;

import edu.wustl.catissuecore.domain.*;
import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.SearchExampleQuery;
import org.apache.commons.lang.StringUtils;

import edu.wustl.common.lookup.DefaultLookupResult;
import gov.nih.nci.system.applicationservice.ApplicationException;
import edu.common.dynamicextensions.domain.integration.AbstractRecordEntry;
import org.springframework.web.servlet.theme.FixedThemeResolver;

/**
 * @author Ion C. Olaru
 * */
public class AdminTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {

	public AdminTestCases() {
		super();
	}

    /**
     * Creates a new CollectionProtocolRegistration for a new Participant and saves the CPR.
     * */
/*
    public void testAddCPRAndNewParticipant() throws ApplicationException {
        Participant p = BaseTestCaseUtility.initParticipant();
        CollectionProtocol cp = getCollectionProtocolByShortTitle(PropertiesLoader.getCPShortTitleForAddParticipantWithCPR());
        CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
        cpr.setCollectionProtocol(cp);
        cpr.setProtocolParticipantIdentifier("PPI" + UniqueKeyGeneratorUtil.getUniqueKey());
        cpr.setActivityStatus("Active");
        cpr.setParticipant(p);
        Object o = insert(cpr);
        assertNotNull(o);
    }
*/

/*
    public void testAddParticipantNewCP() throws ApplicationException {
        Participant participant = BaseTestCaseUtility.initParticipant();
        CollectionProtocol cp = new CollectionProtocol();
        cp.setShortTitle("Test Collection Protocol");
        cp.setTitle("Test Collection Protocol");
        cp.setStartDate(new Date());
        cp.setPrincipalInvestigator(new User());
        cp = insert(cp);
        System.out.println(cp.getId());
        CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
        List<CollectionProtocolRegistration> cprc = new ArrayList<CollectionProtocolRegistration>();
        cprc.add(cpr);
        cpr.setCollectionProtocol(cp);
        cpr.setParticipant(participant);
        participant.setCollectionProtocolRegistrationCollection(cprc);
        participant = insert(participant);
        assertNotNull(participant.getId());
    }

*/
    public void testAddInstitution() throws ApplicationException {
        Institution i = new Institution();
        i.setName("Some Inst. Name - 01" + UniqueKeyGeneratorUtil.getUniqueKey());
        assertNull(i.getId());
        i = insert(i);
        assertNotNull(i);
        assertTrue(i.getId() > 0);
    }

    public void testAddParticipantAlone() throws ApplicationException {
        Participant p = new Participant();
        p.setLastName("Malkovich");
        p.setFirstName("John");
        p.setActivityStatus("Active");
        p = insert(p);
        assertNotNull(p.getId());
        assertTrue(p.getId() > 0);
    }

	public void testAddParticipantWithCPR() {
		try {
			Participant participant = BaseTestCaseUtility.initParticipant();
			CollectionProtocol cp = getCollectionProtocolByShortTitle(PropertiesLoader.getCPShortTitleForAddParticipantWithCPR());
			User user = getUserByLoginName(PropertiesLoader.getAdminUsername());

			CollectionProtocolRegistration cpr = BaseTestCaseUtility.initCollectionProtocolRegistration(participant, cp, user);
			Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection = new HashSet<CollectionProtocolRegistration>();
			collectionProtocolRegistrationCollection.add(cpr);
			participant.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);

			participant = insert(participant);
			assertTrue("Participant inserted successfully." + participant.getId(), true);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to insert Participant.", true);
		}
	}

	public void testAddParticipantWithMedicalIdentifiers() {
		try {
			Participant p = BaseTestCaseUtility.initParticipant();
            Collection<ParticipantMedicalIdentifier> pmis = new ArrayList<ParticipantMedicalIdentifier>();
            ParticipantMedicalIdentifier pmi  = new ParticipantMedicalIdentifier();
            pmi.setMedicalRecordNumber("MRN-01-ABC-" + UniqueKeyGeneratorUtil.getUniqueKey());
            pmi.setParticipant(p);
            pmi.setSite(new Site());
            pmi.getSite().setId((long) 1);
            pmi.getSite().setName("In Transit");
            pmis.add(pmi);
            p.setParticipantMedicalIdentifierCollection(pmis);
			p = insert(p);
			assertTrue("Participant inserted successfully." + p.getId(), true);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to insert Participant.", true);
		}
	}

	public void testAddParticipantWithRecordEntries() {
		try {
			Participant p = BaseTestCaseUtility.initParticipant();
            Collection<ParticipantRecordEntry> pres = new HashSet<ParticipantRecordEntry>();
            ParticipantRecordEntry pre = new ParticipantRecordEntry();
            pre.setParticipant(p);
            pre.setActivityStatus("Active");
            pres.add(pre);
            p.setParticipantRecordEntryCollection(pres);
			p = insert(p);
			assertTrue("Participant inserted successfully." + p.getId(), true);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to insert Participant.", true);
		}
	}

	public void testUpdateParticipant() {
		Participant participant = BaseTestCaseUtility.initParticipant();
		try {
			CollectionProtocol cp = getCollectionProtocolByShortTitle(PropertiesLoader.getCPTitleForUpdateParticipant());
			User user = getUserByLoginName(PropertiesLoader.getAdminUsername());

			CollectionProtocolRegistration cpr = BaseTestCaseUtility.initCollectionProtocolRegistration(participant, cp, user);
			Collection<CollectionProtocolRegistration> cprCollection = new HashSet<CollectionProtocolRegistration>();
			cprCollection.add(cpr);
			participant.setCollectionProtocolRegistrationCollection(cprCollection);
		} catch (ParseException e) {
			e.printStackTrace();
			assertFalse("Failed to generate the mock data", true);
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Failed to generate the mock data", true);
		}

		try {
			participant = insert(participant);
			BaseTestCaseUtility.updateParticipant(participant);

			Participant updatedParticipant = update(participant);

			Collection<CollectionProtocolRegistration> cprs = updatedParticipant.getCollectionProtocolRegistrationCollection();
			if (updatedParticipant != null
					&& updatedParticipant.getRaceCollection().contains("Unknown")
					&& updatedParticipant.getRaceCollection().contains("Black or African American") && cprs != null
					&& cprs.size() > 0) {
				assertTrue("Participant updated successfully", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to update participant: " + participant.getId(), true);
		}
	}

	public void testUpdateCPRWithBarcode() {
		Participant participant = BaseTestCaseUtility.initParticipant();

		try {
			CollectionProtocol cp = getCollectionProtocolByShortTitle(PropertiesLoader.getCPTitleForUpdateParticipantWithBarcodeinCPR());
			User user = getUserByLoginName(PropertiesLoader.getAdminUsername());
			CollectionProtocolRegistration cpr = BaseTestCaseUtility.initCollectionProtocolRegistration(participant, cp, user);
			Collection<CollectionProtocolRegistration> cprs = new HashSet<CollectionProtocolRegistration>();
			cprs.add(cpr);
			participant.setCollectionProtocolRegistrationCollection(cprs);
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Failed to generate the mock data", true);
		} catch (ParseException e) {
			e.printStackTrace();
			assertFalse("Failed to generate the mock data", true);
		}

		try {
			participant = insert(participant);

			BaseTestCaseUtility.updateParticipant(participant);

			CollectionProtocolRegistration cpr1 = participant.getCollectionProtocolRegistrationCollection().iterator().next();
			String barcode = "PATICIPANT" + UniqueKeyGeneratorUtil.getUniqueKey();
			cpr1.setBarcode(barcode);

			Participant updatedParticipant = update(participant);

			CollectionProtocolRegistration cpr2 = updatedParticipant.getCollectionProtocolRegistrationCollection().iterator().next();
			if (!barcode.equals(cpr2.getBarcode())) {
				assertFalse("Failed to update participant for setting CPR having barcode value as " + barcode, true);
			}
			assertTrue("Domain object successfully updated ---->" + updatedParticipant, true);
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to update participant for setting CPR having barcode value", true);
		}
	}

	public void testMatchingParticipant() {
		Participant participant = new Participant();
		participant.setFirstName(PropertiesLoader.getFirstNameForMatchingParticipant());

		try {
			List<Object> resultList = getApplicationService().getParticipantMatchingObects(participant);

			for (Object object : resultList) {
				if (object instanceof Participant || object instanceof DefaultLookupResult) {
					Participant result = null;
					if (object instanceof DefaultLookupResult) {
						DefaultLookupResult d = (DefaultLookupResult) object;
						result = (Participant) d.getObject();
					} else {
						result = (Participant) object;
					}

					if (!StringUtils.contains(result.getFirstName(), PropertiesLoader.getFirstNameForMatchingParticipant())) {
						assertFalse("Failed to retrieve matching participants having first name value as " + PropertiesLoader.getFirstNameForMatchingParticipant(), true);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Failed to retrieve matching participants having first name value as " + PropertiesLoader.getFirstNameForMatchingParticipant(), true);
		}
	}

	public void testSearchParticipantByExample() {
		String gender = PropertiesLoader.getGenderForSearchParticipantByExample();
		Participant participant = new Participant();
		participant.setGender(gender);

		List<Participant> result = null;
		try {
			result = searchByExample(Participant.class, participant);

			for (Participant p : result) {
				if (!gender.equals(p.getGender())) {
					assertFalse("Failed to retrieve matching participants having gender value as " + PropertiesLoader.getGenderForSearchParticipantByExample(), true);
					break;
				}
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			assertFalse("Failed to retrieve matching participants having gender value as " + PropertiesLoader.getGenderForSearchParticipantByExample(), true);
		}
	}

	private CollectionProtocol getCollectionProtocolByShortTitle(String shortTitle) throws ApplicationException {
		CollectionProtocol cp = new CollectionProtocol();
		cp.setShortTitle(shortTitle);

		List<CollectionProtocol> collectionProtocols = searchByExample(CollectionProtocol.class, cp);

		CollectionProtocol result = null;
		if (collectionProtocols != null && !collectionProtocols.isEmpty()) {
			result = collectionProtocols.get(0);
		}

		return result;
	}

	private User getUserByLoginName(String loginName) throws ApplicationException {
		User user = new User();
		user.setLoginName(loginName);
		List<User> users = searchByExample(User.class, user);
		User result = null;
		if (users != null && !users.isEmpty()) {
			result = users.get(0);
		}
		return result;
	}

    public void testInsertParticipant() throws Exception {
        Participant p = new Participant();
        assertNull(p.getId());
        p.setFirstName("Jane");
        p.setLastName("Doe");
        p.setActivityStatus("Active");
        SDKQuery query = new InsertExampleQuery(p);
        SDKQueryResult result = applicationService.executeQuery(query);
        p = (Participant) result.getObjectResult();
        assertNotNull(p.getId());
    }

    public void testSearchByExampleUsingInterfaceSearch() throws Exception {
        Participant p = new Participant();
        p.setFirstName("Alice");
        List<Participant> result = searchByExample(Participant.class, p);
        assertEquals(2, result.size());
    }

    public void testSearchByExampleUsingSDKQuery() throws Exception {
        Participant p = new Participant();
        p.setFirstName("Alice");
        SDKQuery sQuery = new SearchExampleQuery(p);
        SDKQueryResult result = applicationService.executeQuery(sQuery);
        Collection c = result.getCollectionResult();
        assertEquals(2, c.size());
    }

    public void testInsertCollectionProtocol() throws ApplicationException {
        String key = UniqueKeyGeneratorUtil.getUniqueKey();

        CollectionProtocol cp = new CollectionProtocol();
        cp.setActivityStatus("Active");
        cp.setTitle("CP Title - " + key);
        cp.setShortTitle("CP Short Title - " + key);
        cp.setStartDate(new Date());

        User user = getUserByLoginName(PropertiesLoader.getAdminUsername());

        cp.setPrincipalInvestigator(user);
        cp.setCoordinatorCollection(new HashSet());

        cp = insert(cp);

        System.out.println(">>> CP Inserted: " + cp.getId());
    }
}
