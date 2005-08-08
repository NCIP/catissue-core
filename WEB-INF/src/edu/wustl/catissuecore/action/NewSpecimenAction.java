/**
 * <p>Title: NewSpecimenAction Class>
 * <p>Description:	NewSpecimenAction initializes the fields in the New Specimen page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * NewSpecimenAction initializes the fields in the New Specimen page.
 * @author gautam_shetty
 */
public class NewSpecimenAction extends Action
{
    
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        String pageOf = request.getParameter(Constants.PAGEOF);
        
        request.setAttribute(Constants.PAGEOF,pageOf);
        
        NewSpecimenBizLogic dao = (NewSpecimenBizLogic)BizLogicFactory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
        NewSpecimenForm newForm = (NewSpecimenForm) form;
        
        try
		{
            //Sets the Biohazard list of corresponding type.
//        	HashMap map = new HashMap();
//        	
//        	for(int i=0;i<Constants.BIOHAZARD_TYPE_ARRAY.length;i++)
//        	{
//	            String sourceObjectName = Biohazard.class.getName();
//	            String[] displayNameFields = {"name"};
//	            String valueField = "systemIdentifier";
//	            String[] whereColumnName = {"type"};
//	            String[] whereColumnCondition = {"="};
//	            Object[] whereColumnValue = {Constants.BIOHAZARD_TYPE_ARRAY[i]};
//	            String joinCondition = Constants.AND_JOIN_CONDITION;
//	            String separatorBetweenFields = "";
//				
//	            List biohazardList = dao.getList(sourceObjectName, displayNameFields, valueField,whereColumnName,
//	            				whereColumnCondition,whereColumnValue,joinCondition,separatorBetweenFields);
//	            
//	            map.put(Constants.BIOHAZARD_TYPE_ARRAY[i],biohazardList);
//        	}
//        	
//        	request.setAttribute(Constants.BIOHAZARD_NAME_LIST, map);
        	request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, Constants.BIOHAZARD_TYPE_ARRAY);
        	
           	List biohazardList = dao.retrieve(Biohazard.class.getName());
        	Iterator iterator = biohazardList.iterator();
        	if(biohazardList!=null && biohazardList.size()>0)
        	{
	        	String [] bhIdArray =  new String[biohazardList.size()];
	        	String [] bhTypeArray =  new String[biohazardList.size()];
	        	String [] bhNameArray =  new String[biohazardList.size()];
	        	int i=0;
	        	
	        	while(iterator.hasNext())
	        	{
	        		Biohazard hazard = (Biohazard)iterator.next();
	        		bhIdArray[i] = String.valueOf(hazard.getSystemIdentifier());
	        		bhNameArray[i] = hazard.getName();
	        		bhTypeArray[i] = hazard.getType();
	        		i++;
	        	}
	        	
	        	request.setAttribute(Constants.BIOHAZARD_NAME_LIST, bhNameArray);
	        	request.setAttribute(Constants.BIOHAZARD_ID_LIST, bhIdArray);
	        	request.setAttribute(Constants.BIOHAZARD_TYPES_LIST, bhTypeArray);
        	}
		}
        catch(Exception e)
		{
        	e.printStackTrace();
		}
        
        String [] specimenCollectionGroupArray = {Constants.SELECT_OPTION,"1","2","3","4"};
        
        request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_LIST,specimenCollectionGroupArray);
        
        request.setAttribute(Constants.SPECIMEN_TYPE_LIST, Constants.SPECIMEN_TYPE_VALUES);
        
        request.setAttribute(Constants.SPECIMEN_SUB_TYPE_LIST, Constants.SPECIMEN_SUB_TYPE_VALUES);
        
        request.setAttribute(Constants.TISSUE_SITE_LIST,Constants.TISSUE_SITE_VALUES);
        
        request.setAttribute(Constants.TISSUE_SIDE_LIST,Constants.TISSUE_SIDE_VALUES);
        
        request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, Constants.PATHOLOGICAL_STATUS_VALUES);
        
        request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, Constants.BIOHAZARD_TYPE_ARRAY);
        
        return mapping.findForward(Constants.SUCCESS);
    }

}
