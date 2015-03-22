
package com.krishagni.catissueplus.core.administrative.repository.impl;

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
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Site.class)
				.add(Restrictions.eq("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()))
				.addOrder(Order.asc("name"))
				.setFirstResult(listCrit.startAt())
				.setMaxResults(listCrit.maxResults());
				
		if (StringUtils.isNotBlank(listCrit.query())) {
			MatchMode mathMode = listCrit.exactMatch() ? MatchMode.EXACT : MatchMode.ANYWHERE;
			query.add(Restrictions.ilike("name", listCrit.query(), mathMode));
		}
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Site getSiteByName(String siteName) {
		List<Site> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SITE_BY_NAME)
				.setString("siteName", siteName)
				.list();
		
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
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getSiteDependencyStat(Long siteId) {
		return sessionFactory.getCurrentSession()
				.createSQLQuery(GET_SITE_DEPENDENCY_STAT_SQL)
				.setLong("siteId", siteId)
				.list();
	}
	
	private static final String GET_SITE_DEPENDENCY_STAT_SQL = 
			"select 'Visit' as entityName, count(*) as count from catissue_specimen_coll_group scg where scg.site_id = :siteId " +
			"Union " +
			"select 'Storage Container' as entityName, count(*) as count from os_storage_containers sc where sc.site_id = :siteId " +
			"Union " +
			"select 'Collection Protocol' as entityName, count(*) as count from catissue_site_cp cp where cp.site_id = :siteId " +
			"union " +
			"select 'MRN' as entityName, count(*) as count from catissue_part_medical_id pm where pm.site_id = :siteId ";
			

	private static final String FQN = Site.class.getName();

	private static final String GET_SITE_BY_NAME = FQN + ".getSiteByName";
	
	private static final String GET_SITE_BY_CODE = FQN + ".getSiteByCode";

}
