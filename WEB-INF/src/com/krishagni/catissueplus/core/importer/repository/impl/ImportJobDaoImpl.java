package com.krishagni.catissueplus.core.importer.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
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

		if (crit.params() != null && !crit.params().isEmpty()) {
			query.createAlias("params", "params");

			Disjunction orCond = Restrictions.disjunction();
			query.add(orCond);

			for (Map.Entry<String, String> kv : crit.params().entrySet()) {
				orCond.add(Restrictions.and(
					Restrictions.eq("params.indices", kv.getKey()),
					Restrictions.eq("params.elements", kv.getValue())
				));
			}
		}
						
		return query.list();
	}
}
