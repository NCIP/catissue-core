package edu.wustl.catissuecore.testcase;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.dao.exception.DAOException;
/**
 * This class contains test cases for Specimen Collection Group add/edit
 * @author Himanshu Aseeja
 */
public class SpecimenCollectionGroupTestCases extends CaTissueSuiteBaseTest
{
	/**
	 * Test Specimen Collection Group Add.
	 */
	@Test
	public void testSpecimenCollectionGroupAdd()
	{
		
		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocol");
		setRequestPathInfo("/CPQuerySpecimenCollectionGroupAdd");
		addRequestParameter("clinicalDiagnosis", "Not Specified");
		addRequestParameter("clinicalStatus", "Not Specified");
		addRequestParameter("collectionStatus", "Complete");
		addRequestParameter("collectionProtocolName", collectionProtocol.getTitle());
		
		Participant participant = (Participant) TestCaseUtility.getNameObjectMap("Participant");
		String participantNameWithProtocolId = ""+participant.getLastName()+", "+participant.getFirstName()+"("+collectionProtocol.getId()+")";
		
		addRequestParameter("participantNameWithProtocolId", participantNameWithProtocolId);
		
		Site specimenCollectionSite = (Site) TestCaseUtility.getNameObjectMap("Site");
		addRequestParameter("siteId", "" + specimenCollectionSite.getId());
		
		addRequestParameter("collectionProtocolId","" + collectionProtocol.getId());
				
		Map collectionProtocolEventMap =  (Map) TestCaseUtility.getNameObjectMap("CollectionProtocolEventMap");
		CollectionProtocolEventBean event = (CollectionProtocolEventBean) collectionProtocolEventMap.get("E1");
	
		addRequestParameter("collectionProtocolEventId", ""+event.getId());
		addRequestParameter("collectionEventId","0");		
		addRequestParameter("participantId", "" + participant.getId());
		String participantName = ""+participant.getLastName()+","+participant.getFirstName();
		addRequestParameter("participantName", participantName);
		
		addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
		addRequestParameter("protocolParticipantIdentifier", "" + participant.getId());
		
		addRequestParameter("collectionEventSpecimenId", "0");
		addRequestParameter("collectionEventdateOfEvent", "01-28-2009");
		addRequestParameter("collectionEventTimeInHours", "11");
		addRequestParameter("collectionEventTimeInMinutes", "2");
		addRequestParameter("collectionEventUserId","1");
		addRequestParameter("collectionEventCollectionProcedure","Use CP Defaults");
		addRequestParameter("collectionEventContainer","Use CP Defaults");
		
		addRequestParameter("receivedEventId", "" + event.getId());
		addRequestParameter("receivedEventDateOfEvent", "01-28-2009");
		addRequestParameter("receivedEventTimeInHours", "11");
		addRequestParameter("receivedEventTimeInMinutes", "2");
		addRequestParameter("receivedEventUserId","1");
		addRequestParameter("receivedEventCollectionProcedure","Use CP Defaults");
		addRequestParameter("receivedEventContainer","Use CP Defaults");
		addRequestParameter("receivedEventReceivedQuality", "Acceptable");
		addRequestParameter("receivedEventUserId","1");
		
		addRequestParameter("name","cp_specimen_" + UniqueKeyGeneratorUtil.getUniqueKey());

		addRequestParameter("operation", "add");

		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		SpecimenCollectionGroupForm form = (SpecimenCollectionGroupForm) getActionForm();
		
		SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
		specimenCollectionGroup.setId(form.getId());
		specimenCollectionGroup.setSpecimenCollectionSite(specimenCollectionSite);
		specimenCollectionGroup.setActivityStatus("Active");
		specimenCollectionGroup.setClinicalDiagnosis(form.getClinicalDiagnosis());
		specimenCollectionGroup.setClinicalStatus(form.getClinicalStatus());
		specimenCollectionGroup.setCollectionStatus(form.getCollectionStatus());
		specimenCollectionGroup.setName(form.getName());
				
		CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
		collectionProtocolRegistration.setParticipant(participant);
		collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
		
		specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
				
		TestCaseUtility.setNameObjectMap("SpecimenCollectionGroup",specimenCollectionGroup);
	}
	
