package com.krishagni.catissueplus.core.biospecimen.events;

public class CopyCpOpDetail {
	private CollectionProtocolDetail cp;
	
	private Long cpId;

	public CollectionProtocolDetail getCp() {
		return cp;
	}

	public void setCp(CollectionProtocolDetail cp) {
		this.cp = cp;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

}
