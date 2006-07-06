/**
 * <p>Title: QuickEventsSpecimenSearchAction Class</p>
 * <p>Description:  This class validates the entries from the Quickevents webpage and directs the flow accordingly. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 3, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.QuickEventsForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;



/**
 * <p>Title: QuickEventsSpecimenSearchAction Class</p>
 * <p>Description:  This class validates the entries from the Quickevents webpage and directs the flow accordingly. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 3, 2006
 */

public class QuickEventsSpecimenSearchAction extends BaseAction {
    /**
     * Overrides the execute method of Action class.
     * Redirects the user to either the events page or the QuickEvents webpage based on the condition.
     * */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
  	 	QuickEventsForm qEForm = (QuickEventsForm)form;
  	 	Logger.out.debug(qEForm.getSpecimenEventParameter()   );
  	 	String pageOf = Constants.SUCCESS;
  	 	
  	 	pageOf = validate(request ,qEForm );
  	 	
  	 	if(pageOf.equals(Constants.SUCCESS ) )
  	 	{
  	 		DefaultBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
  	 		String specimenFound="0";
  	 		String errorString="";
  	        if(qEForm.getCheckedButton().equals("1" ))
  	        {
  	        	String specimenID = qEForm.getSpecimenID();
  	        	specimenFound = isExistingSpecimen(Constants.SYSTEM_IDENTIFIER,specimenID ,bizLogic  );
  	        	errorString ="quickEvents.specimenID";
  	        }
  	        else if(qEForm.getCheckedButton().equals("2" ) )
  	        {
  	        	String barCode = qEForm.getBarCode();
  	        	specimenFound = isExistingSpecimen("barcode",barCode ,bizLogic  );
  	        	errorString = "quickEvents.barcode";
  	        }
  	        
  	        if(!specimenFound.equalsIgnoreCase("0" ))
  	        {
  	        	request.setAttribute(Constants.SPECIMEN_ID, specimenFound );
  	        	
  	        	String selectedEvent = qEForm.getSpecimenEventParameter();  
  	        	request.setAttribute(Constants.EVENT_SELECTED, selectedEvent );
  	        	
  	        	request.setAttribute("isQuickEvent","true");  
  	        	pageOf = Constants.SUCCESS;
  	        }
  	        else
  	        {
	  	  		ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
	  			if(errors == null)
	  			{
	  				errors = new ActionErrors();
	  			}
	  			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("quickEvents.specimen.notExists",errorString));
	  			saveErrors(request,errors);

	  			pageOf = Constants.FAILURE ;
  	        }
  	 	}
  	 	
        return mapping.findForward(pageOf );
    }
	
	
	//This method validates the formbean.
	private String validate(HttpServletRequest request, QuickEventsForm form)
	{
        Validator validator = new Validator();
		ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
		
		String pageOf = Constants.SUCCESS;
		
		if(errors == null)
		{
			errors = new ActionErrors();
		}
                 
        if (!validator.isValidOption(form.getSpecimenEventParameter()))
        {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("quickEvents.eventparameters")));
        	pageOf = Constants.FAILURE;  
        }
        if(form.getCheckedButton().equals("1" ) && !validator.isValidOption(form.getSpecimenID()) )
        {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("quickEvents.specimenID")));
        	pageOf = Constants.FAILURE;  
        }
        if(form.getCheckedButton().equals("2" ) && validator.isEmpty(form.getBarCode()) )
        {
        	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("quickEvents.barcode")));
        	pageOf = Constants.FAILURE;  
        }
        
        saveErrors(request,errors);
        return pageOf;
	}
	
	
	 private String isExistingSpecimen(String sourceObject, String value, DefaultBizLogic bizlogic) throws Exception
	 {
	 	String returnValue= "0";
	 	
	 	String sourceObjectName = Specimen.class.getName();
        String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER  };
        String[] whereColumnName = {sourceObject}; //"storageContainer."+Constants.SYSTEM_IDENTIFIER
        String[] whereColumnCondition = {"="};
        Object[] whereColumnValue = {value};
        String joinCondition = null;

        List list = bizlogic.retrieve(sourceObjectName, selectColumnName,
                whereColumnName, whereColumnCondition,
                whereColumnValue, joinCondition);

        Logger.out.debug("MD 04-July-06 : - ><><>< "+ sourceObject+ " : "+value );
        String specimenID="0";
        if (!list.isEmpty())
        {	
        	Object obj = list.get(0 );
        	Logger.out.debug("04-July-06 :- "+obj.getClass().getName());
        	Long specimen = (Long)obj;
        	specimenID = specimen.toString() ;  

        	returnValue = specimenID;
        }
        else
        {
        	returnValue ="0";
        }
        Logger.out.debug("MD 04-July-06 : - ><><>< "+ sourceObject+ " : "+value + " Found SpecimenID:  " + returnValue );
        
        return returnValue ;
	 }



}
