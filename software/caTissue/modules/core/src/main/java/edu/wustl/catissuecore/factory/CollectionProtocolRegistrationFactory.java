package edu.wustl.catissuecore.factory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;

public class CollectionProtocolRegistrationFactory implements
		InstanceFactory<CollectionProtocolRegistration>
{
	private static CollectionProtocolRegistrationFactory collectionProtocolRegistrationFactory;

	private CollectionProtocolRegistrationFactory()
	{
		super();
	}

	public static synchronized CollectionProtocolRegistrationFactory getInstance()
	{
		if(collectionProtocolRegistrationFactory==null){
			collectionProtocolRegistrationFactory = new CollectionProtocolRegistrationFactory();
		}
		return collectionProtocolRegistrationFactory;
	}

	public CollectionProtocolRegistration createClone(CollectionProtocolRegistration obj)
	{
		CollectionProtocolRegistration cpr = createObject();

		cpr.setActivityStatus(obj.getActivityStatus());
		cpr.setCollectionProtocol(obj.getCollectionProtocol());
		cpr.setConsentSignatureDate(obj.getConsentSignatureDate());
		copyConsentTierResponseCollection(cpr);
		//cpr.setConsentWithdrawalOption(obj.getConsentWithdrawalOption());
		cpr.setConsentWitness(obj.getConsentWitness());
		//cpr.setIsConsentAvailable(obj.getIsConsentAvailable());
		cpr.setParticipant(obj.getParticipant());
		cpr.setProtocolParticipantIdentifier(obj.getProtocolParticipantIdentifier());
		cpr.setRegistrationDate(obj.getConsentSignatureDate());
		cpr.setSignedConsentDocumentURL(obj.getSignedConsentDocumentURL());
		cpr.setSpecimenCollectionGroupCollection(obj.getSpecimenCollectionGroupCollection());

		return cpr;
	}

	public CollectionProtocolRegistration createObject()
	{
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		initDefaultValues(cpr);
		return cpr;
	}

	public void initDefaultValues(CollectionProtocolRegistration obj)
	{
		obj.setSpecimenCollectionGroupCollection(new HashSet <SpecimenCollectionGroup>());
		//obj.setConsentWithdrawalOption(Constants.WITHDRAW_RESPONSE_NOACTION);
		//obj.setIsToInsertAnticipatorySCGs(Boolean.TRUE);
		obj.setParticipant(null);
	}

	private void copyConsentTierResponseCollection(CollectionProtocolRegistration cpr)
	{
		if (cpr.getConsentTierResponseCollection() != null)
		{
			final Collection<ConsentTierResponse> consentTierResponseCollClone = new HashSet<ConsentTierResponse>();
			final Iterator<ConsentTierResponse> itr = cpr.getConsentTierResponseCollection().iterator();
			while (itr.hasNext())
			{
				final ConsentTierResponse consentTierResponse = itr.next();
				InstanceFactory<ConsentTierResponse> instFact = DomainInstanceFactory.getInstanceFactory(ConsentTierResponse.class);
				final ConsentTierResponse consentTierResponseClone = instFact.createClone(consentTierResponse);
				consentTierResponseCollClone.add(consentTierResponseClone);
			}

			cpr.setConsentTierResponseCollection(consentTierResponseCollClone);
		}
		else
		{
			cpr.setConsentTierResponseCollection(null);
		}
	}
}
