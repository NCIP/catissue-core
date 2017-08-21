
package com.krishagni.catissueplus.core.common.service.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.events.ListPvCriteria;
import com.krishagni.catissueplus.core.administrative.events.PvDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.PermissibleValueService;

public class PermissibleValueServiceImpl implements PermissibleValueService {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<PvDetail>> getPermissibleValues(RequestEvent<ListPvCriteria> req) {
		ListPvCriteria crit = req.getPayload();		
		List<PermissibleValue> pvs = daoFactory.getPermissibleValueDao().getPvs(crit);
 		return ResponseEvent.response(PvDetail.from(pvs, crit.includeParentValue()));
	}

}
