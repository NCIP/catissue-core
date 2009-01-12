/**
 * <p>Title: CreateSpecimenAction Class>
 * <p>Description:	CreateSpecimenAction initializes the fields in the Create Specimen page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
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
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Specimen;
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
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.tag.ScriptGenerator;

/**
 * CreateSpecimenAction initializes the fields in the Create Specimen page.
 * @author aniruddha_phadnis
 */
public class CreateSpecimenAction extends SecureAction
{

	/**
	 * Overrides the execute method of Action class.
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		CreateSpecimenForm createForm = (CreateSpecimenForm) form;
		
		List<NameValueBean> storagePositionList =  Utility.getStoragePositionTypeList();
		
		request.setAttribute("storageList", storagePositionList);
		//List of keys used in map of ActionForm
		List key = new ArrayList();
		key.add("ExternalIdentifier:i_name");
		key.add("ExternalIdentifier:i_value");

		//boolean to indicate whether the suitable containers to be shown in dropdown 
		//is exceeding the max limit.
		String exceedingMaxLimit = "false";
		
		//Gets the map from ActionForm
		Map map = createForm.getExternalIdentifier();

		//Calling DeleteRow of BaseAction class
		MapDataParser.deleteRow(key, map, request.getParameter("status"));

		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);

		//Sets the operation attribute to be used in the Add/Edit User Page. 
		request.setAttribute(Constants.OPERATION, operation);
		String virtuallyLocated = request.getParameter("virtualLocated");
		if(virtuallyLocated!=null && virtuallyLocated.equals("true"))
		{
			createForm.setVirtuallyLocated(true);
		}
        
         /**
          * Patch ID: 3835_1_16
          * See also: 1_1 to 1_5
          * Description : CreatedOn date by default should be current date.
          */ 
    
         createForm.setCreatedDate(Utility.parseDateToString(Calendar.getInstance().getTime(), Variables.dateFormat));
                       
         String pageOf = null;
         String tempPageOf = (String)request.getParameter("pageOf"); 
         if(tempPageOf == null || tempPageOf.equals(""))
         {
        	 pageOf = (String)request.getSession().getAttribute("pageOf");        	
         }
         else
         {
        	 pageOf = request.getParameter(Constants.PAGEOF);
        	 request.getSession().setAttribute("pageOf", pageOf);
         }
         
         SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
         
