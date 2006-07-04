
package edu.wustl.catissuecore.bizlogic.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.constraint.IsAnything;
import com.mockobjects.constraint.IsEqual;
import com.mockobjects.dynamic.FullConstraintMatcher;
import com.mockobjects.dynamic.Mock;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantLookupLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.test.MockBizLogicFactory;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.lookup.DefaultLookupParameters;
import edu.wustl.common.lookup.DefaultLookupResult;

/**
 * This is a Junit test class for ParticipantLookupLogic.This class uses mock objects for biz logic layer. 
 * @author sujay_narkar
 */
public class ParticipantLookupLogicTest extends TestCase
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
		Constraint[] constraints = {new IsAnything(), new IsAnything(), new IsAnything(),
				new IsAnything(), new IsAnything()};

		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		bizLogicInterfaceMock.expectAndReturn("retrieve", fullConstraintMatcher, null);

		ParticipantLookupLogic participantLookupLogic = new ParticipantLookupLogic();
		List ParicipentList = null;
		try
		{
			ParicipentList = participantLookupLogic.lookup(null);
			//Testcase fails here if exception is not thrown
			fail();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		assertNull(ParicipentList);
	}

	/**
	 * This is a negative test case.The retrive method returns null. ParticipantLookupException should be thrown 
	 * from look up method.If an ParticipantLookupException is not thrown then fail method is called.
	 * 
	 * @testDesc This test case provides proper inputs and checks for result
	 *
	 */
	public void testNullParticipentList()
	{
		DefaultLookupParameters lookupParameters = new DefaultLookupParameters();
		lookupParameters.setObject(getParticipant());
		lookupParameters.setCutoff(new Double(2));

		Constraint[] constraints = {new IsAnything(), new IsAnything(), new IsAnything(),
				new IsAnything(), new IsAnything()};

		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		bizLogicInterfaceMock.expectAndReturn("retrieve", fullConstraintMatcher, null);

		ParticipantLookupLogic participantLookupLogic = new ParticipantLookupLogic();
		List ParicipentList = null;
		try
		{
			ParicipentList = participantLookupLogic.lookup(lookupParameters);
			//Testcase fails here if exception is not thrown
			fail();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		assertNull(ParicipentList);
	}

	/**
	 * This is a negative test case.The retrive method returns an empty.
	 * The lookup method should return an empty list.
	 * 
	 * @testDesc This test case provides proper inputs and checks for result
	 *
	 */
	public void testEmptyParticipentList()
	{
		DefaultLookupParameters lookupParameters = new DefaultLookupParameters();
		lookupParameters.setObject(getParticipant());
		lookupParameters.setCutoff(new Double(2));

		/*Constraint[] constraints = {new IsAnything(), new IsAnything(), new
		 IsAnything(), new IsAnything(),new IsAnything()};*/
		Constraint[] constraints = {
				new IsEqual(new String("edu.wustl.catissuecore.domain.Participant")),
				new IsAnything(), new IsAnything(), new IsAnything(), new IsAnything()};

		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		bizLogicInterfaceMock.expectAndReturn("retrieve", fullConstraintMatcher, new ArrayList());

		ParticipantLookupLogic participantLookupLogic = new ParticipantLookupLogic();
		List paricipentList = null;
		try
		{
			paricipentList = participantLookupLogic.lookup(lookupParameters);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		assertNotNull(paricipentList);
		assertTrue(paricipentList.size() == 0);
	}

	/**
	 * This is a positive testcase.The mock method returns a list of participant objects.
	 * 
	 * @testDesc This test case provides proper inputs and checks for result
	 *
	 */
	public void testParticipantLookupLogic()
	{
		DefaultLookupParameters lookupParameters = new DefaultLookupParameters();
		lookupParameters.setObject(getParticipant());
		lookupParameters.setCutoff(new Double(2));

		Constraint[] constraints = {new IsAnything(), new IsAnything(), new IsAnything(),
				new IsAnything(), new IsAnything()};

		FullConstraintMatcher fullConstraintMatcher = new FullConstraintMatcher(constraints);
		bizLogicInterfaceMock.expectAndReturn("retrieve", fullConstraintMatcher,
				getParticipantList());

		ParticipantLookupLogic participantLookupLogic = new ParticipantLookupLogic();
		List paricipentList = null;
		try
		{
			paricipentList = participantLookupLogic.lookup(lookupParameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		assertNotNull(paricipentList);

		assertTrue(paricipentList.size() == 1);

		Iterator paricipentIterator = paricipentList.iterator();
		Object object;
		while (paricipentIterator.hasNext())
		{
			object = paricipentIterator.next();
			assertTrue(object instanceof DefaultLookupResult);
		}

		DefaultLookupResult defaultLookupResult = (DefaultLookupResult) paricipentList.get(0);
		assertNotNull(defaultLookupResult.getObject());
		assertNotNull(defaultLookupResult.getClass());
		assertNotNull(defaultLookupResult.getProbablity());

		Participant participant = (Participant) defaultLookupResult.getObject();
		assertEquals(participant.getFirstName(), "sujay");
		assertEquals(participant.getMiddleName(), "ratnakar");
		assertEquals(participant.getLastName(), "narkar");
		assertNotNull(participant.getBirthDate());

	}

	/**
	 * This method returns Participant List.
	 * @return
	 */
	public List getParticipantList()
	{
		List ParticipantList = new ArrayList();
		ParticipantList.add(getParticipant());
		return ParticipantList;

	}

	/**
	 * This method returns participant object 
	 * @return
	 */
	public Participant getParticipant()
	{
		Participant participant = new Participant();
		participant.setFirstName("sujay");
		participant.setMiddleName("ratnakar");
		participant.setLastName("narkar");

		String ds = "April 28, 1982";
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

		participant.setSocialSecurityNumber("100");
		return participant;
	}
}