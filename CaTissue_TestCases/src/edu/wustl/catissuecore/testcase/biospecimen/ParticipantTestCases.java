package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;

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
		ParticipantForm partForm = new ParticipantForm() ;
		partForm.setFirstName("participant_first_name_" + UniqueKeyGeneratorUtil.getUniqueKey()) ;
		partForm.setLastName("participant_last_name_" + UniqueKeyGeneratorUtil.getUniqueKey()) ;
		partForm.setGender("Male Gender") ;
		partForm.setVitalStatus("Alive") ;
		partForm.setGenotype("Klinefelter's Syndrome");
		partForm.setBirthDate("01-12-1985");
		partForm.setEthnicity("Hispanic or Latino");
		partForm.setRaceTypes(new String[] {"Asian"});
		partForm.setOperation("add") ;

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocol");
		
		Map<String,String> collProtRegVal = new LinkedHashMap<String,String>();
		
		collProtRegVal.put("CollectionProtocolRegistration:" +
				"1_CollectionProtocol_shortTitle",collectionProtocol.getShortTitle()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_registrationDate", "01-01-2008") ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_activityStatus", collectionProtocol.getActivityStatus()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_isConsentAvailable", "None Defined") ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_CollectionProtocol_id", ""+collectionProtocol.getId()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_CollectionProtocol_Title", collectionProtocol.getTitle()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_protocolParticipantIdentifier", ""+collectionProtocol.getId()) ;
		
		partForm.setCollectionProtocolRegistrationValues(collProtRegVal) ;
		
		setRequestPathInfo("/ParticipantAdd");
		setActionForm(partForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
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
		
		SimpleQueryInterfaceForm simpleForm = new SimpleQueryInterfaceForm() ;
		simpleForm.setAliasName("Participant") ;
		simpleForm.setPageOf("pageOfParticipant");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Participant");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field", "Participant.FIRST_NAME.varchar");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Starts With");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_value", "p");
		
		setActionForm(simpleForm) ;
		actionPerform();
	
		Participant participant = (Participant) TestCaseUtility.getNameObjectMap("Participant");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<Participant> participantList = null;
		try 
		{
			participantList = bizLogic.retrieve("Participant");
		}
		catch (BizLogicException e) 
		{
			e.printStackTrace();
			System.out.println("ParticipantTestCases.testParticipantEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(participantList.size() > 1)
		{
		    verifyForward("success");
		    verifyNoActionErrors();
		}
		else if(participantList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfParticipant&operation=search&id=" + participant.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
		
		/*Participant Search to generate ParticipantForm*/
		setRequestPathInfo("/ParticipantSearch");
		addRequestParameter("id", "" + participant.getId());
		actionPerform();
		verifyForward("pageOfParticipant");
		verifyNoActionErrors();
		
		/*Edit Action*/
		ParticipantForm partForm = (ParticipantForm)getActionForm() ;
		partForm.setFirstName("Shriparticipant_first_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
		partForm.setLastName("Shriparticipant_last_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
		partForm.setOperation("edit") ;
		setRequestPathInfo("/ParticipantEdit");
		setActionForm(partForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		participant.setFirstName(partForm.getFirstName());
		participant.setLastName(partForm.getLastName());
		TestCaseUtility.setNameObjectMap("Participant",participant);
	}
	/**
	 * Test Participant Add with empty parameters.
	 * Negative Test Case.
	 */
	@Test
	public void testParticpantAddWithEmptyParameters()
	{
		ParticipantForm partForm = new ParticipantForm() ;
		partForm.setFirstName("") ;
		partForm.setLastName("") ;
		partForm.setGender("-1") ;
		partForm.setVitalStatus("-1") ;
		partForm.setGenotype("");
		partForm.setBirthDate("");
		partForm.setDeathDate("") ;
		partForm.setEthnicity("-1");
		partForm.setSocialSecurityNumberPartA("") ;
		partForm.setSocialSecurityNumberPartB("") ;
		partForm.setSocialSecurityNumberPartC("") ;
		partForm.setRaceTypes(new String[] {"Asian"});
		partForm.setOperation("add") ;

		CollectionProtocol collectionProtocol = (CollectionProtocol)
						TestCaseUtility.getNameObjectMap("CollectionProtocol");
		
		Map<String,String> collProtRegVal = new LinkedHashMap<String,String>();
		
		collProtRegVal.put("CollectionProtocolRegistration:" +
				"1_CollectionProtocol_shortTitle",collectionProtocol.getShortTitle()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_registrationDate", "01-01-2008") ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_activityStatus", collectionProtocol.getActivityStatus()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_isConsentAvailable", "None Defined") ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_CollectionProtocol_id", ""+collectionProtocol.getId()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_CollectionProtocol_Title", collectionProtocol.getTitle()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_protocolParticipantIdentifier", ""+collectionProtocol.getId()) ;
		
		partForm.setCollectionProtocolRegistrationValues(collProtRegVal) ;
		
		setRequestPathInfo("/ParticipantAdd");
		setActionForm(partForm);
		actionPerform();
		verifyForward("failure");
		verifyActionErrors(new String[]{"errors.participant.atLeastOneFieldRequired"});
	}
	/**
	 * Test Participant Add with same name.
	 * Negative Test Case.
	 */
	@Test
	public void testParticipantAddWithSameName()
	{
		Participant participant = (Participant) TestCaseUtility.getNameObjectMap("Participant");
		ParticipantForm partForm = new ParticipantForm() ;
		partForm.setFirstName(participant.getFirstName()) ;
		partForm.setLastName(participant.getLastName()) ;
		partForm.setGender("Male Gender") ;
		partForm.setVitalStatus("Alive") ;
		partForm.setGenotype("Unknown");
		partForm.setBirthDate("01-12-1988");
		partForm.setEthnicity("Unknown");
		partForm.setSocialSecurityNumberPartA("111") ;
		partForm.setSocialSecurityNumberPartB("22") ;
		partForm.setSocialSecurityNumberPartC("3333") ;
		partForm.setRaceTypes(new String[] {"Asian"});
		partForm.setOperation("add") ;

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocol");
		
		Map<String,String> collProtRegVal = new LinkedHashMap<String,String>();
		
		collProtRegVal.put("CollectionProtocolRegistration:" +
				"1_CollectionProtocol_shortTitle",collectionProtocol.getShortTitle()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_registrationDate", "01-01-2008") ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_activityStatus", collectionProtocol.getActivityStatus()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_isConsentAvailable", "None Defined") ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_CollectionProtocol_id", ""+collectionProtocol.getId()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_CollectionProtocol_Title", collectionProtocol.getTitle()) ;
		
		collProtRegVal.put("CollectionProtocol" +
				"Registration:1_protocolParticipantIdentifier", ""+collectionProtocol.getId()) ;
		
		partForm.setCollectionProtocolRegistrationValues(collProtRegVal) ;
		
		setRequestPathInfo("/ParticipantAdd");
		setActionForm(partForm);
		actionPerform();
		verifyForward("failure");
		verifyActionErrors(new String[]{"errors.item"});
	}
}