	/**
	 * Test Specimen Collection Group Edit.
	 */
	@Test
	public void testSpecimenCollectionGroupEdit()
	{
		/*Simple Search Action*/
		setRequestPathInfo("/SimpleSearch");
		addRequestParameter("aliasName","SpecimenCollectionGroup");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "SpecimenCollectionGroup");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)","SpecimenCollectionGroup.NAME.varchar");
		addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)","Starts With");
		addRequestParameter("value(SimpleConditionsNode:1__Condition_value)","");
		addRequestParameter("pageOf","pageOfSpecimenCollectionGroup");
		addRequestParameter("operation","search");
		actionPerform();
		
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<SpecimenCollectionGroup> specimenCollectionGroupList = null;
		try 
		{
			specimenCollectionGroupList = bizLogic.retrieve("SpecimenCollectionGroup");
		}
		catch (DAOException e) 
		{
			e.printStackTrace();
			System.out.println("SpecimenCollectionGroupTestCases.testSpecimenCollectionGroupEdit(): "+e.getMessage());
			fail(e.getMessage());
		}
		
		if(specimenCollectionGroupList.size() > 1)
		{
		    verifyForward("success");
		    verifyNoActionErrors();
		}
		else if(specimenCollectionGroupList.size() == 1)
		{
			verifyForwardPath("/SearchObject.do?pageOf=pageOfSpecimenCollectionGroup&operation=search&id=" + specimenCollectionGroup.getId());
			verifyNoActionErrors();
		}
		else
		{
			verifyForward("failure");
			//verify action errors
			String errorNames[] = new String[]{"simpleQuery.noRecordsFound"};
			verifyActionErrors(errorNames);
		}
		
        /*Specimen Collection Group Search to generate SpecimenCollectionGroupForm*/
		setRequestPathInfo("/SpecimenCollectionGroupSearch");
		addRequestParameter("id", "" + specimenCollectionGroup.getId());
		actionPerform();
		verifyForward("pageOfSpecimenCollectionGroup");
		verifyNoActionErrors();

		/*Edit Action*/
		specimenCollectionGroup.setComment("abc");
		addRequestParameter("comment", specimenCollectionGroup.getComment());
		setRequestPathInfo("/SpecimenCollectionGroupEdit");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();
		
		TestCaseUtility.setNameObjectMap("SpecimenCollectionGroup",specimenCollectionGroup);
	}
	
	@Test

    public void testEditSCGAndAnticipatorySpecimen()

    {
          SpecimenCollectionGroup scg = (SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
          scg.setIsCPBasedSpecimenEntryChecked(true);
          setRequestPathInfo("/CPQuerySpecimenCollectionGroupEdit");
          addRequestParameter("id", "" + scg.getId());
          addRequestParameter("collectionStatus", scg.getCollectionStatus());
          addRequestParameter("clinicalStatus", scg.getClinicalStatus());
          addRequestParameter("clinicalDiagnosis", scg.getClinicalDiagnosis());
          addRequestParameter("restrictSCGCheckbox", "true");
          addRequestParameter("pageOf", "pageOfSpecimenCollectionGroupCPQuery");
          
          CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocol");
          Participant participant = (Participant) TestCaseUtility.getNameObjectMap("Participant");
  		  String participantNameWithProtocolId = ""+participant.getLastName()+", "+participant.getFirstName()+"("+collectionProtocol.getId()+")";
          addRequestParameter("participantId", "" + participant.getId());
  		  String participantName = ""+participant.getLastName()+","+participant.getFirstName();
  		  addRequestParameter("participantName", participantName);
  		  addRequestParameter("participantNameWithProtocolId", participantNameWithProtocolId);
  		  
  		  Map collectionProtocolEventMap =  (Map) TestCaseUtility.getNameObjectMap("CollectionProtocolEventMap");
		  CollectionProtocolEventBean event = (CollectionProtocolEventBean) collectionProtocolEventMap.get("E1");
	 
		  addRequestParameter("collectionProtocolId", "" + collectionProtocol.getId());
		  
		  addRequestParameter("collectionProtocolEventId", ""+event.getId());
  		
  		  Site specimenCollectionSite = (Site) TestCaseUtility.getNameObjectMap("Site");
		  addRequestParameter("siteId", "" + specimenCollectionSite.getId());
  		
		  StorageContainer storageContainer = (StorageContainer) TestCaseUtility.getNameObjectMap("StorageContainer");
		 	  
		
		  addRequestParameter("collectionEventdateOfEvent", "01-28-2009");
  		  addRequestParameter("collectionEventCollectionProcedure", "Use CP Defaults");
  		  addRequestParameter("collectionEventTimeInHours", "11");
		  addRequestParameter("collectionEventTimeInMinutes", "2");
		  addRequestParameter("collectionEventContainer", "Use CP Defaults");
		  
		  addRequestParameter("receivedEventDateOfEvent", "01-28-2009");
		  addRequestParameter("receivedEventTimeInHours", "11");
		  addRequestParameter("receivedEventTimeInMinutes", "2");
		  addRequestParameter("receivedEventUserId","1");
		  addRequestParameter("receivedEventCollectionProcedure","Use CP Defaults");
		  addRequestParameter("receivedEventContainer","Use CP Defaults");
		  addRequestParameter("receivedEventReceivedQuality", "Acceptable");
		  
		  addRequestParameter("name",scg.getName());
			  
		  addRequestParameter("operation", "edit");
          actionPerform();
          verifyForward("success");
    }

}
