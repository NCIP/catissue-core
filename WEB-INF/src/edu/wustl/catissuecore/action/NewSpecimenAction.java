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
import edu.wustl.common.cde.CDEManager;


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
        	request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, Constants.BIOHAZARD_TYPE_ARRAY);
        	
           	//List biohazardList = dao.retrieve(Biohazard.class.getName());
        	String selectColNames[] = {"systemIdentifier","name","type"}; 
        	List biohazardList = dao.retrieve(Biohazard.class.getName(),selectColNames);
        	Iterator iterator = biohazardList.iterator();
        	
        	String [] bhIdArray =  null;
        	String [] bhTypeArray = null;
        	String [] bhNameArray = null;
        	
        	if(biohazardList!=null && biohazardList.size()>0)
        	{
	        	bhIdArray =  new String[biohazardList.size() + 1];
	        	bhTypeArray =  new String[biohazardList.size() + 1];
	        	bhNameArray =  new String[biohazardList.size() + 1];
	        	
	        	bhIdArray[0] = "-1";
	        	bhTypeArray[0] = "";
	        	bhNameArray[0] = Constants.SELECT_OPTION;
	        	
	        	int i=1;
	        	
	        	while(iterator.hasNext())
	        	{
	        		Object hazard[] = (Object[])iterator.next();
	        		bhIdArray[i] = String.valueOf(hazard[0]);
	        		bhNameArray[i] = (String)hazard[1];
	        		bhTypeArray[i] = (String)hazard[2];
	        		i++;
	        	}
        	}
        	else
        	{
        		bhIdArray =  new String[1];
	        	bhTypeArray =  new String[1];
	        	bhNameArray =  new String[1];
	        	
	        	bhIdArray[0] = "-1";
	        	bhTypeArray[0] = Constants.SELECT_OPTION;
	        	bhNameArray[0] = Constants.SELECT_OPTION;;
        	}
        	
        	request.setAttribute(Constants.BIOHAZARD_NAME_LIST, bhNameArray);
        	request.setAttribute(Constants.BIOHAZARD_ID_LIST, bhIdArray);
        	request.setAttribute(Constants.BIOHAZARD_TYPES_LIST, bhTypeArray);
        	
        	//Setting Secimen Collection Group
			String sourceObjectName = SpecimenCollectionGroup.class.getName();
			String[] displayNameFields = {"systemIdentifier"};
			String valueField = "systemIdentifier";
	
			List specimenList = dao.getList(sourceObjectName, displayNameFields, valueField);
			request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_LIST, specimenList);
		}
        catch(Exception e)
		{
        	e.printStackTrace();
		}
        
        //request.setAttribute(Constants.SPECIMEN_TYPE_LIST, Constants.SPECIMEN_TYPE_VALUES);
        List specimenList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SPECIMEN_TYPE);
    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenList);
        
        request.setAttribute(Constants.SPECIMEN_SUB_TYPE_LIST, Constants.SPECIMEN_SUB_TYPE_VALUES);
        
        //request.setAttribute(Constants.TISSUE_SITE_LIST,Constants.TISSUE_SITE_ARRAY);
    	List tissueSiteList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SITE);
    	request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);

        
        //request.setAttribute(Constants.TISSUE_SIDE_LIST,Constants.TISSUE_SIDE_VALUES);
    	List tissueSideList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SIDE);
    	request.setAttribute(Constants.TISSUE_SIDE_LIST, tissueSideList);
        
        //request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, Constants.PATHOLOGICAL_STATUS_VALUES);
    	List pathologicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_PATHOLOGICAL_STATUS);
    	request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologicalStatusList);
        
        request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, Constants.BIOHAZARD_TYPE_ARRAY);
        
        return mapping.findForward(Constants.SUCCESS);
    }

}
