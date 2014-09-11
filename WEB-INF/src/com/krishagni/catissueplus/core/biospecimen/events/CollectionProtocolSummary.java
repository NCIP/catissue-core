
package com.krishagni.catissueplus.core.biospecimen.events;

public class CollectionProtocolSummary implements Comparable<CollectionProtocolSummary>{

	private Long id;

	private String shortTitle;

	private String title;

	private String ppidFormat;

	private String cpType;

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

	public String getCpType() {
		return cpType;
	}

	public void setCpType(String cpType) {
		this.cpType = cpType;
	}

	@Override
	public int compareTo(CollectionProtocolSummary cpSummary) {
		return this.shortTitle.toUpperCase().compareTo(cpSummary.getShortTitle().toUpperCase());
	}

}
