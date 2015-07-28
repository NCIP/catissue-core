package com.krishagni.catissueplus.core.de.events;

public class FormRecordStat {
	private String level;
	
	private Long recordCount;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}
	
	public static FormRecordStat from(String level, Long recordCount) {
		FormRecordStat stat = new FormRecordStat();
		stat.setLevel(level);
		stat.setRecordCount(recordCount);
		
		return stat;
	}
}
