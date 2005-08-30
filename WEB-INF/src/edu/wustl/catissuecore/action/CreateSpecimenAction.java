/**
 * <p>Title: CreateSpecimenAction Class>
 * <p>Description:	CreateSpecimenAction initializes the fields in the Create Specimen page.</p>
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

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CreateSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;


/**
 * CreateSpecimenAction initializes the fields in the Create Specimen page.
 * @author aniruddha_phadnis
 */
public class CreateSpecimenAction extends Action
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
        
        CreateSpecimenBizLogic dao = (CreateSpecimenBizLogic)BizLogicFactory.getBizLogic(Constants.CREATE_SPECIMEN_FORM_ID);
        CreateSpecimenForm createForm = (CreateSpecimenForm) form;
        
        try
		{
        	String [] parentSpecimenIdArray = null;

            List parentSpecimenList = dao.retrieve(Specimen.class.getName());
            
    	 	if(parentSpecimenList!=null && parentSpecimenList.size()>0)
    		{
    	 		parentSpecimenIdArray = new String[parentSpecimenList.size() + 1];
    	 		parentSpecimenIdArray[0] = Constants.SELECT_OPTION;
    	 		Iterator it = parentSpecimenList.iterator();
    			int i=1;
    			
    			while(it.hasNext())
    			{
    				Specimen specimen = (Specimen)it.next();
    				parentSpecimenIdArray[i] = String.valueOf(specimen.getSystemIdentifier());
    				i++;
    			}
    		}
    	 	else
    	 	{
    	 		parentSpecimenIdArray = new String[1];
    	 		parentSpecimenIdArray[0] = Constants.SELECT_OPTION;
    	 	}
    	 	
    	 	request.setAttribute(Constants.PARENT_SPECIMEN_ID_LIST,parentSpecimenIdArray);
    	 	
            //request.setAttribute(Constants.SPECIMEN_TYPE_LIST, Constants.SPECIMEN_TYPE_VALUES);
    	 	List specimenList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SPECIMEN_TYPE);
        	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenList);
            
            request.setAttribute(Constants.SPECIMEN_SUB_TYPE_LIST, Constants.SPECIMEN_SUB_TYPE_VALUES);
            
            request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, Constants.BIOHAZARD_TYPE_ARRAY);
		}
        catch(Exception e)
		{
        	e.printStackTrace();
		}
        
        return mapping.findForward(Constants.SUCCESS);
    }

}
