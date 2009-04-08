/**
 * <p>Title: BiohazardAction Class>
 * <p>Description:	This class initializes the fields of Biohazard.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

public class NewSpecimenEventParametersAction  extends SecureAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in Biohazard.jsp Page.
     * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws IOException I/O exception
	 * @throws ServletException servlet exception
	 * @return value for ActionForward object
     */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
//        //Gets the value of the operation parameter.
//        String operation = request.getParameter(Constants.OPERATION);
//        
//        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
//        request.setAttribute(Constants.OPERATION, operation);
        try
		{
        	IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
	    	
	    	String identifier = (String)request.getAttribute("specimenIdentifier");
	    	if(identifier == null)
	    		identifier = (String)request.getParameter("specimenIdentifier");
	    	
	    	Object object = bizLogic.retrieve(Specimen.class.getName(), new Long(identifier));
	    	
	    	if(object !=null)
	    	{
	    		Specimen specimen = (Specimen) object;
	    		
	    		//Setting Specimen Event Parameters' Grid            
	            Collection specimenEventCollection = specimen.getSpecimenEventCollection();
	            
	            if(specimenEventCollection != null)
	            {
	            	List gridData = new ArrayList();
	            	Iterator it = specimenEventCollection.iterator();
	            	//int i=1;
	            	
	            	while(it.hasNext())
	            	{
	            		List rowData = new ArrayList();
	            		SpecimenEventParameters eventParameters = (SpecimenEventParameters)it.next();
	            		            		
	            		if(eventParameters != null)
	            		{
	            			String [] events = EventsUtil.getEvent(eventParameters);
	            			rowData.add(String.valueOf(eventParameters.getId()));
	            			rowData.add(events[0]);//Event Name
	            			            			
	            			User user = eventParameters.getUser();
	            			rowData.add(user.getLastName() + ", " + user.getFirstName());
	            			rowData.add(edu.wustl.common.util.Utility.parseDateToString(eventParameters.getTimestamp(),CommonServiceLocator.getInstance().getDatePattern()));
	            			rowData.add(events[1]);//pageOf
	            			gridData.add(rowData);
	            		}
	            	}
	            	
	            	request.setAttribute(edu.wustl.simplequery.global.Constants.SPREADSHEET_DATA_LIST,gridData);
	            }
	    	}
	    	
	    	request.setAttribute(Constants.EVENT_PARAMETERS_LIST,Constants.EVENT_PARAMETERS);
		}
        catch(Exception e)
		{
        	Logger.out.error(e.getMessage(),e);
		}
    	
        return mapping.findForward((String)request.getParameter(Constants.PAGE_OF));
    }
}