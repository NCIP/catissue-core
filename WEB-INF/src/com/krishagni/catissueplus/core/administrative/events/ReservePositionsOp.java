package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

public class ReservePositionsOp {
	private Long cpId;

	private String reservationToCancel;

	private List<TenantDetail> tenants = new ArrayList<>();

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getReservationToCancel() {
		return reservationToCancel;
	}

	public void setReservationToCancel(String reservationToCancel) {
		this.reservationToCancel = reservationToCancel;
	}

	public List<TenantDetail> getTenants() {
		return tenants;
	}

	public void setTenants(List<TenantDetail> tenants) {
		this.tenants = tenants;
	}
}
