
package edu.wustl.catissuecore.testcase;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.MapDataParser;

/**
 * Description : This class will contain following function 
 *               1. addCollectionProtocol()
 *               2. addParticipant().
 *               3. EditCollectionProtocolWithCloseActivityStatus().
 * @author renuka_bajpai
 *
 */
public class CollectionProtocolCloseActivityStatus extends CaTissueSuiteBaseTest
{

	/**
	 * Test Collection Protocol Add.
	 * @method : addCollectionProtocol()
	 */
	public void testAddCollectionProtocol()
	{
		/*Collection Protocol Details*/
		setRequestPathInfo("/OpenCollectionProtocol");
		addRequestParameter("isParticiapantReg", "true");
		addRequestParameter("principalInvestigatorId", "1");
		addRequestParameter("title", "cpRenu_2_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("shortTitle", "cpRenu_2__" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("startDate", "01-12-2009");
		addRequestParameter("pageOf", "pageOfCollectionProtocol");
		actionPerform();
		verifyForward("success");

		/*Event Details*/
		setRequestPathInfo("/DefineEvents");
		addRequestParameter("pageOf", "pageOfDefineEvents");
		addRequestParameter("operation", "add");
		addRequestParameter("invokeFunction", "null");
		actionPerform();
		verifyForward("pageOfDefineEvents");

		setRequestPathInfo("/SaveProtocolEvents");
		//addRequestParameter("pageOf", "pageOfDefineEvents");
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

		//1st specimen requirement.

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

		//2nd specimen requirement.

		setRequestPathInfo("/SaveSpecimenRequirements");
		addRequestParameter("displayName", "spreq_" + UniqueKeyGeneratorUtil.getUniqueKey());

		SessionDataBean bean2 = (SessionDataBean) getSession().getAttribute("sessionData");
		addRequestParameter("collectionUserName", "" + bean2.getLastName() + ","
				+ bean2.getFirstName());

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

		setRequestPathInfo("/SubmitCollectionProtocol");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		Map innerLoopValues = (Map) getSession().getAttribute("collectionProtocolEventMap");

		Set s = innerLoopValues.keySet();
		Collection values = innerLoopValues.values();

		CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) getSession()
				.getAttribute("collectionProtocolBean");
		CollectionProtocolForm form = (CollectionProtocolForm) getActionForm();
	
		
		CollectionProtocol collectionProtocol = new CollectionProtocol();

		collectionProtocol.setCollectionProtocolEventCollection(values);
		collectionProtocol.setId(collectionProtocolBean.getIdentifier());
		collectionProtocol.setActivityStatus(form.getActivityStatus());
		collectionProtocol.setTitle(form.getTitle());
		collectionProtocol.setShortTitle(form.getShortTitle());
		

		User principalInvestigator = new User();
		principalInvestigator.setId(form.getPrincipalInvestigatorId());
		collectionProtocol.setPrincipalInvestigator(principalInvestigator);
		
		//collectionProtocol.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);

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

		TestCaseUtility.setNameObjectMap("CollectionProtocolEventMapCloseActivityStatus",
				innerLoopValues);
		TestCaseUtility.setNameObjectMap("CollectionProtocolCloseActivityStatus",
				collectionProtocol);
	}

	// Add participant ...

		public void testParticipantAdd()
		{
			//Participant add and registration
			addRequestParameter("lastName","BajpaiTestCases_1_" + UniqueKeyGeneratorUtil.getUniqueKey());
			addRequestParameter("firstName","RenukaTestCases_1_" + UniqueKeyGeneratorUtil.getUniqueKey());
			addRequestParameter("gender","Female Gender");
			addRequestParameter("vitalStatus","Alive");
			addRequestParameter("genotype","Klinefelter's Syndrome");
			addRequestParameter("birthDate","01-12-1985");
			addRequestParameter("ethnicity","Hispanic or Latino");
			addRequestParameter("raceTypes","Asian");
			addRequestParameter("operation", "add");
			
			CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocolCloseActivityStatus");

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
			verifyNoActionErrors();
			
			ParticipantForm form=(ParticipantForm) getActionForm();
			
			Participant participant = new Participant();
			participant.setId(form.getId());
			participant.setFirstName(form.getFirstName());
			participant.setLastName(form.getLastName());
			
			
			//form.getAllCollectionProtocolRegistrationValues();
			
			
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
			
			
			
			Map mapCollectionProtocolRegistrationCollection =
				form.getCollectionProtocolRegistrationValues();
			MapDataParser parserCollectionProtocolRegistrationCollection =
				new MapDataParser("edu.wustl.catissuecore.domain");
			
				try
				{
					participant.setCollectionProtocolRegistrationCollection(parserCollectionProtocolRegistrationCollection
						.generateData(mapCollectionProtocolRegistrationCollection));
					
				}
				catch (Exception e)
				{					
					e.printStackTrace();
				}
			
			
		//	participant.setCollectionProtocolRegistrationCollection(form.getAllCollectionProtocolRegistrationValues());
			
			/*Collection collectionProtocolRegistrationCollection = new HashSet();
			collectionProtocolRegistrationCollection.add(collectionProtocol);
			participant.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);*/
			
			TestCaseUtility.setNameObjectMap("ParticipantCloseActivityStatus",participant);
		}	
		
