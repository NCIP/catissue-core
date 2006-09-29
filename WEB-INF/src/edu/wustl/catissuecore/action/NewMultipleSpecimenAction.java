
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
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
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//		String comments = (String) request.getSession().getAttribute("Comments");		
		request.setAttribute("output", "init");

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
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
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
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		String comments = ((NewSpecimenForm) form).getComments();
		//		request.getSession().setAttribute("Comments",comments);

		request.setAttribute("output", "success");

		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		multipleSpecimenMap.put(keyInSpecimenMap, comments);

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
	private Map chkForMultipleSpecimenMap(HttpServletRequest request)
	{
		Map multipleSpecimenMap = (Map) request.getSession().getAttribute(
				Constants.MULTIPLE_SPECIMEN_MAP_KEY);

		if (multipleSpecimenMap == null)
		{
			Logger.out.debug("adding new multipleSpecimenMap to session");
			multipleSpecimenMap = new HashMap();
			request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY,
					multipleSpecimenMap);
		}

		return multipleSpecimenMap;
	}

	/**
	 * displays external identifer page populated with previously added information.
	 * 
	 */
	public ActionForward showExtenalIdentifierDialog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		request.setAttribute("type",Constants.EXTERNALIDENTIFIER_TYPE);
		request.setAttribute("output", "init");

		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String keyForCount = keyInSpecimenMap + Constants.APPEND_COUNT;
		Map externalIdentifiers = (Map) multipleSpecimenMap.get(keyInSpecimenMap);
		Integer externalIdCnt = (Integer) multipleSpecimenMap.get(keyForCount);
		if (externalIdentifiers != null)
		{
			((NewSpecimenForm) form).setExternalIdentifier(externalIdentifiers);
			if (externalIdCnt != null)
				((NewSpecimenForm) form).setExIdCounter(externalIdCnt.intValue());
		}
		return mapping.findForward("externalIdentifier");
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward submitExternalIdentifiers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		Map externalIdentifiers = ((NewSpecimenForm) form).getExternalIdentifier();
		int externalIdCnt = ((NewSpecimenForm) form).getExIdCounter();
		//		request.getSession().setAttribute("Comments",comments);

		request.setAttribute("output", "success");

		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String keyForCount = keyInSpecimenMap + Constants.APPEND_COUNT;
		multipleSpecimenMap.put(keyInSpecimenMap, externalIdentifiers);
		multipleSpecimenMap.put(keyForCount, new Integer(externalIdCnt));

		return mapping.findForward("externalIdentifier");
	}

	public ActionForward deleteExternalIdentifiers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		request.setAttribute("output", "init");
		request.setAttribute("type",Constants.EXTERNALIDENTIFIER_TYPE);
		String button = request.getParameter("button");
		Map externalIdentifiers = ((NewSpecimenForm) form).getExternalIdentifier();

		if (button != null)
		{
			if (button.equals("deleteExId"))
			{
				List key = new ArrayList();
				key.add("ExternalIdentifier:i_name");
				key.add("ExternalIdentifier:i_value");
				MapDataParser.deleteRow(key, externalIdentifiers, request.getParameter("status"));
			}
		}

		return mapping.findForward("externalIdentifier");

	}
	

	
	/**
	 * displays external identifer page populated with previously added information.
	 * 
	 */
	public ActionForward showBioHazardDialog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		request.setAttribute("type",Constants.BIOHAZARD_TYPE);
		request.setAttribute("output", "init");
		
		setBioHazardRequestAttributes(request);
		
		
		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String keyForCount = keyInSpecimenMap + Constants.APPEND_COUNT;
		Map bioHazards = (Map) multipleSpecimenMap.get(keyInSpecimenMap);
		Integer bioHazardCnt = (Integer) multipleSpecimenMap.get(keyForCount);
		if (bioHazards != null)
		{
			((NewSpecimenForm) form).setBiohazard(bioHazards);
			if (bioHazardCnt != null)
				((NewSpecimenForm) form).setBhCounter(bioHazardCnt.intValue());
		}
		return mapping.findForward("bioHazard");
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward submitBioHazards(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		

		Map bioHazards = ((NewSpecimenForm) form).getBiohazard();
		int bioHazardCnt = ((NewSpecimenForm) form).getBhCounter();

		request.setAttribute("output", "success");
		request.setAttribute("type",Constants.BIOHAZARD_TYPE);
		setBioHazardRequestAttributes(request);

		Map multipleSpecimenMap = chkForMultipleSpecimenMap(request);
		String keyInSpecimenMap = request.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY);
		String keyForCount = keyInSpecimenMap + Constants.APPEND_COUNT;
		multipleSpecimenMap.put(keyInSpecimenMap, bioHazards);
		multipleSpecimenMap.put(keyForCount, new Integer(bioHazardCnt));
		
		return mapping.findForward("bioHazard");
	}

	public ActionForward deleteBioHazards(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		request.setAttribute("output", "init");
		request.setAttribute("type",Constants.BIOHAZARD_TYPE);
		String button = request.getParameter("button");
		Map bioHazards = ((NewSpecimenForm) form).getBiohazard();

		if (button != null)
		{
			if (button.equals("deleteExId"))
			{
				List key = new ArrayList();
				key.add("Biohazard:i_type");
				key.add("Biohazard:i_id");
				MapDataParser.deleteRow(key, bioHazards, request.getParameter("status"));

			}
		}
		setBioHazardRequestAttributes(request);
		return mapping.findForward("bioHazard");

	}
	

	private void setBioHazardRequestAttributes(HttpServletRequest request) throws DAOException
	{

		//Setting biohazard list
		
		NewSpecimenBizLogic bizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance()
		.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);

		
		String[] bhIdArray = {"-1"};
		String[] bhTypeArray = {Constants.SELECT_OPTION};
		String[] bhNameArray = {Constants.SELECT_OPTION};

		String selectColNames[] = {Constants.SYSTEM_IDENTIFIER, "name", "type"};
		List biohazardList = bizLogic.retrieve(Biohazard.class.getName(), selectColNames);
		Iterator iterator = biohazardList.iterator();

		//Creating & setting the biohazard name, id & type list
		if (biohazardList != null && !biohazardList.isEmpty())
		{
			bhIdArray = new String[biohazardList.size() + 1];
			bhTypeArray = new String[biohazardList.size() + 1];
			bhNameArray = new String[biohazardList.size() + 1];

			bhIdArray[0] = "-1";
			bhTypeArray[0] = "";
			bhNameArray[0] = Constants.SELECT_OPTION;

			int i = 1;

			while (iterator.hasNext())
			{
				Object hazard[] = (Object[]) iterator.next();
				bhIdArray[i] = String.valueOf(hazard[0]);
				bhNameArray[i] = (String) hazard[1];
				bhTypeArray[i] = (String) hazard[2];
				i++;
			}
		}

		request.setAttribute(Constants.BIOHAZARD_NAME_LIST, bhNameArray);
		request.setAttribute(Constants.BIOHAZARD_ID_LIST, bhIdArray);
		request.setAttribute(Constants.BIOHAZARD_TYPES_LIST, bhTypeArray);

		biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_BIOHAZARD, null);
		request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);

	}
}
