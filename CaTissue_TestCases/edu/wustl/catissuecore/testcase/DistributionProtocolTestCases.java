package edu.wustl.catissuecore.testcase;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.DistributionProtocolForm;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class contains test cases for Distribution Protocol add/edit
 * @author Himanshu Aseeja
 */
public class DistributionProtocolTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Test Distribution Protocol Add.
	 */
	@Test
	public void testDistributionProtocolAdd()
	{
		addRequestParameter("principalInvestigatorId", "2");
		addRequestParameter("title", "dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("shortTitle", "dp_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("startDate", "01-12-2009");
		addRequestParameter("operation", "add");
		setRequestPathInfo("/DistributionProtocolAdd");
		actionPerform();
		verifyForward("success");	
		DistributionProtocolForm form = (DistributionProtocolForm) getActionForm();
		DistributionProtocol distributionProtocol = new DistributionProtocol();
		
		User principalInvestigator=new User();
		principalInvestigator.setId(form.getId());
		
		distributionProtocol.setTitle(form.getTitle());
		distributionProtocol.setShortTitle(form.getShortTitle());
		distributionProtocol.setId(form.getId());
		
		
		Date date = new Date();
		String dd = new String();
		String mm = new String();
		String yyyy = new String();
		dd = form.getStartDate().substring(0,1);
		mm = form.getStartDate().substring(3,4);
		yyyy = form.getStartDate().substring(6,9);
		
		date.setDate(Integer.parseInt(dd));
		date.setMonth(Integer.parseInt(mm));
		date.setYear(Integer.parseInt(yyyy));
		
		distributionProtocol.setStartDate(date);
		TestCaseUtility.setNameObjectMap("DistributionProtocol",distributionProtocol);
	}
	
	/**
	 * Test Distribution Protocol Edit.
	 */
	@Test
	public void testDistributionProtocolEdit()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName", "DistributionProtocol");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "DistributionProtocol");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","SpecimenProtocol.TITLE.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","");
		addRequestParameter("pageOf","pageOfDistributionProtocol");
		addRequestParameter("operation","search");
		actionPerform();

		DistributionProtocol distributionProtocol = (DistributionProtocol) TestCaseUtility.getNameObjectMap("DistributionProtocol");
		AbstractDAO dao = (AbstractDAO) TestCaseUtility.getNameObjectMap("DAO");
		List l = null;
		try 
		{
			dao.openSession(null);
			l = dao.retrieve("DistributionProtocol");
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
			verifyForwardPath("/SearchObject.do?pageOf=pageOfDistributionProtocol&operation=search&id=" + distributionProtocol.getId());
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
	
        /*Distribution Protocol search action to generate DistributionProtocol form*/
		setRequestPathInfo("/DistributionProtocolSearch");
		addRequestParameter("id", "" + distributionProtocol.getId());
     	actionPerform();
		verifyForward("pageOfDistributionProtocol");

		/*Edit Action*/
		distributionProtocol.setTitle("dp1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		distributionProtocol.setShortTitle("dp1_"+UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("title",distributionProtocol.getTitle());
		addRequestParameter("shortTitle",distributionProtocol.getShortTitle());
		setRequestPathInfo("/DistributionProtocolEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		TestCaseUtility.setNameObjectMap("DistributionProtocol",distributionProtocol);
	}

}
