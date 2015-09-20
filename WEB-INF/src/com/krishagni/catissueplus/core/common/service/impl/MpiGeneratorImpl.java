
package com.krishagni.catissueplus.core.common.service.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.service.MpiGenerator;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

public class MpiGeneratorImpl implements MpiGenerator {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public String generateMpi() {
		Boolean mpiEnabled = Boolean.parseBoolean(
				ConfigUtil.getInstance().getStrSetting(ConfigParams.MODULE, ConfigParams.MPI_AUTO_ENABLED, null));
		if(mpiEnabled){
			String mpiFmt = ConfigUtil.getInstance().getStrSetting(ConfigParams.MODULE, ConfigParams.MPI_PATTERN, null);

			if (StringUtils.isNotBlank(mpiFmt)) {
				Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId("MPI", "MPI");
				return String.format(mpiFmt, uniqueId.intValue());
			}
		}
		return null;
	}

}
