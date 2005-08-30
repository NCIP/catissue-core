/**
 * <p>Title: CollectionProtocolAction Class>
 * <p>Description:	This class initializes the fields in the User Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 22, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * @author gautam_shetty
 */
public class SpecimenProtocolAction  extends SecureAction
{

    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in User Add/Edit webpage.
     * */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION, operation);
  
        try
		{
        	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
        	Collection coll =  userBizLogic.getUsers(Constants.ACTIVITY_STATUS_ACTIVE);
        	request.setAttribute(Constants.USERLIST, coll);
        	
	    	String [] specimenClassArry = {Constants.SELECT_OPTION, "Fluid Specimen","Tissue Specimen","Cell Specimen","Molecular Specimen"};
	    	String [] specimenClassIdArry = {Constants.SELECT_OPTION, "Fluid","Tissue","Cell","Molecular"};
	    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassArry);
	    	request.setAttribute(Constants.SPECIMEN_CLASS_ID_LIST, specimenClassIdArry);

	    	String [] specimenTypeArry = {Constants.SELECT_OPTION,"Blood","Cerum"};
	    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeArry);
	    	
	    	request.setAttribute(Constants.TISSUE_SITE_LIST, Constants.TISSUE_SITE_ARRAY);
	    		
	    	String [] pathologyStatusArry = {Constants.SELECT_OPTION,"Primary Tumor","Metastatic Node","Non-Malignant Tissue"};
	    	request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologyStatusArry);
		}
        catch(Exception e)
		{
        	e.printStackTrace();
		}
        
        return mapping.findForward(Constants.SUCCESS);
    }
}