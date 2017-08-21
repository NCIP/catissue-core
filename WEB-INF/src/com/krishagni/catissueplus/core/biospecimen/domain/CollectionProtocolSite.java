package com.krishagni.catissueplus.core.biospecimen.domain;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import com.krishagni.catissueplus.core.administrative.domain.Site;

@Audited
@AuditTable(value="CATISSUE_SITE_CP_AUD")
public class CollectionProtocolSite extends BaseEntity {
	private CollectionProtocol collectionProtocol;
	
	private Site site;
	
	private String code;

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public void update(CollectionProtocolSite other) {
		setCollectionProtocol(other.getCollectionProtocol());
		setSite(other.getSite());
		setCode(other.getCode());
	}
	
	public CollectionProtocolSite copy() {
		CollectionProtocolSite result = new CollectionProtocolSite();
		result.setSite(getSite());
		result.setCode(getCode());
		return result;
	}
}
