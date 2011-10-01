
package edu.wustl.catissuecore.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;

public class ParticipantFactory implements InstanceFactory<Participant>
{
	private static ParticipantFactory participantFactory;

	private ParticipantFactory() {
		super();
	}

	public static synchronized ParticipantFactory getInstance() {
		if(participantFactory == null) {
			participantFactory = new ParticipantFactory();
		}
		return participantFactory;
	}

	public Participant createClone(Participant obj)
	{
		Participant participant = createObject();//new Participant();

		participant.setId(Long.valueOf(obj.getId().longValue()));
		participant.setLastName(obj.getLastName());
		participant.setFirstName(obj.getFirstName());
		participant.setMiddleName(obj.getMiddleName());
		participant.setBirthDate(obj.getBirthDate());
		participant.setGender(obj.getGender());
		participant.setSexGenotype(obj.getSexGenotype());
		participant.setEthnicity(obj.getEthnicity());
		participant.setSocialSecurityNumber(obj.getSocialSecurityNumber());
		participant.setActivityStatus(obj.getActivityStatus());
		participant.setDeathDate(obj.getDeathDate());
		participant.setVitalStatus(obj.getVitalStatus());
		participant.setCollectionProtocolRegistrationCollection(null);

		final Collection<Race> raceCollection = new ArrayList<Race>();
		final Iterator<Race> raceItr = obj.getRaceCollection().iterator();
		InstanceFactory<Race> instFact = DomainInstanceFactory.getInstanceFactory(Race.class);
		while (raceItr.hasNext())
		{
			final Race race = instFact.createClone(raceItr.next());
			race.setParticipant(participant);
			raceCollection.add(race);
		}
		participant.setRaceCollection(raceCollection);

		final Collection<ParticipantMedicalIdentifier> pmiCollection = new ArrayList<ParticipantMedicalIdentifier>();
		if (participant.getParticipantMedicalIdentifierCollection() != null)
		{
			final Iterator<ParticipantMedicalIdentifier> pmiItr = participant
					.getParticipantMedicalIdentifierCollection().iterator();
			InstanceFactory<ParticipantMedicalIdentifier> pmiInstFact = DomainInstanceFactory.getInstanceFactory(ParticipantMedicalIdentifier.class);
			while (pmiItr.hasNext())
			{
				final ParticipantMedicalIdentifier pmi =pmiInstFact.createClone(pmiItr.next());
				pmi.setParticipant(participant);
				pmiCollection.add(pmi);
			}
			participant.setParticipantMedicalIdentifierCollection(pmiCollection);
		}
		return participant;

	}

	public void initDefaultValues(Participant obj)
	{
		obj.setCollectionProtocolRegistrationCollection(new HashSet<CollectionProtocolRegistration>());
		obj.setParticipantMedicalIdentifierCollection(new LinkedHashSet<ParticipantMedicalIdentifier>());
		obj.setRaceCollection(new HashSet<Race>());
		obj.setParticipantRecordEntryCollection(new HashSet<ParticipantRecordEntry>());
	}

	public Participant createObject()
	{
		Participant p = new Participant();
		initDefaultValues(p);
		return p;
	}

}
