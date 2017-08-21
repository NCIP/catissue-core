package com.krishagni.catissueplus.core.common.domain;

import java.util.Date;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class UpgradeLog extends BaseEntity {
	private String version;
	
	private Date upgradeDate;
	
	private String upgradedBy;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getUpgradeDate() {
		return upgradeDate;
	}

	public void setUpgradeDate(Date upgradeDate) {
		this.upgradeDate = upgradeDate;
	}

	public String getUpgradedBy() {
		return upgradedBy;
	}

	public void setUpgradedBy(String upgradedBy) {
		this.upgradedBy = upgradedBy;
	}
}