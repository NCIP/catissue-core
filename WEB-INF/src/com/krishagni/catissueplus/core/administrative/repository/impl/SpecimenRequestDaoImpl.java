package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.administrative.repository.SpecimenRequestDao;
import com.krishagni.catissueplus.core.administrative.repository.SpecimenRequestListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenRequestDaoImpl extends AbstractDao<SpecimenRequest> implements SpecimenRequestDao {
	public Class<SpecimenRequest> getType() {
		return SpecimenRequest.class;
	}

	@Override
	public List<SpecimenRequest> getSpecimenRequests(SpecimenRequestListCriteria listCrit) {
		Criteria query = getSessionFactory().getCurrentSession()
			.createCriteria(SpecimenRequest.class)
			.setFirstResult(listCrit.startAt())
			.setMaxResults(listCrit.maxResults())
			.addOrder(Order.desc("id"));

		addRequestorCond(query, listCrit);
		addSiteCond(query, listCrit);
		return query.list();
	}

	private void addRequestorCond(Criteria query, SpecimenRequestListCriteria listCrit) {
		if (listCrit.requestorId() == null) {
			return;
		}

		query.createAlias("requestor", "requestor")
			.add(Restrictions.eq("requestor.id", listCrit.requestorId()));
	}

	private void addSiteCond(Criteria query, SpecimenRequestListCriteria listCrit) {
		if (CollectionUtils.isEmpty(listCrit.siteIds())) {
			return;
		}

		query.createAlias("cp", "cp")
			.createAlias("cp.sites", "cpSite")
			.createAlias("cpSite.site", "site")
			.add(Restrictions.in("site.id", listCrit.siteIds()));
	}
}
