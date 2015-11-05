package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.DpRequirement;
import com.krishagni.catissueplus.core.administrative.events.DpRequirementDetail;
import com.krishagni.catissueplus.core.administrative.events.DpRequirementListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.DpRequirementDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DpRequirementDaoImpl extends AbstractDao<DpRequirement>
		implements DpRequirementDao {
	
	@Override
	public Class<DpRequirement> getType() {
		return DpRequirement.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DpRequirementDetail> getRequirements(DpRequirementListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(DpRequirement.class);
		
		addListConditions(query, crit);
		List<DpRequirement> requirements = query.list();
		List<DpRequirementDetail> reqDetails = DpRequirementDetail.from(requirements);
		
		if (crit.includeDistQty() && crit.dpId() != null) {
			Map<Long, BigDecimal> distQtys = getDistributionQty(crit.dpId());
			
			for (DpRequirementDetail detail : reqDetails) {
				BigDecimal qty = distQtys.get(detail.getId());
				detail.setDistributedQty(qty == null ? new BigDecimal(0) : qty);
			}
		}
		
		return reqDetails;
	}
	
	private void addListConditions(Criteria query, DpRequirementListCriteria crit) {
		if(crit.dpId() != null) {
			query.createAlias("distributionProtocol", "dp")
			.add(Restrictions.eq("dp.id", crit.dpId()));
		}
		
		if (!StringUtils.isBlank(crit.specimenType())) {
			query.add(Restrictions.eq("specimenType", crit.specimenType()));
		}
		
		if (!StringUtils.isBlank(crit.anatomicSite())) {
			query.add(Restrictions.eq("anatomicSite", crit.anatomicSite()));
		}
		
		if (!StringUtils.isBlank(crit.pathologyStatus())) {
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
	
	private static final String FQN = DpRequirement.class.getName();
	
	private static final String GET_DISTRIBUTION_QTY = FQN + ".getDistributionQty";
	
}
