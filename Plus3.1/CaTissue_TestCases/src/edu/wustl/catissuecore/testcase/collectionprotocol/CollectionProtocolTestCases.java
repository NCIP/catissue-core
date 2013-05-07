package edu.wustl.catissuecore.testcase.collectionprotocol;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.beans.SessionDataBean;

/**
 * This class contains test cases for Collection Protocol add/edit
 * @author Himanshu Aseeja
 */
public class CollectionProtocolTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Test Collection Protocol Add.
	 */

	public void testCollectionProtocolAdd()
	{
		/*Collection Protocol Details*/
		CollectionProtocolForm collForm = new CollectionProtocolForm();
		collForm.setPrincipalInvestigatorId(1L) ;
		collForm.setTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
		collForm.setOperation("add") ;
		collForm.setShortTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
		collForm.setStartDate("01-12-2009");
		collForm.setSpecimenLabelFormat("CP_%PPI%_%SP_TYPE%_%PPI_YOC_UID%_%YR_OF_COLL%");
		collForm.setAliquotLabelFormat("%PSPEC_LABEL%_%PSPEC_UID%");
		collForm.setDerivativeLabelFormat("CP_%PPI%_%SP_TYPE%_%PPI_YOC_UID%_%YR_OF_COLL%");
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
		addRequestParameter("studyCalendarEventPoint","20");
		addRequestParameter("collectionProtocolEventkey", "-1");
		addRequestParameter("collectionPointLabel", "ECP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("clinicalStatus","Not Specified");
		addRequestParameter("clinicalDiagnosis", "Not Specified");

		addRequestParameter("collectionEventId", "1");
		addRequestParameter("collectionEventUserId", "1");
		addRequestParameter("collectionUserName", "admin,admin");
		addRequestParameter("collectionEventSpecimenId", "0");

		addRequestParameter("receivedEventId", "1" );
		addRequestParameter("receivedEventUserId", "1");
		addRequestParameter("receivedUserName", "admin,admin");
		addRequestParameter("receivedEventSpecimenId", "admin,admin");
		addRequestParameter("pageOf", "specimenRequirement");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForwardPath("/CreateSpecimenTemplate.do?operation=add");

//		setRequestPathInfo("/ProtocolEventsDetails");
//		addRequestParameter("activityStatus", "active");
//		addRequestParameter("pageOf", "newEvent");
//		addRequestParameter("forwardTo", "success");
//		actionPerform();
//		verifyForward("success");

		/*Save Protocol Events*/
//		setRequestPathInfo("/SaveProtocolEvents");
//		addRequestParameter("operation","add");
//		actionPerform();
//		verifyForward("newEvent");

		/*Specimen Requirements Details*/

//		Map innerLoopValues = (Map) getSession().getAttribute("collectionProtocolEventMap");
//		CollectionProtocolEventBean event = (CollectionProtocolEventBean) innerLoopValues.get("E1");

//		CollectionProtocolEventBean event = (CollectionProtocolEventBean) innerLoopValues.get("E1");
		setRequestPathInfo("/SaveSpecimenRequirements");
		addRequestParameter("displayName", "spreq_" + UniqueKeyGeneratorUtil.getUniqueKey());

		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		addRequestParameter("collectionUserName", "" + bean.getLastName() + "," + bean.getFirstName());

		addRequestParameter("collectionEventCollectionProcedure", "");

		addRequestParameter("collectionEventContainer", "Not Specified");

		addRequestParameter("key", "E1");
		addRequestParameter("receivedEventReceivedQuality", "Not Specified");
		addRequestParameter("collectionEventId", "1");
		addRequestParameter("collectionEventUserId", "1");
		addRequestParameter("collectionUserName", "admin,admin");
		addRequestParameter("collectionEventSpecimenId", "0");

		addRequestParameter("receivedEventId", "1" );
		addRequestParameter("receivedEventUserId", "1");
		addRequestParameter("receivedUserName", "admin,admin");
		addRequestParameter("receivedEventSpecimenId", "admin,admin");

		addRequestParameter("collectionEventCollectionProcedure", "Lavage");
		addRequestParameter("collectionEventContainer", "CPT");
		addRequestParameter("className", "Tissue");
		addRequestParameter("tissueSite", "Anal canal");
		addRequestParameter("tissueSide", "Left");
		addRequestParameter("pathologicalStatus", "Metastatic");
		addRequestParameter("storageLocationForSpecimen","Auto");
		addRequestParameter("type","Frozen Tissue");
		addRequestParameter("collectionEventComments", "");
		addRequestParameter("receivedEventReceivedQuality","Frozen");
		addRequestParameter("receivedEventComments", "");
		addRequestParameter("quantity", "10");
		addRequestParameter("quantityPerAliquot", "5");
		addRequestParameter("noOfAliquots", "2");
		addRequestParameter("storageLocationForAliquotSpecimen", "Virtual");
		addRequestParameter("operation", "add");

		addRequestParameter("labelFormat", "%CP_DEFAULT%");

		addRequestParameter("labelFormatForAliquot", "%CP_DEFAULT%");

		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		setRequestPathInfo("/SubmitCollectionProtocol");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		Map innerLoopValues = (Map) getSession().getAttribute("collectionProtocolEventMap");

		Set s = innerLoopValues.keySet();
		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) getSession().getAttribute("collectionProtocolBean");
		CollectionProtocolForm form = (CollectionProtocolForm) getActionForm();

		CollectionProtocol collectionProtocol = new CollectionProtocol();

		collectionProtocol.setCollectionProtocolEventCollection(s);
    	collectionProtocol.setId(collectionProtocolBean.getIdentifier());
		collectionProtocol.setTitle(form.getTitle());
		collectionProtocol.setShortTitle(form.getShortTitle());
		collectionProtocol.setCollectionProtocolEventCollection(s);

		User principalInvestigator = new User();
		principalInvestigator.setId(form.getPrincipalInvestigatorId());
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);

		Date startDate = new Date();
		String dd = new String();
		String mm = new String();
		String yyyy = new String();
		dd = form.getStartDate().substring(0,1);
		mm = form.getStartDate().substring(3,4);
		yyyy = form.getStartDate().substring(6,9);

		startDate.setDate(Integer.parseInt(dd));
		startDate.setMonth(Integer.parseInt(mm));
		startDate.setYear(Integer.parseInt(yyyy));
		collectionProtocol.setStartDate(startDate);

		TestCaseUtility.setNameObjectMap("CollectionProtocolEventMap",innerLoopValues);
		TestCaseUtility.setNameObjectMap("CollectionProtocol",collectionProtocol);

		//Call to "ShowCollectionProtocol" action to show the CP tree view
		setRequestPathInfo("/ShowCollectionProtocol");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
	}


	public void testAddAnotherCollectionProtocol()
	{
		/*Collection Protocol Details*/
		CollectionProtocolForm collForm = new CollectionProtocolForm();
		collForm.setPrincipalInvestigatorId(1L) ;
		collForm.setTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
		collForm.setOperation("add") ;
		collForm.setShortTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
		collForm.setStartDate("01-12-2009");
		collForm.setSpecimenLabelFormat("%SYS_UID%");
		collForm.setDerivativeLabelFormat("%CP_DEFAULT%");
		collForm.setAliquotLabelFormat("%CP_DEFAULT%");
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
		addRequestParameter("studyCalendarEventPoint","20");
		addRequestParameter("collectionProtocolEventkey", "-1");
		addRequestParameter("collectionPointLabel", "ECP_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("clinicalStatus","Not Specified");
		addRequestParameter("clinicalDiagnosis", "Not Specified");

		addRequestParameter("collectionEventId", "1");
		addRequestParameter("collectionEventUserId", "1");
		addRequestParameter("collectionUserName", "admin,admin");
		addRequestParameter("collectionEventSpecimenId", "0");

		addRequestParameter("receivedEventId", "1" );
		addRequestParameter("receivedEventUserId", "1");
		addRequestParameter("receivedUserName", "admin,admin");
		addRequestParameter("receivedEventSpecimenId", "admin,admin");
		addRequestParameter("pageOf", "specimenRequirement");
		addRequestParameter("operation", "add");

		addRequestParameter("labelFormat", "%CP_DEFAULT%");

		addRequestParameter("labelFormatForAliquot", "%CP_DEFAULT%");

		actionPerform();
		verifyForwardPath("/CreateSpecimenTemplate.do?operation=add");

//		setRequestPathInfo("/ProtocolEventsDetails");
//		addRequestParameter("activityStatus", "active");
//		addRequestParameter("pageOf", "newEvent");
//		addRequestParameter("forwardTo", "success");
//		actionPerform();
//		verifyForward("success");

		/*Save Protocol Events*/
//		setRequestPathInfo("/SaveProtocolEvents");
//		addRequestParameter("operation","add");
//		actionPerform();
//		verifyForward("newEvent");

		/*Specimen Requirements Details*/

//		Map innerLoopValues = (Map) getSession().getAttribute("collectionProtocolEventMap");
//		CollectionProtocolEventBean event = (CollectionProtocolEventBean) innerLoopValues.get("E1");

//		CollectionProtocolEventBean event = (CollectionProtocolEventBean) innerLoopValues.get("E1");
		setRequestPathInfo("/SaveSpecimenRequirements");
		addRequestParameter("displayName", "spreq_" + UniqueKeyGeneratorUtil.getUniqueKey());

		SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
		addRequestParameter("collectionUserName", "" + bean.getLastName() + "," + bean.getFirstName());

		addRequestParameter("collectionEventCollectionProcedure", "");

		addRequestParameter("collectionEventContainer", "Not Specified");

		addRequestParameter("key", "E1");
		addRequestParameter("receivedEventReceivedQuality", "Not Specified");
		addRequestParameter("collectionEventId", "1");
		addRequestParameter("collectionEventUserId", "1");
		addRequestParameter("collectionUserName", "admin,admin");
		addRequestParameter("collectionEventSpecimenId", "0");

		addRequestParameter("receivedEventId", "1" );
		addRequestParameter("receivedEventUserId", "1");
		addRequestParameter("receivedUserName", "admin,admin");
		addRequestParameter("receivedEventSpecimenId", "admin,admin");

		addRequestParameter("collectionEventCollectionProcedure", "Lavage");
		addRequestParameter("collectionEventContainer", "CPT");
		addRequestParameter("className", "Tissue");
		addRequestParameter("tissueSite", "Anal canal");
		addRequestParameter("tissueSide", "Left");
		addRequestParameter("pathologicalStatus", "Metastatic");
		addRequestParameter("storageLocationForSpecimen","Auto");
		addRequestParameter("type","Frozen Tissue");
		addRequestParameter("collectionEventComments", "");
		addRequestParameter("receivedEventReceivedQuality","Frozen");
		addRequestParameter("receivedEventComments", "");
		addRequestParameter("quantity", "10");
		addRequestParameter("quantityPerAliquot", "5");
		addRequestParameter("noOfAliquots", "2");
		addRequestParameter("storageLocationForAliquotSpecimen", "Virtual");
		addRequestParameter("operation", "add");
		addRequestParameter("labelFormat", "%CP_DEFAULT%");

		addRequestParameter("labelFormatForAliquot", "%CP_DEFAULT%");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		setRequestPathInfo("/SubmitCollectionProtocol");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		Map innerLoopValues = (Map) getSession().getAttribute("collectionProtocolEventMap");

		Set s = innerLoopValues.keySet();
		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) getSession().getAttribute("collectionProtocolBean");
		CollectionProtocolForm form = (CollectionProtocolForm) getActionForm();

		CollectionProtocol collectionProtocol = new CollectionProtocol();

		collectionProtocol.setCollectionProtocolEventCollection(s);
    	collectionProtocol.setId(collectionProtocolBean.getIdentifier());
		collectionProtocol.setTitle(form.getTitle());
		collectionProtocol.setShortTitle(form.getShortTitle());
		collectionProtocol.setCollectionProtocolEventCollection(s);

		User principalInvestigator = new User();
		principalInvestigator.setId(form.getPrincipalInvestigatorId());
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);

		Date startDate = new Date();
		String dd = new String();
		String mm = new String();
		String yyyy = new String();
		dd = form.getStartDate().substring(0,1);
		mm = form.getStartDate().substring(3,4);
		yyyy = form.getStartDate().substring(6,9);

		startDate.setDate(Integer.parseInt(dd));
		startDate.setMonth(Integer.parseInt(mm));
		startDate.setYear(Integer.parseInt(yyyy));
		collectionProtocol.setStartDate(startDate);

		TestCaseUtility.setNameObjectMap("CollectionProtocolEventMap2",innerLoopValues);
		TestCaseUtility.setNameObjectMap("CollectionProtocol2",collectionProtocol);
	}

	public void testClickOnClinicalDiagnosis()
	{
		try {
		setRequestPathInfo("/ClinicalDiagnosisData");
		addRequestParameter("limit", "15");
		addRequestParameter("query", "");
		addRequestParameter("start", "0");
		actionPerform();
		String errormsg[] = new String[] {""};
		verifyActionErrors(errormsg);
		}catch (UnsupportedOperationException e) {
			e.printStackTrace();
			String errormsg[] = new String[] {""};
			verifyActionErrors(errormsg);
		}

	}

	/**
	 * Test case for testing the CollectionProtocolAction.java class
	 */
	public void testCollectionProtocalAction()
	{
		setRequestPathInfo("/CollectionProtocol");
		addRequestParameter("pageOf", "pageOfCollectionProtocol");
		addRequestParameter("operation", "add");
		addRequestParameter("menuSelected", "9");
		actionPerform();

		verifyNoActionErrors();
		verifyForward("pageOfCollectionProtocol");
	}

	/**
	 * Test Collection Protocol Edit.
	 */
