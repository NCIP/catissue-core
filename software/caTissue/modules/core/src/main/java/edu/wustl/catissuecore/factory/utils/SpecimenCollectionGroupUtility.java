package edu.wustl.catissuecore.factory.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;


public class SpecimenCollectionGroupUtility
{
	/**
	 * Set ConsentTier Status Collection From CPR.
	 * @param collectionProtocolRegistration of CollectionProtocolRegistration type.
	 */
	public static SpecimenCollectionGroup setConsentTierStatusCollectionFromCPR(
			CollectionProtocolRegistration collectionProtocolRegistration, SpecimenCollectionGroup scg)
	{
		Collection<ConsentTierStatus> consentTierStatusCollectionN = scg.getConsentTierStatusCollection();
		if (consentTierStatusCollectionN == null)
		{
			consentTierStatusCollectionN = new HashSet<ConsentTierStatus>();
		}
		final Collection<ConsentTierResponse> consentTierResponseCollection = collectionProtocolRegistration
		.getConsentTierResponseCollection();
		final Collection<ConsentTierStatus> scgConsTierColl = scg.getConsentTierStatusCollection();
		boolean hasMoreConsents = false;

		try
		{
			if (consentTierResponseCollection != null && !consentTierResponseCollection.isEmpty())
			{
				final Iterator<ConsentTierResponse> iterator = consentTierResponseCollection.iterator();
				Iterator<ConsentTierStatus> scgIterator = null;
				if (scgConsTierColl != null)
				{
					scgIterator = scgConsTierColl.iterator();
					hasMoreConsents = scgIterator.hasNext();
				}
				while (iterator.hasNext())
				{
					final ConsentTierResponse consentTierResponse = iterator
					.next();
					ConsentTierStatus consentTierstatusN;
					if (hasMoreConsents)
					{
						consentTierstatusN = scgIterator.next();
					}
					else
					{
						consentTierstatusN = (ConsentTierStatus)DomainInstanceFactory.getInstanceFactory(ConsentTierStatus.class).createObject();//new ConsentTierStatus();
						consentTierStatusCollectionN.add(consentTierstatusN);
					}
					InstanceFactory<ConsentTier> instFact = DomainInstanceFactory.getInstanceFactory(ConsentTier.class);
					final ConsentTier consentTier = instFact.createClone(consentTierResponse.getConsentTier());//new ConsentTier(consentTierResponse
							//.getConsentTier());
					consentTierstatusN.setConsentTier(consentTier);
					consentTierstatusN.setStatus(consentTierResponse.getResponse());

				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		scg.setConsentTierStatusCollection(consentTierStatusCollectionN);
		return scg;
	}

	public static SpecimenCollectionGroup createSCGFromCollProtEvent(CollectionProtocolEvent collectionProtocolEvent)
	{
		InstanceFactory<SpecimenCollectionGroup> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenCollectionGroup.class);
		SpecimenCollectionGroup scg=instFact.createObject();
		scg.setCollectionProtocolEvent(collectionProtocolEvent);
		scg.setActivityStatus(collectionProtocolEvent.getActivityStatus());
		scg.setClinicalDiagnosis(collectionProtocolEvent.getClinicalDiagnosis());
		scg.setClinicalStatus(collectionProtocolEvent.getClinicalStatus());

		scg.setCollectionStatus(Constants.COLLECTION_STATUS_PENDING);
		return scg;
	}

	/**
	 * Set Default Specimen Group Name.
	 * @param collectionProtocol of CollectionProtocol type.
	 * @param ParticipantId of integer type.
	 * @param SCGId of integer type.
	 */
	/*public void setDefaultSpecimenGroupName(CollectionProtocol collectionProtocol,
			int ParticipantId, int SCGId)
	{
		final String collectionProtocolTitle = collectionProtocol.getTitle();
		String maxCollTitle = collectionProtocolTitle;
		if (collectionProtocolTitle.length() > Constants.COLLECTION_PROTOCOL_TITLE_LENGTH)
		{
			maxCollTitle = collectionProtocolTitle.substring(0,
					Constants.COLLECTION_PROTOCOL_TITLE_LENGTH - 1);
		}
		this.setName(maxCollTitle + "_" + ParticipantId + "_" + SCGId);
	}*/

}
