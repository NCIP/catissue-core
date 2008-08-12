package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.wustl.catissuecore.util.global.Constants;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.cde.CDEConConfig;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class sets the link values for the Help.jsp page tab/icons
 * @author sagar_baldwa
 */
public class RedirectToHelpAction extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException, DAOException
	{
		//Retrives link values from caTissueCore_Properties.xml file
		request.setAttribute(Constants.USER_GUIDE_LINK_PROPERTY, XMLPropertyHandler.getValue(Constants.USER_GUIDE_LINK_PROPERTY));
		request.setAttribute(Constants.TECHNICAL_GUIDE_LINK_PROPERTY, XMLPropertyHandler.getValue(Constants.TECHNICAL_GUIDE_LINK_PROPERTY));
		request.setAttribute(Constants.TRAINING_GUIDE_LINK_PROPERTY, XMLPropertyHandler.getValue(Constants.TRAINING_GUIDE_LINK_PROPERTY));
		request.setAttribute(Constants.UML_MODEL_LINK_PROPERTY, XMLPropertyHandler.getValue(Constants.UML_MODEL_LINK_PROPERTY));
		request.setAttribute(Constants.GFORGE_GUIDE_LINK_PROPERTY, XMLPropertyHandler.getValue(Constants.GFORGE_GUIDE_LINK_PROPERTY));
		request.setAttribute(Constants.KNOWLEDGE_CENTER_LINK_PROPERTY, XMLPropertyHandler.getValue(Constants.KNOWLEDGE_CENTER_LINK_PROPERTY));
		
		return mapping.findForward(Constants.SUCCESS);
      }
}
