package edu.wustl.catissuecore.testcase.bizlogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
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
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.AssignDataException;
import gov.nih.nci.common.util.HQLCriteria;

public class ShiftSpecimenInSCG extends CaTissueSuiteBaseTest {

	private static int rowNo = 1;
	private static int participantNo = rowNo;
	

	// static ApplicationService appService = null;
	private static Connection con = null;

	public void shift(String excel[][]) throws Exception {

		try {
			do {
				// System.out.println("Inside outer while participantNo "+
				// participantNo);
				SpecimenCollectionGroup specimenCollectioGroup = null;
				CollectionProtocolRegistration collectionProtocolRegistration = searchCPR(excel);
				do {
					try {
						// System.out.println("Inside inner While rowNo "+
						// rowNo);
						String newSCGLabel = excel[rowNo][13];
						// System.out.println("newSCGLabel " +newSCGLabel);
						if (collectionProtocolRegistration.getId() != null) {

							System.out.println("got specimenCollectioGroup "
									+ specimenCollectioGroup);
							if (specimenCollectioGroup == null
									|| !specimenCollectioGroup
											.getCollectionStatus().equals(
													"Complete")) {
								if (newSCGLabel != null || newSCGLabel != " ") {
									newSCGLabel = "'" + newSCGLabel + "'";
									// specimenCollectioGroup =
									// addSCGWithNameAndEvents
									// (collectionProtocolRegistration
									// ,specimenCollectioGroup, excel);
									specimenCollectioGroup = searchSCG(
											collectionProtocolRegistration,
											newSCGLabel);
									//disableSpecimenInSCG(specimenCollectioGroup
									// );

								}
							}
							if (specimenCollectioGroup != null) {
								System.out
										.println("specimenCollectioGroup is already created ");
								setNewSCGForSpecimnes(specimenCollectioGroup,
										excel);
							} else
								System.out
										.println("specimenCollectionGroup is null");
						} else {
							System.out
									.println("collectionProtocolRegistration not found");
						}
					} catch (Exception e) {
						System.out.println("Exception in inside while");
						System.err.println("Exception in inside while");
						e.printStackTrace();
						throw e;
					}

					rowNo++;
					System.out.println(" excel[rowNo][13] " + excel[rowNo][13]
							+ rowNo);
				} while (excel[rowNo][13] == null
						|| excel[rowNo][13].trim().equals(""));

				participantNo = rowNo;
				System.out.println("participantNo " + participantNo);
				try {
					closeConnection();
				} catch (Exception e) {
					System.out.println("Exceptionwhile closing connection");
					e.printStackTrace();
				}
			} while (participantNo < excel.length - 1);

		} catch (Exception e) {
			System.out.println("Exception in shift() out of participant while");
			e.printStackTrace();
			throw e;
		}
		System.out.println("Done rowNo " + rowNo);

	}

	private SpecimenCollectionGroup searchSCG(
			CollectionProtocolRegistration collectionProtocolRegistration,
			String newSCGLabel) throws Exception {
		// System.out.println(" in searchSCG newSCGlabel "+ newSCGLabel);
		HQLCriteria hqlcri = null;
		Collection<SpecimenCollectionGroup> scgList = new ArrayList();
		SpecimenCollectionGroup specimenCollectioGroup = null;

		try {
			String query = "select specimenCollectionGroup from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as specimenCollectionGroup where "
					+ "specimenCollectionGroup.collectionProtocolRegistration.id= "
					+ collectionProtocolRegistration.getId()
					+ " and specimenCollectionGroup.collectionProtocolEvent.collectionPointLabel = "
					+ newSCGLabel + "";

			// System.out.println("query "+ query);
			hqlcri = new HQLCriteria(query);
			//scgList = appService.query(hqlcri, Specimen.class.getName());
			scgList = appService.search(query);
			// System.out.println("No of scgs  " + scgList .size());
			Iterator<SpecimenCollectionGroup> scgItr = scgList.iterator();
			while (scgItr.hasNext()) {
				specimenCollectioGroup = (SpecimenCollectionGroup) scgItr
						.next();
			}
		} catch (Exception e) {
			System.out.println("Exception in searchSCG ");
			throw e;
		}
		return specimenCollectioGroup;
	}

