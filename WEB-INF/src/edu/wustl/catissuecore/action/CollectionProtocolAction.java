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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * @author gautam_shetty
 */
public class CollectionProtocolAction extends Action
{

    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in User Add/Edit webpage.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION, operation);
  
        //String userList[] = {"Rakesh","Mark","Kapil","Srikant","Mandar"};
        
        try
		{
	    	String [] userArray = {Constants.SELECT_OPTION, "Phadnis, Aniruddha","Kaveeshwar, Kapil","ABC","BBC","CCC"};
	    	String [] userIdArray = {"-1","1","2","3","4","5"};
	    	
	    	request.setAttribute(Constants.USERLIST, userArray);
	    	request.setAttribute(Constants.USERIDLIST, userIdArray);
	    	
	    	
	    	String [] clinicalStatusArry = {Constants.SELECT_OPTION, "Pre-Opt","Post-Opt"};
	    	request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusArry);
	    	
	    	String [] specimenClassArry = {Constants.SELECT_OPTION, "Fluid Specimen","Tissue Specimen","Cell Specimen","Molecular Specimen"};
	    	String [] specimenClassIdArry = {Constants.SELECT_OPTION, "Fluid","Tissue","Cell","Molecular"};
	    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassArry);
	    	request.setAttribute(Constants.SPECIMEN_CLASS_ID_LIST, specimenClassIdArry);

	    	String [] specimenTypeArry = {Constants.SELECT_OPTION,"Blood","Cerum"};
	    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeArry);
	    	
	    	String [] tissueSiteArry = {Constants.SELECT_OPTION,"Adrenal-Cortex","Adrenal-Medulla","Adrenal-NOS"};
	    	request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteArry);
	    		
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