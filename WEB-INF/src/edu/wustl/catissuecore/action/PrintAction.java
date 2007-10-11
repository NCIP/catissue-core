
package edu.wustl.catissuecore.action;

import java.io.IOException;
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

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
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
    			//objUser = SecurityManager.getInstance(this.getClass()).getUserById(strUserId);
    			//HashMap forwardToPrintMap = (HashMap)request.getAttribute("forwardToPrintMap");
    			//Get old actionmessage object from request and append print message to it.
    			ActionMessages messages =(ActionMessages) request.getAttribute(MESSAGE_KEY);
    			messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("print.success"));
    			saveMessages(request,messages);
    			
		    	/*if (forwardToPrintMap != null)
				{
					String specimenCollectionGroupId = (String) forwardToPrintMap.get("specimenCollectionGroupId");
					SpecimenCollectionGroup objSCG = retriveSCG(specimenCollectionGroupId,request);
		        	LabelPrinterImpl labelPrinter = new LabelPrinterImpl();
		        	labelPrinter.printLabel(objSCG, strIpAddress, objUser);
				}*/
    	
    		}
    		catch (Exception e) {
			
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
    

}