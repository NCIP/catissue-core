/**
 * <p>Title: NewSpecimenAction Class>
 * <p>Description:	NewSpecimenAction initializes the fields in the New Specimen page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
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
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * NewSpecimenAction initializes the fields in the New Specimen page.
 * @author aniruddha_phadnis
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
        	
           	//List biohazardList = dao.retrieve(Biohazard.class.getName());
        	String selectColNames[] = {"systemIdentifier","name","type"}; 
        	List biohazardList = dao.retrieve(Biohazard.class.getName(),selectColNames);
        	Iterator iterator = biohazardList.iterator();
        	if(biohazardList!=null && biohazardList.size()>0)
        	{
	        	String [] bhIdArray =  new String[biohazardList.size()];
	        	String [] bhTypeArray =  new String[biohazardList.size()];
	        	String [] bhNameArray =  new String[biohazardList.size()];
	        	int i=0;
	        	
	        	while(iterator.hasNext())
	        	{
	        		//Biohazard hazard = (Biohazard)iterator.next();
	        		Object hazard[] = (Object[])iterator.next();
	        		bhIdArray[i] = String.valueOf(hazard[0]);//.getSystemIdentifier());
	        		bhNameArray[i] = (String)hazard[1];//.getName();
	        		bhTypeArray[i] = (String)hazard[2];//.getType();
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
        
        String [] specimenCollectionGroupArray = null;

     	List specimenCollectionList = dao.retrieve(SpecimenCollectionGroup.class.getName());
     	if(specimenCollectionList!=null && specimenCollectionList.size()>0)
    	{
    		Iterator it = specimenCollectionList.iterator();
    		specimenCollectionGroupArray = new String[specimenCollectionList.size() + 1];
    		specimenCollectionGroupArray[0] = Constants.SELECT_OPTION;
    		int i=1;
    		
    		while(it.hasNext())
    		{
    			SpecimenCollectionGroup scg = (SpecimenCollectionGroup)it.next();
    			specimenCollectionGroupArray[i] = String.valueOf(scg.getSystemIdentifier());
    			i++;
    		}
    	}
     	else
     	{
     		specimenCollectionGroupArray = new String[1];
    		specimenCollectionGroupArray[0] = Constants.SELECT_OPTION;
     	}
        
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