		/*
		 // ---- chetan 15-06-06 ----
		 StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		 Map containerMap = bizLogic.getAllocatedContainerMap();
		 request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
		 // -------------------------
		 request.setAttribute(Constants.PAGEOF,pageOf);
		 */
		request.setAttribute(Constants.PAGEOF,pageOf);
		DefaultBizLogic dao = new DefaultBizLogic(); 
		TreeMap containerMap = new TreeMap();
		List initialValues = null;
		if (operation.equals(Constants.ADD))
		{
			//if this action bcos of delete external identifier then validation should not happen.
			if (request.getParameter("button") == null)
			{
				String parentSpecimenLabel = null;
				Long parentSpecimenID = null;
				//Bug-2784: If coming from NewSpecimen page, then only set parent specimen label.
				Map forwardToHashMap = (Map) request.getAttribute("forwardToHashMap");
				if(forwardToHashMap != null && forwardToHashMap.get("parentSpecimenId") != null && forwardToHashMap.get(Constants.SPECIMEN_LABEL) != null)
				{
					parentSpecimenID = (Long)forwardToHashMap.get("parentSpecimenId");
					parentSpecimenLabel = Utility.toString(forwardToHashMap.get(Constants.SPECIMEN_LABEL));
					request.setAttribute(Constants.PARENT_SPECIMEN_ID, parentSpecimenID);
					createForm.setParentSpecimenLabel(parentSpecimenLabel);
					createForm.setLabel("");					
				}

				if(createForm.getLabel()==null || createForm.getLabel().equals(""))
				{
					/**
		        	 * Name : Virender Mehta
		             * Reviewer: Sachin Lale
		             * Description: By getting instance of AbstractSpecimenGenerator abstract class current label retrived and set.
		        	 */
					//int totalNoOfSpecimen = bizLogic.totalNoOfSpecimen(sessionData)+1;
					
					HashMap inputMap = new HashMap();
					inputMap.put(Constants.PARENT_SPECIMEN_LABEL_KEY, parentSpecimenLabel);
					inputMap.put(Constants.PARENT_SPECIMEN_ID_KEY, String.valueOf(parentSpecimenID));

					//SpecimenLabelGenerator abstractSpecimenGenerator  = SpecimenLabelGeneratorFactory.getInstance();
					//createForm.setLabel(abstractSpecimenGenerator.getNextAvailableDeriveSpecimenlabel(inputMap));
				}
				
				if (forwardToHashMap == null && ((createForm.getRadioButton().equals("1") && createForm
						.getParentSpecimenLabel() != null && !createForm.getParentSpecimenLabel().equals(""))
						|| (createForm.getRadioButton().equals("2") && createForm
								.getParentSpecimenBarcode() != null && !createForm.getParentSpecimenBarcode().equals(""))))
				{
					String errorString = null;
					String []columnName = new String[1];
					Object []columnValue = new String[1];

					// checks whether label or barcode is selected
					if (createForm.getRadioButton().equals("1"))
					{
						columnName[0] = Constants.SYSTEM_LABEL; 
						columnValue[0] = createForm.getParentSpecimenLabel().trim();
						errorString = ApplicationProperties.getValue("quickEvents.specimenLabel");
					}
					else
					{
						columnName[0] = Constants.SYSTEM_BARCODE;
						columnValue[0] = createForm.getParentSpecimenBarcode().trim();
						errorString = ApplicationProperties.getValue("quickEvents.barcode");
					}
					
					String []selectColumnName={Constants.COLUMN_NAME_SCG_ID};
					String []whereColumnCondition={"="};
					List scgList = dao.retrieve(Specimen.class.getName(),selectColumnName,columnName
							,whereColumnCondition,columnValue,null ); 
					
					boolean isSpecimenExist = true;
					if (scgList != null && !scgList.isEmpty())
					{
//						Specimen sp = (Specimen) spList.get(0);
						Long scgId = (Long)scgList.get(0);
						long cpId = getCpId(dao,scgId);
						if(cpId == -1)
						{
							isSpecimenExist = false;
						}
						String spClass = createForm.getClassName();
						
						request.setAttribute(Constants.COLLECTION_PROTOCOL_ID, cpId + "");
						request.setAttribute(Constants.SPECIMEN_CLASS_NAME, spClass);
						if(virtuallyLocated!=null && virtuallyLocated.equals("false")) 
						{
							createForm.setVirtuallyLocated(false);
						}
						if(spClass!=null && createForm.getStContSelection() != Constants.RADIO_BUTTON_VIRTUALLY_LOCATED)
						{
						
							StorageContainerBizLogic scbizLogic = (StorageContainerBizLogic) BizLogicFactory
								.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
							containerMap = scbizLogic.getAllocatedContaienrMapForSpecimen(cpId,
									spClass, 0,exceedingMaxLimit,sessionData,true);
							ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
							if (containerMap.isEmpty())
							{
								if (errors == null || errors.size() == 0)
								{
									errors = new ActionErrors();
								}
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
										"storageposition.not.available"));
								saveErrors(request, errors);
							}
							if (errors == null || errors.size() == 0)
							{
								initialValues = StorageContainerUtil.checkForInitialValues(containerMap);
							}
							else
							{
								String[] startingPoints = new String[3];
								startingPoints[0] = createForm.getStorageContainer();
								startingPoints[1] = createForm.getPositionDimensionOne();
								startingPoints[2] = createForm.getPositionDimensionTwo() ;
								initialValues = new ArrayList();
								initialValues.add(startingPoints);
							}
					
						}
						/**
						 * Name : Vijay_Pande
						 * Patch ID: 4283_2 
						 * See also: 1-3
						 * Description: If radio button is clicked for map then clear values in the drop down list for storage position
						 */
						if(spClass!=null && createForm.getStContSelection() == Constants.RADIO_BUTTON_FOR_MAP)
						{
							String[] startingPoints = new String[]{"-1", "-1", "-1"};
							initialValues = new ArrayList();
							initialValues.add(startingPoints);
							request.setAttribute("initValues", initialValues);
						}
						/** -- patch ends here  --*/
					}
					else
					{
						isSpecimenExist = false;
					}
					
					if(!isSpecimenExist)
					{
						ActionErrors errors = (ActionErrors) request
						.getAttribute(Globals.ERROR_KEY);
						if (errors == null)
						{
							errors = new ActionErrors();
						}
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"quickEvents.specimen.notExists", errorString));
						saveErrors(request, errors);
						request.setAttribute("disabled", "true");
						createForm.setVirtuallyLocated(true);
					}

				}
			}
		}
		else
		{
			containerMap = new TreeMap();
			Integer id = new Integer(createForm.getStorageContainer());
			String parentContainerName = "";
			
			Object object = dao.retrieve(StorageContainer.class.getName(), new Long(
					createForm.getStorageContainer()));
			if (object != null)
			{
				StorageContainer container = (StorageContainer) object;
				parentContainerName = container.getName();

			}
			Integer pos1 = new Integer(createForm.getPositionDimensionOne());
			Integer pos2 = new Integer(createForm.getPositionDimensionTwo());

			List pos2List = new ArrayList();
			pos2List.add(new NameValueBean(pos2, pos2));

			Map pos1Map = new TreeMap();
			pos1Map.put(new NameValueBean(pos1, pos1), pos2List);
			containerMap.put(new NameValueBean(parentContainerName, id), pos1Map);

			String[] startingPoints = new String[]{"-1", "-1", "-1"};
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
		request.setAttribute("initValues", initialValues);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		// -------------------------
		//Setting the specimen type list
		List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
				//Setting biohazard list
		List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_BIOHAZARD, null);
		request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);
		
		Map subTypeMap = Utility.getSpecimenTypeMap();	
		List specimenClassList = Utility.getSpecimenClassList();
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
		// set the map to subtype
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);

		//*************  ForwardTo implementation *************
		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");

		if (forwardToHashMap != null)
		{
			Long parentSpecimenId = (Long) forwardToHashMap.get("parentSpecimenId");

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
//				containerMap = getContainerMap(createForm.getParentSpecimenId(), createForm
//						.getClassName(), dao, scbizLogic,exceedingMaxLimit,request);
//				initialValues = checkForInitialValues(containerMap);
				/**
				 * Name : Vijay_Pande
				 * Reviewer Name : Sachin_Lale
				 * Bug ID: 4283
				 * Patch ID: 4283_1 
				 * See also: 1-3
				 * Description: Proper Storage location of derived specimen was not populated while coming from newly created parent specimen page.
				 * Initial value were generated but not set to form variables.
				 */
//				if(initialValues!=null)
//				{
//					initialValues = checkForInitialValues(containerMap);
//					String[] startingPoints=(String[])initialValues.get(0);
//					createForm.setStorageContainer(startingPoints[0]);
//					createForm.setPos1(startingPoints[1]);
//					createForm.setPos2(startingPoints[2]);		
//				}
				/**  --patch ends here -- */
			}
		}
		//*************  ForwardTo implementation *************
		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT,exceedingMaxLimit);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		if (createForm.isVirtuallyLocated())
		{
			request.setAttribute("disabled", "true");
		}
		setPageData(request, createForm);	
		request.setAttribute("createdDate", createForm.getCreatedDate());
		List dataList =(List)request.getAttribute(Constants.SPREADSHEET_DATA_LIST);
		List columnList = new ArrayList();
		columnList.addAll(Arrays.asList(Constants.DERIVED_SPECIMEN_COLUMNS));
		Utility.setGridData( dataList,columnList, request);
		Integer identifierFieldIndex = new Integer(4);
		request.setAttribute("identifierFieldIndex", identifierFieldIndex.intValue());
		return mapping.findForward(Constants.SUCCESS);
	}

	TreeMap getContainerMap(String specimenId, String className, DefaultBizLogic dao,
			StorageContainerBizLogic scbizLogic,String exceedingMaxLimit, HttpServletRequest request) throws DAOException,SMException
	{
		TreeMap containerMap = new TreeMap();

		Object object = dao.retrieve(Specimen.class.getName(), new Long(
				specimenId));
		SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		if (object != null)
		{
			Specimen sp = (Specimen) object;
			long cpId = sp.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
					.getCollectionProtocol().getId().longValue();
			String spClass = className;
			Logger.out.info("cpId :" + cpId + "spClass:" + spClass);
			containerMap = scbizLogic.getAllocatedContaienrMapForSpecimen(cpId, spClass, 0,exceedingMaxLimit,sessionData,true);
		}

		return containerMap;
	}
	
	private long getCpId(DefaultBizLogic dao, Long scgId) throws DAOException
	{
		long cpId = -1;
		String []columnName = new String[1];
		Object []columnValue = new Long[1];

		columnName[0] = Constants.SYSTEM_IDENTIFIER; 
		columnValue[0] = scgId;
		String []selectColumnName={"collectionProtocolRegistration.collectionProtocol.id"};
		String []whereColumnCondition={"="};
		List cpList = dao.retrieve(SpecimenCollectionGroup.class.getName(),selectColumnName,columnName
				,whereColumnCondition,columnValue,null );
		if (cpList != null && !cpList.isEmpty())
		{
			cpId = (Long)cpList.get(0);
		}
		return cpId;
	}
	
	//Mandar : 23June08 : For JSP Refractoring --------
	// Mandar : 16June08 For JSP refactoring
	
	private void setPageData(HttpServletRequest request, CreateSpecimenForm form)
	{
		
		setConstantValues(request);
		Utility.setDefaultPrinterTypeLocation(form);
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);
		setPageData1(request,  form);
		setPageData2(request, form, pageOf);
		setPageData3(request, form, pageOf);
		
		setNComboData(request, form);
		setXterIdData(request, form);
	}
	private void setPageData1(HttpServletRequest request, CreateSpecimenForm form)
	{
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);

		//TODO to check where are these used
