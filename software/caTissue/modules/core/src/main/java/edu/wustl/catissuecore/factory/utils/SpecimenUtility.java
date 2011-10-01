package edu.wustl.catissuecore.factory.utils;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimenRequirement;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.global.Status;


public class SpecimenUtility
{

	public static Specimen initSpecimenFromRequirement(Specimen specimen, SpecimenRequirement reqSpecimen)
	{
		specimen.setActivityStatus (Status.ACTIVITY_STATUS_ACTIVE.toString());
		specimen.setInitialQuantity(new Double(reqSpecimen.getInitialQuantity()));
		specimen.setAvailableQuantity(new Double(0));
		specimen.setLineage(reqSpecimen.getLineage());
		specimen.setPathologicalStatus(reqSpecimen.getPathologicalStatus());
		specimen.setCollectionStatus(Constants.COLLECTION_STATUS_PENDING);
		if (reqSpecimen.getSpecimenCharacteristics() != null)
		{
			InstanceFactory<SpecimenCharacteristics> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenCharacteristics.class);
			specimen.setSpecimenCharacteristics(instFact.createClone(reqSpecimen.getSpecimenCharacteristics()));//new SpecimenCharacteristics(reqSpecimen
			//.getSpecimenCharacteristics()));
		}
		specimen.setSpecimenType(reqSpecimen.getSpecimenType());
		specimen.setSpecimenClass(reqSpecimen.getSpecimenClass());
		specimen.setIsAvailable(Boolean.FALSE);
		specimen.setSpecimenRequirement(reqSpecimen);

		return specimen;
	}


	public static MolecularSpecimen initSpecimenFromMolecularRequirement(Specimen specimen,SpecimenRequirement reqSpecimen)
	{
		MolecularSpecimen mSpecimen =(MolecularSpecimen) initSpecimenFromRequirement(specimen, reqSpecimen);
		mSpecimen.setConcentrationInMicrogramPerMicroliter(((MolecularSpecimenRequirement) reqSpecimen).getConcentrationInMicrogramPerMicroliter());
		return mSpecimen;
	}

	/**
	 * Set DefaultSpecimenEventCollection.
	 * @param userID Long.
	 */
