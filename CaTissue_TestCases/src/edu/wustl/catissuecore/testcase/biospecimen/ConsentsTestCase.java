
package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts.action.ActionFormBeans;
import org.junit.Test;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.actionForm.ConsentResponseForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.simplequery.actionForm.SimpleQueryInterfaceForm;

public class ConsentsTestCase extends CaTissueSuiteBaseTest
{

	/**
	 * Test Collection Protocol Add.
	 */
	@Test
	public void testCollectionProtocolWithConsentTierAdd()
	{
		/*Collection Protocol Details*/
		CollectionProtocolForm collForm = new CollectionProtocolForm();
		collForm.setPrincipalInvestigatorId(1L);
		collForm.setTitle("cp_c" + UniqueKeyGeneratorUtil.getUniqueKey());
		System.out.println("title " + collForm.getTitle());
		collForm.setOperation("add");
		collForm.setShortTitle("cp_c" + UniqueKeyGeneratorUtil.getUniqueKey());
		collForm.setStartDate("01-12-2009");
		setRequestPathInfo("/OpenCollectionProtocol");
		setActionForm(collForm);
		actionPerform();
		verifyForward("success");
		//verifyForward("/pages/Layout.jsp");

		/*Event Details*/
		setRequestPathInfo("/DefineEvents");
		addRequestParameter("pageOf", "pageOfDefineEvents");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfDefineEvents");

		setRequestPathInfo("/SaveProtocolEvents");
		addRequestParameter("pageOf", "pageOfDefineEvents");
		addRequestParameter("studyCalendarEventPoint", "20");
		addRequestParameter("collectionProtocolEventkey", "-1");
		addRequestParameter("collectionPointLabel", "ECP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("clinicalStatus", "Not Specified");
		addRequestParameter("clinicalDiagnosis", "Not Specified");

		addRequestParameter("collectionEventId", "1");
		addRequestParameter("collectionEventUserId", "1");
		addRequestParameter("collectionUserName", "admin,admin");
		addRequestParameter("collectionEventSpecimenId", "0");

		addRequestParameter("receivedEventId", "1");
		addRequestParameter("receivedEventUserId", "1");
		addRequestParameter("receivedUserName", "admin,admin");
		addRequestParameter("receivedEventSpecimenId", "admin,admin");
		addRequestParameter("pageOf", "specimenRequirement");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForwardPath("/CreateSpecimenTemplate.do?operation=add");

		setRequestPathInfo("/SaveSpecimenRequirements");
		addRequestParameter("displayName", "spreq_" + UniqueKeyGeneratorUtil.getUniqueKey());

		SessionDataBean bean = (SessionDataBean) getSession().getAttribute("sessionData");
		addRequestParameter("collectionUserName", "" + bean.getLastName() + ","
				+ bean.getFirstName());

		addRequestParameter("collectionEventCollectionProcedure", "");

		addRequestParameter("collectionEventContainer", "Not Specified");

		addRequestParameter("key", "E1");
		addRequestParameter("receivedEventReceivedQuality", "Not Specified");
		addRequestParameter("collectionEventId", "1");
		addRequestParameter("collectionEventUserId", "1");
		addRequestParameter("collectionUserName", "admin,admin");
		addRequestParameter("collectionEventSpecimenId", "0");

		addRequestParameter("receivedEventId", "1");
		addRequestParameter("receivedEventUserId", "1");
		addRequestParameter("receivedUserName", "admin,admin");
		addRequestParameter("receivedEventSpecimenId", "admin,admin");

		addRequestParameter("collectionEventCollectionProcedure", "Lavage");
		addRequestParameter("collectionEventContainer", "CPT");
		addRequestParameter("className", "Tissue");
		addRequestParameter("tissueSite", "Anal canal");
		addRequestParameter("tissueSide", "Left");
		addRequestParameter("pathologicalStatus", "Metastatic");
		addRequestParameter("storageLocationForSpecimen", "Auto");
		addRequestParameter("type", "Frozen Tissue");
		addRequestParameter("collectionEventComments", "");
		addRequestParameter("receivedEventReceivedQuality", "Frozen");
		addRequestParameter("receivedEventComments", "");
		addRequestParameter("quantity", "10");
		addRequestParameter("quantityPerAliquot", "5");
		addRequestParameter("noOfAliquots", "2");
		addRequestParameter("storageLocationForAliquotSpecimen", "Virtual");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		Map consentValues = new HashMap();
		consentValues.put("ConsentBean:0_statement", "Dispose specimen after use");
		consentValues.put("ConsentBean:1_statement", "Withdraw it if required");

		collForm.setConsentValues(consentValues);
		collForm.setConsentTierCounter(2);
		addRequestParameter("type", "");
		setActionForm(collForm);

		setRequestPathInfo("/DefineEvents");
		addRequestParameter("pageOf", "pageOfDefineEvents");
		addRequestParameter("operation", "add");

		actionPerform();
		verifyForward("pageOfDefineEvents");

		setRequestPathInfo("/SubmitCollectionProtocol");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		Map innerLoopValues = (Map) getSession().getAttribute("collectionProtocolEventMap");

		Set s = innerLoopValues.keySet();
		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) getSession()
				.getAttribute("collectionProtocolBean");
		CollectionProtocolForm form = (CollectionProtocolForm) getActionForm();

		CollectionProtocol collectionProtocol = new CollectionProtocol();

		collectionProtocol.setCollectionProtocolEventCollection(s);
		collectionProtocol.setId(collectionProtocolBean.getIdentifier());
		collectionProtocol.setTitle(form.getTitle());
		collectionProtocol.setShortTitle(form.getShortTitle());
		collectionProtocol.setCollectionProtocolEventCollection(s);
		collectionProtocol.setConsentTierCollection(form.getConsentValues().values());

		User principalInvestigator = new User();
		principalInvestigator.setId(form.getPrincipalInvestigatorId());
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);

