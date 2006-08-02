package edu.wustl.catissuecore.test;

import java.util.List;

import junit.framework.TestCase;
import edu.wustl.catissuecore.bizlogic.ParticipantLookupLogic;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.XMLPropertyHandler;


public class ParticipantLookupLogicTest extends TestCase
{
	ParticipantLookupLogic participantLookupLogic=null;
	

	protected void setUp() throws Exception
	{
		super.setUp();
		participantLookupLogic = (ParticipantLookupLogic) Utility
		.getObject(XMLPropertyHandler.getValue(Constants.PARTICIPANT_LOOKUP_ALGO));
		
	}

	public void testParticipantLookup() throws Exception
	{
		Participant participant=new Participant();
		participant.setFirstName("aaa");
		participant.setMiddleName("aaa");
		participant.setLastName("aaa");
		//List participantList=participantLookupLogic.lookup(participant);
		//assertEquals("Fail",participantList.size(),0);
		
	}
	protected void tearDown() throws Exception
	{
		super.tearDown();
		participantLookupLogic=null;
	}
	

}
