
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.xmi.AnnotationUtil;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.caties.util.ViewSPRUtil;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * @author vijay_pande Action class to show Surgical Pathology Report
 */
public class ViewSurgicalPathologyReportAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger
			.getCommonLogger(ViewSurgicalPathologyReportAction.class);

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

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final ViewSurgicalPathologyReportForm viewSPR = (ViewSurgicalPathologyReportForm) form;
		String pageOf = viewSPR.getPageOf();
		final String operation = viewSPR.getOperation();
		final String submittedFor = viewSPR.getSubmittedFor();
		final String forwardTo = viewSPR.getForwardTo();
		final String strId = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		String reportIdStr = request.getParameter("reportId");
		// If reportId is null in request then retrieved from form. For
		// Review/Quarantine event param. Bug id: 9260
		if (reportIdStr == null)
		{
			reportIdStr = viewSPR.getIdentifiedReportId();
		}
		final Long reportId = Long.valueOf(reportIdStr);

		Long id = null;
		if (strId != null)
		{
			id = new Long(strId);
			viewSPR.setId(id);
		}
		final String strIdentifier = request.getParameter(Constants.IDENTIFIER);
		Long identifier = null;
		if (strIdentifier != null)
		{
			identifier = new Long(strIdentifier);
			viewSPR.setId(identifier);
			retriveFromReportId(identifier, request, viewSPR);
		}
		if (reportId != null && reportId != 0
				&& operation.equalsIgnoreCase(Constants.VIEW_SURGICAL_PATHOLOGY_REPORT))
		{
			retrieveAndSetObject(pageOf, reportId, request, viewSPR);
		}
		final String aliasName = "";
		request.setAttribute(Constants.PARTICIPANTIDFORREPORT, viewSPR.getParticipantIdForReport());
		viewSPR.setHasAccess(isAuthorized(getSessionBean(request), viewSPR
				.getCollectionProtocolId(), aliasName));
		// If request is from Query to view Deidentfied report
		if (viewSPR.getIdentifiedReportId() == null || viewSPR.getIdentifiedReportId() == "")
		{
			viewSPR.setHasAccess(false);
		}
		request.setAttribute(Constants.PAGE_OF, pageOf);
		request.setAttribute(Constants.OPERATION, Constants.VIEW_SURGICAL_PATHOLOGY_REPORT);
		request.setAttribute(Constants.REQ_PATH, "");
		request.setAttribute(Constants.SUBMITTED_FOR, submittedFor);
		request.setAttribute(Constants.FORWARD_TO, forwardTo);
		// Falguni:Performance Enhancement.
		Long specimenEntityId = null;
		/*if (CatissueCoreCacheManager.getInstance().getObjectFromCache("specimenEntityId") != null)
		{
			specimenEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
					"specimenEntityId");
		}
		else
		{
			specimenEntityId = AnnotationUtil.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN);
			CatissueCoreCacheManager.getInstance().addObjectToCache("specimenEntityId",
					specimenEntityId);
		}
		request.setAttribute("specimenEntityId", specimenEntityId);*/

		if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
				AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID) != null)
		{
			specimenEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
					AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID);
		}
		else
		{
			specimenEntityId = AnnotationUtil
					.getEntityId(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY);
			CatissueCoreCacheManager.getInstance().addObjectToCache(
					AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID, specimenEntityId);
		}
		request.setAttribute(AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID, specimenEntityId);

		// Falguni:Performance Enhancement -User clicks on Report tab then
		// annotation page on Edit participant page
		Long participantEntityId = null;
		/*if (CatissueCoreCacheManager.getInstance().getObjectFromCache("participantEntityId") != null)
		{
			participantEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
					"participantEntityId");
		}
		else
		{
			participantEntityId = AnnotationUtil
					.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT);
			CatissueCoreCacheManager.getInstance().addObjectToCache("participantEntityId",
					participantEntityId);
		}
		request.setAttribute("participantEntityId", participantEntityId);*/
		if (CatissueCoreCacheManager.getInstance().getObjectFromCache(
				AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID) != null)
		{
			participantEntityId = (Long) CatissueCoreCacheManager.getInstance().getObjectFromCache(
					AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID);
		}
		else
		{
			participantEntityId = AnnotationUtil
					.getEntityId(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY);
			CatissueCoreCacheManager.getInstance().addObjectToCache(
					AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID, participantEntityId);
		}
		request.setAttribute(AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID,
				participantEntityId);

		if (pageOf.equalsIgnoreCase(Constants.PAGE_OF_NEW_SPECIMEN)
				|| pageOf.equalsIgnoreCase(Constants.PAGE_OF_SPECIMEN_CP_QUERY))
		{
			request.setAttribute(Constants.ID, id.toString());
		}
		final String flow = request.getParameter("flow");
		if (flow != null && flow.equals("viewReport"))
		{
			pageOf = "gridViewReport";
		}
		return mapping.findForward(pageOf);

	}

	/**
	 * * This method retrives the appropriate SurgicalPathologyReport object and
	 * set values of ViewSurgicalPathologyReportForm object.
	 *
	 * @param pageOf
	 *            : pageOf
	 * @param reportId
	 *            : reportId
	 * @param request
	 *            : request
	 * @param viewSPR
	 *            : viewSPR
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private void retrieveAndSetObject(String pageOf, Long reportId, HttpServletRequest request,
			ViewSurgicalPathologyReportForm viewSPR) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		if (pageOf.equalsIgnoreCase(Constants.PAGE_OF_PARTICIPANT)
				|| pageOf.equalsIgnoreCase(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
		{

			final Long participantId = getParticipantId(reportId);
			final ParticipantBizLogic bizLogic = (ParticipantBizLogic) factory
					.getBizLogic(Participant.class.getName());
			final List scgList = bizLogic.getSCGList(participantId);

			viewSPR.setReportIdList(getReportIdList(scgList));
		}

		if (reportId != null)
		{
			final IdentifiedSurgicalPathologyReportBizLogic bizLogic = (IdentifiedSurgicalPathologyReportBizLogic) factory
					.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
			final SurgicalPathologyReport report = new SurgicalPathologyReport();
			report.setId(reportId);
			try
			{
				bizLogic.populateUIBean(SurgicalPathologyReport.class.getName(), report.getId(),
						viewSPR);
				final DeidentifiedSurgicalPathologyReport deidReport = new DeidentifiedSurgicalPathologyReport();
				deidReport.setId(viewSPR.getDeIdentifiedReportId());
				final List conceptBeanList = ViewSPRUtil.getConceptBeanList(deidReport);
				request.setAttribute(Constants.CONCEPT_BEAN_LIST, conceptBeanList);
			}
			catch (final Exception ex)
			{
				logger.error(ex.getMessage(),ex);
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @param scgList
	 *            : scgList
	 * @return List : List
	 */
	private List getReportIdList(List scgList)
	{
		final List<NameValueBean> reportIDList = new ArrayList<NameValueBean>();
		Object[] obj = null;
		for (int i = 0; i < scgList.size(); i++)
		{
			obj = (Object[]) scgList.get(i);
			if (obj[1] != null || (obj[1] != null && !((String) obj[1]).equals("")))
			{
				final NameValueBean nb = new NameValueBean(obj[1], ((Long) obj[2]).toString());
				reportIDList.add(nb);
			}
		}
		return reportIDList;
	}

	/**
	 * @param identifier
	 *            : identifier
	 * @param request
	 *            : request
	 * @param viewSPR
	 *            : viewSPR
	 * @throws DAOException
	 *             : DAOException
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	public void retriveFromReportId(Long identifier, HttpServletRequest request,
			ViewSurgicalPathologyReportForm viewSPR) throws DAOException, BizLogicException
	{
		IBizLogic bizLogic = null;

		String witnessFullName = null;
		final DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		if (pageOf.equalsIgnoreCase(Constants.PAGE_OF_REVIEW_SPR))
		{
			request.setAttribute(Constants.OPERATION, Constants.REVIEW);
			viewSPR.setOperation(Constants.REVIEW);
			bizLogic = factory.getBizLogic(Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID);
			final Object object = bizLogic.retrieve(PathologyReportReviewParameter.class.getName(),
					identifier);
			final PathologyReportReviewParameter pathologyReportReviewParameter = (PathologyReportReviewParameter) object;
			viewSPR.setUserComments(pathologyReportReviewParameter.getComment());
			final User user = (User) defaultBizLogic.retrieveAttribute(
					PathologyReportReviewParameter.class.getName(), pathologyReportReviewParameter
							.getId(), "user");
			witnessFullName = user.getFirstName() + ", " + user.getLastName() + "'s";
			viewSPR.setUserName(witnessFullName);
			final SurgicalPathologyReport surgicalPathologyReport = (SurgicalPathologyReport) defaultBizLogic
					.retrieveAttribute(PathologyReportReviewParameter.class.getName(),
							pathologyReportReviewParameter.getId(), "surgicalPathologyReport");
			if (surgicalPathologyReport instanceof DeidentifiedSurgicalPathologyReport)
			{
				final Long identifiedSurgicalPathologyReportId = (Long) defaultBizLogic
						.retrieveAttribute(DeidentifiedSurgicalPathologyReport.class.getName(),
								surgicalPathologyReport.getId(), "specimenCollectionGroup."
										+ "identifiedSurgicalPathologyReport.id");
				defaultBizLogic.populateUIBean(IdentifiedSurgicalPathologyReport.class.getName(),
						identifiedSurgicalPathologyReportId, viewSPR);
			}
			else
			{
				final IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport = (IdentifiedSurgicalPathologyReport) surgicalPathologyReport;
				defaultBizLogic.populateUIBean(IdentifiedSurgicalPathologyReport.class.getName(),
						identifiedSurgicalPathologyReport.getId(), viewSPR);
			}
		}
		else
		{
			request.setAttribute(Constants.OPERATION, Constants.QUARANTINE);
			viewSPR.setOperation(Constants.QUARANTINE);
			bizLogic = factory.getBizLogic(Constants.QUARANTINE_EVENT_PARAMETER_FORM_ID);
			final Object object = bizLogic.retrieve(QuarantineEventParameter.class.getName(),
					identifier);
			final QuarantineEventParameter quarantineEventParameter = (QuarantineEventParameter) object;
			viewSPR.setUserComments(quarantineEventParameter.getComment());
			final User user = (User) defaultBizLogic.retrieveAttribute(
					QuarantineEventParameter.class.getName(), quarantineEventParameter.getId(),
					"user");
			witnessFullName = user.getLastName() + ", " + user.getFirstName();
			viewSPR.setUserName(witnessFullName);
			final Long deIdentifiedSurgicalPathologyReportId = (Long) defaultBizLogic
					.retrieveAttribute(QuarantineEventParameter.class.getName(),
							quarantineEventParameter.getId(),
							"deIdentifiedSurgicalPathologyReport." + "specimenCollectionGroup."
									+ "identifiedSurgicalPathologyReport.id");
			defaultBizLogic.populateUIBean(IdentifiedSurgicalPathologyReport.class.getName(),
					deIdentifiedSurgicalPathologyReportId, viewSPR);
		}
	}

	/**
	 * @param identifiedReportId
	 *            : identifiedReportId
	 * @return Long : Long
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private Long getParticipantId(Long identifiedReportId) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IdentifiedSurgicalPathologyReportBizLogic bizLogic = (IdentifiedSurgicalPathologyReportBizLogic) factory
				.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());

		final String sourceObjectName = IdentifiedSurgicalPathologyReport.class.getName();
		final String[] selectColumnName = {Constants.COLUMN_NAME_SCG_CPR_PARTICIPANT_ID};
		final String[] whereColumnName = {Constants.SYSTEM_IDENTIFIER};
		final String[] whereColumnCondition = {"="};
		final Object[] whereColumnValue = {identifiedReportId};
		final String joinCondition = "";

		final List participantIdList = bizLogic.retrieve(sourceObjectName, selectColumnName,
				whereColumnName, whereColumnCondition, whereColumnValue, joinCondition);
		if (participantIdList != null && participantIdList.size() > 0)
		{
			return (Long) participantIdList.get(0);
		}
		return null;
	}

	/**
	 * This method is to retrieve sessionDataBean from request object.
	 *
	 * @param request
	 *            HttpServletRequest object
	 * @return sessionBean SessionDataBean object
	 */
	private SessionDataBean getSessionBean(HttpServletRequest request)
	{
		try
		{
			final SessionDataBean sessionBean = (SessionDataBean) request.getSession()
					.getAttribute(Constants.SESSION_DATA);
			return sessionBean;
		}
		catch (final Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * @param sessionBean
	 *            : sessionBean
	 * @param identifier
	 *            : identifier
	 * @param aliasName
	 *            : aliasName
	 * @return boolean : boolean
	 * @throws Exception
	 *             : Exception
	 */
	private boolean isAuthorized(SessionDataBean sessionBean, long identifier, String aliasName)
			throws Exception
	{
		final String userName = sessionBean.getUserName();

		// To get privilegeCache through
		// Singleton instance of PrivilegeManager, requires User LoginName
		final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(userName);
		boolean isAuthorized = true;
		if (sessionBean.isSecurityRequired())
		{
			// ISecurityManager sm =
			// SecurityManagerFactory.getSecurityManager();
			aliasName = CollectionProtocol.class.getName();

			// String userName = sessionBean.getUserName();

			// Call to SecurityManager.checkPermission bypassed &
			// instead, call redirected to privilegeCache.hasPrivilege
			isAuthorized = privilegeCache.hasPrivilege(
					aliasName + "_" + String.valueOf(identifier), Permissions.READ_DENIED);
			// boolean isAuthorized =
			// SecurityManager.getInstance(ViewSurgicalPathologyReportAction
			// .class).
			// checkPermission(userName, aliasName, identifier,
			// Permissions.READ_DENIED, PrivilegeType.ObjectLevel);
			if (!isAuthorized)
			{
				// Check the permission of the user on the identified data of
				// the object.
				// Call to SecurityManager.checkPermission bypassed &
				// instead, call redirected to privilegeCache.hasPrivilege
				boolean hasPrivilegeOnIdentifiedData = privilegeCache.hasPrivilege(aliasName + "_"
						+ identifier, Permissions.REGISTRATION);
				if (!hasPrivilegeOnIdentifiedData)
				{
					hasPrivilegeOnIdentifiedData = AppUtility.checkForAllCurrentAndFutureCPs(
							Permissions.REGISTRATION, sessionBean, String.valueOf(identifier));
				}
				// boolean hasPrivilegeOnIdentifiedData =
				// SecurityManager.getInstance
				// (ViewSurgicalPathologyReportAction.class).
				// checkPermission(userName, aliasName, identifier,
				// Permissions.IDENTIFIED_DATA_ACCESS,
				// PrivilegeType.ObjectLevel);

				if (!hasPrivilegeOnIdentifiedData)
				{
					isAuthorized = false;
				}
				else
				{
					isAuthorized = true;
				}
			}
		}
		return isAuthorized;
	}
}