		Date startDate = new Date();
		String dd = new String();
		String mm = new String();
		String yyyy = new String();
		dd = form.getStartDate().substring(0, 1);
		mm = form.getStartDate().substring(3, 4);
		yyyy = form.getStartDate().substring(6, 9);

		startDate.setDate(Integer.parseInt(dd));
		startDate.setMonth(Integer.parseInt(mm));
		startDate.setYear(Integer.parseInt(yyyy));
		collectionProtocol.setStartDate(startDate);

		TestCaseUtility.setNameObjectMap("CollectionProtocolEventMapWithConsents", innerLoopValues);
		TestCaseUtility.setNameObjectMap("CollectionProtocolWithConsents", collectionProtocol);
	}

	public void testParticipantRegister()
	{

		setRequestPathInfo("/CpBasedSearch");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		setRequestPathInfo("/Participant");
		addRequestParameter("pageOf", "pageOfParticipant");
		addRequestParameter("clearConsentSession", "true");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfParticipant");
		verifyNoActionErrors();

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility
				.getNameObjectMap("CollectionProtocolWithConsents");
		System.out
				.println("collection protocol short title  " + collectionProtocol.getShortTitle());
		//ParticipantForm partForm = new ParticipantForm() ;
		ParticipantForm partForm = (ParticipantForm) getActionForm();
		partForm.setFirstName("participant_first_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
		partForm.setLastName("participant_last_name_" + UniqueKeyGeneratorUtil.getUniqueKey());
		partForm.setGender("Male Gender");
		partForm.setVitalStatus("Alive");
		partForm.setGenotype("Klinefelter's Syndrome");
		partForm.setBirthDate("01-12-1985");
		partForm.setEthnicity("Hispanic or Latino");
		partForm.setRaceTypes(new String[]{"Asian"});
		partForm.setOperation("add");

		Map<String, String> collProtRegVal = new LinkedHashMap<String, String>();

		collProtRegVal.put("CollectionProtocolRegistration:" + "1_CollectionProtocol_shortTitle",
				collectionProtocol.getShortTitle());

		collProtRegVal.put("CollectionProtocol" + "Registration:1_registrationDate", "01-01-2008");

		collProtRegVal.put("CollectionProtocol" + "Registration:1_activityStatus",
				collectionProtocol.getActivityStatus());

		collProtRegVal.put("CollectionProtocol" + "Registration:1_isConsentAvailable",
				"None Defined");

		collProtRegVal.put("CollectionProtocol" + "Registration:1_CollectionProtocol_id", ""
				+ collectionProtocol.getId());

		collProtRegVal.put("CollectionProtocol" + "Registration:1_CollectionProtocol_Title",
				collectionProtocol.getTitle());

		collProtRegVal.put("CollectionProtocol" + "Registration:1_protocolParticipantIdentifier",
				"" + collectionProtocol.getId());

		partForm.setCollectionProtocolRegistrationValues(collProtRegVal);

		setRequestPathInfo("/ParticipantAdd");
		setActionForm(partForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		ParticipantForm form = (ParticipantForm) getActionForm();

		Participant participant = new Participant();
		participant.setId(form.getId());
		participant.setFirstName(form.getFirstName());
		participant.setLastName(form.getLastName());

		Date birthDate = new Date();
		String dd = new String();
		String mm = new String();
		String yyyy = new String();
		dd = form.getBirthDate().substring(0, 1);
		mm = form.getBirthDate().substring(3, 4);
		yyyy = form.getBirthDate().substring(6, 9);

		birthDate.setDate(Integer.parseInt(dd));
		birthDate.setMonth(Integer.parseInt(mm));
		birthDate.setYear(Integer.parseInt(yyyy));
		participant.setBirthDate(birthDate);

		Collection collectionProtocolRegistrationCollection = new HashSet();
		collectionProtocolRegistrationCollection.add(collectionProtocol);
		participant
				.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);

		TestCaseUtility.setNameObjectMap("ParticipantHavingConsents", participant);

	}

	public void testParticipantEditConsentsResponce()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");

		SimpleQueryInterfaceForm simpleForm = new SimpleQueryInterfaceForm();
		simpleForm.setAliasName("Participant");
		simpleForm.setPageOf("pageOfParticipant");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_DataElement_table", "Participant");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_DataElement_field",
				"Participant.FIRST_NAME.varchar");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_Operator_operator", "Starts With");
		simpleForm.setValue("SimpleConditionsNode:1_Condition_value", "p");

		setActionForm(simpleForm);
		actionPerform();

		Participant participant = (Participant) TestCaseUtility
				.getNameObjectMap("ParticipantHavingConsents");
		/*DefaultBizLogic bizLogic = new DefaultBizLogic();
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
		*/
		/*Participant Search to generate ParticipantForm*/
		setRequestPathInfo("/ParticipantSearch");
		addRequestParameter("id", "" + participant.getId());
		actionPerform();
		verifyForward("pageOfParticipant");
		verifyNoActionErrors();

		System.out.println("getActualForward " + getActualForward());
		ParticipantForm perForm = (ParticipantForm) getActionForm();
		perForm.setOperation("edit");
		setRequestPathInfo(getActualForward());
		addRequestParameter("pageOf", "pageOfParticipant");
		addRequestParameter("operation", "edit");
		addRequestParameter("menuSelected", "12");
		actionPerform();
		verifyForward("pageOfParticipant");

		perForm = (ParticipantForm) getActionForm();
		String cprId = perForm.getCollectionProtocolRegistrationValue(
				"CollectionProtocolRegistration:1_id").toString();

		System.out.println("participant last name " + participant.getLastName());
		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility
				.getNameObjectMap("CollectionProtocolWithConsents");

		setRequestPathInfo("/ConsentDisplay");
		addRequestParameter("pageOf", "pageOfConsent");
		addRequestParameter("operation", "edit");
		addRequestParameter("cpSearchCpId", collectionProtocol.getId().toString());
		addRequestParameter("collectionProtocolRegIdValue", cprId);
		actionPerform();
		verifyForward("pageOfConsent");

		ConsentResponseForm consentResponseForm = (ConsentResponseForm) getActionForm();
		consentResponseForm.setConsentResponseValue("ConsentBean:1_participantResponse", "yes");
		consentResponseForm.setConsentResponseValue("ConsentBean:0_participantResponse", "yes");

		setRequestPathInfo("/ConsentSubmit");
		setActionForm(consentResponseForm);
		actionPerform();
		// verifyForward(null);
		//verifyNoActionErrors();

		setRequestPathInfo("/ParticipantEdit");
		setActionForm(perForm);
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		setRequestPathInfo("/Participant");
		addRequestParameter("pageOf", "pageOfParticipantCPQuery");
		addRequestParameter("operation", "edit");
		addRequestParameter("menuSelected", "12");
		addRequestParameter("fromSubmitAction", "true");
		setActionForm(perForm);
		actionPerform();
		verifyForward("pageOfParticipantCPQuery");
		verifyNoActionErrors();

		System.out.println("getActualForward() " + getActualForward());
	}

	public void testSpecimenCollectionGroupEdit()
	{
		setRequestPathInfo("/CpBasedSearch");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility
				.getNameObjectMap("CollectionProtocolWithConsents");
		setRequestPathInfo("/QueryParticipant");
		addRequestParameter("refresh", "false");
		addRequestParameter("cpSearchCpId", collectionProtocol.getId().toString());
		addRequestParameter("pageOf", "pageOfParticipantCPQuery");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("pageOfParticipantCPQuery");
		verifyNoActionErrors();

		Participant participant = (Participant) TestCaseUtility
				.getNameObjectMap("ParticipantHavingConsents");
		setRequestPathInfo("/ParticipantSearch");
		addRequestParameter("id", "" + participant.getId());
		actionPerform();
		verifyForward("pageOfParticipant");
		verifyNoActionErrors();

		ParticipantForm partForm = (ParticipantForm) getActionForm();

		/*DefaultBizLogic bizLogic = new DefaultBizLogic();
		
		
		List<Participant> participantList = null;
		try 
		{
			participant = (Participant)bizLogic.retrieve("edu.wustl.catissuecore.domain.Participant", participant.getId());
			
		
		}
		catch (BizLogicException e) 
		{
			e.printStackTrace();
			System.out.println("ParticipantTestCases.testParticipantEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		setRequestPathInfo("/QuerySpecimenCollectionGroup");
		addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
		addRequestParameter("treeRefresh", "false");
		addRequestParameter("clickedNodeId", "SpecimenCollectionGroup_"+730907);
		addRequestParameter("operation", "edit");
		
		actionPerform();
		verifyForward("pageOfSpecimenCollectionGroupCPQuery");
		verifyNoActionErrors();*/

	}

}
