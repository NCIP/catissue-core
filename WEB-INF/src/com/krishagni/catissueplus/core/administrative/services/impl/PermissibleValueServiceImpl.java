
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.events.ListPvCriteria;
import com.krishagni.catissueplus.core.administrative.events.PvDetail;
import com.krishagni.catissueplus.core.administrative.services.PermissibleValueService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

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

	@Override
	public Boolean validate(String attr, List<String> values, String ... parentAttr) {
		//Commenting code as database doesn't containing all attribute and PV values.
		//TODO: Include all attributes and their respective PV values through script.  
		/*if (event.getParentValue() != null
				&& daoFactory.getPermissibleValueDao().isPvAvailable(event.getAttribute(), event.getParentValue(),
						event.getValues().get(0))) {
			return false;
		} else {
			List<String> values = daoFactory.getPermissibleValueDao().getAllValuesByAttribute(event.getAttribute());
			for (String value : event.getValues()) {
				if (!values.contains(value)) {
					return false;
				}
			}
		}*/

		return true;
	}
}
