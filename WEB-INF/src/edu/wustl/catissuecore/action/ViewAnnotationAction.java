/**
 * <p>
 * Title: ParticipantAction Class>
 * <p>
 * Description: This class initializes the fields in the Participant Add/Edit
 * webpage.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Gautam Shetty
 * @version 1.00 Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * 
 *
 * @author pathik_sheth
 */
public class ViewAnnotationAction extends CatissueBaseAction {

	/** logger. */
	private static final Logger LOGGER = Logger
			.getCommonLogger(ViewAnnotationAction.class);

	/**
	  * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 *
	 * @return value for ActionForward object
	 *
	 * @throws Exception
	 *             generic exception
	 */
	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final String refPart = request.getParameter("refresh");
		final SessionDataBean sessionDataBean = this.getSessionData( request );
		if (refPart != null) {
			request.setAttribute("refresh", refPart);

		}
		Long participantEntityId = null;
		if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
				AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID) != null) {
			participantEntityId = (Long) CatissueCoreCacheManager
					.getInstance()
					.getObjectFromCache(
							AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID);
		} else {
			participantEntityId = AnnotationUtil
					.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY);
			CatissueCoreCacheManager.getInstance().addObjectToCache(
					AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID,
					participantEntityId);
		}
		request.setAttribute(
				AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID,
				participantEntityId);
		//---------------------------------
		boolean matchingParticipantCase = false;
		final String associatedStaticEntity = edu.wustl.catissuecore.annotations.AnnotationUtil
				.getAssociatedStaticEntityForRecordEntry(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY);
		List cpIdsList = edu.wustl.query.util.global.Utility.getCPIdsList(associatedStaticEntity,
				Long.valueOf(request.getParameter("entityRecordId")), sessionDataBean);
		if (cpIdsList.size() > 1 && Participant.class.getName().equals(associatedStaticEntity))
		{
			matchingParticipantCase = true;
		}
		else
		{
			if (cpIdsList.size() > 1)
			{
				cpIdsList.remove(1);
			}
		}

		if (!sessionDataBean.isAdmin())
		{
			final PrivilegeCache privilegeCache = PrivilegeManager.getInstance().getPrivilegeCache(
					sessionDataBean.getUserName() );
			final StringBuffer stringBuffer = new StringBuffer();
			boolean hasPrivilege = false;
			stringBuffer.append( Constants.COLLECTION_PROTOCOL_CLASS_NAME ).append( '_' );

			for (final Object cpId : cpIdsList)
			{
				hasPrivilege = ( ( privilegeCache.hasPrivilege( stringBuffer.toString() + cpId.toString(),
						Permissions.REGISTRATION ) ) || ( privilegeCache.hasPrivilege( stringBuffer
						.toString()
						+ cpId.toString(), Permissions.SPECIMEN_PROCESSING ) ) );

				if (!hasPrivilege)
				{
					hasPrivilege = AppUtility.checkForAllCurrentAndFutureCPs(
							Permissions.REGISTRATION + "," + Permissions.SPECIMEN_PROCESSING,
							sessionDataBean, cpId.toString() );
				}

				if (matchingParticipantCase && hasPrivilege)
				{
					break;
				}
			}
			if (!hasPrivilege)
			{
				final ActionErrors errors = new ActionErrors();
				final ActionError error = new ActionError( "access.view.action.denied" );
				errors.add( ActionErrors.GLOBAL_ERROR, error );
				this.saveErrors( request, errors );
				return mapping.findForward( Constants.ACCESS_DENIED );
				// throw new DAOException(
				// "Access denied ! User does not have privilege to view/edit this information."
				// );
			}
		}
		request.setAttribute( "entityId", participantEntityId.toString());
		request.setAttribute( "entityRecordId", request.getParameter("participantId"));
		request.setAttribute( Constants.ID, request.getParameter( Constants.ID ) );
		request.setAttribute( "staticEntityName", AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY);
		request.setAttribute("participantId", String.valueOf(request.getParameter("participantId")));
		request.setAttribute(Constants.PAGE_OF, request.getParameter(Constants.PAGE_OF));
		return mapping.findForward(request.getParameter(Constants.PAGE_OF));
	}
}
