
package com.krishagni.catissueplus.core.biospecimen.events;

public class CollectionProtocolSummary {

	private Long id;

	private String shortTitle;

	private String title;

	private String ppidFormat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPpidFormat() {
		return ppidFormat;
	}

	public void setPpidFormat(String ppidFormat) {
		this.ppidFormat = ppidFormat;
	}

}
