
package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ListPvCriteria;
import com.krishagni.catissueplus.core.administrative.events.PvDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface PermissibleValueService {
	ResponseEvent<List<PvDetail>> getPermissibleValues(RequestEvent<ListPvCriteria> req);
	
	Boolean validate(String attr, List<String> values, String ... parentAttr);
}