//	public static void setDefaultSpecimenEventCollection(Specimen specimen,Long userID)
//	{
//		final Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
//		InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
//		final User user = instFact.createObject();
//		user.setId(userID);
//		final CollectionEventParameters collectionEventParameters = EventsUtil
//		.populateCollectionEventParameters(user);
//		collectionEventParameters.setSpecimen(specimen);
//		specimenEventCollection.add(collectionEventParameters);
//
//		final ReceivedEventParameters receivedEventParameters = EventsUtil
//		.populateReceivedEventParameters(user);
//		receivedEventParameters.setSpecimen(specimen);
//		specimenEventCollection.add(receivedEventParameters);
//		specimen.setSpecimenEventCollection(specimenEventCollection);
//	}

	/**
	 * Set ConsentTierStatusCollectionFromSCG.
	 * @param specimenCollectionGroup SpecimenCollectionGroup.
	 */
	public static void setConsentTierStatusCollectionFromSCG(Specimen specimen, SpecimenCollectionGroup specimenCollectionGroup)
	{
		Collection<ConsentTierStatus> consentTierStatusCollectionN = specimen.getConsentTierStatusCollection();
		if (consentTierStatusCollectionN == null)
		{
			consentTierStatusCollectionN = new HashSet<ConsentTierStatus>();
		}

		final Collection<ConsentTierStatus> consentTierStatusCollection = specimenCollectionGroup
		.getConsentTierStatusCollection();
		final Collection<ConsentTierStatus> specConsTierColl = specimen.getConsentTierStatusCollection();
		boolean hasMoreConsents = false;
		if (consentTierStatusCollection != null && !consentTierStatusCollection.isEmpty())
		{
			final Iterator<ConsentTierStatus> iterator = consentTierStatusCollection.iterator();
			Iterator<ConsentTierStatus> specCoIterator = null;
			if (specConsTierColl != null)
			{
				specCoIterator = specConsTierColl.iterator();
				hasMoreConsents = specCoIterator.hasNext();
			}
			while (iterator.hasNext())
			{
				final ConsentTierStatus consentTierStatus = iterator.next();
				ConsentTierStatus consentTierstatusN = null;

				if (hasMoreConsents)
				{
					consentTierstatusN = (ConsentTierStatus) specCoIterator.next();
					consentTierstatusN.setConsentTier(consentTierStatus.getConsentTier());
					consentTierstatusN.setStatus(consentTierStatus.getStatus());//bug 7637
					hasMoreConsents = specCoIterator.hasNext();
				}
				else
				{
					//consentTierstatusN = new ConsentTierStatus(consentTierStatus);
					InstanceFactory<ConsentTierStatus> instFact = DomainInstanceFactory.getInstanceFactory(ConsentTierStatus.class);
					consentTierstatusN = instFact.createClone(consentTierStatus);
					consentTierStatusCollectionN.add(consentTierstatusN);
				}
			}
		}
		specimen.setConsentTierStatusCollection(consentTierStatusCollectionN);
	}



	//bug no. 7690
	/**
	 * Set PropogatingSpecimenEventCollection.
	 * @param specimenEventColl Collection.
	 * @param userId Long.
	 * @param specimen Specimen.
	 */
	public static void setPropogatingSpecimenEventCollection(Specimen persistentSpecimen, Collection specimenEventColl, Long userId,
			Specimen specimen)
	{
		InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
		final User user =instFact.createObject();
		user.setId(userId);
		String collProcedure = Constants.CP_DEFAULT;
		String collContainer = Constants.CP_DEFAULT;
		String recQty = Constants.CP_DEFAULT;
		Date collTimestamp = new Date(System.currentTimeMillis());
		Date recTimestamp = new Date(System.currentTimeMillis());
		User collEventUser = user;
		User recEventUser = user;

		Collection<SpecimenEventParameters> specimenEventParamColl=specimen.getSpecimenCollectionGroup().getSpecimenEventParametersCollection();
		if(specimenEventParamColl!=null)
		{
			final Iterator<SpecimenEventParameters> eventCollItr = specimenEventParamColl.iterator();
			while (eventCollItr.hasNext())
			{
				final SpecimenEventParameters eventParam = (SpecimenEventParameters) eventCollItr
				.next();
//				if (eventParam instanceof CollectionEventParameters)
//				{
//					collProcedure = ((CollectionEventParameters) eventParam).getCollectionProcedure();
//					collContainer = ((CollectionEventParameters) eventParam).getContainer();
//					collTimestamp = ((CollectionEventParameters) eventParam).getTimestamp();
//					collEventUser = ((CollectionEventParameters) eventParam).getUser();
//				}
//				if (eventParam instanceof ReceivedEventParameters)
//				{
//					recQty = ((ReceivedEventParameters) eventParam).getReceivedQuality();
//					recTimestamp = ((ReceivedEventParameters) eventParam).getTimestamp();
//					recEventUser = ((ReceivedEventParameters) eventParam).getUser();
//				}
			}
		}

//		final Collection<SpecimenEventParameters> specimenEventCollection = new HashSet<SpecimenEventParameters>();
//		final Iterator itr = specimenEventColl.iterator();
//		while (itr.hasNext())
//		{
//			final SpecimenEventParameters eventParam = (SpecimenEventParameters) itr.next();
//			if (eventParam instanceof CollectionEventParameters)
//			{
//				final CollectionEventParameters collEventParam = (CollectionEventParameters) eventParam;
//				if (collEventParam != null)
//				{
//					final CollectionEventParameters collectionEventParameters = (CollectionEventParameters) DomainInstanceFactory.getInstanceFactory(CollectionEventParameters.class).createClone(collEventParam);//new CollectionEventParameters(
//					//collEventParam);
//					collectionEventParameters.setSpecimen(persistentSpecimen);
//					collectionEventParameters.setTimestamp(collTimestamp);
//					collectionEventParameters.setUser(collEventUser);
//					if (!Constants.CP_DEFAULT.equals(collProcedure))
//					{
//						collectionEventParameters.setCollectionProcedure(collProcedure);
//					}
//					if (!Constants.CP_DEFAULT.equals(collContainer))
//					{
//						collectionEventParameters.setContainer(collContainer);
//					}
//					specimenEventCollection.add(collectionEventParameters);
//				}
//			}
//			if (eventParam instanceof ReceivedEventParameters)
//			{
//				final ReceivedEventParameters recEventParam = (ReceivedEventParameters) eventParam;
//				if (recEventParam != null)
//				{
//					final ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) DomainInstanceFactory.getInstanceFactory(ReceivedEventParameters.class).createClone(recEventParam);//new ReceivedEventParameters(
//					//recEventParam);
//					receivedEventParameters.setSpecimen(persistentSpecimen);
//					receivedEventParameters.setTimestamp(recTimestamp);
//					receivedEventParameters.setUser(recEventUser);
//					if (!Constants.CP_DEFAULT.equals(recQty))
//					{
//						receivedEventParameters.setReceivedQuality(recQty);
//					}
//					specimenEventCollection.add(receivedEventParameters);
//				}
//			}
//		}
//		persistentSpecimen.setSpecimenEventCollection(specimenEventCollection);
	}


	/**
	 * Do the round off for the required attributes (if any)
	 */
	public static void doRoundOff(Specimen specimen) {
		if (specimen.getInitialQuantity() != null) {
			//initialQuantity = AppUtility.truncate(initialQuantity, 5);

			specimen.setInitialQuantity(AppUtility.roundOff(specimen.getInitialQuantity(), Constants.QUANTITY_PRECISION));
		}
		if (specimen.getAvailableQuantity() != null) {
			//availableQuantity = AppUtility.truncate(availableQuantity, 5);
			specimen.setAvailableQuantity(AppUtility.roundOff(specimen.getAvailableQuantity(), Constants.QUANTITY_PRECISION));
		}
	}

}