		@Test

	      public void testSpecimenEdit() throws BizLogicException
	      {
			Participant participant = (Participant) TestCaseUtility.getNameObjectMap("ParticipantCloseActivityStatus");
			//participant.getCollectionProtocolRegistrationCollection();
			CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocolCloseActivityStatus");
			Collection<CollectionProtocolRegistration> collectionProtocolRegistration= participant.getCollectionProtocolRegistrationCollection();
			Iterator<CollectionProtocolRegistration> itr = collectionProtocolRegistration.iterator();
			CollectionProtocolRegistration collectionProtocolRegistrationNew = null;
			Long cpId = collectionProtocol.getId();//121
			while(itr.hasNext())
			{
				collectionProtocolRegistrationNew = itr.next();		
				if(collectionProtocolRegistrationNew.getCollectionProtocol().getId() == collectionProtocol.getId())
				{
					break;
				}
				
			}
			Long cpIdNew = collectionProtocolRegistrationNew.getCollectionProtocol().getId();//121
			
			
			Long id = collectionProtocolRegistrationNew.getId();
			SpecimenCollectionGroup specimenCollectionGroup = getSCGFromCpr(collectionProtocolRegistrationNew);
			if(specimenCollectionGroup != null)
			{
				System.out.println("...................Renuka..................");
			}
		
			
	      }
		
