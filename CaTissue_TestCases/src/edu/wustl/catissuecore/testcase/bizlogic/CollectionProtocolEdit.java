package edu.wustl.catissuecore.testcase.bizlogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CellSpecimenRequirement;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.FluidSpecimenRequirement;
import edu.wustl.catissuecore.domain.MolecularSpecimenRequirement;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

public class CollectionProtocolEdit extends CaTissueSuiteBaseTest implements java.io.Serializable {
	public static int specNo = 1;
	public static int eveNo = 1;
	public static int drvNo = 1;
	public static int cpNo = 1;
	public static Connection con = null;
	public static Collection<SpecimenRequirement> specimenCollection = null;

	static ApplicationService appService = null;

	public CollectionProtocolEdit() {
		super();
	}

	/**
	 * This Method Copies Collection Protocol that read from Excel sheet to
	 * String Array into Collection Protocol Object.
	 * 
	 */
	public void setCP(String ExcelCp[][]) throws Exception {
		try {
			appService = ApplicationServiceProvider.getApplicationService();

			CollectionProtocol collectionProtocol = null;
			Collection collectionProtocolEventList = new LinkedHashSet();
			CollectionProtocolEvent collectionProtocolEvent = null;
			List<?> cPResultList = null;
			if (ExcelCp != null) {
				System.out.println(" In setCP ... Total No of Rows in Excel "
						+ ExcelCp.length);

				while (cpNo < ExcelCp.length - 1) {

					System.out.println("In setCP .. while cpNo " + cpNo);
					// System.out.println("ExcelCp[cpNo][0] "+ExcelCp[cpNo][0]);
					do {
						if (!ExcelCp[cpNo][0].equals("")
								&& !ExcelCp[cpNo][0].equals("-")) {
							System.out
									.println("COLLECTION PROTOCOL START Short Title "
											+ ExcelCp[cpNo][0]);
							collectionProtocol = new CollectionProtocol();
							String cpShortTitle = ExcelCp[cpNo][0];
							collectionProtocol.setShortTitle(ExcelCp[cpNo][0]);
							String query = "from edu.wustl.catissuecore.domain.CollectionProtocol as collectionProtocol where "
								+ "collectionProtocol.shortTitle= '"+cpShortTitle+"'";	
							cPResultList = CollectionProtocolBizTestCases.appService.search(query);
//							cPResultList = CollectionProtocolTestCases.appService
//									.search(CollectionProtocol.class,
//											collectionProtocol);
							System.out.println("cPResultList.size() "
									+ cPResultList.size());
							if (cPResultList.size() != 0) {
								// System.out.println("No of CPs "+
								// ExcelCp[cpNo][0]
								// + "  retreived from database "+
								// cPResultList.size() );
								collectionProtocol = (CollectionProtocol) cPResultList
										.get(0);
								collectionProtocol.setEnrollment(3);
								collectionProtocol
										.setAliquotInSameContainer(new Boolean(
												true));
								collectionProtocol.setActivityStatus("Active");
								collectionProtocolEventList = new LinkedHashSet();
								// collectionProtocolEventList =
								// collectionProtocol.
								// getCollectionProtocolEventCollection();
								do {
									System.out.println("In setCp .. eveNo "
											+ eveNo);
									collectionProtocolEvent = setCollectionProtocolEvent(
											collectionProtocol, ExcelCp, eveNo);
									System.out
											.println("collectionProtocol.getId() "
													+ collectionProtocol
															.getId());
									collectionProtocolEvent
											.setCollectionProtocol(collectionProtocol);
									collectionProtocolEventList
											.add(collectionProtocolEvent);
								} while (ExcelCp[++eveNo][0].equals(""));
								System.out.println("ExcelCp[eveNo][1] "
										+ ExcelCp[eveNo][1]);
								// System.out.println(
								// "setting setCollectionProtocolEventCollection collectionProtocolEventList.size "
								// + collectionProtocolEventList.size());
								collectionProtocol
										.setCollectionProtocolEventCollection(collectionProtocolEventList);
							}
						}

					} while (ExcelCp[++cpNo][0] == "");
					eveNo = cpNo;
					drvNo = cpNo;
					specNo = cpNo;
					System.out.println("cpNo...............  " + cpNo);

					if (cPResultList.size() != 0) {
						System.out.println("updating");
						CollectionProtocolBizTestCases.appService
								.updateObject(collectionProtocol);//updateObject(
						// collectionProtocol
						// );//
					}
				} // while ends
			}
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					System.out.println("Exception while closing connection");
					e.printStackTrace();
				}
			}
		}

	}

	public static CollectionProtocolEvent setCollectionProtocolEvent(
			CollectionProtocol collectionProtocol, String ExcelCp[][], int row)
			throws Exception {
		CollectionProtocolEvent collectionProtocolEvent = getCollectionProtocolEvent(
				collectionProtocol, ExcelCp, row);
		SpecimenRequirement specimenRequirement = null;

		Double stydyEventPoint = new Double(ExcelCp[row][2]);
		System.out.println(" stydyEventPoint " + stydyEventPoint);
		collectionProtocolEvent.setStudyCalendarEventPoint(stydyEventPoint);
		collectionProtocolEvent.setClinicalStatus("Not Specified");

		// Collection<SpecimenRequirement> specimenCollection = new
		// LinkedHashSet<SpecimenRequirement>();
		List<SpecimenRequirement> specListDB = new ArrayList<SpecimenRequirement>();
		List<SpecimenRequirement> specEditedList = new ArrayList<SpecimenRequirement>();
		List<SpecimenRequirement> specDupList = new ArrayList<SpecimenRequirement>();

		if (collectionProtocolEvent.getId() != null) {
			Collection<SpecimenRequirement> specReqColl = getSpecReqColl(collectionProtocolEvent);
			if (specReqColl != null) {
				specListDB.addAll(specReqColl);
				specDupList.addAll(specReqColl);
			}
		}
		specimenCollection = new LinkedHashSet<SpecimenRequirement>();
		// System.out.println("creating specimenCollection ");
		do { // specimen starts
			specimenRequirement = new SpecimenRequirement();
			SpecimenRequirement specDO1 = null;
			SpecimenRequirement specDO = null;
			String parentClass = ExcelCp[specNo][6];
			String parentType = ExcelCp[specNo][8];
			if (parentClass == "" || parentClass == null)
				parentClass = ExcelCp[specNo][7].trim();
			if (parentType == "" || parentType == null)
				parentType = ExcelCp[specNo][9].trim();
			// System.out.println(" parentClass " + parentClass + " parentType "
			// + parentType);

			if ((!specListDB.isEmpty()) && specListDB.size() > 0) {
				// System.out.println("specListDB.size() "+ specListDB.size());
				Iterator spcItr = specListDB.iterator();
				while (spcItr.hasNext()) {
					specDO1 = (SpecimenRequirement) spcItr.next();
					System.out
							.println("specDO1.getClassName() "
									+ specDO1.getSpecimenClass()
									+ "  specDO.getType() "
									+ specDO1.getSpecimenType());
					if (specDO1.getClassName() != null
							&& specDO1.getClassName().equalsIgnoreCase(
									parentClass)
							&& specDO1.getSpecimenType() != null
							&& specDO1.getSpecimenType().equalsIgnoreCase(
									parentType)) {
						specDO = specDO1;
						System.out.println("Got matching specimenRequirement "
								+ specDO.getId());
						break;
					}
				}
				if (specDO != null) {
					specEditedList.add(specDO);
				}

				System.out.println("in setCollectionProtocolEvent .. specNo "
						+ specNo);
			}

			specimenRequirement = setSpecimen(specDO, ExcelCp, specNo,
					collectionProtocolEvent);
			specimenRequirement
					.setCollectionProtocolEvent(collectionProtocolEvent);
			specimenCollection.add(specimenRequirement);
			if (specDO != null) {
				System.out.println("specimen removing from list .. size "
						+ specListDB.size());
				boolean removed = specListDB.remove(specDO);
				System.out.println("size after removing an element "
						+ specListDB.size());
			}
			System.out.println("specNo " + specNo);
		} while (ExcelCp[++specNo][1] == null || ExcelCp[specNo][1].equals(""));

		/* for Deleting existing parent Specimen */
		System.out.println("  specDupList.size() " + specDupList.size());
		Iterator itr1 = specDupList.iterator();
		while (itr1.hasNext()) {
			SpecimenRequirement specDB = (SpecimenRequirement) itr1.next();
			if (!specEditedList.contains(specDB)) {
				System.out.println("dispose this specimen class "
						+ specDB.getSpecimenClass() + " type "
						+ specDB.getSpecimenType());
				if (specDB.getLineage().equals("New")) {
					System.out.println("specimen is new so disposing");
					// dispose child specimens too here
					// SpecimenRequirement sp = disposeSpecimen(specDB); //
					// commented while integrating with nightly build
					// specimenCollection.add(sp);
				}

			}
		}

		// System.out.println("size of specimenCollection in collectionEvent " +
		// specimenCollection.size());
		/*
		 * Iterator<SpecimenRequirement> srItr1 = specimenCollection.iterator();
		 * while(srItr1.hasNext()) { specimenRequirement =
		 * (SpecimenRequirement)srItr1.next();
		 * System.out.println("specimenRequirement in collectionEvent "+
		 * specimenRequirement.getSpecimenClass() +" "+
		 * specimenRequirement.getSpecimenType() + " "
		 * +specimenRequirement.getId()); }
		 */
		collectionProtocolEvent
				.setSpecimenRequirementCollection(specimenCollection);
		return collectionProtocolEvent;
	}

	public static CollectionProtocolEvent getCollectionProtocolEvent(
			CollectionProtocol collectionProtocol, String ExcelCp[][], int row)
			throws Exception {

		CollectionProtocolEvent collectionProtocolEvent = null;
		Collection<SpecimenRequirement> specReqColl = null;
		SpecimenRequirement specimenRequirement = null;
		String stydyEventPointLable = ExcelCp[row][1].trim();
		System.out.println("stydyEventPointLable " + stydyEventPointLable);
		Collection cpEveColl = collectionProtocol
				.getCollectionProtocolEventCollection();
		Iterator itr = cpEveColl.iterator();
		// System.out.println("cpEveColl.size() " +cpEveColl.size());
		while (itr.hasNext()) {
			collectionProtocolEvent = (CollectionProtocolEvent) itr.next();
			// System.out.println("CP event already presents in DB "+
			// (collectionProtocolEvent).getCollectionPointLabel());
			if ((collectionProtocolEvent.getCollectionPointLabel().trim()
					.toLowerCase()).equals(stydyEventPointLable.trim()
					.toLowerCase())) {
				System.out.println("CPEventfound .. CPEvent id is  "
						+ collectionProtocolEvent.getId());
				HQLCriteria hqlcri = null;
				specReqColl = new ArrayList();
				// p try {
				String query = "select specimenRequirementCollection from edu.wustl.catissuecore.domain.CollectionProtocolEvent as collectionProtocolEvent where "
						+ "collectionProtocolEvent.id= "
						+ collectionProtocolEvent.getId();

				// System.out.println("query "+ query);
				hqlcri = new HQLCriteria(query);
				specReqColl = appService.query(hqlcri,
						CollectionProtocolEvent.class.getName());
				System.out.println("No of specimenRequirements  "
						+ specReqColl.size());
				Iterator<SpecimenRequirement> srItr = specReqColl.iterator();
				while (srItr.hasNext()) {
					specimenRequirement = (SpecimenRequirement) srItr.next();
					// System.out.println("specimenRequirement "+
					// specimenRequirement.getSpecimenClass() +" "+
					// specimenRequirement.getSpecimenType() + " "
					// +specimenRequirement.getId());
				}
				// p }
				// pcatch (Exception e){
				// p e.printStackTrace();
				// p}
				break;
			} else {
				System.out.println("CPEvent not found .. So create new");
				collectionProtocolEvent = new CollectionProtocolEvent();
				collectionProtocolEvent
						.setCollectionPointLabel(stydyEventPointLable); // commented
				// after
				// productuin
				// upgradtion
				collectionProtocolEvent.setActivityStatus(Constants.DISABLED);
				collectionProtocolEvent.setClinicalDiagnosis("Not Specified");
				collectionProtocolEvent
						.setCollectionProtocol(collectionProtocol);
			}
		}
		return collectionProtocolEvent;
	}

	public static Collection getSpecReqColl(
			CollectionProtocolEvent collectionProtocolEvent) throws Exception {
		HQLCriteria hqlcri = null;
		Collection<SpecimenRequirement> specReqColl = new ArrayList<SpecimenRequirement>();
		SpecimenRequirement specimenRequirement = null;
		// p try {
		String query = "select specimenRequirementCollection from edu.wustl.catissuecore.domain.CollectionProtocolEvent as collectionProtocolEvent where "
				+ "collectionProtocolEvent.id= "
				+ collectionProtocolEvent.getId();

		// System.out.println("query "+ query);
		hqlcri = new HQLCriteria(query);
		specReqColl = appService.query(hqlcri, CollectionProtocolEvent.class
				.getName());
		System.out.println("No of specimenRequirements  " + specReqColl.size());
		Iterator<SpecimenRequirement> srItr = specReqColl.iterator();
		while (srItr.hasNext()) {
			specimenRequirement = (SpecimenRequirement) srItr.next();
			// System.out.println("specimenRequirement "+
			// specimenRequirement.getSpecimenClass() +" "+
			// specimenRequirement.getSpecimenType() + " "
			// +specimenRequirement.getId());
		}
		/*
		 * p} catch (Exception e){ e.printStackTrace(); }
		 */
		return specReqColl;
	}

	public static SpecimenRequirement disposeSpecimen(
			SpecimenRequirement specReq) throws Exception {

		// specReq.setAvailable(true);
		System.out.println("in disposeSpecimen");
		System.out.println("specimen id " + specReq.getId());
		System.out.println("specimen type " + specReq.getSpecimenType());
		System.out.println("specimen class " + specReq.getSpecimenClass());
		// System.out.println(DAOFactory.getInstance());
		con = getConnection();
		Statement stmt = con.createStatement();
		String query = "update catissue_cp_req_specimen "
				+ "set collection_protocol_event_id = null "
				+ "where identifier = " + specReq.getId();
		System.out.println(" update query " + query);
		int noOfRowsUpdated = stmt.executeUpdate(query);
		System.out.println("noOfRowsUpdated " + noOfRowsUpdated);
		query = "update catissue_specimen  " + "set req_specimen_id = null "
				+ "where req_specimen_id = " + specReq.getId();
		System.out.println(" update query " + query);
		try {
			noOfRowsUpdated = stmt.executeUpdate(query);
			System.out.println("noOfRowsUpdated " + noOfRowsUpdated);

			con.commit();
		} catch (Exception e) {
			System.out.println("Exception in Disposespecimen");
			con.rollback();
			throw e;
		}
		return specReq;
	}

	public static Connection getConnection() throws Exception {

		String driver = null;
		String url = null;
		String username = null;
		String password = null;
		if (con != null) {
			return con;
		}
		Class.forName(driver);
		con = DriverManager.getConnection(url, username, password);
		System.out.println(" In getConnection  made connection " + con);
		return con;
	}

	public static SpecimenRequirement setSpecimen(
			SpecimenRequirement specimenRequirement, String ExcelCp[][],
			int row, CollectionProtocolEvent collectionProtocolEvent)
			throws Exception {
		// Map deriveSpecimenMap = new LinkedHashMap();
		Collection specCollection = new LinkedHashSet();

		SpecimenRequirement parentSpecReq = null;
		String lineage = "New";

		specimenRequirement = createSpecimen(specimenRequirement,
				parentSpecReq, ExcelCp, row, lineage);

		/********************** Derivatives ******************/
		drvNo = row;
		// System.out.println("in setSpecimen drvNo " + drvNo);

		// if(ExcelCp[drvNo][17] != "0" && ExcelCp[drvNo][17] != "-" &&
		// ExcelCp[drvNo][17] != " " ) {
		do {
			String totaleDrv = ExcelCp[drvNo][14];
			System.out.println("totaleDrv " + totaleDrv);

			// System.out.println(" in setSpecimen ..  drvNo "+ drvNo +
			// " ExcelCp[drvNo][17] "+ ExcelCp[drvNo][17]);
			if (ExcelCp[drvNo][17] != null && !ExcelCp[drvNo][17].equals("0.0")
					&& !ExcelCp[drvNo][17].equals("")) {
				if (totaleDrv != null && totaleDrv != "") {
					for (int i = 0; i < Double.parseDouble(totaleDrv.trim()); i++) {
						// System.out.println("before size specimenCollection "+
						// specimenCollection.size());
						SpecimenRequirement drvSpecReq = getDerivedSpecimen(
								"Derived", specimenRequirement, ExcelCp, drvNo,
								collectionProtocolEvent);
						drvSpecReq
								.setCollectionProtocolEvent(collectionProtocolEvent);
						specimenCollection.add(drvSpecReq);
						// System.out.println("after size specimenCollection "+
						// specimenCollection.size());
						specCollection.add(drvSpecReq);
					}
				}

			} else if (!ExcelCp[drvNo][20].equals("")
					&& !ExcelCp[drvNo][20].equals("0.0")) {
				// System.out.println("in setSpecimen creating Aliquots drvNO "
				// + drvNo);
				System.out.println("in setSpecimen .. no of Aliquots  "
						+ ExcelCp[drvNo][20]);
				LinkedHashSet hashset = (LinkedHashSet) getAliquotsSpecimen(
						"Aliquot", specimenRequirement, ExcelCp, drvNo,
						collectionProtocolEvent);
				specCollection.addAll(hashset);
				// specimenRequirement..setNoOfAliquots(hashset.size());
				// System.out.println("created aliquots ");
			}
			// }
			// }
			specNo = drvNo;
			eveNo = drvNo;
			cpNo = drvNo;
			// System.out.println(" in setSpecimen drvNo "+ drvNo);
			// System.out.println("ExcelCp[drvNo + 1][4] "+ExcelCp[drvNo +
			// 1][4]);
			// }
			// }
		} while (ExcelCp[++drvNo][4] == null || ExcelCp[drvNo][4] == ""
				|| ExcelCp[drvNo][4].equals(""));
		// System.out.println("out of while in setSpecimen");
		// }

		if (!specCollection.equals(null)) {
			specimenRequirement.setChildSpecimenCollection(specCollection);
		}
		return specimenRequirement;
	}

	public static SpecimenRequirement getDerivedSpecimen(String lineage,
			SpecimenRequirement parentSpecReq, String ExcelCp[][], int row,
			CollectionProtocolEvent collectionProtocolEvent) throws Exception {
		// System.out.println(
		// "in getDrived  parentSpecimen.getSpecimenCollectionGroup() "+
		// parentSpecimen.getSpecimenCollectionGroup());

		Collection specCollection = new LinkedHashSet();
		SpecimenRequirement specReq = new SpecimenRequirement();
		specReq = createSpecimen(specReq, parentSpecReq, ExcelCp, row,
				"Derived");

		String drvInitQunt = ExcelCp[row][23];
		// System.out.println("ExcelCp[row][23] " + ExcelCp[row][23]);

		if (drvInitQunt == null || drvInitQunt == "") {
			drvInitQunt = "0";
		} else if (drvInitQunt.contains("ml")) {
			drvInitQunt = drvInitQunt.split("ml")[0].trim();
		}
		// System.out.println("drvInitQunt "+ drvInitQunt);
		Double initialQuantity = Double.parseDouble(drvInitQunt); // / Double.
		// parseDouble
		// (ExcelCp[
		// row
		// ][23]);
		specReq.setInitialQuantity(initialQuantity);
		// System.out.println("after create specimen row  "+ row);
		// specReq.setSpecimenCollectionGroup(parentSpecReq.
		// getSpecimenCollectionGroup());
		if (ExcelCp[row][20] != null && !ExcelCp[row][20].equals("")
				&& !ExcelCp[row][20].equals("0.0")) {
			// System.out.println("in getDerivedSpecimen .. no of Aliquots  "+
			// ExcelCp[row][20]);
			LinkedHashSet hashset = (LinkedHashSet) getAliquotsSpecimen(
					"Aliquot", specReq, ExcelCp, row, collectionProtocolEvent);
			specCollection.addAll(hashset);
		}

		specReq.setChildSpecimenCollection(specCollection);
		specReq.setId(null);
		return specReq;
	}

	public static Collection getAliquotsSpecimen(String lineage,
			SpecimenRequirement parentSpecReq, String ExcelCp[][], int row,
			CollectionProtocolEvent collectionProtocolEvent) throws Exception {
		// System.out.println("In getAliquotsSpecimen");
		Collection<SpecimenRequirement> specColl = new LinkedHashSet<SpecimenRequirement>();
		String noOfAliquotes = ExcelCp[row][20].trim();
		String quantityPerAliquot = ExcelCp[row][24].trim();
		Double aliquotCount = Double.parseDouble(noOfAliquotes);

		if (quantityPerAliquot.contains("ml")) {
			quantityPerAliquot = quantityPerAliquot.split("ml")[0].trim();
		}
		// System.out.println(" quantityPerAliquot "+ quantityPerAliquot);
		Double aliquotQuantity = 0D;
		aliquotQuantity = Double.parseDouble(quantityPerAliquot);

		try {
			for (int i = 1; i <= aliquotCount; i++) {
				SpecimenRequirement specReq = new SpecimenRequirement();
				specReq = createSpecimen(specReq, parentSpecReq, ExcelCp, row,
						lineage);
				if (quantityPerAliquot == null || quantityPerAliquot.equals("")) {
					quantityPerAliquot = "0";
				}
				specReq.setInitialQuantity(aliquotQuantity);
				// System.out.println("in getAliquotsSpecimen aliquot " +
				// specReq.getSpecimenType()+ " created " +
				// specReq.getSpecimenType() );
				specReq.setCollectionProtocolEvent(collectionProtocolEvent);
				specimenCollection.add(specReq);
				specColl.add(specReq);
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Exception in getChildSpecimenMap" + ex);
		}
		return specColl;
	}

	private static SpecimenRequirement createSpecimen(
			SpecimenRequirement specReq, SpecimenRequirement parentSpecReq,
			String ExcelCp[][], int row, String lineage) throws Exception {
		// System.out.println("in createSpecimen .. specimen  "+ specReq );
		// System.out.println("in createSpecimen .. lineage  "+ lineage );
		if (lineage.equals("New")) {
			if (specReq == null) {
				System.out.println("create new object " + ExcelCp[row][7]);

				if (ExcelCp[row][7].equals("Tissue")) {
					specReq = new TissueSpecimenRequirement();
					specReq.setSpecimenClass("Tissue");
				} else if (ExcelCp[row][7].equals("Fluid")) {
					specReq = new FluidSpecimenRequirement();
					specReq.setSpecimenClass("Fluid");
				} else if (ExcelCp[row][7].equals("Cell")) {
					specReq = new CellSpecimenRequirement();
					specReq.setSpecimenClass("Cell");
				} else if (ExcelCp[row][7].equals("Molecular")) {
					specReq = new MolecularSpecimenRequirement();
					specReq.setSpecimenClass("Molecular");
				}

				specReq.setInitialQuantity(new Double(0.0));
			}
		} else if (lineage.equals("Aliquot")
				&& parentSpecReq.getLineage().equals("New")) {
			// System.out.println("class "+ExcelCp[row][7] );
			// System.out.println(" parentSpecimen.getLineage() "+
			// parentSpecReq.getLineage());
			if (ExcelCp[row][7].equals("Tissue")) {
				specReq = new TissueSpecimenRequirement();
				specReq.setSpecimenClass("Tissue");
			} else if (ExcelCp[row][7].equals("Fluid")) {
				specReq = new FluidSpecimenRequirement();
				specReq.setSpecimenClass("Fluid");
			} else if (ExcelCp[row][7].equals("Cell")) {
				specReq = new CellSpecimenRequirement();
				specReq.setSpecimenClass("Cell");
			} else if (ExcelCp[row][7].equals("Molecular")) {
				specReq = new MolecularSpecimenRequirement();
				specReq.setSpecimenClass("Molecular");
			}
			specReq.setSpecimenType(ExcelCp[row][9].trim());
			System.out.println("ExcelCp[row][23]) " + ExcelCp[row][23] + " "
					+ lineage);
			/* not tested */parentSpecReq.setInitialQuantity(new Double(
					ExcelCp[row][23]));
		} else {
			// System.out.println("createSpeciemn in else  "+ ExcelCp[row][17]);

			if (ExcelCp[row][17].equals("Tissue")) {
				specReq = new TissueSpecimenRequirement();
				specReq.setSpecimenClass("Tissue");
			} else if (ExcelCp[row][17].equals("Fluid")) {
				specReq = new FluidSpecimenRequirement();
				specReq.setSpecimenClass("Fluid");
			} else if (ExcelCp[row][17].equals("Cell")) {
				specReq = new CellSpecimenRequirement();
				specReq.setSpecimenClass("Cell");
			} else if (ExcelCp[row][17].equals("Molecular")) {
				specReq = new MolecularSpecimenRequirement();
				specReq.setSpecimenClass("Molecular");
			}
			specReq.setSpecimenType(ExcelCp[row][18].trim());
		}
		if (parentSpecReq != null) {
			// System.out.println("in createSpecimen .. setting parent in "+
			// lineage);
			specReq.setParentSpecimen(parentSpecReq);
		}
		specReq.setLineage(lineage);
		String storageType = null;

		if (lineage.equals("New")) {
			storageType = ExcelCp[row][5].trim();
			SpecimenCharacteristics specimenCharacteristics = specReq
					.getSpecimenCharacteristics();
			if (specReq.getId() == null && specimenCharacteristics == null) {
				specimenCharacteristics = new SpecimenCharacteristics();
				specimenCharacteristics.setTissueSide("Not Specified");
			}
			// System.out.println("Tissue Site "+ ExcelCp[row][10]);
			specimenCharacteristics.setTissueSite(ExcelCp[row][10]);

			specReq.setSpecimenCharacteristics(specimenCharacteristics);

			specReq.setSpecimenType(ExcelCp[row][9].trim());
			// System.out.println("createSpecimen "+ lineage + " class " +
			// specReq.getSpecimenClass() + " type "+ specReq.getSpecimenType()
			// + " row " + row);
			// setSpecimenEvents(specimen, ExcelCp, row);

			Collection evColl = specReq.getSpecimenEventCollection();

			if (evColl != null) {
				Iterator itr = evColl.iterator();
				while (itr.hasNext()) {
					SpecimenEventParameters spEvPar = (SpecimenEventParameters) itr
							.next();
					if (spEvPar instanceof CollectionEventParameters) {
						// System.out.println("after setSpecimenEvents "+
						// ((CollectionEventParameters)
						// spEvPar).getCollectionProcedure());
						// System.out.println("after setSpecimenEvents "+
						// ((CollectionEventParameters)
						// spEvPar).getContainer());
					}
				}
			}
			if (ExcelCp[row][11] != null)
				specReq.setPathologicalStatus(ExcelCp[row][11].trim()); // //
			// just
			// uncommented
			else
				specReq.setPathologicalStatus(ExcelCp[row][11]);
		} else if (lineage.equals("Derived")) {
			storageType = ExcelCp[row][19].trim();
			specReq.setSpecimenCharacteristics(specReq.getParentSpecimen()
					.getSpecimenCharacteristics());
			specReq.setPathologicalStatus(specReq.getParentSpecimen()
					.getPathologicalStatus());
			// System.out.println("createSpecimen "+ lineage + " class " +
			// specReq.getSpecimenClass() + " type "+ specReq.getSpecimenType()
			// + " row " + row);
		} else if (lineage.equals("Aliquot")) {
			// System.out.println("storageType "+ ExcelCp[row][25]);
			storageType = ExcelCp[row][25].trim();
			specReq.setSpecimenCharacteristics(specReq.getParentSpecimen()
					.getSpecimenCharacteristics());

			specReq.setPathologicalStatus(specReq.getParentSpecimen()
					.getPathologicalStatus());
			// System.out.println("createSpecimen "+ lineage + " class " +
			// specReq.getSpecimenClass() + " type "+ specReq.getSpecimenType()+
			// " row " + row);
			// specimen.setSpecimenEventCollection(specimen.getParentSpecimen().
			// getSpecimenEventCollection());
		}
		setSpecimenEvents(specReq, ExcelCp, row);

		if (storageType.equalsIgnoreCase("A/M")) {
			storageType = "Auto";
			StorageContainer storageContainer = new StorageContainer();
			storageContainer.setId(20374L);
		} else {
			// specReq.setStorageContainer(null);
		}
		specReq.setStorageType(storageType);
		specReq.setActivityStatus(Constants.DISABLED);
		// System.out.println("createspecimen end");

		return specReq;
	}

	private static void setSpecimenEvents(SpecimenRequirement specReq,
			String ExcelCp[][], int row) throws Exception {
		// seting collection event values
		Collection<SpecimenEventParameters> specimenEventCollection = new LinkedHashSet<SpecimenEventParameters>();
		CollectionEventParameters collectionEvent = null;
		ReceivedEventParameters receivedEvent = null;

		String collectionProcedure = null;
		String collectionEventContainer = "Not Specified";
		if (ExcelCp[row][13].indexOf(",") != -1) {
			collectionEventContainer = (ExcelCp[row][13].split(",")[1]).trim();
			// System.out.println(
			// " in setSpecimenEvents collectionEventContainer "+
			// collectionEventContainer);
			collectionProcedure = (ExcelCp[row][13].split(",")[0]).trim();
		} else {
			collectionProcedure = (ExcelCp[row][13]).trim();
		}

		Collection evColl = specReq.getSpecimenEventCollection();
		if (evColl != null) {
			Iterator itr = evColl.iterator();
			while (itr.hasNext()) {
				// System.out.println("Events allready exist for specimenReq "+
				// specReq.getId());
				SpecimenEventParameters spEvPar = (SpecimenEventParameters) itr
						.next();
				if (spEvPar instanceof CollectionEventParameters) {
					collectionEvent = (CollectionEventParameters) spEvPar;
				} else if (spEvPar instanceof ReceivedEventParameters) {
					receivedEvent = (ReceivedEventParameters) spEvPar;
				}
			}
		}

		if (collectionEvent == null) {
			//System.out.println("creating new collectionEvent for specimenReq "
			// + specReq.getId());
			collectionEvent = new CollectionEventParameters();
			User collectionEventUser = new User();
			collectionEventUser.setId(new Long(1)); // c ltr
			collectionEvent.setUser(collectionEventUser);
			collectionEvent.setId(null);
		}
		// System.out.println(" collectionProcedure setting " +
		// collectionProcedure);
		// System.out.println(" collectionEventContainer  setting " +
		// collectionEventContainer);
		if (!specReq.getLineage().equals("New")) {
			// System.out.println(" in setSpecimenEventsnot a new specimen");
			Collection parentSpcEvColl = specReq.getParentSpecimen()
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
			collectionEvent.setCollectionProcedure(collectionProcedure);
			collectionEvent.setContainer(collectionEventContainer);
		}
		collectionEvent.setSpecimen(specReq);
		specimenEventCollection.add(collectionEvent);

		// setting received event values

		if (receivedEvent == null) {
			receivedEvent = new ReceivedEventParameters();
			receivedEvent.setReceivedQuality("Not Specified"); // c ltr
			User receivedEventUser = new User();
			receivedEventUser.setId(new Long(1));
			receivedEvent.setUser(receivedEventUser);
			receivedEvent.setId(null);
		}
		receivedEvent.setSpecimen(specReq);
		specimenEventCollection.add(receivedEvent);
		specReq.setSpecimenEventCollection(specimenEventCollection);
	}

}
