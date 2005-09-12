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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.logger.Logger;


/**
 * NewSpecimenAction initializes the fields in the New Specimen page.
 * @author aniruddha_phadnis
 */
public class NewSpecimenAction  extends SecureAction
{   
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String pageOf = request.getParameter(Constants.PAGEOF);
        request.setAttribute(Constants.PAGEOF,pageOf);

        NewSpecimenForm specimenForm = (NewSpecimenForm)form;
        
        //FIXME
        //if(!operation.equals(Constants.ADD))
        {
        	request.setAttribute(Constants.EVENT_PARAMETERS_LIST,Constants.EVENT_PARAMETERS);
        	request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,Constants.EVENT_PARAMETERS_COLUMNS);
        	request.setAttribute(Constants.SPREADSHEET_DATA_LIST, specimenForm.getGridData());
        }
        
        NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic)BizLogicFactory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
        
        try
		{	
        	if(specimenForm.isParentPresent())
        	{
        		String [] fields = {Constants.SYSTEM_IDENTIFIER};
                List parentSpecimenList = bizLogic.getList(Specimen.class.getName(), fields, Constants.SYSTEM_IDENTIFIER); 	 	
        	 	request.setAttribute(Constants.PARENT_SPECIMEN_ID_LIST, parentSpecimenList);
        	}
        	
        	String [] bhIdArray =  {"-1"};
        	String [] bhTypeArray =  {Constants.SELECT_OPTION};
        	String [] bhNameArray =  {Constants.SELECT_OPTION};
        	
        	String selectColNames[] = {Constants.SYSTEM_IDENTIFIER,"name","type"}; 
        	List biohazardList = bizLogic.retrieve(Biohazard.class.getName(), selectColNames);
        	Iterator iterator = biohazardList.iterator();
        	if(biohazardList!=null && !biohazardList.isEmpty())
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
        	
        	request.setAttribute(Constants.BIOHAZARD_NAME_LIST, bhNameArray);
        	request.setAttribute(Constants.BIOHAZARD_ID_LIST, bhIdArray);
        	request.setAttribute(Constants.BIOHAZARD_TYPES_LIST, bhTypeArray);
        	
        	//Setting Secimen Collection Group
			String sourceObjectName = SpecimenCollectionGroup.class.getName();
			String[] displayNameFields = {Constants.SYSTEM_IDENTIFIER};
			String valueField = Constants.SYSTEM_IDENTIFIER;
	
			List specimenList = bizLogic.getList(sourceObjectName, displayNameFields, valueField);
			request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_LIST, specimenList);
		}
        catch(Exception e)
		{
        	Logger.out.error(e.getMessage(),e);
        	return mapping.findForward(Constants.FAILURE);
		}
        
        List specimenClassList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SPECIMEN_CLASS);
    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
    	
    	List specimenTypeList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SPECIMEN_TYPE);
    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
        
    	List tissueSiteList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SITE);
    	request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);

    	List tissueSideList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_TISSUE_SIDE);
    	request.setAttribute(Constants.TISSUE_SIDE_LIST, tissueSideList);
        
    	List pathologicalStatusList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_PATHOLOGICAL_STATUS);
    	request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologicalStatusList);
        
    	List biohazardList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_BIOHAZARD);
    	request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);
        
    	return mapping.findForward(pageOf);
    }
}