	public SpecimenCollectionGroup addSCGWithNameAndEvents(
			CollectionProtocolRegistration collectionProtocolRegistration,
			SpecimenCollectionGroup specimenCollectionGroup, String[][] excel)
			throws Exception {
		System.out.println(" in addSCGWithNameAndEvents ");

		CollectionProtocolEvent collectionProtocolEvent = null; // specimenCollectionGroup
		// .
		// getCollectionProtocolEvent
		// ();
		String newSCGLabel = excel[rowNo][13];
		HQLCriteria hqlcri = null;
		Collection<CollectionProtocolEvent> cpeList = new ArrayList();
		// String studyPoint = excel[rowNo][4].trim();
		try {
			String query = "select collectionProtocolEvent from edu.wustl.catissuecore.domain.CollectionProtocolEvent as collectionProtocolEvent where "
					+ "collectionProtocolEvent.collectionProtocol.id= "
					+ collectionProtocolRegistration.getCollectionProtocol()
							.getId()
					+ " and collectionProtocolEvent.collectionPointLabel = '"
					+ newSCGLabel + "'";
			System.out.println("query " + query);
			hqlcri = new HQLCriteria(query);
			//cpeList = appService.query(hqlcri, Specimen.class.getName());
			cpeList = appService.search(query);
			System.out.println("No of cpe  " + cpeList.size());
			Iterator<CollectionProtocolEvent> cpeItr = cpeList.iterator();
			while (cpeItr.hasNext()) {
				collectionProtocolEvent = (CollectionProtocolEvent) cpeItr
						.next();
			}

			specimenCollectionGroup = (SpecimenCollectionGroup) createSCG(collectionProtocolEvent);
			if (specimenCollectionGroup != null) {
				// System.out.println("123 "+
				// specimenCollectionGroup.getName());
				specimenCollectionGroup.setName("abc");
				// System.out.println("123" +specimenCollectionGroup.getName());
				specimenCollectionGroup.setName(null);
				specimenCollectionGroup
						.setCollectionProtocolRegistration(collectionProtocolRegistration);
				System.out.println("set cpr in scg");
				setEventParametersAndSite(specimenCollectionGroup,
						collectionProtocolRegistration, excel);
				specimenCollectionGroup.setCollectionStatus("Complete");

				specimenCollectionGroup.setClinicalStatus("Not Specified");
				specimenCollectionGroup.setClinicalDiagnosis("Not Specified");
				// collectionProtocolEvent.setStudyCalendarEventPoint(Double.
				// parseDouble(studyPoint));
				SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
				specimenCollectionGroup = (SpecimenCollectionGroup) appService
						.createObject(specimenCollectionGroup,bean);
				System.out.println("specimenCollectionGroup created");
			}
			disableSpecimenInSCG(specimenCollectionGroup);
		} catch (Exception e) {
			System.out.println("Exception in addSCGWithNameAndEvents");
			throw e;
		}
		return specimenCollectionGroup;
	}

