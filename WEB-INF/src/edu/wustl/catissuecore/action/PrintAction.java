
package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.HashMap;
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
import edu.wustl.catissuecore.print.LabelPrinterImpl;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.XMLPropertyHandler;
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
    	String printerClassName = XMLPropertyHandler.getValue("labelPrinterClass");
    	if(printerClassName!= null)
    	{
    		
    		SessionDataBean objBean = (SessionDataBean) request.getSession().getAttribute("sessionData");
    		String strIpAddress = objBean.getIpAddress();
    		String strUserId = objBean.getUserId().toString();
    		gov.nih.nci.security.authorization.domainobjects.User objUser = null;
    		try 
    		{
    			gov.nih.nci.security.authorization.domainobjects.User user = SecurityManager.getInstance(this.getClass()).getUserById(strUserId);    			
    			HashMap forwardToPrintMap = (HashMap)request.getAttribute("forwardToPrintMap");
    			
    			//Check for Specimen type of object.Retrieve object based on Id and call printerimpl class
		    	if (forwardToPrintMap != null && forwardToPrintMap.get("specimenId")!=null)
				{
					String specimenId = (String) forwardToPrintMap.get("specimenId");
					Specimen objSpecimen = retriveSpecimen(specimenId,request);
		        	LabelPrinterImpl labelPrinter = new LabelPrinterImpl();
		        	boolean printStauts = labelPrinter.printLabel(objSpecimen, strIpAddress, objUser);
		        	
		        	if(printStauts)
	    			{
		        		//printservice returns true ,Printed Successfully
		        		ActionMessages messages =(ActionMessages) request.getAttribute(MESSAGE_KEY);
		        		messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("print.success"));
		    			saveMessages(request,messages);
	    			}
		        	else
		        	{
		        		
		        		//If any case print service return false ,it means error while printing.
		        		ActionMessages messages =(ActionMessages) request.getAttribute(MESSAGE_KEY);
		    			messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("print.failure"));
		    			saveMessages(request,messages);
		        	}
	    			
				}
    	
    		}
    		catch (Exception e) 
    		{
    			//Any other exception
    			e.printStackTrace();
        		ActionMessages messages =(ActionMessages) request.getAttribute(MESSAGE_KEY);
    			messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("print.failure"));
    			saveMessages(request,messages);
    		}
    	}
    	else
    	{
    		//Get old actionmessage object from request and append print message to it.
    		ActionMessages messages =(ActionMessages) request.getAttribute(MESSAGE_KEY);
			messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("print.failure"));
			saveMessages(request,messages);
    	}
    	
        return mapping.findForward(nextforwardTo);
    }
   
    /**
     * @param scgId
     * @param request
     * @return
     * @throws DAOException
     */
    public SpecimenCollectionGroup  retriveSCG(String scgId ,HttpServletRequest request)  	throws DAOException
	{
    	try 
		{
    		DAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
        	SessionDataBean sessionDataBean = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
    		((HibernateDAO)dao).openSession(sessionDataBean);
		
    		if(scgId!= null && !scgId.equals(""))
    		{
    			List scgList = null;    			
				scgList = dao.retrieve(SpecimenCollectionGroup.class.getName(), "id", new Long(scgId));
				((HibernateDAO)dao).closeSession();			
				if(scgList!=null && !scgList.isEmpty())
				{
					SpecimenCollectionGroup objSCG  = (SpecimenCollectionGroup)scgList.get(0);
					return objSCG;
				}
    		}
    	
		}
    	catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
    	return null;
	}
    
    public Specimen  retriveSpecimen(String specimenId ,HttpServletRequest request)  	throws DAOException
	{
    	try 
		{
    		DAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
        	SessionDataBean sessionDataBean = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
    		((HibernateDAO)dao).openSession(sessionDataBean);
		
    		if(specimenId!= null && !specimenId.equals(""))
    		{
    			List specimenList = null;    			
				specimenList = dao.retrieve(Specimen.class.getName(), "id", new Long(specimenId));
				((HibernateDAO)dao).closeSession();			
				if(specimenList!=null && !specimenList.isEmpty())
				{
					Specimen objSpecimen  = (Specimen)specimenList.get(0);
					return objSpecimen;
				}
    		}
    	
		}
    	catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
    	return null;
	}
}