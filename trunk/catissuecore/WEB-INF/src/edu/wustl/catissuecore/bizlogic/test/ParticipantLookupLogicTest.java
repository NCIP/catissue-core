
package edu.wustl.catissuecore.bizlogic.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.constraint.IsAnything;
import com.mockobjects.dynamic.FullConstraintMatcher;
import com.mockobjects.dynamic.Mock;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantLookupLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.test.MockBizLogicFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * This is a Junit test class for ParticipantLookupLogic.This class uses mock objects for biz logic layer. 
 * @author santosh_chandak
 */
public class ParticipantLookupLogicTest extends BaseTestCase
{

	/**
	 * Mock for BizLogicInterface.This mock object mocks the behaviour of BizLogicInterface. 
	 */
	Mock bizLogicInterfaceMock;

	/**
	 * Constructor for ParticipantLookupLogicTest.
	 * @param arg0 Name of the test case
	 */
	public ParticipantLookupLogicTest(String name)
	{
		super(name);

	}

	/**
	 * This method makes an initial set up for test cases.
	 * 
	 * JUnit calls setUp and tearDown for each test so that there can be no side effects among test runs.
	 * Here bizLogicInterfaceMock is instantiated.As this mock depicts the behaviour of BizLogicInterface,this
	 * interface is passed as an input parameter to the constructor.
	 * 
	 * The original instance of BizLogicFactory is replaced with an instance of MockBizLogicFactory.
	 * Whenever a  call to the BizLogicFactory is made,MockBizLogicFactory will come into picture.This 
	 * mock factory will return the instance of bizLogicInterfaceMock.
	 */

	protected void setUp()
	{
		bizLogicInterfaceMock = new Mock(IBizLogic.class);

		MockBizLogicFactory factory = new MockBizLogicFactory();
		factory.setBizLogic((IBizLogic) bizLogicInterfaceMock.proxy());
		BizLogicFactory.setBizLogicFactory(factory);
		Variables.applicationHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");

		try
		{
			XMLPropertyHandler.init("C://jboss-4.0.0//server//default//catissuecore-properties//caTissueCore_Properties.xml");
		}
		catch (Exception e)
		{
			fail("can not do setup, check path of caTissueCore_Properties.xml for possible error");
		}

	}

	/**
	 * This is a negative test case.We are sending null as an input to lookup method.
	 * We are expecting  ParticipantLookupException thrown out of lookup method.
	 * If ParticipantLookupException is thrown test will pass.If any other exception is thrown
	 * the testcase will fail. 
	 * 
	 * @testDesc This test case provides proper inputs and checks for result
	 *
	 */