	private void disableSpecimenInSCG(
			SpecimenCollectionGroup specimenCollectionGroup) throws Exception {

		Collection<Specimen> speList = new ArrayList<Specimen>();
		HQLCriteria hqlcri = null;

		String query = "select specimen from edu.wustl.catissuecore.domain.Specimen as specimen where "
				+ "specimen.specimenCollectionGroup.id = "
				+ specimenCollectionGroup.getId();
		// System.out.println("in disableSpecimenInSCG label "+
		// specimenCollectionGroup
		// .getCollectionProtocolEvent().getCollectionPointLabel());

		System.out.println("query " + query);
		hqlcri = new HQLCriteria(query);
		//speList = appService.query(hqlcri, Specimen.class.getName());
		speList = appService.search(query);
		Iterator<Specimen> speItr = speList.iterator();
		System.out.println("No of  Specimens in SCG " + speList.size());
		while (speItr.hasNext()) {
			Specimen specimen = (Specimen) speItr.next();
			Connection con = getConnection();
			Statement stmt = con.createStatement();
			query = "update catissue_specimen  set activity_status = 'Disabled'  where identifier = "
					+ specimen.getId();
			// System.out.println(" update query "+ query);
			int noOfRowsUpdated = stmt.executeUpdate(query);
			con.commit();
			System.out.println("noOfRowsUpdated and commited "
					+ noOfRowsUpdated);
		}
	}

	public static Connection getConnection() throws Exception {
		String driver = null;
		String url = null;
		String username = null;
		String password = null;
		/*
		 * driver = "oracle.jdbc.driver.OracleDriver"; url =
		 * "jdbc:oracle:thin:@10.39.196.235:1521:clindb"; username =
		 * "suite_1_1_prod"; password ="dbsuite4adm";
		 */

		Class.forName(driver);
		if (con != null) {
			return con;
		}
		System.out.println("Making connection ");
		con = DriverManager.getConnection(url, username, password);
		return con;
	}

	private static void closeConnection() throws Exception {
		if (con != null) {
			System.out.println("Closing connection");
			con.close();
			con = null;
		}
	}

	private DisposalEventParameters createDisposeEvent(AbstractSpecimen specimen) {
		DisposalEventParameters disposalEvent = new DisposalEventParameters();
		disposalEvent.setSpecimen(specimen);
		disposalEvent.setReason("Specimen was not needed");
		disposalEvent.setTimestamp(new Date(System.currentTimeMillis()));
		User user = getUser("admin@admin.com");
		disposalEvent.setUser(user);
		disposalEvent.setActivityStatus(Constants.DISABLED);
		return disposalEvent;
	}

	private void setNewSCGForSpecimnes(
			SpecimenCollectionGroup specimenCollectionGroup, String[][] excel)
			throws Exception {

		String specimenId = excel[rowNo][7];
		Specimen specimen = new Specimen();
		// System.out.println("before specimenId "+ specimenId);
		if (specimenId.contains(".")) {
			// System.out.println("specimenId containes .");
			specimenId = specimenId.substring(0, specimenId.indexOf("."));
			System.out.println("after specimenId " + specimenId);
		}
		specimen.setId(Long.parseLong(specimenId));
		List<?> specList = null;
		try {
			String query = "from edu.wustl.catissuecore.domain.Specimen as specimen where "
				+ "specimen.id= "+specimenId;	
			specList = appService.search(query);
			System.out.println("in setNewSCGForSpecimnes specList.size() "
					+ specList.size());
			if (specList.size() > 0) {
				specimen = (Specimen) specList.get(0);
				System.out.println("New specimenCollectionGroup name is "
						+ specimenCollectionGroup.getCollectionProtocolEvent()
								.getCollectionPointLabel() + " id is "
						+ specimenCollectionGroup.getId());
				updateSpecimen(specimen, specimenCollectionGroup);
				System.out.println("specimen updated " + specimen);
				updateChildSpecimne(specimen, specimenCollectionGroup);
				System.out.println("All Specimens updated successfully");
			} else {
				System.out.println("Specimen not found " + specimenId);
			}
		} catch (Exception e1) {
			System.out.println("Exception in setNewSCGForSpecimnes");
			System.err.println("Exception in setNewSCGForSpecimnes");
			e1.printStackTrace();
			throw e1;
		}

	}

