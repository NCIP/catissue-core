/**
 * <p>
 * Title: CreateSpecimenAction Class>
 * <p>
 * Description: CreateSpecimenAction initializes the fields in the Create
 * Specimen page.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.bean.ExternalIdentifierBean;
import edu.wustl.catissuecore.bizlogic.StorageContainerForSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.SpecimenUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.tag.ScriptGenerator;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;

// TODO: Auto-generated Javadoc
/**
 * CreateSpecimenAction initializes the fields in the Create Specimen page.
 *
 * @author aniruddha_phadnis
 */
public class CreateSpecimenAction extends SecureAction
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(CreateSpecimenAction.class);

	/** The Constant TRUE_STRING. */
	private static final String TRUE_STRING="true";

	/** The Constant PAGE_OF_STRING. */
	private static final String PAGE_OF_STRING="pageOf";

	/** The Constant INITVALUES_STRING. */
	private final static String INITVALUES_STRING="initValues";

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return ActionForward : ActionForward
	 *
	 * @throws Exception generic exception
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final CreateSpecimenForm createForm = (CreateSpecimenForm) form;

		final List<NameValueBean> strgPosList = AppUtility.getStoragePositionTypeList();

		request.setAttribute("storageList", strgPosList);
		// List of keys used in map of ActionForm
		final List key = new ArrayList();
		key.add("ExternalIdentifier:i_name");
		key.add("ExternalIdentifier:i_value");

		// boolean to indicate whether the suitable containers to be shown in
		// dropdown
		// is exceeding the max limit.
		String exceedingMaxLimit = "false";

		// Gets the map from ActionForm
		final Map map = createForm.getExternalIdentifier();

		// Calling DeleteRow of BaseAction class
		MapDataParser.deleteRow(key, map, request.getParameter("status"));

		// Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);

		// Sets the operation attribute to be used in the Add/Edit User Page.
		request.setAttribute(Constants.OPERATION, operation);
		final String virtuallyLocated = request.getParameter("virtualLocated");
		if (virtuallyLocated != null && virtuallyLocated.equals(TRUE_STRING))
		{
			createForm.setVirtuallyLocated(true);
		}

		/**
		 * Patch ID: 3835_1_16 See also: 1_1 to 1_5 Description : CreatedOn date
		 * by default should be current date.
		 */
		if(request.getParameter("path") != null || (createForm.getCreatedDate() == null || createForm.getCreatedDate() == ""))
		{
			createForm.setCreatedDate(edu.wustl.common.util.Utility.parseDateToString(Calendar
					.getInstance().getTime(), CommonServiceLocator.getInstance().getDatePattern()));
		}
		String pageOf = null;
		final String tempPageOf = (String) request.getParameter(PAGE_OF_STRING);
		if (tempPageOf == null || tempPageOf.equals(""))
		{
			pageOf = (String) request.getSession().getAttribute(PAGE_OF_STRING);
		}
		else
		{
			pageOf = request.getParameter(Constants.PAGE_OF);
			request.getSession().setAttribute(PAGE_OF_STRING, pageOf);
		}

		final SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);

		/*
		 * // ---- chetan 15-06-06 ---- StorageContainerBizLogic bizLogic =
		 * (StorageContainerBizLogic
		 * )BizLogicFactory.getInstance().getBizLogic(Constants
		 * .STORAGE_CONTAINER_FORM_ID); Map containerMap =
		 * bizLogic.getAllocatedContainerMap();
		 * request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
		 * // -------------------------
		 * request.setAttribute(Constants.PAGE_OF,pageOf);
		 */
		request.setAttribute(Constants.PAGE_OF, pageOf);

		TreeMap containerMap = new TreeMap();
		List initialValues = null;
		final DAO dao = AppUtility.openDAOSession(sessionData);
		try
		{
			if (operation.equals(Constants.ADD))
			{
				// if this action bcos of delete external identifier then
				// validation should not happen.
				if (request.getParameter("button") == null)
				{
					String parentSpecLbl = null;
					Long parentSpecimenID = null;
					// Bug-2784: If coming from NewSpecimen page, then only set
					// parent specimen label.
					final Map forwardToHashMap = (Map) request.getAttribute("forwardToHashMap");
					if (forwardToHashMap != null
							&& forwardToHashMap.get("parentSpecimenId") != null
							&& forwardToHashMap.get(Constants.SPECIMEN_LABEL) != null)
					{
						parentSpecimenID = (Long) forwardToHashMap.get("parentSpecimenId");
						parentSpecLbl = forwardToHashMap.get(Constants.SPECIMEN_LABEL)
								.toString();
						request.setAttribute(Constants.PARENT_SPECIMEN_ID, parentSpecimenID);
						createForm.setParentSpecimenLabel(parentSpecLbl);
						String hql = "select specimen.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.specimenLabelFormat, " +
						"specimen.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.derivativeLabelFormat, " +
						"specimen.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.aliquotLabelFormat " +
						"from edu.wustl.catissuecore.domain.Specimen as specimen where specimen.id ="+ parentSpecimenID;

						List formatList = AppUtility.executeQuery(hql);
						String parentLabelFormat = null;
						String deriveLabelFormat = null;
						String alqLabelFrmt = null;
						if(!formatList.isEmpty())
						{
							Object[] obje = (Object[])formatList.get(0);
							if(obje[0] != null)
							{
								parentLabelFormat = obje[0].toString();
							}
							if(obje[1] != null)
							{
								deriveLabelFormat = obje[1].toString();
							}
							if(obje[2] != null)
							{
								alqLabelFrmt = obje[2].toString();
							}
						}
						if(Variables.isTemplateBasedLblGeneratorAvl)
						{
							createForm.setGenerateLabel(SpecimenUtil.isLblGenOnForCP(parentLabelFormat, deriveLabelFormat, alqLabelFrmt, Constants.DERIVED_SPECIMEN));
						}
						else if(Variables.isSpecimenLabelGeneratorAvl)
						{
							createForm.setGenerateLabel(true);
						}
						else
						{
							createForm.setGenerateLabel(false);
						}

						createForm.setLabel("");
					}

					if (createForm.getLabel() == null || createForm.getLabel().equals(""))
					{
						/**
						 * Name : Virender Mehta Reviewer: Sachin Lale
						 * Description: By getting instance of
						 * AbstractSpecimenGenerator abstract class current
						 * label retrived and set.
						 */
						// int totalNoOfSpecimen =
						// bizLogic.totalNoOfSpecimen(sessionData)+1;
						final HashMap inputMap = new HashMap();
						inputMap.put(Constants.PARENT_SPECIMEN_LABEL_KEY, parentSpecLbl);
						inputMap.put(Constants.PARENT_SPECIMEN_ID_KEY, String
								.valueOf(parentSpecimenID));

						// SpecimenLabelGenerator abstractSpecimenGenerator =
						// SpecimenLabelGeneratorFactory.getInstance();
						// createForm.setLabel(abstractSpecimenGenerator.
						// getNextAvailableDeriveSpecimenlabel(inputMap));
					}

					if (forwardToHashMap == null
							&& ((createForm.getRadioButton().equals("1")
									&& createForm.getParentSpecimenLabel() != null && !createForm
									.getParentSpecimenLabel().equals("")) || (createForm
									.getRadioButton().equals("2")
									&& createForm.getParentSpecimenBarcode() != null && !createForm
									.getParentSpecimenBarcode().equals(""))))
					{
						String errorString;
						String columnName;
						String columnValue;

						// checks whether label or barcode is selected
						if (createForm.getRadioButton().equals("1"))
						{
							columnName = Constants.SYSTEM_LABEL;
							columnValue = createForm.getParentSpecimenLabel().trim();
							errorString = ApplicationProperties
									.getValue("quickEvents.specimenLabel");
						}
						else
						{
							columnName = Constants.SYSTEM_BARCODE;
							columnValue = createForm.getParentSpecimenBarcode().trim();
							errorString = ApplicationProperties.getValue("quickEvents.barcode");
						}

						final String[] selectColumnName = {Constants.COLUMN_NAME_SCG_ID};
						final QueryWhereClause queryWhereClause = new QueryWhereClause(
								Specimen.class.getName());
						queryWhereClause.addCondition(new EqualClause(columnName, columnValue));
						final List scgList = dao.retrieve(Specimen.class.getName(),
								selectColumnName, queryWhereClause);

						boolean isSpecimenExist = true;
						if (scgList != null && !scgList.isEmpty())
						{
							// Specimen sp = (Specimen) spList.get(0);
							final Long scgId = (Long) scgList.get(0);
							final long cpId = this.getCpId(dao, scgId);
							if (cpId == -1)
							{
								isSpecimenExist = false;
							}
							final String spClass = createForm.getClassName();
							final String spType = createForm.getType();
							request.setAttribute(Constants.COLLECTION_PROTOCOL_ID, String.valueOf(cpId));
							request.setAttribute(Constants.SPECIMEN_CLASS_NAME, spClass);
							if (virtuallyLocated != null && virtuallyLocated.equals("false"))
							{
								createForm.setVirtuallyLocated(false);
							}
							if (spClass != null
									&& createForm.getStContSelection() != Constants.RADIO_BUTTON_VIRTUALLY_LOCATED)
							{

								final StorageContainerForSpecimenBizLogic scbizLogic = new StorageContainerForSpecimenBizLogic();
								containerMap = scbizLogic.
								getAllocatedContainerMapForSpecimen
								(AppUtility.setparameterList(cpId,spClass,0,spType),
								sessionData, dao);
								ActionErrors errors = (ActionErrors) request
										.getAttribute(Globals.ERROR_KEY);
								if (containerMap.isEmpty())
								{
									if (isErrorsEmpty(errors))
									{
										errors = new ActionErrors();
									}
									errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
											"storageposition.not.available"));
									this.saveErrors(request, errors);
								}
								if (isErrorsEmpty(errors))
								{
									initialValues = StorageContainerUtil
											.checkForInitialValues(containerMap);
								}
								else
								{
									final String[] startingPoints = new String[3];
									startingPoints[0] = createForm.getStorageContainer();
									startingPoints[1] = createForm.getPositionDimensionOne();
									startingPoints[2] = createForm.getPositionDimensionTwo();
									initialValues = new ArrayList();
									initialValues.add(startingPoints);
								}

							}
							/**
							 * Name : Vijay_Pande Patch ID: 4283_2 See also: 1-3
							 * Description: If radio button is clicked for map
							 * then clear values in the drop down list for
							 * storage position
							 */
							if (spClass != null
									&& createForm.getStContSelection() == Constants.RADIO_BUTTON_FOR_MAP)
							{
								final String[] startingPoints = new String[]{"-1", "-1", "-1"};
								initialValues = new ArrayList();
								initialValues.add(startingPoints);
								request.setAttribute(INITVALUES_STRING, initialValues);
							}
							/** -- patch ends here -- */
						}
						else
						{
							isSpecimenExist = false;
						}

						if (!isSpecimenExist)
						{
							ActionErrors errors = (ActionErrors) request
									.getAttribute(Globals.ERROR_KEY);
							if (errors == null)
							{
								errors = new ActionErrors();
							}
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"quickEvents.specimen.notExists", errorString));
							this.saveErrors(request, errors);
							request.setAttribute("disabled", TRUE_STRING);
							createForm.setVirtuallyLocated(true);
						}

					}
				}
			}

			else
			{
				containerMap = new TreeMap();
				final Integer identifier = Integer.valueOf(createForm.getStorageContainer());
				String parentCntName = "";

				final Object object = dao.retrieveById(StorageContainer.class.getName(), Long.valueOf(
						createForm.getStorageContainer()));
				if (object != null)
				{
					final StorageContainer container = (StorageContainer) object;
					parentCntName = container.getName();

				}
				final Integer pos1 = Integer.valueOf(createForm.getPositionDimensionOne());
				final Integer pos2 = Integer.valueOf(createForm.getPositionDimensionTwo());

				final List pos2List = new ArrayList();
				pos2List.add(new NameValueBean(pos2, pos2));

				final Map pos1Map = new TreeMap();
				pos1Map.put(new NameValueBean(pos1, pos1), pos2List);
				containerMap.put(new NameValueBean(parentCntName, identifier), pos1Map);

				final String[] startingPoints = new String[]{"-1", "-1", "-1"};
				if (createForm.getStorageContainer() != null
						&& !createForm.getStorageContainer().equals("-1"))
				{
					startingPoints[0] = createForm.getStorageContainer();

				}
				if (createForm.getPositionDimensionOne() != null
						&& !createForm.getPositionDimensionOne().equals("-1"))
				{
					startingPoints[1] = createForm.getPositionDimensionOne();
				}
				if (createForm.getPositionDimensionTwo() != null
						&& !createForm.getPositionDimensionTwo().equals("-1"))
				{
					startingPoints[2] = createForm.getPositionDimensionTwo();
				}
				initialValues = new ArrayList();
				initialValues.add(startingPoints);

			}
		}
		catch (final DAOException daoException)
		{
			LOGGER.error(daoException.getMessage(),daoException);
			throw AppUtility.getApplicationException(daoException, daoException.getErrorKeyName(),
					daoException.getMsgValues());
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
		request.setAttribute(INITVALUES_STRING, initialValues);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		// -------------------------
		// Setting the specimen type list
		final List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
		// Setting biohazard list
		final List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_BIOHAZARD, null);
		request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);

		final Map subTypeMap = AppUtility.getSpecimenTypeMap();
		final List specimenClassList = AppUtility.getSpecimenClassList();
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
		// set the map to subtype
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);

		// ************* ForwardTo implementation *************
		final HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");

		if (forwardToHashMap != null)
		{
			final Long parentSpecimenId = (Long) forwardToHashMap.get("parentSpecimenId");

			if (parentSpecimenId != null)
			{
				createForm.setParentSpecimenId(parentSpecimenId.toString());
				createForm.setPositionInStorageContainer("");
				createForm.setSelectedContainerName("");
				createForm.setQuantity("");
				createForm.setPositionDimensionOne("");
				createForm.setPositionDimensionTwo("");
				createForm.setStorageContainer("");
				createForm.setBarcode(null);
				map.clear();
				createForm.setExternalIdentifier(map);
				createForm.setExIdCounter(1);
				createForm.setVirtuallyLocated(false);
				createForm.setStContSelection(1);
				// containerMap =
				// getContainerMap(createForm.getParentSpecimenId(), createForm
				// .getClassName(), dao, scbizLogic,exceedingMaxLimit,request);
				// initialValues = checkForInitialValues(containerMap);
				/**
				 * Name : Vijay_Pande Reviewer Name : Sachin_Lale Bug ID: 4283
				 * Patch ID: 4283_1 See also: 1-3 Description: Proper Storage
				 * location of derived specimen was not populated while coming
				 * from newly created parent specimen page. Initial value were
				 * generated but not set to form variables.
				 */
				// if(initialValues!=null)
				// {
				// initialValues = checkForInitialValues(containerMap);
				// String[] startingPoints=(String[])initialValues.get(0);
				// createForm.setStorageContainer(startingPoints[0]);
				// createForm.setPos1(startingPoints[1]);
				// createForm.setPos2(startingPoints[2]);
				// }
				/** --patch ends here -- */
			}
		}
		// ************* ForwardTo implementation *************
		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		if (createForm.isVirtuallyLocated())
		{
			request.setAttribute("disabled", TRUE_STRING);
		}
		this.setPageData(request, createForm);
		request.setAttribute("createdDate", createForm.getCreatedDate());
		final List dataList = (List) request
				.getAttribute(edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST);
		final List columnList = new ArrayList();
		columnList.addAll(Arrays.asList(Constants.DERIVED_SPECIMEN_COLUMNS));
		AppUtility.setGridData(dataList, columnList, request);
		int idFieldIndex = 4;
		request.setAttribute("identifierFieldIndex", idFieldIndex);
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * Checks if is errors empty.
	 *
	 * @param errors the errors
	 *
	 * @return true, if checks if is errors empty
	 */
	private boolean isErrorsEmpty(ActionErrors errors)
	{
		return errors == null || errors.size() == 0;
	}

	/**
	 * Gets the cp id.
	 *
	 * @param dao : dao
	 * @param scgId : scgId
	 *
	 * @return long : long
	 *
	 * @throws BizLogicException : BizLogicException
	 */
	private long getCpId(DAO dao, Long scgId) throws BizLogicException
	{
		long cpId = -1;

		try
		{
			final String columnName = Constants.SYSTEM_IDENTIFIER;
			final String columnValue = String.valueOf(scgId);
			final String[] selectColumnName = {"collectionProtocolRegistration.collectionProtocol.id"};
			final QueryWhereClause queryWhereClause = new QueryWhereClause(
					SpecimenCollectionGroup.class.getName());
			queryWhereClause.addCondition(new EqualClause(columnName, columnValue));
			final List cpList = dao.retrieve(SpecimenCollectionGroup.class.getName(),
					selectColumnName, queryWhereClause);
			if (cpList != null && !cpList.isEmpty())
			{
				cpId = (Long) cpList.get(0);
			}
		}
		catch (final DAOException excep)
		{
			LOGGER.error(excep.getMessage(),excep);
			throw new BizLogicException(excep);
		}
		return cpId;
	}

	/**
	 * Sets the page data.
	 *
	 * @param request : request
	 * @param form : form
	 */

	private void setPageData(HttpServletRequest request, CreateSpecimenForm form)
	{

		this.setConstantValues(request);
		AppUtility.setDefaultPrinterTypeLocation(form);
		final String pageOf = (String) request.getAttribute(Constants.PAGE_OF);
		this.setPageData1(request, form);
		this.setPageData2(request,pageOf);
		this.setPageData3(request, form);

		this.setNComboData(request, form);
		this.setXterIdData(request);
	}

	/**
	 * Sets the page data1.
	 *
	 * @param request : request
	 * @param form : form
	 */
	private void setPageData1(HttpServletRequest request, CreateSpecimenForm form)
	{
		final String operation = (String) request.getAttribute(Constants.OPERATION);
		final String pageOf = (String) request.getAttribute(Constants.PAGE_OF);

		String formName = operation;
		String editViewButton = "buttons." + Constants.EDIT;
		boolean readOnlyForAll = false;
		String printAction = "printDeriveSpecimen";

		if (operation != null && operation.equals(Constants.EDIT))
		{
			editViewButton = "buttons." + Constants.VIEW;
			formName = Constants.CREATE_SPECIMEN_EDIT_ACTION;
		}
		else
		{
			formName = Constants.CREATE_SPECIMEN_ADD_ACTION;
			if (isPageOfSpecimenCPQuery(pageOf))
			{
				formName = Constants.CP_QUERY_CREATE_SPECIMEN_ADD_ACTION;
				printAction = "CPQueryPrintDeriveSpecimen";
			}
		}

		if (operation != null && operation.equals(Constants.VIEW))
		{
			readOnlyForAll = true;
		}

		// -------------

		final String changeAction3 = "setFormAction('" + formName + "')";
		final String confirmDisableFuncName = "confirmDisable('" + formName
				+ "',document.forms[0].activityStatus)";
		final String addMoreSubmitFunctionName = "setSubmitted('ForwardTo','" + printAction + "','"
				+ Constants.SPECIMEN_FORWARD_TO_LIST[3][1] + "')";
		final String addMoreSubmit = addMoreSubmitFunctionName + "," + confirmDisableFuncName;

		request.setAttribute("changeAction3", changeAction3);
		request.setAttribute("addMoreSubmit", addMoreSubmit);
		// -----------------

		request.setAttribute("pageOf", pageOf);
		request.setAttribute("operation", operation);

		request.setAttribute("formName", formName);
		request.setAttribute("editViewButton", editViewButton);
		request.setAttribute("readOnlyForAll", readOnlyForAll);
		request.setAttribute("printAction", printAction);

		String unitSpecimen = "";
		String frdTo = "";

		int exIdRows = 1;
		Map map = null;
		if (form != null)
		{
			map = form.getExternalIdentifier();
			exIdRows = form.getExIdCounter();
			frdTo = form.getForwardTo();
			if (form.getUnit() != null)
			{
				unitSpecimen = form.getUnit();
			}
			if (frdTo == null || frdTo.equals(""))
			{
				frdTo = "eventParameters";
			}
		}
		final List exIdList = new ArrayList();
		for (int i = exIdRows; i >= 1; i--)
		{
			final ExternalIdentifierBean exd = new ExternalIdentifierBean(i, map);
			exIdList.add(exd);
		}
		request.setAttribute("exIdList", exIdList);

		request.setAttribute("unitSpecimen", unitSpecimen);
		request.setAttribute("frdTo", frdTo);

		String multipleSpecimen = "0";
		String action = Constants.CREATE_SPECIMEN_ADD_ACTION;
		if (request.getAttribute("multipleSpecimen") != null)
		{
			multipleSpecimen = "1";
			action = "DerivedMultipleSpecimenAdd.do?retainForm=true";
		}
		request.setAttribute("multipleSpecimen", multipleSpecimen);
		request.setAttribute("action", action);

	}

	/**
	 * Sets the page data2.
	 *
	 * @param request : request
	 * @param pageOf : pageOf
	 */
	private void setPageData2(HttpServletRequest request,String pageOf)
	{
		setActionToCall2ToReq(request, pageOf);
		setActionToCall1ToReq(request, pageOf);
		String deleteChecked = "";
		final String multipleSpecimen = (String) request.getAttribute("multipleSpecimen");
		if ("1".equals(multipleSpecimen))
		{
			deleteChecked = "deleteChecked(\'addExternalIdentifier\',"
					+ "\'NewMultipleSpecimenAction.do?method="
					+ "showDerivedSpecimenDialog&status=true&retainForm="
					+ "true\',document.forms[0].exIdCounter,\'chk_ex_\'," + "false);";
		}
		else
		{
			if (isPageOfSpecimenCPQuery(pageOf))
			{
				deleteChecked = "deleteChecked(\'addExternalIdentifier\',\'"
						+ "CPQueryCreateSpecimen.do?pageOf="
						+ "pageOfCreateSpecimenCPQuery&status=true&button=deleteExId\',"
						+ "document.forms[0].exIdCounter,\'chk_ex_\',false);";
			}
			else
			{
				deleteChecked = "deleteChecked(\'addExternalIdentifier\',\'CreateSpecimen.do?"
						+ "pageOf=pageOfCreateSpecimen&status=true&button=deleteExId\',"
						+ "document.forms[0].exIdCounter,\'chk_ex_\',false);";
			}
		}
		request.setAttribute("deleteChecked", deleteChecked);

		setActionToCallToReq(request, pageOf);

		String showRefreshTree = "false";
		if (isPageOfSpecimenCPQuery(pageOf) && request.getAttribute(Constants.PARENT_SPECIMEN_ID) != null)
		{
				final Long parentSpecimenId = (Long) request
						.getAttribute(Constants.PARENT_SPECIMEN_ID);
				final String nodeId = "Specimen_" + parentSpecimenId.toString();
				showRefreshTree = TRUE_STRING;
				final String refreshTree = "refreshTree('" + Constants.CP_AND_PARTICIPANT_VIEW
						+ "','" + Constants.CP_TREE_VIEW + "','" + Constants.CP_SEARCH_CP_ID
						+ "','" + Constants.CP_SEARCH_PARTICIPANT_ID + "','" + nodeId + "');";
				request.setAttribute("refreshTree", refreshTree);
		}
		request.setAttribute("showRefreshTree", showRefreshTree);
	}

	/**
	 * Sets the action to call to req.
	 *
	 * @param request the request
	 * @param pageOf the page of
	 */
	private void setActionToCallToReq(HttpServletRequest request, String pageOf)
	{
		String actionToCall = "AddSpecimen.do?isQuickEvent=true";
		if (isPageOfSpecimenCPQuery(pageOf))
		{
			actionToCall = Constants.CP_QUERY_CREATE_SPECIMEN_ADD_ACTION + "?isQuickEvent=true";
		}
		request.setAttribute("actionToCall", actionToCall);
	}

	/**
	 * Sets the action to call1 to req.
	 *
	 * @param request the request
	 * @param pageOf the page of
	 */
	private void setActionToCall1ToReq(HttpServletRequest request, String pageOf)
	{
		String actionToCall1 = "CreateSpecimen.do?operation=add&pageOf=&menuSelected=15&virtualLocated=false";
		if (isPageOfSpecimenCPQuery(pageOf))
		{
			actionToCall1 = Constants.CP_QUERY_CREATE_SPECIMEN_ACTION + "?operation=add";
		}
		request.setAttribute("actionToCall1", actionToCall1);
	}

	/**
	 * Sets the action to call2 to req.
	 *
	 * @param request the request
	 * @param pageOf the page of
	 */
	private void setActionToCall2ToReq(HttpServletRequest request, String pageOf)
	{
		String actionToCall2 = "CreateSpecimen.do?operation=add&pageOf=&menuSelected=15&virtualLocated=false";
		if (isPageOfSpecimenCPQuery(pageOf))
		{
			actionToCall2 = Constants.CP_QUERY_CREATE_SPECIMEN_ACTION + "?pageOf="
					+ Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY
					+ "&operation=add&virtualLocated=false";
		}
		request.setAttribute("actionToCall2", actionToCall2);
	}

	/**
	 * Checks if is page of specimen cp query.
	 *
	 * @param pageOf the page of
	 *
	 * @return true, if checks if is page of specimen cp query
	 */
	private boolean isPageOfSpecimenCPQuery(String pageOf)
	{
		return pageOf != null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY);
	}

	/**
	 * Sets the page data3.
	 *
	 * @param request : request
	 * @param form : form
	 */
	private void setPageData3(HttpServletRequest request, CreateSpecimenForm form)
	{
		final boolean readOnlyForAll = ((Boolean) request.getAttribute("readOnlyForAll"))
				.booleanValue();
		final String changeAction1 = "setFormAction('MakeParticipantEditable.do?"
				+ Constants.EDITABLE + "=" + !readOnlyForAll + "')";
		request.setAttribute("changeAction1", changeAction1);
		final List specClassList = (List) request.getAttribute(Constants.SPECIMEN_CLASS_LIST);
		request.setAttribute("specClassList", specClassList);

		List specimenTypeList = (List) request.getAttribute(Constants.SPECIMEN_TYPE_LIST);
		final HashMap specimenTypeMap = (HashMap) request.getAttribute(Constants.SPECIMEN_TYPE_MAP);
		final String classValue = (String) form.getClassName();

		specimenTypeList = (List) specimenTypeMap.get(classValue);
		if (specimenTypeList == null)
		{
			specimenTypeList = new ArrayList();
			specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
		}
		request.setAttribute("specimenTypeList", specimenTypeList);
		request.setAttribute("specimenTypeMap", specimenTypeMap);

	}

	/**
	 * Sets the constant values.
	 *
	 * @param request : request
	 */
	private void setConstantValues(HttpServletRequest request)
	{
		request.setAttribute("oper", Constants.OPERATION);
		request.setAttribute("query", Constants.QUERY);
		request.setAttribute("search", Constants.SEARCH);
		request.setAttribute("view", Constants.VIEW);
		request.setAttribute("isSpecimenLabelGeneratorAvl", Variables.isSpecimenLabelGeneratorAvl);
		request.setAttribute("UNIT_MG", Constants.UNIT_MG);
		request.setAttribute("labelNames", Constants.STORAGE_CONTAINER_LABEL);
		request.setAttribute("ADD", Constants.ADD);
		request.setAttribute("isSpecimenBarcodeGeneratorAvl",
				Variables.isSpecimenBarcodeGeneratorAvl);
		request.setAttribute("SPECIMEN_BUTTON_TIPS", Constants.SPECIMEN_BUTTON_TIPS[3]);
		request.setAttribute("SPECIMEN_FORWARD_TO_LIST", Constants.SPECIMEN_FORWARD_TO_LIST[3][0]);
	}

	/**
	 * Sets the n combo data.
	 *
	 * @param request : request
	 * @param form : form
	 */
	private void setNComboData(HttpServletRequest request, CreateSpecimenForm form)
	{
		final String[] attrNames = {"storageContainer", "positionDimensionOne",
				"positionDimensionTwo"};
		final String[] tdStyleClassArray = {"formFieldSized15", "customFormField",
				"customFormField"};

		request.setAttribute("attrNames", attrNames);
		request.setAttribute("tdStyleClassArray", tdStyleClassArray);

		String[] initValues = new String[3];
		final List initValuesList = (List) request.getAttribute(INITVALUES_STRING);
		if (initValuesList != null)
		{
			initValues = (String[]) initValuesList.get(0);
		}
		request.setAttribute(INITVALUES_STRING, initValues);

		String className = (String) request.getAttribute(Constants.SPECIMEN_CLASS_NAME);
		if (className == null)
		{
			className = "";
		}

		String collectionProtocolId = (String) request
				.getAttribute(Constants.COLLECTION_PROTOCOL_ID);
		if (collectionProtocolId == null)
		{
			collectionProtocolId = "";
		}

		final String url = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;"
				+ "selectedContainerName=selectedContainerName&amp;"
				+ "pos1=pos1&amp;pos2=pos2&amp;containerId=containerId" + "&"
				+ Constants.CAN_HOLD_SPECIMEN_CLASS + "=" + className + "&"
				+ Constants.CAN_HOLD_COLLECTION_PROTOCOL + "=" + collectionProtocolId;
		final String buttonOnClicked = "mapButtonClickedOnNewSpecimen('" + url
				+ "','createSpecimen')";

		request.setAttribute("buttonOnClicked", buttonOnClicked);

		final int radioSelected = form.getStContSelection();

		String storagePosition = getStoragePosition(radioSelected);

		request.setAttribute("storagePosition", storagePosition);
		final Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
		final String jsForOutermostDataTable = ScriptGenerator.getJSForOutermostDataTable();
		final String jsEquivalentFor = ScriptGenerator.getJSEquivalentFor(dataMap, "1");

		request.setAttribute("dataMap", dataMap);
		request.setAttribute("jsEquivalentFor", jsEquivalentFor);
		request.setAttribute("jsForOutermostDataTable", jsForOutermostDataTable);
	}

	/**
	 * Gets the storage position.
	 *
	 * @param radioSelected the radio selected
	 *
	 * @return the storage position
	 */
	private String getStoragePosition(final int radioSelected)
	{
		String storagePosition = Constants.STORAGE_TYPE_POSITION_VIRTUAL;
		if (radioSelected == 1)
		{
			storagePosition = Constants.STORAGE_TYPE_POSITION_VIRTUAL;
		}
		else if (radioSelected == 2)
		{
			storagePosition = Constants.STORAGE_TYPE_POSITION_AUTO;
		}
		else if (radioSelected == 3)
		{
			storagePosition = Constants.STORAGE_TYPE_POSITION_MANUAL;
		}
		return storagePosition;
	}

	/**
	 * Sets the xter id data.
	 *
	 * @param request : request
	 */
	private void setXterIdData(HttpServletRequest request)
	{
		final String eiDispType1 = request.getParameter("eiDispType");
		request.setAttribute("eiDispType1", eiDispType1);

		String delExtIds = "deleteExternalIdentifiers('pageOfMultipleSpecimen')";
		if ((String) request.getAttribute(Constants.PAGE_OF) != null)
		{
			delExtIds = "deleteExternalIdentifiers('"
					+ (String) request.getAttribute(Constants.PAGE_OF) + "');";
		}
		request.setAttribute("delExtIds", delExtIds);
	}

	/**
	 * @param form
	 *            : form
	 * @return String : String
	 */

	/*protected String getObjectId(AbstractActionForm form)
	{
		final CreateSpecimenForm createSpecimenForm = (CreateSpecimenForm) form;
		SpecimenCollectionGroup specimenCollectionGroup = null;
		if (createSpecimenForm.getParentSpecimenId() != null
				&& createSpecimenForm.getParentSpecimenId() != "")
		{
			final Specimen specimen = AppUtility.getSpecimen(createSpecimenForm
					.getParentSpecimenId());
			specimenCollectionGroup = specimen.getSpecimenCollectionGroup();
			final CollectionProtocolRegistration cpr = specimenCollectionGroup
					.getCollectionProtocolRegistration();
			if (cpr != null)
			{
				final CollectionProtocol cp = cpr.getCollectionProtocol();
				return Constants.COLLECTION_PROTOCOL_CLASS_NAME + "_" + cp.getId();
			}
		}
		return null;
	}*/
}