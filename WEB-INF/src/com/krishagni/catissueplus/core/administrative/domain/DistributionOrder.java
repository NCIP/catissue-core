package com.krishagni.catissueplus.core.administrative.domain;

public class DistributionOrder extends Order {
	private DistributionProtocol distributionProtocol;
	
	private User distributor;

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
	
	public static boolean isDistributionStatusValid(String status) {
		if (PENDING.equals(status) || 
				DISTRIBUTED.equals(status) || 
				DISTRIBUTED_AND_CLOSED.equals(status)) {
			return true;
		}
		
		return false;
	}

	public void update(DistributionOrder other) {
		setName(other.name);
		setRequester(other.requester);
		setDistributor(other.distributor);
		setStatus(other.status);
		setDistributionProtocol(other.distributionProtocol);
		setRequestedDate(other.requestedDate);
	}
}