	private void updateSpecimen(AbstractSpecimen absSpecimen,
			SpecimenCollectionGroup specimenCollectionGroup) throws Exception {

		Specimen specimen = (Specimen) absSpecimen;

		// System.out.println("before.. scg in specimen is "+
		// specimen.getSpecimenCollectionGroup().getId());
		specimen.setSpecimenCollectionGroup(specimenCollectionGroup);
		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		Specimen newSpecimen = (Specimen) appService.updateObject(specimen,bean);
		// System.out.println("after..  scg in specimen is "+
		// newSpecimen.getSpecimenCollectionGroup().getId() + " label is "+
		// newSpecimen.getLabel());

		if (!specimen.getLineage().equals("New")) {
			System.out.println("Lineage " + specimen.getLineage());
			System.out
					.println("specimen is a child specimen so update parent specimen too");
			updateSpecimen(specimen.getParentSpecimen(),
					specimenCollectionGroup);
		}
	}

	private void updateChildSpecimne(AbstractSpecimen specimen,
			SpecimenCollectionGroup specimenCollectionGroup) throws Exception {

		Collection<Specimen> speList = new ArrayList<Specimen>();
		List<?> specList = null;
		HQLCriteria hqlcri = null;
		String query = "select specimen from edu.wustl.catissuecore.domain.Specimen as specimen where "
				+ "specimen.parentSpecimen = " + specimen.getId();
		// System.out.println("query "+ query);
		hqlcri = new HQLCriteria(query);
//		speList = appService.query(hqlcri, Specimen.class.getName());
		speList = appService.search(query);
		Iterator<Specimen> speItr = speList.iterator();
		// System.out.println("no of Child Specimens "+ speList.size());
		while (speItr.hasNext()) {
			Specimen childSpec = (Specimen) speItr.next();
			childSpec.setSpecimenCollectionGroup(specimenCollectionGroup);
			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			childSpec = (Specimen) appService.updateObject(childSpec,bean);
			System.out.println("child Specimen updated");
			updateChildSpecimne(childSpec, specimenCollectionGroup);
		}
	}

	private Collection serchSpeciemens(String[][] excel) {

		ArrayList specArr = new ArrayList();

		String specimenId = excel[rowNo][7];
		Specimen specimen = new Specimen();
		specimen.setId(Long.parseLong(specimenId));

		List<?> resultList = null;
		try {
			String query = "from edu.wustl.catissuecore.domain.Specimen as specimen where "
				+ "specimen.id= "+specimenId;	
			resultList = appService.search(query);
			if (resultList.size() > 0) {
				specimen = (Specimen) resultList.get(0);
				specArr.add(specimen);
				Specimen childSpecimen = new Specimen();
				childSpecimen.setParentSpecimen(specimen);
				 query = "from edu.wustl.catissuecore.domain.Specimen as specimen where "
					+ "specimen.parentSpecimen.id= "+specimen.getId();	
				resultList = null;
				resultList = appService.search(query);
				System.out
						.println("no of Child Specimens " + resultList.size());
				if (resultList.size() > 0) {
					specArr.add((Specimen) resultList.get(0));
				}
			}
			System.out.println("Total size of specimens " + specArr.size());
		} catch (Exception e1) {
			System.out.println("Exception in searching site");
			e1.printStackTrace();
		}

		return specArr;

	}

	/*
	 * private void setNewSCGForSpecimnes(Long scgId, String[][] excel) throws
	 * Exception {
	 * 
	 * String specimenId = excel[rowNo][7]; HQLCriteria hqlcri = null;
	 * Collection<SpecimenCollectionGroup> scgList = new ArrayList();
	 * 
	 * String query =
	 * "update edu.wustl.catissuecore.domain.Specimen as specimen" +
	 * "set specimen.specimenCollectionGroup.id = " + scgId +
	 * " where specimen.id = " + specimenId + " or specimen.id in " +
	 * " ( select id from edu.wustl.catissuecore.domain.AbstractSpecimen as abstractSpecimen where   "
	 * + " abstractSpecimen.parentSpecimen.id = " + specimenId + " )";
	 * System.out.println("update specimen query "+ query);
	 * 
	 * hqlcri = new HQLCriteria(query); scgList = appService.query(hqlcri,
	 * Specimen.class.getName()); System.out.println("No of scgs  " + scgList
	 * .size()); SpecimenCollectionGroup specimenCollectioGroup = null;
	 * Iterator<SpecimenCollectionGroup> scgItr = scgList.iterator();
	 * while(scgItr.hasNext()) { specimenCollectioGroup =
	 * (SpecimenCollectionGroup)scgItr.next(); } }
	 */

