package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.bean.CpAndParticipentsBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.flex.FlexInterface;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.CaTissueSuiteTestUtil;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.util.global.AppUtility;



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

}
