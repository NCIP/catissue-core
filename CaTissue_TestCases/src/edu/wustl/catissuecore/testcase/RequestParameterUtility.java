
package edu.wustl.catissuecore.testcase;

import java.util.List;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import servletunit.struts.MockStrutsTestCase;

public class RequestParameterUtility
{

	public static void setAddSiteParams(MockStrutsTestCase testcase)
	{
		testcase.addRequestParameter("name", "Site_" + UniqueKeyGeneratorUtil.getUniqueKey());
		testcase.addRequestParameter("emailAddress", "admin@admin.com");
		testcase.addRequestParameter("coordinatorId", "1");
		testcase.addRequestParameter("street", "xyz");
		testcase.addRequestParameter("state", "Alaska");
		testcase.addRequestParameter("country", "India");
		testcase.addRequestParameter("zipCode", "335001");
		testcase.addRequestParameter("phoneNumber", "9011083118");
		testcase.addRequestParameter("city", "Sri Ganga Nagar");
		testcase.addRequestParameter("operation", "add");

	}

	public static void setEditSiteParams(MockStrutsTestCase testcase)
	{
		testcase.addRequestParameter("aliasName", "Site");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)",
				"Site");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)",
				"Site.NAME.varchar");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)",
				"Starts With");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_value)", "");
		testcase.addRequestParameter("counter", "1");
		testcase.addRequestParameter("pageOf", "pageOfSite");
		testcase.addRequestParameter("operation", "search");
	}

	public static void setEditBioHazardParams(MockStrutsTestCase testcase)
	{
		testcase.addRequestParameter("aliasName", "Biohazard");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)",
				"Biohazard");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)",
				"Biohazard.NAME.varchar");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)",
				"Starts With");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_value)", "");
		testcase.addRequestParameter("pageOf", "pageOfBioHazard");
		testcase.addRequestParameter("operation", "search");
	}

	/**
	 * Set the request parameter for adding Specimen Requirement
	 */
	public static void setAddSpecimenRequirementParams(MockStrutsTestCase testcase,
			SessionDataBean bean2)//()//(MockStrutsTestCase testcase)
	{
		testcase.addRequestParameter("collectionUserName", "" + bean2.getLastName() + ","
				+ bean2.getFirstName());

		testcase.addRequestParameter("collectionEventCollectionProcedure", "");
		testcase.addRequestParameter("collectionEventContainer", "Not Specified");
		testcase.addRequestParameter("key", "E1");
		testcase.addRequestParameter("receivedEventReceivedQuality", "Not Specified");
		testcase.addRequestParameter("collectionEventId", "1");
		testcase.addRequestParameter("collectionEventUserId", "1");
		testcase.addRequestParameter("collectionUserName", "admin,admin");
		testcase.addRequestParameter("collectionEventSpecimenId", "0");

		testcase.addRequestParameter("receivedEventId", "1");
		testcase.addRequestParameter("receivedEventUserId", "1");
		testcase.addRequestParameter("receivedUserName", "admin,admin");
		testcase.addRequestParameter("receivedEventSpecimenId", "admin,admin");

		testcase.addRequestParameter("collectionEventCollectionProcedure", "Lavage");
		testcase.addRequestParameter("collectionEventContainer", "CPT");
		testcase.addRequestParameter("className", "Tissue");
		testcase.addRequestParameter("tissueSite", "Anal canal");
		testcase.addRequestParameter("tissueSide", "Left");
		testcase.addRequestParameter("pathologicalStatus", "Metastatic");
		testcase.addRequestParameter("storageLocationForSpecimen", "Auto");
		testcase.addRequestParameter("type", "Frozen Tissue");
		testcase.addRequestParameter("collectionEventComments", "");
		testcase.addRequestParameter("receivedEventReceivedQuality", "Frozen");
		testcase.addRequestParameter("receivedEventComments", "");
		testcase.addRequestParameter("quantity", "10");
		testcase.addRequestParameter("quantityPerAliquot", "1");
		testcase.addRequestParameter("noOfAliquots", "1");
		testcase.addRequestParameter("storageLocationForAliquotSpecimen", "Virtual");
		testcase.addRequestParameter("operation", "add");
	}

	/**
	 * Set the request parameter for adding Protocol Events.
	 */
	public static void setSaveProtocolEventsParams(MockStrutsTestCase testcase)//(MockStrutsTestCase testcase)
	{
		testcase.addRequestParameter("studyCalendarEventPoint", "20");
		testcase.addRequestParameter("collectionProtocolEventkey", "-1");
		testcase.addRequestParameter("collectionPointLabel", "ECP_"
				+ UniqueKeyGeneratorUtil.getUniqueKey());
		testcase.addRequestParameter("clinicalStatus", "Not Specified");
		testcase.addRequestParameter("clinicalDiagnosis", "Not Specified");

		testcase.addRequestParameter("collectionEventId", "1");
		testcase.addRequestParameter("collectionEventUserId", "1");
		testcase.addRequestParameter("collectionUserName", "admin,admin");
		testcase.addRequestParameter("collectionEventSpecimenId", "0");

		testcase.addRequestParameter("receivedEventId", "1");
		testcase.addRequestParameter("receivedEventUserId", "1");
		testcase.addRequestParameter("receivedUserName", "admin,admin");
		testcase.addRequestParameter("receivedEventSpecimenId", "admin,admin");
		testcase.addRequestParameter("pageOf", "specimenRequirement");
		testcase.addRequestParameter("operation", "add");
	}

	/**
	 * Set the request parameter for adding Participant.
	 */
	public static void setAddParticipantParams(MockStrutsTestCase testcase,
			CollectionProtocol collectionProtocol)//(MockStrutsTestCase testcase)
	{
		testcase.addRequestParameter("lastName", "Tiwari_" + UniqueKeyGeneratorUtil.getUniqueKey());
		testcase.addRequestParameter("firstName", "Neha_" + UniqueKeyGeneratorUtil.getUniqueKey());
		testcase.addRequestParameter("gender", "Female Gender");
		testcase.addRequestParameter("vitalStatus", "Alive");
		testcase.addRequestParameter("genotype", "Klinefelter's Syndrome");
		testcase.addRequestParameter("birthDate", "01-12-1985");
		testcase.addRequestParameter("ethnicity", "Hispanic or Latino");
		testcase.addRequestParameter("raceTypes", "Asian");
		testcase.addRequestParameter("operation", "add");

		testcase
				.addRequestParameter(
						"collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_CollectionProtocol_shortTitle)",
						collectionProtocol.getShortTitle());
		testcase
				.addRequestParameter(
						"collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_registrationDate)",
						"" + "01-01-2008");
		testcase
				.addRequestParameter(
						"collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_activityStatus)",
						collectionProtocol.getActivityStatus());
		testcase
				.addRequestParameter(
						"collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_isConsentAvailable)",
						"None Defined");
		testcase
				.addRequestParameter(
						"collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_CollectionProtocol_id)",
						"" + collectionProtocol.getId());
		testcase
				.addRequestParameter(
						"collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_CollectionProtocol_Title)",
						collectionProtocol.getTitle());
		testcase
				.addRequestParameter(
						"collectionProtocolRegistrationValue(CollectionProtocolRegistration:1_protocolParticipantIdentifier)",
						"" + collectionProtocol.getId());
	}

	/**
	 * 
	 * @method getCollectionProtocolRegistration()
	 * @param collectionProtocol
	 * @return CollectionProtocolRegistration
	 * @throws BizLogicException
	 */

	public static CollectionProtocolRegistration getCollectionProtocolRegistration(
			CollectionProtocol collectionProtocol) throws BizLogicException
	{

		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<CollectionProtocolRegistration> collectionProtocolRegistrationList = null;

		try
		{
			collectionProtocolRegistrationList = bizLogic
					.retrieve("CollectionProtocolRegistration");
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
		}

		CollectionProtocolRegistration collectionProtocolRegistration2 = null;
		if (collectionProtocolRegistrationList.size() > 1)
		{

			for (CollectionProtocolRegistration collectionProtocolRegistration1 : collectionProtocolRegistrationList)
			{

				collectionProtocolRegistration2 = collectionProtocolRegistration1;
				long cpIdFromcpr = collectionProtocolRegistration2.getCollectionProtocol().getId();
				long cpId = collectionProtocol.getId();

				if (cpIdFromcpr == cpId)
				{

					break;
				}
			}
		}

		return collectionProtocolRegistration2;

	}

	/**
	 * 
	 * @param collectionProtocolRegistrationNew
	 * @return SpecimenCollectionGroup
	 * @throws BizLogicException
	 */
	public static SpecimenCollectionGroup getSCGFromCpr(
			CollectionProtocolRegistration collectionProtocolRegistrationNew)
			throws BizLogicException
	{

		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<SpecimenCollectionGroup> specimenCollectionGroupList = null;
		try
		{
			specimenCollectionGroupList = bizLogic.retrieve("SpecimenCollectionGroup");

		}
		catch (BizLogicException e)
		{
			e.printStackTrace();
		}
		SpecimenCollectionGroup specimenCollectionGroup2 = null;
		if (specimenCollectionGroupList.size() > 1)
		{
			for (SpecimenCollectionGroup specimenCollectionGroup1 : specimenCollectionGroupList)
			{
				specimenCollectionGroup2 = specimenCollectionGroup1;

				long id1 = specimenCollectionGroup1.getCollectionProtocolEvent()
						.getCollectionProtocol().getId();
				long id2 = collectionProtocolRegistrationNew.getCollectionProtocol().getId();

				if (id1 == id2)
				{

					break;
				}
			}
		}

		return specimenCollectionGroup2;
	}

	/**
	 * 
	 * @param testcase
	 * @param scgId
	 */
	public static void specimenEditParams(MockStrutsTestCase testcase, long scgId)
	{
		//Simple Search Action

		testcase.setRequestPathInfo("/SimpleSearch");
		testcase.addRequestParameter("aliasName", "StorageContainer");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)",
				"Specimen");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)",
				"Specimen.SPECIMEN_COLLECTION_GROUP_ID.bigint");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)",
				"=");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_value)", "" + scgId);
		testcase.addRequestParameter("pageOf", "pageOfNewSpecimen");
		testcase.addRequestParameter("operation", "search");
		testcase.actionPerform();

		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<Specimen> specimenList = null;
		try
		{
			specimenList = bizLogic.retrieve(Specimen.class.getName(), "specimenCollectionGroup",
					"" + scgId);

		}
		catch (BizLogicException e)
		{
			e.printStackTrace();

			testcase.fail(e.getMessage());
		}

		if (specimenList.size() > 1)
		{
			testcase.verifyForward("success");
			for (Specimen specimenFromList : specimenList)
			{

				if (!specimenFromList.getCollectionStatus().equals("Collected")
						&& specimenFromList.getId() != null
						&& specimenFromList.getLineage().equals("New"))
				{

					testcase.setRequestPathInfo("/NewSpecimenSearch");
					testcase.addRequestParameter("id", "" + specimenFromList.getId());
					testcase.actionPerform();
					testcase.verifyForward("pageOfNewSpecimen");
					//testcase.verifyNoActionErrors();
					//edit operation

					specimenFromList.setLabel("spcimen1_" + UniqueKeyGeneratorUtil.getUniqueKey());
					testcase.addRequestParameter("label", specimenFromList.getLabel());

					testcase.setRequestPathInfo("/NewSpecimenEdit");
					testcase.addRequestParameter("stContSelection", "2");

					testcase.addRequestParameter("collectionStatus", "Collected");
					StorageContainer container = (StorageContainer) TestCaseUtility
							.getNameObjectMap("StorageContainer");
					testcase.addRequestParameter("storageContainer", container.getId().toString());
					testcase.addRequestParameter("positionDimensionOne", "5");
					testcase.addRequestParameter("positionDimensionTwo", "6");
					testcase.addRequestParameter("operation", "edit");
					testcase.actionPerform();
					testcase.verifyForward("success");
					//testcase.verifyForward("pageOfNewSpecimenCPQuery");
					testcase.verifyNoActionErrors();

					NewSpecimenForm form = (NewSpecimenForm) testcase.getActionForm();

					Specimen specimen = new Specimen();
					specimen.setCollectionStatus(form.getCollectionStatus());
					specimen.setActivityStatus(form.getActivityStatus());
					specimen.setBarcode(form.getBarcode());
					specimen.setLabel(form.getLabel());
					specimen.setLineage(form.getLineage());
					specimen.setComment(form.getComments());
					specimen.setId(form.getId());
					specimen.setPathologicalStatus(form.getPathologicalStatus());
					specimen.setSpecimenType(form.getType());

					TestCaseUtility.setNameObjectMap("CollectedSpecimen", specimen);
					break;
				}
			}

		}
	}

	/**
	 * 
	 * @param testcase
	 * @param scgId
	 */

	public static void editSpecimenFunction(MockStrutsTestCase testcase, long scgId)
	{

		testcase.setRequestPathInfo("/SimpleSearch");
		testcase.addRequestParameter("aliasName", "StorageContainer");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)",
				"Specimen");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)",
				"Specimen.SPECIMEN_COLLECTION_GROUP_ID.bigint");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)",
				"=");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_value)", "" + scgId);
		testcase.addRequestParameter("pageOf", "pageOfNewSpecimen");
		testcase.addRequestParameter("operation", "search");
		testcase.actionPerform();

		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<Specimen> specimenList = null;
		try
		{
			specimenList = bizLogic.retrieve(Specimen.class.getName(), "specimenCollectionGroup",
					"" + scgId);
		}
		catch (BizLogicException e)
		{
			e.printStackTrace();

			testcase.fail(e.getMessage());
		}

		Specimen specimenParent = null;
		String parentCollectionStatus = null;
		if (specimenList.size() > 1)
		{
			testcase.verifyForward("success");
			for (Specimen specimenFromList : specimenList)
			{
				if (specimenFromList.getParentSpecimen() != null)
				{
					specimenParent = (Specimen) specimenFromList.getParentSpecimen();
					parentCollectionStatus = specimenParent.getCollectionStatus();
				}

				if (parentCollectionStatus != null)
				{
					if (parentCollectionStatus.equals("Collected")
							&& specimenFromList.getId() != null
							&& !specimenFromList.getLineage().equals("New"))
					{

						testcase.setRequestPathInfo("/NewSpecimenSearch");
						testcase.addRequestParameter("id", "" + specimenFromList.getId());
						testcase.actionPerform();
						testcase.verifyForward("pageOfNewSpecimen");
						testcase.verifyNoActionErrors();
						//edit operation

						specimenFromList.setLabel("spcimen1_"
								+ UniqueKeyGeneratorUtil.getUniqueKey());
						testcase.addRequestParameter("label", specimenFromList.getLabel());

						testcase.setRequestPathInfo("/NewSpecimenEdit");
						testcase.addRequestParameter("stContSelection", "2");

						testcase.addRequestParameter("collectionStatus", "Collected");
						StorageContainer container = (StorageContainer) TestCaseUtility
								.getNameObjectMap("StorageContainer");
						testcase.addRequestParameter("storageContainer", container.getId()
								.toString());
						testcase.addRequestParameter("positionDimensionOne", "7");
						testcase.addRequestParameter("positionDimensionTwo", "3");
						testcase.addRequestParameter("operation", "edit");
						testcase.actionPerform();
						testcase.verifyForward("success");
						testcase.verifyNoActionErrors();

						NewSpecimenForm form = (NewSpecimenForm) testcase.getActionForm();

						Specimen specimen = new Specimen();
						specimen.setCollectionStatus(form.getCollectionStatus());
						specimen.setActivityStatus(form.getActivityStatus());
						specimen.setBarcode(form.getBarcode());
						specimen.setLabel(form.getLabel());
						specimen.setLineage(form.getLineage());
						specimen.setComment(form.getComments());
						specimen.setId(form.getId());
						specimen.setPathologicalStatus(form.getPathologicalStatus());
						specimen.setSpecimenType(form.getType());

						TestCaseUtility.setNameObjectMap("CollectedSpecimen", specimen);
						break;
					}
				}
			}

		}
	}

	/**
	 * 
	 * @param testcase
	 * @param scgId
	 */
	public static void SpecimenEditCPIsClosed(MockStrutsTestCase testcase, long scgId)
	{
		//Simple Search Action

		testcase.setRequestPathInfo("/SimpleSearch");
		testcase.addRequestParameter("aliasName", "StorageContainer");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_table)",
				"Specimen");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_DataElement_field)",
				"Specimen.SPECIMEN_COLLECTION_GROUP_ID.bigint");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_Operator_operator)",
				"=");
		testcase.addRequestParameter("value(SimpleConditionsNode:1_Condition_value)", "" + scgId);
		testcase.addRequestParameter("pageOf", "pageOfNewSpecimen");
		testcase.addRequestParameter("operation", "search");
		testcase.actionPerform();

		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List<Specimen> specimenList = null;
		try
		{
			specimenList = bizLogic.retrieve(Specimen.class.getName(), "specimenCollectionGroup",
					"" + scgId);

		}
		catch (BizLogicException e)
		{
			e.printStackTrace();

			testcase.fail(e.getMessage());
		}

		if (specimenList.size() > 1)
		{
			testcase.verifyForward("success");
			for (Specimen specimenFromList : specimenList)
			{
				String activityStatus = specimenFromList.getSpecimenCollectionGroup()
						.getCollectionProtocolEvent().getCollectionProtocol().getActivityStatus();

				if (activityStatus.equals("Closed") && specimenFromList.getId() != null
						&& specimenFromList.getLineage().equals("New"))
				{

					testcase.setRequestPathInfo("/NewSpecimenSearch");
					testcase.addRequestParameter("id", "" + specimenFromList.getId());
					testcase.actionPerform();
					testcase.verifyForward("pageOfNewSpecimen");
					testcase.verifyNoActionErrors();
					//edit operation

					specimenFromList.setLabel("spcimen1_" + UniqueKeyGeneratorUtil.getUniqueKey());
					testcase.addRequestParameter("label", specimenFromList.getLabel());

					testcase.setRequestPathInfo("/NewSpecimenEdit");
					testcase.addRequestParameter("stContSelection", "2");

					testcase.addRequestParameter("collectionStatus", "Collected");
					StorageContainer container = (StorageContainer) TestCaseUtility
							.getNameObjectMap("StorageContainer");
					testcase.addRequestParameter("storageContainer", container.getId().toString());
					testcase.addRequestParameter("positionDimensionOne", "1");
					testcase.addRequestParameter("positionDimensionTwo", "4");
					testcase.addRequestParameter("operation", "edit");
					testcase.actionPerform();
					testcase.verifyForward("failure");
					testcase.verifyNoActionErrors();

					NewSpecimenForm form = (NewSpecimenForm) testcase.getActionForm();

					Specimen specimen = new Specimen();
					specimen.setCollectionStatus(form.getCollectionStatus());
					specimen.setActivityStatus(form.getActivityStatus());
					specimen.setBarcode(form.getBarcode());
					specimen.setLabel(form.getLabel());
					specimen.setLineage(form.getLineage());
					specimen.setComment(form.getComments());
					specimen.setId(form.getId());
					specimen.setPathologicalStatus(form.getPathologicalStatus());
					specimen.setSpecimenType(form.getType());

					TestCaseUtility.setNameObjectMap("CollectedSpecimen", specimen);
					break;
				}
			}

		}
	}
}
