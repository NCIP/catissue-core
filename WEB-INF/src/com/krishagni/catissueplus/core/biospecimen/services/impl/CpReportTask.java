package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CpReportSettings;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

@Configurable
public class CpReportTask implements ScheduledTask, Runnable {
	private static final Log logger = LogFactory.getLog(CpReportTask.class);

	@Autowired
	private DaoFactory daoFactory;

	private Long cpId;

	public CpReportTask() {

	}

	public CpReportTask(Long cpId) {
		this.cpId = cpId;
	}

	@Override
	public void doJob(ScheduledJobRun jobRun)
	throws Exception {
		CpReportSettings sysSettings = getSysRptSetting();
		for (Long cpId : getAllCpIds()) {
			generateCpReport(cpId, sysSettings);
		}
	}

	@Override
	public void run() {
		generateCpReport(cpId, null);
	}

	@PlusTransactional
	private List<Long> getAllCpIds() {
		return daoFactory.getCollectionProtocolDao().getAllCpIds();
	}

	@PlusTransactional
	private void generateCpReport(Long cpId, CpReportSettings sysSettings) {
		try {
			CpReportSettings cpSettings = daoFactory.getCpReportSettingsDao().getByCp(cpId);
			if (cpSettings != null && !cpSettings.isEnabled()) {
				return;
			}

			if (sysSettings == null) {
				sysSettings = getSysRptSetting();
			}

			AuthUtil.setCurrentUser(daoFactory.getUserDao().getSystemUser());
			CollectionProtocol cp = daoFactory.getCollectionProtocolDao().getById(cpId);
			new CpReportGenerator().generateReport(cp, sysSettings, cpSettings);
		} catch (Exception e) {
			logger.error("Error generating report for collection protocol: " + cpId, e);
		}
	}

	private CpReportSettings getSysRptSetting() {
		try {
			String cfg = ConfigUtil.getInstance().getFileContent(ConfigParams.MODULE, ConfigParams.SYS_RPT_SETTINGS, null);

			CpReportSettings settings = new CpReportSettings();
			if (StringUtils.isNotBlank(cfg)) {
				settings = new ObjectMapper().readValue(cfg, CpReportSettings.class);
			}

			return settings;
		} catch (Exception e) {
			logger.error("Error reading system level CP report settings", e);
			throw new RuntimeException("Error reading system level CP report settings", e);
		}
	}
}
