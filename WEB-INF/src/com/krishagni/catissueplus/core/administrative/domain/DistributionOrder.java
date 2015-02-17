package com.krishagni.catissueplus.core.administrative.domain;

public class DistributionOrder extends Order {
	public enum DistributionAction {
		DISTRIBUTED,
		DISTRIBUTED_AND_CLOSED 
	};
	
	private DistributionAction distributionAction;
	
	private DistributionProtocol distributionProtocol;
	
	private User distributor;

	public DistributionAction getDistributionAction() {
		return distributionAction;
	}

	public void setDistributionAction(DistributionAction distributionAction) {
		this.distributionAction = distributionAction;
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
	
	public void update(DistributionOrder other) {
		setName(other.name);
		setRequester(other.requester);
		setDistributor(other.distributor);
		setActivityStatus(other.activityStatus);
		setDistributionProtocol(other.distributionProtocol);
		setRequestedDate(other.requestedDate);
		
	}
}