//			String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
//			Integer identifierFieldIndex = new Integer(4);
//			String pageView=operation;
//			boolean readOnlyValue=false;
		
		String formName=operation;
		String editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyForAll=false;
		String printAction="printDeriveSpecimen";

		if(operation!=null && operation.equals(Constants.EDIT))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.CREATE_SPECIMEN_EDIT_ACTION;
//			readOnlyValue=true;
		}
		else
		{
			formName = Constants.CREATE_SPECIMEN_ADD_ACTION;
			if(pageOf!=null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY))
			{
				formName = Constants.CP_QUERY_CREATE_SPECIMEN_ADD_ACTION ;
				printAction="CPQueryPrintDeriveSpecimen";
			}
//			readOnlyValue=false;
		}

		if (operation!=null && operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}

		//-------------
		
		String changeAction3 = "setFormAction('"+formName+"')";
		String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
		String submitAndDistribute = "setSubmitted('ForwardTo','"+printAction+"','" + Constants.SPECIMEN_FORWARD_TO_LIST[4][1]+"')," + confirmDisableFuncName;
		String addMoreSubmitFunctionName = "setSubmitted('ForwardTo','"+printAction+"','" + Constants.SPECIMEN_FORWARD_TO_LIST[3][1]+"')";
		String addMoreSubmit = addMoreSubmitFunctionName + ","+confirmDisableFuncName;	

		request.setAttribute("changeAction3",changeAction3);
		request.setAttribute("addMoreSubmit",addMoreSubmit);
		// -----------------
		
		request.setAttribute("pageOf",pageOf);
		request.setAttribute("operation",operation);

		request.setAttribute("formName",formName);
		request.setAttribute("editViewButton",editViewButton);
		request.setAttribute("readOnlyForAll",readOnlyForAll);
		request.setAttribute("printAction",printAction);

		String unitSpecimen = "";
		String frdTo = "";
		
		int exIdRows=1;		 
		Map map = null;		
		if(form != null)
		{
			map = form.getExternalIdentifier();
			exIdRows = form.getExIdCounter();
			frdTo = form.getForwardTo();
			if(form.getUnit() != null)
			{	unitSpecimen = form.getUnit();	}
			if(frdTo.equals("") || frdTo==null)
			{	frdTo= "eventParameters";	}	
		}
		List exIdList = new ArrayList();
		for(int i=exIdRows;i>=1;i--)
	  	{
			ExternalIdentifierBean exd = new ExternalIdentifierBean(i,map);
			exIdList.add(exd);
	  	}	
		request.setAttribute("exIdList",exIdList);
		
		request.setAttribute("unitSpecimen",unitSpecimen);
		request.setAttribute("frdTo",frdTo);
		
		String multipleSpecimen = "0";
		String action = Constants.CREATE_SPECIMEN_ADD_ACTION;
		if(request.getAttribute("multipleSpecimen")!=null) 
		{
		   multipleSpecimen = "1";
		   action = "DerivedMultipleSpecimenAdd.do?retainForm=true";
		}
		request.setAttribute("multipleSpecimen",multipleSpecimen);
		request.setAttribute("action",action);
		