	private Site createSite(String[][] excel) {

		String collSite = "Memorial Health Univ. Medical Center";//excel[rowNo][
		// 9];
		Site site = new Site();
		site.setName(collSite);
		List<?> resultList1 = null;
		try {
			String query = "from edu.wustl.catissuecore.domain.Site as site where "
				+ "site.name= '"+collSite+"'";	
			resultList1 = appService.search(query);
			if (resultList1.size() > 0) {
				System.out.println("No of Sites retrived from DB "
						+ resultList1.size());
				site = (Site) resultList1.get(0);
				System.out.println("site got");
			}
		} catch (Exception e1) {
			System.out.println("Exception in searching site");
			e1.printStackTrace();
		}
		return site;
	}

	private CollectionProtocolRegistration searchCPR(String excel[][])
			throws Exception {

		String pPI = excel[rowNo][1].trim();
		String title = excel[rowNo][0].trim();
		String partLastName = excel[rowNo][2].trim();
		String partFirstName = excel[rowNo][3].trim();
		// System.out.println("pPI "+ pPI);
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		try {
			if (pPI.contains(".")) {
				// System.out.println("pPI containes .");
				pPI = pPI.substring(0, pPI.indexOf("."));
				// System.out.println("pPI "+ pPI);
			}
			HQLCriteria hqlcri = null;
			String query = "select collectionProtocolRegistration from edu.wustl.catissuecore.domain.CollectionProtocolRegistration as collectionProtocolRegistration where "
					+ "collectionProtocolRegistration.protocolParticipantIdentifier ='"
					+ pPI
					+ "' and collectionProtocolRegistration.participant.lastName = '"
					+ partLastName
					+ "' and collectionProtocolRegistration.participant.firstName ='"
					+ partFirstName
					+ "' and collectionProtocolRegistration.collectionProtocol.shortTitle ="
					+ "'" + title + "'";
			// System.out.println("query "+ query);
			hqlcri = new HQLCriteria(query);
			List list = appService.search(query);
			// System.out.println("No of events  " + eventList .size());
			if (list.size() > 0) {
				Iterator<CollectionProtocolRegistration> itr = list.iterator();

				while (itr.hasNext()) {
					collectionProtocolRegistration = (CollectionProtocolRegistration) itr
							.next();
					System.out.println(" collectionProtocolRegistration got");
				}
			} else {
				collectionProtocolRegistration = null;
			}

		} catch (Exception e) {
			System.out.println("Exception in searchCPR ");
			throw e;
		}

		return collectionProtocolRegistration;
	}

