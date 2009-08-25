/**
 * <p>Title: ParticipantRegistrationSelectAction Class>
 * <p>Description:	This Class is used when participant is selected from the list.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author abhishek_mehta
 * @Created on June 06, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 *
 */
public class ParticipantRegistrationSelectAction extends CommonAddEditAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(ParticipantRegistrationSelectAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		ActionForward forward = null;
		try
		{
			AbstractDomainObject abstractDomain = null;

			final ParticipantForm participantForm = (ParticipantForm) form;
			final IDomainObjectFactory iDomainObjectFactory = AbstractFactoryConfig.getInstance()
					.getDomainObjectFactory();
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory.getBizLogic(participantForm.getFormId());

			final String objectName = iDomainObjectFactory.getDomainObjectName(participantForm
					.getFormId());

			this.logger.info("Participant Id-------------------"
					+ request.getParameter("participantId"));

			final Object object = bizLogic.retrieve(objectName, new Long(request
					.getParameter("participantId")));
			abstractDomain = (AbstractDomainObject) object;
			final Participant participant = (Participant) abstractDomain;

			this.logger.info("Last name in ParticipantSelectAction:" + participant.getLastName());

			// To append the cpr to already existing cprs
			//Gets the collection Protocol Registration map from ActionForm
			final Map mapCollectionProtocolRegistration = participantForm
					.getCollectionProtocolRegistrationValues();
			final int cprCount = participantForm.getCollectionProtocolRegistrationValueCounter();
			final Collection consentResponseBeanCollection = participantForm
					.getConsentResponseBeanCollection();
			final Map consentResponseHashTable = participantForm.getConsentResponseHashTable();
			final Map mapParticipantMedicalIdentifier = this
					.participantMedicalIdentifierMap(participantForm.getValues());

			//Gets the collection Protocol Registration map from Database
			final DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			defaultBizLogic.populateUIBean(Participant.class.getName(), participant.getId(),
					participantForm);

			final Map mapCollectionProtocolRegistrationOld = participantForm
					.getCollectionProtocolRegistrationValues();
			final int cprCountOld = participantForm.getCollectionProtocolRegistrationValueCounter();
			final Collection consentResponseBeanCollectionOld = participantForm
					.getConsentResponseBeanCollection();
			final Map consentResponseHashTableOld = participantForm.getConsentResponseHashTable();

			final Map mapCollectionProtocolRegistrationAppended = this
					.appendCollectionProtocolRegistrations(mapCollectionProtocolRegistration,
							cprCount, mapCollectionProtocolRegistrationOld, cprCountOld);
			final Map mapParticipantMedicalIdentifierOld = this
					.participantMedicalIdentifierMap(participantForm.getValues());

			if (consentResponseBeanCollection != null)
			{
				this.updateConsentResponse(consentResponseBeanCollection,
						consentResponseBeanCollectionOld, consentResponseHashTableOld);
			}

			participantForm
					.setCollectionProtocolRegistrationValues(mapCollectionProtocolRegistrationAppended);
			participantForm.setCollectionProtocolRegistrationValueCounter((cprCountOld + cprCount));
			participantForm.setValues(mapParticipantMedicalIdentifierOld);
			participantForm.setConsentResponseBeanCollection(consentResponseBeanCollectionOld);
			participantForm.setConsentResponseHashTable(consentResponseHashTableOld);

			forward = super.execute(mapping, participantForm, request, response);

			if (!forward.getName().equals("failure"))
			{
				request.removeAttribute("participantForm");
				request.setAttribute("participantForm1", participantForm);
				request.setAttribute("participantSelect", "yes");
			}
			else
			{
				participantForm
						.setCollectionProtocolRegistrationValues(mapCollectionProtocolRegistration);
				participantForm.setCollectionProtocolRegistrationValueCounter(cprCount);
				participantForm.setValues(mapParticipantMedicalIdentifier);
				participantForm.setConsentResponseBeanCollection(consentResponseBeanCollection);
				participantForm.setConsentResponseHashTable(consentResponseHashTable);
				request.setAttribute("continueLookup", "yes");
			}
		}
		catch (final Exception e)
		{
			this.logger.info(e.getMessage());
		}

		return forward;
	}

	/**
	 *
	 * @param consentResponseBeanCollection : consentResponseBeanCollection
	 * @param consentResponseBeanCollectionOld : consentResponseBeanCollectionOld
	 * @param consentResponseHashTableOld : consentResponseHashTableOld
	 */
	private void updateConsentResponse(Collection consentResponseBeanCollection,
			Collection consentResponseBeanCollectionOld, Map consentResponseHashTableOld)
	{
		final Iterator it = consentResponseBeanCollection.iterator();
		while (it.hasNext())
		{
			final ConsentResponseBean consentResponseBean = (ConsentResponseBean) it.next();
			final long collectionProtocolId = consentResponseBean.getCollectionProtocolID();
			if (collectionProtocolId > 0)
			{
				if (!this.isAlreadyExist(consentResponseBeanCollectionOld, collectionProtocolId))
				{
					consentResponseBeanCollectionOld.add(consentResponseBean);
					final String key = Constants.CONSENT_RESPONSE_KEY + collectionProtocolId;
					consentResponseHashTableOld.put(key, consentResponseBean);
				}
			}
		}
	}

	/**
	 *
	 * @param consentResponseBeanCollection : consentResponseBeanCollection
	 * @param collectionProtocolId : collectionProtocolId
	 * @return boolean : boolean
	 */
	private boolean isAlreadyExist(Collection consentResponseBeanCollection,
			long collectionProtocolId)
	{

		final Iterator it = consentResponseBeanCollection.iterator();
		while (it.hasNext())
		{
			final ConsentResponseBean consentResponseBean = (ConsentResponseBean) it.next();
			final long cpId = consentResponseBean.getCollectionProtocolID();
			if (cpId == collectionProtocolId)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @param participantMedicalIdentifier : participantMedicalIdentifier
	 * @return Map : Map
	 */
	private Map participantMedicalIdentifierMap(Map participantMedicalIdentifier)
	{
		final Validator validator = new Validator();
		final String className = "ParticipantMedicalIdentifier:";
		final String key1 = "_Site_" + Constants.SYSTEM_IDENTIFIER;
		final String key2 = "_medicalRecordNumber";
		final String key3 = "_" + Constants.SYSTEM_IDENTIFIER;
		int index = 1;

		while (true)
		{
			final String keyOne = className + index + key1;
			final String keyTwo = className + index + key2;
			final String keyThree = className + index + key3;

			final String value1 = (String) participantMedicalIdentifier.get(keyOne);
			final String value2 = (String) participantMedicalIdentifier.get(keyTwo);

			if (value1 == null || value2 == null)
			{
				break;
			}
			else if (!validator.isValidOption(value1) && value2.trim().equals(""))
			{
				participantMedicalIdentifier.remove(keyOne);
				participantMedicalIdentifier.remove(keyTwo);
				participantMedicalIdentifier.remove(keyThree);
			}
			index++;
		}
		return participantMedicalIdentifier;
	}

	/**
	 *
	 * @param mapCollectionProtocolRegistration : mapCollectionProtocolRegistration
	 * @param cprCount : cprCount
	 * @param mapCollectionProtocolRegistrationOld : mapCollectionProtocolRegistrationOld
	 * @param cprCountOld : cprCountOld
	 * @return Map : Map
	 * @throws Exception : Exception
	 */
	private Map appendCollectionProtocolRegistrations(Map mapCollectionProtocolRegistration,
			int cprCount, Map mapCollectionProtocolRegistrationOld, int cprCountOld)
			throws Exception
	{
		final int cprCountNew = cprCount + cprCountOld;
		for (int i = cprCountOld + 1; i <= cprCountNew; i++)
		{
			final String collectionProtocolId = "CollectionProtocolRegistration:"
					+ (i - cprCountOld) + "_CollectionProtocol_id";
			final String collectionProtocolTitle = "CollectionProtocolRegistration:"
					+ (i - cprCountOld) + "_CollectionProtocol_shortTitle";
			final String collectionProtocolParticipantId = "CollectionProtocolRegistration:"
					+ (i - cprCountOld) + "_protocolParticipantIdentifier";
			final String collectionProtocolRegistrationDate = "CollectionProtocolRegistration:"
					+ (i - cprCountOld) + "_registrationDate";
			final String collectionProtocolIdentifier = "CollectionProtocolRegistration:"
					+ (i - cprCountOld) + "_id";
			final String isConsentAvailable = "CollectionProtocolRegistration:" + (i - cprCountOld)
					+ "_isConsentAvailable";
			final String isActive = "CollectionProtocolRegistration:" + (i - cprCountOld)
					+ "_activityStatus";

			final String collectionProtocolIdNew = "CollectionProtocolRegistration:" + i
					+ "_CollectionProtocol_id";
			final String collectionProtocolTitleNew = "CollectionProtocolRegistration:" + i
					+ "_CollectionProtocol_shortTitle";
			final String collectionProtocolParticipantIdNew = "CollectionProtocolRegistration:" + i
					+ "_protocolParticipantIdentifier";
			final String collectionProtocolRegistrationDateNew = "CollectionProtocolRegistration:"
					+ i + "_registrationDate";
			final String collectionProtocolIdentifierNew = "CollectionProtocolRegistration:" + i
					+ "_id";
			final String isConsentAvailableNew = "CollectionProtocolRegistration:" + i
					+ "_isConsentAvailable";
			final String isActiveNew = "CollectionProtocolRegistration:" + i + "_activityStatus";

			mapCollectionProtocolRegistrationOld.put(collectionProtocolIdNew,
					mapCollectionProtocolRegistration.get(collectionProtocolId));
			mapCollectionProtocolRegistrationOld.put(collectionProtocolTitleNew,
					mapCollectionProtocolRegistration.get(collectionProtocolTitle));
			mapCollectionProtocolRegistrationOld.put(collectionProtocolParticipantIdNew,
					mapCollectionProtocolRegistration.get(collectionProtocolParticipantId));
			mapCollectionProtocolRegistrationOld.put(collectionProtocolRegistrationDateNew,
					mapCollectionProtocolRegistration.get(collectionProtocolRegistrationDate));
			mapCollectionProtocolRegistrationOld.put(collectionProtocolIdentifierNew,
					mapCollectionProtocolRegistration.get(collectionProtocolIdentifier));
			mapCollectionProtocolRegistrationOld.put(isConsentAvailableNew,
					mapCollectionProtocolRegistration.get(isConsentAvailable));
			String status = Status.ACTIVITY_STATUS_ACTIVE.toString();
			if (mapCollectionProtocolRegistration.get(isActive) != null)
			{
				status = (String) mapCollectionProtocolRegistration.get(isActive);
			}

			mapCollectionProtocolRegistrationOld.put(isActiveNew, status);
		}

		mapCollectionProtocolRegistrationOld = this
				.participantCollectionProtocolRegistration(mapCollectionProtocolRegistrationOld);

		return mapCollectionProtocolRegistrationOld;
	}

	/**
	 *
	 * @param collectionProtocolRegistrationValues : collectionProtocolRegistrationValues
	 * @return Map : Map
	 */
	private Map participantCollectionProtocolRegistration(Map collectionProtocolRegistrationValues)
	{
		final Validator validator = new Validator();
		final String collectionProtocolClassName = "CollectionProtocolRegistration:";
		final String collectionProtocolId = "_CollectionProtocol_id";
		final String collectionProtocolParticipantId = "_protocolParticipantIdentifier";
		final String collectionProtocolRegistrationDate = "_registrationDate";
		final String collectionProtocolIdentifier = "_id";
		final String isConsentAvailable = "_isConsentAvailable";
		final String isActive = "_activityStatus";
		final String collectionProtocolTitle = "_CollectionProtocol_shortTitle";

		int index = 1;

		while (true)
		{
			final String keyOne = collectionProtocolClassName + index + collectionProtocolId;
			final String keyTwo = collectionProtocolClassName + index
					+ collectionProtocolParticipantId;
			final String keyThree = collectionProtocolClassName + index
					+ collectionProtocolRegistrationDate;
			final String keyFour = collectionProtocolClassName + index
					+ collectionProtocolIdentifier;
			final String keyFive = collectionProtocolClassName + index + isConsentAvailable;
			final String keySix = collectionProtocolClassName + index + isActive;
			final String KeySeven = collectionProtocolClassName + index + collectionProtocolTitle;

			final String value1 = (String) collectionProtocolRegistrationValues.get(keyOne);
			final String value2 = (String) collectionProtocolRegistrationValues.get(keyTwo);
			final String value3 = (String) collectionProtocolRegistrationValues.get(keyThree);

			if (value1 == null || value2 == null || value3 == null)
			{
				break;
			}
			else if (!validator.isValidOption(value1))
			{
				collectionProtocolRegistrationValues.remove(keyOne);
				collectionProtocolRegistrationValues.remove(keyTwo);
				collectionProtocolRegistrationValues.remove(keyThree);
				collectionProtocolRegistrationValues.remove(keyFour);
				collectionProtocolRegistrationValues.remove(keyFive);
				collectionProtocolRegistrationValues.remove(keySix);
				collectionProtocolRegistrationValues.remove(KeySeven);
			}
			index++;
		}

		return collectionProtocolRegistrationValues;
	}

}
