/**
 * <p>
 * Title: ShowFramedPageAction Class>
 * <p>
 * Description: ShowFramedPageAction is used to display the query results view.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.SpecimenTreeBizLogic;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;

/**
 * ShowFramedPageAction is used to display the query results view.
 *
 * @author gautam_shetty
 */
public class ShowFramedPageAction extends Action
{

	/**
	 * Overrides the execute method in Action class.
	 *
	 * @param mapping
	 *            ActionMapping object
	 * @param form
	 *            ActionForm object
	 * @param request
	 *            HttpServletRequest object
	 * @param response
	 *            HttpServletResponse object
	 * @return ActionForward object
	 * @throws Exception
	 *             object
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// Sets the pageOf attribute (for Add,Edit or Query Interface)
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		request.setAttribute(Constants.PAGE_OF, pageOf);
		HttpSession session = request.getSession();

		// Aniruddha : For removing hardcoded names of html component
		session.setAttribute(Constants.CONTAINER_STYLEID, request
				.getParameter(Constants.CONTAINER_STYLEID));
		System.out.println(request.getParameter(Constants.CONTAINER_STYLEID));
		session.setAttribute(Constants.CONTAINER_STYLE, request
				.getParameter(Constants.CONTAINER_STYLE));
		session.setAttribute(Constants.XDIM_STYLEID, request.getParameter(Constants.XDIM_STYLEID));
		session.setAttribute(Constants.YDIM_STYLEID, request.getParameter(Constants.YDIM_STYLEID));

		session.setAttribute(Constants.SELECTED_CONTAINER_NAME, request
				.getParameter(Constants.SELECTED_CONTAINER_NAME));
		session.setAttribute(Constants.CONTAINERID, request.getParameter(Constants.CONTAINERID));
		session.setAttribute(Constants.POS1, request.getParameter(Constants.POS1));
		session.setAttribute(Constants.POS2, request.getParameter(Constants.POS2));
		session.removeAttribute(Constants.CAN_HOLD_CONTAINER_TYPE);
		session.removeAttribute(Constants.CAN_HOLD_COLLECTION_PROTOCOL);
		session.removeAttribute(Constants.CAN_HOLD_SPECIMEN_CLASS);
		session.removeAttribute(Constants.CAN_HOLD_SPECIMEN_TYPE);
		session.removeAttribute(Constants.CAN_HOLD_SPECIMEN_ARRAY_TYPE);

		if (pageOf.equals(Constants.PAGE_OF_SPECIMEN) || pageOf.equals(Constants.PAGE_OF_ALIQUOT)
				|| pageOf.equals(Constants.PAGE_OF_STORAGE_CONTAINER))
		{
			final String storageType = request.getParameter("storageType");
			final String isStorageContainer = request.getParameter("storageContainer");
			session.setAttribute("storageContainer", isStorageContainer);

			final String collectionProtocol = request
					.getParameter(Constants.CAN_HOLD_COLLECTION_PROTOCOL);
			final String specimenClass = request.getParameter(Constants.CAN_HOLD_SPECIMEN_CLASS);
			final String specimenType = request.getParameter(Constants.CAN_HOLD_SPECIMEN_TYPE);
			final String specimenarrayType = request
					.getParameter(Constants.CAN_HOLD_SPECIMEN_ARRAY_TYPE);

			session.setAttribute(Constants.CAN_HOLD_CONTAINER_TYPE, storageType);
			session.setAttribute(Constants.CAN_HOLD_COLLECTION_PROTOCOL, collectionProtocol);
			session.setAttribute(Constants.CAN_HOLD_SPECIMEN_CLASS, specimenClass);
			session.setAttribute(Constants.CAN_HOLD_SPECIMEN_TYPE, specimenType);
			session.setAttribute(Constants.CAN_HOLD_SPECIMEN_ARRAY_TYPE, specimenarrayType);
		}
		else if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION))
		{
			final String storageContainerType = request
					.getParameter(Constants.STORAGE_CONTAINER_TYPE);
			request.setAttribute(Constants.STORAGE_CONTAINER_TYPE, storageContainerType);
			final String storageContainerID = request
					.getParameter(Constants.STORAGE_CONTAINER_TO_BE_SELECTED);
			request.setAttribute(Constants.STORAGE_CONTAINER_TO_BE_SELECTED, storageContainerID);
			final String position = request.getParameter(Constants.STORAGE_CONTAINER_POSITION);
			request.setAttribute(Constants.STORAGE_CONTAINER_POSITION, position);

		}
		else if (pageOf.equals(Constants.PAGE_OF_TISSUE_SITE))
		{
			final String propertyName = request.getParameter(Constants.PROPERTY_NAME);
			request.setAttribute(Constants.PROPERTY_NAME, propertyName);

			final String cdeName = request.getParameter(Constants.CDE_NAME);
			session.setAttribute(Constants.CDE_NAME, cdeName);
		}
		else if (pageOf.equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN))
		{
			final String specimenClass = request.getParameter(Constants.SPECIMEN_CLASS);
			final String collectionGroupName = request
					.getParameter(Constants.SPECIMEN_COLLECTION_GROUP);
			session.setAttribute(Constants.SPECIMEN_CLASS, specimenClass);
			session.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP, collectionGroupName);
			session.setAttribute(Constants.SPECIMEN_ATTRIBUTE_KEY, request
					.getParameter(Constants.SPECIMEN_ATTRIBUTE_KEY));
			session.setAttribute(Constants.SPECIMEN_CALL_BACK_FUNCTION, request
					.getParameter(Constants.SPECIMEN_CALL_BACK_FUNCTION));

			final DefaultBizLogic bizLogic = new DefaultBizLogic();
			session.setAttribute(Constants.CAN_HOLD_SPECIMEN_CLASS, specimenClass);

			final List list = bizLogic.retrieve(SpecimenCollectionGroup.class.getName(), "name",
					collectionGroupName);
			if (list != null && !list.isEmpty())
			{
				final SpecimenCollectionGroup group = (SpecimenCollectionGroup) list.get(0);
				final String collectionProtocol = group.getCollectionProtocolRegistration()
						.getCollectionProtocol().getId()
						+ "";
				session.setAttribute(Constants.CAN_HOLD_COLLECTION_PROTOCOL, collectionProtocol);
			}
		}
		else if (pageOf.equals(Constants.PAGE_OF_SPECIMEN_TREE))
		{
			session = request.getSession();
			IBizLogic bizLogic = null;

			if (request.getParameter(Constants.SPECIMEN_TREE_SPECIMEN_ID) != null)
			{
				final String strSpecimenId = request
						.getParameter(Constants.SPECIMEN_TREE_SPECIMEN_ID);
				// String strSpecimenId = (String)
				// session.getAttribute(Constants.SPECIMEN_TREE_SPECIMEN_ID);
				final Long specimenId = new Long(strSpecimenId);
				bizLogic = new SpecimenTreeBizLogic(specimenId, false);

			}
			// SCG Id is set in case of Pathology Case
			if (request.getParameter(Constants.SPECIMEN_TREE_SPECCOLLGRP_ID) != null)
			{
				final String strSpecimenCollgrpId = request
						.getParameter(Constants.SPECIMEN_TREE_SPECCOLLGRP_ID);
				final Long specimenCollgrpId = new Long(strSpecimenCollgrpId);
				bizLogic = new SpecimenTreeBizLogic(specimenCollgrpId, true);
			}

			final String propertyName = request.getParameter(Constants.PROPERTY_NAME);
			final String specimentype = request.getParameter(Constants.SPECIMEN_TYPE);
			final String specimenClass = request.getParameter(Constants.SPECIMEN_CLASS);

			request.setAttribute(Constants.PROPERTY_NAME, propertyName);
			request.setAttribute(Constants.SPECIMEN_TYPE, specimentype);
			request.setAttribute(Constants.SPECIMEN_CLASS, specimenClass);

			// Obtain the tree nodes in a vector format.
			final Vector dataList = ((SpecimenTreeBizLogic) bizLogic).getTreeViewData();
			// Set the vector in request scope to be accessed in
			// SpecimenTreeView.jsp
			request.setAttribute(Constants.TREE_DATA_LIST, dataList);
		}
		return mapping.findForward(pageOf);
	}
}