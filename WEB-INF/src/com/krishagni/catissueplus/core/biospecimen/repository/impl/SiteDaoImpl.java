
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import com.krishagni.catissueplus.core.biospecimen.repository.SiteDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.Site;

public class SiteDaoImpl extends AbstractDao<Site> implements SiteDao {

	@Override
	public Site getSite(String name) {
		
		return null;
	}

	@Override
	public Site getSite(Long id) {
		// TODO Auto-generated method stub
		return (Site)sessionFactory.getCurrentSession().get(Site.class, id);
	}

}
