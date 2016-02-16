
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
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CpListCriteria;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

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
	public List<CollectionProtocol> getCpsByShortTitle(Collection<String> shortTitles, String siteName) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CPS_BY_SHORT_TITLE_N_SITE)
				.setParameterList("shortTitles", shortTitles)
				.setString("siteName", siteName)
				.list();
	}
	
	@SuppressWarnings("unchecked")
 	@Override
	public List<CollectionProtocol> getExpiringCps(Date fromDate, Date toDate) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_EXPIRING_CPS)
				.setDate("fromDate", fromDate)
				.setDate("toDate", toDate)
				.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CollectionProtocol getCpByCode(String code) {
		List<CollectionProtocol> cps = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CP_BY_CODE)
				.setString("code", code)
				.list();
		return CollectionUtils.isEmpty(cps) ? null : cps.iterator().next();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getCpIdsBySiteIds(Collection<Long> siteIds) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CP_IDS_BY_SITE_IDS)
				.setParameterList("siteIds", siteIds)
				.list();
	}

	@Override
	public Map<String, Object> getCpIds(String key, Object value) {
		return getObjectIds("cpId", key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getSiteIdsByCpIds(Collection<Long> cpIds) {
		return getSessionFactory().getCurrentSession()
				.getNamedQuery(GET_SITE_IDS_BY_CP_IDS)
				.setParameterList("cpIds", cpIds)
				.list();
	}	
	
	@Override
	public CollectionProtocolEvent getCpe(Long cpeId) {
		List<CollectionProtocolEvent> events = getCpes(Collections.singleton(cpeId));
		return CollectionUtils.isEmpty(events) ? null : events.iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CollectionProtocolEvent> getCpes(Collection<Long> cpeIds) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CPE_BY_IDS)
				.setParameterList("cpeIds", cpeIds)
				.list();
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
				.getNamedQuery(GET_CPE_BY_CP_TITLE_AND_LABEL)
				.setString("title", title)
				.setString("label", label)
				.list();
		
		return CollectionUtils.isEmpty(events) ? null : events.iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CollectionProtocolEvent getCpeByShortTitleAndEventLabel(String shortTitle, String label) {
		List<CollectionProtocolEvent> events = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CPE_BY_CP_SHORT_TITLE_AND_LABEL)
				.setString("shortTitle", shortTitle)
				.setString("label", label)
				.list();
		
		return CollectionUtils.isEmpty(events) ? null : events.iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CollectionProtocolEvent getCpeByCode(String shortTitle, String code) {
		List<CollectionProtocolEvent> events = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CPE_BY_CODE)
				.setString("shortTitle", shortTitle)
				.setString("code", code)
				.list();
		
		return CollectionUtils.isEmpty(events) ? null : events.iterator().next();
	}

	@Override
	public int getMinEventPoint(Long cpId) {
		Number minEventPoint = (Number)sessionFactory.getCurrentSession()
			.getNamedQuery(GET_MIN_CPE_CAL_POINT)
			.setLong("cpId", cpId)
			.uniqueResult();

		return minEventPoint != null ? minEventPoint.intValue() : 0;
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

	@SuppressWarnings("unchecked")
	@Override	
	public SpecimenRequirement getSrByCode(String code) {
		List<SpecimenRequirement> srs = sessionFactory.getCurrentSession()
			.getNamedQuery(GET_SR_BY_CODE)
			.setString("code", code)
			.list();
		
		return CollectionUtils.isEmpty(srs) ? null : srs.iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CpWorkflowConfig> getCpWorkflows(Collection<Long> cpIds) {
		return sessionFactory.getCurrentSession()
			.createCriteria(CpWorkflowConfig.class)
			.add(Restrictions.in("id", cpIds))
			.list();
	}

	@Override
	public void saveCpWorkflows(CpWorkflowConfig cfg) {
		sessionFactory.getCurrentSession().saveOrUpdate(cfg);
	}

	@Override
	@SuppressWarnings("unchecked")
	public CpWorkflowConfig getCpWorkflows(Long cpId) {		
		List<CpWorkflowConfig> cfgs = getCpWorkflows(Collections.singleton(cpId));
		return CollectionUtils.isEmpty(cfgs) ? null : cfgs.iterator().next();
	}

	@Override
	public ConsentTier getConsentTier(Long consentId) {
		return (ConsentTier) sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CONSENT_TIER)
				.setLong("consentId", consentId)
				.uniqueResult();
	}
	
	@Override
	public ConsentTier getConsentTierByStatement(Long cpId, String statement) {
		return (ConsentTier) sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CONSENT_TIER_BY_STATEMENT)
				.setLong("cpId", cpId)
				.setString("statement", statement);
	}
	
	@Override
	public int getConsentRespsCount(Long consentId) {
		return ((Number)sessionFactory.getCurrentSession()
				.getNamedQuery(GET_CONSENT_RESP_COUNT)
				.setLong("consentId", consentId)
				.uniqueResult()).intValue();
	}
		
	@Override
	public Class<CollectionProtocol> getType() {
		return CollectionProtocol.class;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object[]> getCpList(CpListCriteria cpCriteria) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(CollectionProtocol.class)
				.setFirstResult(cpCriteria.startAt())
				.setMaxResults(cpCriteria.maxResults())
				.add(Restrictions.eq("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()))
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
			query.createCriteria("sites", "cpSite")
				.createAlias("cpSite.site", "site")
				.add(Restrictions.eq("site.name", repositoryName));
		}

		applyIdsFilter(query, "id", cpCriteria.ids());
	}
	
	private void addProjections(Criteria query, CpListCriteria cpCriteria) {
		ProjectionList projs = Projections.projectionList();
		query.setProjection(projs);
		
		projs.add(Projections.property("id"));
		projs.add(Projections.property("shortTitle"));
		projs.add(Projections.property("title"));
		projs.add(Projections.property("code"));
		projs.add(Projections.property("startDate"));
		projs.add(Projections.property("ppidFormat"));
		projs.add(Projections.property("manualPpidEnabled"));

		if (cpCriteria.includePi()) {
			projs.add(Projections.property("pi.id"));
			projs.add(Projections.property("pi.firstName"));
			projs.add(Projections.property("pi.lastName"));
			projs.add(Projections.property("pi.loginName"));
		}
	}

	private CollectionProtocolSummary getCp(Object[] fields, boolean includePi) {
		int idx = 0;
		CollectionProtocolSummary cp = new CollectionProtocolSummary();		
		cp.setId((Long)fields[idx++]);
		cp.setShortTitle((String)fields[idx++]);
		cp.setTitle((String)fields[idx++]);
		cp.setCode((String)fields[idx++]);
		cp.setStartDate((Date)fields[idx++]);
		cp.setPpidFmt((String)fields[idx++]);
		cp.setManualPpidEnabled((Boolean)fields[idx++]);
		if (includePi) {
			UserSummary user = new UserSummary();
			user.setId((Long)fields[idx++]);
			user.setFirstName((String)fields[idx++]);
			user.setLastName((String)fields[idx++]);
			user.setLoginName((String)fields[idx++]);
			cp.setPrincipalInvestigator(user);
		}
		
		return cp;		
	}
	
	private static final String FQN = CollectionProtocol.class.getName();
	
	private static final String GET_PARTICIPANT_N_SPECIMEN_CNT = FQN + ".getParticipantAndSpecimenCount";
	
	private static final String GET_CPE_BY_CP_AND_LABEL = FQN + ".getCpeByCpIdAndEventLabel";
	
	private static final String GET_CPE_BY_CP_TITLE_AND_LABEL = FQN + ".getCpeByTitleAndEventLabel";
	
	private static final String GET_CPE_BY_CP_SHORT_TITLE_AND_LABEL = FQN + ".getCpeByShortTitleAndEventLabel";
	
	private static final String GET_CP_BY_TITLE = FQN + ".getCpByTitle";
	
	private static final String GET_CPS_BY_SHORT_TITLE = FQN + ".getCpsByShortTitle";
	
	private static final String GET_CPS_BY_SHORT_TITLE_N_SITE = FQN + ".getCpsByShortTitleAndSite";
	
	private static final String GET_EXPIRING_CPS = FQN + ".getExpiringCps";
	
	private static final String GET_CP_BY_CODE = FQN + ".getByCode";
	
	private static final String GET_CP_IDS_BY_SITE_IDS = FQN + ".getCpIdsBySiteIds";
	
	private static final String GET_SITE_IDS_BY_CP_IDS = FQN + ".getRepoIdsByCps";
	
	private static final String GET_CONSENT_TIER = FQN + ".getConsentTier";

	private static final String GET_CONSENT_TIER_BY_STATEMENT = FQN + ".getConsentTierByStatement";
	
	private static final String GET_CONSENT_RESP_COUNT = FQN + ".getConsentResponsesCount";
		
	private static final String CPE_FQN = CollectionProtocolEvent.class.getName();
	
	private static final String GET_CPE_BY_IDS = CPE_FQN + ".getCpeByIds";
	
	private static final String GET_CPE_BY_CODE = CPE_FQN + ".getByCode";

	private static final String GET_MIN_CPE_CAL_POINT = CPE_FQN + ".getMinEventPoint";
	
	private static final String SR_FQN = SpecimenRequirement.class.getName();
	
	private static final String GET_SR_BY_CODE = SR_FQN + ".getByCode";
}
