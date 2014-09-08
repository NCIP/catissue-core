
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllSpecimensEvent extends ResponseEvent {

	private List<SpecimenDetail> specimensInfo;

	private List<SpecimenInfo> info;

	private Long count;

	public Long getCount() {
		return count;
	}

	public void setCount(Long conut) {
		this.count = conut;
	}

	public List<SpecimenDetail> getSpecimensInfo() {
		return specimensInfo;
	}

	public void setSpecimensInfo(List<SpecimenDetail> specimensInfo) {
		this.specimensInfo = specimensInfo;
	}

	public List<SpecimenInfo> getInfo() {
		return info;
	}

	public void setInfo(List<SpecimenInfo> info) {
		this.info = info;
	}

	public static AllSpecimensEvent ok(List<SpecimenDetail> specimensInfo, Long count) {
		AllSpecimensEvent event = new AllSpecimensEvent();
		event.setSpecimensInfo(specimensInfo);
		event.setCount(count);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static AllSpecimensEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		AllSpecimensEvent resp = new AllSpecimensEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static AllSpecimensEvent badRequest(String msg, Throwable t) {
		AllSpecimensEvent resp = new AllSpecimensEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(msg);
		resp.setException(t);
		return resp;
	}

	public static AllSpecimensEvent ok(List<SpecimenInfo> specimensList) {
		AllSpecimensEvent event = new AllSpecimensEvent();
		event.setInfo(specimensList);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
