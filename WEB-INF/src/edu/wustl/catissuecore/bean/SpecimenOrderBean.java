package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.List;

import edu.wustl.common.beans.NameValueBean;

public class SpecimenOrderBean implements Serializable
{

	private String label;
	private String availableQty;
	private String specimenClass;
	private String specimenType;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getAvailableQty() {
		return availableQty;
	}
	public void setAvailableQty(String availableQty) {
		this.availableQty = availableQty;
	}
	public String getSpecimenClass() {
		return specimenClass;
	}
	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}
	public String getSpecimenType() {
		return specimenType;
	}
	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isConsentAvl() {
		return consentAvl;
	}
	public void setConsentAvl(boolean consentAvl) {
		this.consentAvl = consentAvl;
	}
	public List<NameValueBean> getChildSpecimens() {
		return childSpecimens;
	}
	public void setChildSpecimens(List<NameValueBean> childSpecimens) {
		this.childSpecimens = childSpecimens;
	}
	private String id;
	private boolean consentAvl;
	private List<NameValueBean> childSpecimens;
	private boolean consentWaived;
	public boolean isConsentWaived() {
		return consentWaived;
	}
	public void setConsentWaived(boolean consentWaived) {
		this.consentWaived = consentWaived;
	}
	
}
