
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.util.global.Constants;

public class CollectionProtocolDaoImpl extends AbstractDao<CollectionProtocol> implements CollectionProtocolDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<CollectionProtocolSummary> getAllCollectionProtocols(boolean includePi, boolean includeStats) {
		List<CollectionProtocolSummary> cpList = new ArrayList<CollectionProtocolSummary>();
		Map<Long, CollectionProtocolSummary> cpMap = new HashMap<Long, CollectionProtocolSummary>();
		
		List<Object[]> rows = getCpList(includePi);
		for (Object[] row : rows) {
			CollectionProtocolSummary cp = getCp(row, includePi);
			if (includeStats) {
				cpMap.put(cp.getId(), cp);
			}
			
			cpList.add(cp);
		}
		
		if (includeStats) {
			rows = getSessionFactory().getCurrentSession()
					.getNamedQuery(GET_PARTICIPANT_N_SPECIMEN_CNT)
					.list();
			
			for (Object[] row : rows) {
				Long cpId = (Long)row[0];
				CollectionProtocolSummary cp = cpMap.get(cpId);
				cp.setParticipantCount((Long)row[1]);
				cp.setSpecimenCount((Long)row[2]);			
			}			
		}
				
		return cpList;
	}
	
	private List<Object[]> getCpList(boolean includePi) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(CollectionProtocol.class);
		query.add(Restrictions.eq("activityStatus", Constants.ACTIVITY_STATUS_ACTIVE));
		
		ProjectionList projs = Projections.projectionList();
		query.setProjection(projs);
		
		projs.add(Projections.property("id"));
		projs.add(Projections.property("shortTitle"));
		projs.add(Projections.property("title"));
		projs.add(Projections.property("startDate"));
		
		if (includePi) {
			query.createAlias("principalInvestigator", "pi");
			projs.add(Projections.property("pi.id"));
			projs.add(Projections.property("pi.firstName"));
			projs.add(Projections.property("pi.lastName"));
		}
		
		return query.addOrder(Order.asc("title")).list();
	}
	
	private CollectionProtocolSummary getCp(Object[] fields, boolean includePi) {
		CollectionProtocolSummary cp = new CollectionProtocolSummary();
		cp.setId((Long)fields[0]);
		cp.setShortTitle((String)fields[1]);
		cp.setTitle((String)fields[2]);
		cp.setStartDate((Date)fields[3]);
		
		if (includePi) {
			UserSummary user = new UserSummary();
			user.setId((Long)fields[4]);
			user.setFirstName((String)fields[5]);
			user.setLastName((String)fields[6]);
			cp.setPrincipalInvestigator(user);
		}
		
		return cp;		
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public CollectionProtocol getCollectionProtocol(String cpTitle) {
		List<CollectionProtocol> cpList = sessionFactory.getCurrentSession()
				.createCriteria(CollectionProtocol.class)
				.add(Restrictions.eq("title", cpTitle))
				.list();
		return cpList == null || cpList.isEmpty() ? null : cpList.iterator().next();
	}

	@Override
	public CollectionProtocol getCollectionProtocol(Long cpId) {
		return (CollectionProtocol) sessionFactory.getCurrentSession()
				.get(CollectionProtocol.class.getName(), cpId);
	}
	
	@Override
	public CollectionProtocolEvent getCpe(Long cpeId) {
		return (CollectionProtocolEvent) sessionFactory.getCurrentSession()
				.get(CollectionProtocolEvent.class.getName(), cpeId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CollectionProtocolEvent getCpeByEventLabel(Long cpId, String label) {
		List<CollectionProtocolEvent> events = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CPE_BY_CP_AND_LABEL)
				.setLong("cpId", cpId)
				.setString("label", label)
				.list();
		
		return events != null && !events.isEmpty() ? events.iterator().next() : null;
	}

	@Override
	public SpecimenRequirement getSpecimenRequirement(Long requirementId) {
		return (SpecimenRequirement) sessionFactory.getCurrentSession()
				.get(SpecimenRequirement.class, requirementId);
	}
	
	private static final String FQN = CollectionProtocol.class.getName();
	
	private static final String GET_PARTICIPANT_N_SPECIMEN_CNT = FQN + ".getParticipantAndSpecimenCount";
	
	private static final String GET_CPE_BY_CP_AND_LABEL = FQN + ".getCpeByTitleAndEventLabel";

	
//
//	@Override
//	public CollectionProtocolEvent getCpe(Long cpeId) {
//		return (CollectionProtocolEvent) sessionFactory.getCurrentSession().get(CollectionProtocolEvent.class, cpeId);
//	}
//
//	@Override
//	public SpecimenRequirement getSpecimenRequirement(Long requirementId) {
//		return (SpecimenRequirement) sessionFactory.getCurrentSession().get(SpecimenRequirement.class, requirementId);
//	}
//
//	@Override
//	@SuppressWarnings(value = {"unchecked"})
//	public com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol getCPByTitle(String cpTitle) {
//		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CP_BY_NAME);
//		query.setString("cpTitle", cpTitle);
//		List<com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol> cpList = query.list();
//		return cpList.isEmpty() ? null : cpList.get(0);
//	}
//
//	@Override
//	@SuppressWarnings(value = {"unchecked"})
//	public com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol getCPByShortTitle(String cpShortTitle) {
//		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CP_BY_SHORT_NAME);
//		query.setString("cpShortTitle", cpShortTitle);
//		List<com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol> cpList = query.list();
//		return cpList.isEmpty() ? null : cpList.get(0);
//	}
//
//	@Override
//	@SuppressWarnings("unchecked")
//	public CollectionProtocol getCpByShortTitle(String shortTitle) {
//		List<CollectionProtocol> cps = sessionFactory.getCurrentSession().getNamedQuery(GET_OLD_CP_BY_SHORT_NAME)
//				.setString("cpShortTitle", shortTitle).list();
//		return cps.size() > 0 ? cps.get(0) : null;
//	}
//	
//	@Override
//	@SuppressWarnings("unchecked")
//	public CollectionProtocol getCpByTitle(String title) {
//		List<CollectionProtocol> cps = sessionFactory.getCurrentSession().getNamedQuery(GET_OLD_CP_BY_NAME).setString("cpTitle", title).list();
//		return cps.size() > 0 ? cps.get(0) : null;
//	}
//
//	@Override
//	public List<SpecimenRequirement> getSpecimenRequirments(Long cpeId) {
//			Object object = sessionFactory.getCurrentSession().get(CollectionProtocolEvent.class.getName(), cpeId);
//			if (object == null) {
//				return Collections.emptyList();
//			}
//			CollectionProtocolEvent cpe = (CollectionProtocolEvent) object;
//			return new ArrayList<SpecimenRequirement>(cpe.getSpecimenRequirementCollection());
//	}
//
//	@Override
//	public List<CollectionProtocolSummary> getChildProtocols(Long cpId) {
//		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CHILD_CPS);
//		query.setLong("parentId", cpId);
//		query.setString("activityStatus", Constants.ACTIVITY_STATUS_ACTIVE); 
//		List<Object[]> rows = query.list();
//		List<CollectionProtocolSummary> cps = new ArrayList<CollectionProtocolSummary>();
//		for (Object[] row : rows) {
//			CollectionProtocolSummary cp = new CollectionProtocolSummary();
//			cp.setId((Long) row[0]);
//			cp.setShortTitle((String) row[1]);
//			cp.setTitle((String) row[2]);
//			cp.setPpidFormat((String) row[3]);
//			cp.setCpType((String)row[4]);
//			cps.add(cp);
//		}
//
//		return cps;
//	}
//	
//	@Override
//	public CollectionProtocolEvent getCpeByCollectionPointLabel(Long cpId, String collectionPointLabel) {
//		List<CollectionProtocolEvent> cpeList = sessionFactory.getCurrentSession().getNamedQuery(GET_CPE_BY_CP_ID_AND_COLLECTION_POINT_LABEL).setLong("cpId", cpId)
//		.setString("collectionPointLabel", collectionPointLabel).list();
//		
//		return cpeList.size() > 0 ? cpeList.get(0) : null;
//	}
//	
//	private static final String FQN = CollectionProtocol.class.getName();
//
//	private static final String GET_ALL_CPS = FQN + ".getAllProtocols";
//	
//	private static final String GET_CHILD_CPS = FQN + ".getChildProtocols";
//	
//	private static final String GET_OLD_CP_BY_NAME = FQN + ".getOldCpByTitle";
//	
//	private static final String GET_CP_BY_NAME = com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.class
//			.getName() + ".getCpByTitle";
//
//	private static final String GET_OLD_CP_BY_SHORT_NAME = FQN + ".getOldCpByShortTitle";
//	
//	private static final String GET_CP_BY_SHORT_NAME = com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.class
//			.getName() + ".getCpByShortTitle";
//	
//	private static final String GET_CPE_BY_CP_ID_AND_COLLECTION_POINT_LABEL = com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.class
//			.getName()+ ".getCpeByCpIdAndCpl";

}
