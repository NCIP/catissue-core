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
import edu.wustl.catissuecore.util.global.AppUtility;
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
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class DistributionAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient Logger logger = Logger.getCommonLogger(DistributionAction.class);
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
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		DistributionBizLogic dao = (DistributionBizLogic) factory
				.getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		// Sets the Site list.
		String sourceObjectName = Site.class.getName();
		String[] displayNameFields = {"name"};
		String valueField = Constants.SYSTEM_IDENTIFIER;

		List siteList = dao.getList(sourceObjectName, displayNameFields, valueField, true);

		request.setAttribute(Constants.FROM_SITE_LIST, siteList);
		request.setAttribute(Constants.TO_SITE_LIST, siteList);

		// Sets the Distribution Protocol Id List.
		sourceObjectName = DistributionProtocol.class.getName();

		String[] displayName = {"title"};

		List protocolList = dao.getList(sourceObjectName, displayName, valueField, true);
		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);

		// SET THE SPECIMEN Ids LIST
		String[] displayNameField = {Constants.SYSTEM_IDENTIFIER};
		List specimenList = dao.getList(Specimen.class.getName(), displayNameField, valueField,
				true);
		request.setAttribute(Constants.SPECIMEN_ID_LIST, specimenList);

		// Sets the activityStatusList attribute to be used in the Site Add/Edit
		// Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		List distributionType = new ArrayList();
		distributionType.add(new NameValueBean("Specimen", new Integer(
				Constants.SPECIMEN_DISTRIBUTION_TYPE)));
		distributionType.add(new NameValueBean("Specimen Array", new Integer(
				Constants.SPECIMEN_ARRAY_DISTRIBUTION_TYPE)));

		List distributionBasedOn = new ArrayList();
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
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DistributionForm dForm = (DistributionForm) form;
		setCommonRequestParameters(request);

		// EventParametersForm eventParametersForm = (EventParametersForm)form;

		// if operation is add
		if (dForm.isAddOperation())
		{
			if (dForm.getUserId() == 0)
			{
				SessionDataBean sessionData = getSessionData(request);
				if (sessionData != null && sessionData.getUserId() != null)
				{
					long userId = sessionData.getUserId().longValue();
					dForm.setUserId(userId);
				}
			}
			// set the current Date and Time for the event.
			Calendar cal = Calendar.getInstance();
			if (dForm.getDateOfEvent() == null)
			{
				dForm.setDateOfEvent(Utility.parseDateToString(cal.getTime(), CommonServiceLocator
						.getInstance().getDatePattern()));
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
			String test = null;
			test.getBytes();
			setSpecimenCharateristics(dForm, request);
		}

		String distributionBasedOn = request.getParameter("distributionBasedOnOption");
		if (distributionBasedOn != null && !distributionBasedOn.equals("")
				&& distributionBasedOn.equals("unknown"))
		{
			dForm.setDistributionBasedOn(new Integer(0));
		}

		/* If forwarded from speciman page */
		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
		if (forwardToHashMap == null)
		{
			String specimenLabel = request.getParameter(Constants.SYSTEM_LABEL);
			if (specimenLabel != null)
			{
				IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
				List list = bizLogic.retrieve(Specimen.class.getName(), Constants.SYSTEM_LABEL,
						specimenLabel);
				if (list != null && !list.isEmpty())
				{
					Specimen specimen = (Specimen) list.get(0);
					forwardToHashMap = new HashMap();
					forwardToHashMap.put("specimenObjectKey", specimen);
				}
			}
		}

		if (forwardToHashMap != null)
		{
			Object specimenObjectOrList = forwardToHashMap.get("specimenObjectKey");
			if (specimenObjectOrList != null)
			{
				if (specimenObjectOrList instanceof Specimen)
				{
					/*
					 * this code is for setting specimenId for showing specimen
					 * selected in tree
					 */
					Specimen sp = (Specimen) specimenObjectOrList;
					String spId = sp.getId().toString();
					request.setAttribute(Constants.SPECIMEN_ID, spId);

					addDistributionSample((DistributionForm) form, 1,
							(Specimen) specimenObjectOrList);
				}
				else
				{
					List specimenIdList = (List) specimenObjectOrList;
					IFactory factory = AbstractFactoryConfig.
					getInstance().getBizLogicFactory();
					DistributionBizLogic dao = (DistributionBizLogic) factory
							.getBizLogic(Constants.DISTRIBUTION_FORM_ID);

					for (int i = 0; i < specimenIdList.size(); i++)
					{
						Long specimenId = Long.getLong((String) specimenIdList.get(i));
						List list = dao.retrieve(Specimen.class.getName(),
								Constants.SYSTEM_IDENTIFIER, specimenId);
						Specimen specimen = (Specimen) list.get(0);
						addDistributionSample((DistributionForm) form,
								i + 1, (Specimen) specimen);
					}
				}
			}
			else
			// If forwarded from Aliquout Summary page;
			{
				IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
				String noOfAliquots = (String) forwardToHashMap.get("noOfAliquots");
				String labelKey;
				List specimenList;
				for (int i = 0; i < Integer.valueOf(noOfAliquots).intValue(); i++)
				{
					labelKey = "Specimen:" + (i + 1) + "_label";
					String label = (String) forwardToHashMap.get(labelKey);
					specimenList = bizLogic.retrieve(Specimen.class.getName(),
							Constants.SYSTEM_LABEL, label);
					if (!specimenList.isEmpty())
					{
						Specimen specimen = (Specimen) specimenList.get(0);
						addDistributionSample((DistributionForm) form,
								i + 1, (Specimen) specimen);
					}

				}

			}
		}
		// Consent Tracking (Virender Mehta)
		// Show Consents for Specimen
		String specimenConsents = request.getParameter(Constants.SPECIMEN_CONSENTS); // "specimenConsents"
		if (specimenConsents != null && specimenConsents.equalsIgnoreCase(Constants.YES))
		{
			String labelBarcodeDistributionBasedOnValue = request
					.getParameter(Constants.DISTRIBUTION_ON);// "labelBarcode"
			String barcodeLableValue = request.getParameter(Constants.BARCODE_LABLE);// barcodelabel
			int distributionOn = Integer.parseInt(labelBarcodeDistributionBasedOnValue);
			StringTokenizer stringToken = new StringTokenizer(barcodeLableValue, "|");
			listOfMap = new ArrayList();
			listOfStringArray = new ArrayList();
			while (stringToken.hasMoreTokens())
			{
				String barcodeLable = stringToken.nextToken();
				// function that will check if the barcode/lable is present in
				// the Database or not
				Specimen specimen = getConsentListForSpecimen(barcodeLable, distributionOn);
				// Set all the consents and attributes related to specimen in
				// List.
				showConsents(dForm, specimen, request, barcodeLable);
			}
			request.setAttribute("listOfStringArray", listOfStringArray);
			request.setAttribute("listOfMap", listOfMap);
			return mapping.findForward(Constants.VIEWAll);// ViewAll
		}
		// Show All Consents for Specimen(Consent tarcking)

		// Show Consents for Specimen(Consent Tracking)
		String showConsents = request.getParameter(Constants.SHOW_CONSENTS);
		if (showConsents != null && showConsents.equalsIgnoreCase(Constants.YES))
		{
			String barcodeLable = request.getParameter(Constants.BARCODE_LABLE);
			int barcodeLabelBasedDistribution = 1;
			String labelBarcodeDistributionValue =
				request.getParameter(Constants.DISTRIBUTION_ON);// labelBarcode
			if (labelBarcodeDistributionValue.equalsIgnoreCase(Constants.BARCODE_DISTRIBUTION))// "1"
			{
				barcodeLabelBasedDistribution = 1;
			}
			else
			{
				barcodeLabelBasedDistribution = 2;
			}
			// Getting SpecimenCollectionGroup object
			Specimen specimen = getConsentListForSpecimen(barcodeLable,
					barcodeLabelBasedDistribution);
			// SpecimenCollectionGroup specimenCollectionGroup =
			// getConsentListForSpecimen(barcode);
			showConsents(dForm, specimen, request, barcodeLable);

			request.setAttribute("barcodeStatus", Constants.VALID);// valid
			return mapping.findForward(Constants.POPUP);
		}
		// Consent Tracking (Virender Mehta)
		logger.debug("executeSecureAction");
		String pageOf = request.getParameter(Constants.PAGE_OF);
		request.setAttribute(Constants.PAGE_OF, pageOf);

		return mapping.findForward((String) request.getParameter(Constants.PAGE_OF));
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

		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String initialURLValue = "";
		String initialWitnessValue = "";
		String initialSignedConsentDateValue = "";

		Long specimenId = (Long) specimen.getId();
		String colProtHql = "select scg.collectionProtocolRegistration"
				+ " from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg,"
				+ " edu.wustl.catissuecore.domain.Specimen as spec "
				+ " where spec.specimenCollectionGroup.id=scg.id and spec.id=" + specimenId;

		List collectionProtocolRegistrationList = AppUtility.executeQuery(colProtHql);
		CollectionProtocolRegistration collectionProtocolRegistration = null;
		if (collectionProtocolRegistrationList != null)
		{
			collectionProtocolRegistration =
				(CollectionProtocolRegistration) collectionProtocolRegistrationList
					.get(0);
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
		if (collectionProtocolRegistration.getConsentSignatureDate() == null)
		{
			initialSignedConsentDateValue = Constants.NULL;
		}
		List cprObjectList = new ArrayList();
		cprObjectList.add(collectionProtocolRegistration);
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		CaCoreAppServicesDelegator caCoreAppServicesDelegator = new CaCoreAppServicesDelegator();
		String userName = Utility.toString(sessionDataBean.getUserName());
		List collProtObject = null;
		try
		{
			collProtObject = caCoreAppServicesDelegator.delegateSearchFilter(userName,
					cprObjectList);
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}
		CollectionProtocolRegistration cprObject = collectionProtocolRegistration;// (
		// CollectionProtocolRegistration
		// )
		// collProtObject
		// .
		// get
		// (
		// 0
		// )
		// ;
		// Getting WitnessName,Consent Date,Signed Url using
		// collectionProtocolRegistration object
		String witnessName = "";
		String getConsentDate = "";
		String getSignedConsentURL = "";
		User witness = cprObject.getConsentWitness();
		if (witness == null)
		{
			if (initialWitnessValue.equals(Constants.NULL))
			{
				witnessName = initialWitnessValue;
			}
			else
			{
				witnessName = Constants.HASHED_OUT;
			}
			dForm.setWitnessName(witnessName);
		}
		else
		{
			witness = (User) bizLogic.retrieveAttribute(CollectionProtocolRegistration.class
					.getName(), cprObject.getId(), "consentWitness");
			String witnessFullName = witness.getLastName() + ", " + witness.getFirstName();
			dForm.setWitnessName(witnessFullName);
		}
		if (cprObject.getConsentSignatureDate() == null)
		{
			if (initialSignedConsentDateValue.equals(Constants.NULL))
			{
				getConsentDate = "";
			}
			else
			{
				getConsentDate = Constants.HASHED_OUT;
			}
		}
		else
		{
			getConsentDate = Utility.parseDateToString(cprObject.getConsentSignatureDate(),
					CommonServiceLocator.getInstance().getDatePattern());
		}

		if (cprObject.getSignedConsentDocumentURL() == null)
		{
			if (initialURLValue.equals(Constants.NULL))
			{
				getSignedConsentURL = "";
			}
			else
			{
				getSignedConsentURL = Constants.HASHED_OUT;
			}
		}
		else
		{
			getSignedConsentURL = Utility.toString(cprObject.getSignedConsentDocumentURL());
		}
		// Setting WitnessName,ConsentDate and Signed Consent Url
		dForm.setConsentDate(getConsentDate);
		dForm.setSignedConsentUrl(getSignedConsentURL);

		// Getting ConsentResponse collection for CPR level
		// Resolved lazy ---
		// collectionProtocolRegistration.getConsentTierResponseCollection();
		Collection participantResponseCollection = (Collection) bizLogic.retrieveAttribute(
				CollectionProtocolRegistration.class.getName(), collectionProtocolRegistration
						.getId(), "elements(consentTierResponseCollection)");
		// Getting ConsentResponse collection for Specimen level
		// Resolved lazy --- specimen.getConsentTierStatusCollection();
		Collection specimenLevelResponseCollection = (Collection) bizLogic
				.retrieveAttribute(Specimen.class.getName(), specimen.getId(),
						"elements(consentTierStatusCollection)");
		// Prepare Map and iterate both Collections
		Map tempMap = prepareConsentMap(participantResponseCollection,
				specimenLevelResponseCollection);
		// Setting map and counter in the form
		dForm.setConsentResponseForDistributionValues(tempMap);
		dForm.setConsentTierCounter(consentTierCounter);
		String specimenConsents = request.getParameter(Constants.SPECIMEN_CONSENTS);
		if (specimenConsents != null && specimenConsents.equalsIgnoreCase(Constants.YES))
		{
			// For no consents and Consent waived
			if (consentTierCounter > 0
					&& !(specimen.getActivityStatus().
							equalsIgnoreCase(Constants.DISABLED)))// disabled
			{
				String[] barcodeLabelAttribute = new String[5];
				barcodeLabelAttribute[0] = witnessName;
				barcodeLabelAttribute[1] = getConsentDate;
				barcodeLabelAttribute[2] = getSignedConsentURL;
				barcodeLabelAttribute[3] = Integer.toString(consentTierCounter);
				barcodeLabelAttribute[4] = barcodeLable;
				listOfMap.add(tempMap);
				listOfStringArray.add(barcodeLabelAttribute);
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
		String keyPrefix = "DistributedItem:" + itemNo + "_";

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
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		DistributionBizLogic dao = (DistributionBizLogic) factory
				.getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		String specimenIdKey = request.getParameter("specimenIdKey");

		// Parse key to get row number. Key is in the format
		// DistributedItem:rowNo_Specimen_id
		MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.Distribution");
		int rowNo = parser.parseKeyAndGetRowNo(specimenIdKey);

		// int a = Integer.parseInt()
		logger.debug("row number of the dist item: " + rowNo);

		String specimenId = (String) dForm.getValue("DistributedItem:" + rowNo + "_Specimen_id");

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
			List list = dao.retrieve(Specimen.class.getName(), Constants.SYSTEM_IDENTIFIER,
					specimenId);
			Specimen specimen = (Specimen) list.get(0);
			dForm.setValue("DistributedItem:" + rowNo + "_availableQty", dForm
					.getAvailableQty(specimen));
		}

		logger.debug("Map values after speci chars are set: " + dForm.getValues());
		// Set back the idChange boolean to false.
		dForm.setIdChange(false);
	}

	// Consent Tracking Virender Mehta
	/**
	 * Prepare Map for Consent tiers
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
		Map tempMap = new HashMap();
		Long consentTierID;
		Long consentID;
		if (participantResponseList != null)
		{
			int i = 0;
			Iterator consentResponseCollectionIter = participantResponseList.iterator();
			while (consentResponseCollectionIter.hasNext())
			{
				ConsentTierResponse consentTierResponse =
					(ConsentTierResponse) consentResponseCollectionIter
						.next();
				consentTierID = consentTierResponse.getConsentTier().getId();
				Iterator specimenCollectionIter = specimenLevelResponseList.iterator();
				while (specimenCollectionIter.hasNext())
				{
					ConsentTierStatus specimenConsentResponse =
						(ConsentTierStatus) specimenCollectionIter
							.next();
					consentID = specimenConsentResponse.getConsentTier().getId();
					if (consentTierID.longValue() == consentID.longValue())
					{
						ConsentTier consent = consentTierResponse.getConsentTier();
						String idKey = "ConsentBean:" + i + "_consentTierID";
						String statementKey = "ConsentBean:" + i + "_statement";
						String responseKey = "ConsentBean:" + i + "_participantResponse";
						String participantResponceIdKey = "ConsentBean:" + i
								+ "_participantResponseID";
						String specimenResponsekey = "ConsentBean:"
							+ i + "_specimenLevelResponse";
						String specimenResponseIDkey = "ConsentBean:" + i
								+ "_specimenLevelResponseID";
						// Adding Keys and its data into the Map
						tempMap.put(idKey, consent.getId());
						tempMap.put(statementKey, consent.getStatement());
						tempMap.put(responseKey, consentTierResponse.getResponse());
						tempMap.put(participantResponceIdKey, consentTierResponse.getId());
						tempMap.put(specimenResponsekey,
								specimenConsentResponse.getStatus());
						tempMap.put(specimenResponseIDkey,
								specimenConsentResponse.getId());
						i++;
						break;
					}
				}
			}
			consentTierCounter = i;
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
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) factory
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
		List specimenList = newSpecimenBizLogic
				.retrieve(Specimen.class.getName(), colName, barcode);
		Specimen specimen = (Specimen) specimenList.get(0);
		return specimen;
	}

	// Consent Tracking Virender Mehta

	/**
	 * This method sets all the common parameters for the SpecimenEventParameter
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
		String operation = request.getParameter(Constants.OPERATION);

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
		String specimenId = request.getParameter(Constants.SPECIMEN_ID);
		request.setAttribute(Constants.SPECIMEN_ID, specimenId);
		logger.debug("\t\t SpecimenEventParametersAction************************************ : "
				+ specimenId);

		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		UserBizLogic userBizLogic = (UserBizLogic) factory.getBizLogic(Constants.USER_FORM_ID);
		Collection userCollection = userBizLogic.getUsers(operation);

		request.setAttribute(Constants.USERLIST, userCollection);

		// This method will be overridden by the sub classes
		setRequestParameters(request);

	}

}