//
//	public void testCollectionProtocolEdit()
//	{
//		/*Simple Search Action*/
//		setRequestPathInfo("/SimpleSearch");
//		addRequestParameter("aliasName", "CollectionProtocol");
//		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "CollectionProtocol");
//		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","SpecimenProtocol.TITLE.varchar");
//		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
//		addRequestParameter("value(SimpleConditionsNode:1_Condition_value)","c");
//		addRequestParameter("pageOf","pageOfCollectionProtocol");
//		addRequestParameter("operation","search");
//		actionPerform();
//		verifyForward("success");
//
//
//        /*Collection Protocol search action to generate CollectionProtocolForm*/
//		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocol");
//		setRequestPathInfo("/CollectionProtocolSearch");
//		addRequestParameter("id", "" + collectionProtocol.getId());
//     	actionPerform();
//		verifyForward("pageOfCollectionProtocol");
//
//		/*Edit Action*/
//		collectionProtocol.setTitle("cp1_" + UniqueKeyGeneratorUtil.getUniqueKey());
//		collectionProtocol.setShortTitle("cp1_" + UniqueKeyGeneratorUtil.getUniqueKey());
//		addRequestParameter("title",collectionProtocol.getTitle());
//		addRequestParameter("shortTitle", collectionProtocol.getShortTitle());
//		setRequestPathInfo("/CollectionProtocolEdit");
//		addRequestParameter("operation", "edit");
//		actionPerform();
//		verifyForward("success");
//
//		TestCaseUtility.setNameObjectMap("CollectionProtocol",collectionProtocol);
//
//     }

	public void testAddCPR()
	{
		setRequestPathInfo("/CPQuerySubCollectionProtocolRegistrationAdd");
		CollectionProtocolRegistrationForm cprForm = new CollectionProtocolRegistrationForm();
		cprForm.setCollectionProtocolShortTitle("");
		cprForm.setParticipantID(0);
		cprForm.setParticipantName("");
		cprForm.setParticipantProtocolID("");
		cprForm.setRegistrationDate("");
		cprForm.setBarcode("");
		cprForm.setConsentResponseValues(new HashMap());
		cprForm.setConsentTierCounter(0);
		cprForm.setSignedConsentUrl("");
		cprForm.setWitnessId(0);
		cprForm.setConsentDate("");

		cprForm.setWithdrawlButtonStatus("");
		cprForm.setStudyCalEvtPoint("");
		cprForm.setOffset(0);
		cprForm.setConsentDate("");
		cprForm.setConsentDate("");
		cprForm.setConsentDate("");cprForm.setConsentDate("");

		actionPerform();

		cprForm = (CollectionProtocolRegistrationForm)getActionForm();
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setId(cprForm.getId());
		TestCaseUtility.setNameObjectMap("CollectionProtocolRegistration",cpr);
		String errormsg[] = new String[]{"errors.item"};
		//verifyActionErrors(errormsg);



	}


	public void testAliquotCountInCP()
	{

			/*Collection Protocol Details*/
			CollectionProtocolForm collForm = new CollectionProtocolForm();
			collForm.setPrincipalInvestigatorId(1L) ;
			collForm.setTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
			collForm.setOperation("add") ;
			collForm.setShortTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
			collForm.setStartDate("01-12-2009");
			collForm.setSpecimenLabelFormat("CP_%PPI%_%SP_TYPE%_%PPI_YOC_UID%_%YR_OF_COLL%");
			collForm.setAliquotLabelFormat("%PSPEC_LABEL%_%PSPEC_UID%");
			collForm.setDerivativeLabelFormat("CP_%PPI%_%SP_TYPE%_%PPI_YOC_UID%_%YR_OF_COLL%");
			setRequestPathInfo("/OpenCollectionProtocol");
			setActionForm(collForm);
			actionPerform();
	        verifyForward("success");

	        /*Event Details*/
	        setRequestPathInfo("/DefineEvents");
	        addRequestParameter("pageOf", "pageOfDefineEvents");
			addRequestParameter("operation", "add");
			actionPerform();
			verifyForward("pageOfDefineEvents");

	        setRequestPathInfo("/SaveProtocolEvents");
			addRequestParameter("pageOf", "pageOfDefineEvents");
			addRequestParameter("studyCalendarEventPoint","20");
			addRequestParameter("collectionProtocolEventkey", "-1");
			addRequestParameter("collectionPointLabel", "ECP_" + UniqueKeyGeneratorUtil.getUniqueKey());
			addRequestParameter("clinicalStatus","Not Specified");
			addRequestParameter("clinicalDiagnosis", "Not Specified");

			addRequestParameter("collectionEventId", "1");
			addRequestParameter("collectionEventUserId", "1");
			addRequestParameter("collectionUserName", "admin,admin");
			addRequestParameter("collectionEventSpecimenId", "0");

			addRequestParameter("receivedEventId", "1" );
			addRequestParameter("receivedEventUserId", "1");
			addRequestParameter("receivedUserName", "admin,admin");
			addRequestParameter("receivedEventSpecimenId", "admin,admin");
			addRequestParameter("pageOf", "specimenRequirement");
			addRequestParameter("operation", "add");
			actionPerform();
			verifyForwardPath("/CreateSpecimenTemplate.do?operation=add");

			/*Save Specimen Requirements*/

			setRequestPathInfo("/SaveSpecimenRequirements");
			addRequestParameter("displayName", "spreq_" + UniqueKeyGeneratorUtil.getUniqueKey());

			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			addRequestParameter("collectionUserName", "" + bean.getLastName() + "," + bean.getFirstName());

			addRequestParameter("collectionEventCollectionProcedure", "");

			addRequestParameter("collectionEventContainer", "Not Specified");

			addRequestParameter("key", "E1");
			addRequestParameter("receivedEventReceivedQuality", "Not Specified");
			addRequestParameter("collectionEventId", "1");
			addRequestParameter("collectionEventUserId", "1");
			addRequestParameter("collectionUserName", "admin,admin");
			addRequestParameter("collectionEventSpecimenId", "0");

			addRequestParameter("receivedEventId", "1" );
			addRequestParameter("receivedEventUserId", "1");
			addRequestParameter("receivedUserName", "admin,admin");
			addRequestParameter("receivedEventSpecimenId", "admin,admin");

			addRequestParameter("collectionEventCollectionProcedure", "Lavage");
			addRequestParameter("collectionEventContainer", "CPT");
			addRequestParameter("className", "Tissue");
			addRequestParameter("tissueSite", "Anal canal");
			addRequestParameter("tissueSide", "Left");
			addRequestParameter("pathologicalStatus", "Metastatic");
			addRequestParameter("storageLocationForSpecimen","Auto");
			addRequestParameter("type","Frozen Tissue");
			addRequestParameter("collectionEventComments", "");
			addRequestParameter("receivedEventReceivedQuality","Frozen");
			addRequestParameter("receivedEventComments", "");
			addRequestParameter("quantity", "10");
			addRequestParameter("quantityPerAliquot", "5");
			addRequestParameter("noOfAliquots", "1.5");
			addRequestParameter("storageLocationForAliquotSpecimen", "Virtual");
			addRequestParameter("operation", "add");

			addRequestParameter("labelFormat", "%CP_DEFAULT%");

			addRequestParameter("labelFormatForAliquot", "%CP_DEFAULT%");

			actionPerform();
			verifyForward("failure");
			String errormsg[] = new String[]{"errors.item.format"};
			verifyActionErrors(errormsg);
	}

	public void testDecimalAliquotCountInCP()
	{

			/*Collection Protocol Details*/
			CollectionProtocolForm collForm = new CollectionProtocolForm();
			collForm.setPrincipalInvestigatorId(1L) ;
			collForm.setTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
			collForm.setOperation("add") ;
			collForm.setShortTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
			collForm.setStartDate("01-12-2009");
			collForm.setSpecimenLabelFormat("CP_%PPI%_%SP_TYPE%_%PPI_YOC_UID%_%YR_OF_COLL%");
			collForm.setAliquotLabelFormat("%PSPEC_LABEL%_%PSPEC_UID%");
			collForm.setDerivativeLabelFormat("CP_%PPI%_%SP_TYPE%_%PPI_YOC_UID%_%YR_OF_COLL%");
			setRequestPathInfo("/OpenCollectionProtocol");
			setActionForm(collForm);
			actionPerform();
	        verifyForward("success");

	        /*Event Details*/
	        setRequestPathInfo("/DefineEvents");
	        addRequestParameter("pageOf", "pageOfDefineEvents");
			addRequestParameter("operation", "add");
			actionPerform();
			verifyForward("pageOfDefineEvents");

	        setRequestPathInfo("/SaveProtocolEvents");
			addRequestParameter("pageOf", "pageOfDefineEvents");
			addRequestParameter("studyCalendarEventPoint","20");
			addRequestParameter("collectionProtocolEventkey", "-1");
			addRequestParameter("collectionPointLabel", "ECP_" + UniqueKeyGeneratorUtil.getUniqueKey());
			addRequestParameter("clinicalStatus","Not Specified");
			addRequestParameter("clinicalDiagnosis", "Not Specified");

			addRequestParameter("collectionEventId", "1");
			addRequestParameter("collectionEventUserId", "1");
			addRequestParameter("collectionUserName", "admin,admin");
			addRequestParameter("collectionEventSpecimenId", "0");

			addRequestParameter("receivedEventId", "1" );
			addRequestParameter("receivedEventUserId", "1");
			addRequestParameter("receivedUserName", "admin,admin");
			addRequestParameter("receivedEventSpecimenId", "admin,admin");
			addRequestParameter("pageOf", "specimenRequirement");
			addRequestParameter("operation", "add");
			actionPerform();
			verifyForwardPath("/CreateSpecimenTemplate.do?operation=add");

			/*Save Specimen Requirements*/

			setRequestPathInfo("/SaveSpecimenRequirements");
			addRequestParameter("displayName", "spreq_" + UniqueKeyGeneratorUtil.getUniqueKey());

			SessionDataBean bean = (SessionDataBean)getSession().getAttribute("sessionData");
			addRequestParameter("collectionUserName", "" + bean.getLastName() + "," + bean.getFirstName());

			addRequestParameter("collectionEventCollectionProcedure", "");

			addRequestParameter("collectionEventContainer", "Not Specified");

			addRequestParameter("key", "E1");
			addRequestParameter("receivedEventReceivedQuality", "Not Specified");
			addRequestParameter("collectionEventId", "1");
			addRequestParameter("collectionEventUserId", "1");
			addRequestParameter("collectionUserName", "admin,admin");
			addRequestParameter("collectionEventSpecimenId", "0");

			addRequestParameter("receivedEventId", "1" );
			addRequestParameter("receivedEventUserId", "1");
			addRequestParameter("receivedUserName", "admin,admin");
			addRequestParameter("receivedEventSpecimenId", "admin,admin");

			addRequestParameter("collectionEventCollectionProcedure", "Lavage");
			addRequestParameter("collectionEventContainer", "CPT");
			addRequestParameter("className", "Tissue");
			addRequestParameter("tissueSite", "Anal canal");
			addRequestParameter("tissueSide", "Left");
			addRequestParameter("pathologicalStatus", "Metastatic");
			addRequestParameter("storageLocationForSpecimen","Auto");
			addRequestParameter("type","Frozen Tissue");
			addRequestParameter("collectionEventComments", "");
			addRequestParameter("receivedEventReceivedQuality","Frozen");
			addRequestParameter("receivedEventComments", "");
			addRequestParameter("quantity", "10");
			addRequestParameter("quantityPerAliquot", "5");
			addRequestParameter("noOfAliquots", "1.0");
			addRequestParameter("storageLocationForAliquotSpecimen", "Virtual");
			addRequestParameter("operation", "add");

			addRequestParameter("labelFormat", "%CP_DEFAULT%");

			addRequestParameter("labelFormatForAliquot", "%CP_DEFAULT%");

			actionPerform();
			verifyForward("success");
			verifyNoActionErrors();
	}


}
