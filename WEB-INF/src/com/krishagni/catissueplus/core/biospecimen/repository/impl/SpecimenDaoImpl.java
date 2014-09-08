
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

public class SpecimenDaoImpl extends AbstractDao<Specimen> implements SpecimenDao {


	
	@Override
	public List<Specimen> getAllSpecimens(int startAt, int maxRecords, String... searchString) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Specimen.class, "s")
				.add(Restrictions.or(
						Restrictions.eq("s.activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()),
						Restrictions.eq("s.activityStatus", Status.ACTIVITY_STATUS_CLOSED.getStatus())));
		
		addSearchConditions(criteria, searchString);
		criteria.addOrder(Order.asc("s.label"));
		addLimits(criteria, startAt, maxRecords);
		return getSpecimens(criteria);
	}
	
	@Override
	public Long getSpecimensCount(String... searchString) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Specimen.class, "s")
				.add(Restrictions.or(
						Restrictions.eq("s.activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()),
						Restrictions.eq("s.activityStatus", Status.ACTIVITY_STATUS_CLOSED.getStatus())))
				.setProjection(Projections.countDistinct("s.id"));
		
		addSearchConditions(criteria, searchString);
		return ((Number)criteria.uniqueResult()).longValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getScgId(Long specimenId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SCG_ID_BY_SPECIMEN_ID);
		query.setLong("specimenId", specimenId);

		List<Long> rows = query.list();
		if (rows != null && !rows.isEmpty()) {
			return rows.iterator().next();
		}
		else {
			return null;
		}
	}

	@Override
	public Specimen getSpecimen(Long id) {
		return (Specimen)sessionFactory.getCurrentSession().get(Specimen.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Specimen getSpecimenByLabel(String label) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_BY_LABEL);
		query.setString("label", label);
		List<Specimen> results = query.list();
		return results.isEmpty()? null: results.get(0);
	}
	
	@Override
	public boolean isLabelUnique(String label) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_BY_LABEL);
		query.setString("label", label);
		return query.list().isEmpty()?true:false;
	}

	@Override
	public boolean isBarcodeUnique(String barcode) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SPECIMEN_ID_BY_BARCODE);
		query.setString("barcode", barcode);
		return query.list().isEmpty()?true:false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Specimen> getSpecimensByLabel(List<String> labels) {
		List<Specimen> specimens = new ArrayList<Specimen>();
		
		int i = 0;
		int numLabels = labels.size();
		while (i < numLabels) {
			List<String> params = labels.subList(i, i + 500 > numLabels ? numLabels : i + 500);
			i += 500;
			
			specimens.addAll(
				sessionFactory.getCurrentSession()
					.getNamedQuery(GET_SPECIMENS_BY_LABEL)
					.setParameterList("labels", params)
					.list());					
		}
		
		return specimens;
	}
	
	private void addSearchConditions(Criteria criteria, String[] searchString) {
		if (searchString == null || searchString.length == 0 || StringUtils.isBlank(searchString[0])) {
			return;
		}
		
		Disjunction srchCond = Restrictions.disjunction();
		srchCond.add(Restrictions.or(
				Restrictions.ilike("s.label", searchString[0], MatchMode.ANYWHERE),
				Restrictions.ilike("s.barcode", searchString[0], MatchMode.ANYWHERE)
				));
		criteria.add(srchCond);
	}
	
	
	private void addLimits(Criteria criteria, int start, int maxRecords) {
		criteria.setFirstResult(start <= 0 ? 0 : start);
		if (maxRecords > 0) {
			criteria.setMaxResults(maxRecords);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Specimen> getSpecimens(Criteria criteria) {
		List<Specimen> result = criteria.list();
		return result;		
	}

	private static final String FQN = Specimen.class.getName();

	private static final String GET_SCG_ID_BY_SPECIMEN_ID = FQN + ".getScgIdBySpecimenId";

	private static final String GET_SPECIMEN_ID_BY_BARCODE = FQN +".getSpecimenIdByBarcode";
	
	private static final String GET_SPECIMEN_BY_LABEL = FQN +".getSpecimenByLabel";
	
	private static final String GET_SPECIMENS_BY_LABEL = FQN + ".getSpecimensByLabel";


}
