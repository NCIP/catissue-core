
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
	public Site getSite(Long id) {
		return (Site)sessionFactory.getCurrentSession().get(Site.class, id);
	}
	
	private static final String FQN = Site.class.getName();

	private static final String GET_SITE_BY_NAME = FQN + ".getSiteByName";

}
