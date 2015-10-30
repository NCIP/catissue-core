package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocolRequirement;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolRequirementDetail;
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
	public List<DistributionProtocolRequirementDetail> getRequirements(DistributionProtocolRequirementListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DistributionProtocolRequirement.class)
				.setFirstResult(crit.startAt() < 0 ? 0 : crit.startAt())
				.setMaxResults(crit.maxResults() < 0 || crit.maxResults() > 100 ? 100 : crit.maxResults());
		
		addListConditions(query, crit);
		List<DistributionProtocolRequirement> requirements = query.list();
		List<DistributionProtocolRequirementDetail> reqDetails = DistributionProtocolRequirementDetail.from(requirements);
		
		if (crit.includeDistQty()) {
			Map<Long, BigDecimal> distQtys = getDistributionQty(crit.dpId());
			
			for (DistributionProtocolRequirementDetail detail : reqDetails) {
				BigDecimal qty = distQtys.get(detail.getId());
				detail.setDistributedQty(qty == null ? new BigDecimal(0) : qty);
			}
		}
		
		return reqDetails;
	}
	
	private void addListConditions(Criteria query, DistributionProtocolRequirementListCriteria crit) {
		if(crit.dpId() != null) {
			query.createAlias("distributionProtocol", "dp")
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
	
	@SuppressWarnings("unchecked")
	private Map<Long, BigDecimal> getDistributionQty(Long dpId) {
		List<Object []> data = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DISTRIBUTION_QTY)
				.setLong("dpId", dpId)
				.list();
		
		Map<Long, BigDecimal> result = new HashMap<Long, BigDecimal>();
		for (Object []row : data) {
			result.put((Long)row[0], (BigDecimal)row[1]);
		}
		
		return result;
	}
	
	private static final String FQN = DistributionProtocolRequirement.class.getName();
	
	private static final String GET_DISTRIBUTION_QTY = FQN + ".getDistributionQty";
	
}
