
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.events.CprSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantSummary;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenCollectionGroupInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CprListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;

@Repository("collectionProtocolRegistrationDao")
public class CollectionProtocolRegistrationDaoImpl 
	extends AbstractDao<CollectionProtocolRegistration>
	implements CollectionProtocolRegistrationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<CprSummary> getCprList(CprListCriteria cprCrit) {
		Criteria query = getCprListQuery(cprCrit);
		query.setProjection(getCprSummaryFields(cprCrit));
		
		List<CprSummary> cprs = new ArrayList<CprSummary>();
		Map<Long, CprSummary> cprMap = new HashMap<Long, CprSummary>();
		
		List<Object[]> rows = query.list();				
		for (Object[] row : rows) {
			CprSummary cpr = getCprSummary(row);
			if (cprCrit.includeStat()) {
				cprMap.put(cpr.getCprId(), cpr);
			}
			
			cprs.add(cpr);
		}
		
		if (!cprCrit.includeStat()) {
			return cprs;
		}
		
		List<Object[]> countRows = getScgAndSpecimenCounts(
				cprCrit.cpId(), cprCrit.startAt(), cprCrit.maxResults());
		for (Object[] row : countRows) {
			CprSummary cpr = cprMap.get((Long)row[0]);
			cpr.setScgCount((Long)row[1]);
			cpr.setSpecimenCount((Long)row[2]);
		}
		
		return cprs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ParticipantSummary> getPhiParticipants(Long cpId, String searchString) {
//		boolean isSearchTermSpecified = !StringUtils.isBlank(searchString);
//
//		String queryName = GET_PHI_PARTICIPANTS_BY_CP_ID;
//		if (isSearchTermSpecified) {
//			queryName = GET_PHI_PARTICIPANTS_BY_CP_ID_AND_SEARCH_TERM;
//		}
//		
//		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
//		query.setLong("cpId", cpId);
//		if (isSearchTermSpecified) {
//			query.setString("searchTerm", "%" + searchString.toLowerCase() + "%");
//		}
//		query.setMaxResults(Utility.getMaxParticipantCnt());
//		List<ParticipantSummary> result = new ArrayList<ParticipantSummary>();
//		List<Object[]> rows = query.list();
//		for (Object[] row : rows) {
//			result.add(preparePhiParticipantInfo(row));
//		}
//
//		return result;
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SpecimenCollectionGroupInfo> getScgList(Long cprId) {

//		CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) sessionFactory.getCurrentSession().get(
//				CollectionProtocolRegistration.class, cprId);
//		Set<SpecimenCollectionGroupInfo> scgsInfo = new HashSet<SpecimenCollectionGroupInfo>();
//		Collection<SpecimenCollectionGroup> groups = cpr.getScgCollection();
//		for (SpecimenCollectionGroup specimenCollectionGroup : groups) {
//			if(!Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(specimenCollectionGroup.getActivityStatus())){
//			scgsInfo.add(SpecimenCollectionGroupInfo.fromScg(specimenCollectionGroup, cpr.getRegistrationDate()));
//			}
//		}
//		Collection<CollectionProtocolEvent> cpes = cpr.getCollectionProtocol().getCollectionProtocolEventCollection();
//		for (CollectionProtocolEvent collectionProtocolEvent : cpes) {
//			if(!Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(collectionProtocolEvent.getActivityStatus())){
//			scgsInfo.add(SpecimenCollectionGroupInfo.fromCpe(collectionProtocolEvent, cpr.getRegistrationDate()));
//			}
//		}
		return null;

		//				" ";
		//				"scg.collectionProtocolEvent cpe " +
		//				"where cpr.id=:cprId";
		//		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_COLLECTION_GROUPSBY_CPR_ID);
		//		Query query = sessionFactory.getCurrentSession().createQuery(hhhh);
		//		
		//		query.setLong("cprId", cprId);
		////		query.setLong("cprId2", cprId);
		//		List<Object[]> results = query.list();
		//		
		//Query query1 = sessionFactory.getCurrentSession().createQuery(hql3);
		//		
		//		query.setLong("cprId", cprId);
		//		List<Object[]> results1 = query.list();
		//
		////		List<SpecimenCollectionGroupInfo> scgs = new ArrayList<SpecimenCollectionGroupInfo>();
		//		for (Object[] object : results) {
		//			SpecimenCollectionGroupInfo scg = new SpecimenCollectionGroupInfo();
		//			scg.setId(object[0]==null?0l:Long.valueOf(object[0].toString()));
		//			scg.setName(object[1] == null ? "" : object[1].toString());
		//			scg.setCollectionStatus(object[2]==null?"pending":object[2].toString());
		//			if (object[3] != null) {
		//				scg.setReceivedDate((Date) object[7]);
		//			}
		//			scg.setEventPoint(Double.parseDouble(object[5].toString()));
		//			scg.setCollectionPointLabel(object[6].toString());
		////			scg.setRegistrationDate((Date) object[7]);
		//			scgs.add(scg);
		//		}
//		List<SpecimenCollectionGroupInfo> list = new ArrayList<SpecimenCollectionGroupInfo>(scgsInfo);
//		if (list != null) {
//			Collections.sort(list);
//		}
//		return list;
	}

	@Override
	public boolean isBarcodeUnique(String barcode) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CPR_ID_BY_BARCODE);
		query.setString("barcode", barcode);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public CollectionProtocolRegistration getCpr(Long cprId) {

		return (CollectionProtocolRegistration) sessionFactory.getCurrentSession().get(
				CollectionProtocolRegistration.class, cprId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public CollectionProtocolRegistration getCprByPpId(Long cpId, String protocolParticipantIdentifier) {

		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CPR_BY_PPID_AND_CPID);
		query.setString("ppid", protocolParticipantIdentifier);
		query.setLong("cpId", cpId);
		List<CollectionProtocolRegistration> result = query.list();
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public boolean isPpidUniqueForProtocol(Long cpId, String protocolParticipantIdentifier) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CPID_BY_PPID_AND_CPID);
		query.setString("ppid", protocolParticipantIdentifier);
		query.setLong("cpId", cpId);
		boolean isUnique = query.list().isEmpty() ? true : false;
		return isUnique;
	}
	
	@Override
	public ParticipantSummary getPhiParticipant(Long cpId, Long participantId) {
//		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PHI_PARTICIPANT_BY_CP_PARTICIPANT_ID);
//		query.setLong("cpId", cpId);
//		query.setLong("participantId", participantId);
//		
//		List<Object[]> rows = query.list();
//		return preparePhiParticipantInfo(rows.get(0));
		return null;
	}

	@Override
	public ParticipantSummary getParticipant(Long cpId, Long participantId) {
//		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PARTICIPANT_BY_CP_PARTICIPANT_ID);
//		query.setLong("cpId", cpId);
//		query.setLong("participantId", participantId);
//		
//		List<Object[]> rows = query.list();
//		return prepareParticipantInfo(rows.get(0));
		return null;
	}
	
	@Override
	public List<CollectionProtocolRegistration> getRegDetailsForParticipant(Long participantId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_REGISTRATION_BY_PARTICIPANT_ID);
		query.setLong("participantId", participantId);
		List<CollectionProtocolRegistration> registrations = query.list();
		return registrations;
	}
	
	@Override
	public List<CollectionProtocolRegistration> getSubRegDetailForParticipantAndCp(Long participantId, Long cpId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SUB_REGISTRATIONS_BY_PARTICIPANT_AND_CP_ID);
		query.setLong("participantId", participantId);
		query.setLong("cpId", cpId);
		return query.list();
	}
	
