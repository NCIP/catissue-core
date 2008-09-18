/**
 * <p>Title: AliquotAction Class>
 * <p>Description:	AliquotAction initializes all the fields of the page, Aliquots.jsp.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on May 12, 2006
 */

package edu.wustl.catissuecore.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bean.AliquotBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.tag.ScriptGenerator;

/**
 * AliquotAction initializes all the fields of the page, Aliquots.jsp.
 * @author aniruddha_phadnis
 */
public class AliquotAction extends SecureAction
{
	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		//-- code for handling method calls
		if (request.getParameter(Constants.CP_QUERY) != null)
		{
			request.setAttribute(Constants.CP_QUERY, "CPQuery");
		}
		String methodName = request.getParameter(Constants.METHOD_NAME);
		if (methodName == null)
		{
			methodName = "executeAliquotAction";
		}
		
		List<NameValueBean> storagePositionList =  Utility.getStoragePositionTypeList();
		request.setAttribute("storageList", storagePositionList);
         /**
          * Patch ID: 3835_1_6
          * See also: 1_1 to 1_5
          * Description : set current date to aliquot
          */ 
         if(((AliquotForm)form).getCreatedDate() == null)
         {
             ((AliquotForm)form).setCreatedDate(Utility.parseDateToString(Calendar.getInstance().getTime(), Constants.DATE_PATTERN_MM_DD_YYYY));
         }      
		return invokeMethod(methodName, mapping, form, request, response);

	}

	/**
	 * Overrides the execute method of Action class.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 */
	public ActionForward executeContainerChange(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		AliquotForm aliquotForm = (AliquotForm) form;
		//boolean to indicate whether the suitable containers to be shown in dropdown 
		//is exceeding the max limit.
		String exceedingMaxLimit = "false";

		//Getting the value pageOf parameter.

		String pageOf = request.getParameter(Constants.PAGEOF);
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

		//Map containerMap = bizLogic.getAllocatedContainerMap();
		TreeMap containerMap = new TreeMap();
		int aliquotCount = 0;
		if (aliquotForm.isAliqoutInSameContainer())
		{
			aliquotCount = Integer.parseInt(aliquotForm.getNoOfAliquots());
		}
		SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		containerMap = bizLogic.getAllocatedContaienrMapForSpecimen(aliquotForm.getSpCollectionGroupId(), aliquotForm.getClassName(),
				aliquotCount, exceedingMaxLimit, sessionData, true);

		populateStorageLocationsOnContainerChange(aliquotForm, containerMap, request);

		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		request.setAttribute(Constants.PAGEOF, pageOf);
		setParentSpecimenInRequest(request);
		setPageData(request, pageOf,aliquotForm);
		return mapping.findForward(pageOf);
	}

	/**
	 * @param aliquotForm object of AliquotForm
	 * @param containerMap object of TreeMap for container data
	 * @param request object of HttpServletRequest
	 */
	private void populateStorageLocationsOnContainerChange(AliquotForm aliquotForm, TreeMap containerMap, HttpServletRequest request)
	{
		Map aliquotMap = aliquotForm.getAliquotMap();
		String aliquotCount = aliquotForm.getNoOfAliquots();
		int counter = Integer.parseInt(request.getParameter("rowNo"));
		String containerKey = "Specimen:" + counter + "_StorageContainer_id";
		String containerName = (String) aliquotMap.get(containerKey);
		boolean flag = true;
		if (containerName.equals("-1"))
		{
			return;
		}
		if (!containerMap.isEmpty())
		{
			Object[] containerId = containerMap.keySet().toArray();
			for (int i = 0; i < containerId.length; i++)
			{
				NameValueBean containerNvb = (NameValueBean) containerId[i];
				if (flag && containerNvb.getValue().toString().equals(containerName))
				{
					flag = false;
				}
				if (flag)
				{
					continue;
				}
				Map xDimMap = (Map) containerMap.get(containerId[i]);
				counter = StorageContainerUtil.setAliquotMap("Specimen",xDimMap, containerId, aliquotCount, counter, aliquotMap, i);
				if (counter <= Integer.parseInt(aliquotCount))
				{
					i=containerId.length;
				}
			}
		}
		aliquotForm.setAliquotMap(aliquotMap);
		if (counter < Integer.parseInt(aliquotCount))
		{
			ActionErrors errors = getActionErrors(request);

			if (errors == null)
			{
				errors = new ActionErrors();
			}
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.locations.notSufficientForAliquotDropdown"));
			saveErrors(request, errors);
		}

	}

	/**
	 * Overrides the execute method of Action class.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 */
	public ActionForward executeAliquotAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		AliquotForm aliquotForm = (AliquotForm) form;
		//boolean to indicate whether the suitable containers to be shown in dropdown 
		//is exceeding the max limit.
		String exceedingMaxLimit = "false";
		String specimenLabel = aliquotForm.getSpecimenLabel();
		request.setAttribute("createdDate", aliquotForm.getCreatedDate());
		//For Storing Label/Barcode, Aliquot Count, Quantity per Aliquot
		Map tempAliquotMap = new HashMap();
		//Extracting the values of the operation & pageOf parameters.
		String operation = request.getParameter(Constants.OPERATION);
		String pageOf = request.getParameter(Constants.PAGEOF);
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		/**
		 *  Following code ensures that 
		 *  1. Label/Barcode, Aliquot Count, Quantity per Aliquot submitted on click of Submit button 
		 *  and that on click of create button is same. In case they are different show the error 
		 *  'You have to resubmit the page in case you edit any of Parent Specimen Label/Barcode, Aliquot Count, Quantity per Aliquot.'
		 *  2. If checkbox 'Create all Aliquots in the Same Container' is checked(true), populate 
		 *  Aliquot form with only those containers which has sufficient empty space to hold all the aliquots.
		 *  3. If checkbox 'Create all Aliquots in the Same Container' is checked(true), on click of create, 
		 *  check whether all the specimens are in the same container. If they are not in the same container,
		 *  show error 'Create failed because you can not choose different containers to store the Aliquots' 
		 */
		// --------- Start Bug 2309--------------
		/**
		 * Store the values of label/barcode,aliquot count, quantity per aliquot on click of submit 
		 */
		if (aliquotForm.getButtonClicked().equalsIgnoreCase("submit"))
		{
			tempAliquotMap.put("aliquotcount", aliquotForm.getNoOfAliquots());
			tempAliquotMap.put("quantityperaliquot", aliquotForm.getQuantityPerAliquot());
			aliquotForm.setAliqoutInSameContainer(false);
			request.getSession().setAttribute("tempAliquotMap", tempAliquotMap);
		}
		else if (aliquotForm.getButtonClicked().equalsIgnoreCase("create") || aliquotForm.getButtonClicked().equalsIgnoreCase("checkbox"))
		{
			// arePropertiesChanged is used to identify if any of  label/barcode,aliquot count, quantity per aliquot are changed
			boolean arePropertiesChanged = false;
			tempAliquotMap = (HashMap) request.getSession().getAttribute("tempAliquotMap");
			String label = (String) tempAliquotMap.get("label");
			String barcode = (String) tempAliquotMap.get("barcode");
			if (aliquotForm.getCheckedButton().equals("1"))
			{
				if (label == null || !label.trim().equalsIgnoreCase(aliquotForm.getSpecimenLabel().trim()))
				{
					arePropertiesChanged = true;
				}
			}
			else
			{
				if (barcode == null || !barcode.trim().equalsIgnoreCase(aliquotForm.getBarcode().trim()))
				{
					arePropertiesChanged = true;
				}

			}

			String aliquotcount = (String) tempAliquotMap.get("aliquotcount");
			if (!aliquotcount.trim().equalsIgnoreCase(aliquotForm.getNoOfAliquots().trim()))
			{
				arePropertiesChanged = true;
			}

			String quantityperaliquot = (String) tempAliquotMap.get("quantityperaliquot");
			if (!quantityperaliquot.trim().equalsIgnoreCase(aliquotForm.getQuantityPerAliquot().trim()))
			{
				arePropertiesChanged = true;
			}

			/** 
			 * areContainersDifferent checks whether user has selected diffrent containers when 
			 * 'Store all Containers aliquots in same container' is true
			 */
			boolean areContainersDifferent = false;
			boolean wrongStorageContainerName = false;

			if (aliquotForm.isAliqoutInSameContainer() && aliquotForm.getButtonClicked().equalsIgnoreCase("create"))
			{
				Map aliquotMap = aliquotForm.getAliquotMap();
				String specimenKey = "Specimen:";
				int noOfAliquots = Integer.parseInt(aliquotForm.getNoOfAliquots().trim());
				String tempContainerId = null;

				for (int i = 1; i <= noOfAliquots; i++)
				{
					String radioButonKey = "radio_" + i;
					String containerIdKey = specimenKey + i + "_StorageContainer_id";
					String containerNameKey = specimenKey + i + "_StorageContainer_name";
					String containerId = null;
					if (aliquotMap.get(radioButonKey)!=null && aliquotMap.get(radioButonKey).equals("2"))
					{
						containerId = (String) aliquotMap.get(containerIdKey);
					}
					else if (aliquotMap.get(radioButonKey)!=null && aliquotMap.get(radioButonKey).equals("3"))
					{
						String containerName = (String) aliquotMap.get(containerNameKey + "_fromMap");

						String sourceObjectName = StorageContainer.class.getName();
						String[] selectColumnName = {"id"};
						String[] whereColumnName = {"name"};
						String[] whereColumnCondition = {"="};
						Object[] whereColumnValue = {containerName};
						String joinCondition = null;
						IBizLogic defaultBizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
						List list = defaultBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName, whereColumnCondition,
								whereColumnValue, joinCondition);

						if (!list.isEmpty())
						{
							containerId = list.get(0).toString();
						}
						else
						{
							wrongStorageContainerName = true;
							ActionErrors errors = getActionErrors(request);
							if (errors == null)
							{
								errors = new ActionErrors();
							}
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.specimen.storageContainerEditBox"));
							saveErrors(request, errors);
						}

					}

					if (i == 1)
					{
						tempContainerId = containerId;
					}
					else
					{
						// check whether all container IDs are same
						if (containerId == null || !containerId.equals(tempContainerId))
						{
							areContainersDifferent = true;
						}
					}

				}
			}

			/**
			 *  Repopulate the form with available storage container locations in case
			 *  1. User has changed any of label/barcode, aliquot count, quantity per aliquote
			 *  2. User has selected different containers to store the aliquotes when 'Store all aliquots in same container..'
			 *     checkbox is checked(true)
			 *  3. User has changed state of checkbox from checked to unchecked or vice versa    
			 */

			if (arePropertiesChanged == true || areContainersDifferent == true || aliquotForm.getButtonClicked().equalsIgnoreCase("checkbox"))
			{

				aliquotForm.setNoOfAliquots(aliquotcount);
				aliquotForm.setSpecimenLabel(label);
				aliquotForm.setBarcode(barcode);
				aliquotForm.setQuantityPerAliquot(quantityperaliquot);
				TreeMap containerMap = null;

				if (aliquotForm.isAliqoutInSameContainer())
				{

					containerMap = bizLogic.getAllocatedContaienrMapForSpecimen(aliquotForm.getSpCollectionGroupId(), aliquotForm.getClassName(),
							Integer.parseInt(aliquotForm.getNoOfAliquots()), exceedingMaxLimit, sessionData, true);
				}
				else
				{
					containerMap = bizLogic.getAllocatedContaienrMapForSpecimen(aliquotForm.getSpCollectionGroupId(), aliquotForm.getClassName(),
							0, exceedingMaxLimit, sessionData, true);
				}
				populateAliquotsStorageLocations(aliquotForm, containerMap);

				ActionErrors errors = getActionErrors(request);

				if (errors == null)
				{   
					errors = new ActionErrors();
				}	
				
				if (arePropertiesChanged == true)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.aliquots.reSubmit"));
				}
				else if (areContainersDifferent == true && !wrongStorageContainerName)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.aliquots.sameStorageContainer"));
				}
				else if (containerMap.size() == 0)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.locations.notSufficientForAliquot"));
				}
				
				saveErrors(request, errors);
				request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
				request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
				request.setAttribute(Constants.PAGEOF, Constants.PAGEOF_CREATE_ALIQUOT);
				setPageData(request, pageOf,aliquotForm);
				return mapping.findForward(Constants.PAGEOF_CREATE_ALIQUOT);

			}

			/**
			 *  Forward to addEditAction in following cases
			 *  1. 'Store all aliquots in same container..' Checkbox is checked and user has selected the same container
			 *  2. 'Store all aliquots in same container..' Checkbox is unchecked
			 */
			if (areContainersDifferent == false)
			{
				Map aliquotMap = aliquotForm.getAliquotMap();
				Iterator keyIterator = aliquotMap.keySet().iterator();
				/**
				 * 	 Bug no. 560 If total quantity entered by user is greater than initial available quantity,
				 *   show the 'System can not distribute available quantity among the aliquots due to insufficient amount' error message  
				 * 
				 */

				double totalQuantity = 0;
				ActionErrors errors = getActionErrors(request);

				if (errors == null)
				{
					errors = new ActionErrors();
				}

				while (keyIterator.hasNext())
				{
					Validator validator = new Validator();
					String key = (String) keyIterator.next();
					if (key.endsWith("_quantity"))
					{
						String value = (String) aliquotMap.get(key);
						if (!validator.isDouble(value))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
									.getValue("specimen.quantity")));
						}
						else
							totalQuantity += Double.parseDouble(value);
					}
				}
				//Resolved bug# 4314 Virender
				DecimalFormat dFormat = new DecimalFormat("#.000");
				totalQuantity = Double.parseDouble(dFormat.format(totalQuantity));
				
				if (totalQuantity > Double.parseDouble(aliquotForm.getInitialAvailableQuantity()))
				{
					errors = getActionErrors(request);
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.qtyInsufficient"));
					saveErrors(request, errors);
					TreeMap containerMap = null;
					if (aliquotForm.isAliqoutInSameContainer())
					{
						containerMap = bizLogic.getAllocatedContaienrMapForSpecimen(aliquotForm.getSpCollectionGroupId(), aliquotForm
								.getClassName(), Integer.parseInt(aliquotForm.getNoOfAliquots()), exceedingMaxLimit, sessionData, true);
					}
					else
					{
						containerMap = bizLogic.getAllocatedContaienrMapForSpecimen(aliquotForm.getSpCollectionGroupId(), aliquotForm
								.getClassName(), 0, exceedingMaxLimit, sessionData, true);
					}

					populateAliquotsStorageLocations(aliquotForm, containerMap);
					request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
					request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
					request.setAttribute(Constants.PAGEOF, Constants.PAGEOF_CREATE_ALIQUOT);
					setPageData(request, pageOf,aliquotForm);
					return mapping.findForward(Constants.PAGEOF_CREATE_ALIQUOT);
				}
				aliquotForm.setButtonClicked("none");
				setPageData(request, pageOf,aliquotForm);
				return mapping.findForward(Constants.COMMON_ADD_EDIT);
			}

		}
		// --------END Bug 2309----------		

		if (Constants.PAGEOF_ALIQUOT_SUMMARY.equals(pageOf))
		{
			Map map = (Map) request.getAttribute("forwardToHashMap");		

			if (map != null)
			{
				aliquotForm.setClassName(Utility.toString(map.get(Constants.CDE_NAME_SPECIMEN_CLASS)));
				aliquotForm.setType(Utility.toString(map.get(Constants.CDE_NAME_SPECIMEN_TYPE)));
				aliquotForm.setTissueSite(Utility.toString(map.get(Constants.CDE_NAME_TISSUE_SITE)));
				aliquotForm.setTissueSide(Utility.toString(map.get(Constants.CDE_NAME_TISSUE_SIDE)));
				aliquotForm.setPathologicalStatus(Utility.toString(map.get(Constants.CDE_NAME_PATHOLOGICAL_STATUS)));
				aliquotForm.setAvailableQuantity(Utility.toString(map.get(Constants.SPECIMEN_TYPE_QUANTITY)));
				aliquotForm.setConcentration(Utility.toString(map.get("concentration")));

				aliquotForm.setAliquotMap(map);
			}

			ActionErrors errors = getActionErrors(request);

			if (errors == null || errors.size() == 0)
			{
				ActionMessages messages = new ActionMessages();
				messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("aliquots.success"));
				saveMessages(request, messages);
			}

			setParentSpecimenInRequest(request);
			setPageData(request, pageOf,aliquotForm);
			return mapping.findForward(pageOf);

		}

		// this code is required when we come from Specimen page
		if (specimenLabel == null || specimenLabel.trim().equals(""))
		{
			Map map = (Map) request.getAttribute("forwardToHashMap");
			if(map != null)
			{
				specimenLabel = Utility.toString(map.get(Constants.SPECIMEN_LABEL));
			}
		}
		// this code is used in case we come from Specimen page
		if (specimenLabel != null)
		{
			aliquotForm.setSpecimenLabel(specimenLabel);
			//by falguni-When user selects parent specimen barcode and submit.
			if(!aliquotForm.getCheckedButton().equals("2"))
			aliquotForm.setCheckedButton("1");
			//resolvede bug #4236 Virender Mehta
			if (aliquotForm.getCheckedButton().equals("1"))
			{
				tempAliquotMap.put("label", aliquotForm.getSpecimenLabel());
				tempAliquotMap.put("barcode", aliquotForm.getBarcode());
			}
			else
			{
				tempAliquotMap.put("barcode", aliquotForm.getBarcode());
			}
		}

		//Map containerMap = bizLogic.getAllocatedContainerMap();
		TreeMap containerMap = new TreeMap();
		if (Constants.PAGEOF_CREATE_ALIQUOT.equals(request.getParameter(Constants.PAGEOF)))
		{
			pageOf = validate(request, aliquotForm);

			if (Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
			{
				if (!aliquotForm.getButtonClicked().equals("none"))
				{
					pageOf = checkForSpecimen(request, aliquotForm);
				}
				if (Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
				{
					int aliquotCount = Integer.parseInt(aliquotForm.getNoOfAliquots());
					if (aliquotForm.isAliqoutInSameContainer())
					{

						containerMap = bizLogic.getAllocatedContaienrMapForSpecimen(aliquotForm.getSpCollectionGroupId(), aliquotForm
								.getClassName(), Integer.parseInt(aliquotForm.getNoOfAliquots()), exceedingMaxLimit, sessionData, true);
					}
					else
					{
						containerMap = bizLogic.getAllocatedContaienrMapForSpecimen(aliquotForm.getSpCollectionGroupId(), aliquotForm
								.getClassName(), 0, exceedingMaxLimit, sessionData, true);
					}
					pageOf = checkForSufficientAvailablePositions(request, containerMap, aliquotCount);

					if (Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
					{
						if (!aliquotForm.getButtonClicked().equals("none"))
						{
							populateAliquotsStorageLocations(aliquotForm, containerMap);
						}
					}
				}
			}
		}
		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		request.setAttribute(Constants.PAGEOF, pageOf);

		/*  String [] displayNameField = {Constants.SYSTEM_IDENTIFIER};
		 List specimenIdList = bizLogic.getList(Specimen.class.getName(), displayNameField, Constants.SYSTEM_IDENTIFIER, true);
		 request.setAttribute(Constants.SPECIMEN_ID_LIST,specimenIdList); */
		setParentSpecimenInRequest(request);
		setPageData(request, pageOf,aliquotForm);
		return mapping.findForward(pageOf);
	}

	/**
	 * This method checks whether the specimen with given label exists or not.
	 * @param request object of HttpServletRequest
	 * @param form object of AliquotForm
	 * @return String containing value for PAGEOF 
	 * @throws Exception generic exception
	 */
	private String checkForSpecimen(HttpServletRequest request, AliquotForm form) throws Exception
	{
		String pageOf = request.getParameter(Constants.PAGEOF);
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		String specimenLabel = form.getSpecimenLabel();
		List specimenList = new ArrayList();
		String errorString = "";

		if (form.getCheckedButton().equals("1"))
		{
			specimenList = bizLogic.retrieve(Specimen.class.getName(), Constants.SYSTEM_LABEL, specimenLabel);
			errorString = Constants.SYSTEM_LABEL;
		}
		else
		{
			String barcode = form.getBarcode().trim();
			specimenList = bizLogic.retrieve(Specimen.class.getName(), "barcode", barcode);
			errorString = "barcode";
		}

		if (specimenList == null || specimenList.isEmpty())
		{
			ActionErrors errors = getActionErrors(request);

			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("aliquots.specimen.notExists", errorString));
			saveErrors(request, errors);

			return Constants.PAGEOF_ALIQUOT;
		}
		
		else
		{
			Specimen specimen = (Specimen) specimenList.get(0);
			
			if(specimen.getActivityStatus().equals(Constants.ACTIVITY_STATUS_CLOSED))
			{
				/**
	 			* Name : Virender Mehta
	 			* Reviewer Name : Sachin Lale 
	 			* Bug ID: 4040
	 			* Description: Added new error message and check for pageOf flow, if user clicks directly aliquot 
	 			* 			   link and specimen status is closed then validation is done at Aliquot.jsp else validation message will be shown 
	 			* 			   is directed to NewSpecimen.jsp page		
				*/
				ActionErrors errors = getActionErrors(request);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.parentobject.closed" , "Specimen"));
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aliquots" , "Aliquot(s)"));
				saveErrors(request, errors);
				if(pageOf.equalsIgnoreCase(Constants.PAGEOF_ALIQUOT))
				{
					return Constants.PAGEOF_ALIQUOT;
				}
				else if(pageOf.equalsIgnoreCase(Constants.PAGEOF_CREATE_ALIQUOT))
				{
					return Constants.PAGEOF_CREATE_ALIQUOT;
				}
				else
				{
					return Constants.PAGE_OF_SPECIMEN;
				}
			}
			else if(specimen.getActivityStatus().equals(Constants.ACTIVITY_STATUS_DISABLED))
			{
				/**
	 			* Name : Falguni Sachde
	 			* Reviewer Name : Sachin Lale 
	 			* Bug ID: 4919
	 			* Description: Added new error message and check for pageOf flow, if user clicks directly aliquot 
	 			* 			   link and specimen status is disabled 	
				*/
				ActionErrors errors = getActionErrors(request);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.parentobject.disabled" , "Specimen"));
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.aliquots" , "Aliquot(s)"));
				saveErrors(request, errors);
				if(pageOf.equalsIgnoreCase(Constants.PAGEOF_CREATE_ALIQUOT))
				{
					return Constants.PAGEOF_CREATE_ALIQUOT;
				}
				else
				{
					return Constants.PAGE_OF_SPECIMEN;
				}
			}
			/**
			 * set the value of aliquotInSameContainer which was set while creation of collection protocol
			 * required for Bug 2309
			 */
			
			/**
			 * Name: Virender Mehta
			 * Reviewer name: Aarti Sharma
			 * Description: Resolved Performance Issue 
			 * For retrive specimen.getSpecimenCollectionGroup().getCollectionProtocolEvent().getCollectionProtocol().getAliqoutInSameContainer();
			 *  fired hql.
			 */
			Boolean aliquotInSameContainer = null;
			aliquotInSameContainer =(Boolean)bizLogic.retrieveAttribute(Specimen.class.getName(),specimen.getId(),"specimenCollectionGroup.collectionProtocolEvent.collectionProtocol.aliquotInSameContainer");
			if (aliquotInSameContainer != null)
			{
				form.setAliqoutInSameContainer(aliquotInSameContainer.booleanValue());
			}
			populateParentSpecimenData(form, specimen, bizLogic);
			
			form.setSpecimenID("" + specimen.getId());
			
			pageOf = checkQuantityPerAliquot(request, form);
			
			if (Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
			{
				boolean isDouble = Utility.isQuantityDouble(form.getClassName(), form.getType());

				if (!distributeAvailableQuantity(form, isDouble))
				{
					ActionErrors errors = getActionErrors(request);

					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.qtyInsufficient"));
					saveErrors(request, errors);

					return Constants.PAGEOF_ALIQUOT;
				}
				else
				{
					return Constants.PAGEOF_CREATE_ALIQUOT;
				}
			}
			else
			{
				return Constants.PAGEOF_ALIQUOT;
			}
		}
	}

	/**
	 * This method checks whether there are sufficient storage locations are available or not.
	 * @param request object of HttpServletRequest
	 * @param containerMap Map containing data for contaner
	 * @param aliquotCount aliquot count in int
	 * @return String containing value for PAGEOF 
	 */
	private String checkForSufficientAvailablePositions(HttpServletRequest request, Map containerMap, int aliquotCount)
	{
		int counter = 0;
		if (containerMap.isEmpty())
		{
			ActionErrors errors = getActionErrors(request);

			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.locations.notSufficient"));
			saveErrors(request, errors);

			return Constants.PAGEOF_ALIQUOT;
		}
		else
		{
			counter = StorageContainerUtil.checkForLocation(containerMap, aliquotCount, counter);
		}
		if (counter >= aliquotCount)
		{
			return Constants.PAGEOF_CREATE_ALIQUOT;
		}
		else
		{
			ActionErrors errors = getActionErrors(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.locations.notSufficient"));
			saveErrors(request, errors);
			return Constants.PAGEOF_ALIQUOT;
		}
	}

	/**
	 * This method populates parent specimen's data in formbean
	 * @param form object of AliquotForm
	 * @param specimen object of Specimen
	 * @param bizLogic object of class implemeting IBizLogic
	 * @throws DAOException DAO exception
	 * @throws ClassNotFoundException 
	 */
	private void populateParentSpecimenData(AliquotForm form, Specimen specimen, IBizLogic bizLogic) throws DAOException, ClassNotFoundException
	{
		//SpecimenCharacteristics chars= null;
		Long cpID=null;
		form.setClassName(specimen.getClassName());
		form.setType(specimen.getSpecimenType());
		SpecimenCharacteristics chars = specimen.getSpecimenCharacteristics();
		form.setTissueSite(chars.getTissueSite());
		form.setTissueSide(chars.getTissueSide());
		form.setPathologicalStatus(specimen.getPathologicalStatus());
		form.setInitialAvailableQuantity(getAvailableQuantity(specimen));
		form.setAvailableQuantity(getAvailableQuantity(specimen));
		/**
		 * Name: Virender Mehta
		 * Reviewer name: Aarti Sharma
		 * Description: Resolved Performance Issue 
		 * For retrive specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getId(),
		 * fired hql.
		 */
		String colProtHql = "select scg.collectionProtocolRegistration.collectionProtocol"+
		" from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg," +
		" edu.wustl.catissuecore.domain.Specimen as spec " +
		" where spec.specimenCollectionGroup.id=scg.id and spec.id="+specimen.getId();

		List collectionProtocolList = Utility.executeQuery(colProtHql);
		Object obj = (Object) collectionProtocolList.get(0);
		if(obj!=null)
		{
			CollectionProtocol collectionProtocol = (CollectionProtocol) obj;
			cpID = collectionProtocol.getId();
		}
		//cpID = (Long)bizLogic.retrieveAttribute(Specimen.class.getName(), specimen.getId(), "specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.id" );
		form.setSpCollectionGroupId(cpID);
		if (specimen instanceof MolecularSpecimen)
		{
			String concentration = Utility.toString(((MolecularSpecimen) specimen).getConcentrationInMicrogramPerMicroliter());
			form.setConcentration(concentration);
		}
	}

	/**
	 * This method returns the available quantity as per the specimen type
	 * @param specimen object of Specimen
	 * @return String containing value for availableQuantity
	 */
	private String getAvailableQuantity(Specimen specimen)
	{
		/* Aniruddha : 17/06/2006 TO BE DELETED LATER
		 String availableQuantity = "";
		 
		 if(specimen instanceof CellSpecimen)
		 {
		 availableQuantity = String.valueOf(((CellSpecimen)specimen).getAvailableQuantityInCellCount());
		 }
		 else if(specimen instanceof FluidSpecimen)
		 {
		 availableQuantity = String.valueOf(((FluidSpecimen)specimen).getAvailableQuantityInMilliliter());
		 }
		 else if(specimen instanceof MolecularSpecimen)
		 {
		 availableQuantity = String.valueOf(((MolecularSpecimen)specimen).getAvailableQuantityInMicrogram());
		 }
		 else if(specimen instanceof TissueSpecimen)
		 {
		 availableQuantity = String.valueOf(((TissueSpecimen)specimen).getAvailableQuantityInGram());
		 }
		 
		 return availableQuantity;
		 */
		return specimen.getAvailableQuantity().toString();
	}

	/**
	 * This method distributes the available quantity among the aliquots. On successful distribution the function return true else false.
	 * @param form object of AliquotForm
	 * @param isDouble boolean for isDouble
	 * @return boolean value for success
	 * @throws Exception generic exception
	 */
	private boolean distributeAvailableQuantity(AliquotForm form, boolean isDouble) throws Exception
	{
		int aliquotCount = Integer.parseInt(form.getNoOfAliquots());
		String distributedQuantity;

		if (isDouble)
		{
			double availableQuantity = Double.parseDouble(form.getAvailableQuantity());
			// Bug no 560
			if (availableQuantity < 0)
			{
				return false;
			}
			double dQuantity;
			DecimalFormat dFormat = new DecimalFormat("#.00");

			if (form.getQuantityPerAliquot() == null || form.getQuantityPerAliquot().trim().length() == 0)
			{
				//Resolved bug# 4403 
//				dQuantity = availableQuantity / aliquotCount;
//				dQuantity = Double.parseDouble(dFormat.format(dQuantity));
				BigDecimal bgAvailTemp = new BigDecimal(availableQuantity);
				BigDecimal bgCntTemp = new BigDecimal(aliquotCount);
				BigDecimal bgAvail = bgAvailTemp.setScale(2,BigDecimal.ROUND_HALF_EVEN);
				BigDecimal bgCnt  = bgCntTemp.setScale(2,BigDecimal.ROUND_HALF_EVEN);
				BigDecimal bgQuantity = bgAvail.divide(bgCnt,2,BigDecimal.ROUND_FLOOR); 
				dQuantity = bgQuantity.doubleValue();
				/**
				 *  Fixed bug 3656
				 */
				//availableQuantity = 0;
				availableQuantity = availableQuantity - Double.parseDouble(dFormat.format((dQuantity * aliquotCount)));
				availableQuantity = Double.parseDouble(dFormat.format(availableQuantity));
				
			}
			else
			{
				dQuantity = Double.parseDouble(form.getQuantityPerAliquot());
				availableQuantity = availableQuantity - Double.parseDouble(dFormat.format((dQuantity * aliquotCount)));
			}

			distributedQuantity = String.valueOf(dQuantity);
			
	    	if (availableQuantity < 0)    
			{
				return false;
			}

			form.setAvailableQuantity(String.valueOf(availableQuantity));
		}
		else
		{
			int availableQuantity = (int) Double.parseDouble(form.getAvailableQuantity());
			// Bug no 560
			if (availableQuantity < 0)
			{
				return false;
			}
			int iQauntity = (int) (availableQuantity / aliquotCount);

			if (form.getQuantityPerAliquot() == null || form.getQuantityPerAliquot().trim().length() == 0)
			{
				iQauntity = (int) (availableQuantity / aliquotCount);
			}
			else
			{
				iQauntity = (int) Double.parseDouble(form.getQuantityPerAliquot());
			}

			distributedQuantity = String.valueOf(iQauntity);
			availableQuantity = availableQuantity - (iQauntity * aliquotCount);

			if (availableQuantity < 0)
			{
				return false;
			}

			form.setAvailableQuantity(String.valueOf(availableQuantity));
		}

		Map aliquotMap = form.getAliquotMap();
		/**
    	 * Name : Virender Mehta
         * Reviewer: Sachin Lale
         * Description: By getting instance of AbstractSpecimenGenerator abstract class current label retrived and set. 
    	 */
//		Commented by Falguni -As now label generation is done in Biz logic
		/*SpecimenLabelGenerator abstractSpecimenGenerator  = SpecimenLabelGeneratorFactory.getInstance();
		
		HashMap inputMap = new HashMap();
		inputMap.put(Constants.PARENT_SPECIMEN_LABEL_KEY, form.getSpecimenLabel());
		inputMap.put(Constants.PARENT_SPECIMEN_ID_KEY, form.getSpecimenID());
		
		List<String> aliquotLabels = abstractSpecimenGenerator.getNextAvailableAliquotSpecimenlabel(inputMap, aliquotCount);
		*/
		long totalAliquotCount = 0;
		if(!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl && form.getSpecimenLabel() != null)
		{
			totalAliquotCount = getTotalNoOfAliquotSpecimen(Long.valueOf(form.getSpecimenID()));
		}	
		for (int i = 1; i <= aliquotCount; i++)
		{
			String qtyKey = "Specimen:" + i + "_quantity";
			aliquotMap.put(qtyKey, distributedQuantity);
			//bug no. 8081 & 8083
			if(!edu.wustl.catissuecore.util.global.Variables.isSpecimenLabelGeneratorAvl && form.getSpecimenLabel() != null)
			{
					String labelKey = "Specimen:" + i + "_label";
//			aliquotMap.put(labelKey,"Abc");
					aliquotMap.put(labelKey, form.getSpecimenLabel()+"_"+ ++totalAliquotCount);
			}
			
		}

		form.setAliquotMap(aliquotMap);
		return true;
	}

	/**
	 * This function populates the availability map with available storage locations
	 * @param form object of AliquotForm
	 * @param containerMap Map containing data for container
	 */
	private void populateAliquotsStorageLocations(AliquotForm form, Map containerMap)
	{
		Map aliquotMap = form.getAliquotMap();
		int counter = 1;
		if (!containerMap.isEmpty())
		{
			Object[] containerId = containerMap.keySet().toArray();
			for (int i = 0; i < containerId.length; i++)
			{
				Map xDimMap = (Map) containerMap.get(containerId[i]);
				if (!xDimMap.isEmpty())
				{
					Object[] xDim = xDimMap.keySet().toArray();
					for (int j = 0; j < xDim.length; j++)
					{
						List yDimList = (List) xDimMap.get(xDim[j]);
						for (int k = 0; k < yDimList.size(); k++)
						{
							if (counter <= Integer.parseInt(form.getNoOfAliquots()))
							{
								String containerKey = "Specimen:" + counter + "_StorageContainer_id";
								String pos1Key = "Specimen:" + counter + "_positionDimensionOne";
								String pos2Key = "Specimen:" + counter + "_positionDimensionTwo";

								aliquotMap.put(containerKey, ((NameValueBean) containerId[i]).getValue());
								aliquotMap.put(pos1Key, ((NameValueBean) xDim[j]).getValue());
								aliquotMap.put(pos2Key, ((NameValueBean) yDimList.get(k)).getValue());

								counter++;
							}
							else
							{
								j = xDim.length;
								i = containerId.length;
								break;
							}
						}
					}
				}
			}
		}
		form.setAliquotMap(aliquotMap);
	}

	/**
	 * This function checks the quantity per aliquot is valid or not
	 * @param request object of HttpServletRequest
	 * @param form object of AliquotForm
	 * @return String containing value for pageOf
	 * @throws Exception generic exception
	 */
	private String checkQuantityPerAliquot(HttpServletRequest request, AliquotForm form) throws Exception
	{
		Validator validator = new Validator();
		String quantityPerAliquot = form.getQuantityPerAliquot();
		ActionErrors errors = getActionErrors(request);

		if (quantityPerAliquot != null && quantityPerAliquot.trim().length() != 0)
		{
			try
			{
				quantityPerAliquot = new BigDecimal(quantityPerAliquot).toPlainString();
				if (Utility.isQuantityDouble(form.getClassName(), form.getType()))
				{
					if (!validator.isDouble(quantityPerAliquot.trim()))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
								.getValue("aliquots.qtyPerAliquot")));
						saveErrors(request, errors);
						return Constants.PAGEOF_ALIQUOT;
					}
				}
				else
				{
					if (!validator.isNumeric(quantityPerAliquot.trim()))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties
								.getValue("aliquots.qtyPerAliquot")));
						saveErrors(request, errors);
						return Constants.PAGEOF_ALIQUOT;
					}
				}
			}
			catch (NumberFormatException exp)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties.getValue("specimen.quantity")));
				saveErrors(request, errors);
				return Constants.PAGEOF_ALIQUOT;
			}
		}

		return Constants.PAGEOF_CREATE_ALIQUOT;
	}

	/**
	 * This method validates the formbean.
	 * @param request object of HttpServletRequest
	 * @param form object of AliquotForm
	 * @return String containing value for pageOf
	 */
	private String validate(HttpServletRequest request, AliquotForm form)
	{
		Validator validator = new Validator();
		ActionErrors errors = getActionErrors(request);

		String pageOf = Constants.PAGEOF_CREATE_ALIQUOT;

		if (form.getCheckedButton().equals("1"))
		{
			if (validator.isEmpty(form.getSpecimenLabel()))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties
						.getValue("createSpecimen.parentLabel")));
				pageOf = Constants.PAGEOF_ALIQUOT;
			}
		}
		else
		{
			if (form.getBarcode() == null || form.getBarcode().trim().length() == 0)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", ApplicationProperties.getValue("specimen.barcode")));
				pageOf = Constants.PAGEOF_ALIQUOT;
			}
		}

		if (!validator.isNumeric(form.getNoOfAliquots()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format", ApplicationProperties.getValue("aliquots.noOfAliquots")));
			pageOf = Constants.PAGEOF_ALIQUOT;
		}

		saveErrors(request, errors);
		return pageOf;
	}

	/**
	 * This method returns the ActionErrors object present in the request scope.
	 * If it is absent method creates & returns new ActionErrors object.
	 * @param request object of HttpServletRequest
	 * @return ActionErrors
	 */
	private ActionErrors getActionErrors(HttpServletRequest request)
	{
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);

		if (errors == null)
		{
			errors = new ActionErrors();
		}

		return errors;
	}

	/**
	 * @param request object of HttpServletRequest
	 */
	/*
	private void setParentSpecimenInRequest(HttpServletRequest request)
	{
		Map forwardToHashMap = (Map) request.getAttribute("forwardToHashMap");
		if (forwardToHashMap != null && forwardToHashMap.get("parentSpecimenId") != null)
		{   
			if(forwardToHashMap.get("parentSpecimenId") instanceof Long)
			{
				String parentSpecimenId = forwardToHashMap.get("parentSpecimenId").toString();
				request.setAttribute(Constants.PARENT_SPECIMEN_ID, parentSpecimenId);
				
			}
			else
			{
				request.setAttribute(Constants.PARENT_SPECIMEN_ID, forwardToHashMap.get("parentSpecimenId"));
			}
		}
		else
		{
			if (request.getParameter(Constants.PARENT_SPECIMEN_ID) != null)
			{
				request.setAttribute(Constants.PARENT_SPECIMEN_ID, request.getParameter(Constants.PARENT_SPECIMEN_ID));
			}
		}
	}
	*/
	//--- Mandar : 23June08 : For JSP Refactoring ------------------
	/**
	 * @param request object of HttpServletRequest
	 */
	private void setParentSpecimenInRequest(HttpServletRequest request)
	{
		Map forwardToHashMap = (Map) request.getAttribute("forwardToHashMap");
		Object parentSPId = "-1";
		if (forwardToHashMap != null && forwardToHashMap.get("parentSpecimenId") != null)
		{   
			if(forwardToHashMap.get("parentSpecimenId") instanceof Long)
			{
				parentSPId = forwardToHashMap.get("parentSpecimenId").toString();
			}
			else
			{
				parentSPId =  forwardToHashMap.get("parentSpecimenId");
			}
		}
		else
		{
			if (request.getParameter(Constants.PARENT_SPECIMEN_ID) != null)
			{
				parentSPId = request.getParameter(Constants.PARENT_SPECIMEN_ID);
			}
		}
		request.setAttribute(Constants.PARENT_SPECIMEN_ID,parentSPId);
		request.setAttribute("parentSPId",parentSPId);
	}
	
	private void setPageData(HttpServletRequest request, String pageOf, AliquotForm form)
	{
		request.setAttribute("pageOf", pageOf);
		String buttonKey = "";
		// To set
		String CPQuery= (String)request.getAttribute(Constants.CP_QUERY);
		String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
		if(exceedsMaxLimit == null)
		{	exceedsMaxLimit = "";	}
		request.setAttribute("exceedsMaxLimit",exceedsMaxLimit);
		if(Constants.PAGEOF_ALIQUOT.equals(pageOf))
		{	buttonKey = "buttons.submit";	}
		else if(Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
		{	buttonKey = "buttons.resubmit";	}
		request.setAttribute("buttonKey", buttonKey);
		 
		String nodeId="Specimen_";
		String psid = (String) request.getAttribute("parentSPId");
		if(psid!= null)
		{	nodeId = nodeId+psid;	}
		String refreshTree = "refreshTree('"+Constants.CP_AND_PARTICIPANT_VIEW+"','"+Constants.CP_TREE_VIEW+"','"+Constants.CP_SEARCH_CP_ID+"','"+Constants.CP_SEARCH_PARTICIPANT_ID+"','"+nodeId+"');";
		request.setAttribute("refreshTree", refreshTree);

		request.setAttribute("CREATE_ALIQUOT_ACTION", Constants.CREATE_ALIQUOT_ACTION);
		request.setAttribute("PAGEOF_CREATE_ALIQUOT",Constants.PAGEOF_CREATE_ALIQUOT);
		request.setAttribute("CP_QUERY_CREATE_ALIQUOT_ACTION",Constants.CP_QUERY_CREATE_ALIQUOT_ACTION);
		request.setAttribute("ALIQUOT_ACTION",Constants.ALIQUOT_ACTION);
		request.setAttribute("PAGEOF_ALIQUOT",Constants.PAGEOF_ALIQUOT);
		
		String action1 = "";
		String action2 = "";
		String action3 = "";
		if(CPQuery !=null)
		{
			action1 =  Constants.CP_QUERY_CREATE_ALIQUOT_ACTION + "?pageOf=" + Constants.PAGEOF_CREATE_ALIQUOT + 
			"&operation=add&menuSelected=15&buttonClicked=submit&" +Constants.PARENT_SPECIMEN_ID+"="+psid+"&"+Constants.CP_QUERY+"="+CPQuery;
			
			action2 = Constants.CP_QUERY_CREATE_ALIQUOT_ACTION + "?pageOf=" + Constants.PAGEOF_CREATE_ALIQUOT + 
			"&operation=add&menuSelected=15&buttonClicked=create&"+Constants.PARENT_SPECIMEN_ID+"="+psid+"&"+Constants.CP_QUERY+"="+CPQuery;

			action3 = Constants.CP_QUERY_CREATE_ALIQUOT_ACTION + "?pageOf=" + Constants.PAGEOF_CREATE_ALIQUOT + 
			"&operation=add&menuSelected=15&buttonClicked=checkbox&"+Constants.PARENT_SPECIMEN_ID+"="+psid+"&"+Constants.CP_QUERY+"="+CPQuery;
		}
		request.setAttribute("action1",action1);
		request.setAttribute("action2",action2);
		request.setAttribute("action3",action3);

		String unit = "";
		if(form != null)
		{
			unit = Utility.getUnit(form.getClassName(),form.getType());
		}
		//request.setAttribute("operation",Utility.toString(Constants.OPERATION));
		request.setAttribute("unit",unit);
		request.setAttribute("ADD",Constants.ADD);
		
		if(Variables.isSpecimenLabelGeneratorAvl)
		{request.setAttribute("isSpecimenLabelGeneratorAvl","true");}
		else
		{request.setAttribute("isSpecimenLabelGeneratorAvl","false");}
	
		if(Variables.isSpecimenBarcodeGeneratorAvl)
		{request.setAttribute("isSpecimenBarcodeGeneratorAvl","true");}
		else
		{request.setAttribute("isSpecimenBarcodeGeneratorAvl","false");}
		
		int colspanValue1 = 0;
		
        if((!Variables.isSpecimenLabelGeneratorAvl && Variables.isSpecimenBarcodeGeneratorAvl ) || (Variables.isSpecimenLabelGeneratorAvl && !Variables.isSpecimenBarcodeGeneratorAvl) )
		{   colspanValue1 =1;} 
		else if(!Variables.isSpecimenLabelGeneratorAvl && !Variables.isSpecimenBarcodeGeneratorAvl )	
		{   colspanValue1 =2;}
        request.setAttribute("colspanValue1",colspanValue1);
        
        request.setAttribute("JSForOutermostDataTable",ScriptGenerator.getJSForOutermostDataTable());
        
        setAliquotData(request, form);
        setAliquotBeans(request, form, psid, CPQuery);
	}
	
	private void setAliquotData(HttpServletRequest request, AliquotForm form)
	{
		Map dataMap = new HashMap();
		if(request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP) !=null)
		{	dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);}
		request.setAttribute("dataMap",dataMap);
		
		String[] labelNames = Constants.STORAGE_CONTAINER_LABEL;
		request.setAttribute("labelNames",labelNames);
		
		String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"};
		request.setAttribute("tdStyleClassArray",tdStyleClassArray);
	}
	
	private void setAliquotBeans(HttpServletRequest request, AliquotForm form, String psid, String cpQuery)
	{
		Map aliquotMap = new HashMap();
		Map dataMap = (Map)request.getAttribute("dataMap");
		int counter=0;
		if(form != null && form.getNoOfAliquots().trim().length() > 0)
		{
			counter = Integer.parseInt(form.getNoOfAliquots());
			aliquotMap = form.getAliquotMap();
		}
		List aliquotBeanList = new ArrayList();
		String sClass = (form.getClassName() == null ? "" : form.getClassName()) ;
		String scp = (form.getSpCollectionGroupId() == 0 ? "" : ""+form.getSpCollectionGroupId());
		
		for(int i=1;i<=counter;i++)
		{
			AliquotBean aliquotBean = new AliquotBean(i,aliquotMap, dataMap);
			aliquotBean.setAllData(psid, scp,sClass, cpQuery );
			aliquotBeanList.add(aliquotBean);
		}
		request.setAttribute("aliquotBeanList", aliquotBeanList);
	}
	
  public void setDateComponentData(HttpServletRequest request, String createdDate)
	{
//		String createdDate = Utility.toString(form.getCreatedDate());
		String nameOfForm ="aliquotForm";
		String dateFormName = "createdDate";
		if(createdDate.trim().length() > 0)
		{
				Integer specimenYear = new Integer(Utility.getYear(createdDate ));
				Integer specimenMonth = new Integer(Utility.getMonth(createdDate ));
				Integer specimenDay = new Integer(Utility.getDay(createdDate ));

				request.setAttribute("specimenYear",specimenYear);
				request.setAttribute("specimenMonth",specimenMonth);
				request.setAttribute("specimenDay",specimenDay);

		}
		request.setAttribute("createdDate",createdDate);
		request.setAttribute("nameOfForm",nameOfForm);
		request.setAttribute("dateFormName",dateFormName);
	}
  
  /* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#getObjectId(edu.wustl.common.actionForm.AbstractActionForm)
	 */
	@Override
	protected String getObjectId(AbstractActionForm form)
	{
		AliquotForm aliquotForm = (AliquotForm) form;
		if(aliquotForm.getSpCollectionGroupId()!=0L && aliquotForm.getSpCollectionGroupId()!= -1L)
		{
			SpecimenCollectionGroup specimenCollectionGroup = null;
			return Constants.COLLECTION_PROTOCOL_CLASS_NAME +"_"+aliquotForm.getSpCollectionGroupId();
		}
		return null; 
	}
	public synchronized long getTotalNoOfAliquotSpecimen(Long specId)throws DAOException
	{
		long aliquotChildCount =0;
		String[] selectColumnName = {"id"};
		String[] whereColumnName = {"parentSpecimen.id", "lineage", "collectionStatus"};
		String[] whereColumnCondition = {"=", "=" , "="};
		Object[] whereColumnValue = {specId, "Aliquot", "Collected"};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		DefaultBizLogic bizLogic = new DefaultBizLogic();
		List AliquotChildList = bizLogic.retrieve(Specimen.class.getName(), selectColumnName, whereColumnName,
				whereColumnCondition, whereColumnValue ,joinCondition);
		if(AliquotChildList!=null && !AliquotChildList.isEmpty())
		{
			aliquotChildCount=AliquotChildList.size();
		}
	
	    return aliquotChildCount;

	}

}