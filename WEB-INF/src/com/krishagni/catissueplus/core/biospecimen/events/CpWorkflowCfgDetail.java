package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig.Workflow;

public class CpWorkflowCfgDetail {
	private Long cpId;
	
	private String shortTitle;
	
	private Map<String, WorkflowDetail> workflows = new HashMap<String, WorkflowDetail>();
	
	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public Map<String, WorkflowDetail> getWorkflows() {
		return workflows;
	}

	public void setWorkflows(Map<String, WorkflowDetail> workflows) {
		this.workflows = workflows;
	}
	
	public static CpWorkflowCfgDetail from(CpWorkflowConfig cfg) {
		CpWorkflowCfgDetail result = new CpWorkflowCfgDetail();

		if (cfg.getCp() != null) {
			result.setCpId(cfg.getCp().getId());
			result.setShortTitle(cfg.getCp().getShortTitle());
		}

		for (Workflow workflow : cfg.getWorkflows().values()) {
			WorkflowDetail wfDetail = new WorkflowDetail();
			BeanUtils.copyProperties(workflow, wfDetail);
			result.getWorkflows().put(wfDetail.getName(), wfDetail);			
		}
		
		return result;
	}
	
	public static class WorkflowDetail {
		private String name;
		
		private String view;
		
		private String ctrl;
		
		private Map<String, Object> data = new HashMap<String, Object>();
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getView() {
			return view;
		}

		public void setView(String view) {
			this.view = view;
		}

		public String getCtrl() {
			return ctrl;
		}

		public void setCtrl(String ctrl) {
			this.ctrl = ctrl;
		}

		public Map<String, Object> getData() {
			return data;
		}

		public void setData(Map<String, Object> data) {
			this.data = data;
		}		
	}
}