		public static SpecimenCollectionGroup getSCGFromCpr(CollectionProtocolRegistration collectionProtocolRegistrationNew) throws BizLogicException
		{ 
			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) TestCaseUtility.getNameObjectMap("SpecimenCollectionGroup");
            DefaultBizLogic bizLogic = new DefaultBizLogic();
            List<SpecimenCollectionGroup> specimenCollectionGroupList = null;
            specimenCollectionGroupList = bizLogic.retrieve("SpecimenCollectionGroup");
            if (specimenCollectionGroupList.size() > 1)
		    {	                  
            	for (SpecimenCollectionGroup specimenCollectionGroup1 : specimenCollectionGroupList)
            	{
            		
            		//if(specimenCollectionGroup1.getCollectionProtocolRegistration().getId() == collectionProtocolRegistrationNew.getId())// collectionProtocolRegistrationNew.getId() = null
            		if(specimenCollectionGroup1.getCollectionProtocolEvent().getCollectionProtocol().getId() == collectionProtocolRegistrationNew.getCollectionProtocol().getId())
		             {
		                return specimenCollectionGroup1;
		             }
            	}
		    }
			return null;
		}

		

	/*	*//**
		 * Test Specimen Collection Group Add.
		 *//*
		@Test
		public void testSpecimenCollectionGroupAdd()
		{
			
			CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility.getNameObjectMap("CollectionProtocolCloseActivityStatus");
			setRequestPathInfo("/CPQuerySpecimenCollectionGroupAdd");
			addRequestParameter("clinicalDiagnosis", "Not Specified");
			addRequestParameter("clinicalStatus", "Not Specified");
			addRequestParameter("collectionStatus", "Complete");
			addRequestParameter("collectionProtocolName", collectionProtocol.getTitle());
			
			Participant participant = (Participant) TestCaseUtility.getNameObjectMap("ParticipantCloseActivityStatus");
			String participantNameWithProtocolId = ""+participant.getLastName()+", "+participant.getFirstName()+"("+collectionProtocol.getId()+")";
			
			addRequestParameter("participantNameWithProtocolId", participantNameWithProtocolId);
			
			Site specimenCollectionSite = (Site) TestCaseUtility.getNameObjectMap("Site");
			addRequestParameter("siteId", "" + specimenCollectionSite.getId());
			
			addRequestParameter("collectionProtocolId","" + collectionProtocol.getId());
					
			Map collectionProtocolEventMap =  (Map) TestCaseUtility.getNameObjectMap("CollectionProtocolEventMapCloseActivityStatus");
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
			
			SpecimenRequirementBean spBean = specimenFunction();
			
			
			CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();
			collectionProtocolRegistration.setParticipant(participant);
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
			
			specimenCollectionGroup.setCollectionProtocolRegistration(collectionProtocolRegistration);
					
			TestCaseUtility.setNameObjectMap("SpecimenCollectionGroupCloseActivityStatus",specimenCollectionGroup);
		}
		
		
		
		

	private static edu.wustl.catissuecore.bean.SpecimenRequirementBean specimenFunction()
	{		
		
		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility
				.getNameObjectMap("CollectionProtocolCloseActivityStatus");
		Collection collectionProtocolEventCollection = collectionProtocol
				.getCollectionProtocolEventCollection();
		System.out.println("");
		Long id = null;
		int c = 0;
		edu.wustl.catissuecore.bean.SpecimenRequirementBean specimenRequirementBean = null;
		if (collectionProtocolEventCollection != null)
		{
			Iterator iterator = collectionProtocolEventCollection.iterator();

			while (iterator.hasNext())
			{
				Object val = iterator.next();
				System.out.println("Renuka : " + val.getClass().getName());
				CollectionProtocolEventBean bean = (CollectionProtocolEventBean) val;
				Map specimenRequirementbeanMap = new LinkedHashMap();
				specimenRequirementbeanMap = bean.getSpecimenRequirementbeanMap();

				Iterator<String> tr = specimenRequirementbeanMap.keySet().iterator();

				while (tr.hasNext())
				{
					if (c == 1)
					{
						break;
					}
					specimenRequirementBean = (edu.wustl.catissuecore.bean.SpecimenRequirementBean) specimenRequirementbeanMap
							.get(tr.next());
					id = (Long) specimenRequirementBean.getId();
					System.out.println("Renuka you have got SR id : " + id);

					c++;

				}

			}
		}
		return specimenRequirementBean;

	}

    *//**
     * Test Specimen Add.
     * @throws DAOException 
     * @throws Exception 
     *//*

    @Test
    public void testSpecimenEdit() throws DAOException, BizLogicException
    {
          Simple Search Action
          setRequestPathInfo("/SimpleSearch");
          addRequestParameter("aliasName", "StorageContainer");
          addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)", "Specimen");
          addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)",
                      "Specimen.BARCODE.varchar");
          addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)",
                      "Starts With");
          addRequestParameter("value(SimpleConditionsNode:1_Condition_value)", "1");
          addRequestParameter("pageOf", "pageOfNewSpecimen");
          addRequestParameter("operation", "search");
          actionPerform();

          Specimen specimen = (Specimen) TestCaseUtility.getNameObjectMap("Specimen");
          DefaultBizLogic bizLogic = new DefaultBizLogic();
          List<Specimen> specimenList = null;
          specimenList = bizLogic.retrieve("Specimen");

          if (specimenList.size() > 1)
          {
                verifyForward("success");
                for (Specimen specimenFromList : specimenList)
                {

                      if (!specimenFromList.getCollectionStatus().equals("Collected")
                                  && specimenFromList.getId() != null
                                  && specimenFromList.getLineage().equals("New"))
                      {
                            TestCaseUtility.setNameObjectMap("specimen", specimenFromList);
                            setRequestPathInfo("/NewSpecimenSearch");
                            addRequestParameter("id", "" + specimenFromList.getId());
                            actionPerform();
                            verifyForward("pageOfNewSpecimen");
                            verifyNoActionErrors();

                            edit operation

                            specimenFromList.setLabel("spcimen1_" + UniqueKeyGeneratorUtil.getUniqueKey());
                            addRequestParameter("label", specimenFromList.getLabel());
                            setRequestPathInfo("/NewSpecimenEdit");
                            addRequestParameter("stContSelection", "2");
                            addRequestParameter("collectionStatus", "Collected");
                            StorageContainer container = (StorageContainer) TestCaseUtility
                                        .getNameObjectMap("StorageContainer");
                            addRequestParameter("storageContainer", container.getId().toString());
                            addRequestParameter("positionDimensionOne", "2");
                            addRequestParameter("positionDimensionTwo", "2");
                            addRequestParameter("operation", "edit");
                            actionPerform();
                            verifyForward("success");
                            verifyNoActionErrors();
                            NewSpecimenForm form = (NewSpecimenForm) getActionForm();
                            assertEquals(true, form.isAvailable());
                            TestCaseUtility.setNameObjectMap("specimen", specimenFromList);
                            break;
                      }
                }
                verifyNoActionErrors();
          }
          else if (specimenList.size() == 1)
          {
                verifyForwardPath("/SearchObject.do?pageOf=pageOfStorageContainer&operation=search&id="
                            + specimen.getId());
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
*/
}
