package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


public class NewMultipleSpecimenAction extends DispatchAction
{

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showCommentsDialog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
//		String comments = (String) request.getSession().getAttribute("Comments");		
		request.setAttribute("output","init");
		
		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String comments = (String) multipleSpecimenMap.get(keyInSpecimenMap);
        
		Logger.out.debug("setting comments to " + comments);
		
		((NewSpecimenForm) form).setComments(comments);		
		return mapping.findForward("comments");
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward showMultipleSpecimen(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		return mapping.findForward("specimen");
	}
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward submitComments(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		String comments = ((NewSpecimenForm) form).getComments();
//		request.getSession().setAttribute("Comments",comments);
		
		request.setAttribute("output","success");
		
		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		multipleSpecimenMap.put(keyInSpecimenMap,comments);
		
		Logger.out.debug("User entered " + comments);
		return mapping.findForward("comments");
	}
	
	/**
	 * This method returns  multipleSpecimenMap from session if present
	 * if not it adds a new one. 
	 * 
	 * @param request
	 * @return
	 */
	private Map chkForMultipleSpecimenMap(HttpServletRequest request) {
		Map multipleSpecimenMap = (Map) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY);

		if (multipleSpecimenMap == null) {
	        Logger.out.debug("adding new multipleSpecimenMap to session");
	        multipleSpecimenMap = new HashMap();
	        request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY,multipleSpecimenMap);
		}
		
		return multipleSpecimenMap;
	}
	
	/**
	 * displays external identifer page populated with previously added information.
	 * 
	 */
	public ActionForward showExtenalIdentifierDialog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		request.setAttribute("output","init");
		
		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String comments = (String) multipleSpecimenMap.get(keyInSpecimenMap);
        
		
		
		((NewSpecimenForm) form).setComments(comments);		
		return mapping.findForward("externalIdentifier");
	}

}
