package com.krishagni.catissueplus.core.administrative.domain;

public class DistributionOrder extends Order {
	public enum DistributionStatus {
		DISTRIBUTED,
		DISTRIBUTED_AND_CLOSED 
	};
	
	private DistributionStatus distributionStatus;
	
	private DistributionProtocol distributionProtocol;
	
	private User distributor;

	public DistributionStatus getDistributionStatus() {
		return distributionStatus;
	}

	public void setDistributionStatus(DistributionStatus distributionStatus) {
		this.distributionStatus = distributionStatus;
	}

	public DistributionProtocol getDistributionProtocol() {
		return distributionProtocol;
	}

	public void setDistributionProtocol(DistributionProtocol distributionProtocol) {
		this.distributionProtocol = distributionProtocol;
	}

	public User getDistributor() {
		return distributor;
	}

	public void setDistributor(User distributor) {
		this.distributor = distributor;
	}
}
