package com.krishagni.catissueplus.core.importer.repository.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.importer.domain.ImportJob;
import com.krishagni.catissueplus.core.importer.repository.ImportJobDao;
import com.krishagni.catissueplus.core.importer.repository.ListImportJobsCriteria;

public class ImportJobDaoImpl extends AbstractDao<ImportJob> implements ImportJobDao {
	
	public Class<ImportJob> getType() {
		return ImportJob.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ImportJob> getImportJobs(ListImportJobsCriteria crit) {
		int startAt = crit.startAt() <= 0 ? 0 : crit.startAt();
		int maxResults = crit.maxResults() <= 0 || crit.maxResults() > 100 ? 100 : crit.maxResults();
		
		Criteria query = sessionFactory.getCurrentSession().createCriteria(ImportJob.class)
				.setFirstResult(startAt)
				.setMaxResults(maxResults)
				.addOrder(Order.desc("id"));
		
		if (crit.userId() != null) {
			query.createAlias("createdBy", "createdBy")
				.add(Restrictions.eq("createdBy.id", crit.userId()));
		}
		
		if (CollectionUtils.isNotEmpty(crit.objectTypes())) {
			query.add(Restrictions.in("name", crit.objectTypes()));
		}
						
		return query.list();
	}
}
