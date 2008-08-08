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
		String userGuideLink = XMLPropertyHandler.getValue("userguide.link");
		String technicalGuideLink = XMLPropertyHandler.getValue("technicalguide.link");
		String trainingGuideLink = XMLPropertyHandler.getValue("trainingguide.link");
		String umlmodelGuideLink = XMLPropertyHandler.getValue("umlmodelguide.link");
		String gforeGuideLink = XMLPropertyHandler.getValue("gforge.link");
		String knowledgeCenterGuideLink = XMLPropertyHandler.getValue("knowledgecenter.link");
		
		request.setAttribute("UserGuide", userGuideLink);
		request.setAttribute("TechnicalGuide", technicalGuideLink);
		request.setAttribute("TrainingGuide", trainingGuideLink);
		request.setAttribute("UmlModelGuide", umlmodelGuideLink);
		request.setAttribute("GforgeGuide", gforeGuideLink);
		request.setAttribute("KnowledgeCenterGuide", knowledgeCenterGuideLink);
		
		return mapping.findForward(Constants.SUCCESS);
      }
}
