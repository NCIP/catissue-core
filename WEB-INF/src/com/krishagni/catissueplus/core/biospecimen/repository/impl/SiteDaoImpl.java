
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.biospecimen.repository.SiteDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.Site;

public class SiteDaoImpl extends AbstractDao<Site> implements SiteDao {

	@Override
	@SuppressWarnings("unchecked")
	public Site getSite(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SITE_BY_NAME);
		query.setString("name", name);
		List<Site> siteList = query.list();
		return !siteList.isEmpty() ? siteList.get(0):null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public com.krishagni.catissueplus.core.biospecimen.domain.Site getSiteByName(String siteName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_SITE_BY_SITE_NAME);
		query.setString("siteName", siteName);
		List<com.krishagni.catissueplus.core.biospecimen.domain.Site> siteList = query.list();
		return siteList.isEmpty()? null : siteList.get(0);
	}

	@Override
	public Site getSite(Long id) {
		return (Site)sessionFactory.getCurrentSession().get(Site.class, id);
	}
	
	private static final String FQN = Site.class.getName(); //To Do remove the mapping from participant and all from old site

	private static final String GET_SITE_BY_NAME = FQN + ".getSiteByName";

	private static final String GET_SITE_BY_SITE_NAME = com.krishagni.catissueplus.core.biospecimen.domain.Site.class
			.getName() + ".getSiteByName";

}
