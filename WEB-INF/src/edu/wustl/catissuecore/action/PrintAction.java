
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
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.catissuecore.printserviceclient.LabelPrinterFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.dbManager.DAOException;
import gov.nih.nci.security.authorization.domainobjects.User;


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
		String printerType = request.getParameter("printerType");
		String printerLocation=request.getParameter("printerLocation");
		if(printerType==null || printerType.equals(""))
		{
			printerType = " ";
		}
		if(printerLocation==null || printerLocation.equals(""))
		{
			printerLocation = " ";
		}
		SessionDataBean objBean = (SessionDataBean) request.getSession().getAttribute("sessionData");
		String strIpAddress = objBean.getIpAddress();
		try 
		{
			gov.nih.nci.security.authorization.domainobjects.User objUser = getUserObject(request,objBean);
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
					Object object = dao.retrieve(SpecimenCollectionGroup.class.getName(), new Long(scgId));

					if (object != null)
					{
						objSCG = (SpecimenCollectionGroup) object;
					}

					LabelPrinter  labelPrinter= LabelPrinterFactory.getInstance("specimencollectiongroup");
					printStauts = labelPrinter.printLabel(objSCG, strIpAddress, objUser,printerType,printerLocation);

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
				boolean printStauts  =false;
				try
				{
					dao.openSession(null);
					Specimen objSpecimen = (Specimen)dao.retrieve(Specimen.class.getName(), new Long(specimenId));

					LabelPrinter  labelPrinter= LabelPrinterFactory.getInstance("specimen");
					printStauts = labelPrinter.printLabel(objSpecimen, strIpAddress, objUser,printerType,printerLocation);

				}
				catch (DAOException exception)
				{
					throw exception;
				}
				finally
				{
					dao.closeSession();
				}
				setStatusMessage(printStauts,request);
			}

			//nextforwardTo = printAliquotLabel(form, request, nextforwardTo,objBean);
			//nextforwardTo - coming null now CPQueryPrintSpecimenEdit
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
				}
				LabelPrinter  labelPrinter= LabelPrinterFactory.getInstance("specimen");
				boolean printStauts = labelPrinter.printLabel(specimenList, strIpAddress, objUser,printerType,printerLocation);
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
				}
				LabelPrinter  labelPrinter= LabelPrinterFactory.getInstance("specimen");
				boolean printStauts = labelPrinter.printLabel(specimenList, strIpAddress, objUser,printerType,printerLocation);
				setStatusMessage(printStauts,request);

				nextforwardTo = "printAntiSuccess";
			}	
			//added for Storage Container Printing 
			if(forwardToPrintMap != null &&  forwardToPrintMap.size() >0 && forwardToPrintMap.get("StorageContainerObjID")!= null )
			{
				AbstractDAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
				boolean printStauts = false;		    		
				List similarContainerList = (List)request.getAttribute("similarContainerList");
				LabelPrinter  labelPrinter= LabelPrinterFactory.getInstance("storagecontainer");
				if(similarContainerList==null || similarContainerList.size()==0)
				{
					String containerId = (String) forwardToPrintMap.get("StorageContainerObjID");
					if(containerId!=null)
					{
						NameValueBean bean = new NameValueBean(containerId,containerId);
						similarContainerList = new ArrayList();
						similarContainerList.add(bean);
					} 
				}
				try
				{
					dao.openSession(null);
					List<AbstractDomainObject> containerList = new ArrayList<AbstractDomainObject>();
					for(int i=0;i<similarContainerList.size();i++)
					{
						NameValueBean bean = (NameValueBean) similarContainerList.get(i);
						String value = bean.getValue();
						Object object = dao.retrieve(StorageContainer.class.getName(),new Long(value));
						if (object != null)
						{
							StorageContainer containerObj = (StorageContainer) object;
							containerList.add(containerObj);
						}
					}
					printStauts = labelPrinter.printLabel(containerList, strIpAddress, objUser,printerType,printerLocation);
				}
				catch (DAOException exception)
				{
					throw exception;
				}
				finally
				{
					dao.closeSession();
				}
				setStatusMessage(printStauts,request);
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
	 * @param form
	 * @param request
	 * @param nextforwardTo
	 * @param strIpAddress
	 * @param objUser
	 * @return
	 * @throws Exception
	 */
	public String printAliquotLabel(ActionForm form, HttpServletRequest request,
			String nextforwardTo, SessionDataBean objBean) throws Exception
	{
		gov.nih.nci.security.authorization.domainobjects.User objUser = null;
		try
		{
			objUser = getUserObject(request, objBean);
		}
		catch (SMException e)
		{
			e.printStackTrace();
			throw new SMException("Error while Creating User Object");
		}   
		
		List<AbstractDomainObject> listofAliquot = (List<AbstractDomainObject>)request.getAttribute("specimenList");
		boolean printStauts  =false;
		LabelPrinter labelPrinter = null;
		try
		{
			labelPrinter = LabelPrinterFactory.getInstance("specimen");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("Error Printing label");
		}
		if(listofAliquot!=null)
		{
			String printerType = request.getParameter("printerType");
	    	String printerLocation=request.getParameter("printerLocation");
	  	    printStauts = labelPrinter.printLabel(listofAliquot, objBean.getIpAddress(), objUser,printerType,printerLocation);
		}
		setStatusMessage(printStauts,request);
	   	nextforwardTo = (String)request.getParameter("forwardTo");
		
		return nextforwardTo;
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
    
    private User getUserObject(HttpServletRequest request,SessionDataBean objBean) throws SMException
    {
    	String strUserId = objBean.getUserId().toString();
    	gov.nih.nci.security.authorization.domainobjects.User objUser  = 
    		SecurityManager.getInstance(this.getClass()).getUserById(strUserId); 
    	return objUser;
    }

}