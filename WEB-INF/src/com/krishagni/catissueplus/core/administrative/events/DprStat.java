package com.krishagni.catissueplus.core.administrative.events;

import java.math.BigDecimal;

public class DprStat {
	private Long distributedCnt;

	private BigDecimal distributedQty;

	public Long getDistributedCnt() {
		return distributedCnt;
	}

	public void setDistributedCnt(Long distributedCnt) {
		this.distributedCnt = distributedCnt;
	}

	public BigDecimal getDistributedQty() {
		return distributedQty;
	}

	public void setDistributedQty(BigDecimal distributedQty) {
		this.distributedQty = distributedQty;
	}
}
