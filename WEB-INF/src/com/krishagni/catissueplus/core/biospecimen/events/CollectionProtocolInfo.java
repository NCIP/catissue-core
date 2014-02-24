
package com.krishagni.catissueplus.core.biospecimen.events;

import edu.wustl.catissuecore.domain.CollectionProtocol;

public class CollectionProtocolInfo {

	private String shortTitle;

	private Long id;

	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static CollectionProtocolInfo fromCollectionProtocol(CollectionProtocol collectionProtocol) {
		CollectionProtocolInfo collectionProtocolInfo = new CollectionProtocolInfo();
		collectionProtocolInfo.setId(collectionProtocol.getId());
		collectionProtocolInfo.setShortTitle(collectionProtocol.getShortTitle());
		collectionProtocolInfo.setTitle(collectionProtocol.getTitle());
		return collectionProtocolInfo;
	}

	public CollectionProtocolInfo(String shortTitle, Long id, String title) {
		super();
		this.shortTitle = shortTitle;
		this.id = id;
		this.title = title;
	}

	public CollectionProtocolInfo() {
		super();
	}

}
