
package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllPvsEvent extends ResponseEvent {

	private List<PvInfo> pvs;

	public List<PvInfo> getPvs() {
		return pvs;
	}

	public void setPvs(List<PvInfo> pvs) {
		this.pvs = pvs;
	}

	public static AllPvsEvent ok(List<PvInfo> pvs) {
		AllPvsEvent resp = new AllPvsEvent();
		resp.setStatus(EventStatus.OK);
		resp.setPvs(pvs);
		return resp;
	}

}
