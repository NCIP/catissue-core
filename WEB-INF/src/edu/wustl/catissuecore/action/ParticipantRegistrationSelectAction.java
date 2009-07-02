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
	private transient Logger logger = Logger
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
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		ActionForward forward = null;
		try
		{
			AbstractDomainObject abstractDomain = null;

			ParticipantForm participantForm = (ParticipantForm) form;
			IDomainObjectFactory iDomainObjectFactory = AbstractFactoryConfig.getInstance()
					.getDomainObjectFactory();
			IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			IBizLogic bizLogic = factory.getBizLogic(participantForm.getFormId());

			String objectName = iDomainObjectFactory.getDomainObjectName(participantForm
					.getFormId());

			logger
					.info("Participant Id-------------------"
							+ request.getParameter("participantId"));

			Object object = bizLogic.retrieve(objectName, new Long(request
					.getParameter("participantId")));
			abstractDomain = (AbstractDomainObject) object;
			Participant participant = (Participant) abstractDomain;

			logger.info("Last name in ParticipantSelectAction:" + participant.getLastName());

			// To append the cpr to already existing cprs
			//Gets the collection Protocol Registration map from ActionForm
			Map mapCollectionProtocolRegistration = participantForm
					.getCollectionProtocolRegistrationValues();
			int cprCount = participantForm.getCollectionProtocolRegistrationValueCounter();
			Collection consentResponseBeanCollection = participantForm
					.getConsentResponseBeanCollection();
			Map consentResponseHashTable = participantForm.getConsentResponseHashTable();
			Map mapParticipantMedicalIdentifier = participantMedicalIdentifierMap(participantForm
					.getValues());

			//Gets the collection Protocol Registration map from Database
			DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
			defaultBizLogic.populateUIBean(Participant.class.getName(), participant.getId(),
					participantForm);

			Map mapCollectionProtocolRegistrationOld = participantForm
					.getCollectionProtocolRegistrationValues();
			int cprCountOld = participantForm.getCollectionProtocolRegistrationValueCounter();
			Collection consentResponseBeanCollectionOld = participantForm
					.getConsentResponseBeanCollection();
			Map consentResponseHashTableOld = participantForm.getConsentResponseHashTable();

			Map mapCollectionProtocolRegistrationAppended = appendCollectionProtocolRegistrations(
					mapCollectionProtocolRegistration, cprCount,
					mapCollectionProtocolRegistrationOld, cprCountOld);
			Map mapParticipantMedicalIdentifierOld = participantMedicalIdentifierMap(participantForm
					.getValues());

			if (consentResponseBeanCollection != null)
			{
				updateConsentResponse(consentResponseBeanCollection,
						consentResponseBeanCollectionOld, consentResponseHashTableOld);
			}

			participantForm
					.setCollectionProtocolRegistrationValues
					(mapCollectionProtocolRegistrationAppended);
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
						.setCollectionProtocolRegistrationValues
						(mapCollectionProtocolRegistration);
				participantForm.setCollectionProtocolRegistrationValueCounter(cprCount);
				participantForm.setValues(mapParticipantMedicalIdentifier);
				participantForm.setConsentResponseBeanCollection(consentResponseBeanCollection);
				participantForm.setConsentResponseHashTable(consentResponseHashTable);
				request.setAttribute("continueLookup", "yes");
			}
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
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
		Iterator it = consentResponseBeanCollection.iterator();
		while (it.hasNext())
		{
			ConsentResponseBean consentResponseBean = (ConsentResponseBean) it.next();
			long collectionProtocolId = consentResponseBean.getCollectionProtocolID();
			if (collectionProtocolId > 0)
			{
				if (!isAlreadyExist(consentResponseBeanCollectionOld, collectionProtocolId))
				{
					consentResponseBeanCollectionOld.add(consentResponseBean);
					String key = Constants.CONSENT_RESPONSE_KEY + collectionProtocolId;
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

		Iterator it = consentResponseBeanCollection.iterator();
		while (it.hasNext())
		{
			ConsentResponseBean consentResponseBean = (ConsentResponseBean) it.next();
			long cpId = consentResponseBean.getCollectionProtocolID();
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
		Validator validator = new Validator();
		String className = "ParticipantMedicalIdentifier:";
		String key1 = "_Site_" + Constants.SYSTEM_IDENTIFIER;
		String key2 = "_medicalRecordNumber";
		String key3 = "_" + Constants.SYSTEM_IDENTIFIER;
		int index = 1;

		while (true)
		{
			String keyOne = className + index + key1;
			String keyTwo = className + index + key2;
			String keyThree = className + index + key3;

			String value1 = (String) participantMedicalIdentifier.get(keyOne);
			String value2 = (String) participantMedicalIdentifier.get(keyTwo);

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
		int cprCountNew = cprCount + cprCountOld;
		for (int i = cprCountOld + 1; i <= cprCountNew; i++)
		{
			String collectionProtocolId = "CollectionProtocolRegistration:" + (i - cprCountOld)
					+ "_CollectionProtocol_id";
			String collectionProtocolTitle = "CollectionProtocolRegistration:" + (i - cprCountOld)
					+ "_CollectionProtocol_shortTitle";
			String collectionProtocolParticipantId = "CollectionProtocolRegistration:"
					+ (i - cprCountOld) + "_protocolParticipantIdentifier";
			String collectionProtocolRegistrationDate = "CollectionProtocolRegistration:"
					+ (i - cprCountOld) + "_registrationDate";
			String collectionProtocolIdentifier = "CollectionProtocolRegistration:"
					+ (i - cprCountOld) + "_id";
			String isConsentAvailable = "CollectionProtocolRegistration:" + (i - cprCountOld)
					+ "_isConsentAvailable";
			String isActive = "CollectionProtocolRegistration:" + (i - cprCountOld)
					+ "_activityStatus";

			String collectionProtocolIdNew = "CollectionProtocolRegistration:" + i
					+ "_CollectionProtocol_id";
			String collectionProtocolTitleNew = "CollectionProtocolRegistration:" + i
					+ "_CollectionProtocol_shortTitle";
			String collectionProtocolParticipantIdNew = "CollectionProtocolRegistration:" + i
					+ "_protocolParticipantIdentifier";
			String collectionProtocolRegistrationDateNew = "CollectionProtocolRegistration:" + i
					+ "_registrationDate";
			String collectionProtocolIdentifierNew = "CollectionProtocolRegistration:" + i + "_id";
			String isConsentAvailableNew = "CollectionProtocolRegistration:" + i
					+ "_isConsentAvailable";
			String isActiveNew = "CollectionProtocolRegistration:" + i + "_activityStatus";

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

		mapCollectionProtocolRegistrationOld =
			participantCollectionProtocolRegistration(mapCollectionProtocolRegistrationOld);

		return mapCollectionProtocolRegistrationOld;
	}

	/**
	 *
	 * @param collectionProtocolRegistrationValues : collectionProtocolRegistrationValues
	 * @return Map : Map
	 */
	private Map participantCollectionProtocolRegistration(Map collectionProtocolRegistrationValues)
	{
		Validator validator = new Validator();
		String collectionProtocolClassName = "CollectionProtocolRegistration:";
		String collectionProtocolId = "_CollectionProtocol_id";
		String collectionProtocolParticipantId = "_protocolParticipantIdentifier";
		String collectionProtocolRegistrationDate = "_registrationDate";
		String collectionProtocolIdentifier = "_id";
		String isConsentAvailable = "_isConsentAvailable";
		String isActive = "_activityStatus";
		String collectionProtocolTitle = "_CollectionProtocol_shortTitle";

		int index = 1;

		while (true)
		{
			String keyOne = collectionProtocolClassName + index + collectionProtocolId;
			String keyTwo = collectionProtocolClassName + index + collectionProtocolParticipantId;
			String keyThree = collectionProtocolClassName + index
					+ collectionProtocolRegistrationDate;
			String keyFour = collectionProtocolClassName + index + collectionProtocolIdentifier;
			String keyFive = collectionProtocolClassName + index + isConsentAvailable;
			String keySix = collectionProtocolClassName + index + isActive;
			String KeySeven = collectionProtocolClassName + index + collectionProtocolTitle;

			String value1 = (String) collectionProtocolRegistrationValues.get(keyOne);
			String value2 = (String) collectionProtocolRegistrationValues.get(keyTwo);
			String value3 = (String) collectionProtocolRegistrationValues.get(keyThree);

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
