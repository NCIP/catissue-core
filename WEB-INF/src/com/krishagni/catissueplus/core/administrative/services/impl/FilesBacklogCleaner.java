package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

public class FilesBacklogCleaner implements ScheduledTask {

	private static final int period = 30; //30 days
	
	@Override
	public void doJob(ScheduledJobRun jobRun) 
	throws Exception {
		cleanupOlderFiles();
	}
	
	private void cleanupOlderFiles() {
		Calendar timeBefore = Calendar.getInstance();
		timeBefore.setTime(new Date());
		timeBefore.add(Calendar.DATE, -period);
		Long timeInMilliseconds = timeBefore.getTimeInMillis();
		
		cleanupFolder(getExportDataDir(), timeInMilliseconds);
	}
	
	private void cleanupFolder(String directory, Long timeBefore) {
		try {
			File dir = new File(directory);
		
			if (!dir.isDirectory()) {
				return;
			}
			
			File[] files = dir.listFiles();
			for (File file : files) {
				if (!file.isDirectory() && (file.lastModified() < timeBefore)) {
					file.delete();
				} 
			}
		} catch (Exception e) {
			
		}
	}
	
	private String getExportDataDir() {
		String dir = new StringBuilder()
			.append(ConfigUtil.getInstance().getDataDir()).append(File.separator)			
			.append("scheduled-jobs").append(File.separator)
			.toString();
		
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				throw new RuntimeException("Error couldn't create directory for exporting scheduled jobs data");
			}
		}
		
		return dir;
	}
}
