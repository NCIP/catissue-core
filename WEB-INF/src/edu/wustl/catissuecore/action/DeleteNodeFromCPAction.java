
package edu.wustl.catissuecore.action;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ProtocolEventDetailsForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * This class is for Support delete feature from CP.
 *
 * @author virender_mehta
 */
public class DeleteNodeFromCPAction extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ProtocolEventDetailsForm protocolEventDetailsForm = (ProtocolEventDetailsForm) form;
		HttpSession session = request.getSession();
		Map collectionProtocolEventMap = null;
		if (session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP) != null)
		{
			collectionProtocolEventMap = (LinkedHashMap) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		}

		if ("specimenRequirement".equals(request.getParameter("pageOf")))
		{
			String collectionProtocolEventKey = (String) request.getParameter(Constants.EVENT_KEY);
			String eventMapKey = null;
			StringTokenizer st = new StringTokenizer(collectionProtocolEventKey, "_");
			if (st.hasMoreTokens())
			{
				eventMapKey = st.nextToken();
			}
			CollectionProtocolEventBean collectionProtocolEventBean =
				(CollectionProtocolEventBean) collectionProtocolEventMap
					.get(eventMapKey);
			Map specimenReqMap = (LinkedHashMap) collectionProtocolEventBean
					.getSpecimenRequirementbeanMap();
			if (specimenReqMap.containsKey(collectionProtocolEventKey))
			{
				specimenReqMap.remove(collectionProtocolEventKey);
			}
			else
			{
				String parentReqMapKey = eventMapKey + "_" + st.nextToken();
				char specimenType = getSpecimenType(collectionProtocolEventKey);
				removeChildSpecimen(specimenReqMap, parentReqMapKey, collectionProtocolEventKey,
						st, specimenType);
			}
		}
		else
		{
			String mapkey = protocolEventDetailsForm.getCollectionProtocolEventkey();
			collectionProtocolEventMap.remove(mapkey);
		}
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * This method is for removing child specimen.
	 *
	 * @param specimenReqMap
	 *            Requirement Specimen map
	 * @param parentReqMapKey
	 *            Map key
	 * @param collectionProtocolEventKey
	 *            Event Key
	 * @param st
	 *            String toknizer
	 * @param specimenType
	 *            type of specimen
	 */
	private void removeChildSpecimen(Map specimenReqMap, String parentReqMapKey,
			String collectionProtocolEventKey, StringTokenizer st, char specimenType)
	{
		SpecimenRequirementBean spReqBean = (SpecimenRequirementBean) specimenReqMap
				.get(parentReqMapKey);
		Map childMap = null;
		if (specimenType == 'A')
		{
			childMap = removeAliquot(collectionProtocolEventKey, spReqBean);
		}
		else
		{
			childMap = removeDerive(collectionProtocolEventKey, spReqBean);
		}
		if (childMap != null)
		{
			parentReqMapKey = parentReqMapKey + "_" + st.nextToken();
			removeChildSpecimen(childMap, parentReqMapKey, collectionProtocolEventKey, st,
					specimenType);
		}
	}

	/**
	 * @param specimenKey
	 *            Specimen key.
	 * @return child specimen's first character
	 */
	private char getSpecimenType(String specimenKey)
	{
		String specimenType = null;
		StringTokenizer st = new StringTokenizer(specimenKey, "_");
		while (st.hasMoreTokens())
		{
			specimenType = st.nextToken();
		}
		return specimenType.charAt(0);
	}

	/**
	 * Remove aliquot specimen.
	 *
	 * @param collectionProtocolEventKey
	 *            Event key
	 * @param spReqBean
	 *            Requirement bean
	 * @return aliquotMap map of aliquot specimen
	 */
	private Map removeAliquot(String collectionProtocolEventKey, SpecimenRequirementBean spReqBean)
	{

		Map aliquotMap = spReqBean.getAliquotSpecimenCollection();
		if (aliquotMap.containsKey(collectionProtocolEventKey))
		{
			aliquotMap.remove(collectionProtocolEventKey);
			aliquotMap = null;
		}
		return aliquotMap;
	}

	/**
	 * Remove derive specimen.
	 *
	 * @param collectionProtocolEventKey
	 *            : collectionProtocolEventKey
	 * @param spReqBean
	 *            : spReqBean
	 * @return Map : Map
	 */
	private Map removeDerive(String collectionProtocolEventKey, SpecimenRequirementBean spReqBean)
	{

		Map driveMap = spReqBean.getDeriveSpecimenCollection();
		if (driveMap.containsKey(collectionProtocolEventKey))
		{
			driveMap.remove(collectionProtocolEventKey);
			driveMap = null;
		}
		return driveMap;
	}
}