//		String onCheckboxChange = "setVirtuallyLocated(this,"+multipleSpecimen+")" ;		Not used anywhere
	}
	
	private void setPageData2(HttpServletRequest request, CreateSpecimenForm form, String pageOf)
	{
		String actionToCall2 = null;
		actionToCall2 = "CreateSpecimen.do?operation=add&pageOf=&menuSelected=15&virtualLocated=false";
		if(pageOf != null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY))
		{
			actionToCall2 = Constants.CP_QUERY_CREATE_SPECIMEN_ACTION+"?pageOf="+Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY+"&operation=add&virtualLocated=false";
		}
		request.setAttribute("actionToCall2",actionToCall2);
		
		String actionToCall1 = null;
		actionToCall1 = "CreateSpecimen.do?operation=add&pageOf=&menuSelected=15&virtualLocated=false";
		if(pageOf != null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY))
		{
			actionToCall1 = Constants.CP_QUERY_CREATE_SPECIMEN_ACTION+"?operation=add";
		}
		request.setAttribute("actionToCall1",actionToCall1);
		
		String deleteChecked = "";
		String multipleSpecimen = (String)request.getAttribute("multipleSpecimen");
		if(multipleSpecimen.equals("1"))
		{
			deleteChecked = "deleteChecked(\'addExternalIdentifier\',\'NewMultipleSpecimenAction.do?method=showDerivedSpecimenDialog&status=true&retainForm=true\',document.forms[0].exIdCounter,\'chk_ex_\',false);";
		}
		else
		{			
			if(pageOf != null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY))
			{
				deleteChecked = "deleteChecked(\'addExternalIdentifier\',\'CPQueryCreateSpecimen.do?pageOf=pageOfCreateSpecimenCPQuery&status=true&button=deleteExId\',document.forms[0].exIdCounter,\'chk_ex_\',false);";
			}
			else
			{
				deleteChecked = "deleteChecked(\'addExternalIdentifier\',\'CreateSpecimen.do?pageOf=pageOfCreateSpecimen&status=true&button=deleteExId\',document.forms[0].exIdCounter,\'chk_ex_\',false);";
			}
		}
		request.setAttribute("deleteChecked",deleteChecked);
		
		String actionToCall = "AddSpecimen.do?isQuickEvent=true";
		if(pageOf != null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY))
		{
			actionToCall = Constants.CP_QUERY_CREATE_SPECIMEN_ADD_ACTION+"?isQuickEvent=true";
		}
		request.setAttribute("actionToCall",actionToCall);
		
		String showRefreshTree ="false";
		if(pageOf!=null && pageOf.equals(Constants.PAGE_OF_CREATE_SPECIMEN_CP_QUERY))
		{
			if(request.getAttribute(Constants.PARENT_SPECIMEN_ID) != null )
			{
				Long parentSpecimenId = (Long) request.getAttribute(Constants.PARENT_SPECIMEN_ID);
				String nodeId = "Specimen_"+parentSpecimenId.toString();	
				showRefreshTree ="true";
			String refreshTree = "refreshTree('"+Constants.CP_AND_PARTICIPANT_VIEW+"','"+Constants.CP_TREE_VIEW+"','"+Constants.CP_SEARCH_CP_ID+"','"+Constants.CP_SEARCH_PARTICIPANT_ID+"','"+nodeId+"');";
			request.setAttribute("refreshTree",refreshTree);
			}
		}
		request.setAttribute("showRefreshTree",showRefreshTree);
	}

	private void setPageData3(HttpServletRequest request, CreateSpecimenForm form, String pageOf)
	{
		boolean readOnlyForAll = ((Boolean)request.getAttribute("readOnlyForAll")).booleanValue();
		String changeAction1 = "setFormAction('MakeParticipantEditable.do?"+Constants.EDITABLE+"="+!readOnlyForAll+"')";
		request.setAttribute("changeAction1",changeAction1);
		List specClassList = (List)request.getAttribute(Constants.SPECIMEN_CLASS_LIST);
		request.setAttribute("specClassList",specClassList);

		List specimenTypeList = (List) request.getAttribute(Constants.SPECIMEN_TYPE_LIST);
		HashMap specimenTypeMap = (HashMap) request.getAttribute(Constants.SPECIMEN_TYPE_MAP);
		String classValue = (String)form.getClassName();

		specimenTypeList = (List)specimenTypeMap.get(classValue);
		if(specimenTypeList == null)
		{
			specimenTypeList = new ArrayList();
			specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
		}
		request.setAttribute("specimenTypeList", specimenTypeList);
		request.setAttribute("specimenTypeMap",specimenTypeMap);

	}
	
	private void setConstantValues(HttpServletRequest request)
	{
		request.setAttribute("oper",Constants.OPERATION);
		request.setAttribute("query",Constants.QUERY);
		request.setAttribute("search",Constants.SEARCH);
		request.setAttribute("view",Constants.VIEW);
		request.setAttribute("isSpecimenLabelGeneratorAvl",Variables.isSpecimenLabelGeneratorAvl);
		request.setAttribute("UNIT_MG",Constants.UNIT_MG);
		request.setAttribute("labelNames",Constants.STORAGE_CONTAINER_LABEL);
		request.setAttribute("ADD",Constants.ADD);
		request.setAttribute("isSpecimenBarcodeGeneratorAvl",Variables.isSpecimenBarcodeGeneratorAvl);
		request.setAttribute("SPECIMEN_BUTTON_TIPS",Constants.SPECIMEN_BUTTON_TIPS[3]);
		request.setAttribute("SPECIMEN_FORWARD_TO_LIST",Constants.SPECIMEN_FORWARD_TO_LIST[3][0]);
	}
	
	private void setNComboData(HttpServletRequest request, CreateSpecimenForm form)
	{
			String[] attrNames = {"storageContainer", "positionDimensionOne", "positionDimensionTwo"};
			String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"};
			
			request.setAttribute("attrNames",attrNames);
			request.setAttribute("tdStyleClassArray",tdStyleClassArray);
			
			String[] initValues = new String[3];
			List initValuesList = (List)request.getAttribute("initValues");
			if(initValuesList != null)
			{
				initValues = (String[])initValuesList.get(0);
			}
			request.setAttribute("initValues",initValues);
			
			String className = (String) request.getAttribute(Constants.SPECIMEN_CLASS_NAME);
			if (className==null)
				className="";
			
			String collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);
			if (collectionProtocolId==null)
				collectionProtocolId="";
			
			String url = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;selectedContainerName=selectedContainerName&amp;pos1=pos1&amp;pos2=pos2&amp;containerId=containerId"
			+ "&" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+className
			+ "&" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + collectionProtocolId;				
			String buttonOnClicked = "mapButtonClickedOnNewSpecimen('"+url+"','createSpecimen')";

			request.setAttribute("buttonOnClicked",buttonOnClicked);
			
			int radioSelected = form.getStContSelection();
			String storagePosition = Constants.STORAGE_TYPE_POSITION_VIRTUAL;
			if(radioSelected == 1)
			{
				storagePosition = Constants.STORAGE_TYPE_POSITION_VIRTUAL;
			}
			else if(radioSelected == 2)
			{									
				storagePosition = Constants.STORAGE_TYPE_POSITION_AUTO;
			}
			else if(radioSelected == 3)
			{
				storagePosition = Constants.STORAGE_TYPE_POSITION_MANUAL;
			}
			
			request.setAttribute("storagePosition",storagePosition);			
			Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
			String jsForOutermostDataTable = ScriptGenerator.getJSForOutermostDataTable();
			String jsEquivalentFor = ScriptGenerator.getJSEquivalentFor(dataMap,"1");
			
			request.setAttribute("dataMap",dataMap);
			request.setAttribute("jsEquivalentFor",jsEquivalentFor);
			request.setAttribute("jsForOutermostDataTable",jsForOutermostDataTable);
	}
	
	private void setXterIdData(HttpServletRequest request, CreateSpecimenForm form)
	{
		String eiDispType1=request.getParameter("eiDispType");
		request.setAttribute("eiDispType1",eiDispType1);
		
		String delExtIds="deleteExternalIdentifiers('pageOfMultipleSpecimen')";
		if((String)request.getAttribute(Constants.PAGEOF)!=null)
		{
			delExtIds="deleteExternalIdentifiers('"+(String)request.getAttribute(Constants.PAGEOF)+"');";
		}
		request.setAttribute("delExtIds",delExtIds);
	}
	
	
	/* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#getObjectId(edu.wustl.common.actionForm.AbstractActionForm)
	 */
	@Override
	protected String getObjectId(AbstractActionForm form)
	{ 
		CreateSpecimenForm createSpecimenForm = (CreateSpecimenForm) form;
		SpecimenCollectionGroup specimenCollectionGroup = null;
		if(createSpecimenForm.getParentSpecimenId() != null && createSpecimenForm.getParentSpecimenId() != "")
		{
				Specimen specimen = Utility.getSpecimen(createSpecimenForm.getParentSpecimenId());
				specimenCollectionGroup = specimen.getSpecimenCollectionGroup();
				CollectionProtocolRegistration cpr = specimenCollectionGroup.getCollectionProtocolRegistration();
				if (cpr!= null)
				{
					CollectionProtocol cp = cpr.getCollectionProtocol();
					return Constants.COLLECTION_PROTOCOL_CLASS_NAME +"_"+cp.getId();
				}
		}
		return null;
		 
	}
}