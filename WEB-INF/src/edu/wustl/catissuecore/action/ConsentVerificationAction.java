/**
 * <p>
 * Title: ConsentVerificationAction Class>
 * <p>
 * Description: This class used to verify the consents at order view page.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author kalpana thakur
 * @version 1.00 Created on oct 5, 2007
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class ConsentVerificationAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(ConsentVerificationAction.class);
	// This counter will keep track of the no of consentTiers
	/**
	 * consentTierCounter.
	 */
	int consentTierCounter;
	/**
	 * listOfMap.
	 */

	List listOfMap = null;
	/**
	 * listOfStringArray.
	 */
	List listOfStringArray = null;
	/**
	 * labelIndexCount.
	 */
	String labelIndexCount = "";

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final DistributionForm dForm = (DistributionForm) form;

		// Show Consents for Specimen
		final String specimenConsents
		= request.getParameter(Constants.SPECIMEN_CONSENTS); // "specimenConsents"

		final String specimenIdentifier = request.getParameter(Constants.SPECIMEN_ID);
		Long specimenId = null;

		if (specimenIdentifier != null)
		{
			specimenId = Long.parseLong(specimenIdentifier);
			final Specimen specimen = this.getListOfSpecimen(specimenId);
			this.showConsents(dForm, specimen, request, specimen.getLabel());

			request.setAttribute("barcodeStatus", Constants.VALID);// valid
			return mapping.findForward(Constants.POPUP);

		}
		else
		{
			if (specimenConsents != null && specimenConsents.equalsIgnoreCase(Constants.YES))
			{
				final String speciemnIdValue
				= request.getParameter("speciemnIdValue");// barcodelabel
				this.labelIndexCount = request.getParameter("labelIndexCount");
				final StringTokenizer stringToken = new StringTokenizer(speciemnIdValue, "|");
				// StringTokenizer stringTokenForIndex = new
				// StringTokenizer(labelIndexCount, "|");
				this.listOfMap = new ArrayList();
				this.listOfStringArray = new ArrayList();
				while (stringToken.hasMoreTokens())
				{
					specimenId = Long.parseLong(stringToken.nextToken());

					final Specimen specimen = this.getListOfSpecimen(specimenId);
					this.showConsents(dForm, specimen, request, specimen.getLabel());
				}
				request.setAttribute("listOfStringArray", this.listOfStringArray);
				request.setAttribute("listOfMap", this.listOfMap);
				request.setAttribute("labelIndexCount", this.labelIndexCount);

				return mapping.findForward(Constants.VIEWAll);// ViewAll
			}

		}
		// Consent Tracking
		this.logger.debug("executeSecureAction");
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		request.setAttribute(Constants.PAGE_OF, pageOf);

		return mapping.findForward(request.getParameter(Constants.PAGE_OF));
	}

	/**
	 * This function will fetch witness name,url,consent date for a
	 * barcode/lable.
	 * @param dForm : dForm
	 * @param specimen : specimen
	 * @param request : request
	 * @param barcodeLable : barcodeLable
	 * @throws ApplicationException : ApplicationException
	 */
	private void showConsents(DistributionForm dForm, Specimen specimen,
			HttpServletRequest request, String barcodeLable) throws ApplicationException
	{

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String initialURLValue = "";
		String initialSignedConsentDateValue = "";
		String initialWitnessValue = "";
		
		CollectionProtocolRegistration collectionProtocolRegistration = ConsentUtil.getCollectionProtRegistration(specimen);

		if (collectionProtocolRegistration.getConsentSignatureDate() == null)
		{
			initialSignedConsentDateValue = Constants.NULL;
		}
		
		if (collectionProtocolRegistration.getSignedConsentDocumentURL() == null)
		{
			initialURLValue = Constants.NULL;
		}
		User consentWitness = null;
		if (collectionProtocolRegistration.getId() != null)
		{
			consentWitness = (User) bizLogic.retrieveAttribute(CollectionProtocolRegistration.class
					.getName(), collectionProtocolRegistration.getId(), "consentWitness");
		}
		// Resolved Lazy ---- User consentWitness=
		// collectionProtocolRegistration.getConsentWitness();
		if (consentWitness == null)
		{
			initialWitnessValue = Constants.NULL;
		}
		
		final List cprObjectList = new ArrayList();
		cprObjectList.add(collectionProtocolRegistration);
		final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
				.getAttribute(Constants.SESSION_DATA);
		final CaCoreAppServicesDelegator caCoreAppServicesDelegator = new CaCoreAppServicesDelegator();
		final String userName = sessionDataBean.getUserName().toString();
		try
		{
			caCoreAppServicesDelegator.delegateSearchFilter(userName, cprObjectList);
		}
		catch (final Exception e)
		{
			this.logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		final CollectionProtocolRegistration cprObject = collectionProtocolRegistration;
		// Getting WitnessName,Consent Date,Signed Url using
		// collectionProtocolRegistration object
		String witnessName = ConsentUtil.getWitnessName( bizLogic, initialWitnessValue,
				cprObject);
		String consentDate  = ConsentUtil.getConsentDate(initialSignedConsentDateValue,
				cprObject);
    	String signedConsentURL = ConsentUtil.getSignedConsentURL(initialURLValue, cprObject);
	
    	// Setting WitnessName,ConsentDate and Signed Consent Url
    	dForm.setWitnessName(witnessName);
    	dForm.setConsentDate(consentDate);
		dForm.setSignedConsentUrl(signedConsentURL);

		// Getting ConsentResponse collection for CPR level
		// Resolved lazy ---
		// collectionProtocolRegistration.getConsentTierResponseCollection();
		final Collection participantResponseCollection = (Collection) bizLogic.retrieveAttribute(
				CollectionProtocolRegistration.class.getName(), collectionProtocolRegistration
						.getId(), "elements(consentTierResponseCollection)");
		// Getting ConsentResponse collection for Specimen level
		// Resolved lazy --- specimen.getConsentTierStatusCollection();
		final Collection specimenLevelResponseCollection = (Collection) bizLogic
				.retrieveAttribute(Specimen.class.getName(), specimen.getId(),
						"elements(consentTierStatusCollection)");
		// Prepare Map and iterate both Collections
		final Map tempMap = this.prepareConsentMap(participantResponseCollection,
				specimenLevelResponseCollection);
		// Setting map and counter in the form
		dForm.setConsentResponseForDistributionValues(tempMap);
		dForm.setConsentTierCounter(this.consentTierCounter);
		final String specimenConsents = request.getParameter(Constants.SPECIMEN_CONSENTS);
		if (specimenConsents != null && specimenConsents.equalsIgnoreCase(Constants.YES))
		{
			// For no consents and Consent waived
			if (this.consentTierCounter > 0
					&& !(specimen.getActivityStatus()
							.equalsIgnoreCase(Constants.DISABLED)))// disabled
			{
				final String[] barcodeLabelAttribute = new String[5];
				barcodeLabelAttribute[0] =
					witnessName;
				barcodeLabelAttribute[1] =
					consentDate;
				barcodeLabelAttribute[2] =
					signedConsentURL;
				barcodeLabelAttribute[3] =
					Integer.toString(this.consentTierCounter);
				barcodeLabelAttribute[4] =
					barcodeLable;
				this.listOfMap.add(tempMap);
				this.listOfStringArray.add(barcodeLabelAttribute);
			}

		}

	}

	/**
	 * @param dForm
	 *            object of DistributionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @throws DAOException
	 *             DAO exception
	 */

	// Consent Tracking
	/**
	 * Prepare Map for Consent tiers.
	 *
	 * @param participantResponseList
	 *            This list will be iterated and added to map to populate
	 *            participant Response status.
	 * @param specimenLevelResponseList
	 *            This List will be iterated and added to map to populate
	 *            Specimen Level response.
	 * @return tempMap
	 */
	private Map prepareConsentMap(Collection participantResponseList,
			Collection specimenLevelResponseList)
	{
		final Map tempMap = new HashMap();
		Long consentTierID;
		Long consentID;
		if (participantResponseList != null)
		{
			int i = 0;
			final Iterator consentResponseCollectionIter = participantResponseList.iterator();
			while (consentResponseCollectionIter.hasNext())
			{
				final ConsentTierResponse consentTierResponse
				= (ConsentTierResponse) consentResponseCollectionIter
						.next();
				consentTierID = consentTierResponse.getConsentTier().getId();
				final Iterator specimenCollectionIter = specimenLevelResponseList.iterator();
				while (specimenCollectionIter.hasNext())
				{
					final ConsentTierStatus specimenConsentResponse
					= (ConsentTierStatus) specimenCollectionIter
							.next();
					consentID = specimenConsentResponse.getConsentTier().getId();
					if (consentTierID.longValue() == consentID.longValue())
					{
						final ConsentTier consent = consentTierResponse.getConsentTier();
						final String idKey = "ConsentBean:" + i + "_consentTierID";
						final String statementKey = "ConsentBean:" + i + "_statement";
						final String responseKey
						= "ConsentBean:" + i + "_participantResponse";
						final String participantResponceIdKey = "ConsentBean:" + i
								+ "_participantResponseID";
						final String specimenResponsekey = "ConsentBean:" + i
								+ "_specimenLevelResponse";
						final String specimenResponseIDkey = "ConsentBean:" + i
								+ "_specimenLevelResponseID";
						// Adding Keys and its data into the Map
						tempMap.put(idKey, consent.getId());
						tempMap.put(statementKey, consent.getStatement());
						tempMap.put(responseKey, consentTierResponse.getResponse());
						tempMap.put(participantResponceIdKey, consentTierResponse.getId());
						tempMap.
						put(specimenResponsekey, specimenConsentResponse.getStatus());
						tempMap.
						put(specimenResponseIDkey, specimenConsentResponse.getId());
						i++;
						break;
					}
				}
			}
			this.consentTierCounter = i;
			return tempMap;
		}
		else
		{
			return null;
		}

	}

	/**
	 * This method sets all the common parameters for the SpecimenEventParameter
	 * pages.
	 * @param specimenId : specimenId
	 * @return Specimen : Specimen
	 * @throws BizLogicException : BizLogicException
	 */

	private Specimen getListOfSpecimen(Long specimenId) throws BizLogicException
	{

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) factory
				.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		final Object object = newSpecimenBizLogic.retrieve(Specimen.class.getName(), specimenId);
		final Specimen specimen = (Specimen) object;
		return specimen;
	}
}