	public void testNullInputForLookup()
	{
		Constraint[] constraints = {new IsAnything()};
		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		bizLogicInterfaceMock.expectAndReturn("retrieve", fullConstraintMatcher, null);

		ParticipantLookupLogic participantLookupLogic = new ParticipantLookupLogic();
		List matchingParticipantList = null;
		try
		{
			matchingParticipantList = participantLookupLogic.lookup(null);
			//Testcase fails here if exception is not thrown
			fail();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		assertNull(matchingParticipantList);
	}

	/**
	 * @testDesc When we give participantList as Null, matching Participant List 
	 * returned from lookUp method is null
	 *
	 */

	public void testNullParticipantList()
	{
		DefaultLookupParameters lookupParameters = new DefaultLookupParameters();
		lookupParameters.setObject(getParticipant());
		
		ParticipantLookupLogic participantLookupLogic = new ParticipantLookupLogic();
		List matchingParicipantList = null;
		try
		{
			matchingParicipantList = participantLookupLogic.lookup(lookupParameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		assertNull(matchingParicipantList);
	}

	/**
	 * @testDesc When we give participantList as empty, matching Participant List 
	 * returned from lookUp method is null
	 *
	 */

	public void testEmptyParticipantList()
	{
		DefaultLookupParameters lookupParameters = new DefaultLookupParameters();
		lookupParameters.setObject(getParticipant());
		lookupParameters.setListOfParticipants(new ArrayList());
	
		ParticipantLookupLogic participantLookupLogic = new ParticipantLookupLogic();
		List matchingParticipantList = null;
		try
		{
			matchingParticipantList = participantLookupLogic.lookup(lookupParameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		assertNull(matchingParticipantList);

	}

	/**
	 * @testDesc This test case provides proper inputs and checks for result. As input data consists of 
	 * different cases, result is checked for varied set of input values
	 *
	 */
	public void testParticipantLookupLogic()
	{
		DefaultLookupParameters lookupParameters = new DefaultLookupParameters();
		lookupParameters.setObject(getParticipant());
		lookupParameters.setListOfParticipants(getParticipantList());
	
		ParticipantLookupLogic participantLookupLogic = new ParticipantLookupLogic();
		List matchingParicipantList = null;
		try
		{
			matchingParicipantList = participantLookupLogic.lookup(lookupParameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		assertNotNull(matchingParicipantList);

		// Insures that 4 matching participants are found
		assertTrue(matchingParicipantList.size() == 4);

		Iterator paricipentIterator = matchingParicipantList.iterator();
		Object object;
		while (paricipentIterator.hasNext())
		{
			object = paricipentIterator.next();
			assertTrue(object instanceof DefaultLookupResult);
		}

		DefaultLookupResult defaultLookupResult = (DefaultLookupResult) matchingParicipantList.get(0);
		assertNotNull(defaultLookupResult.getObject());
		assertNotNull(defaultLookupResult.getClass());
		/*int probabilityCutoff = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_CUTOFF)) * 100
				/ Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_TOTAL_POINTS));
		assertEquals(defaultLookupResult.getProbablity().intValue() >= probabilityCutoff, true);*/

		Participant participant = (Participant) defaultLookupResult.getObject();
		assertEquals(participant.getFirstName(), "santosh");
		assertEquals(participant.getMiddleName(), "hariprasad");
		assertEquals(participant.getLastName(), "chandak");
		assertNotNull(participant.getBirthDate());

		defaultLookupResult = (DefaultLookupResult) matchingParicipantList.get(1);
		assertNotNull(defaultLookupResult.getObject());
		assertNotNull(defaultLookupResult.getClass());
	//	assertEquals(defaultLookupResult.getProbablity().intValue() >= probabilityCutoff, true);

		defaultLookupResult = (DefaultLookupResult) matchingParicipantList.get(2);
		assertNotNull(defaultLookupResult.getObject());
		assertNotNull(defaultLookupResult.getClass());
	//	assertEquals(defaultLookupResult.getProbablity().intValue() >= probabilityCutoff, true);

		defaultLookupResult = (DefaultLookupResult) matchingParicipantList.get(3);
		assertNotNull(defaultLookupResult.getObject());
		assertNotNull(defaultLookupResult.getClass());
	//	assertEquals(defaultLookupResult.getProbablity().intValue() >= probabilityCutoff, true);

	}

	/**
	 * @testDesc This test case provides proper inputs and checks for result. Input data consists of
	 * 1. Flipped first name and last name as user participant
	 * 2. comma saperated first name and last name in last name field of participant in the Existing Participant List 
	 *
	 */
	public void testParticipantLookupLogicForFlipped()
	{
		DefaultLookupParameters lookupParameters = new DefaultLookupParameters();
		lookupParameters.setObject(getParticipant1());
		lookupParameters.setListOfParticipants(getParticipantListForCheckingFlipped());

		ParticipantLookupLogic participantLookupLogic = new ParticipantLookupLogic();
		List matchingParicipantList = null;
		try
		{
			matchingParicipantList = participantLookupLogic.lookup(lookupParameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		assertNotNull(matchingParicipantList);

		// Insures that 1 matching participant is found
		assertTrue(matchingParicipantList.size() == 1);

		Iterator paricipentIterator = matchingParicipantList.iterator();
		Object object;
		while (paricipentIterator.hasNext())
		{
			object = paricipentIterator.next();
			assertTrue(object instanceof DefaultLookupResult);
		}

		DefaultLookupResult defaultLookupResult = (DefaultLookupResult) matchingParicipantList.get(0);
		assertNotNull(defaultLookupResult.getObject());
		assertNotNull(defaultLookupResult.getClass());

	/*	int probabilityCutoff = Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_CUTOFF)) * 100
		/ Integer.parseInt(XMLPropertyHandler.getValue(Constants.PARTICIPANT_TOTAL_POINTS));
		assertEquals(defaultLookupResult.getProbablity().intValue() >= probabilityCutoff, true);*/

		Participant participant = (Participant) defaultLookupResult.getObject();
		assertEquals(participant.getFirstName(), "chandak");
		assertEquals(participant.getMiddleName(), "");
		assertEquals(participant.getLastName(), "santosh");
		assertNotNull(participant.getBirthDate());

	}

	/**
	 * This method returns Participant List.
	 * @return List- list of participants 
	 */
	public List getParticipantList()
	{
		List ParticipantList = new ArrayList();

		// All the participants are to be compared with participant of getParticipant()

		/**
		 * Participant no.1 --> Duplicate of user Participant
		 * All attributes of this partcipant match with user participant
		 * Expected result --> Should be there in the Matching participants List 
		 */
		ParticipantList.add(getParticipant());

		/**
		 *  Participant no.2 --> Most of the fields of this participant do not match with 
		 *  user participant.  
		 *  Expected result --> Should not be there in the Matching participants List 
		 */
		Participant participant = new Participant();
		participant.setFirstName("reetesh");
		participant.setMiddleName("g");
		participant.setLastName("");

		participant.setGender("male");
		/*String ds = "May 23, 1983";
		DateFormat df = DateFormat.getDateInstance();
		Date date = null;
		try
		{
			date = df.parse(ds);
		}
		catch (ParseException e)
		{
			System.out.println("Unable to parse " + ds);
		}*/
		participant.setBirthDate(null);

		participant.setSocialSecurityNumber("234111134");
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);

		ParticipantList.add(participant);

		/**
		 *  Participant no.3 -->  Cases considered here
		 * 1. Partial match of Social Security Number, Middle Name
		 * 2. Complete match of First Name, Last Name, Gender, DOB
		 * Expected result --> Should be there in the Matching participants List 
		 */
		participant = new Participant();
		participant.setFirstName("santosh");
		participant.setMiddleName("");
		participant.setLastName("chandak");

		participant.setGender("male");
		String ds = "April 16, 1984";
		DateFormat df = DateFormat.getDateInstance();
		Date date = null;
		try
		{
			date = df.parse(ds);
		}
		catch (ParseException e)
		{
			System.out.println("Unable to parse " + ds);
		}
		participant.setBirthDate(date);

		participant.setSocialSecurityNumber("231111112");
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);

		ParticipantList.add(participant);

		/**
		 *  Participant no.4 -->  Cases considered here
		 * 1. Complete match of First Name, Last Name, DOB 
		 * 2. All other attributes do not match
		 * Expected result --> Should be there in the Matching participants List because of 
		 * extra points given due to complete match of First Name, Last Name DOB
		 */
		participant = new Participant();
		participant.setFirstName("santosh");
		participant.setLastName("chandak");

		participant.setGender("male");
		ds = "April 16, 1984";
		df = DateFormat.getDateInstance();
		date = null;
		try
		{
			date = df.parse(ds);
		}
		catch (ParseException e)
		{
			System.out.println("Unable to parse " + ds);
		}
		participant.setBirthDate(date);

		participant.setSocialSecurityNumber("511341112");
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);

		ParticipantList.add(participant);

		/**
		 *  Participant no.5 -->  Cases considered here
		 * 1. Partial match of First Name, Last Name, Middle Name,DOB,SSN 
		 * 2. Complete match of race,Gender
		 * Expected result --> Should be there in the Matching participants List 
		 */

		participant = new Participant();
		participant.setFirstName("santy");
		participant.setMiddleName("hari");
		participant.setLastName("chand--");
		List race = new ArrayList();
		race.add("asian");
		race.add("white");
		participant.setRaceCollection(new ArrayList());
		participant.setGender("male");
		ds = "April 16, 1983";
		df = DateFormat.getDateInstance();
		date = null;
		try
		{
			date = df.parse(ds);
		}
		catch (ParseException e)
		{
			System.out.println("Unable to parse " + ds);
		}
		participant.setBirthDate(date);

		participant.setSocialSecurityNumber("231111112");
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);

		ParticipantList.add(participant);
		return ParticipantList;

	}

	/**
	 * This method returns Participant List with .
	 * @return List- list of participants 
	 */

	public List getParticipantListForCheckingFlipped()
	{
		List ParticipantList = new ArrayList();
		/**
		 *  Participant no.1 -->  Cases considered with respect to participant of getParticipant1()
		 * 1. Flipped First Name and Last name 
		 * 2. Partial match of Middle Name, SSN, race, DOB
		 * 3. Complete match of Gender
		 * Expected result --> Should be there in the Matching participants List 
		 */

		Participant participant = new Participant();
		participant.setFirstName("chandak");
		participant.setMiddleName("");
		participant.setLastName("santosh");
		List race = new ArrayList();
		race.add("white");
		participant.setRaceCollection(race);
		participant.setGender("male");
		String ds = "April 17, 1984";
		DateFormat df = DateFormat.getDateInstance();
		Date date = null;
		try
		{
			date = df.parse(ds);
		}
		catch (ParseException e)
		{
			System.out.println("Unable to parse " + ds);
		}
		participant.setBirthDate(date);

		participant.setSocialSecurityNumber("321111111");
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);

		ParticipantList.add(participant);

		return ParticipantList;
	}

	/**
	 * This method returns participant object, this is the participant entered by the user. 
	 * @return
	 */
	public Participant getParticipant()
	{
		Participant participant = new Participant();
		participant.setFirstName("santosh");
		participant.setMiddleName("hariprasad");
		participant.setLastName("chandak");
		participant.setGender("male");
		String ds = "April 16, 1984";
		DateFormat df = DateFormat.getDateInstance();
		Date date = null;
		try
		{
			date = df.parse(ds);
		}
		catch (ParseException e)
		{
			System.out.println("Unable to parse " + ds);
		}
		participant.setBirthDate(date);

		participant.setSocialSecurityNumber("231111111");
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		return participant;
	}

	/**
	 * This method returns participant object in which first name and last name are entered in the lastName
	 * @return
	 */
	public Participant getParticipant1()
	{
		Participant participant = new Participant();
		participant.setFirstName("");
		participant.setMiddleName("hariprasad");
		participant.setLastName("chandak,santosh");
		participant.setGender("male");
		String ds = "April 16, 1984";
		DateFormat df = DateFormat.getDateInstance();
		Date date = null;
		try
		{
			date = df.parse(ds);
		}
		catch (ParseException e)
		{
			System.out.println("Unable to parse " + ds);
		}
		participant.setBirthDate(date);

		participant.setSocialSecurityNumber("231111111");
		participant.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		return participant;
	}
	
}