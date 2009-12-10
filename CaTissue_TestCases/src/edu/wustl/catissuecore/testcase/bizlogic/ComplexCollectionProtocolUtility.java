package edu.wustl.catissuecore.testcase.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Status;


public class ComplexCollectionProtocolUtility
{

	public static CollectionProtocol initComplexCollectionProtocol()
	{
		CollectionProtocol collectionProtocol = createComplexCollectionProtocolObject();
		return collectionProtocol;
	}

	
	private static CollectionProtocol createComplexCollectionProtocolObject()
	{
		CollectionProtocol collectionProtocol = new CollectionProtocol();
	    collectionProtocol.setSequenceNumber(new Integer(0));
	    collectionProtocol.setStudyCalendarEventPoint(new Double(0));
		
		String title = "Osteosarcoma-2"+UniqueKeyGeneratorUtil.getUniqueKey();
		String shortTitle = "Osteosarcoma-2"+UniqueKeyGeneratorUtil.getUniqueKey();
		collectionProtocol = setAttributesOfCP(collectionProtocol,title,shortTitle,"Parent",null);
		
		List specimenEventList = new ArrayList();
		List studyCalEvtPointList = new ArrayList();
		collectionProtocol = setCollectionProtocolEventList(collectionProtocol,specimenEventList,studyCalEvtPointList);
		collectionProtocol = createParentChild(collectionProtocol);
		return collectionProtocol;
		
		
	}
		private static CollectionProtocol createParentChild(CollectionProtocol collectionProtocol)
	{
		Collection childCPCollection = new LinkedHashSet();
		
		String title = null;
		String shortTitle = null;
		List specimenEventList = null;
		List studyCalEvtPointList = null;
		//Phase 1
		CollectionProtocol phaseCP1 = new CollectionProtocol();
		phaseCP1.setSequenceNumber(new Integer(0));
		title = "Osteo Pre-Surgery-2"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Osteo Pre-Surgery-2"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP1 = setAttributesOfCP(phaseCP1,title,shortTitle,"Phase","1");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 4");
		specimenEventList.add("Wk 5");
		specimenEventList.add("Wk 6");
		specimenEventList.add("Wk 9");
		specimenEventList.add("Wk 10");
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("7");
		studyCalEvtPointList.add("28");
		studyCalEvtPointList.add("32");
		studyCalEvtPointList.add("39");
		studyCalEvtPointList.add("59");
		studyCalEvtPointList.add("65");
		phaseCP1 = setCollectionProtocolEventList(phaseCP1,specimenEventList,studyCalEvtPointList);
		phaseCP1.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP1);

		
		CollectionProtocol phaseCP2 = new CollectionProtocol();
		phaseCP2.setSequenceNumber(new Integer(1));
		title = "Osteo Pose Surgery-2"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Osteo Post Surgery-2"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP2 = setAttributesOfCP(phaseCP2,title,shortTitle,"Phase","80");
		specimenEventList = new ArrayList();
		studyCalEvtPointList = new ArrayList();
		phaseCP2 = setCollectionProtocolEventList(phaseCP2,specimenEventList,studyCalEvtPointList);
		phaseCP2.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP2);

		Collection childPhse2Collection = new LinkedHashSet();
			
			CollectionProtocol arm1 = new CollectionProtocol();
			arm1.setSequenceNumber(new Integer(0));
			title = "Post GR MAP-2"+UniqueKeyGeneratorUtil.getUniqueKey();
			shortTitle = "Post GR MAP-2"+UniqueKeyGeneratorUtil.getUniqueKey();
			arm1 = setAttributesOfCP(arm1,title,shortTitle,"Arm","0");
			specimenEventList = new ArrayList();
			specimenEventList.add("Wk 12");
			specimenEventList.add("Wk 15");
			specimenEventList.add("Wk 16");
			specimenEventList.add("Wk 17");
			specimenEventList.add("Wk 20");
			specimenEventList.add("Wk 21");
			specimenEventList.add("Wk 22");
			specimenEventList.add("Wk 24");
			specimenEventList.add("Wk 25");
			specimenEventList.add("Wk 26");
			specimenEventList.add("Wk 28");
			specimenEventList.add("Wk 29");
			
			
			studyCalEvtPointList = new ArrayList();
			studyCalEvtPointList.add("0");
			studyCalEvtPointList.add("21");
			studyCalEvtPointList.add("27");
			studyCalEvtPointList.add("35");
			studyCalEvtPointList.add("55");
			studyCalEvtPointList.add("61");
			studyCalEvtPointList.add("67");
			studyCalEvtPointList.add("87");
			studyCalEvtPointList.add("92");
			studyCalEvtPointList.add("97");
			studyCalEvtPointList.add("107");
			studyCalEvtPointList.add("112");
			
			
			arm1 = setCollectionProtocolEventList(arm1,specimenEventList,studyCalEvtPointList);
			arm1.setParentCollectionProtocol(phaseCP2);
			childPhse2Collection.add(arm1);
			
			CollectionProtocol arm2 = new CollectionProtocol();
			arm2.setSequenceNumber(new Integer(0));
			title = "Post GR MAPfin-2"+UniqueKeyGeneratorUtil.getUniqueKey();
			shortTitle = "Post GR MAPfin-2"+UniqueKeyGeneratorUtil.getUniqueKey();
			arm2 = setAttributesOfCP(arm2,title,shortTitle,"Arm","0");
			specimenEventList = new ArrayList();
			specimenEventList.add("Wk 12");
			specimenEventList.add("Wk 15");
			specimenEventList.add("Wk 16");
			specimenEventList.add("Wk 17");
			specimenEventList.add("Wk 20");
			specimenEventList.add("Wk 21");
			specimenEventList.add("Wk 22");
			specimenEventList.add("Wk 24");
			specimenEventList.add("Wk 25");
			specimenEventList.add("Wk 26");
			specimenEventList.add("Wk 28");
			specimenEventList.add("Wk 29");
			specimenEventList.add("Wk 30");
			specimenEventList.add("Wk 33");
			specimenEventList.add("Wk 34");
			specimenEventList.add("Wk 35");
			
			studyCalEvtPointList = new ArrayList();
			studyCalEvtPointList.add("0");
			studyCalEvtPointList.add("21");
			studyCalEvtPointList.add("27");
			studyCalEvtPointList.add("35");
			studyCalEvtPointList.add("55");
			studyCalEvtPointList.add("61");
			studyCalEvtPointList.add("67");
			studyCalEvtPointList.add("87");
			studyCalEvtPointList.add("92");
			studyCalEvtPointList.add("97");
			studyCalEvtPointList.add("107");
			studyCalEvtPointList.add("112");
			studyCalEvtPointList.add("117");
			studyCalEvtPointList.add("137");
			studyCalEvtPointList.add("142");
			studyCalEvtPointList.add("149");
			arm2 = setCollectionProtocolEventList(arm2,specimenEventList,studyCalEvtPointList);
			arm2.setParentCollectionProtocol(phaseCP2);
			childPhse2Collection.add(arm2);
			
			CollectionProtocol arm3 = new CollectionProtocol();
			arm3.setSequenceNumber(new Integer(0));
			title = "POST PR MAP-2"+UniqueKeyGeneratorUtil.getUniqueKey();
			shortTitle = "POST PR MAP-2"+UniqueKeyGeneratorUtil.getUniqueKey();
			arm3 = setAttributesOfCP(arm3,title,shortTitle,"Arm","0");
			specimenEventList = new ArrayList();
			specimenEventList.add("Wk 12");
			specimenEventList.add("Wk 15");
			specimenEventList.add("Wk 16");
			specimenEventList.add("Wk 17");
			specimenEventList.add("Wk 20");
			specimenEventList.add("Wk 21");
			specimenEventList.add("Wk 22");
			specimenEventList.add("Wk 24");
			specimenEventList.add("Wk 25");
			specimenEventList.add("Wk 26");
			specimenEventList.add("Wk 28");
			specimenEventList.add("Wk 29");
			
			
			studyCalEvtPointList = new ArrayList();
			studyCalEvtPointList.add("0");
			studyCalEvtPointList.add("21");
			studyCalEvtPointList.add("27");
			studyCalEvtPointList.add("35");
			studyCalEvtPointList.add("55");
			studyCalEvtPointList.add("61");
			studyCalEvtPointList.add("67");
			studyCalEvtPointList.add("87");
			studyCalEvtPointList.add("92");
			studyCalEvtPointList.add("97");
			studyCalEvtPointList.add("107");
			studyCalEvtPointList.add("112");
			arm3 = setCollectionProtocolEventList(arm3,specimenEventList,studyCalEvtPointList);
			arm3.setParentCollectionProtocol(phaseCP2);
			childPhse2Collection.add(arm3);
			
			
			CollectionProtocol arm4 = new CollectionProtocol();
			arm4.setSequenceNumber(new Integer(0));
			title = "POST PR MAPIE-2"+UniqueKeyGeneratorUtil.getUniqueKey();
			shortTitle = "POST PR MAPIE-2"+UniqueKeyGeneratorUtil.getUniqueKey();
			arm4 = setAttributesOfCP(arm4,title,shortTitle,"Arm","0");
			specimenEventList = new ArrayList();
			studyCalEvtPointList = new ArrayList();
			
			arm4 = setCollectionProtocolEventList(arm4,specimenEventList,studyCalEvtPointList);
			arm4.setParentCollectionProtocol(phaseCP2);
			childPhse2Collection.add(arm4);
			
			
			
			Collection childArm4Collection = new LinkedHashSet();
			
				CollectionProtocol cycle1 = new CollectionProtocol();
				cycle1.setSequenceNumber(new Integer(0));
				title = "Cycle1-2"+UniqueKeyGeneratorUtil.getUniqueKey();
				shortTitle = "Cycle1-2"+UniqueKeyGeneratorUtil.getUniqueKey();
				cycle1 = setAttributesOfCP(cycle1,title,shortTitle,"Cycle","0");
				specimenEventList = new ArrayList();
				specimenEventList.add("Wk 12");
				specimenEventList.add("Wk 15");
				specimenEventList.add("Wk 16");
				specimenEventList.add("Wk 19");
		
				studyCalEvtPointList = new ArrayList();
				studyCalEvtPointList.add("0");
				studyCalEvtPointList.add("21");
				studyCalEvtPointList.add("27");
				studyCalEvtPointList.add("47");
				cycle1 = setCollectionProtocolEventList(cycle1,specimenEventList,studyCalEvtPointList);
				cycle1.setParentCollectionProtocol(arm4);
				childArm4Collection.add(cycle1);
				
				CollectionProtocol cycle2 = new CollectionProtocol();
				cycle2.setSequenceNumber(new Integer(1));
				title = "Cycle2-2"+UniqueKeyGeneratorUtil.getUniqueKey();
				shortTitle = "Cycle2-2"+UniqueKeyGeneratorUtil.getUniqueKey();
				cycle2 = setAttributesOfCP(cycle2,title,shortTitle,"Cycle","52");
				specimenEventList = new ArrayList();
				specimenEventList.add("Wk 20");
				specimenEventList.add("Wk 23");
				specimenEventList.add("Wk 24");
				specimenEventList.add("Wk 27");
				studyCalEvtPointList = new ArrayList();
				studyCalEvtPointList.add("0");
				studyCalEvtPointList.add("21");
				studyCalEvtPointList.add("28");
				studyCalEvtPointList.add("48");
				
				cycle2 = setCollectionProtocolEventList(cycle2,specimenEventList,studyCalEvtPointList);
				cycle2.setParentCollectionProtocol(arm4);
				childArm4Collection.add(cycle2);
				
				CollectionProtocol cycle3 = new CollectionProtocol();
				cycle3.setSequenceNumber(new Integer(2));
				title = "Cycle3-2"+UniqueKeyGeneratorUtil.getUniqueKey();
				shortTitle = "Cycle3-2"+UniqueKeyGeneratorUtil.getUniqueKey();
				cycle3 = setAttributesOfCP(cycle3,title,shortTitle,"Cycle","92");
				specimenEventList = new ArrayList();
				specimenEventList.add("Wk 28");
				specimenEventList.add("Wk 31");
				specimenEventList.add("Wk 32");
				specimenEventList.add("Wk 35");
				studyCalEvtPointList = new ArrayList();
				studyCalEvtPointList.add("0");
				studyCalEvtPointList.add("21");
				studyCalEvtPointList.add("28");
				studyCalEvtPointList.add("48");
				
				
				cycle3 = setCollectionProtocolEventList(cycle3,specimenEventList,studyCalEvtPointList);
				cycle3.setParentCollectionProtocol(arm4);
				childArm4Collection.add(cycle3);
				
				CollectionProtocol cycle4 = new CollectionProtocol();
				cycle4.setSequenceNumber(new Integer(3));
				title = "Cycle4-2"+UniqueKeyGeneratorUtil.getUniqueKey();
				shortTitle = "Cycle4-2"+UniqueKeyGeneratorUtil.getUniqueKey();
				cycle4 = setAttributesOfCP(cycle4,title,shortTitle,"Cycle","215");
				specimenEventList = new ArrayList();
				specimenEventList.add("Wk 36");
				specimenEventList.add("Wk 39");
				specimenEventList.add("Wk 40");
				studyCalEvtPointList = new ArrayList();
				studyCalEvtPointList.add("0");
				studyCalEvtPointList.add("21");
				studyCalEvtPointList.add("28");
				cycle4 = setCollectionProtocolEventList(cycle4,specimenEventList,studyCalEvtPointList);
				cycle4.setParentCollectionProtocol(arm4);
				childArm4Collection.add(cycle4);

		CollectionProtocol phaseCP3 = new CollectionProtocol();
		phaseCP3.setSequenceNumber(new Integer(2));
		title = "Osteo Post ttm Follow UP-2"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Osteo Post ttm Follow UP-2"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP3 = setAttributesOfCP(phaseCP3,title,shortTitle,"Phase",null);
		specimenEventList = new ArrayList();
		specimenEventList.add("0 months");
		specimenEventList.add("3 months");
		specimenEventList.add("6 months");
		specimenEventList.add("9 months");
		specimenEventList.add("12 months");
		specimenEventList.add("18 months");
		specimenEventList.add("24 months");
		
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("0");
		studyCalEvtPointList.add("90");
		studyCalEvtPointList.add("180");
		studyCalEvtPointList.add("270");
		studyCalEvtPointList.add("360");
		studyCalEvtPointList.add("540");
		studyCalEvtPointList.add("720");
		
		
		phaseCP3 = setCollectionProtocolEventList(phaseCP3,specimenEventList,studyCalEvtPointList);
		phaseCP3.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP3);	
		
		CollectionProtocol phaseCP4 = new CollectionProtocol();
		phaseCP4.setSequenceNumber(null);
		title = "Osteo Relapse-2"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Osteo Relapse-2"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP4 = setAttributesOfCP(phaseCP4,title,shortTitle,"Phase","0");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 2");
		specimenEventList.add("Wk 3");
		specimenEventList.add("Wk 4");
		specimenEventList.add("Wk 5");
		specimenEventList.add("Wk 6");
		specimenEventList.add("Wk 7");
		specimenEventList.add("Wk 8");
		specimenEventList.add("Wk 9");
		specimenEventList.add("Wk 10");
		specimenEventList.add("Wk 11");
		specimenEventList.add("Wk 12");
		specimenEventList.add("Wk 13");
		specimenEventList.add("Wk 14");
		specimenEventList.add("Wk 15");
		
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("7");
		studyCalEvtPointList.add("14");
		studyCalEvtPointList.add("21");
		studyCalEvtPointList.add("28");
		studyCalEvtPointList.add("35");
		studyCalEvtPointList.add("42");
		studyCalEvtPointList.add("49");
		studyCalEvtPointList.add("56");
		studyCalEvtPointList.add("63");
		studyCalEvtPointList.add("70");
		studyCalEvtPointList.add("77");
		studyCalEvtPointList.add("84");
		studyCalEvtPointList.add("91");
		studyCalEvtPointList.add("98");
		studyCalEvtPointList.add("105");
		
		
		phaseCP4 = setCollectionProtocolEventList(phaseCP4,specimenEventList,studyCalEvtPointList);
		phaseCP4.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP4);

		phaseCP2.setChildCollectionProtocolCollection(childPhse2Collection);
		arm4.setChildCollectionProtocolCollection(childArm4Collection);
		collectionProtocol.setChildCollectionProtocolCollection(childCPCollection);
		return collectionProtocol;
	}
	private static CollectionProtocol setCollectionProtocolEventList(CollectionProtocol collectionProtocol,List specimenEventList,List studyCalEvtPointList)
	{
	
		Collection collectionProtocolEventList = new LinkedHashSet();
		for(int specimenEventCount = 0 ;specimenEventCount<specimenEventList.size() ;specimenEventCount++)
		{
			CollectionProtocolEvent collectionProtocolEvent= new CollectionProtocolEvent();
			setCollectionProtocolEvent(collectionProtocolEvent,specimenEventList.get(specimenEventCount).toString(),studyCalEvtPointList.get(specimenEventCount).toString());
			collectionProtocolEvent.setCollectionProtocol(collectionProtocol);
			collectionProtocolEventList.add(collectionProtocolEvent);
		}
		collectionProtocol.setCollectionProtocolEventCollection(collectionProtocolEventList);
		return collectionProtocol;
	}

	private static CollectionProtocol setAttributesOfCP(CollectionProtocol collectionProtocol ,String title,String shortTitle,String type,String studyCalEventPoint)
	{

	    Collection consentTierColl = new HashSet();
		consentTierColl = createConsentTiers(consentTierColl);
		collectionProtocol.setConsentTierCollection(consentTierColl);
		collectionProtocol.setAliquotInSameContainer(new Boolean(true));
		collectionProtocol.setDescriptionURL("");
		collectionProtocol.setActivityStatus("Active");
		
		collectionProtocol.setEndDate(null);
		collectionProtocol.setEnrollment(null);
		collectionProtocol.setIrbIdentifier("77777");
		collectionProtocol.setTitle(title);
		collectionProtocol.setShortTitle(shortTitle);
		collectionProtocol.setEnrollment(2);
		collectionProtocol.setType(type);
		if(studyCalEventPoint != null)
			collectionProtocol.setStudyCalendarEventPoint(new Double(studyCalEventPoint));
		try
		{
			collectionProtocol.setStartDate(Utility.parseDate("08/15/2003", Utility.datePattern("08/15/1975")));
		
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage()); 
			e.printStackTrace();
		}
		User principalInvestigator = new User();		
		principalInvestigator.setId(new Long("1"));		
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);
		
		Collection protocolCordinatorCollection = new HashSet();
		collectionProtocol.setCoordinatorCollection(protocolCordinatorCollection);
		return collectionProtocol;
		
	}
	private static void setCollectionProtocolEvent(CollectionProtocolEvent collectionProtocolEvent,String collectionPointLabel,String studyCalEvtPoint)
	{
		collectionProtocolEvent.setStudyCalendarEventPoint(new Double(studyCalEvtPoint));
		collectionProtocolEvent.setCollectionPointLabel(collectionPointLabel);
		collectionProtocolEvent.setClinicalStatus("Operative");		
		//SpecimenCollectionRequirementGroup specimenCollectionRequirementGroup = new SpecimenCollectionRequirementGroup();
		//specimenCollectionRequirementGroup.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		//specimenCollectionRequirementGroup.setClinicalDiagnosis("Abdominal fibromatosis");
		//specimenCollectionRequirementGroup.setClinicalStatus("Operative");
		//collectionProtocolEvent.setRequiredCollectionSpecimenGroup(specimenCollectionRequirementGroup);
		collectionProtocolEvent.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		collectionProtocolEvent.setClinicalDiagnosis("Abdominal fibromatosis");
		collectionProtocolEvent.setClinicalStatus("Operative");
		Collection specimenCollection =null;
		CollectionProtocolEventBean cpEventBean = new CollectionProtocolEventBean();
		SpecimenRequirementBean specimenRequirementBean = createSpecimenBean();
		cpEventBean.addSpecimenRequirementBean(specimenRequirementBean);
		Map specimenMap =(Map)cpEventBean.getSpecimenRequirementbeanMap();
		if (specimenMap!=null && !specimenMap.isEmpty())
		{
			specimenCollection =edu.wustl.catissuecore.util.CollectionProtocolUtil.getReqSpecimens(
					specimenMap.values()
					,null, collectionProtocolEvent);	
		}
		collectionProtocolEvent.setSpecimenRequirementCollection(specimenCollection);
	
	}
	
	private static SpecimenRequirementBean createSpecimenBean()
	{
		SpecimenRequirementBean specimenRequirementBean = createSpecimen();
		
		Map aliquotSpecimenMap = (Map)getChildSpecimenMap("Aliquot");
		Map deriveSpecimenMap = (Map)getChildSpecimenMap("Derived");
		specimenRequirementBean.setAliquotSpecimenCollection((LinkedHashMap)aliquotSpecimenMap);
		specimenRequirementBean.setDeriveSpecimenCollection((LinkedHashMap)deriveSpecimenMap);
		return specimenRequirementBean;
	}
	private static SpecimenRequirementBean createSpecimen()
	{
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
	
		//Collected and received events
		specimenRequirementBean.setCollectionEventUserId(1);
		specimenRequirementBean.setReceivedEventUserId(1);
		specimenRequirementBean.setCollectionEventContainer("Heparin Vacutainer");
		specimenRequirementBean.setReceivedEventReceivedQuality("Cauterized");
		specimenRequirementBean.setCollectionEventCollectionProcedure("Lavage");
		
		//Aliquot
		specimenRequirementBean.setNoOfAliquots("2");
		specimenRequirementBean.setQuantityPerAliquot("1");
		specimenRequirementBean.setStorageContainerForAliquotSpecimem("Auto");
		
		specimenRequirementBean.setNoOfDeriveSpecimen(1);
		specimenRequirementBean.setDeriveSpecimen(null);
		return specimenRequirementBean;
	}
	private static Map getChildSpecimenMap(String lineage)
	{
		String noOfAliquotes = "2";
		String quantityPerAliquot = "1";
		Map aliquotMap = new LinkedHashMap();
		Double aliquotCount = Double.parseDouble(noOfAliquotes);
		Double parentQuantity = Double.parseDouble("10");
		Double aliquotQuantity=0D;
		if(quantityPerAliquot==null||quantityPerAliquot.equals(""))
		{
			aliquotQuantity = parentQuantity/aliquotCount;
			parentQuantity = parentQuantity - (aliquotQuantity * aliquotCount);
		}
		else
		{
			aliquotQuantity=Double.parseDouble(quantityPerAliquot); ;
			parentQuantity = parentQuantity - (aliquotQuantity*aliquotCount);
		}
		System.out.println(aliquotCount+":aliquotCount");
		for(int iCount=1; iCount<= 2; iCount++)
		{
			SpecimenRequirementBean specimenRequirementBean = createSpecimen();
			specimenRequirementBean.setParentName("Specimen_E1_S0");
			specimenRequirementBean.setUniqueIdentifier("E1_S0_A1");
			specimenRequirementBean.setDisplayName("Specimen_E1_S0_A1");
			specimenRequirementBean.setLineage(lineage);
			if(quantityPerAliquot==null || quantityPerAliquot.equals(""))
			{
				quantityPerAliquot="0";
			}
			specimenRequirementBean.setQuantity(quantityPerAliquot);
			specimenRequirementBean.setNoOfAliquots(null);
			specimenRequirementBean.setQuantityPerAliquot(null);
			specimenRequirementBean.setStorageContainerForAliquotSpecimem(null);
			specimenRequirementBean.setStorageContainerForSpecimen("Auto");
			specimenRequirementBean.setDeriveSpecimen(null);
			aliquotMap.put(iCount, specimenRequirementBean);
		}
		System.out.println(aliquotMap.size()+":aliquotMap.size()");
		return aliquotMap;
	}

	private static Collection createConsentTiers(Collection consentTierColl)
	{
		ConsentTier c1 = new ConsentTier();
		c1.setStatement("Consent for aids research");
		consentTierColl.add(c1);
		ConsentTier c2 = new ConsentTier();
		c2.setStatement("Consent for cancer research");
		consentTierColl.add(c2);		
		ConsentTier c3 = new ConsentTier();
		c3.setStatement("Consent for Tb research");
		consentTierColl.add(c3);
		return consentTierColl;
		
	}
	public static CollectionProtocol initComplexCollectionProtocol1()
	{
		CollectionProtocol collectionProtocol = createComplexCollectionProtocolObject1();
		return collectionProtocol;
	}
	private static CollectionProtocol createComplexCollectionProtocolObject1()
	{
		CollectionProtocol collectionProtocol = new CollectionProtocol();
	    collectionProtocol.setSequenceNumber(new Integer(0));
	    collectionProtocol.setStudyCalendarEventPoint(new Double(0));
		
		String title = "ALL-1"+UniqueKeyGeneratorUtil.getUniqueKey();
		String shortTitle = "ALL-1"+UniqueKeyGeneratorUtil.getUniqueKey();
		collectionProtocol = setAttributesOfCP(collectionProtocol,title,shortTitle,"Parent",null);
		
		List specimenEventList = new ArrayList();
		List studyCalEvtPointList = new ArrayList();
		collectionProtocol = setCollectionProtocolEventList(collectionProtocol,specimenEventList,studyCalEvtPointList);
		collectionProtocol = createParentChild1(collectionProtocol);
		return collectionProtocol;
		
		
	}
	private static CollectionProtocol createParentChild1(CollectionProtocol collectionProtocol)
	{
		Collection childCPCollection = new LinkedHashSet();
		
		String title = null;
		String shortTitle = null;
		List specimenEventList = null;
		List studyCalEvtPointList = null;
		//Phase 1
		CollectionProtocol phaseCP1 = new CollectionProtocol();
		phaseCP1.setSequenceNumber(new Integer(0));
		title = "Study Entry"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Study Entry"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP1 = setAttributesOfCP(phaseCP1,title,shortTitle,"Phase","0");
		specimenEventList = new ArrayList();
		studyCalEvtPointList = new ArrayList();
		phaseCP1 = setCollectionProtocolEventList(phaseCP1,specimenEventList,studyCalEvtPointList);
		phaseCP1.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP1);
		
		
		Collection childPhse1Collection = new LinkedHashSet();
		
		CollectionProtocol arm1 = new CollectionProtocol();
		arm1.setSequenceNumber(new Integer(0));
		title = "DC Induction"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "DC Induction"+UniqueKeyGeneratorUtil.getUniqueKey();
		arm1 = setAttributesOfCP(arm1,title,shortTitle,"Arm","7");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 2");
		
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("5");
		studyCalEvtPointList.add("6");
		
		arm1 = setCollectionProtocolEventList(arm1,specimenEventList,studyCalEvtPointList);
		arm1.setParentCollectionProtocol(phaseCP1);
		childPhse1Collection.add(arm1);
		
		CollectionProtocol arm2 = new CollectionProtocol();
		arm2.setSequenceNumber(new Integer(0));
		title = "DH Induction"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "DH Induction"+UniqueKeyGeneratorUtil.getUniqueKey();
		arm2 = setAttributesOfCP(arm2,title,shortTitle,"Arm","7");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 2");
		
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("12");
		studyCalEvtPointList.add("13");
		arm2 = setCollectionProtocolEventList(arm2,specimenEventList,studyCalEvtPointList);
		arm2.setParentCollectionProtocol(phaseCP1);
		childPhse1Collection.add(arm2);
		
		CollectionProtocol arm3 = new CollectionProtocol();
		arm3.setSequenceNumber(new Integer(0));
		title = "PC Induction"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "PC Induction"+UniqueKeyGeneratorUtil.getUniqueKey();
		arm3 = setAttributesOfCP(arm3,title,shortTitle,"Arm","7");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 2");
		
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("12");
		studyCalEvtPointList.add("16");
		arm3 = setCollectionProtocolEventList(arm3,specimenEventList,studyCalEvtPointList);
		arm3.setParentCollectionProtocol(phaseCP1);
		childPhse1Collection.add(arm3);
		
		
		CollectionProtocol arm4 = new CollectionProtocol();
		arm4.setSequenceNumber(new Integer(0));
		title = "PH Induction"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "PH Induction"+UniqueKeyGeneratorUtil.getUniqueKey();
		arm4 = setAttributesOfCP(arm4,title,shortTitle,"Arm","7");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 2");
		
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("12");
		studyCalEvtPointList.add("16");
		arm4 = setCollectionProtocolEventList(arm4,specimenEventList,studyCalEvtPointList);
		arm4.setParentCollectionProtocol(phaseCP1);
		childPhse1Collection.add(arm4);
		
		CollectionProtocol phaseCP2 = new CollectionProtocol();
		phaseCP2.setSequenceNumber(new Integer(1));
		title = "Evaluation"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Evaluation"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP2 = setAttributesOfCP(phaseCP2,title,shortTitle,"Phase","7");
		specimenEventList = new ArrayList();
		studyCalEvtPointList = new ArrayList();
		phaseCP2 = setCollectionProtocolEventList(phaseCP2,specimenEventList,studyCalEvtPointList);
		phaseCP2.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP2);

		Collection childPhse2Collection = new LinkedHashSet();
			
			CollectionProtocol arm21 = new CollectionProtocol();
			arm21.setSequenceNumber(new Integer(0));
			title = "Negative D29 NO MLL RER"+UniqueKeyGeneratorUtil.getUniqueKey();
			shortTitle = "Negative D29 NO MLL RER"+UniqueKeyGeneratorUtil.getUniqueKey();
			arm21 = setAttributesOfCP(arm21,title,shortTitle,"Arm","7");
			specimenEventList = new ArrayList();
			specimenEventList.add("Wk 1");
			specimenEventList.add("Wk 3");
			specimenEventList.add("Wk 4");
			studyCalEvtPointList = new ArrayList();
			studyCalEvtPointList.add("5");
			studyCalEvtPointList.add("6");
			studyCalEvtPointList.add("7");
			arm21 = setCollectionProtocolEventList(arm21,specimenEventList,studyCalEvtPointList);
			arm21.setParentCollectionProtocol(phaseCP2);
			childPhse2Collection.add(arm21);
			
			CollectionProtocol arm22 = new CollectionProtocol();
			arm22.setSequenceNumber(new Integer(0));
			title = "Negative D29 YES MLL RER"+UniqueKeyGeneratorUtil.getUniqueKey();
			shortTitle = "Negative D29 YES MLL RER"+UniqueKeyGeneratorUtil.getUniqueKey();
			arm22 = setAttributesOfCP(arm22,title,shortTitle,"Arm","7");
			specimenEventList = new ArrayList();
			specimenEventList.add("Wk 1");
			specimenEventList.add("Wk 4");
			specimenEventList.add("Wk 5");
			studyCalEvtPointList = new ArrayList();
			studyCalEvtPointList.add("12");
			studyCalEvtPointList.add("13");
			studyCalEvtPointList.add("14");
			arm22 = setCollectionProtocolEventList(arm22,specimenEventList,studyCalEvtPointList);
			arm22.setParentCollectionProtocol(phaseCP2);
			childPhse2Collection.add(arm22);
			
			CollectionProtocol arm23 = new CollectionProtocol();
			arm23.setSequenceNumber(new Integer(0));
			title = "Positive D29 NO MLL RER"+UniqueKeyGeneratorUtil.getUniqueKey();
			shortTitle = "Positive D29 NO MLL RER"+UniqueKeyGeneratorUtil.getUniqueKey();
			arm23 = setAttributesOfCP(arm23,title,shortTitle,"Arm","7");
			specimenEventList = new ArrayList();
			specimenEventList.add("Wk 1");
			specimenEventList.add("Wk 4");
			specimenEventList.add("Wk 5");
			studyCalEvtPointList = new ArrayList();
			studyCalEvtPointList.add("12");
			studyCalEvtPointList.add("16");
			studyCalEvtPointList.add("17");
			arm23 = setCollectionProtocolEventList(arm23,specimenEventList,studyCalEvtPointList);
			arm23.setParentCollectionProtocol(phaseCP2);
			childPhse2Collection.add(arm23);
			
			
			
		CollectionProtocol phaseCP3 = new CollectionProtocol();
		phaseCP3.setSequenceNumber(new Integer(2));
		title = "Consolidation"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Consolidation"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP3 = setAttributesOfCP(phaseCP3,title,shortTitle,"Phase","27");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 6");
		specimenEventList.add("Wk 7");
		specimenEventList.add("Wk 10");
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("3");
		studyCalEvtPointList.add("6");
		studyCalEvtPointList.add("9");
		studyCalEvtPointList.add("10");
		phaseCP3 = setCollectionProtocolEventList(phaseCP3,specimenEventList,studyCalEvtPointList);
		phaseCP3.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP3);	
		
		CollectionProtocol phaseCP4 = new CollectionProtocol();
		phaseCP4.setSequenceNumber(new Integer(3));
		title = "Interim Maintainance"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Interim Maintainance"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP4 = setAttributesOfCP(phaseCP4,title,shortTitle,"Phase","30");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 11");
		specimenEventList.add("Wk 12");
		specimenEventList.add("Wk 13");
		specimenEventList.add("Wk 14");
		specimenEventList.add("Wk 15");
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("2");
		studyCalEvtPointList.add("3");
		studyCalEvtPointList.add("4");
		studyCalEvtPointList.add("5");
		studyCalEvtPointList.add("6");
		
		phaseCP4 = setCollectionProtocolEventList(phaseCP4,specimenEventList,studyCalEvtPointList);
		phaseCP4.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP4);
		
		
		CollectionProtocol phaseCP5 = new CollectionProtocol();
		phaseCP5.setSequenceNumber(new Integer(4));
		title = "Treatment"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Treatment"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP5 = setAttributesOfCP(phaseCP5,title,shortTitle,"Phase","30");
		specimenEventList = new ArrayList();
		studyCalEvtPointList = new ArrayList();
		phaseCP5 = setCollectionProtocolEventList(phaseCP5,specimenEventList,studyCalEvtPointList);
		phaseCP5.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP5);
		
		Collection childPhse5Collection = new LinkedHashSet();
		
		CollectionProtocol arm51 = new CollectionProtocol();
		arm51.setSequenceNumber(new Integer(0));
		title = "Capizzi"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Capizzi"+UniqueKeyGeneratorUtil.getUniqueKey();
		arm51 = setAttributesOfCP(arm51,title,shortTitle,"Arm","7");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 2");
		specimenEventList.add("Wk 3");
		specimenEventList.add("Wk 4");
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("1");
		studyCalEvtPointList.add("2");
		studyCalEvtPointList.add("3");
		studyCalEvtPointList.add("4");
		arm51 = setCollectionProtocolEventList(arm51,specimenEventList,studyCalEvtPointList);
		arm51.setParentCollectionProtocol(phaseCP5);
		childPhse5Collection.add(arm51);
		
		CollectionProtocol arm52 = new CollectionProtocol();
		arm52.setSequenceNumber(new Integer(0));
		title = "HD"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "HD"+UniqueKeyGeneratorUtil.getUniqueKey();
		arm52 = setAttributesOfCP(arm52,title,shortTitle,"Arm","7");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 2");
		specimenEventList.add("Wk 3");
		specimenEventList.add("Wk 4");
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("1");
		studyCalEvtPointList.add("2");
		studyCalEvtPointList.add("3");
		studyCalEvtPointList.add("4");
		arm52 = setCollectionProtocolEventList(arm52,specimenEventList,studyCalEvtPointList);
		arm52.setParentCollectionProtocol(phaseCP5);
		childPhse5Collection.add(arm52);
		
		
		
		CollectionProtocol phaseCP6 = new CollectionProtocol();
		phaseCP6.setSequenceNumber(new Integer(5));
		title = "Delayed Intensification"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "Delayed Intensification"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP6 = setAttributesOfCP(phaseCP6,title,shortTitle,"Phase","30");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 5");
		specimenEventList.add("Wk 10");
		specimenEventList.add("Wk 15");
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("2");
		studyCalEvtPointList.add("3");
		studyCalEvtPointList.add("4");
		studyCalEvtPointList.add("5");
				
		phaseCP6 = setCollectionProtocolEventList(phaseCP6,specimenEventList,studyCalEvtPointList);
		phaseCP6.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP6);
		
		
		
		CollectionProtocol phaseCP7 = new CollectionProtocol();
		phaseCP7.setSequenceNumber(null);
		title = "POST"+UniqueKeyGeneratorUtil.getUniqueKey();
		shortTitle = "POST"+UniqueKeyGeneratorUtil.getUniqueKey();
		phaseCP7 = setAttributesOfCP(phaseCP7,title,shortTitle,"Phase","30");
		specimenEventList = new ArrayList();
		specimenEventList.add("Wk 1");
		specimenEventList.add("Wk 5");
		specimenEventList.add("Wk 10");
		specimenEventList.add("Wk 15");
		studyCalEvtPointList = new ArrayList();
		studyCalEvtPointList.add("2");
		studyCalEvtPointList.add("3");
		studyCalEvtPointList.add("4");
		studyCalEvtPointList.add("5");
				
		phaseCP7 = setCollectionProtocolEventList(phaseCP7,specimenEventList,studyCalEvtPointList);
		phaseCP7.setParentCollectionProtocol(collectionProtocol);
		childCPCollection.add(phaseCP7);
		
		
		
		
		
		
		
		
		phaseCP1.setChildCollectionProtocolCollection(childPhse1Collection);
		phaseCP2.setChildCollectionProtocolCollection(childPhse2Collection);
		phaseCP5.setChildCollectionProtocolCollection(childPhse5Collection);
		collectionProtocol.setChildCollectionProtocolCollection(childCPCollection);
		return collectionProtocol;
	}
	

}
