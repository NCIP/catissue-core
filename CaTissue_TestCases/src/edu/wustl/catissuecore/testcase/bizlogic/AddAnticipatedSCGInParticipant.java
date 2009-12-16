package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.AssignDataException;
import gov.nih.nci.common.util.HQLCriteria;


public class AddAnticipatedSCGInParticipant extends CaTissueSuiteBaseTest {

	private static int rowNo = 1;
	private Set CpCpeId = new HashSet<Long>();

	public CollectionProtocol getCP(String excel[][]) throws Exception {

		String cpShortTitle = excel[rowNo][0].trim();

		CollectionProtocol collectionProtocol = new CollectionProtocol();
		collectionProtocol.setShortTitle(cpShortTitle);
		System.out.println("cpShortTitle " + cpShortTitle);
		List<?> resultList1 = null;
		try {
			String query = "from edu.wustl.catissuecore.domain.CollectionProtocol as collectionProtocol where "
				+ "collectionProtocol.shortTitle= '"+cpShortTitle+"'";	
//			resultList1 = appService.search(CollectionProtocol.class,
//					collectionProtocol);
			resultList1 = appService.search(query);
			System.out.println("No of CP retrived from DB "
					+ resultList1.size());
			if (resultList1.size() > 0) {
				collectionProtocol = (CollectionProtocol) resultList1.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return collectionProtocol;
	}

	public void getAllCPR(CollectionProtocol collectionProtocol)
			throws Exception {
		try {
			CollectionProtocol cp = new CollectionProtocol();
			cp.setId(collectionProtocol.getId());
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
			collectionProtocolRegistration.setCollectionProtocol(cp);
			String query = "from edu.wustl.catissuecore.domain.CollectionProtocolRegistration as collectionProtocolRegistration where "
				+ "collectionProtocolRegistration.collectionProtocol.id="+collectionProtocol.getId();
			List cprResultList = appService.search(query);
//			List cprResultList = appService.search(
//					CollectionProtocolRegistration.class,
//					collectionProtocolRegistration);
			Iterator cprResultIterator = cprResultList.iterator();
			System.out.println("No of CPR retrived " + cprResultList.size());
			while (cprResultIterator.hasNext()) {
				collectionProtocolRegistration = (CollectionProtocolRegistration) cprResultIterator
						.next();
				getAllSCGs(collectionProtocolRegistration);
				// System.out.println("CollectionProtocolEvent "+
				// collectionProtocolRegistration
				// .getSpecimenCollectionGroupCollection().size());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

	}

	public void getAllSCGs(CollectionProtocolRegistration cpr) throws Exception {
		try {
			String query = null;
			HQLCriteria hqlcri = null;
			query = "select specimenCollectionGroup.collectionProtocolEvent from edu.wustl.catissuecore.domain.SpecimenCollectionGroup"
					+ " as specimenCollectionGroup where "
					+ " specimenCollectionGroup.collectionProtocolRegistration.id= "
					+ cpr.getId();
			//hqlcri = new HQLCriteria(query);
//			List cpeList = appService.query(hqlcri,
//					CollectionProtocolEvent.class.getName());
			List cpeList = appService.search(query);
			Iterator<CollectionProtocolEvent> cpeItr = cpeList.iterator();
			List cpeIdList = new ArrayList();
			while (cpeItr.hasNext()) {
				cpeIdList
						.add(((CollectionProtocolEvent) cpeItr.next()).getId());
			}
			Iterator<Long> itr = this.CpCpeId.iterator();
			while (itr.hasNext()) {
				Long id = itr.next();
				if (!cpeIdList.contains(id)) {
					System.out.println("cpe id not present " + id);
					addSCGWithNameAndEvents(cpr, id);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public void addSCGs(String excel[][]) throws Exception {
		while (rowNo < excel.length - 1) {

			CollectionProtocol collectionProtocol = getCP(excel);
			if (collectionProtocol.getId() != null) {

				System.out.println("CollectionProtocolEvent size "
						+ collectionProtocol
								.getCollectionProtocolEventCollection().size());
				Iterator<CollectionProtocolEvent> cprItr = collectionProtocol
						.getCollectionProtocolEventCollection().iterator();
				while (cprItr.hasNext()) {
					CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) cprItr
							.next();
					System.out
							.println("CollectionProtocolEvent "
									+ collectionProtocolEvent
											.getCollectionPointLabel());
					CpCpeId.add(collectionProtocolEvent.getId());
				}
				getAllCPR(collectionProtocol);
				System.out.println("End..");
			}
			rowNo++;
			CpCpeId = new HashSet<Long>();
		}
	}

	public SpecimenCollectionGroup addSCGWithNameAndEvents(
			CollectionProtocolRegistration cpr, Long cpeId) throws Exception {
		// System.out.println(" in addSCGWithNameAndEvents ");
		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
		CollectionProtocolEvent collectionProtocolEvent = getCpe(cpeId);
		specimenCollectionGroup = (SpecimenCollectionGroup) createSCG(collectionProtocolEvent);
		if (specimenCollectionGroup != null) {
			Site site = createSite();
			specimenCollectionGroup.setSpecimenCollectionSite(site);
			String scgName = "scg added through api"
					+ UniqueKeyGeneratorUtil.getUniqueKey();
			specimenCollectionGroup.setName(scgName);
			specimenCollectionGroup.setCollectionProtocolRegistration(cpr);
			System.out.println("set cpr in scg");
			setEventParameters(specimenCollectionGroup);
			try {
				specimenCollectionGroup = (SpecimenCollectionGroup) appService
						.createObject(specimenCollectionGroup);
			} catch (Exception e) {
				System.out.println("Exception in addSCGWithNameAndEvents");
				System.err.println("Exception in addSCGWithNameAndEvents");
				e.printStackTrace();
				throw e;
			}
		}
		return specimenCollectionGroup;
	}

	public CollectionProtocolEvent getCpe(Long id) throws Exception {
		CollectionProtocolEvent cpe = null;
		try {
			String query = null;
			HQLCriteria hqlcri = null;
			query = "select collectionProtocolEvent from edu.wustl.catissuecore.domain.CollectionProtocolEvent"
					+ " as collectionProtocolEvent where "
					+ " collectionProtocolEvent.id= " + id;
//			List cpeList = appService.query(hqlcri,
//					CollectionProtocolEvent.class.getName());
			List cpeList = appService.search(query);
			if (cpeList != null) {
				Iterator<CollectionProtocolEvent> cpeItr = cpeList.iterator();
				System.out.println("cpeList.size() " + cpeList.size());
				while (cpeItr.hasNext()) {
					cpe = (CollectionProtocolEvent) cpeItr.next();
					System.out.println(" cpe ID  " + cpe.getId());
					System.out.println(" cpe label "
							+ cpe.getCollectionPointLabel());

				}
			}
		} catch (Exception ex) {
			System.out.println("Exception in getCpe");
			throw ex;
		}
		return cpe;
	}

	/*
	 * public CollectionProtocolEvent getCpe(Long id) throws Exception {
	 * CollectionProtocolEvent cpe = null; try { String query = null;
	 * HQLCriteria hqlcri = null; cpe = new CollectionProtocolEvent();
	 * System.out.println(" before cpe ID " + id); //
	 * cpe.setCollectionPointLabel("newEveUI"); cpe.setId(id);
	 * System.out.println(" after cpe ID " + cpe.getId()); List cpeIdList =
	 * appService.search(CollectionProtocolEvent.class, cpe);
	 * System.out.println(" cpeIdList size " + cpeIdList.size());
	 * 
	 * if (cpeIdList != null && cpeIdList.size() > 0) { cpe =
	 * (CollectionProtocolEvent) cpeIdList.get(0); System.out.println(" cpe ID "
	 * + cpe.getId()); System.out .println(" cpe label" +
	 * cpe.getCollectionPointLabel()); }
	 * 
	 * } catch (Exception ex) { ex.printStackTrace(); throw ex; } return cpe; }
	 */

	public SpecimenCollectionGroup createSCG(
			CollectionProtocolEvent collectionProtocolEvent) throws Exception {
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;
		try {
			User user = getUser(" ");
			specimenCollectionGroup = new SpecimenCollectionGroup(
					collectionProtocolEvent);
			System.out.println("scg label "
					+ specimenCollectionGroup.getCollectionProtocolEvent()
							.getCollectionPointLabel());
			specimenCollectionGroup
					.setCollectionProtocolRegistration(collectionProtocolEvent
							.getCollectionProtocolRegistration());

			LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory
					.getInstance("speicmenCollectionGroupLabelGeneratorClass");
			specimenCollectionGroupLableGenerator
					.setLabel(specimenCollectionGroup);

			Collection cloneSpecimenCollection = new LinkedHashSet();
			Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent
					.getSpecimenRequirementCollection();
			if (specimenCollection != null && !specimenCollection.isEmpty()) {
				Iterator itSpecimenCollection = specimenCollection.iterator();
				while (itSpecimenCollection.hasNext()) {
					SpecimenRequirement reqSpecimen = (SpecimenRequirement) itSpecimenCollection
							.next();
					if (reqSpecimen.getLineage().equalsIgnoreCase("new")) {
						Specimen cloneSpecimen = getCloneSpecimen(specimenMap,
								reqSpecimen, null, specimenCollectionGroup,
								user);
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
		} catch (Exception e) {
			System.out.println("Exception in create SCG");
			System.err.println("Exception in create SCG");
			e.printStackTrace();
			// throw e;
		}
		return specimenCollectionGroup;
	}

	private static Specimen getCloneSpecimen(
			Map<Specimen, List<Specimen>> specimenMap,
			SpecimenRequirement reqSpecimen, Specimen pSpecimen,
			SpecimenCollectionGroup specimenCollectionGroup, User user) {
		Collection childrenSpecimen = new LinkedHashSet<Specimen>();
		Specimen newSpecimen = null;
		try {
			newSpecimen = (Specimen) new SpecimenObjectFactory()
					.getDomainObject(reqSpecimen.getClassName(), reqSpecimen);
		} catch (AssignDataException e1) {
			System.out.println("Exception in getCloneSpecimen");
			System.err.println("Exception in getCloneSpecimen");
			e1.printStackTrace();
			return null;
		}
		newSpecimen.setParentSpecimen(pSpecimen);
		newSpecimen.setDefaultSpecimenEventCollection(user.getId());
		newSpecimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		newSpecimen
				.setConsentTierStatusCollectionFromSCG(specimenCollectionGroup);
		if (newSpecimen.getParentSpecimen() == null) {
			specimenMap.put(newSpecimen, new ArrayList<Specimen>());
		} else {
			specimenMap.put(newSpecimen, null);
		}

		Collection childrenSpecimenCollection = reqSpecimen
				.getChildSpecimenCollection();
		if (childrenSpecimenCollection != null
				&& !childrenSpecimenCollection.isEmpty()) {
			Iterator<SpecimenRequirement> it = childrenSpecimenCollection
					.iterator();
			while (it.hasNext()) {
				SpecimenRequirement childReqSpecimen = it.next();
				Specimen newchildSpecimen = getCloneSpecimen(specimenMap,
						childReqSpecimen, newSpecimen, specimenCollectionGroup,
						user);
				childrenSpecimen.add(newchildSpecimen);
				newSpecimen.setChildSpecimenCollection(childrenSpecimen);
			}
		}
		return newSpecimen;
	}

	private User getUser(String loginName) {

		User user = new User();
		// user.setLoginName(loginName);
		//user.setId(new Long(1)); // changes made for nightly build inegration
		try {
			String query = "from edu.wustl.catissuecore.domain.User as user where "
				+ "user.id= 1";			
			//List resultList = appService.search(User.class, user);
			List resultList = appService.search(query);

			if (resultList != null && resultList.size() > 0) {
				user = (User) resultList.get(0);
			}
		} catch (Exception e) {
			System.out.println("Exception in getUser");
			System.err.println("Exception in getUser");
			e.printStackTrace();
		}

		return user;

	}

	private void setEventParameters(SpecimenCollectionGroup sprObj) {
		System.out.println("Inside setEventParameters for SCG");
		Collection specimenEventParametersCollection = new HashSet();
		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		collectionEventParameters.setCollectionProcedure("Not Specified");
		collectionEventParameters.setComment("");
		collectionEventParameters.setContainer("Not Specified");
		try {
			Date timestamp = EventsUtil.setTimeStamp("08-15-1975", "15", "45");
			collectionEventParameters.setTimestamp(timestamp);
		} catch (Exception e) {
			System.out
					.println("Exception while setting colection date in setEventParameters");
			e.printStackTrace();
		}

		User user = getUser(" ");
		collectionEventParameters.setUser(user);
		collectionEventParameters.setSpecimenCollectionGroup(sprObj);

		// Received Events
		receivedEventParameters.setComment("");
		User receivedUser = getUser("abrink@pathology.wustl.edu");
		receivedEventParameters.setUser(receivedUser);
		receivedEventParameters.setReceivedQuality("Not Specified");
		Date receivedTimestamp = EventsUtil.setTimeStamp("08-15-1975", "15",
				"45");
		receivedEventParameters.setTimestamp(receivedTimestamp);
		receivedEventParameters.setSpecimenCollectionGroup(sprObj);
		specimenEventParametersCollection.add(collectionEventParameters);
		specimenEventParametersCollection.add(receivedEventParameters);
		sprObj
				.setSpecimenEventParametersCollection(specimenEventParametersCollection);
	}

	private void setSpecimenEvents(Specimen specimen, String excel[][], int row)
			throws Exception

	{
		System.out.println("In setSpecimenEvents");
		String collCotainer = excel[row][8];
		String collProcedure = excel[row][7];
		String collDate = excel[row][6];

		Collection<SpecimenEventParameters> specimenEventCollection = new LinkedHashSet<SpecimenEventParameters>();
		CollectionEventParameters collectionEvent = null;
		ReceivedEventParameters receivedEvent = null;

		Collection evColl = specimen.getSpecimenEventCollection();
		// System.out.println( " evColl.size() "+ evColl.size());
		// if(specimen.getLineage()!="Aliquot") {
		if (evColl != null && evColl.size() != 0) {
			System.out.println("got eventColl from specimen ");
			Iterator itr = evColl.iterator();
			while (itr.hasNext()) {
				SpecimenEventParameters spEvPar = (SpecimenEventParameters) itr
						.next();
				if (spEvPar instanceof CollectionEventParameters) {
					System.out.println("got CollectionEventParameters");
					collectionEvent = (CollectionEventParameters) spEvPar;
				} else if (spEvPar instanceof ReceivedEventParameters) {
					System.out.println("got ReceivedEventParameters");
					receivedEvent = (ReceivedEventParameters) spEvPar;
				}
			}
		}
		// }

		if (collectionEvent == null)
			collectionEvent = new CollectionEventParameters();

		User collectionEventUser = getUser(" ");
		collectionEvent.setUser(collectionEventUser);

		if (!specimen.getLineage().equals("New")) {
			System.out.println(" in setSpecimenEventsnot a new specimen");
			Collection parentSpcEvColl = specimen.getParentSpecimen()
					.getSpecimenEventCollection();

			if (parentSpcEvColl != null) {
				Iterator itr = parentSpcEvColl.iterator();
				while (itr.hasNext()) {
					SpecimenEventParameters spEvPar = (SpecimenEventParameters) itr
							.next();
					if (spEvPar instanceof CollectionEventParameters) {
						collectionEvent
								.setCollectionProcedure(((CollectionEventParameters) spEvPar)
										.getCollectionProcedure());
						collectionEvent
								.setContainer(((CollectionEventParameters) spEvPar)
										.getContainer());
					}
				}
			}
		} else {
			System.out.println("in else ");
			collectionEvent.setCollectionProcedure(collProcedure);
			collectionEvent.setContainer(collCotainer);
		}

		try {
			Date timestamp = EventsUtil.setTimeStamp(collDate, "15", "45");
			collectionEvent.setTimestamp(timestamp);
			System.out.println("set collDate " + collDate);

		} catch (Exception e1) {
			System.out.println("exception in setSpecimenEvents");
			e1.printStackTrace();
		}

		collectionEvent.setSpecimen(specimen);
		specimenEventCollection.add(collectionEvent);

		// setting received event values

		if (receivedEvent == null)
			receivedEvent = new ReceivedEventParameters();

		receivedEvent.setReceivedQuality("Not Specified"); // c ltr
		User receivedEventUser = getUser("abrink@pathology.wustl.edu");
		receivedEvent.setUser(receivedEventUser);
		// }

		try {
			Date receivedTimestamp = EventsUtil.setTimeStamp(collDate, "15",
					"45");
			receivedEvent.setTimestamp(receivedTimestamp);
		} catch (Exception e) {
			System.out.println("exception in setSpecimenEvents");
			e.printStackTrace();
		}
		receivedEvent.setSpecimen(specimen);
		specimenEventCollection.add(receivedEvent);
		specimen.setSpecimenEventCollection(specimenEventCollection);

		evColl = specimen.getSpecimenEventCollection();
		if (evColl != null) {
			System.out.println("got eventColl from specimen ");
			Iterator itr = evColl.iterator();
			while (itr.hasNext()) {
				SpecimenEventParameters spEvPar = (SpecimenEventParameters) itr
						.next();
				if (spEvPar instanceof CollectionEventParameters) {
					System.out.println("in setting event "
							+ ((CollectionEventParameters) spEvPar).getUser()
									.getFirstName());
				} else if (spEvPar instanceof ReceivedEventParameters) {
					// ReceivedEventParameters) spEvPar;
				}
			}
		}

	}

	public SpecimenCollectionGroup createSCG(
			CollectionProtocolRegistration collectionProtocolRegistration) {
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;
		try {
			Collection collectionProtocolEventCollection = collectionProtocolRegistration
					.getCollectionProtocol()
					.getCollectionProtocolEventCollection();
			Iterator collectionProtocolEventIterator = collectionProtocolEventCollection
					.iterator();
			// User user = (User)TestCaseUtility.getObjectMap(User.class);
			User user = getUser("abrink@pathology.wustl.edu");
			while (collectionProtocolEventIterator.hasNext()) {
				CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent) collectionProtocolEventIterator
						.next();
				specimenCollectionGroup = new SpecimenCollectionGroup(
						collectionProtocolEvent);
				specimenCollectionGroup
						.setCollectionProtocolRegistration(collectionProtocolRegistration);
				specimenCollectionGroup
						.setConsentTierStatusCollectionFromCPR(collectionProtocolRegistration);

				LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory
						.getInstance("speicmenCollectionGroupLabelGeneratorClass");
				specimenCollectionGroupLableGenerator
						.setLabel(specimenCollectionGroup);

				Collection cloneSpecimenCollection = new LinkedHashSet();
				Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent
						.getSpecimenRequirementCollection();
				if (specimenCollection != null && !specimenCollection.isEmpty()) {
					Iterator itSpecimenCollection = specimenCollection
							.iterator();
					while (itSpecimenCollection.hasNext()) {
						SpecimenRequirement reqSpecimen = (SpecimenRequirement) itSpecimenCollection
								.next();
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
		} catch (Exception e) {
			System.err.println("Exception in create SCG");
			System.out.println("Exception in create SCG");
			e.printStackTrace();
		}
		return specimenCollectionGroup;
	}

	private Site createSite() throws Exception {

		// String collSite = excel[rowNo][9];
		Site site = new Site();
		// site.setId(new Long(700036)); // Siteman Cancer Center id
	    String name = "testSite";
		site.setName(name);
		List<?> resultList1 = null;
		try {
			String query = "from edu.wustl.catissuecore.domain.Site as site where "
				+ "site.name= '"+name+"'";	
			resultList1 = appService.search(query);
			if (resultList1.size() > 0) {
				System.out.println("No of Sites retrived from DB "
						+ resultList1.size());
				site = (Site) resultList1.get(0);
				System.out.println("site got");
			}
		} catch (Exception e1) {
			System.out.println("Exception in searching site");
			System.err.println("Exception in searching site");
			e1.printStackTrace();
			throw e1;
		}
		return site;
	}

}