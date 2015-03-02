package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;

public interface ScheduledJobFactory {

	public ScheduledJob createScheduledJob(ScheduledJobDetail detail);
}
