package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.dao.CPRDAO;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.query.generator.ColumnValueBean;


public class ParticipantDashboardSCGAction extends CatissueBaseAction
{

	final String FORWARD_TO_SUMARRY_PAGE="anticipatedSpecimenSumarryPage";
	final String FORWARD_TO_MULTIPLE_SPECIMEN_PAGE = "unplannedMultipleSpecimenAdd";
	final String FORWARD_TO_NEW_SPECIMEN_PAGE = "unplannedSpecimenAdd";
	final String FORWARD_TO_ADD_SCG = "addSCG";
	final String FORWARD_TO_EDIT_SCG ="editSCG";
	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String forwardTo = Constants.FAILURE;
		SessionDataBean sessionDataBean = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		String operation = request.getParameter("operation");
		HibernateDAO hibernateDAO = null;
		try
		{
			hibernateDAO = (HibernateDAO)AppUtility.openDAOSession(null);
			SpecimenCollectionGroupBizLogic collectionGroupBizLogic = new SpecimenCollectionGroupBizLogic();
			Boolean isPlanned = Boolean.valueOf(request.getParameter("isPlanned").toString());
			request.setAttribute("cpeId",request.getParameter("cpeId"));
			request.setAttribute("operation",request.getParameter("operation"));
			request.setAttribute("scgId",request.getParameter("scgId"));
			request.setAttribute("numberOfSpecimens",request.getParameter("numberOfSpecimens"));
			request.setAttribute("cpSearchParticipantId",request.getParameter("cpSearchParticipantId"));
			request.setAttribute("cpSearchCpId",request.getParameter("cpSearchCpId"));
			String cpId = request.getParameter("cpSearchCpId");
			String paricipantId = request.getParameter("cpSearchParticipantId");
			request.setAttribute("id", paricipantId);
			request.setAttribute("cpSearchCpId",request.getParameter("cpSearchCpId"));
			SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
			if("addSCG".equals(operation))
			{
				String eventId = request.getParameter("cpeId");
				CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)hibernateDAO.retrieveById(CollectionProtocolEvent.class.getName(), Long.valueOf(eventId));
				Collection<CollectionProtocolEvent> cpeCollection = new HashSet<CollectionProtocolEvent>();
				cpeCollection.add(collectionProtocolEvent);
//				String cprSql = "select cpr from " +CollectionProtocolRegistration.class.getName()
//						+ " as cpr where cpr.participant.id=? and cpr.collectionProtocol.id =?";
//				List<ColumnValueBean> columnValueBeans = new ArrayList();
//				ColumnValueBean columnValueBean = new ColumnValueBean(Long.valueOf(paricipantId));
//				ColumnValueBean columnValueBean1 = new ColumnValueBean(Long.valueOf(cpId));
//				columnValueBeans.add(columnValueBean);
//				columnValueBeans.add(columnValueBean1);
//				
//				List<CollectionProtocolRegistration> cprList = hibernateDAO.executeQuery(cprSql, columnValueBeans);
				CPRDAO cprdao = new CPRDAO();
				
				CollectionProtocolRegistration cpr = cprdao.getCPRByCPAndParticipantId(hibernateDAO, Long.valueOf(paricipantId), Long.valueOf(cpId));
				specimenCollectionGroup = new SpecimenCollectionGroup(collectionProtocolEvent);
				
				if(!isPlanned && collectionProtocolEvent.getDefaultSite() == null)
				{
					ActionErrors errors = new ActionErrors();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Site"));
					saveErrors(request, errors);
					forwardTo = FORWARD_TO_ADD_SCG;
				}
				else
				{
					specimenCollectionGroup.setSpecimenCollectionSite(collectionProtocolEvent.getDefaultSite());
					specimenCollectionGroup.setCollectionStatus(isPlanned?Constants.COLLECTION_STATUS_PENDING:Constants.COMPLETE);
					specimenCollectionGroup.setIsCPBasedSpecimenEntryChecked(isPlanned);
					specimenCollectionGroup.setCollectionProtocolRegistration(cpr);
					collectionGroupBizLogic.insertSCG(specimenCollectionGroup,hibernateDAO,sessionDataBean);
				}
				setRequestAttr(request, cpId, paricipantId, specimenCollectionGroup);
//				forwardTo = getForwardTo(request, isPlanned);
				
			}
			else if("editSCG".equals(operation))
			{
				String scgId = request.getParameter("scgId");
				request.setAttribute("id", scgId);
				specimenCollectionGroup = (SpecimenCollectionGroup)hibernateDAO.retrieveById(SpecimenCollectionGroup.class.getName(), Long.valueOf(scgId));
				CollectionProtocolEvent collectionProtocolEvent = specimenCollectionGroup.getCollectionProtocolEvent();
				if(!isPlanned && (Validator.isEmpty(specimenCollectionGroup.getCollectionStatus()) || Constants.COLLECTION_STATUS_PENDING.equals(specimenCollectionGroup.getCollectionStatus())))
				{
					if(specimenCollectionGroup.getSpecimenCollectionSite() == null && collectionProtocolEvent.getDefaultSite() == null)
					{
						ActionErrors errors = new ActionErrors();
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Site"));
						saveErrors(request, errors);
						request.setAttribute("id", paricipantId);
						request.setAttribute("cpSearchCpId",request.getParameter("cpSearchCpId"));
						forwardTo = FORWARD_TO_EDIT_SCG;
					}
					else 
					{
						specimenCollectionGroup.setSpecimenCollectionSite(collectionProtocolEvent.getDefaultSite());
						specimenCollectionGroup.setCollectionStatus(Constants.COMPLETE);
						specimenCollectionGroup.setIsCPBasedSpecimenEntryChecked(isPlanned);
						collectionGroupBizLogic.updateSCG(specimenCollectionGroup, hibernateDAO, sessionDataBean);
					}
					
					
				}
				setRequestAttr(request, cpId, paricipantId, specimenCollectionGroup);
//				forwardTo = getForwardTo(request, isPlanned);
			}
			
