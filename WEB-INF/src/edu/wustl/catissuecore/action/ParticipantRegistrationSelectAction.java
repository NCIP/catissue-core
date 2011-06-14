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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bean.ConsentResponseBean;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.CommonAddEditAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.participant.bizlogic.EMPIParticipantRegistrationBizLogic;
import edu.wustl.common.participant.utility.ParticipantManagerUtility;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;

// TODO: Auto-generated Javadoc
/**
 * The Class ParticipantRegistrationSelectAction.
 *
 * @author renuka_bajpai
 */
public class ParticipantRegistrationSelectAction extends CommonAddEditAction
{

	/** logger. */
	private static final Logger LOGGER = Logger
			.getCommonLogger(ParticipantRegistrationSelectAction.class);

	/** The object name. */
	private transient String objectName;

	/** The biz logic. */
	private transient IBizLogic bizLogic;

	/** The map coll proto reg. */
	private transient Map mapCollProtoReg = null;

	/** The cpr count. */
	private transient int cprCount = 0;

	/** The map pcpant med id. */
	private transient Map mapPcpantMedId = null;

	/** The const resp bean coll. */
	private transient Collection constRespBeanColl = null;

	/** The consent resp tbl. */
	private transient Map consentRespTbl = null;


	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		ActionForward forward = null;
		boolean isEMPIPartiUpdate = false;
		long participantId = 0l;
		long genEMPIForPartId = 0l;
		try
		{


			final ParticipantForm participantForm = (ParticipantForm) form;

			//to identify that grid value was selected
			initPartiBizLogicObj(participantForm);
			LOGGER.info("Participant Id-------------------"
					+ request.getParameter("participantId"));
			final String partiIdForeMPIGen = request.getParameter("generateeMPIIdforPartiId");
			final String selected = request.getParameter(Constants.PARTICIPANT_ID) == null
			? "EMPI"
			: request.getParameter(Constants.PARTICIPANT_ID);

			participantId = Long.parseLong(selected);
			if (partiIdForeMPIGen != null && !"".equals(partiIdForeMPIGen))
			{
				genEMPIForPartId = Long.parseLong(partiIdForeMPIGen);
			}
			if (participantId <= 0l)
			{
				isEMPIPartiUpdate = true;
				generateEMPI(request, participantForm, genEMPIForPartId);
			}
			else
			{
				updateLocalParticipant(request, participantForm);
			}


			forward = super.executeXSS(mapping, participantForm, request, response);

			forward = processResult(forward, mapping, request, participantForm, genEMPIForPartId,
					participantId);

		}
		catch (final Exception e)
		{
			LOGGER.error(e.getMessage());
		}

