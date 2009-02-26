package src.edu.wustl.catissuecore.testcase;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.SpecimenArrayTypeForm;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.util.dbManager.DAOException;

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
	public void testSpecimenArrayTypeEdit()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "SpecimenArrayType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "SpecimenArrayType");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","ContainerType.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","");
		addRequestParameter("pageOf","pageOfSpecimenArrayType");
		addRequestParameter("operation","search");
		actionPerform();

		SpecimenArrayType arrayType = (SpecimenArrayType) TestCaseUtility.getNameObjectMap("SpecimenArrayType");
		AbstractDAO dao = (AbstractDAO) TestCaseUtility.getNameObjectMap("DAO");
		List l = null;
		try 
		{
			dao.openSession(null);
			l = dao.retrieve("SpecimenArrayType");
		}
		catch (DAOException e) 
		{
			e.printStackTrace();
		}
		
		if(l.size() > 1)
		{
		    verifyForward("success");
		}
		else if(l.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfSpecimenArrayType&operation=search&id=" + arrayType.getId());
		}
		else
		{
			verifyForward("failure");
		}
		try
		{
			dao.closeSession();
		}
		catch (DAOException e) 
		{
		    e.printStackTrace();
		}
	
        /*SpecimenArrayType search action to generate SpecimenArrayTypeForm*/
		setRequestPathInfo("/SpecimenArrayTypeSearch");
		addRequestParameter("id", "" + arrayType.getId());
     	actionPerform();
		verifyForward("pageOfSpecimenArrayType");

		/*Edit Action*/
		arrayType.setName("array1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("name",arrayType.getName());
		setRequestPathInfo("/SpecimenArrayTypeEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		TestCaseUtility.setNameObjectMap("SpecimenArrayType",arrayType);
	}
}