			request.setAttribute("id", specimenCollectionGroup.getId());
			if(!FORWARD_TO_ADD_SCG.equals(forwardTo) && !FORWARD_TO_EDIT_SCG.equals(forwardTo))
			{
				forwardTo = getForwardTo(request, isPlanned);
				if(specimenCollectionGroup.getId() == null)
				{
					ActionErrors errors = new ActionErrors();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("specimen.collectionStatus")));
					saveErrors(request, errors);
					forwardTo = Constants.FAILURE;
				}
			}
			
			
			hibernateDAO.commit();
		}
		catch(ApplicationException e)
		{
			ActionErrors errors = new ActionErrors();
			ActionError actionError = new ActionError("errors.item",
					e.getCause().getMessage());
			errors.add(ActionErrors.GLOBAL_ERROR, actionError);
			saveErrors(request, errors);
			if("addSCG".equals(operation))
			{
				forwardTo = FORWARD_TO_ADD_SCG;
			}
			else if("editSCG".equals(operation))
			{
				forwardTo = FORWARD_TO_EDIT_SCG;
			}
			
		}
		finally
		{
			AppUtility.closeDAOSession(hibernateDAO);
		}
//		return mapping.findForward("anticipatedSpecimenSumarryPage");
		return mapping.findForward(forwardTo);
				
	}

	private void setRequestAttr(HttpServletRequest request, String cpId, String paricipantId,
			SpecimenCollectionGroup specimenCollectionGroup)
	{
		HashMap hashMap = new HashMap();
		hashMap.put("specimenCollectionGroupName", specimenCollectionGroup.getName());
		hashMap.put(Constants.COLLECTION_PROTOCOL_ID, Long.valueOf(cpId));
		hashMap.put(Constants.PARTICIPANT_ID, Long.valueOf(paricipantId));
		hashMap.put("specimenCollectionGroupId", specimenCollectionGroup.getId());
		hashMap.put("COLLECTION_PROTOCOL_EVENT_ID", specimenCollectionGroup.getCollectionProtocolEvent().getId());
		request.setAttribute("forwardToHashMap", hashMap);
	}

	private String getForwardTo(HttpServletRequest request, Boolean isPlanned)
	{
		String forwardTo = FORWARD_TO_SUMARRY_PAGE;
		if(!isPlanned)
		{
			String specimenCount = request.getParameter("numberOfSpecimens");
			if(Validator.isEmpty(specimenCount) || Integer.valueOf(specimenCount) == 1)
			{
				forwardTo = FORWARD_TO_NEW_SPECIMEN_PAGE;
			}
			else if(Integer.valueOf(specimenCount) > 1)
			{
				forwardTo = FORWARD_TO_MULTIPLE_SPECIMEN_PAGE;
			}
			
		}
		return forwardTo;
	}

}
