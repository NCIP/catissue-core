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
		Constants.USER_GUIDE = XMLPropertyHandler.getValue("userguide.link");
		Constants.TECHNICAL_GUIDE = XMLPropertyHandler.getValue("technicalguide.link");
		Constants.TRAINING_GUIDE = XMLPropertyHandler.getValue("trainingguide.link");
		Constants.UMLMODEL_GUIDE = XMLPropertyHandler.getValue("umlmodel.link");
		Constants.GFORGE_GUIDE = XMLPropertyHandler.getValue("gforge.link");
		Constants.KNOWLEDGECENTER_GUIDE = XMLPropertyHandler.getValue("knowledgecenter.link");
		
		request.setAttribute(Constants.USER_GUIDE_LINK, Constants.USER_GUIDE);
		request.setAttribute(Constants.TECHNICAL_GUIDE_LINK, Constants.TECHNICAL_GUIDE);
		request.setAttribute(Constants.TRAINING_GUIDE_LINK, Constants.TRAINING_GUIDE);
		request.setAttribute(Constants.UMLMODEL_GUIDE_LINK, Constants.UMLMODEL_GUIDE);
		request.setAttribute(Constants.GFORGE_GUIDE_LINK, Constants.GFORGE_GUIDE);
		request.setAttribute(Constants.KNOWLEDGECENTER_GUIDE_LINK, Constants.KNOWLEDGECENTER_GUIDE);
		
		return mapping.findForward(Constants.SUCCESS);
      }
}
