package edu.wustl.catissuecore.testcase.admin;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.SpecimenArrayTypeForm;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;

/**
 * This class contains test cases for Specimen Array Type add/edit
 * @author Himanshu Aseeja
 */
public class SpecimenArrayTypeTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Test Specimen Array Type Add.
	 */
	@Test
	public void testSpecimenArrayTypeAdd()
	{
		addRequestParameter("name","array_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("specimenClass","Fluid");
		addRequestParameter("specimenTypes","Sweat");
		addRequestParameter("oneDimensionCapacity","4");
		addRequestParameter("twoDimensionCapacity","4");
		addRequestParameter("operation","add");
		setRequestPathInfo("/SpecimenArrayTypeAdd");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		SpecimenArrayTypeForm form = (SpecimenArrayTypeForm) getActionForm();
		SpecimenArrayType arrayType = new SpecimenArrayType();
		
		arrayType.setName(form.getName());
		arrayType.setSpecimenClass(form.getSpecimenClass());
		arrayType.setId(form.getId());
		
		Collection collection = new HashSet();
		collection.add(form.getSpecimenTypes());
		arrayType.setSpecimenTypeCollection(collection);
		
		TestCaseUtility.setNameObjectMap("SpecimenArrayType",arrayType);
	}
	
	/**
	 * Test Specimen Array Type Edit.
	 */
	@Test
	public void testSpecimenArrayTypeSearch()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "SpecimenArrayType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "SpecimenArrayType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","ContainerType.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","a");
		addRequestParameter("pageOf","pageOfSpecimenArrayType");
		addRequestParameter("operation","search");
		actionPerform();

		SpecimenArrayType arrayType = (SpecimenArrayType) TestCaseUtility.getNameObjectMap("SpecimenArrayType");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<SpecimenArrayType> arrayTypeList = null;
		try 
		{
			arrayTypeList = bizLogic.retrieve("SpecimenArrayType");
		}
		catch (BizLogicException e) 
		{
			e.printStackTrace();
			System.out.println("SpecimenArratTypeTestCases.testSpecimenArratTypeEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(arrayTypeList.size() > 1)
		{
		    verifyForward("success");
		    verifyNoActionErrors();
		}
		else if(arrayTypeList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfSpecimenArrayType&operation=search&id=" + arrayType.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
			
   
	}
	
	/**
	 * Test Specimen Array Type Edit.
	 */
	@Test
	public void testSpecimenArrayTypeEdit()
	{
//	     /*SpecimenArrayType search action to generate SpecimenArrayTypeForm*/
//		setRequestPathInfo("/SpecimenArrayTypeSearch");
//		addRequestParameter("id", "" + arrayType.getId());
//     	actionPerform();
//		verifyForward("pageOfSpecimenArrayType");
//		verifyNoActionErrors();
//
//		/*Edit Action*/
//		arrayType.setName("array1_"+UniqueKeyGeneratorUtil.getUniqueKey());
//		addRequestParameter("name",arrayType.getName());
//		setRequestPathInfo("/SpecimenArrayTypeEdit");
//		addRequestParameter("operation", "edit");
//		actionPerform();
//		verifyForward("success");
//		verifyNoActionErrors();
//		
//		TestCaseUtility.setNameObjectMap("SpecimenArrayType",arrayType);
		//TODO
		fail("Need to write test case");
	}
	
	/**
	 */
	@Test
	public void testSpecimenArrayTypeAddWithSameName()
	{
		//TODO
		fail("Need to write test case");
	}
	
	/**
	 */
	@Test
	public void testSpecimenArrayTypeAddTissueClass()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeAddFluidClass()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeAddMolecularClass()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeAddCellClass()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeAddWithoutActivityStatus()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeAddWithEmptyTypeName()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeAddWithEmptySpecimenClass()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeAddWithinvalidSpecimenType()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeAddWithEmptyOneDimension()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeEditWithEmptySpecimenClassAndType()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeBizLogicAddWithNullObject()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeBizLogicAddWithNullType()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeBizLogicAddWithNullSpecimenClass()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeBizLogicAddWithNullSpecimenType()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeBizLogicAddWithNullDimension()
	{
		//TODO
		fail("Need to write test case");
	}
	
	/**
	 */
	@Test
	public void testSpecimenArrayTypeEditDecreaseCapacity()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeEditIncreaseCapacity()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeEditSpecimenClassAfterArrayCreation()
	{
		//TODO
		fail("Need to write test case");
	}
	/**
	 */
	@Test
	public void testSpecimenArrayTypeEditDimensionAfterArrayCreation()
	{
		//TODO
		fail("Need to write test case");
	}

}
