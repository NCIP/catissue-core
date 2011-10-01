package edu.wustl.catissuecore.api.testcases;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.utils.SpecimenCollectionGroupUtility;
import edu.wustl.catissuecore.factory.utils.SpecimenUtility;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Status;

public class BaseTestCaseUtility {

	public static Participant initParticipant() {
		Participant participant = new Participant();
		participant.setLastName("lname" + UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setFirstName("fname" + UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setMiddleName("mname" + UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setGender("Male Gender");
		participant.setEthnicity("Unknown");
		participant.setSexGenotype("XX");
		participant.setRaceCollection(initRaceCollection(participant, new String[] { "White", "Asian" }));
		participant.setActivityStatus("Active");
		participant.setEthnicity("Hispanic or Latino");
		return participant;
	}

	private static Collection<Race> initRaceCollection(Participant participant, String[] raceNames) {
		Collection<Race> raceCollection = new HashSet<Race>();
		for (String raceName : raceNames) {
			Race race = new Race();
			race.setRaceName(raceName);
			race.setParticipant(participant);
			raceCollection.add(race);
		}
		return raceCollection;
	}

	public static CollectionProtocolRegistration initCollectionProtocolRegistration(Participant participant, CollectionProtocol collectionProtocol, User user) throws ParseException {
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

		collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setConsentWitness(user);

		collectionProtocolRegistration.setProtocolParticipantIdentifier("");
		collectionProtocolRegistration.setActivityStatus("Active");

		collectionProtocolRegistration.setRegistrationDate(Utility.parseDate("08/15/1975", Utility.datePattern("08/15/1975")));

		// Setting Consent Tier Responses.
		collectionProtocolRegistration.setConsentSignatureDate(Utility
				.parseDate("11/23/2006", Utility.datePattern("11/23/2006")));

		collectionProtocolRegistration
				.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");

		Collection<ConsentTierResponse> consentTierResponseCollection = new HashSet<ConsentTierResponse>();
		Collection<ConsentTier> consentTierCollection = collectionProtocol
				.getConsentTierCollection();
		if (consentTierCollection != null) {
			for (ConsentTier consentTier : consentTierCollection) {
				ConsentTierResponse consentResponse = new ConsentTierResponse();
				consentResponse.setConsentTier(consentTier);
				consentResponse.setResponse("Yes");
				consentTierResponseCollection.add(consentResponse);
			}
		}
		collectionProtocolRegistration
				.setConsentTierResponseCollection(consentTierResponseCollection);

		Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection = new HashSet<SpecimenCollectionGroup>();
		collectionProtocolRegistration
				.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);

		return collectionProtocolRegistration;
	}

	public static void updateCollectionProtocol(
			CollectionProtocol collectionProtocol) {
		collectionProtocol.setIrbIdentifier("abcdef");
		collectionProtocol.setShortTitle("PC1"
				+ UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active"); // Active
		collectionProtocol.setTitle("cp updated title"
				+ UniqueKeyGeneratorUtil.getUniqueKey());
		try {
			collectionProtocol.setStartDate(Utility.parseDate("08/15/1975",
					Utility.datePattern("08/15/1975")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static CollectionProtocolEvent initCollectionProtocolEvent() {
		CollectionProtocolEvent collectionProtocolEvent = new CollectionProtocolEvent();
		collectionProtocolEvent.setClinicalStatus("New Diagnosis");
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(1));

		// Setting collection point label
		collectionProtocolEvent.setCollectionPointLabel("User entered value"
				+ UniqueKeyGeneratorUtil.getUniqueKey());

		return collectionProtocolEvent;
	}

	public static SpecimenCollectionGroup createSCG(
			CollectionProtocolRegistration collectionProtocolRegistration)
			throws NameGeneratorException, AssignDataException {
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;

		Collection<CollectionProtocolEvent> collectionProtocolEventCollection = collectionProtocolRegistration
				.getCollectionProtocol().getCollectionProtocolEventCollection();
		if (collectionProtocolEventCollection != null) {
			User user = BaseTestCaseUtility.initUser();
			for (CollectionProtocolEvent collectionProtocolEvent : collectionProtocolEventCollection) {
				specimenCollectionGroup = new SpecimenCollectionGroup();
				specimenCollectionGroup
						.setCollectionProtocolEvent(collectionProtocolEvent);
				specimenCollectionGroup
						.setCollectionProtocolRegistration(collectionProtocolRegistration);

				specimenCollectionGroup = SpecimenCollectionGroupUtility
						.setConsentTierStatusCollectionFromCPR(
								collectionProtocolRegistration,
								specimenCollectionGroup);

				LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory
						.getInstance("speicmenCollectionGroupLabelGeneratorClass");
				specimenCollectionGroupLableGenerator
						.setLabel(specimenCollectionGroup);

				Collection<Specimen> cloneSpecimenCollection = new LinkedHashSet<Specimen>();
				Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent
						.getSpecimenRequirementCollection();
				if (specimenCollection != null) {
					for (SpecimenRequirement reqSpecimen : specimenCollection) {
						if (reqSpecimen.getLineage().equalsIgnoreCase("new")) {
							Specimen cloneSpecimen = getCloneSpecimen(
									specimenMap, reqSpecimen, null,
									specimenCollectionGroup, user);

							LabelGenerator specimenLableGenerator = LabelGeneratorFactory
									.getInstance("specimenLabelGeneratorClass");
							specimenLableGenerator.setLabel(cloneSpecimen);

							cloneSpecimen
									.setSpecimenCollectionGroup(specimenCollectionGroup);
							cloneSpecimenCollection.add(cloneSpecimen);
						}
					}
				}

				specimenCollectionGroup
						.setSpecimenCollection(cloneSpecimenCollection);
			}
		}

		return specimenCollectionGroup;
	}

	private static Specimen getCloneSpecimen(
			Map<Specimen, List<Specimen>> specimenMap,
			SpecimenRequirement reqSpecimen, Specimen pSpecimen,
			SpecimenCollectionGroup specimenCollectionGroup, User user)
			throws AssignDataException {
		Collection<AbstractSpecimen> childrenSpecimen = new LinkedHashSet<AbstractSpecimen>();

		Specimen newSpecimen = (Specimen) new SpecimenObjectFactory()
				.getDomainObject(reqSpecimen.getSpecimenClass(), reqSpecimen);

		if (newSpecimen != null) {
			newSpecimen.setParentSpecimen(pSpecimen);
//			SpecimenUtility.setDefaultSpecimenEventCollection(newSpecimen, user
//					.getId());
			newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);

			SpecimenUtility.setConsentTierStatusCollectionFromSCG(newSpecimen,
					specimenCollectionGroup);

			if (newSpecimen.getParentSpecimen() == null) {
				specimenMap.put(newSpecimen, new ArrayList<Specimen>());
			} else {
				specimenMap.put(newSpecimen, null);
			}

			Collection<AbstractSpecimen> childrenSpecimenCollection = reqSpecimen
					.getChildSpecimenCollection();
			if (childrenSpecimenCollection != null) {
				for (AbstractSpecimen absSpecimen : childrenSpecimenCollection) {
					SpecimenRequirement childReqSpecimen = (SpecimenRequirement) absSpecimen;
					Specimen newchildSpecimen = getCloneSpecimen(specimenMap,
							childReqSpecimen, newSpecimen,
							specimenCollectionGroup, user);
					childrenSpecimen.add(newchildSpecimen);
					newSpecimen.setChildSpecimenCollection(childrenSpecimen);
				}
			}
		}
		return newSpecimen;
	}

	public static void setCollectionProtocolEvent(
			CollectionProtocolEvent collectionProtocolEvent) {
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(1.0));
		collectionProtocolEvent.setCollectionPointLabel("PreStudy1"
				+ Math.random());
		collectionProtocolEvent.setClinicalStatus("Operative");
		collectionProtocolEvent.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE
				.toString());
		collectionProtocolEvent
				.setClinicalDiagnosis("Abdominal fibromatosis (disorder)");
		Collection<SpecimenRequirement> specimenRequirementCollection = null;
		CollectionProtocolEventBean cpEventBean = new CollectionProtocolEventBean();
		SpecimenRequirementBean specimenRequirementBean = createSpecimenBean();
		cpEventBean.addSpecimenRequirementBean(specimenRequirementBean);
		Map<String, GenericSpecimen> specimenMap = cpEventBean
				.getSpecimenRequirementbeanMap();
		if (specimenMap != null && !specimenMap.isEmpty()) {
			specimenRequirementCollection = edu.wustl.catissuecore.util.CollectionProtocolUtil
					.getReqSpecimens(specimenMap.values(), null,
							collectionProtocolEvent);
		}
		collectionProtocolEvent
				.setSpecimenRequirementCollection(specimenRequirementCollection);
	}

	private static SpecimenRequirementBean createSpecimenBean() {
		SpecimenRequirementBean specimenRequirementBean = createSpecimen();

		Map<String, ? extends GenericSpecimen> aliquotSpecimenMap = getChildSpecimenMap("Aliquot");
		Map<String, ? extends GenericSpecimen> deriveSpecimenMap = getChildSpecimenMap("Derived");
		specimenRequirementBean
				.setAliquotSpecimenCollection((LinkedHashMap<String, GenericSpecimen>) aliquotSpecimenMap);
		specimenRequirementBean
				.setDeriveSpecimenCollection((LinkedHashMap<String, GenericSpecimen>) deriveSpecimenMap);
		return specimenRequirementBean;
	}

	private static SpecimenRequirementBean createSpecimen() {
		SpecimenRequirementBean specimenRequirementBean = new SpecimenRequirementBean();
		specimenRequirementBean.setParentName("Specimen_E1");
		specimenRequirementBean.setUniqueIdentifier("E1_S0");
		specimenRequirementBean.setDisplayName("Specimen_E1_S0");
		specimenRequirementBean.setLineage("New");
		specimenRequirementBean.setClassName("Tissue");
		specimenRequirementBean.setType("Fixed Tissue");
		specimenRequirementBean.setTissueSite("Accessory sinus, NOS");
		specimenRequirementBean.setTissueSide("Left");
		specimenRequirementBean.setPathologicalStatus("Malignant, Invasive");
		specimenRequirementBean.setConcentration("0");
		specimenRequirementBean.setQuantity("10");
		specimenRequirementBean.setStorageContainerForSpecimen("Auto");
		specimenRequirementBean.setLabelFormat(null);

		// Collected and received events
		specimenRequirementBean.setCollectionEventUserId(1);
		specimenRequirementBean.setReceivedEventUserId(1);
		specimenRequirementBean
				.setCollectionEventContainer("Heparin Vacutainer");
		specimenRequirementBean.setReceivedEventReceivedQuality("Cauterized");
		specimenRequirementBean.setCollectionEventCollectionProcedure("Lavage");

		// Aliquot
		specimenRequirementBean.setNoOfAliquots("2");
		specimenRequirementBean.setQuantityPerAliquot("1");
		specimenRequirementBean.setStorageContainerForAliquotSpecimem("Auto");
		specimenRequirementBean.setLabelFormatForAliquot(null);

		specimenRequirementBean.setNoOfDeriveSpecimen(1);
		specimenRequirementBean.setDeriveSpecimen(null);
		return specimenRequirementBean;
	}

	private static Map<String, ? extends GenericSpecimen> getChildSpecimenMap(
			String lineage) {
		String noOfAliquotes = "2";
		String quantityPerAliquot = "1";
		Map<String, SpecimenRequirementBean> aliquotMap = new LinkedHashMap<String, SpecimenRequirementBean>();
		Double aliquotCount = Double.parseDouble(noOfAliquotes);
		Double parentQuantity = Double.parseDouble("10");
		Double aliquotQuantity = 0D;
		if (quantityPerAliquot == null || quantityPerAliquot.equals("")) {
			aliquotQuantity = parentQuantity / aliquotCount;
			parentQuantity = parentQuantity - (aliquotQuantity * aliquotCount);
		} else {
			aliquotQuantity = Double.parseDouble(quantityPerAliquot);
			parentQuantity = parentQuantity - (aliquotQuantity * aliquotCount);
		}

		for (int iCount = 1; iCount <= 2; iCount++) {
			SpecimenRequirementBean specimenRequirementBean = createSpecimen();
			specimenRequirementBean.setParentName("Specimen_E1_S0");
			specimenRequirementBean.setUniqueIdentifier("E1_S0_A1");
			specimenRequirementBean.setDisplayName("Specimen_E1_S0_A1");
			specimenRequirementBean.setLineage(lineage);
			if (quantityPerAliquot == null || quantityPerAliquot.equals("")) {
				quantityPerAliquot = "0";
			}
			specimenRequirementBean.setQuantity(quantityPerAliquot);
			specimenRequirementBean.setNoOfAliquots(null);
			specimenRequirementBean.setQuantityPerAliquot(null);
			specimenRequirementBean.setStorageContainerForAliquotSpecimem(null);
			specimenRequirementBean.setStorageContainerForSpecimen("Auto");
			specimenRequirementBean.setDeriveSpecimen(null);
			specimenRequirementBean.setLabelFormat("");
			aliquotMap.put(Integer.toString(iCount), specimenRequirementBean);
		}
		return aliquotMap;
	}

	public static void updateParticipant(Participant participant) {
		participant.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
		participant
				.setFirstName("frst" + UniqueKeyGeneratorUtil.getUniqueKey());
		participant
				.setMiddleName("mdl" + UniqueKeyGeneratorUtil.getUniqueKey());

		participant.setVitalStatus("Alive");
		participant.setGender("Male Gender");
		participant.setSexGenotype("");

		participant.setRaceCollection(initRaceCollection(participant,
				new String[] { "Black or African American", "Unknown" }));
		participant.setActivityStatus("Active");
		participant.setEthnicity("Unknown");

		participant
				.setParticipantMedicalIdentifierCollection(new HashSet<ParticipantMedicalIdentifier>());
	}

	public static CollectionProtocol initCollectionProtocol()
			throws ParseException {
		CollectionProtocol collectionProtocol = new CollectionProtocol();

		Collection<ConsentTier> consentTierColl = new LinkedHashSet<ConsentTier>();
		ConsentTier c1 = new ConsentTier();
		c1.setStatement("Consent for aids research");
		consentTierColl.add(c1);
		ConsentTier c2 = new ConsentTier();
		c2.setStatement("Consent for cancer research");
		consentTierColl.add(c2);
		ConsentTier c3 = new ConsentTier();
		c3.setStatement("Consent for Tb research");
		consentTierColl.add(c3);

		collectionProtocol.setConsentTierCollection(consentTierColl);
		collectionProtocol.setAliquotInSameContainer(new Boolean(true));
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active");

		collectionProtocol.setEndDate(null);
		collectionProtocol.setEnrollment(null);
		collectionProtocol.setIrbIdentifier("77777");

		collectionProtocol.setTitle("ColProt"
				+ UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocol.setShortTitle("cp"
				+ UniqueKeyGeneratorUtil.getUniqueKey());
		collectionProtocol.setEnrollment(2);

		collectionProtocol.setSpecimenLabelFormat("");
		collectionProtocol.setDerivativeLabelFormat("");
		collectionProtocol.setAliquotLabelFormat("");

		collectionProtocol.setStartDate(Utility.parseDate("08/15/2003", Utility
				.datePattern("08/15/1975")));

		Collection<CollectionProtocolEvent> collectionProtocolEventList = new LinkedHashSet<CollectionProtocolEvent>();
		CollectionProtocolEvent collectionProtocolEvent = null;
		for (int specimenEventCount = 0; specimenEventCount < 2; specimenEventCount++) {
			collectionProtocolEvent = new CollectionProtocolEvent();
			setCollectionProtocolEvent(collectionProtocolEvent);
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			collectionProtocolEventList.add(collectionProtocolEvent);
		}
		collectionProtocol
				.setCollectionProtocolEventCollection(collectionProtocolEventList);

		User principalInvestigator = new User();
		principalInvestigator.setId(new Long("1"));
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);

		Collection<User> protocolCordinatorCollection = new HashSet<User>();
		collectionProtocol
				.setCoordinatorCollection(protocolCordinatorCollection);

		return collectionProtocol;
	}

	public static User initUser() {
		User userObj = new User();
		userObj.setEmailAddress("ddd" + UniqueKeyGeneratorUtil.getUniqueKey()
				+ "@admin.com");
		userObj.setLoginName(userObj.getEmailAddress());
		userObj.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
		userObj.setFirstName("name" + UniqueKeyGeneratorUtil.getUniqueKey());

		Address address = new Address();
		address.setStreet("Main street");
		address.setCity("New hampshier");
		address.setState("Alabama");
		address.setZipCode("12345");
		address.setCountry("United States");
		address.setPhoneNumber("212-223-2424");
		address.setFaxNumber("212-223-2424");

		userObj.setAddress(address);

		Institution inst = new Institution();
		inst.setId(new Long(1));
		userObj.setInstitution(inst);

		Department department = new Department();
		department.setId(new Long(1));
		userObj.setDepartment(department);

		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
		cancerResearchGroup.setId(new Long(1));
		userObj.setCancerResearchGroup(cancerResearchGroup);

		userObj.setRoleId("1");
		userObj.setActivityStatus("Active");

		return userObj;
	}

	public static User initUpdateUser(User userObj) {
		userObj.setEmailAddress("sup" + UniqueKeyGeneratorUtil.getUniqueKey()
				+ "@sup.com");
		userObj.setLoginName(userObj.getEmailAddress());
		userObj.setLastName("last" + UniqueKeyGeneratorUtil.getUniqueKey());
		userObj.setFirstName("name" + UniqueKeyGeneratorUtil.getUniqueKey());

		Address address = new Address();
		address.setStreet("Main street");
		address.setCity("New hampshier");
		address.setState("Alabama");
		address.setZipCode("12345");
		address.setCountry("United States");
		address.setPhoneNumber("212-223-3434");
		address.setFaxNumber("212-223-3434");

		userObj.setAddress(address);

		Institution inst = BaseTestCaseUtility.initInstitution();
		userObj.setInstitution(inst);

		Department department = BaseTestCaseUtility.initDepartment();
		userObj.setDepartment(department);

		CancerResearchGroup cancerResearchGroup = BaseTestCaseUtility
				.initCancerResearchGrp();

		userObj.setCancerResearchGroup(cancerResearchGroup);
		userObj.setRoleId("1");
		userObj.setActivityStatus("Active");

		return userObj;
	}

	public static Site initSite() {
		Site siteObj = new Site();
		User userObj = new User();
		userObj.setId(new Long(1));

		siteObj.setEmailAddress("admin@admin.com");
		siteObj.setName("sit" + UniqueKeyGeneratorUtil.getUniqueKey());
		siteObj.setType("Laboratory");
		siteObj.setActivityStatus("Active");
		siteObj.setCoordinator(userObj);

		Address addressObj = new Address();
		addressObj.setCity("Saint Louis");
		addressObj.setCountry("United States");
		addressObj.setFaxNumber("555-555-5555");
		addressObj.setPhoneNumber("212-223-2424");
		addressObj.setState("Missouri");
		addressObj.setStreet("4939 Children's Place");
		addressObj.setZipCode("63110");
		siteObj.setAddress(addressObj);
		return siteObj;
	}

	public static void updateSite(Site siteObj) {
		siteObj.setEmailAddress("admin1@admin.com");
		siteObj.setName("updatedSite" + UniqueKeyGeneratorUtil.getUniqueKey());
		siteObj.setType("Repository");
		siteObj.setActivityStatus("Active");
		siteObj.getAddress().setCity("Saint Louis1");
		siteObj.getAddress().setCountry("United States");
		siteObj.getAddress().setFaxNumber("777-777-7777");
		siteObj.getAddress().setPhoneNumber("212-223-2424");
		siteObj.getAddress().setState("Missouri");
		siteObj.getAddress().setStreet("4939 Children's Place1");
		siteObj.getAddress().setZipCode("63111");
	}

	public static Department initDepartment() {
		Department dept = new Department();
		dept.setName("department name" + UniqueKeyGeneratorUtil.getUniqueKey());
		return dept;
	}

	public static void updateDepartment(Department department) {
		department.setName("dt" + UniqueKeyGeneratorUtil.getUniqueKey());
	}

	public static Institution initInstitution() {
		Institution institutionObj = new Institution();
		institutionObj.setName("inst" + UniqueKeyGeneratorUtil.getUniqueKey());
		return institutionObj;
	}

	public static void updateInstitution(Institution institution) {
		institution.setName("inst" + UniqueKeyGeneratorUtil.getUniqueKey());
	}

	public static CancerResearchGroup initCancerResearchGrp() {
		CancerResearchGroup cancerResearchGroup = new CancerResearchGroup();
		cancerResearchGroup.setName("crgName"
				+ UniqueKeyGeneratorUtil.getUniqueKey());
		return cancerResearchGroup;
	}

	public static void updateCancerResearchGrp(
			CancerResearchGroup cancerResearchGroup) {
		cancerResearchGroup.setName("crgName"
				+ UniqueKeyGeneratorUtil.getUniqueKey());
	}

}