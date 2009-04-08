/**
 * <p>Title: CreateArrayInitAction Class>
 * <p>Description:	CreateArrayInitAction populates the fields in the Specimen Array page with array information.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ramya Nagraj
 * @version 1.00
 * Created on Dec 14,2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;
import edu.wustl.catissuecore.bean.DefinedArrayRequestBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;

/**
 * CreateArrayInitAction populates specimenarrayform object to display pre-populated data in specimenarray.jsp when 
 * navigated from CreateArray button of Ordering System module
 * @author ramya_nagraj
 */
public class CreateArrayInitAction extends BaseAction
{
	/**
	 * This function populates defined array information in the ActionForm object for predefined 
	 * values to be shown in SpecimenArray.jsp
	 * @param mapping object
	 * @param form object
	 * @param request object
	 * @param response object
	 * @return ActionForward object
	 * @throws Exception object
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{ 
		//Obtain session object from request.
		HttpSession session = request.getSession();
		  
		//Retrieve from query string
		//String arrayName = request.getParameter("array");
		//String operation = request.getParameter("operation");
		
		String arrayName = (String)request.getAttribute(Constants.ARRAY_NAME);
		String operation = (String)request.getAttribute(Constants.OPERATION);
		List<NameValueBean> storagePositionListForSpecimenArray = AppUtility.getStoragePositionTypeListForTransferEvent();
		request.setAttribute("storagePositionListForSpecimenArray", storagePositionListForSpecimenArray);

		String exceedingMaxLimit = "false";
		SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		
		//Obtain specimenArray BizLogic
		SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
		
		List definedArrayRequestMapList = (ArrayList)session.getAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST);
		Iterator definedArrayRequestMapListItr = definedArrayRequestMapList.iterator();
		while(definedArrayRequestMapListItr.hasNext())
		{
			Map defineArrayMap = (Map)definedArrayRequestMapListItr.next();
			Set defineArrayKeySet = defineArrayMap.keySet();
			Iterator defineArrayKeySetItr = defineArrayKeySet.iterator();
			
			//The Set has only one element(i.e,defineArrayRequestBean instance)
			DefinedArrayRequestBean definedArrayRequestBean = (DefinedArrayRequestBean)defineArrayKeySetItr.next();
			
			if(definedArrayRequestBean.getArrayName().equals(arrayName))
			{
				SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm)form;
				
				//Set Specimen Class in request attribute to be displayed in SpecimenArray.jsp
				List specimenClassList = new ArrayList();
				
				String colName = "specimenClass";
				String colValue = definedArrayRequestBean.getArrayClass();
				List specimenArrayTypeList = (ArrayList)specimenArrayBizLogic.retrieve(SpecimenArrayType.class.getName(),colName,colValue);
				SpecimenArrayType specimenArrayType = (SpecimenArrayType) specimenArrayTypeList.get(0);
				
				String specimenClassId = specimenArrayType.getId().toString();
				String specimenClass = specimenArrayType.getSpecimenClass();
				
				//Populate Specimen Class List in the request scope.
				NameValueBean specClassNameValue = new NameValueBean(specimenClass,specimenClassId);
				specimenClassList.add(specClassNameValue);
				request.setAttribute(Constants.SPECIMEN_CLASS_LIST,specimenClassList);
				
				specimenArrayForm.setSpecimenClass(specimenClass);
				
				//Set Specimen Types 
				List specimenTypeList = setClassAndtype(specimenArrayForm,specimenArrayType);
				
				//specimens ArrayList contains the id of all specimens in the given defined array.   
				List definedArrayDetailsBeanList = (ArrayList)defineArrayMap.get(definedArrayRequestBean);
				
				List specimensObjList = new ArrayList();
				List specimenIdList = (ArrayList)request.getAttribute(Constants.SPECIMEN_ID_LIST);
				//specimensObjList = constructSpecimenObjList(definedArrayDetailsBeanList);
				specimensObjList = constructSpecimenObjList(specimenIdList);
				//Populate arraycontentmap and set it in request scope.
				Map arrayContentMap = new HashMap(); 
				arrayContentMap = populateArrayContentMap(definedArrayRequestBean,specimensObjList);
				request.getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,arrayContentMap);
				specimenArrayForm.setSubOperation("");
				specimenArrayForm.setCreateSpecimenArray("yes");
				
				request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);   
				
				//Set the specimen name (i.e,label) in the form
				specimenArrayForm.setName(definedArrayRequestBean.getArrayName());
							
				//Setting newSpecimenArrayOrderItem Id
				specimenArrayForm.setNewArrayOrderItemId(definedArrayRequestBean.getOrderItemId());
				specimenArrayForm.setIsDefinedArray("True");
								
				//Set the array type in request attribute to be viewed in SpecimenArray.jsp
				List arrayTypeList = new ArrayList();
				String arrayTypeName = definedArrayRequestBean.getArrayType();
				String arrayTypeId = definedArrayRequestBean.getArrayTypeId();
				NameValueBean typeNameValueBean = new NameValueBean(arrayTypeName,arrayTypeId);
				arrayTypeList.add(typeNameValueBean);
				request.setAttribute(Constants.SPECIMEN_ARRAY_TYPE_LIST, arrayTypeList);
				
				specimenArrayForm.setSpecimenArrayTypeId(new Long(arrayTypeId).longValue());
				
				//Set Dimensions in the form
				specimenArrayForm.setOneDimensionCapacity(Integer.parseInt(definedArrayRequestBean.getOneDimensionCapacity()));
				specimenArrayForm.setTwoDimensionCapacity(Integer.parseInt(definedArrayRequestBean.getTwoDimensionCapacity()));
				
				//Set the User List in request attribute.
				UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
		    	Collection userCollection =  userBizLogic.getUsers(operation);
		    	request.setAttribute(Constants.USERLIST, userCollection);
		    	
		    	String subOperation = specimenArrayForm.getSubOperation();
		    	TreeMap containerMap = new TreeMap();
		    	//boolean isChangeArrayType = false;
		    			    	
		    	if (subOperation != null) 
		    	{
		    		SpecimenArrayType arrayType = null;
		        	if (specimenArrayForm.getSpecimenArrayTypeId() > 0) 
		        	{
		        		Object object = specimenArrayBizLogic.retrieve(SpecimenArrayType.class.getName(), specimenArrayForm.getSpecimenArrayTypeId());
		        		if (object != null) {
		        			arrayType = (SpecimenArrayType) object;
		        		}	
		        	}
		        	specimenTypeList = setClassAndtype(specimenArrayForm,arrayType);
		    	}
		    	
		    	StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
				containerMap = storageContainerBizLogic.getAllocatedContaienrMapForSpecimenArray(specimenArrayForm.getSpecimenArrayTypeId(),0,sessionData,exceedingMaxLimit);
				request.setAttribute(Constants.EXCEEDS_MAX_LIMIT,exceedingMaxLimit);
		    	request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
		    	
		    	List initialValues = null;
	    		initialValues = StorageContainerUtil.setInitialValue(specimenArrayBizLogic, specimenArrayForm,
						containerMap);
		    	request.setAttribute("initValues", initialValues);
				
				break;
			}//End if(definedArrayRequestBean.getArrayName().equals(arrayName))
		}//End Outer While
		SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm)form;
		specimenArrayForm.setForwardTo("orderDetails");
		return mapping.findForward("success");
	}
	/**
	 * This function retrieves specimens given the id and populates them in the arraylist
	 * @param definedArrayDetailsBeanList List containing definedArrayDetailsBean instances
	 * @return specimensObjList List containing specimen objects
	 * @throws NumberFormatException
	 * @throws DAOException
	 */
	private List constructSpecimenObjList(List specimenIdList) throws BizLogicException
	{
		List specimensObjList = new ArrayList();
		OrderBizLogic orderBizLogic = (OrderBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_DETAILS_FORM_ID);
		Iterator itr = specimenIdList.iterator();
		while(itr.hasNext())
		{
			//DefinedArrayDetailsBean definedArrayDetailsBean = (DefinedArrayDetailsBean)definedArrayDetailsBeanListItr.next();
			Long specimenId = (Long) itr.next();
			Object object = orderBizLogic.retrieve(Specimen.class.getName(), specimenId);
			//Set the specimen domain instances in the specimens object list.
			specimensObjList.add(object);
		}
		return specimensObjList;
	}
	
