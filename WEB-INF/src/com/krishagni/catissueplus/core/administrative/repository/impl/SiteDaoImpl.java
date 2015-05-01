
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.repository.SiteDao;
import com.krishagni.catissueplus.core.administrative.repository.SiteListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

public class SiteDaoImpl extends AbstractDao<Site> implements SiteDao {
	
	@Override
	public Class<?> getType() {
		return Site.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Site> getSites(SiteListCriteria listCrit) {
		Criteria query = sessionFactory.getCurrentSession()
				.createCriteria(Site.class, "s")
				.add(Restrictions.eq("s.activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()))
				.addOrder(Order.asc("s.name"))
				.setFirstResult(listCrit.startAt())
				.setMaxResults(listCrit.maxResults());
				
		addSearchConditions(query, listCrit);

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Site> getSitesByNames(List<String> siteNames) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SITES_BY_NAMES)
				.setParameterList("siteNames", siteNames)
				.list();
	}
	
	@Override
	public Site getSiteByName(String siteName) {
		List<Site> result = getSitesByNames(Collections.singletonList(siteName));
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Site getSiteByCode(String siteCode) {
		List<Site> result = getSessionFactory().getCurrentSession()
				.getNamedQuery(GET_SITE_BY_CODE)
				.setString("siteCode", siteCode)
				.list();
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	private void addSearchConditions(Criteria query, SiteListCriteria listCrit) {
		MatchMode mathMode = listCrit.exactMatch() ? MatchMode.EXACT : MatchMode.ANYWHERE;
		
		if (StringUtils.isNotBlank(listCrit.query())) {
			query.add(Restrictions.ilike("s.name", listCrit.query(), mathMode));
		}
		
		if (StringUtils.isNotBlank(listCrit.institute())) {
			query.createAlias("s.institute", "institute")
				.add(Restrictions.ilike("institute.name", listCrit.institute(), mathMode));	
		}
	}
			
	private static final String FQN = Site.class.getName();

	private static final String GET_SITES_BY_NAMES = FQN + ".getSitesByNames";
	
	private static final String GET_SITE_BY_CODE = FQN + ".getSiteByCode";

}