//	private ParticipantSummary preparePhiParticipantInfo(Object[] row) {
//		ParticipantSummary participant = prepareParticipantInfo(row);
//		if(row[3] != null){
//			participant.setFirstName((String) row[3]);
//		}
//		if(row[4] != null){
//			participant.setLastName((String) row[4]);
//		}
//		return participant;
//	}
	
	private Criteria getCprListQuery(CprListCriteria cprCrit) {
		Criteria query = getSessionFactory().getCurrentSession().createCriteria(CollectionProtocolRegistration.class);
		query.createAlias("collectionProtocol", "cp");
		query.createAlias("participant", "participant");
		
		query.add(Restrictions.eq("cp.id", cprCrit.cpId()));
		query.add(Restrictions.ne("activityStatus", "Disabled"));
		query.add(Restrictions.ne("participant.activityStatus", "Disabled"));

		query.addOrder(Order.asc("id"));
		query.setFirstResult(cprCrit.startAt() < 0 ? 0 : cprCrit.startAt());
		query.setMaxResults(cprCrit.maxResults() < 0 || cprCrit.maxResults() > 100 ? 100 : cprCrit.maxResults());
		
		String searchTerm = cprCrit.query();
		boolean isSearchTermSpecified = !StringUtils.isBlank(searchTerm);
		if (!isSearchTermSpecified) {
			return query;
		}
		
		Junction searchCrit = Restrictions.disjunction()
					.add(Restrictions.ilike("protocolParticipantIdentifier", searchTerm, MatchMode.ANYWHERE));			
		if (cprCrit.includePhi()) {				
			searchCrit.add(Restrictions.ilike("participant.firstName", searchTerm, MatchMode.ANYWHERE));
			searchCrit.add(Restrictions.ilike("participant.lastName", searchTerm, MatchMode.ANYWHERE));
		}
			
		query.add(searchCrit);
		return query;		
	}
	
	private ProjectionList getCprSummaryFields(CprListCriteria cprCrit) {
		ProjectionList projs = Projections.projectionList()
				.add(Projections.property("id"))
				.add(Projections.property("protocolParticipantIdentifier"))
				.add(Projections.property("registrationDate"))
				.add(Projections.property("participant.id"));
		
		if (cprCrit.includePhi()) {
			projs.add(Projections.property("participant.firstName"))
				.add(Projections.property("participant.lastName"));				
		}
		
		return projs;		
	}
	
	private CprSummary getCprSummary(Object[] row) {
		CprSummary cpr = new CprSummary();
		cpr.setCprId((Long)row[0]);
		cpr.setPpid((String)row[1]);
		cpr.setRegistrationDate((Date)row[2]);
		
		ParticipantSummary participant = new ParticipantSummary();
		cpr.setParticipant(participant);			
		participant.setId((Long)row[3]);
		if (row.length > 4) {
			participant.setFirstName((String)row[4]);
			participant.setLastName((String)row[5]);
		}
		
		return cpr;
	}
	
	private List<Object[]> getScgAndSpecimenCounts(Long cpId, int startAt, int maxResults) {
		Query countQuery = getSessionFactory().getCurrentSession().getNamedQuery(GET_SCG_AND_SPECIMEN_CNT);
		countQuery.setLong("cpId", cpId);
		
		if (startAt <= 0) {
			startAt = 0;
		}
		
		if (maxResults <= 0 || maxResults > 100) {
			maxResults = 100;
		}
		
		return countQuery.setFirstResult(startAt).setMaxResults(maxResults).list();
	}
	
	
	private static final String FQN = CollectionProtocolRegistration.class.getName();
	
	private static final String GET_SCG_AND_SPECIMEN_CNT = FQN + ".getScgAndSpecimenCount";
	
	private static final String GET_REGISTRATION_BY_PARTICIPANT_ID = FQN + ".getRegistrationByParticipantId";
	
	private static final String GET_SUB_REGISTRATIONS_BY_PARTICIPANT_AND_CP_ID = FQN + ".getSubRegistrationByParticipantAndCPId";
	
	private static final String GET_PARTICIPANT_BY_CP_PARTICIPANT_ID = FQN + ".getParticipantByCPAndParticipantId";
	
	private static final String GET_PHI_PARTICIPANT_BY_CP_PARTICIPANT_ID = FQN + ".getPhiParticipantByCPAndParticipantId";

	private static final String GET_PARTICIPANTS_BY_CP_ID = FQN + ".getParticipantsByCpId";

	private static final String GET_PARTICIPANTS_BY_CP_ID_AND_SEARCH_TERM = FQN + ".getParticipantsByCpIdAndSearchTerm";
	
	private static final String GET_PHI_PARTICIPANTS_BY_CP_ID = FQN + ".getPhiParticipantsByCpId";

	private static final String GET_PHI_PARTICIPANTS_BY_CP_ID_AND_SEARCH_TERM = FQN + ".getPhiParticipantsByCpIdAndSearchTerm";

	private static final String GET_COLLECTION_GROUPSBY_CPR_ID = FQN + ".getCollectionGroupsByCprId";

	private static final String GET_CPR_ID_BY_BARCODE = FQN + ".getCprIdByBarcode";

	private static final String GET_CPID_BY_PPID_AND_CPID = FQN + ".getCprIdByPpid";

	private static final String GET_CPR_BY_PPID_AND_CPID = FQN + ".getCprByPpid";

}