	/**
	 * This function populates the map to dispaly it in the grid.
	 * @param definedArrayRequestBean DefinedArrayRequestBean Object
	 * @param specimensObjList ArrayList containing the specimens
	 * @return arrayContentMap Map containing the speicmens to display in the grid.
	 */
	private Map populateArrayContentMap(DefinedArrayRequestBean definedArrayRequestBean,List specimensObjList)
	{
		Map arrayContentMap = new HashMap(); 
		String key = null;
		String value = null;
		//Obtain the one and two dimension capacity
		int row = new Integer(definedArrayRequestBean.getOneDimensionCapacity()).intValue();
		int col = new Integer(definedArrayRequestBean.getTwoDimensionCapacity()).intValue();
		int columnCount = col;
		int listCount = row*col;
		
		/* Size of specimenList is < or == value of listIndex */
		int listIndex=0;
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				if(listIndex <= listCount && listIndex<specimensObjList.size() && specimensObjList.get(listIndex) != null)
				{
					key = SpecimenArrayAppletUtil.getArrayMapKey(i,j,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_LABEL_INDEX);
					Specimen specimen = (Specimen)specimensObjList.get(listIndex);
					value = specimen.getLabel();
					arrayContentMap.put(key,value);
					
					key = SpecimenArrayAppletUtil.getArrayMapKey(i,j,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_ID_INDEX);
					value = specimen.getId().toString();
					arrayContentMap.put(key,value);
					
					key = SpecimenArrayAppletUtil.getArrayMapKey(i,j,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX);
					value = String.valueOf(row);
					arrayContentMap.put(key,value);
					
					key = SpecimenArrayAppletUtil.getArrayMapKey(i,j,columnCount,AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX);
					value = String.valueOf(col);
					arrayContentMap.put(key,value);
					
					listIndex++;
				}
			}
		}
		return arrayContentMap;
	}
	
	//TODO The functions below should be moved to common utility class as it is being used in SpecimenArrayAction.java also.
	
	/**
     * set class & type values for specimen array.
     * @param specimenArrayForm 
     * @param specimenArrayBizLogic
     * @param request
     * @return  array type
     * @throws DAOException
     */
	private List setClassAndtype(SpecimenArrayForm specimenArrayForm,SpecimenArrayType specimenArrayType) throws BizLogicException
	{  
		List specimenTypeList = new ArrayList();
		String specimentype = new String();
		NameValueBean specTypeNameValue = null; 
		SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
		Collection specimenArrayTypeCollection = (Collection) specimenArrayBizLogic.retrieveAttribute(SpecimenArrayType.class.getName(),
				specimenArrayType.getId(), "elements(specimenTypeCollection)");
		
		specimenArrayType.setSpecimenTypeCollection(specimenArrayTypeCollection);
		String[] specimenTypeArr = new String[specimenArrayType.getSpecimenTypeCollection().size()];
		int i=0;
		Iterator listItr=specimenArrayType.getSpecimenTypeCollection().iterator();
		while(listItr.hasNext())
		{
			specimentype = (String)listItr.next();
			specimenTypeArr[i] = specimentype;
			specTypeNameValue = new NameValueBean(specimentype,specimentype);
			specimenTypeList.add(specTypeNameValue);
		}
		//Set specimenType in the form
		specimenArrayForm.setSpecimenTypes(specimenTypeArr);
		return specimenTypeList;
	}
   
}
