package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

// participant transformer
@InputUIRepOfDomain(ParticipantForm.class)
public class ParticipantTransformer implements UIDomainTransformer<ParticipantForm, Participant> {
	private static final Logger logger = Logger.getCommonLogger(ParticipantTransformer.class);

	public Participant createDomainObject(ParticipantForm uiRepOfDomain) {
		InstanceFactory<Participant> instFact = DomainInstanceFactory.getInstanceFactory(Participant.class);
		Participant participant =instFact.createObject();// new Participant();
		overwriteDomainObject(participant, uiRepOfDomain);
		return participant;
	}

	public void overwriteDomainObject(Participant domainObject, ParticipantForm uiRepOfDomain) {
		try {
			final Validator validator = new Validator();

			domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());
			domainObject.setFirstName(uiRepOfDomain.getFirstName());
			domainObject.setMiddleName(uiRepOfDomain.getMiddleName());
			domainObject.setLastName(uiRepOfDomain.getLastName());

			if (validator.isValidOption(uiRepOfDomain.getGender())) {
				domainObject.setGender(uiRepOfDomain.getGender());
			} else {
				domainObject.setGender(null);
			}

			if (validator.isValidOption(uiRepOfDomain.getGenotype())) {
				domainObject.setSexGenotype(uiRepOfDomain.getGenotype());
			} else {
				domainObject.setSexGenotype(null);
			}

			if (validator.isValidOption(uiRepOfDomain.getEthnicity())) {
				domainObject.setEthnicity(uiRepOfDomain.getEthnicity());
			} else {
				domainObject.setEthnicity(null);
			}

			// if(validator.isValidOption(uiRepOfDomain.getRace()) )
				// domainObject.getRace() = uiRepOfDomain.getRace();
			// else
				// domainObject.getRace() = null;
			domainObject.getRaceCollection().clear();
			InstanceFactory<Race> instFact = DomainInstanceFactory.getInstanceFactory(Race.class);
			final String[] raceTypes = uiRepOfDomain.getRaceTypes();
			if (raceTypes != null) {
				for (int i = 0; i < raceTypes.length; i++) {
					if (!raceTypes[i].equals("-1")) {
						final Race race = instFact.createObject();//new Race();
						race.setRaceName(raceTypes[i]);
						race.setParticipant(domainObject);
						domainObject.getRaceCollection().add(race);
					}

				}
			}

			final String socialSecurityNumberTemp = uiRepOfDomain.getSocialSecurityNumberPartA() + "-"
			+ uiRepOfDomain.getSocialSecurityNumberPartB() + "-" + uiRepOfDomain.getSocialSecurityNumberPartC();

			if (!Validator.isEmpty(socialSecurityNumberTemp) && validator.isValidSSN(socialSecurityNumberTemp)) {
				domainObject.setSocialSecurityNumber(socialSecurityNumberTemp);
			} else {
				domainObject.setSocialSecurityNumber(null);
			}

			domainObject.setBirthDate(CommonUtilities.parseDate(uiRepOfDomain.getBirthDate(), CommonUtilities
					.datePattern(uiRepOfDomain.getBirthDate())));

			domainObject.setDeathDate(CommonUtilities.parseDate(uiRepOfDomain.getDeathDate(), CommonUtilities
					.datePattern(uiRepOfDomain.getDeathDate())));

			if (validator.isValidOption(uiRepOfDomain.getVitalStatus())) {
				domainObject.setVitalStatus(uiRepOfDomain.getVitalStatus());
			} else {
				domainObject.setVitalStatus(null);
			}

			domainObject.getParticipantMedicalIdentifierCollection().clear();
			final Map map = uiRepOfDomain.getValues();
			logger.debug("Map " + map);
			final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
			domainObject.setParticipantMedicalIdentifierCollection(parser.generateData(map));

			// Collection Protocol Registration of the participant
			// (Abhishek Mehta)
			if(domainObject.getCollectionProtocolRegistrationCollection()==null)
			{
				domainObject.setCollectionProtocolRegistrationCollection(new HashSet<CollectionProtocolRegistration>());
			}
			domainObject.getCollectionProtocolRegistrationCollection().clear();
			final Map mapCollectionProtocolRegistrationCollection = uiRepOfDomain
			.getCollectionProtocolRegistrationValues();
			logger.debug("Map " + map);
			final MapDataParser parserCollectionProtocolRegistrationCollection = new MapDataParser(
			"edu.wustl.catissuecore.domain");
			//removeIsConsentAvailableFromMap(mapCollectionProtocolRegistrationCollection);
			domainObject.setCollectionProtocolRegistrationCollection(parserCollectionProtocolRegistrationCollection
					.generateData(mapCollectionProtocolRegistrationCollection));
			logger.debug("ParticipantMedicalIdentifierCollection "
					+ domainObject.getParticipantMedicalIdentifierCollection());

			setConsentsResponseToCollectionProtocolRegistration(domainObject, uiRepOfDomain);
		} catch (final Exception excp) {
			// // use of logger as per bug 79
			// Participant.logger.error(excp.getMessage(), excp);
			excp.printStackTrace();
			// final ErrorKey errorKey =
			// ErrorKey.getErrorKey("assign.data.error");
			// throw new AssignDataException(errorKey, null, "Participant.java
			// :");
		}
	}

	/*private void removeIsConsentAvailableFromMap(Map map)
	{
		List arrayList=new ArrayList();
        Set s=map.entrySet();
        Iterator it=s.iterator();
        while(it.hasNext())
        {
            Map.Entry m =(Map.Entry)it.next();
            String key=(String)m.getKey();
          if(key.contains("isConsentAvailable"))
          {
        	  arrayList.add(key);
          }
        }
        for (Iterator iterator = arrayList.iterator(); iterator.hasNext();)
		{
			map.remove(iterator.next());
		}

	}*/
	/**
	 * Setting Consent Response for the collection protocol.
	 *
	 * @param uiRepOfDomain ParticipantForm.
	 * @throws Exception : Exception
	 */
	private void setConsentsResponseToCollectionProtocolRegistration(Participant domainObject,
			ParticipantForm uiRepOfDomain) {
		logger.debug(":: participant id  :" + uiRepOfDomain.getId());
		final Collection<ConsentResponseBean> consentResponseBeanCollection = uiRepOfDomain
		.getConsentResponseBeanCollection();
		Collection<CollectionProtocolRegistration> cprCollection=domainObject.getCollectionProtocolRegistrationCollection();
		if(cprCollection!=null)
		{
			final Iterator<CollectionProtocolRegistration> itr =cprCollection.iterator();
			while (itr.hasNext()) {
				final CollectionProtocolRegistration collectionProtocolRegistration = (CollectionProtocolRegistration) itr
				.next();
				setConsentResponse(domainObject, collectionProtocolRegistration, consentResponseBeanCollection);
			}
		}
	}

	/**
	 * Set Consent Response for given collection protocol.
	 *
	 * @param collectionProtocolRegistration CollectionProtocolRegistration.
	 * @param consentResponseBeanCollection Collection.
	 * @throws Exception : Exception
	 */
	private void setConsentResponse(Participant domainObject,
			CollectionProtocolRegistration collectionProtocolRegistration, Collection<ConsentResponseBean> consentResponseBeanCollection) {
		try {
			if (consentResponseBeanCollection != null && !consentResponseBeanCollection.isEmpty()) {
				final Iterator<ConsentResponseBean> itr = consentResponseBeanCollection.iterator();
				while (itr.hasNext()) {
					final ConsentResponseBean consentResponseBean = itr.next();
					final long cpIDcollectionProtocolRegistration = collectionProtocolRegistration
					.getCollectionProtocol().getId().longValue();
					final long cpIDconsentRegistrationBean = consentResponseBean.getCollectionProtocolID();
					if (cpIDcollectionProtocolRegistration == cpIDconsentRegistrationBean) {

						logger.debug(":: collection protocol id :" + cpIDcollectionProtocolRegistration);
						logger.debug(":: collection protocol Registration id  :"
								+ collectionProtocolRegistration.getId());

						final String signedConsentUrl = consentResponseBean.getSignedConsentUrl();
						final long witnessId = consentResponseBean.getWitnessId();
						final String consentDate = consentResponseBean.getConsentDate();
						final Collection<ConsentTierResponse> consentTierResponseCollection = prepareConsentTierResponseCollection(
								consentResponseBean.getConsentResponse(), true);

						collectionProtocolRegistration.setSignedConsentDocumentURL(signedConsentUrl);
						if (witnessId > 0) {
							InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
							final User consentWitness =instFact.createObject();
							consentWitness.setId(Long.valueOf(witnessId));
							collectionProtocolRegistration.setConsentWitness(consentWitness);
						}

						collectionProtocolRegistration.setConsentSignatureDate(CommonUtilities.parseDate(consentDate));
						collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
						//collectionProtocolRegistration.setConsentWithdrawalOption(consentResponseBean
								//.getConsentWithdrawalOption());
						break;
					}
				}
			} else
				// Setting default response to collection protocol
			{
				if (collectionProtocolRegistration.getCollectionProtocol() != null) {
					final String cpIDcollectionProtocolRegistration = collectionProtocolRegistration
					.getCollectionProtocol().getId().toString();
					final Collection consentTierCollection = getConsentList(cpIDcollectionProtocolRegistration);

					final Collection<ConsentTierResponse> consentTierResponseCollection = prepareConsentTierResponseCollection(
							consentTierCollection, false);
					collectionProtocolRegistration.setConsentTierResponseCollection(consentTierResponseCollection);
				}
			}
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}

	/**
	 * Preparing consent response collection from entered response.
	 *
	 * @param consentResponse Collection.
	 * @param isResponse boolean.
	 * @return Collection.
	 */
	private Collection<ConsentTierResponse> prepareConsentTierResponseCollection(Collection consentResponse, boolean isResponse) {
		final Collection<ConsentTierResponse> consentTierResponseCollection = new HashSet<ConsentTierResponse>();
		if (consentResponse != null && !consentResponse.isEmpty()) {
			if (isResponse) {
				final Iterator iter = consentResponse.iterator();
				ConsentUtil.createConsentResponseColl(consentTierResponseCollection, iter);
			} else {
				final Iterator iter = consentResponse.iterator();
				while (iter.hasNext()) {
					final ConsentTier consentTier = (ConsentTier) iter.next();
					final ConsentTierResponse consentTierResponse = (ConsentTierResponse)DomainInstanceFactory.getInstanceFactory(ConsentTierResponse.class).createObject();//new ConsentTierResponse();
					consentTierResponse.setResponse(Constants.NOT_SPECIFIED);
					consentTierResponse.setConsentTier(consentTier);
					consentTierResponseCollection.add(consentTierResponse);
				}
			}
		}
		return consentTierResponseCollection;
	}

	/**
	 * Consent List for given collection protocol.
	 *
	 * @param collectionProtocolID String.
	 * @return Collection.
	 * @throws BizLogicException : BizLogicException
	 * @throws NumberFormatException : NumberFormatException
	 */
	private Collection getConsentList(String collectionProtocolID) throws NumberFormatException, BizLogicException {
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final CollectionProtocolBizLogic collectionProtocolBizLogic = (CollectionProtocolBizLogic) factory
		.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
		final Collection consentTierCollection = (Collection) collectionProtocolBizLogic.retrieveAttribute(
				CollectionProtocol.class.getName(), Long.valueOf(collectionProtocolID),
				"elements(consentTierCollection)");
		return consentTierCollection;
	}
}
