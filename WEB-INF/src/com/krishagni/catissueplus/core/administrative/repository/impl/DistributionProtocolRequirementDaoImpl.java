package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocolRequirement;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolRequirementListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.DistributionProtocolRequirementDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DistributionProtocolRequirementDaoImpl extends AbstractDao<DistributionProtocolRequirement>
		implements DistributionProtocolRequirementDao {
	
	@Override
	public Class<DistributionProtocolRequirement> getType() {
		return DistributionProtocolRequirement.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DistributionProtocolRequirement> getRequirements(DistributionProtocolRequirementListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DistributionProtocolRequirement.class)
				.setFirstResult(crit.startAt() < 0 ? 0 : crit.startAt())
				.setMaxResults(crit.maxResults() < 0 || crit.maxResults() > 100 ? 100 : crit.maxResults());
		
		addListConditions(query, crit);
		
		return query.list();
	}
	
	private void addListConditions(Criteria query, DistributionProtocolRequirementListCriteria crit) {
		if(crit.dpId() != null) {
			query.createAlias("dp", "dp")
			.add(Restrictions.eq("dp.id", crit.dpId()));
		}
		
		if (!StringUtils.isEmpty(crit.specimenType())) {
			query.add(Restrictions.eq("specimenType", crit.specimenType()));
		}
		
		if (!StringUtils.isEmpty(crit.anatomicSite())) {
			query.add(Restrictions.eq("anatomicSite", crit.anatomicSite()));
		}
		
		if (!StringUtils.isEmpty(crit.pathologyStatus())) {
			query.add(Restrictions.eq("pathologyStatus", crit.pathologyStatus()));
		}
	}
	
}
