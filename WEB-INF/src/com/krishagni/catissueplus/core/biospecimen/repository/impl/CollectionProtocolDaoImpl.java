
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CpListCriteria;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.util.global.Constants;

public class CollectionProtocolDaoImpl extends AbstractDao<CollectionProtocol> implements CollectionProtocolDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<CollectionProtocolSummary> getCollectionProtocols(CpListCriteria cpCriteria) {
		List<CollectionProtocolSummary> cpList = new ArrayList<CollectionProtocolSummary>();
		Map<Long, CollectionProtocolSummary> cpMap = new HashMap<Long, CollectionProtocolSummary>();
		
		boolean includePi = cpCriteria.includePi();
		boolean includeStats = cpCriteria.includeStat();		
		
		List<Object[]> rows = getCpList(cpCriteria);
		for (Object[] row : rows) {
			CollectionProtocolSummary cp = getCp(row, includePi);
			if (includeStats) {
				cpMap.put(cp.getId(), cp);
			}
			
			cpList.add(cp);
		}
		
		if (includeStats && !cpMap.isEmpty()) {
			rows = getSessionFactory().getCurrentSession()
					.getNamedQuery(GET_PARTICIPANT_N_SPECIMEN_CNT)
					.setParameterList("cpIds", cpMap.keySet())
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
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public CollectionProtocol getCollectionProtocol(String cpTitle) {
		List<CollectionProtocol> cpList = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CP_BY_TITLE)
				.setString("title" , cpTitle)
				.list();
		return cpList == null || cpList.isEmpty() ? null : cpList.iterator().next();
	}
	
	@Override
	public CollectionProtocol getCpByShortTitle(String shortTitle) {
		List<CollectionProtocol> cpList = getCpsByShortTitle(Collections.singleton(shortTitle));
		return cpList == null || cpList.isEmpty() ? null : cpList.iterator().next();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CollectionProtocol> getCpsByShortTitle(Collection<String> shortTitles) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CPS_BY_SHORT_TITLE)
				.setParameterList("shortTitles", shortTitles)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getCpIdsBySiteIds(List<Long> siteIds) {
		List<Long> rows = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CP_IDS_BY_SITE_IDS)
				.setParameterList("siteIds", siteIds)
				.list();
		
		return new ArrayList<Long>(rows);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public CollectionProtocolEvent getCpeByEventLabel(String title, String label) {
		List<CollectionProtocolEvent> events = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CPY_BY_CP_TITLE_AND_LABEL)
				.setString("title", title)
				.setString("label", label)
				.list();
		
		return CollectionUtils.isEmpty(events) ? null : events.iterator().next();
	}

	@Override
	public void saveCpe(CollectionProtocolEvent cpe) {
		saveCpe(cpe, false);		
	}

	@Override
	public void saveCpe(CollectionProtocolEvent cpe, boolean flush) {
		getSessionFactory().getCurrentSession().saveOrUpdate(cpe);
		if (flush) {
			getSessionFactory().getCurrentSession().flush();
		}		
	}
	
	@Override
	public SpecimenRequirement getSpecimenRequirement(Long requirementId) {
		return (SpecimenRequirement) sessionFactory.getCurrentSession()
				.get(SpecimenRequirement.class, requirementId);
	}

	@Override
	public Class<CollectionProtocol> getType() {
		return CollectionProtocol.class;
	}
	
	private List<Object[]> getCpList(CpListCriteria cpCriteria) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(CollectionProtocol.class)
				.setFirstResult(cpCriteria.startAt())
				.setMaxResults(cpCriteria.maxResults())
				.add(Restrictions.eq("activityStatus", Constants.ACTIVITY_STATUS_ACTIVE))
				.createAlias("principalInvestigator", "pi");
		
		addSearchConditions(query, cpCriteria);
		addProjections(query, cpCriteria);
		
		return query.addOrder(Order.asc("shortTitle")).list();
	}

	private void addSearchConditions(Criteria query, CpListCriteria cpCriteria) {
		String searchString = cpCriteria.query();
		if (StringUtils.isBlank(searchString)) {
			searchString = cpCriteria.title();
		} 
		
		if (StringUtils.isNotBlank(searchString)) {
			Junction searchCond = Restrictions.disjunction()
					.add(Restrictions.ilike("title", searchString, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("shortTitle", searchString, MatchMode.ANYWHERE));
			
			if (StringUtils.isNotBlank(cpCriteria.query())) {
				searchCond.add(Restrictions.ilike("irbIdentifier", searchString, MatchMode.ANYWHERE));
			}	
			
			query.add(searchCond);
		}
		
		Long piId = cpCriteria.piId();
		if (piId != null) {
			query.add(Restrictions.eq("pi.id", piId));
		}
		
		String repositoryName = cpCriteria.repositoryName();
		if (StringUtils.isNotBlank(repositoryName)) {
			query.createCriteria("repositories", "repo")
				.add(Restrictions.eq("repo.name", repositoryName));
		}

		applyIdsFilter(query, "id", cpCriteria.ids());
	}
	
	private void addProjections(Criteria query, CpListCriteria cpCriteria) {
		ProjectionList projs = Projections.projectionList();
		query.setProjection(projs);
		
		projs.add(Projections.property("id"));
		projs.add(Projections.property("shortTitle"));
		projs.add(Projections.property("title"));
		projs.add(Projections.property("startDate"));
		
		if (cpCriteria.includePi()) {
			projs.add(Projections.property("pi.id"));
			projs.add(Projections.property("pi.firstName"));
			projs.add(Projections.property("pi.lastName"));
			projs.add(Projections.property("pi.loginName"));
		}
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
			user.setLoginName((String)fields[7]);
			cp.setPrincipalInvestigator(user);
		}
		
		return cp;		
	}
	
	private static final String FQN = CollectionProtocol.class.getName();
	
	private static final String GET_PARTICIPANT_N_SPECIMEN_CNT = FQN + ".getParticipantAndSpecimenCount";
	
	private static final String GET_CPE_BY_CP_AND_LABEL = FQN + ".getCpeByCpIdAndEventLabel";
	
	private static final String GET_CPY_BY_CP_TITLE_AND_LABEL = FQN + ".getCpeByTitleAndEventLabel";
	
	private static final String GET_CP_BY_TITLE = FQN + ".getCpByTitle";
	
	private static final String GET_CPS_BY_SHORT_TITLE = FQN + ".getCpsByShortTitle";
	
	private static final String GET_CP_IDS_BY_SITE_IDS = FQN + ".getCpIdsBySiteIds";
}
