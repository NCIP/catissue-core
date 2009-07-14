
package edu.wustl.catissuecore.testcase.biospecimen;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.junit.Test;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.testcase.CaTissueSuiteBaseTest;
import edu.wustl.catissuecore.testcase.util.RequestParameterUtility;
import edu.wustl.catissuecore.testcase.util.TestCaseUtility;
import edu.wustl.catissuecore.testcase.util.UniqueKeyGeneratorUtil;
import edu.wustl.common.beans.SessionDataBean;
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
	 * @method : testAddCollectionProtocol()
	 */
	@Test
	public void testAddCollectionProtocol()
	{
		/*Collection Protocol Details*/
		setRequestPathInfo("/OpenCollectionProtocol");
		addRequestParameter("isParticiapantReg", "true");
		addRequestParameter("principalInvestigatorId", "1");
		addRequestParameter("title", "CP_11481_" + UniqueKeyGeneratorUtil.getUniqueKey());
		addRequestParameter("shortTitle", "CP_11481_" + UniqueKeyGeneratorUtil.getUniqueKey());
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
		RequestParameterUtility.setSaveProtocolEventsParams(this);
		//setSaveProtocolEventsParams();
		actionPerform();
		verifyForwardPath("/CreateSpecimenTemplate.do?operation=add");

		//1st specimen requirement.

		setRequestPathInfo("/SaveSpecimenRequirements");
		addRequestParameter("displayName", "spreq_1_" + UniqueKeyGeneratorUtil.getUniqueKey());

		SessionDataBean bean1 = (SessionDataBean) getSession().getAttribute("sessionData");
		RequestParameterUtility.setAddSpecimenRequirementParams(this, bean1);

		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		//2nd specimen requirement.

		setRequestPathInfo("/SaveSpecimenRequirements");
		addRequestParameter("displayName", "spreq_2_" + UniqueKeyGeneratorUtil.getUniqueKey());
		SessionDataBean bean2 = (SessionDataBean) getSession().getAttribute("sessionData");
		RequestParameterUtility.setAddSpecimenRequirementParams(this, bean2);

		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		setRequestPathInfo("/SubmitCollectionProtocol");
		addRequestParameter("operation", "add");
		actionPerform();
		verifyForward("success");
		verifyNoActionErrors();

		Map innerLoopValues = (Map) getSession().getAttribute("collectionProtocolEventMap");

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

	/**
	 * Test Participant Add.
	 * @method : testParticipantAdd()
	 */
	@Test
	public void testParticipantAdd()
	{
		//Participant add and registration

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility
				.getNameObjectMap("CollectionProtocolCloseActivityStatus");

		RequestParameterUtility.setAddParticipantParams(this, collectionProtocol);

		setRequestPathInfo("/ParticipantAdd");
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

		Map mapCollectionProtocolRegistrationCollection = form
				.getCollectionProtocolRegistrationValues();
		MapDataParser parserCollectionProtocolRegistrationCollection = new MapDataParser(
				"edu.wustl.catissuecore.domain");

		try
		{
			participant
					.setCollectionProtocolRegistrationCollection(parserCollectionProtocolRegistrationCollection
							.generateData(mapCollectionProtocolRegistrationCollection));

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		TestCaseUtility.setNameObjectMap("ParticipantCloseActivityStatus", participant);
	}

	/**
	 * 
	 * Test Specimen Edit.
	 * @method : testSpecimenEdit()
	 * @throws BizLogicException
	 */
	@Test
	public void testSpecimenEdit() throws BizLogicException
	{

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility
				.getNameObjectMap("CollectionProtocolCloseActivityStatus");

		CollectionProtocolRegistration collectionProtocolRegistration3 = null;
		SpecimenCollectionGroup specimenCollectionGroup3 = null;

		collectionProtocolRegistration3 = RequestParameterUtility
				.getCollectionProtocolRegistration(collectionProtocol);

		specimenCollectionGroup3 = RequestParameterUtility
				.getSCGFromCpr(collectionProtocolRegistration3);

		RequestParameterUtility.specimenEditParams(this, specimenCollectionGroup3.getId());

	}

	/**
	 * Test CollectionProtocol Edit. Collection protocol activity status is closed in this case.
	 * @method : testCollectionProtocolEdit()
	 */
	@Test
	public void testCollectionProtocolEdit()
	{

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility
				.getNameObjectMap("CollectionProtocolCloseActivityStatus");

		addRequestParameter("title", collectionProtocol.getTitle());
		addRequestParameter("shortTitle", collectionProtocol.getShortTitle());
		setRequestPathInfo("/CollectionProtocolEdit");
		addRequestParameter("activityStatus", "Closed");
		addRequestParameter("operation", "edit");
		actionPerform();
		verifyForward("success");

		TestCaseUtility.setNameObjectMap("CollectionProtocolCloseActivityStatus_edit",
				collectionProtocol);

	}

	/**
	 * Test Specimen Edit When CP is closed and parent specimen is collected.
	 * @method :testSpecimenEditWhenCpclosedAndParentSpecimenIsCollected()
	 * @throws BizLogicException
	 */
	@Test
	public void testSpecimenEditWhenCpclosedAndParentSpecimenIsCollected() throws BizLogicException
	{

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility
				.getNameObjectMap("CollectionProtocolCloseActivityStatus");
		CollectionProtocolRegistration collectionProtocolRegistration3 = null;
		SpecimenCollectionGroup specimenCollectionGroup3 = null;
		collectionProtocolRegistration3 = RequestParameterUtility
				.getCollectionProtocolRegistration(collectionProtocol);
		specimenCollectionGroup3 = RequestParameterUtility
				.getSCGFromCpr(collectionProtocolRegistration3);
		Specimen parentSpecimenCollected = (Specimen) TestCaseUtility
				.getNameObjectMap("CollectedSpecimen");
		RequestParameterUtility.editSpecimenFunction(this, specimenCollectionGroup3.getId());

	}

	/**
	 * Test Specimen Edit When CP is closed and parent specimen is not collected.
	 * @method :testSpecimenEditWhenCpclosedAndParentSpecimenIsNotCollected()
	 * @throws BizLogicException
	 */

	@Test
	public void testSpecimenEditWhenCpclosedAndParentSpecimenIsNotCollected()
			throws BizLogicException
	{

		CollectionProtocol collectionProtocol = (CollectionProtocol) TestCaseUtility
				.getNameObjectMap("CollectionProtocolCloseActivityStatus");

		CollectionProtocolRegistration collectionProtocolRegistration3 = null;
		SpecimenCollectionGroup specimenCollectionGroup3 = null;

		collectionProtocolRegistration3 = RequestParameterUtility
				.getCollectionProtocolRegistration(collectionProtocol);

		specimenCollectionGroup3 = RequestParameterUtility
				.getSCGFromCpr(collectionProtocolRegistration3);

		RequestParameterUtility.SpecimenEditCPIsClosed(this, specimenCollectionGroup3.getId());

	}

}
