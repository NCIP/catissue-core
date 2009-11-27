
package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.beans.SessionDataBean;

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
		collForm.setTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
		collForm.setOperation("add");
		collForm.setShortTitle("cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
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

}
