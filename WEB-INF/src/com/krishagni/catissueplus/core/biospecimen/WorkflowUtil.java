package com.krishagni.catissueplus.core.biospecimen;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig.Workflow;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

@Configurable
public class WorkflowUtil {
	private static WorkflowUtil instance = null;

	@Autowired
	private ConfigurationService cfgSvc;

	private CpWorkflowConfig sysWorkflows;

	public static WorkflowUtil getInstance() {
		if (instance == null || instance.cfgSvc == null) {
			//
			// Defensive check instance.cfgSvc == null is added to handle
			// scenarios when app is incorrectly wired
			//
			instance = new WorkflowUtil();
			instance.listenForConfigChanges();
		}

		return instance;
	}

	public CpWorkflowConfig getSysWorkflows() {
		return getSysWorkflows0();
	}

	public Workflow getSysWorkflow(String name) {
		CpWorkflowConfig sysWorkflows = getSysWorkflows0();
		if (sysWorkflows == null) {
			return null;
		}

		return sysWorkflows.getWorkflows().get(name);
	}

	private CpWorkflowConfig getSysWorkflows0() {
		if (sysWorkflows != null) {
			return sysWorkflows;
		}

		synchronized (this) {
			if (sysWorkflows == null) {
				String config = ConfigUtil.getInstance().getFileContent(
					ConfigParams.MODULE, ConfigParams.SYS_WORKFLOWS, null);

				sysWorkflows = new CpWorkflowConfig();
				if (StringUtils.isNotBlank(config)) {
					sysWorkflows.setWorkflowsJson(config);
				}
			}
		}

		return sysWorkflows;
	}

	private void listenForConfigChanges() {
		if (cfgSvc == null) {
			return;
		}

		instance.cfgSvc.registerChangeListener(ConfigParams.MODULE, new ConfigChangeListener() {
			@Override
			public void onConfigChange(String name, String value) {
				if (StringUtils.isBlank(name) || name.equals(ConfigParams.SYS_WORKFLOWS)) {
					instance.sysWorkflows = null;
				}
			}
		});
	}
}
