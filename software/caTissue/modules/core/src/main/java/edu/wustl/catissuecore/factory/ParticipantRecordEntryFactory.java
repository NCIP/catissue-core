package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;


public class ParticipantRecordEntryFactory implements InstanceFactory<ParticipantRecordEntry>
{

	private static ParticipantRecordEntryFactory participantRecordEntryFactory;

	private ParticipantRecordEntryFactory()
	{
		super();
	}

	public static synchronized ParticipantRecordEntryFactory getInstance()
	{
		if(participantRecordEntryFactory==null){
			participantRecordEntryFactory = new ParticipantRecordEntryFactory();
		}
		return participantRecordEntryFactory;
	}


	public ParticipantRecordEntry createClone(ParticipantRecordEntry t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ParticipantRecordEntry createObject()
	{
		ParticipantRecordEntry entry = new ParticipantRecordEntry();
		initDefaultValues(entry);
		return entry;
	}

	public void initDefaultValues(ParticipantRecordEntry t)
	{
		// TODO Auto-generated method stub

	}
}
