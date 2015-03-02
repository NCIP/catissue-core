package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobInstance;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.ScheduledJobDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

public class ScheduledJobDaoImpl extends AbstractDao<ScheduledJob> implements ScheduledJobDao {
	@Override
	public List<ScheduledJob> getScheduledJobs(ScheduledJobListCriteria criteria) {
		Criteria query = sessionFactory.getCurrentSession()
				.createCriteria(ScheduledJob.class)
				.add(Restrictions.eq("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()))
				.setFirstResult(criteria.startAt())
                .setMaxResults(criteria.maxResults());

		String searchTerm = criteria.query();
         if (!StringUtils.isBlank(searchTerm)) {
                 query.add(Restrictions.ilike("name", searchTerm, MatchMode.ANYWHERE));
         }

         return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ScheduledJob getJobByName(String name) {
		List<ScheduledJob> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_JOB_BY_NAME)
				.setString("name", name)
				.list();
		
		return result.isEmpty() ? null : result.iterator().next();
	}
	
	@Override
	public ScheduledJobInstance getScheduledJobInstance(Long id) {
		return (ScheduledJobInstance) sessionFactory.getCurrentSession().get(ScheduledJobInstance.class, id);
	}

	@Override
	public void saveScheduledJobInstance(ScheduledJobInstance job) {
		sessionFactory.getCurrentSession().saveOrUpdate(job);
	}
	
	@Override
	public Class getType() {
		return ScheduledJob.class;
	}
	
	private static final String FQN = ScheduledJob.class.getName();
	
	private static final String GET_JOB_BY_NAME = FQN + ".getJobByName";
}
