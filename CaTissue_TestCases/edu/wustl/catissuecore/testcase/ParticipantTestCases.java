package edu.wustl.catissuecore.testcase;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class contains test cases for Participant add/edit
 * @author Himanshu Aseeja
 */
public class ParticipantTestCases extends CaTissueSuiteBaseTest 
{
	/**
	 * Test Participant Add.
	 */
	@Test
	public void testParticipantAdd()
	{
		/*Participant add and registration*/
		addRequestParameter("lastName","participant_first_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("firstName","participant_last_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("gender","Male Gender");
		addRequestParameter("vitalStatus","Alive");
		addRequestParameter("genotype","Klinefelter's Syndrome");
		addRequestParameter("birthDate","01-12-1985");
		addRequestParameter("ethnicity","Hispanic or Latino");
		addRequestParameter("raceTypes","Asian");
		addRequestParameter("operation", "add");
		
		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocol");

		addRequestParameter("collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_CollectionProtocol_shortTitle)", collectionProtocol.getShortTitle());
		addRequestParameter("collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_registrationDate)", "" + "01-01-2008");
		addRequestParameter("collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_activityStatus)", collectionProtocol.getActivityStatus());
		addRequestParameter("collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_isConsentAvailable)", "None Defined");
		addRequestParameter("collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_CollectionProtocol_id)", ""+collectionProtocol.getId());
		addRequestParameter("collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_CollectionProtocol_Title)", collectionProtocol.getTitle());
		addRequestParameter("collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_protocolParticipantIdentifier)", ""+collectionProtocol.getId());
		setRequestPathInfo("/ParticipantAdd");
		actionPerform();
		verifyForward("success");
		
		ParticipantForm form=(ParticipantForm) getActionForm();
		
		Participant participant = new Participant();
		participant.setId(form.getId());
		participant.setFirstName(form.getFirstName());
		participant.setLastName(form.getLastName());
		
		Date birthDate = new Date();
		String dd = new String();
		String mm = new String();
		String yyyy = new String();
		dd = form.getBirthDate().substring(0,1);
		mm = form.getBirthDate().substring(3,4);
		yyyy = form.getBirthDate().substring(6,9);
		
		birthDate.setDate(Integer.parseInt(dd));
		birthDate.setMonth(Integer.parseInt(mm));
		birthDate.setYear(Integer.parseInt(yyyy));
		participant.setBirthDate(birthDate);
		
		Collection collectionProtocolRegistrationCollection = new HashSet();
		collectionProtocolRegistrationCollection.add(collectionProtocol);
		participant.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);
		
		TestCaseUtility.setNameObjectMap("Participant",participant);
	}

	/**
	 * Test Participant Edit.
	 */
	@Test
	public void testParticipantEdit()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName","Participant");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Participant");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","Participant.FIRST_NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1__Condition_value)","");
		addRequestParameter("counter","1");
		addRequestParameter("pageOf","pageOfParticipant");
		addRequestParameter("operation","search");
		actionPerform();
	
		Participant participant = (Participant) TestCaseUtility.getNameObjectMap("Participant");
		AbstractDAO dao = (AbstractDAO) TestCaseUtility.getNameObjectMap("DAO");
		List l = null;
		try 
		{
			dao.openSession(null);
			l = dao.retrieve("Participant");
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
			verifyForwardPath("/SearchObject.do?pageOf=pageOfParticipant&operation=search&id=" + participant.getId());
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

        /*Participant Search to generate ParticipantForm*/
		setRequestPathInfo("/ParticipantSearch");
		addRequestParameter("id", "" + participant.getId());
		actionPerform();
		verifyForward("pageOfParticipant");

		/*Edit Action*/
		participant.setFirstName("participant1_first_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
		participant.setLastName("participant1_last_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("firstName",participant.getFirstName());
		addRequestParameter("lastName",participant.getLastName());
		setRequestPathInfo("/ParticipantEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
	}
}
