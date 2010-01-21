package edu.wustl.catissuecore.testcase.biospecimen;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.bean.CpAndParticipentsBean;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.flex.EventParamtersBean;
import edu.wustl.catissuecore.flex.FlexInterface;
import edu.wustl.catissuecore.flex.SpecimenBean;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.bizlogic.BaseTestCaseUtility;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.Utility;



public class FlexInterfaceTestCase extends CaTissueSuiteBaseTest
{
	public void testFlex()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			CollectionProtocol cp = (CollectionProtocol)TestCaseUtility.getNameObjectMap("CollectionProtocol");
			Participant  p = (Participant)TestCaseUtility.getNameObjectMap("Participant");
			
			String xml  = flexInterface.getTreeData(cp.getId().toString(),p.getId().toString());
			if(xml==null||"".equals(xml))
			{
				assertFalse("SCG Tree XML should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	
	public void testGetTissueSidePVList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getTissueSidePVList();
			if(list==null||list.isEmpty())
			{
				assertFalse("TIssue Side should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetTissueSitePVList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getTissueSitePVList();
			if(list==null||list.isEmpty())
			{
				assertFalse("TIssue Site should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetPathologicalStatusPVList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getPathologicalStatusPVList();
			if(list==null||list.isEmpty())
			{
				assertFalse("Pathological Status should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetSpecimenClassStatusPVList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getSpecimenClassStatusPVList();
			if(list==null||list.isEmpty())
			{
				assertFalse("Specimen Class PV should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetFluidSpecimenTypePVList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getFluidSpecimenTypePVList();
			if(list==null||list.isEmpty())
			{
				assertFalse("FluidSpecimenTypePVList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetTissueSpecimenTypePVList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getTissueSpecimenTypePVList();
			if(list==null||list.isEmpty())
			{
				assertFalse("TissueSpecimenTypePVList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetMolecularSpecimenTypePVList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getMolecularSpecimenTypePVList();
			if(list==null||list.isEmpty())
			{
				assertFalse("MolecularSpecimenTypePVList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetCellSpecimenTypePVList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getCellSpecimenTypePVList();
			if(list==null||list.isEmpty())
			{
				assertFalse("CellSpecimenTypePVList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetSpecimenTypeStatusPVList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getSpecimenTypeStatusPVList();
			if(list==null||list.isEmpty())
			{
				assertFalse("SpecimenTypeStatusPVList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetUserList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getUserList();
			if(list==null||list.isEmpty())
			{
				assertFalse("UserList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetProcedureList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getProcedureList();
			if(list==null||list.isEmpty())
			{
				assertFalse("ProcedureList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetContainerList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getContainerList();
			if(list==null||list.isEmpty())
			{
				assertFalse("ContainerList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetReceivedQualityList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getReceivedQualityList();
			if(list==null||list.isEmpty())
			{
				assertFalse("ReceivedQualityList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetBiohazardTypeList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getBiohazardTypeList();
			if(list==null||list.isEmpty())
			{
				assertFalse("BiohazardTypeList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetBiohazardNameList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<String> list = flexInterface.getBiohazardNameList();
			if(list==null||list.isEmpty())
			{
				assertFalse("BiohazardNameList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	
	public void testReadSpecimenList()
	{
		try
		{
			HttpSession session = getSession();
			HashSet specimenSet = new HashSet();
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
			List l = AppUtility.executeQuery("Select s.id from Specimen s where s.specimenCollectionGroup.id="+scg.getId());
			for(int i =0;i<l.size();i++)
			{
				specimenSet.add(l.get(i));
			}
			session.setAttribute("specimenId", specimenSet);
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(session);
			List list = flexInterface.readSpecimenList();
			if(list==null||list.isEmpty())
			{
				assertFalse("SpecimenList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetCpList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			List<CpAndParticipentsBean> list = flexInterface.getCpList();
			if(list==null||list.isEmpty())
			{
				assertFalse("CPLIst  should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetParticipantsList()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			CollectionProtocol cp = (CollectionProtocol)TestCaseUtility.getNameObjectMap("CollectionProtocol");
			Participant  p = (Participant)TestCaseUtility.getNameObjectMap("Participant");
			
			List<CpAndParticipentsBean> list = flexInterface.getParticipantsList(cp.getId().toString(),cp.getShortTitle());
			if(list==null||list.isEmpty())
			{
				assertFalse("ParticipantsList should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testGetTreeData()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			CollectionProtocol cp = (CollectionProtocol)TestCaseUtility.getNameObjectMap("CollectionProtocol");
			Participant  p = (Participant)TestCaseUtility.getNameObjectMap("Participant");
			
			String xml  = flexInterface.getTreeData(cp.getId().toString(),p.getId().toString());
			if(xml==null||"".equals(xml))
			{
				assertFalse("SCG Tree XML should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testInitFlexInterfaceForMultipleSp()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
			SpecimenBean specimenBean  = flexInterface.initFlexInterfaceForMultipleSp("add","New_Specimen",scg.getName());
			if(specimenBean==null)
			{
				assertFalse("specimenBean should not be empty",true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	public void testWriteSpecimen()
	{
		try
		{
			FlexInterface flexInterface = CaTissueSuiteTestUtil.getFlexInterface(getSession());
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
			SpecimenBean ts = initTissueSpecimen();
			ts.parentType = Constants.NEW_SPECIMEN_TYPE;
			ts.parentName = scg.getName();
			
			SpecimenBean child1 = initTissueSpecimen();
			child1.parentType = Constants.DERIVED_SPECIMEN_TYPE;
			
			SpecimenBean child2 = initTissueSpecimen();
			child2.parentType = Constants.DERIVED_SPECIMEN_TYPE;
			
			ts.derivedColl = new ArrayList<SpecimenBean>();
			ts.derivedColl.add(child1);
			ts.derivedColl.add(child2);
			List<SpecimenBean> l = new ArrayList<SpecimenBean>();
			l.add(ts);
			
			String message = flexInterface.writeSpecimen(l);
			if(!message.equalsIgnoreCase("SUCCESS"))
			{
				fail(message+": couldn't add specimen");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
	
	public static SpecimenBean initTissueSpecimen()
	{
		System.out.println("Inside tissuespecimen");
		SpecimenBean ts= new SpecimenBean();
		ts.specimenClass = "Tissue";
		ts.specimenLabel = "TissueSpecimen_"+UniqueKeyGeneratorUtil.getUniqueKey();
		ts.storage = "Auto";
		ts.lineage = "New";//("Pending");
		ts.specimenBarcode = "Barcode_"+UniqueKeyGeneratorUtil.getUniqueKey();
		ts.specimenType = "Fixed Tissue Block";
		ts.pathologicalStatus = "Malignant";

		ts.tissueSide = "Left";
		ts.tissueSite = "Placenta";

		Double quantity = new Double(10.0);
		ts.quantity = quantity;
		
		EventParamtersBean collectionEventParameters = new EventParamtersBean();

		collectionEventParameters.comment = "";
		
		collectionEventParameters.userName = "admin@admin,com";		

		try
		{
			collectionEventParameters.eventdDate = (Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
			collectionEventParameters.eventHour = "11";
			collectionEventParameters.eventMinute = "10";
					
		}
		catch (ParseException e1)
		{
			System.out.println("exception in APIDemo");
			e1.printStackTrace();
		}
		
		collectionEventParameters.container = "No Additive Vacutainer";
		collectionEventParameters.collectionProcedure = "Needle Core Biopsy";
		
		
		EventParamtersBean receivedEventParameters = new EventParamtersBean();
		receivedEventParameters.userName = "admin@admin,com";		
		try
		{
			System.out.println("--- Start ---- 10");
			receivedEventParameters.eventdDate = (Utility.parseDate("08/15/1975", Utility
					.datePattern("08/15/1975")));
			receivedEventParameters.eventHour = "11";
			receivedEventParameters.eventMinute = "10";
		}
		catch (ParseException e)
		{
			System.out.println("APIDemo");
			e.printStackTrace();
		}
		
		receivedEventParameters.receivedQuality = "Acceptable";
		receivedEventParameters.comment = "fdfd";
		ts.collectionEvent = collectionEventParameters;
		ts.receivedEvent = receivedEventParameters;
				
		return ts;
	}

}
