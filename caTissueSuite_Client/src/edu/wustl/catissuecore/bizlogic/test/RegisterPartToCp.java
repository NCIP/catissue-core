
package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Collection;
import java.util.Map;

import edu.wustl.catissuecore.bizlogic.test.CaTissueBaseTestCase;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class RegisterPartToCp extends CaTissueBaseTestCase
{

	private int rowNo = 1;

	public Participant getParticipant(String excel[][]) throws Exception
	{
		String partId = excel[rowNo][0];
		String protPartId = excel[rowNo][1];

		if (partId.contains("."))
		{
			//System.out.println("true " + partId);
			//System.out.println(partId.substring(0, partId.indexOf('.')));
			partId = partId.substring(0, partId.indexOf('.'));
		}

		Participant participant = new Participant();
		participant.setId(new Long(partId));

		try
		{
			List resultList = appService.search(Participant.class, participant);
			System.out.println("No of Participant retrived " + resultList.size());
			if (resultList.size() == 0){
				participant.setId(null);
			}
			else 
			{
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
				{
					participant = (Participant) resultsIterator.next();
					System.out.println(" Domain Object is successfully Found ---->  :: "
							+ participant.getLastName() + " " + participant.getFirstName());
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("ParticipantTestCases.testSearchParticipant()" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return participant;
	}

	public CollectionProtocol getCP(String excel[][]) throws Exception
	{

		String cpShortTitle = excel[rowNo][2].trim();
		cpShortTitle = "cp_biomarker";
		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setShortTitle(cpShortTitle);
		System.out.println("cpShortTitle " + cpShortTitle);
		List<?> resultList1 = null;
		try
		{
			resultList1 = appService.search(CollectionProtocol.class, collectionProtocol);
			System.out.println("No of CP retrived from DB " + resultList1.size());
			if (resultList1.size() > 0)
			{
				collectionProtocol = (CollectionProtocol) resultList1.get(0);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return collectionProtocol;
	}

	public void registerpartToCp(String excel[][]) throws Exception
	{
		CollectionProtocol collectionProtocol = getCP(excel);
        System.out.println("excel.length "+ excel.length);
		while (rowNo < excel.length)
		{
			Participant participant = getParticipant(excel);
			//CollectionProtocol collectionProtocol = getCP(excel);
			if (participant.getId() != null && participant.getId() != -1)
			{
				Collection participantMedicalIdentifierCollection = new LinkedHashSet();
				participant
						.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);

				Collection coll = participant.getCollectionProtocolRegistrationCollection();
				System.out.println("participant.getCollectionProtocolRegistrationCollection() "
						+ coll.size());

				Iterator<CollectionProtocolRegistration> cprItr = coll.iterator();
			//	System.out.println("here 1");
				while (cprItr.hasNext())
				{
					CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) cprItr
							.next();
					cpr.setConsentTierResponseCollection(null);
				}

				System.out.println("updating participant");
				CollectionProtocolRegistration newCpr = initCollectionProtocolRegistration(
						participant, collectionProtocol, excel);

				System.out
						.println(participant.getCollectionProtocolRegistrationCollection().size());
				Collection cprColl = participant.getCollectionProtocolRegistrationCollection();
				cprColl.add(newCpr);
				participant.setCollectionProtocolRegistrationCollection(cprColl);
				System.out.println("updating participant");
				Participant updatedParticipant = (Participant) appService.updateObject(participant);
				System.out.println("updated participant");

				/*Collection updatedPartColl = updatedParticipant
						.getCollectionProtocolRegistrationCollection();
				System.out
						.println("updatedParticipant.getCollectionProtocolRegistrationCollection() "
								+ updatedPartColl.size());

				Iterator<CollectionProtocolRegistration> newcprItr = updatedPartColl.iterator();
				System.out.println("here 1");
				while (newcprItr.hasNext())
				{
					CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) newcprItr
							.next();
					cpr.getSpecimenCollectionGroupCollection();
					cpr.setConsentTierResponseCollection(null);
				}

				System.out.println();*/
				
			}
			else {
				System.out.println("Participant not avaiable ");
			}
			rowNo++;
		}
	}

	public CollectionProtocolRegistration initCollectionProtocolRegistration(
			Participant participant, CollectionProtocol cp, String excel[][]) throws Exception
	{
		//Logger.configure("");
		String pPI = excel[rowNo][1];

		System.out.println("pPI " + pPI);
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

		collectionProtocolRegistration.setCollectionProtocol(cp);
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setProtocolParticipantIdentifier(pPI);
		collectionProtocolRegistration.setActivityStatus("Active");
		try
		{
			//collectionProtocolRegistration.setRegistrationDate(Utility.parseDate(colldate.replace('/', '-'), "M-d-yyyy"));
			/*collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy  **/
			//Date timestamp = EventsUtil.setTimeStamp(colldate,"15","45");
			Date timestamp = EventsUtil.setTimeStamp("08-15-1975", "15", "45");
			collectionProtocolRegistration.setRegistrationDate(timestamp);

		}
		catch (Exception e)
		{
			System.out.println("Exception in initCollectionProtocolRegistration");
			System.err.println("Exception in initCollectionProtocolRegistration");
			e.printStackTrace();
		}

		try
		{
			collectionProtocolRegistration.setConsentSignatureDate(Utility.parseDate("11/23/2006",
					Utility.datePattern("11/23/2006")));
		}
		catch (ParseException e)
		{
			System.err.println("exception in initCPR in Date");
			System.out.println("exception in initCPR in Date");
			e.printStackTrace();
		}
		// pratha collectionProtocolRegistration.setSignedConsentDocumentURL("F:/doc/consentDoc.doc");
		User user = getUser("abrink@pathology.wustl.edu");
		collectionProtocolRegistration.setConsentWitness(user);

		Collection consentTierResponseCollection = new HashSet();
		Collection consentTierCollection = cp.getConsentTierCollection();
		System.out.println("did not reached");
		if(consentTierCollection != null) 
		{
			Iterator consentTierItr = consentTierCollection.iterator();
			while (consentTierItr.hasNext())
			{
				ConsentTier consentTier = (ConsentTier) consentTierItr.next();
				ConsentTierResponse consentResponse = new ConsentTierResponse();
				consentResponse.setConsentTier(consentTier);
				consentResponse.setResponse("Yes");
				consentTierResponseCollection.add(consentResponse);
			}
		}
		collectionProtocolRegistration
				.setConsentTierResponseCollection(consentTierResponseCollection);
		System.out.println("calling createSCG in initCollectionProtocolRegistration");
		SpecimenCollectionGroup specimenCollectionGroup = createSCG(collectionProtocolRegistration);
		Site site = createSite();
		specimenCollectionGroup.setSpecimenCollectionSite(site);
		Collection specimenCollectionGroupCollection = new HashSet<SpecimenCollectionGroup>();
		collectionProtocolRegistration
				.setSpecimenCollectionGroupCollection(specimenCollectionGroupCollection);

		Collection collectionProtocolRegistrationCollection = new HashSet();

		System.out.println("CPR initiated");
		return collectionProtocolRegistration;
	}

	private User getUser(String loginName)
	{
		User user = new User();
		user.setLoginName(loginName);
		List<?> resultList1 = new LinkedList();
		try
		{
			List resultList = appService.search(User.class, user);
			if (resultList.size() > 0)
			{
				user = (User) resultList.get(0);
			}

		}
		catch (Exception e)
		{
			System.out.println("Exception in getUser");
			System.err.println("Exception in getUser");
			e.printStackTrace();
		}

		return user;
	}

	public SpecimenCollectionGroup createSCG(
			CollectionProtocolRegistration collectionProtocolRegistration)
	{
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;
		try
		{
			Collection collectionProtocolEventCollection = collectionProtocolRegistration
					.getCollectionProtocol().getCollectionProtocolEventCollection();
			Iterator collectionProtocolEventIterator = collectionProtocolEventCollection.iterator();
			//User user = (User)TestCaseUtility.getObjectMap(User.class);
			User user = getUser("abrink@pathology.wustl.edu");
			while (collectionProtocolEventIterator.hasNext())
			{
				CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) collectionProtocolEventIterator
						.next();
				specimenCollectionGroup = new SpecimenCollectionGroup(collectionProtocolEvent);
				specimenCollectionGroup
						.setCollectionProtocolRegistration(collectionProtocolRegistration);
				specimenCollectionGroup
						.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);

				LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory
						.getInstance("speicmenCollectionGroupLabelGeneratorClass");
				specimenCollectionGroupLableGenerator.setLabel(specimenCollectionGroup);

				Collection cloneSpecimenCollection = new LinkedHashSet();
				Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent
						.getSpecimenRequirementCollection();
				if (specimenCollection != null && !specimenCollection.isEmpty())
				{
					Iterator itSpecimenCollection = specimenCollection.iterator();
					while (itSpecimenCollection.hasNext())
					{
						SpecimenRequirement reqSpecimen = (SpecimenRequirement) itSpecimenCollection
								.next();
						if (reqSpecimen.getLineage().equalsIgnoreCase("new"))
						{
							Specimen cloneSpecimen = getCloneSpecimen(specimenMap, reqSpecimen,
									null, specimenCollectionGroup, user);
							LabelGenerator specimenLableGenerator = LabelGeneratorFactory
									.getInstance("specimenLabelGeneratorClass");
							specimenLableGenerator.setLabel(cloneSpecimen);
							cloneSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
							cloneSpecimenCollection.add(cloneSpecimen);
						}
					}
				}

				specimenCollectionGroup.setSpecimenCollection(cloneSpecimenCollection);
			}
		}
		catch (Exception e)
		{
			System.err.println("Exception in create SCG");
			System.out.println("Exception in create SCG");
			e.printStackTrace();
		}
		return specimenCollectionGroup;
	}

	private static Specimen getCloneSpecimen(Map<Specimen, List<Specimen>> specimenMap,
			SpecimenRequirement reqSpecimen, Specimen pSpecimen,
			SpecimenCollectionGroup specimenCollectionGroup, User user)
	{
		Collection childrenSpecimen = new LinkedHashSet<Specimen>();
		Specimen newSpecimen = null;
		try
		{
			newSpecimen = (Specimen) new SpecimenObjectFactory().getDomainObject(reqSpecimen
					.getClassName(), reqSpecimen);
		}
		catch (AssignDataException e1)
		{
			System.out.println("Exception in getCloneSpecimen");
			System.err.println("Exception in getCloneSpecimen");
			e1.printStackTrace();
			return null;
		}
		newSpecimen.setParentSpecimen(pSpecimen);
		newSpecimen.setDefaultSpecimenEventCollection(user.getId());
		newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		newSpecimen.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);
		if (newSpecimen.getParentSpecimen() == null)
		{
			specimenMap.put(newSpecimen, new ArrayList<Specimen>());
		}
		else
		{
			specimenMap.put(newSpecimen, null);
		}

		Collection childrenSpecimenCollection = reqSpecimen.getChildSpecimenCollection();
		if (childrenSpecimenCollection != null && !childrenSpecimenCollection.isEmpty())
		{
			Iterator<SpecimenRequirement> it = childrenSpecimenCollection.iterator();
			while (it.hasNext())
			{
				SpecimenRequirement childReqSpecimen = it.next();
				Specimen newchildSpecimen = getCloneSpecimen(specimenMap, childReqSpecimen,
						newSpecimen, specimenCollectionGroup, user);
				childrenSpecimen.add(newchildSpecimen);
				newSpecimen.setChildSpecimenCollection(childrenSpecimen);
			}
		}
		return newSpecimen;
	}

	private Site createSite() throws Exception
	{

		// String collSite = excel[rowNo][9];
		Site site = new Site();
		site.setId(new Long(700036)); // Siteman Cancer Center id
		List<?> resultList1 = null;
		try
		{
			resultList1 = appService.search(Site.class, site);
			if (resultList1.size() > 0)
			{
				System.out.println("No of Sites retrived from DB " + resultList1.size());
				site = (Site) resultList1.get(0);
				System.out.println("site got");
			}
		}
		catch (Exception e1)
		{
			System.out.println("Exception in searching site");
			System.err.println("Exception in searching site");
			e1.printStackTrace();
			throw e1;
		}
		return site;
	}

}