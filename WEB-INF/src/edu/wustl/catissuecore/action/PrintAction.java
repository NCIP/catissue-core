
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.catissuecore.printserviceclient.LabelPrinterFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * This class is for Print Action
 * @author falguni_sachde
 */
public class PrintAction extends Action
{
    /**
     * Overrides the execute method of Action class.
     * 
     * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws IOException I/O exception
	 * @throws ServletException servlet exception
	 * @return value for ActionForward object
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {

    	String nextforwardTo = request.getParameter("nextForwardTo");
    	SessionDataBean objBean = (SessionDataBean) request.getSession().getAttribute("sessionData");
    	String strIpAddress = objBean.getIpAddress();
    	String strUserId = objBean.getUserId().toString();
    	gov.nih.nci.security.authorization.domainobjects.User objUser = null;
    	try 
    	{
    		objUser = SecurityManager.getInstance(this.getClass()).getUserById(strUserId);    			
    		HashMap forwardToPrintMap = (HashMap)request.getAttribute("forwardToPrintMap");
    		//SCG Label printing
    		if (forwardToPrintMap != null && forwardToPrintMap.size() >0 && forwardToPrintMap.get("specimenCollectionGroupId")!=null)
			{
    				String scgId = (String) forwardToPrintMap.get("specimenCollectionGroupId");
    				AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
    				SpecimenCollectionGroup objSCG =null;
        			boolean printStauts  =false;
    				try
    				{
    					dao.openSession(null);
    					List scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), "id", scgId);

    					if (scgList != null && !scgList.isEmpty())
    					{
   						objSCG = (SpecimenCollectionGroup) scgList.get(0);
    					}
    					LabelPrinter  labelPrinter= LabelPrinterFactory.getInstance("specimencollectiongroup");
    					printStauts = labelPrinter.printLabel(objSCG, strIpAddress, objUser);
        				
    				}
    				catch (DAOException exception)
    				{
    					throw exception;
    				}
    				finally
    				{
    					dao.commit();
    					dao.closeSession();
    				}
    				setStatusMessage(printStauts,request);
    				
    				
				}
				//For Specimen 
    			//Check for Specimen type of object.Retrieve object based on Id and call printerimpl class
		    	if (forwardToPrintMap != null && forwardToPrintMap.size() >0 && forwardToPrintMap.get("specimenId")!=null)
				{
					String specimenId = (String) forwardToPrintMap.get("specimenId");
					AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
    				Specimen objSpecimen = null;
    				boolean printStauts  =false;
    				try
    				{
    					dao.openSession(null);
    					List specimenList = dao.retrieve(Specimen.class.getName(), "id", new Long(specimenId));

    					if (specimenList != null && !specimenList.isEmpty())
    					{
    						objSpecimen = (Specimen) specimenList.get(0);
    					}
    					LabelPrinter  labelPrinter= LabelPrinterFactory.getInstance("specimen");
    		        	printStauts = labelPrinter.printLabel(objSpecimen, strIpAddress, objUser);
        				
    				}
    				catch (DAOException exception)
    				{
    					throw exception;
    				}
    				finally
    				{
    					dao.commit();
    					dao.closeSession();
    				}
    				
		        	
		        	
		        	setStatusMessage(printStauts,request);
				}
		    	if(forwardToPrintMap != null &&  forwardToPrintMap.size() >0 && request.getParameter("forwardTo")!=null &&( request.getParameter("forwardTo").equals("CPQueryPrintAliquot") || request.getParameter("forwardTo").equals("printAliquot")))
		    	{
		    	  
		    		List<AbstractDomainObject> listofAliquot = new ArrayList();
		    		HashMap aliquotMap = forwardToPrintMap;
		    		Iterator it =  aliquotMap.keySet().iterator();
		    		Specimen objSpecimen = null;
		    		boolean printStauts  =false;
		    		AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		    		try
		    		{  
		    			
		    			while (it.hasNext()) 
		    			{
		    			 	dao.openSession(null);
				    	    String  key = (String)it.next();
				    	    if(key.endsWith("_id"))
				    	        {
				    	        	String idValue = (String) aliquotMap.get(key);      	
				    	        	List specimenList = dao.retrieve(Specimen.class.getName(), "id", new Long(idValue));

				    	        	if (specimenList != null && !specimenList.isEmpty())
				    	        	{
				    	        		objSpecimen = (Specimen) specimenList.get(0);
				    	        	}
				    	        	listofAliquot.add(objSpecimen);
				    	        }
		    			  }     
		    				LabelPrinter  labelPrinter= LabelPrinterFactory.getInstance("specimen");
		    		    	printStauts = labelPrinter.printLabel(listofAliquot, strIpAddress, objUser);	
		    		}
		    		catch (DAOException exception)
		    		{
		    			throw exception;
		    		}
		    		finally
		    		{
		    			dao.commit();
		    			dao.closeSession();
		    		}
		    	        	
		        	
		        	setStatusMessage(printStauts,request);
		    		
		    	}
		    	//For multiple specimen page
		    	if(forwardToPrintMap != null &&  forwardToPrintMap.size() >0  && request.getAttribute("printMultiple")!=null  &&   request.getAttribute("printMultiple").equals("1"))
		    	{
		    		
	    			
		    		LinkedHashSet specimenDomainCollection =  (LinkedHashSet) forwardToPrintMap.get("printMultipleSpecimen");
		    		Iterator iterator = specimenDomainCollection.iterator();
		    		List<AbstractDomainObject> specimenList = new ArrayList();
		    		while (iterator.hasNext()) 
		    		  {
		    			Specimen objSpecimen = (Specimen) iterator.next();
		    			specimenList.add(objSpecimen);
//		    			
		    				    			
		    		 }
		    		LabelPrinter  labelPrinter= LabelPrinterFactory.getInstance("specimen");
			        boolean printStauts = labelPrinter.printLabel(specimenList, strIpAddress, objUser);
			    	setStatusMessage(printStauts,request);
			        
			        if(request.getAttribute("pageOf")!=null)
			        	nextforwardTo = request.getAttribute("pageOf").toString();
			        else
			        	nextforwardTo = Constants.SUCCESS;
		    	}
//		    	For Anti. specimen page
		    	if(forwardToPrintMap != null &&  forwardToPrintMap.size() >0  && request.getAttribute("AntiSpecimen")!=null  &&   request.getAttribute("AntiSpecimen").equals("1"))
		    	{
		    		
	    			
		    		HashSet specimenDomainCollection =  (HashSet) forwardToPrintMap.get("printAntiSpecimen");
		    		Iterator iterator = specimenDomainCollection.iterator();
		    		List<AbstractDomainObject> specimenList = new ArrayList();
		    		while (iterator.hasNext()) 
		    		  {
		    			Specimen objSpecimen = (Specimen) iterator.next();
		    			specimenList.add(objSpecimen);
//		    			
		    				    			
		    		 }
		    		LabelPrinter  labelPrinter= LabelPrinterFactory.getInstance("specimen");
			        boolean printStauts = labelPrinter.printLabel(specimenList, strIpAddress, objUser);
			    	setStatusMessage(printStauts,request);
			        
			       	nextforwardTo = "printAntiSuccess";
		    	}	    	
		    	
    	
    		}
    		catch (Exception e) 
    		{
    			//Any other exception
    			//e.printStackTrace();
        		ActionMessages messages =(ActionMessages) request.getAttribute(MESSAGE_KEY);
        		if(messages == null)
        		{
        			messages = new ActionMessages();		
        			
        		}
    			messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("print.failure"));
    			saveMessages(request,messages);
    		}
    	
        return mapping.findForward(nextforwardTo);
    }
   
    /**
     * @param printStauts
     * @param request
     */
    private void setStatusMessage(boolean printStauts, HttpServletRequest request) 
    {
    	if(printStauts)
		{
    		//printservice returns true ,Printed Successfully
    		ActionMessages messages =(ActionMessages) request.getAttribute(MESSAGE_KEY);
    		if(messages == null)
    		{
    			messages = new ActionMessages();		
    			
    		}
    		messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("print.success"));
			saveMessages(request,messages);
		}
    	else
    	{
    		
    		//If any case print service return false ,it means error while printing.
    		ActionMessages messages =(ActionMessages) request.getAttribute(MESSAGE_KEY);
    		if(messages == null)
    		{
    			messages = new ActionMessages();		
    			
    		}
			messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("print.failure"));
			saveMessages(request,messages);
    	}
		
	}

}