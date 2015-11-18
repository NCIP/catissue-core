package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.DpRequirement;
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
	public Map<Long, BigDecimal> getDistributedQtyByDp(Long dpId) {
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