	public SpecimenCollectionGroup createSCG(
			CollectionProtocolEvent collectionProtocolEvent) {
		Map<Specimen, List<Specimen>> specimenMap = new LinkedHashMap<Specimen, List<Specimen>>();
		SpecimenCollectionGroup specimenCollectionGroup = null;
		try {

			User user = getUser("abrink@pathology.wustl.edu");
			specimenCollectionGroup = new SpecimenCollectionGroup(
					collectionProtocolEvent);
			specimenCollectionGroup
					.setCollectionProtocolRegistration(collectionProtocolEvent
							.getCollectionProtocolRegistration());
			// System.out.println("in createSCG scg label "+
			// specimenCollectionGroup
			// .getCollectionProtocolEvent().getCollectionPointLabel());

			LabelGenerator specimenCollectionGroupLableGenerator = LabelGeneratorFactory
					.getInstance("speicmenCollectionGroupLabelGeneratorClass");
			specimenCollectionGroupLableGenerator
					.setLabel(specimenCollectionGroup);

			// specimenCollectionGroupLableGenerator.setLabel(null);
			// System.out.println("specimenCollectionGroupLable is set");
			Collection cloneSpecimenCollection = new LinkedHashSet();
			Collection<SpecimenRequirement> specimenCollection = collectionProtocolEvent
					.getSpecimenRequirementCollection();
			if (specimenCollection != null && !specimenCollection.isEmpty()) {
				Iterator itSpecimenCollection = specimenCollection.iterator();
				while (itSpecimenCollection.hasNext()) {
					SpecimenRequirement reqSpecimen = (SpecimenRequirement) itSpecimenCollection
							.next();
					reqSpecimen.setActivityStatus("Disabled");
					if (reqSpecimen.getLineage().equalsIgnoreCase("new")) {
						Specimen cloneSpecimen = getCloneSpecimen(specimenMap,
								reqSpecimen, null, specimenCollectionGroup,
								user);
						LabelGenerator specimenLableGenerator = LabelGeneratorFactory
								.getInstance("specimenLabelGeneratorClass");
						specimenLableGenerator.setLabel(cloneSpecimen);
						cloneSpecimen
								.setSpecimenCollectionGroup(specimenCollectionGroup);
						cloneSpecimen.setActivityStatus("Disabled");
						cloneSpecimenCollection.add(cloneSpecimen);
					}
				}
			}

			specimenCollectionGroup
					.setSpecimenCollection(cloneSpecimenCollection);
		} catch (Exception e) {
			System.err.println("Exception in create SCG");
			System.out.println("Exception in create SCG");
			e.printStackTrace();
		}
		return specimenCollectionGroup;
	}

	private void setEventParametersAndSite(SpecimenCollectionGroup scgObj,
			CollectionProtocolRegistration collectionProtocolRegistration,
			String[][] excel) throws Exception {
		String collDate = excel[rowNo][12];
		// System.out.println("in setEventParametersAndSite colldate is "+
		// collDate);
		Date timestamp = EventsUtil.setTimeStamp(collDate, "15", "45");
		String scgName = "'" + excel[rowNo][6] + "'";// "Radiology (Baseline)";
		// System.out.println("in setEventParametersAndSite scgName  "+
		// scgName);
		SpecimenCollectionGroup scgBaseLine = searchSCG(
				collectionProtocolRegistration, scgName);
		CollectionEventParameters collectionEventBase = null;
		ReceivedEventParameters receivedEventBase = null;
		HQLCriteria hqlcri = null;
		Collection<SpecimenEventParameters> eventList = new ArrayList();
		Collection specimenEventParametersCollection = new HashSet();
		scgObj.setSpecimenCollectionSite(scgBaseLine
				.getSpecimenCollectionSite());

		String query = "select specimenEventParametersCollection from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as specimenCollectionGroup where "
				+ "specimenCollectionGroup.id = " + scgBaseLine.getId();

		// System.out.println("query "+ query);
		hqlcri = new HQLCriteria(query);
		eventList = appService.search(query);
		// System.out.println("No of events  " + eventList .size());
		Iterator<SpecimenEventParameters> itr = eventList.iterator();

		while (itr.hasNext()) {
			SpecimenEventParameters spEvPar = (SpecimenEventParameters) itr
					.next();
			if (spEvPar instanceof CollectionEventParameters) {
				collectionEventBase = (CollectionEventParameters) spEvPar;
			} else if (spEvPar instanceof ReceivedEventParameters) {
				receivedEventBase = (ReceivedEventParameters) spEvPar;
			}
		}

		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		collectionEventParameters.setCollectionProcedure(collectionEventBase
				.getCollectionProcedure());
		collectionEventParameters.setComment("");
		collectionEventParameters.setContainer(collectionEventBase
				.getContainer());
		try {
			collectionEventParameters.setTimestamp(timestamp);
		} catch (Exception e) {
			System.out
					.println("Exception while setting colection date in setEventParameters");
			e.printStackTrace();
		}

		User user = getUser("abrink@pathology.wustl.edu");
		collectionEventParameters.setUser(user);
		collectionEventParameters.setSpecimenCollectionGroup(scgObj);

		// Received Events
		receivedEventParameters.setComment("");
		User receivedUser = getUser("abrink@pathology.wustl.edu");
		receivedEventParameters.setUser(receivedUser);
		receivedEventParameters.setReceivedQuality("Not Specified");
		receivedEventParameters.setTimestamp(timestamp);
		receivedEventParameters.setSpecimenCollectionGroup(scgObj);

		specimenEventParametersCollection.add(collectionEventParameters);
		specimenEventParametersCollection.add(receivedEventParameters);
		// System.out.println("setting events");
		scgObj
				.setSpecimenEventParametersCollection(specimenEventParametersCollection);
		// System.out.println("set");
	}

