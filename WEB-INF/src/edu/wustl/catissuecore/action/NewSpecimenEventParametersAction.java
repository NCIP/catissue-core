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
import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.EventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.ProcedureEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.domain.ThawEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.logger.Logger;

public class NewSpecimenEventParametersAction  extends SecureAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in Biohazard.jsp Page.
     * */
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
	    	
	    	List specimenList = bizLogic.retrieve(Specimen.class.getName(),Constants.SYSTEM_IDENTIFIER,identifier);
	    	
	    	if(specimenList!=null && specimenList.size() != 0)
	    	{
	    		Specimen specimen = (Specimen)specimenList.get(0);
	    		
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
	            		EventParameters eventParameters = (EventParameters)it.next();
	            		            		
	            		if(eventParameters != null)
	            		{
	            			String [] events = getEvent(eventParameters);
	            			rowData.add(String.valueOf(eventParameters.getId()));
	            			rowData.add(events[0]);//Event Name
	            			            			
	            			User user = eventParameters.getUser();
	            			rowData.add(user.getLastName() + ", " + user.getFirstName());
	            			rowData.add(Utility.parseDateToString(eventParameters.getTimestamp(),Constants.DATE_PATTERN_MM_DD_YYYY));
	            			rowData.add(events[1]);//pageOf
	            			gridData.add(rowData);
	            		}
	            	}
	            	
	            	request.setAttribute(Constants.SPREADSHEET_DATA_LIST,gridData);
	            }
	    	}
	    	
	    	request.setAttribute(Constants.EVENT_PARAMETERS_LIST,Constants.EVENT_PARAMETERS);
		}
        catch(Exception e)
		{
        	Logger.out.error(e.getMessage(),e);
		}
    	
        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
    
    private String[] getEvent(EventParameters eventParameters)
	{
		String [] events = new String[2];
				
		if(eventParameters instanceof CellSpecimenReviewParameters)
		{
			events[0] = "Cell Specimen Review";
			events[1] = "pageOfCellSpecimenReviewParameters";
		}
		else if(eventParameters instanceof CheckInCheckOutEventParameter)
		{
			events[0] = "Check In Check Out";
			events[1] = "pageOfCheckInCheckOutEventParameters";
		}
		else if(eventParameters instanceof CollectionEventParameters)
		{
			events[0] = "Collection";
			events[1] = "pageOfCollectionEventParameters";
		}
		else if(eventParameters instanceof DisposalEventParameters)
		{
			events[0] = "Disposal";
			events[1] = "pageOfDisposalEventParameters";
		}
		else if(eventParameters instanceof EmbeddedEventParameters)
		{
			events[0] = "Embedded";
			events[1] = "pageOfEmbeddedEventParameters";
		}
		else if(eventParameters instanceof FixedEventParameters)
		{
			events[0] = "Fixed";
			events[1] = "pageOfFixedEventParameters";
		}
		else if(eventParameters instanceof FluidSpecimenReviewEventParameters)
		{
			events[0] = "Fluid Specimen Review";
			events[1] = "pageOfFluidSpecimenReviewParameters";
		}
		else if(eventParameters instanceof FrozenEventParameters)
		{
			events[0] = "Frozen";
			events[1] = "pageOfFrozenEventParameters";
		}
		else if(eventParameters instanceof MolecularSpecimenReviewParameters)
		{
			events[0] = "Molecular Specimen Review";
			events[1] = "pageOfMolecularSpecimenReviewParameters";
		}
		else if(eventParameters instanceof ProcedureEventParameters)
		{
			events[0] = "Procedure Event";
			events[1] = "pageOfProcedureEventParameters";
		}
		else if(eventParameters instanceof ReceivedEventParameters)
		{
			events[0] = "Received Event";
			events[1] = "pageOfReceivedEventParameters";
		}
		else if(eventParameters instanceof SpunEventParameters)
		{
			events[0] = "Spun";
			events[1] = "pageOfSpunEventParameters";
		}
		else if(eventParameters instanceof ThawEventParameters)
		{
			events[0] = "Thaw";
			events[1] = "pageOfThawEventParameters";
		}
		else if(eventParameters instanceof TissueSpecimenReviewEventParameters)
		{
			events[0] = "Tissue Specimen Review";
			events[1] = "pageOfTissueSpecimenReviewParameters";
		}
		else if(eventParameters instanceof TransferEventParameters)
		{
			events[0] = "Transfer";
			events[1] = "pageOfTransferEventParameters";
		}
	
		return events;
	}
    
}