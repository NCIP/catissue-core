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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
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
        
        String[] arrayTypeLabelProperty = {"name"};
        String  arrayTypeProperty = "id";
        SpecimenArrayBizLogic specimenArrayBizLogic = (SpecimenArrayBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_ARRAY_FORM_ID);
        
        List specimenArrayTypeList = specimenArrayBizLogic.getList(SpecimenArrayType.class.getName(),arrayTypeLabelProperty, arrayTypeProperty, false);
        
        for (Iterator iter = specimenArrayTypeList.iterator(); iter.hasNext();) {
			NameValueBean nameValueBean = (NameValueBean) iter.next();
			// remove ANY entry from array type list
			if (nameValueBean.getValue().equals(Constants.ARRAY_TYPE_ANY_VALUE) && nameValueBean.getName().equalsIgnoreCase(Constants.ARRAY_TYPE_ANY_NAME))
			{
				iter.remove();
				break;
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
    	SpecimenArrayForm specimenArrayForm = (SpecimenArrayForm) form;
    	Map containerMap = new TreeMap();
    	String subOperation = specimenArrayForm.getSubOperation();

    	if (subOperation != null) 
    	{
    		boolean isChangeArrayType = false;
    		if (subOperation.equals("ChangeArraytype")) 
    		{
	    		specimenArrayForm.setCreateSpecimenArray("no");
	    		isChangeArrayType = true;
	    		request.getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,new HashMap());
    		} 
    		//else if ((subOperation.equalsIgnoreCase("CreateSpecimenArray")) || subOperation.equalsIgnoreCase("ChangeEnterSpecimenBy"))
    		else if (subOperation.equalsIgnoreCase("CreateSpecimenArray"))
    		{
    			specimenArrayForm.setCreateSpecimenArray("yes");
    			request.getSession().setAttribute(Constants.SPECIMEN_ARRAY_CONTENT_KEY,createSpecimenArrayMap(specimenArrayForm));
    		}
    		specimenArrayForm.setSubOperation("");
    		specimenTypeList = doSetClassAndType(specimenArrayForm,specimenArrayBizLogic,isChangeArrayType);
    	}
		StorageContainerBizLogic storageContainerBizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		containerMap = storageContainerBizLogic.getAllocatedContaienrMapForSpecimenArray(specimenArrayForm.getSpecimenArrayTypeId(),0);
    	request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
    	
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
    	
    	if ((operation.equals(Constants.ADD)) && specimenArrayForm.getCreatedBy() == 0)
    	{
    		if ((userCollection != null) && (userCollection.size() > 1))
    		{
    			Iterator iterator = userCollection.iterator();
    			iterator.next();
	        	NameValueBean nameValueBean = (NameValueBean) iterator.next();
	        	specimenArrayForm.setCreatedBy(Long.valueOf(nameValueBean.getValue()).longValue());
    		}
        }
        return mapping.findForward(pageOf);
    }
    
    /**
     * @param specimenArrayForm 
     * @param specimenArrayBizLogic
     * @param request
     * @return  array type
     * @throws DAOException
     */
    private List doSetClassAndType(SpecimenArrayForm specimenArrayForm,SpecimenArrayBizLogic specimenArrayBizLogic,boolean isChangeArrayType) 
    							throws DAOException	
    {
    	SpecimenArrayType arrayType = null;
    	List specimenTypeList = null;
    	if (specimenArrayForm.getSpecimenArrayTypeId() > 0) 
    	{
    		String columnName = "id";
    		String columnVal = "" + specimenArrayForm.getSpecimenArrayTypeId();
    		List specimenArrayTypes = specimenArrayBizLogic.retrieve(SpecimenArrayType.class.getName(),columnName,columnVal);
    		if ((specimenArrayTypes != null) && (!specimenArrayTypes.isEmpty())) {
    			arrayType = (SpecimenArrayType) specimenArrayTypes.get(0);
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
    			if (isChangeArrayType) 
    			{
    				specimenArrayForm.setOneDimensionCapacity(arrayType.getCapacity().getOneDimensionCapacity().intValue());
    				specimenArrayForm.setTwoDimensionCapacity(arrayType.getCapacity().getTwoDimensionCapacity().intValue());
    				specimenArrayForm.setName( arrayType.getName() + "_" + specimenArrayForm.getSpecimenArrayTypeId());
    			}
    		}
    	}
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
}
