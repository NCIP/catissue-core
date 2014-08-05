
package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllBiohazardsEvent extends ResponseEvent {

	private List<BiohazardDetails> biohazards;

	public List<BiohazardDetails> getBiohazards() {
		return biohazards;
	}

	public void setBiohazards(List<BiohazardDetails> biohazards) {
		this.biohazards = biohazards;
	}

	public static AllBiohazardsEvent ok(List<BiohazardDetails> biohazards) {
		AllBiohazardsEvent resp = new AllBiohazardsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setBiohazards(biohazards);
		return resp;
	}

}
