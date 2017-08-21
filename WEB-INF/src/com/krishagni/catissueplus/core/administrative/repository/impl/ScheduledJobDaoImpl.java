package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.administrative.events.JobRunsListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.ScheduledJobDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

public class ScheduledJobDaoImpl extends AbstractDao<ScheduledJob> implements ScheduledJobDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<ScheduledJob> getScheduledJobs(ScheduledJobListCriteria criteria) {
		Criteria query = getScheduledJobsListQuery(criteria)
				.setFirstResult(criteria.startAt())
				.setMaxResults(criteria.maxResults())
				.addOrder(Order.desc("id"));

		return query.list();
	}

	@Override
	public Long getScheduledJobsCount(ScheduledJobListCriteria crit) {
		Number count = (Number) getScheduledJobsListQuery(crit)
				.setProjection(Projections.rowCount())
				.uniqueResult();
		return count.longValue();
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
	public ScheduledJobRun getJobRun(Long id) {
		return (ScheduledJobRun) sessionFactory.getCurrentSession().get(ScheduledJobRun.class, id);
	}

	@Override
	public void saveOrUpdateJobRun(ScheduledJobRun job) {
		sessionFactory.getCurrentSession().saveOrUpdate(job);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ScheduledJobRun> getJobRuns(JobRunsListCriteria listCriteria) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(ScheduledJobRun.class)
				.setFirstResult(listCriteria.startAt())
				.setMaxResults(listCriteria.maxResults())
				.addOrder(Order.desc("id"));
		
		if (listCriteria.scheduledJobId() != null) {
			criteria.createAlias("scheduledJob", "job");
			criteria.add(Restrictions.eq("job.id", listCriteria.scheduledJobId()));
		}
		
		return criteria.list();
	}

	@Override
	public Class getType() {
		return ScheduledJob.class;
	}

	private Criteria getScheduledJobsListQuery(ScheduledJobListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession()
				.createCriteria(ScheduledJob.class)
				.add(Restrictions.eq("activityStatus",Status.ACTIVITY_STATUS_ACTIVE.getStatus()));

		String searchTerm = crit.query();
		if (StringUtils.isNotBlank(searchTerm)) {
			query.add(Restrictions.ilike("name", searchTerm, MatchMode.ANYWHERE));
		}
		
		return query;
	}

	private static final String FQN = ScheduledJob.class.getName();

	private static final String GET_JOB_BY_NAME = FQN + ".getJobByName";
}
