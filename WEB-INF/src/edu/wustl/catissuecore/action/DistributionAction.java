/**
 * <p>
 * Title: Distribution Class>
 * <p>
 * Description: This class initializes the fields in the Distribution Add/Edit
 * webpage.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Aug 10, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Calendar;
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
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.ConsentUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class DistributionAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(DistributionAction.class);
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
	 * @param request
	 *            object of HttpServletRequest
	 * @throws Exception
	 *             generic exception
	 */
	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final DistributionBizLogic dao = (DistributionBizLogic) factory
				.getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		// Sets the Site list.
		String sourceObjectName = Site.class.getName();
		final String[] displayNameFields = {"name"};
		final String valueField = Constants.SYSTEM_IDENTIFIER;

		final List siteList = dao.getList(sourceObjectName, displayNameFields, valueField, true);

		request.setAttribute(Constants.FROM_SITE_LIST, siteList);
		request.setAttribute(Constants.TO_SITE_LIST, siteList);

		// Sets the Distribution Protocol Id List.
		sourceObjectName = DistributionProtocol.class.getName();

		final String[] displayName = {"title"};

		final List protocolList = dao.getList(sourceObjectName, displayName, valueField, true);
		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);

		// SET THE SPECIMEN Ids LIST
		final String[] displayNameField = {Constants.SYSTEM_IDENTIFIER};
		final List specimenList = dao.getList(Specimen.class.getName(), displayNameField,
				valueField, true);
		request.setAttribute(Constants.SPECIMEN_ID_LIST, specimenList);

		// Sets the activityStatusList attribute to be used in the Site Add/Edit
		// Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		final List distributionType = new ArrayList();
		distributionType.add(new NameValueBean("Specimen", new Integer(
				Constants.SPECIMEN_DISTRIBUTION_TYPE)));
		distributionType.add(new NameValueBean("Specimen Array", new Integer(
				Constants.SPECIMEN_ARRAY_DISTRIBUTION_TYPE)));

		final List distributionBasedOn = new ArrayList();
		distributionBasedOn.add(new NameValueBean("Barcode", new Integer(
				Constants.BARCODE_BASED_DISTRIBUTION)));
		distributionBasedOn.add(new NameValueBean("Label", new Integer(
				Constants.LABEL_BASED_DISTRIBUTION)));

		request.setAttribute(Constants.DISTRIBUTION_TYPE_LIST, distributionType);
		request.setAttribute(Constants.DISTRIBUTION_BASED_ON, distributionBasedOn);
	}

	/**
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
	 * @return value for ActionForward object
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final DistributionForm dForm = (DistributionForm) form;
		this.setCommonRequestParameters(request);

		// EventParametersForm eventParametersForm = (EventParametersForm)form;

		// if operation is add
		if (dForm.isAddOperation())
		{
			if (dForm.getUserId() == 0)
			{
				final SessionDataBean sessionData = this.getSessionData(request);
				if (sessionData != null && sessionData.getUserId() != null)
				{
					final long userId = sessionData.getUserId().longValue();
					dForm.setUserId(userId);
				}
			}
			// set the current Date and Time for the event.
			final Calendar cal = Calendar.getInstance();
			if (dForm.getDateOfEvent() == null)
			{
				dForm.setDateOfEvent(CommonUtilities.parseDateToString(cal.getTime(),
						CommonServiceLocator.getInstance().getDatePattern()));
			}
			if (dForm.getTimeInHours() == null)
			{
				dForm.setTimeInHours(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
			}
			if (dForm.getTimeInMinutes() == null)
			{
				dForm.setTimeInMinutes(Integer.toString(cal.get(Calendar.MINUTE)));
			}

		}

		// super.executeSecureAction(mapping, form, request, response);

		// Populate Distributed Items data in the Distribution page if specimen
		// ID is changed.
		if (dForm.isIdChange())
		{
			final String test = null;
			test.getBytes();
			this.setSpecimenCharateristics(dForm, request);
		}

		final String distributionBasedOn = request.getParameter("distributionBasedOnOption");
		if (distributionBasedOn != null && !distributionBasedOn.equals("")
				&& distributionBasedOn.equals("unknown"))
		{
			dForm.setDistributionBasedOn(new Integer(0));
		}

		/* If forwarded from speciman page */
		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
		if (forwardToHashMap == null)
		{
			final String specimenLabel = request.getParameter(Constants.SYSTEM_LABEL);
			if (specimenLabel != null)
			{
				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
				final List list = bizLogic.retrieve(Specimen.class.getName(),
						Constants.SYSTEM_LABEL, specimenLabel);
				if (list != null && !list.isEmpty())
				{
					final Specimen specimen = (Specimen) list.get(0);
					forwardToHashMap = new HashMap();
					forwardToHashMap.put("specimenObjectKey", specimen);
				}
			}
		}

		if (forwardToHashMap != null)
		{
			final Object specimenObjectOrList = forwardToHashMap.get("specimenObjectKey");
			if (specimenObjectOrList != null)
			{
				if (specimenObjectOrList instanceof Specimen)
				{
					/*
					 * this code is for setting specimenId for showing specimen
					 * selected in tree
					 */
					final Specimen sp = (Specimen) specimenObjectOrList;
					final String spId = sp.getId().toString();
					request.setAttribute(Constants.SPECIMEN_ID, spId);

					this.addDistributionSample((DistributionForm) form, 1,
							(Specimen) specimenObjectOrList);
				}
				else
				{
					final List specimenIdList = (List) specimenObjectOrList;
					final IFactory factory = AbstractFactoryConfig.getInstance()
							.getBizLogicFactory();
					final DistributionBizLogic dao = (DistributionBizLogic) factory
							.getBizLogic(Constants.DISTRIBUTION_FORM_ID);

					for (int i = 0; i < specimenIdList.size(); i++)
					{
						final Long specimenId = Long.getLong((String) specimenIdList.get(i));
						final List list = dao.retrieve(Specimen.class.getName(),
								Constants.SYSTEM_IDENTIFIER, specimenId);
						final Specimen specimen = (Specimen) list.get(0);
						this.addDistributionSample((DistributionForm) form, i + 1, specimen);
					}
				}
			}
			else
			// If forwarded from Aliquout Summary page;
			{
				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
				final String noOfAliquots = (String) forwardToHashMap.get("noOfAliquots");
				String labelKey;
				List specimenList;
				for (int i = 0; i < Integer.valueOf(noOfAliquots).intValue(); i++)
				{
					labelKey = "Specimen:" + (i + 1) + "_label";
					final String label = (String) forwardToHashMap.get(labelKey);
					specimenList = bizLogic.retrieve(Specimen.class.getName(),
							Constants.SYSTEM_LABEL, label);
					if (!specimenList.isEmpty())
					{
						final Specimen specimen = (Specimen) specimenList.get(0);
						this.addDistributionSample((DistributionForm) form, i + 1, specimen);
					}

				}

			}
		}
		// Consent Tracking (Virender Mehta)
		// Show Consents for Specimen
		final String specimenConsents = request.getParameter(Constants.SPECIMEN_CONSENTS); // "specimenConsents"
		if (specimenConsents != null && specimenConsents.equalsIgnoreCase(Constants.YES))
		{
			final String labelBarcodeDistributionBasedOnValue = request
					.getParameter(Constants.DISTRIBUTION_ON);// "labelBarcode"
			final String barcodeLableValue = request.getParameter(Constants.BARCODE_LABLE);// barcodelabel
			final int distributionOn = Integer.parseInt(labelBarcodeDistributionBasedOnValue);
			final StringTokenizer stringToken = new StringTokenizer(barcodeLableValue, "|");
			this.listOfMap = new ArrayList();
			this.listOfStringArray = new ArrayList();
			while (stringToken.hasMoreTokens())
			{
				final String barcodeLable = stringToken.nextToken();
				// function that will check if the barcode/lable is present in
				// the Database or not
				final Specimen specimen = this.getConsentListForSpecimen(barcodeLable,
						distributionOn);
				// Set all the consents and attributes related to specimen in
				// List.
				this.showConsents(dForm, specimen, request, barcodeLable);
			}
			request.setAttribute("listOfStringArray", this.listOfStringArray);
			request.setAttribute("listOfMap", this.listOfMap);
			return mapping.findForward(Constants.VIEWAll);// ViewAll
		}
		// Show All Consents for Specimen(Consent tarcking)

		// Show Consents for Specimen(Consent Tracking)
		final String showConsents = request.getParameter(Constants.SHOW_CONSENTS);
		if (showConsents != null && showConsents.equalsIgnoreCase(Constants.YES))
		{
			final String barcodeLable = request.getParameter(Constants.BARCODE_LABLE);
			int barcodeLabelBasedDistribution = 1;
			final String labelBarcodeDistributionValue = request
					.getParameter(Constants.DISTRIBUTION_ON);// labelBarcode
			if (labelBarcodeDistributionValue.equalsIgnoreCase(Constants.BARCODE_DISTRIBUTION))// "1"
			{
				barcodeLabelBasedDistribution = 1;
			}
			else
			{
				barcodeLabelBasedDistribution = 2;
			}
			// Getting SpecimenCollectionGroup object
			final Specimen specimen = this.getConsentListForSpecimen(barcodeLable,
					barcodeLabelBasedDistribution);
			// SpecimenCollectionGroup specimenCollectionGroup =
			// getConsentListForSpecimen(barcode);
			this.showConsents(dForm, specimen, request, barcodeLable);

			request.setAttribute("barcodeStatus", Constants.VALID);// valid
			return mapping.findForward(Constants.POPUP);
		}
		// Consent Tracking (Virender Mehta)
		this.logger.debug("executeSecureAction");
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		request.setAttribute(Constants.PAGE_OF, pageOf);

		return mapping.findForward(request.getParameter(Constants.PAGE_OF));
	}

	/**
	 * @param dForm
	 *            : dForm
	 * @param specimen
	 *            : specimen
	 * @param request
	 *            : request
	 * @param barcodeLable
	 *            : barcodeLable
	 * @throws ApplicationException
	 *             : ApplicationException
	 */
	private void showConsents(DistributionForm dForm, Specimen specimen,
			HttpServletRequest request, String barcodeLable) throws ApplicationException
	{

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String initialURLValue = "";
		String initialWitnessValue = "";
		String initialSignedConsentDateValue = "";

		final CollectionProtocolRegistration collectionProtocolRegistration = ConsentUtil
				.getCollectionProtRegistration(specimen);

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

		if (consentWitness == null)
		{
			initialWitnessValue = Constants.NULL;
		}
		if (collectionProtocolRegistration.getConsentSignatureDate() == null)
		{
			initialSignedConsentDateValue = Constants.NULL;
		}
		final List cprObjectList = new ArrayList();
		cprObjectList.add(collectionProtocolRegistration);
		final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
				.getAttribute(Constants.SESSION_DATA);
		final CaCoreAppServicesDelegator caCoreAppServicesDelegator = new CaCoreAppServicesDelegator();
		final String userName = CommonUtilities.toString(sessionDataBean.getUserName());
		try
		{
			caCoreAppServicesDelegator.delegateSearchFilter(userName, cprObjectList);
		}
		catch (final Exception e)
		{
			this.logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		final CollectionProtocolRegistration cprObject = collectionProtocolRegistration;// (
		final String witnessName = ConsentUtil.getWitnessName(bizLogic, initialWitnessValue,
				cprObject);
		final String consentDate = ConsentUtil.getConsentDate(initialSignedConsentDateValue,
				cprObject);
		final String signedConsentURL = ConsentUtil.getSignedConsentURL(initialURLValue, cprObject);

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
					&& !(specimen.getActivityStatus().equalsIgnoreCase(Constants.DISABLED)))// disabled
			{
				final String[] barcodeLabelAttribute = new String[5];
				barcodeLabelAttribute[0] = witnessName;
				barcodeLabelAttribute[1] = consentDate;
				barcodeLabelAttribute[2] = signedConsentURL;
				barcodeLabelAttribute[3] = Integer.toString(this.consentTierCounter);
				barcodeLabelAttribute[4] = barcodeLable;
				this.listOfMap.add(tempMap);
				this.listOfStringArray.add(barcodeLabelAttribute);
			}
		}
	}

	// Consent tracking (Virender Mehta)
	/**
	 * @param dForm
	 *            object of DistributionForm
	 * @param itemNo
	 *            value for item no in int
	 * @param specimen
	 *            object of Specimen
	 */
	private void addDistributionSample(DistributionForm dForm, int itemNo, Specimen specimen)
	{
		final String keyPrefix = "DistributedItem:" + itemNo + "_";

		dForm.setValue(keyPrefix + "Specimen_id", specimen.getId());
		dForm.setValue(keyPrefix + "Specimen_barcode", specimen.getBarcode());
		dForm.setValue(keyPrefix + "Specimen_label", specimen.getLabel());
		dForm.setValue(keyPrefix + "availableQty", specimen.getAvailableQuantity());

		dForm.setCounter(dForm.getCounter() + 1);
		dForm.setDistributionBasedOn(new Integer(2));
	}

	/**
	 * @param dForm
	 *            object of DistributionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private void setSpecimenCharateristics(DistributionForm dForm, HttpServletRequest request)
			throws BizLogicException
	{
		// Set specimen characteristics according to the specimen ID changed
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final DistributionBizLogic dao = (DistributionBizLogic) factory
				.getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		final String specimenIdKey = request.getParameter("specimenIdKey");

		// Parse key to get row number. Key is in the format
		// DistributedItem:rowNo_Specimen_id
		final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.Distribution");
		final int rowNo = parser.parseKeyAndGetRowNo(specimenIdKey);

		// int a = Integer.parseInt()
		this.logger.debug("row number of the dist item: " + rowNo);

		final String specimenId = (String) dForm.getValue("DistributedItem:" + rowNo
				+ "_Specimen_id");

		// if '--Select--' is selected in the drop down of Specimen Id, empty
		// the row values for that distributed item
		if (specimenId.equals("-1"))
		{
			dForm.setValue("DistributedItem:" + rowNo + "_availableQty", null);
		}
		// retrieve the row values for the distributed item for the selected
		// specimen id
		else
		{
			final List list = dao.retrieve(Specimen.class.getName(), Constants.SYSTEM_IDENTIFIER,
					specimenId);
			final Specimen specimen = (Specimen) list.get(0);
			dForm.setValue("DistributedItem:" + rowNo + "_availableQty", dForm
					.getAvailableQty(specimen));
		}

		this.logger.debug("Map values after speci chars are set: " + dForm.getValues());
		// Set back the idChange boolean to false.
		dForm.setIdChange(false);
	}

	// Consent Tracking Virender Mehta
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
				final ConsentTierResponse consentTierResponse = (ConsentTierResponse) consentResponseCollectionIter
						.next();
				consentTierID = consentTierResponse.getConsentTier().getId();
				final Iterator specimenCollectionIter = specimenLevelResponseList.iterator();
				while (specimenCollectionIter.hasNext())
				{
					final ConsentTierStatus specimenConsentResponse = (ConsentTierStatus) specimenCollectionIter
							.next();
					consentID = specimenConsentResponse.getConsentTier().getId();
					if (consentTierID.longValue() == consentID.longValue())
					{
						final ConsentTier consent = consentTierResponse.getConsentTier();
						final String idKey = "ConsentBean:" + i + "_consentTierID";
						final String statementKey = "ConsentBean:" + i + "_statement";
						final String responseKey = "ConsentBean:" + i + "_participantResponse";
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
						tempMap.put(specimenResponsekey, specimenConsentResponse.getStatus());
						tempMap.put(specimenResponseIDkey, specimenConsentResponse.getId());
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
	 * This function returns SpecimenCollectionGroup object by reading Barcode
	 * value.
	 *
	 * @param barcode
	 *            : barcode
	 * @param barcodeLabelBasedDistribution
	 *            : barcodeLabelBasedDistribution
	 * @return Specimen : Specimen
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private Specimen getConsentListForSpecimen(String barcode, int barcodeLabelBasedDistribution)
			throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) factory
				.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		String colName = null;
		if (barcodeLabelBasedDistribution == Constants.BARCODE_BASED_DISTRIBUTION)
		{
			colName = Constants.SYSTEM_BARCODE;// "barcode"
		}
		else
		{
			colName = Constants.SYSTEM_LABEL;// "label"
		}
		final List specimenList = newSpecimenBizLogic.retrieve(Specimen.class.getName(), colName,
				barcode);
		final Specimen specimen = (Specimen) specimenList.get(0);
		return specimen;
	}

	// Consent Tracking Virender Mehta

	/**
	 * This method sets all the common parameters for the SpecimenEventParameter.
	 * pages
	 *
	 * @param request
	 *            HttpServletRequest instance in which the data will be set.
	 * @throws Exception
	 *             Throws Exception. Helps in handling exceptions at one common
	 *             point.
	 */
	private void setCommonRequestParameters(HttpServletRequest request) throws Exception
	{
		// Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);

		// Sets the operation attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute(Constants.OPERATION, operation);

		// Sets the minutesList attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute(Constants.MINUTES_LIST, Constants.MINUTES_ARRAY);

		// Sets the hourList attribute to be used in the Add/Edit
		// FrozenEventParameters Page.
		request.setAttribute(Constants.HOUR_LIST, Constants.HOUR_ARRAY);

		// The id of specimen of this event.
		final String specimenId = request.getParameter(Constants.SPECIMEN_ID);
		request.setAttribute(Constants.SPECIMEN_ID, specimenId);
		this.logger
				.debug("\t\t SpecimenEventParametersAction************************************ : "
						+ specimenId);

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		final Collection userCollection = userBizLogic.getUsers(operation);

		request.setAttribute(Constants.USERLIST, userCollection);

		// This method will be overridden by the sub classes
		this.setRequestParameters(request);

	}

}