package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.SpecimenRequest;
import com.krishagni.catissueplus.core.administrative.events.SpecimenRequestSummary;
import com.krishagni.catissueplus.core.administrative.repository.SpecimenRequestDao;
import com.krishagni.catissueplus.core.administrative.repository.SpecimenRequestListCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenRequestDaoImpl extends AbstractDao<SpecimenRequest> implements SpecimenRequestDao {
	public Class<SpecimenRequest> getType() {
		return SpecimenRequest.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SpecimenRequestSummary> getSpecimenRequests(SpecimenRequestListCriteria listCrit) {
		Criteria fieldsQuery = getListQuery(listCrit);
		addSummaryFieldsProjection(fieldsQuery);

		List<SpecimenRequestSummary> results = new ArrayList<SpecimenRequestSummary>();
		Map<Long, SpecimenRequestSummary> reqMap = new HashMap<Long, SpecimenRequestSummary>();
		for (Object[] row : (List<Object[]>)fieldsQuery.list()) {
			SpecimenRequestSummary req = getRequest(row);
			results.add(req);

			if (listCrit.includeStat()) {
				reqMap.put(req.getId(), req);
			}
		}

		if (!listCrit.includeStat()) {
			return results;
		}

		Criteria countQuery = getListQuery(listCrit);
		addCountProjection(countQuery);
		for (Object[] row : (List<Object[]>)countQuery.list()) {
			Long reqId = (Long)row[0];
			Integer count = ((Number)row[1]).intValue();
			reqMap.get(reqId).setRequestedSpecimensCount(count);
		}

		return results;
	}

	private Criteria getListQuery(SpecimenRequestListCriteria listCrit) {
		Criteria query = getSessionFactory().getCurrentSession()
			.createCriteria(SpecimenRequest.class)
			.createAlias("cp", "cp")
			.createAlias("requestor", "requestor")
			.setFirstResult(listCrit.startAt())
			.setMaxResults(listCrit.maxResults())
			.addOrder(Order.desc("id"));

		addCpCond(query, listCrit);
		addRequestorCond(query, listCrit);
		addSiteCond(query, listCrit);
		return query;
	}

	private void addCpCond(Criteria query, SpecimenRequestListCriteria listCrit) {
		if (listCrit.cpId() == null) {
			return;
		}

		query.add(Restrictions.eq("cp.id", listCrit.cpId()));
	}

	private void addRequestorCond(Criteria query, SpecimenRequestListCriteria listCrit) {
		if (listCrit.requestorId() == null) {
			return;
		}

		query.add(Restrictions.eq("requestor.id", listCrit.requestorId()));
	}

	private void addSiteCond(Criteria query, SpecimenRequestListCriteria listCrit) {
		if (CollectionUtils.isEmpty(listCrit.siteIds())) {
			return;
		}

		query.createAlias("cp.sites", "cpSite")
			.createAlias("cpSite.site", "site")
			.add(Restrictions.in("site.id", listCrit.siteIds()));
	}

	private void addSummaryFieldsProjection(Criteria query) {
		ProjectionList projs = Projections.projectionList()
			.add(Projections.property("id"))
			.add(Projections.property("dateOfRequest"))
			.add(Projections.property("cp.id"))
			.add(Projections.property("cp.shortTitle"))
			.add(Projections.property("cp.title"))
			.add(Projections.property("requestor.firstName"))
			.add(Projections.property("requestor.lastName"))
			.add(Projections.property("requestor.emailAddress"))
			.add(Projections.property("activityStatus"));

		query.setProjection(projs);
	}

	private SpecimenRequestSummary getRequest(Object[] row) {
		SpecimenRequestSummary req = new SpecimenRequestSummary();

		int idx = 0;
		req.setId((Long)row[idx++]);
		req.setDateOfRequest((Date)row[idx++]);

		CollectionProtocolSummary cp = new CollectionProtocolSummary();
		cp.setId((Long)row[idx++]);
		cp.setShortTitle((String)row[idx++]);
		cp.setTitle((String)row[idx++]);
		req.setCp(cp);

		UserSummary user = new UserSummary();
		user.setFirstName((String)row[idx++]);
		user.setLastName((String)row[idx++]);
		user.setEmailAddress((String)row[idx++]);
		req.setRequestor(user);

		req.setActivityStatus((String)row[idx++]);
		return req;
	}

	private void addCountProjection(Criteria query) {
		query.createAlias("specimens", "specimen");

		query.setProjection(Projections.projectionList()
			.add(Projections.property("id"))
			.add(Projections.count("specimen.id"))
			.add(Projections.groupProperty("id")))
			.list();
	}
}
