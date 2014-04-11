package krishagni.catissueplus.action.annotations;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.HibernateDAO;

public class LoadAnnotationDataEntryPageAction extends BaseAction {
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String entityType = request.getParameter("staticEntityName");
		String entityRecId = request.getParameter("entityRecordId");

		HibernateDAO hibernateDao = null;
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
				.getAttribute(Constants.SESSION_DATA);
		hibernateDao = (HibernateDAO)AppUtility.openDAOSession(sessionDataBean);
		Session session = hibernateDao.getConnectionManager().getSessionFactory().getCurrentSession();
		String userName = new StringBuilder().append(sessionDataBean.getLastName()).append(", ")
				.append(sessionDataBean.getFirstName()).toString();
		
		Transaction txn = null;
		String ppId = null;
		String cpTitle = null; 

		Object[] result = null; 
		try {
			txn = session.beginTransaction();
			if (entityType.equals("Participant")) {
				result = (Object[]) getParticipantInfo(session, entityRecId);
				ppId = (String) result[0];
				cpTitle = (String) result[1];
			} else if (entityType.equals("SpecimenCollectionGroup")) {
				result = (Object[]) getScgInfo(session, entityRecId);
				ppId = (String) result[0];
				cpTitle = (String) result[1];
				String scgLabel = (String) result[2];
				String cpEventLabel = (String) result[3];
				
				request.setAttribute("scgLabel", scgLabel);
				request.setAttribute("cpEventLabel", cpEventLabel);
			} else if (entityType.equals("Specimen")) {
				result = (Object[]) getSpecimenInfo(session, entityRecId);
				ppId = (String) result[0];
				cpTitle = (String) result[1];
				String scgLabel = (String) result[2];
				String cpEventLabel = (String) result[3];
				String specimenLabel =  (String) result[4];

				request.setAttribute("scgLabel", scgLabel);
				request.setAttribute("cpEventLabel", cpEventLabel);
				request.setAttribute("specimenLabel", specimenLabel);
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception while fetching entity related info", e);
		} finally {
			if (txn != null) {
				txn.rollback();
			}
		}
		request.setAttribute("ppId", ppId);
		request.setAttribute("cpTitle", cpTitle);
		request.setAttribute("userName", userName);
		request.setAttribute("entityType", entityType);
		request.setAttribute("entityRecId", entityRecId);

		return mapping.findForward(Constants.SUCCESS);
	}
	
	@SuppressWarnings("unchecked")
	private Object getParticipantInfo(Session session, String entityRecId) {
		Query query = session.createSQLQuery(GET_PARTICIPANT_INFO_SQL);
		query.setLong("cprId", Long.parseLong(entityRecId));
		List<Object> objs = query.list();
		return objs != null && ! objs.isEmpty() ? objs.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	private Object getScgInfo(Session session, String entityRecId) {
		Query query = session.createSQLQuery(GET_SCG_INFO_SQL);
		query.setLong("scgId", Long.parseLong(entityRecId));
		List<Object> objs = query.list();
		return objs != null && ! objs.isEmpty() ? objs.get(0) : null;
	}
	
	
	private Object getSpecimenInfo(Session session, String entityRecId) {
		Query query = session.createSQLQuery(GET_SPECIMEN_INFO_SQL);
		query.setLong("specimenId", Long.parseLong(entityRecId));
		List<Object> objs = query.list();
		return objs != null && ! objs.isEmpty() ? objs.get(0) : null;
	}
	
	private static final String GET_PARTICIPANT_INFO_SQL = 
			" select " +
			"	reg.protocol_participant_id, cp.title " +
			" from " +
			"	catissue_coll_prot_reg reg " +
			"	inner join catissue_collection_protocol cp on cp.identifier = reg.collection_protocol_id " +
			" where " +  
			"	reg.identifier = :cprId";
	
	private static final String GET_SCG_INFO_SQL = 
			" select " + 
			"	reg.protocol_participant_id, cp.title, scg.name, event.collection_point_label " +
			" from " + 
			"	catissue_coll_prot_reg reg " +
			"	inner join catissue_collection_protocol cp on cp.identifier = reg.collection_protocol_id " +
			"	inner join catissue_specimen_coll_group scg on scg.collection_protocol_reg_id = reg.identifier " +
			"	inner join catissue_coll_prot_event event on event.identifier = scg.collection_protocol_event_id " +
			" where " + 
			"	scg.identifier = :scgId";
	
	private static final String GET_SPECIMEN_INFO_SQL = 
			" select " +  
			"	reg.protocol_participant_id, cp.title, scg.name, event.collection_point_label, sp.label " +
			" from " + 
			"	catissue_coll_prot_reg reg " +
			"	inner join catissue_collection_protocol cp on cp.identifier = reg.collection_protocol_id " +
			"	inner join catissue_specimen_coll_group scg on scg.collection_protocol_reg_id = reg.identifier " +
			"	inner join catissue_coll_prot_event event on event.identifier = scg.collection_protocol_event_id " +
			"	inner join catissue_specimen sp on sp.specimen_collection_group_id = scg.identifier " +
			" where " + 
			"	sp.identifier = :specimenId";
}