		return forward;
	}

	/**
	 * Process result.
	 *
	 * @param forward : ActionForward.
	 * @param request : HttpServletRequest
	 * @param participantForm : ParticipantForm
	 * @param genMPIForPartId the gen mpi for part id
	 * @param mapping the mapping
	 * @param participantId the participant id
	 *
	 * @return ParticipantForm
	 *
	 * @throws BizLogicException : BizLogicException
	 * @throws Exception the exception
	 * @throws ApplicationException the application exception
	 */
	private ActionForward processResult(ActionForward forward, final ActionMapping mapping,
			final HttpServletRequest request, final ParticipantForm participantForm,
			final long genMPIForPartId, final long participantId) throws BizLogicException,
			ApplicationException
	{
		try
		{
			if (forward.getName().equals(Constants.FAILURE))
			{
				participantForm.setCollectionProtocolRegistrationValues(mapCollProtoReg);
				participantForm.setCollectionProtocolRegistrationValueCounter(cprCount);
				participantForm.setValues(mapPcpantMedId);
				participantForm.setConsentResponseBeanCollection(constRespBeanColl);
				participantForm.setConsentResponseHashTable(consentRespTbl);
				request.setAttribute("continueLookup", "yes");
				// if its from generate eMPI page
				if (participantId <= 0l)
				{
					forward = mapping.findForward(Constants.PAGE_OF_PARTICIPANT);
				}
			}
			else
			{
				if (genMPIForPartId > 0)
				{
					// registration message to CDR
					sendHL7Message(participantForm);
					// Delete that participant from the processed message queue.
					ParticipantManagerUtility.deleteProcessedParticipant(participantForm.getId());
					setMessage(request);
					forward = mapping.findForward(edu.wustl.common.participant.utility.Constants.PROCESS_NEXT_PARTCIPANT);
				}
				request.removeAttribute("participantForm");
				request.setAttribute("participantForm1", participantForm);
				request.setAttribute("participantSelect", "yes");
			}
		}
		catch (Exception e)
		{
			throw new ApplicationException(null, e, e.getMessage());
		}
		return forward;
	}

	/**
	 * Sets the message.
	 *
	 * @param request the new message
	 */
	private void setMessage(final HttpServletRequest request)
	{
		ActionMessages actionMsgs = (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
		if (actionMsgs == null)
		{
			actionMsgs = new ActionMessages();
		}
		actionMsgs.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
				"participant.empiid.generation.success"));
		saveMessages(request, actionMsgs);

		request.getSession().setAttribute(edu.wustl.common.participant.utility.Constants.EMPI_ID_SUCCESS, "participant.empiid.generation.success");
	}

	/**
	 * Send h l7 message.
	 *
	 * @param participantForm the participant form
	 *
	 * @throws BizLogicException the biz logic exception
	 * @throws Exception the exception
	 * @throws ApplicationException the application exception
	 */
	private void sendHL7Message(final ParticipantForm participantForm) throws BizLogicException,
			ApplicationException
	{
		final Participant participant = new Participant();
		participant.setAllValues(participantForm);
		participant.setId(participantForm.getId());
		final String mrn = ParticipantManagerUtility.getMrnValue(participant
				.getParticipantMedicalIdentifierCollection());
		final EMPIParticipantRegistrationBizLogic eMPIPartiReg = new EMPIParticipantRegistrationBizLogic();
		if (ParticipantManagerUtility.isParticipantValidForEMPI(participant.getLastName(),
				participant.getFirstName(), participant.getBirthDate(), participant
						.getSocialSecurityNumber(), mrn))
		{
			eMPIPartiReg.registerPatientToeMPI(participant);
		}
	}


	private void updateLocalParticipant(HttpServletRequest request, ParticipantForm participantForm) throws Exception
	{
		final Object object = bizLogic.retrieve(objectName, new Long(request
				.getParameter("participantId")));
		AbstractDomainObject abstractDomain = (AbstractDomainObject) object;
		final Participant participant = (Participant) abstractDomain;

		LOGGER.info("Last name in ParticipantSelectAction:" + participant.getLastName());

		// To append the cpr to already existing cprs
		//Gets the collection Protocol Registration map from ActionForm
		mapCollProtoReg = participantForm
				.getCollectionProtocolRegistrationValues();
		cprCount = participantForm.getCollectionProtocolRegistrationValueCounter();
		constRespBeanColl  = participantForm
				.getConsentResponseBeanCollection();
		consentRespTbl = participantForm.getConsentResponseHashTable();
		mapPcpantMedId = this
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
				.appendCollectionProtocolRegistrations(mapCollProtoReg,
						cprCount, mapCollectionProtocolRegistrationOld, cprCountOld);
		final Map mapParticipantMedicalIdentifierOld = this
				.participantMedicalIdentifierMap(participantForm.getValues());

		if (constRespBeanColl != null)
		{
			this.updateConsentResponse(constRespBeanColl,
					consentResponseBeanCollectionOld, consentResponseHashTableOld);
		}

		participantForm
				.setCollectionProtocolRegistrationValues(mapCollectionProtocolRegistrationAppended);
		participantForm.setCollectionProtocolRegistrationValueCounter((cprCountOld + cprCount));
		participantForm.setValues(mapParticipantMedicalIdentifierOld);
		participantForm.setConsentResponseBeanCollection(consentResponseBeanCollectionOld);
		participantForm.setConsentResponseHashTable(consentResponseHashTableOld);


	}
	/**
	 * Generate empi.
	 *
	 * @param request the request
	 * @param participantForm the participant form
	 * @param geneMPIForPartId the gene mpi for part id
	 *
	 * @throws Exception the exception
	 * @throws ApplicationException the application exception
	 */
	private void generateEMPI(final HttpServletRequest request,
			final ParticipantForm participantForm, final Long geneMPIForPartId)
			throws ApplicationException
	{
		try
		{
			// Update the participant only for eMPI ID
			final String partiId = request.getParameter(Constants.CLICKED_ROW_SELECTED);
			final HttpSession session = request.getSession();
			final List partiList = (List) session.getAttribute("MatchedParticpant");
			final DefaultLookupResult partiObj = (DefaultLookupResult) partiList.get(Integer
					.parseInt(partiId) - 1);
			final Participant participantEMPI = (Participant) partiObj.getObject();

			// EMPI participant in Local DB
			final QueryWhereClause queryWhereClause = new QueryWhereClause(objectName);
			queryWhereClause.addCondition(new EqualClause(Constants.SYSTEM_IDENTIFIER, geneMPIForPartId));
			final List participants = bizLogic.retrieve(objectName, new String[]{}, queryWhereClause);
			final AbstractDomainObject abstractDomain = (AbstractDomainObject) participants.get(0);
			final Participant participant = (Participant) abstractDomain;

			mapCollProtoReg = participantForm.getCollectionProtocolRegistrationValues();
			cprCount = participantForm.getCollectionProtocolRegistrationValueCounter();
			constRespBeanColl = participantForm.getConsentResponseBeanCollection();
			consentRespTbl = participantForm.getConsentResponseHashTable();
			mapPcpantMedId = participantMedicalIdentifierMap(participantForm.getValues());
			participant.setCollectionProtocolRegistrationCollection(new HashSet());

			updatePartiAfterEMPIIDGenetation(participant, participantEMPI);
			participantForm.setAllValues(participantEMPI);

			if (participantEMPI.getParticipantMedicalIdentifierCollection().isEmpty())
			{
				participantForm.setValues(new LinkedHashMap());
			}

			participantForm.setCollectionProtocolRegistrationValueCounter(cprCount);
			participantForm.setCollectionProtocolRegistrationValues(mapCollProtoReg);

			participantForm.setConsentResponseBeanCollection(constRespBeanColl);
			participantForm.setConsentResponseHashTable(consentRespTbl);

			participantForm.setOperation(Constants.EDIT);
			request.getSession().setAttribute(edu.wustl.common.participant.utility.Constants.EMPI_GENERATED_PARTICIPANT, participantForm);
			//request.getSession().setAttribute(edu.wustl.common.participant.utility.Constants.EMPI_GENERATED_PARTICIPANT_LN, participantForm.getLastName());
			//request.getSession().setAttribute(edu.wustl.common.participant.utility.Constants.EMPI_GENERATED_PARTICIPANT_FN, participantForm.getFirstName());
		}
		catch (Exception e)
		{
			setErrorMessage(request, "participant.empiid.generation.waiting.message", "");
			throw new ApplicationException(null, e, e.getMessage());
		}
	}

	/**
	 * Sets the error message.
	 *
	 * @param request the new error message
	 * @param key the key
	 * @param errMes the err mes
	 */
	private void setErrorMessage(final HttpServletRequest request, final String key,
			final String errMes)
	{
		ActionErrors actionErr = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (actionErr == null)
		{
			actionErr = new ActionErrors();
		}
		actionErr.add(ActionErrors.GLOBAL_ERROR, new ActionError(key, errMes));
		saveMessages(request, actionErr);
	}


	/**
	 * Update parti after empiid genetation.
	 *
	 * @param participant the participant
	 * @param participantEMPI the participant empi
	 */
	private void updatePartiAfterEMPIIDGenetation(final Participant participant,
			final Participant participantEMPI)
	{

		participantEMPI.setId(participant.getId());
		participantEMPI
				.setEmpiIdStatus(edu.wustl.common.participant.utility.Constants.EMPI_ID_CREATED);
		participantEMPI.setCollectionProtocolRegistrationCollection(participant
				.getCollectionProtocolRegistrationCollection());
		participantEMPI.setCollectionProtocolRegistrationCollection(participant
				.getCollectionProtocolRegistrationCollection());
		updateOldMedId(participant, participantEMPI);
	}

	/**
	 * Updating the old participant with new mrn value.
	 *
	 * @param participant : participant .
	 * @param participantEMPI : participantEMPI
	 */
	private void updateOldMedId(final Participant participant, final Participant participantEMPI)
	{
		final Collection<ParticipantMedicalIdentifier> medIdColTemp = new LinkedHashSet<ParticipantMedicalIdentifier>();
		final Collection medIdColLocal = participant.getParticipantMedicalIdentifierCollection();
		if (medIdColLocal != null && !medIdColLocal.isEmpty())
		{
			final Iterator<ParticipantMedicalIdentifier> iterator = medIdColLocal.iterator();
			while (iterator.hasNext())
			{
				Long localSiteID = null;

				final ParticipantMedicalIdentifier partMedIdLocal = iterator.next();
				final String localMRN = partMedIdLocal.getMedicalRecordNumber();
				if (partMedIdLocal.getSite() != null)
				{
					localSiteID = partMedIdLocal.getSite().getId();
				}
				if ((localMRN != null && !"".equals(localMRN))
						&& (localSiteID != null && localSiteID != -1))
				{
					final Collection<ParticipantMedicalIdentifier> medIdColEMPI = participantEMPI
							.getParticipantMedicalIdentifierCollection();
					if (medIdColEMPI == null)
					{
						medIdColTemp.add(partMedIdLocal);
					}
					else if (!medIdColEMPI.isEmpty())
					{
						removeDuplicatesMRN(medIdColEMPI, partMedIdLocal, localMRN, localSiteID,
								medIdColTemp);
					}
				}
			}

			if (!medIdColTemp.isEmpty())
			{
				participantEMPI.getParticipantMedicalIdentifierCollection().addAll(medIdColTemp);
			}
		}

	}

	/**
	 * Removes the duplicates mrn.
	 *
	 * @param medIdColEMPI the med id col empi
	 * @param partMedIdLocal the part med id local
	 * @param localMRN the local mrn
	 * @param localSiteID the local site id
	 * @param medIdColTemp the med id col temp
	 */
	private void removeDuplicatesMRN(final Collection<ParticipantMedicalIdentifier> medIdColEMPI,
			final ParticipantMedicalIdentifier partMedIdLocal, final String localMRN,
			final Long localSiteID, final Collection<ParticipantMedicalIdentifier> medIdColTemp)
	{
		boolean MRNNotFound = false;
		final Iterator<ParticipantMedicalIdentifier> itrEMPI = medIdColEMPI.iterator();
		while (itrEMPI.hasNext())
		{
			MRNNotFound = false;
			Long empiSite = null;
			final ParticipantMedicalIdentifier partiMedIdEMPI = itrEMPI.next();
			final String empiMRN = partiMedIdEMPI.getMedicalRecordNumber();
			if (partiMedIdEMPI.getSite() != null)
			{
				empiSite = partiMedIdEMPI.getSite().getId();
			}
			if ((empiMRN.equals(localMRN)) && (empiSite.equals(localSiteID)))
			{
				partiMedIdEMPI.setId(partMedIdLocal.getId());
				MRNNotFound = true;
				break;
			}
		}
		if (!MRNNotFound)
		{
			medIdColTemp.add(partMedIdLocal);
		}
	}



	/**
	 * Inits the parti biz logic obj.
	 *
	 * @param participantForm the participant form
	 *
	 * @throws BizLogicException the biz logic exception
	 */
	private void initPartiBizLogicObj(final ParticipantForm participantForm)
			throws BizLogicException
	{
		final IDomainObjectFactory domObjFactry = AbstractFactoryConfig.getInstance()
				.getDomainObjectFactory();
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		bizLogic = factory.getBizLogic(participantForm.getFormId());
		objectName = domObjFactry.getDomainObjectName(participantForm.getFormId());
	}
	/**
	 * Update consent response.
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
	 * Checks if is already exist.
	 *
	 * @param consentResponseBeanCollection : consentResponseBeanCollection
	 * @param collectionProtocolId : collectionProtocolId
	 *
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
	 * Participant medical identifier map.
	 *
	 * @param participantMedicalIdentifier : participantMedicalIdentifier
	 *
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
	 * Append collection protocol registrations.
	 *
	 * @param mapCollectionProtocolRegistration : mapCollectionProtocolRegistration
	 * @param cprCount : cprCount
	 * @param mapCollectionProtocolRegistrationOld : mapCollectionProtocolRegistrationOld
	 * @param cprCountOld : cprCountOld
	 *
	 * @return Map : Map
	 *
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
	 * Participant collection protocol registration.
	 *
	 * @param collectionProtocolRegistrationValues : collectionProtocolRegistrationValues
	 *
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
