package com.krishagni.catissueplus.core.administrative.events;

import java.io.File;

public class JobExportDetail {

	private ScheduledJobRunDetail jobRun;
	
	private File file;
	
	public JobExportDetail(ScheduledJobRunDetail jobRun, File file) {
		this.jobRun = jobRun;
		this.file = file;
	}

	public ScheduledJobRunDetail getJobRun() {
		return jobRun;
	}

	public void setJobRun(ScheduledJobRunDetail jobRun) {
		this.jobRun = jobRun;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
