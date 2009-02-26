package src.edu.wustl.catissuecore.testcase;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.actionForm.ProtocolEventDetailsForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class contains test cases for Collection Protocol add/edit
 * @author Himanshu Aseeja
 */
public class CollectionProtocolTestCases extends CaTissueSuiteBaseTest 
{
	/**
	 * Test Collection Protocol Add.
	 */
	@Test
	public void testCollectionProtocolAdd()
	{
		/*Collection Protocol Details*/
		setRequestPathInfo("/OpenCollectionProtocol");
		addRequestParameter("isParticiapantReg", "true");
		addRequestParameter("principalInvestigatorId", "1");
		addRequestParameter("title", "cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("shortTitle", "cp_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("startDate", "01-12-2009");
		addRequestParameter("pageOf","pageOfCollectionProtocol");
		actionPerform();
        verifyForward("success");
        
        /*Event Details*/
       	setRequestPathInfo("/DefineEvents");
		addRequestParameter("pageOf", "pageOfDefineEvents");
		addRequestParameter("studyCalendarEventPoint","20");
		addRequestParameter("eventKey", "E1");
		addRequestParameter("collectionPointLabel", "ECP_" + UniqueKeyGeneratorUtil.getUniqueKey());
//		addRequestParameter("collectionProtocolEventkey", "-1");
		addRequestParameter("clinicalStatus","Operative");
		addRequestParameter("clinicalDiagnosis", "10-30% body burnt (disorder)");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("pageOfDefineEvents");
		
		setRequestPathInfo("/ProtocolEventsDetails");
		addRequestParameter("activityStatus", "active");
		addRequestParameter("pageOf", "newEvent");
		addRequestParameter("forwardTo", "success");
		actionPerform();
		verifyForward("success");
		
		
		/*Specimen Requirements Details*/

		setRequestPathInfo("/CreateSpecimenTemplate");
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
		actionPerform();
		
		/*Save Protocol Events*/
		setRequestPathInfo("/SaveProtocolEvents");
		addRequestParameter("operation","add");
		actionPerform();
		verifyForward("newEvent");
		
		
		setRequestPathInfo("/SubmitCollectionProtocol");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		
		Map innerLoopValues = (Map) getSession().getAttribute("collectionProtocolEventMap");
		
		Set s = innerLoopValues.keySet();
		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) getSession().getAttribute("collectionProtocolBean");
		CollectionProtocolForm form = (CollectionProtocolForm) getActionForm();	
		
		CollectionProtocol collectionProtocol = new CollectionProtocol();
	
		collectionProtocol.setCollectionProtocolEventCollection(s);
    	collectionProtocol.setId(collectionProtocolBean.getIdentifier());
		collectionProtocol.setTitle(form.getTitle());
		collectionProtocol.setShortTitle(form.getShortTitle());
			
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
	}
	
	/**
	 * Test Collection Protocol Edit.
	 */
//	@Test
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
}