	/*
	 * private void setEventParameters(SpecimenCollectionGroup sprObj, String
	 * excel[][], int row) { String collCotainer = "Not Specified"; String
	 * collProcedure = "Not Specified"; String collDate = excel[row][12];
	 * System.out.println("colldate "+ collDate); collDate = "10/12/2006";
	 * 
	 * System.out.println("Inside setEventParameters for SCG"); Collection
	 * specimenEventParametersCollection = new HashSet();
	 * CollectionEventParameters collectionEventParameters = new
	 * CollectionEventParameters(); ReceivedEventParameters
	 * receivedEventParameters = new ReceivedEventParameters();
	 * collectionEventParameters.setCollectionProcedure(collProcedure);
	 * collectionEventParameters.setComment("");
	 * collectionEventParameters.setContainer(collCotainer); try{ //Date date =
	 * new SimpleDateFormat("mm-dd-yyyy").parse(collDate); Date timestamp =
	 * EventsUtil.setTimeStamp(collDate,"15","45");
	 * collectionEventParameters.setTimestamp(timestamp);
	 * System.out.println("set collDate "+ timestamp); }catch(Exception e){
	 * System
	 * .out.println("Exception while setting colection date in setEventParameters"
	 * ); e.printStackTrace(); }
	 * 
	 * 
	 * User user = getUser("abrink@pathology.wustl.edu");
	 * collectionEventParameters.setUser(user);
	 * collectionEventParameters.setSpecimenCollectionGroup(sprObj);
	 * 
	 * //Received Events receivedEventParameters.setComment(""); User
	 * receivedUser = getUser("abrink@pathology.wustl.edu");
	 * receivedEventParameters.setUser(receivedUser);
	 * receivedEventParameters.setReceivedQuality("Not Specified"); Date
	 * receivedTimestamp = EventsUtil.setTimeStamp(collDate,"15","45");
	 * receivedEventParameters.setTimestamp(receivedTimestamp);
	 * receivedEventParameters.setSpecimenCollectionGroup(sprObj);
	 * specimenEventParametersCollection.add(collectionEventParameters);
	 * specimenEventParametersCollection.add(receivedEventParameters);
	 * sprObj.setSpecimenEventParametersCollection
	 * (specimenEventParametersCollection); }
	 */

	private User getUser(String loginName) {

		User user = new User();
		user.setLoginName(loginName);
		List<?> resultList1 = new LinkedList();
		try {
			String query = "from edu.wustl.catissuecore.domain.User as user where "
				+ "user.loginName= '"+loginName+"'";	
			List resultList = appService.search(query);
			if (resultList.size() > 0) {
				user = (User) resultList.get(0);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return user;
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

}
