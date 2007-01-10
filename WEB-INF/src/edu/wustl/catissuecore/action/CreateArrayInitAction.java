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
import edu.wustl.catissuecore.bean.DefinedArrayDetailsBean;
import edu.wustl.catissuecore.bean.DefinedArrayRequestBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


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
		String arrayName = request.getParameter("array");
		String operation = request.getParameter("operation");
		
		String exceedingMaxLimit = "false";
		
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
				specimenArrayForm.setSubOperation("ChangeArraytype");
				
				//Set Specimen Class in request attribute to be displayed in SpecimenArray.jsp
				List specimenClassList = new ArrayList();
				
				String colName = "specimenClass";
				String colValue = definedArrayRequestBean.getArrayClass();
				List specimenArrayTypeList = (ArrayList)specimenArrayBizLogic.retrieve(SpecimenArrayType.class.getName(),colName,colValue);
				SpecimenArrayType specimenArrayType = (SpecimenArrayType) specimenArrayTypeList.get(0);
				
				String specimenClassId = specimenArrayType.getId().toString();
				String specimenClass = specimenArrayType.getSpecimenClass();
				
				NameValueBean specClassNameValue = new NameValueBean(specimenClass,specimenClassId);
				specimenClassList.add(specClassNameValue);
				request.setAttribute(Constants.SPECIMEN_CLASS_LIST,specimenClassList);
				
				specimenArrayForm.setSpecimenClass(specimenClass);
				
				//Set Specimen Types 
				List specimenTypeList = setClassAndtype(specimenArrayForm,specimenArrayType);
			
				
				/*
				List definedArrayDetailsBeanList = (ArrayList)defineArrayMap.get(definedArrayRequestBean);
				Iterator definedArrayDetailsBeanListItr = definedArrayDetailsBeanList.iterator();
				while(definedArrayDetailsBeanListItr.hasNext())
				{
					DefinedArrayDetailsBean definedArrayDetailsBean = (DefinedArrayDetailsBean)definedArrayDetailsBeanListItr.next();
					specimenTypeList.add(definedArrayDetailsBean.getType());
				}*/
				
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
		    	boolean isChangeArrayType = false;
		    	String idColumnName = "id";
		    	
		    	if (subOperation != null) 
		    	{
		    		SpecimenArrayType arrayType = null;
		        	if (specimenArrayForm.getSpecimenArrayTypeId() > 0) 
		        	{
		        		String columnVal = "" + specimenArrayForm.getSpecimenArrayTypeId();
		        		List specimenArrayTypes = specimenArrayBizLogic.retrieve(SpecimenArrayType.class.getName(),idColumnName,columnVal);
		        		if ((specimenArrayTypes != null) && (!specimenArrayTypes.isEmpty())) {
		        			arrayType = (SpecimenArrayType) specimenArrayTypes.get(0);
		        		}	
		        	}
		        	specimenTypeList = setClassAndtype(specimenArrayForm,arrayType);
		    		if (subOperation.equals("ChangeArraytype")) 
		    		{
			    		//specimenArrayForm.setCreateSpecimenArray("no");
			    		isChangeArrayType = true;
			    		
						specimenArrayForm.setOneDimensionCapacity(arrayType.getCapacity().getOneDimensionCapacity().intValue());
						specimenArrayForm.setTwoDimensionCapacity(arrayType.getCapacity().getTwoDimensionCapacity().intValue());
						specimenArrayForm.setName( arrayType.getName() + "_" + specimenArrayBizLogic.getUniqueIndexForName());
						
		    			specimenArrayForm.setCreateSpecimenArray("yes");
		    			request.getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,createSpecimenArrayMap(specimenArrayForm));
			    		//request.getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,new HashMap());
		    		}
		    		//else if ((subOperation.equalsIgnoreCase("CreateSpecimenArray")) || subOperation.equalsIgnoreCase("ChangeEnterSpecimenBy"))
		    		else if (subOperation.equalsIgnoreCase("CreateSpecimenArray"))
		    		{
		    			specimenArrayForm.setCreateSpecimenArray("yes");
		    			request.getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,createSpecimenArrayMap(specimenArrayForm));
		    		}
		    		specimenArrayForm.setSubOperation("");
		    	}
		    	
		    	StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
				containerMap = storageContainerBizLogic.getAllocatedContaienrMapForSpecimenArray(specimenArrayForm.getSpecimenArrayTypeId(),0,null,exceedingMaxLimit);
				request.setAttribute(Constants.EXCEEDS_MAX_LIMIT,exceedingMaxLimit);
		    	request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
		    	
		    	List initialValues = null;
		    	if (isChangeArrayType)
		    	{
		    		initialValues = checkForInitialValues(containerMap);
		    		
		    	} 
		    	else
		    	{
		    		String[] startingPoints = new String[]{"-1", "-1", "-1"};
		    		
		    		String containerName = null;
					if (specimenArrayForm.getStorageContainer() != null
							&& !specimenArrayForm.getStorageContainer().equals("-1"))
					{
						startingPoints[0] = specimenArrayForm.getStorageContainer();
			        	String[] selectColumnName = {"name"}; 
			        	String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
			        	String[] whereColumnCondition = {"="};
			        	Object[] whereColumnValue = {Long.valueOf(startingPoints[0])};
			        	String joinCondition = Constants.AND_JOIN_CONDITION;
			        	//specimenArrayBizLogic.retrieve(StorageContainer.class.getName(), new Long(specimenArrayForm.getSpecimenArrayTypeId()));
			    		List containerList = specimenArrayBizLogic.retrieve(StorageContainer.class.getName(),selectColumnName,whereColumnName,whereColumnCondition,whereColumnValue,joinCondition);
			    		if ((containerList != null) && (!containerList.isEmpty())) 
			    		{
			    			containerName = (String) containerList.get(0);
			    		}
						//List  = specimenArrayBizLogic.retrieve(StorageContainer.class.getName(), idColumnName, Long.valueOf(startingPoints[0]));
					}
					if (specimenArrayForm.getPositionDimensionOne() != -1)
					{
						startingPoints[1] = String.valueOf(specimenArrayForm.getPositionDimensionOne());
					}
					
					if (specimenArrayForm.getPositionDimensionTwo() != -1)
					{
						startingPoints[2] = String.valueOf(specimenArrayForm.getPositionDimensionTwo());
					}
					initialValues = new ArrayList();
					initialValues.add(startingPoints);
					// if not null
					if (containerName != null)
					{
						addPostions(containerMap,Long.valueOf(startingPoints[0]),containerName,Integer.valueOf(startingPoints[1]),Integer.valueOf(startingPoints[2]));
					}
		    	}
		    	request.setAttribute("initValues", initialValues);
				
				break;
			}
		}
		
		
		return mapping.findForward("success");
	}
	
	private List setClassAndtype(SpecimenArrayForm specimenArrayForm,SpecimenArrayType specimenArrayType) throws DAOException
	{
		List specimenTypeList = new ArrayList();
		String specimentype = new String();
		NameValueBean specTypeNameValue = null; 
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
	
    /**
     * @param specimenArrayForm array Form
     * @return map
     */
    private Map createSpecimenArrayMap(SpecimenArrayForm specimenArrayForm) 
    {
    	Map arrayContentMap = new HashMap();
    	String value = "";
    	int rowCount = specimenArrayForm.getOneDimensionCapacity();
    	int columnCount = specimenArrayForm.getTwoDimensionCapacity();
    	
		for (int i=0; i < rowCount ; i++) 
		{
			  for (int j=0;j < columnCount; j++) 
			  {
				for(int k=0; k < AppletConstants.ARRAY_CONTENT_ATTRIBUTE_NAMES.length; k++) 
				{
/*					if ((k == AppletConstants.ARRAY_CONTENT_ATTR_CONC_INDEX) || (k == AppletConstants.ARRAY_CONTENT_ATTR_QUANTITY_INDEX)) {
						value = "20";
					} else {
						value = "";
					}
*/
					value = "";	
					if(k == AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_ONE_INDEX) {
						value = String.valueOf(i + 1);
					}
					if(k == AppletConstants.ARRAY_CONTENT_ATTR_POS_DIM_TWO_INDEX) {
						value = String.valueOf(j + 1);
					}
					arrayContentMap.put(SpecimenArrayAppletUtil.getArrayMapKey(i,j,columnCount,k),value);
				}
			  }
		}
		return arrayContentMap;
    }
    
    /**
	 * check for initial values for storage container.
	 * @param containerMap container map
	 * @return list of initial values
	 */
	private List checkForInitialValues(Map containerMap)
	{
		List initialValues = null;

		if (containerMap.size() > 0)
		{
			String[] startingPoints = new String[3];

			Set keySet = containerMap.keySet();
			Iterator itr = keySet.iterator();
			NameValueBean nvb = (NameValueBean) itr.next();
			startingPoints[0] = nvb.getValue();

			Map map1 = (Map) containerMap.get(nvb);
			keySet = map1.keySet();
			itr = keySet.iterator();
			nvb = (NameValueBean) itr.next();
			startingPoints[1] = nvb.getValue();

			List list = (List) map1.get(nvb);
			nvb = (NameValueBean) list.get(0);
			startingPoints[2] = nvb.getValue();

			Logger.out.info("Starting points[0]" + startingPoints[0]);
			Logger.out.info("Starting points[1]" + startingPoints[1]);
			Logger.out.info("Starting points[2]" + startingPoints[2]);
			initialValues = new ArrayList();
			initialValues.add(startingPoints);
		}
		return initialValues;
	}
	
	/**
	 * add positions while in edit mode
	 * @param containerMap 
	 * @param id
	 * @param containerName
	 * @param pos1
	 * @param pos2
	 */
	private void addPostions(Map containerMap, Long id, String containerName, Integer pos1, Integer pos2)
	{
		int flag = 0;
		NameValueBean xpos = new NameValueBean(pos1, pos1);
		NameValueBean ypos = new NameValueBean(pos2, pos2);
		NameValueBean parentId = new NameValueBean(containerName, id);

		Set keySet = containerMap.keySet();
		Iterator itr = keySet.iterator();
		while (itr.hasNext())
		{
			NameValueBean nvb = (NameValueBean) itr.next();
			if (nvb.getValue().equals(id.toString()))
			{
				Map pos1Map = (Map) containerMap.get(nvb);
				Set keySet1 = pos1Map.keySet();
				Iterator itr1 = keySet1.iterator();
				while (itr1.hasNext())
				{
					NameValueBean nvb1 = (NameValueBean) itr1.next();
					if (nvb1.getValue().equals(pos1.toString()))
					{
						List pos2List = (List) pos1Map.get(nvb1);
						pos2List.add(ypos);
						flag = 1;
						break;
					}
				}
				if (flag != 1)
				{
					List pos2List = new ArrayList();
					pos2List.add(ypos);
					pos1Map.put(xpos, pos2List);
					flag = 1;
				}
			}
		}
		if (flag != 1)
		{
			List pos2List = new ArrayList();
			pos2List.add(ypos);

			Map pos1Map = new TreeMap();
			pos1Map.put(xpos, pos2List);
			containerMap.put(parentId, pos1Map);

		}

	}

}
