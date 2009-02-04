/*
 * Created on Jul 13, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenArrayForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.util.SpecimenArrayAppletUtil;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;


/**
 * Specimen Array action is used to perform action level operations for specimen array object.
 * @author gautam_shetty
 * @author ashwin_gupta
 */
public class SpecimenArrayAction extends SecureAction
{
    
    /**
     * @see edu.wustl.common.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String operation = request.getParameter(Constants.OPERATION);
        request.setAttribute(Constants.OPERATION, operation);
        SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) form;
        SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		//boolean to indicate whether the suitable containers to be shown in dropdown 
		//is exceeding the max limit.
		String exceedingMaxLimit = "false";
        String[] arrayTypeLabelProperty = {"name"};
        String  arrayTypeProperty = "id";
        SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
        List specimenArrayTypeList = new ArrayList();

        if (operation.equals(Constants.ADD))
        {	
	        specimenArrayTypeList = specimenArrayBizLogic.getList(SpecimenArrayType.class.getName(),arrayTypeLabelProperty, arrayTypeProperty, true);
	        for (Iterator iter = specimenArrayTypeList.iterator(); iter.hasNext();) 
	        {
				NameValueBean nameValueBean = (NameValueBean) iter.next();
				// remove ANY entry from array type list
				if (nameValueBean.getValue().equals(Constants.ARRAY_TYPE_ANY_VALUE) && nameValueBean.getName().equalsIgnoreCase(Constants.ARRAY_TYPE_ANY_NAME))
				{
					iter.remove();
					break;
				}
			}
        }
        else if (operation.equals(Constants.EDIT))
        {
        	String[] selectColumnName = {"id","name"}; 
        	String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
        	String[] whereColumnCondition = {"="};
        	Object[] whereColumnValue = {new Long(specimenArrayForm.getSpecimenArrayTypeId())};
        	String joinCondition = Constants.AND_JOIN_CONDITION;
        	//specimenArrayBizLogic.retrieve(StorageContainer.class.getName(), new Long(specimenArrayForm.getSpecimenArrayTypeId()));
    		List specimenArrayTypes = specimenArrayBizLogic.retrieve(SpecimenArrayType.class.getName(),selectColumnName,whereColumnName,whereColumnCondition,whereColumnValue,joinCondition);
    		if ((specimenArrayTypes != null) && (!specimenArrayTypes.isEmpty())) 
    		{
    			Object[] obj = (Object[]) specimenArrayTypes.get(0);
    			Long id = (Long) obj[0];
    			String name = (String) obj[1];
    			NameValueBean nameValueBean = new NameValueBean(name,id);
    			
    			specimenArrayTypeList.add(nameValueBean);
    		}
        }
        
        
        request.setAttribute(Constants.SPECIMEN_ARRAY_TYPE_LIST, specimenArrayTypeList);
        //Setting the specimen class list
        List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS,null);
    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
    	
		String strMenu = request.getParameter(Constants.MENU_SELECTED);
		if(strMenu != null )
		{
			request.setAttribute(Constants.MENU_SELECTED ,strMenu);
			Logger.out.debug(Constants.MENU_SELECTED + " " +strMenu +" set successfully");
		}
    	
		
    	//Setting the specimen type list
    	List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_TYPE,null);
    	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
    	Collection userCollection =  userBizLogic.getUsers(operation);
    	request.setAttribute(Constants.USERLIST, userCollection);
    	TreeMap containerMap = new TreeMap();
    	String subOperation = specimenArrayForm.getSubOperation();
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
        	specimenTypeList = doSetClassAndType(specimenArrayForm,specimenArrayBizLogic,arrayType);
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
		containerMap = storageContainerBizLogic.getAllocatedContaienrMapForSpecimenArray(specimenArrayForm.getSpecimenArrayTypeId(),0,sessionData,exceedingMaxLimit);
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
    	if (specimenTypeList == null)
    	{
    		// In case of search & edit operation
    		specimenTypeList = (List) request.getAttribute(Constants.SPECIMEN_TYPE_LIST);
    	}
    	
    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);    	
    	String pageOf = (String)request.getParameter(Constants.PAGEOF);
    	
    	if (pageOf == null) {
    		pageOf = Constants.SUCCESS;
    	}
    	
    	if (operation.equals(Constants.ADD))
    	{
    		// set default user
    		if (specimenArrayForm.getCreatedBy() == 0)
    		{	
	    		if ((userCollection != null) && (userCollection.size() > 1))
	    		{
	    			Iterator iterator = userCollection.iterator();
	    			iterator.next();
		        	NameValueBean nameValueBean = (NameValueBean) iterator.next();
		        	specimenArrayForm.setCreatedBy(Long.valueOf(nameValueBean.getValue()).longValue());
	    		}
    		}
        }
        return mapping.findForward(pageOf);
    }
    
    /**
     * set class & type values for specimen array.
     * @param specimenArrayForm 
     * @param specimenArrayBizLogic
     * @param request
     * @return  array type
     * @throws DAOException
     */
    private List doSetClassAndType(SpecimenArrayForm specimenArrayForm,SpecimenArrayBizLogic specimenArrayBizLogic,SpecimenArrayType arrayType) 
    							throws DAOException	
    {
    	
    	List specimenTypeList = null;
    	if (specimenArrayForm.getSpecimenArrayTypeId() > 0) 
    	{
    		if (arrayType != null) {
    			specimenArrayForm.setSpecimenClass(arrayType.getSpecimenClass());
    			String[] specimenTypeArr = new String[arrayType.getSpecimenTypeCollection().size()];
    			specimenTypeList = new ArrayList();
    			int i = 0;
    			String specimenType = null;
    			NameValueBean nameValueBean = null;
    			for (Iterator iter = arrayType.getSpecimenTypeCollection().iterator(); iter
						.hasNext();i++) {
    				specimenType = (String) iter.next();
					specimenTypeArr[i] = specimenType;
					nameValueBean = new NameValueBean(specimenType,specimenType);
					specimenTypeList.add(nameValueBean);
				}
    			specimenArrayForm.setSpecimenTypes(specimenTypeArr);
    			
/*    			if (isChangeArrayType) 
    			{
    				specimenArrayForm.setOneDimensionCapacity(arrayType.getCapacity().getOneDimensionCapacity().intValue());
    				specimenArrayForm.setTwoDimensionCapacity(arrayType.getCapacity().getTwoDimensionCapacity().intValue());
    				specimenArrayForm.setName( arrayType.getName() + "_" + specimenArrayBizLogic.getUniqueIndexForName());
    			}
*/    		}
    	}
    		return specimenTypeList;
    }
    
    /**
     * Creates specimen array map which will contain specimen array contents.
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
    
    // TODO move this function to common util because it is being used at many places.
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
 
	//TODO move this function to common util because it is being used at many places.
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
