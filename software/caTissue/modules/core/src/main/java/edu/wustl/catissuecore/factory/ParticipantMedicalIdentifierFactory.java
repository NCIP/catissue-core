
package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Site;

public class ParticipantMedicalIdentifierFactory
		implements
			InstanceFactory<ParticipantMedicalIdentifier>
{

	private static ParticipantMedicalIdentifierFactory pmiFactory;

	private ParticipantMedicalIdentifierFactory()
	{
		super();
	}

	public static synchronized ParticipantMedicalIdentifierFactory getInstance()
	{
		if (pmiFactory == null)
		{
			pmiFactory = new ParticipantMedicalIdentifierFactory();
		}
		return pmiFactory;
	}

	public ParticipantMedicalIdentifier createClone(
			ParticipantMedicalIdentifier participantMedicalIdentifier)
	{
		ParticipantMedicalIdentifier pmi = createObject();
		pmi.setId(Long.valueOf(participantMedicalIdentifier.getId().longValue()));
		pmi.setMedicalRecordNumber(participantMedicalIdentifier.getMedicalRecordNumber());
		if (participantMedicalIdentifier.getSite() != null)
		{
			InstanceFactory<Site> instFact = DomainInstanceFactory.getInstanceFactory(Site.class);
			pmi.setSite(instFact.createClone(participantMedicalIdentifier.getSite()));
			//pmi.setSite(new Site(participantMedicalIdentifier.getSite()));
		}
		return pmi;
	}

	public ParticipantMedicalIdentifier createObject()
	{
		ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
		initDefaultValues(pmi);
		return pmi;
	}

	public void initDefaultValues(ParticipantMedicalIdentifier pmi)
	{

	}

}
