package com.krishagni.catissueplus.core.administrative.events;

public class RequirementDetail {
	private Long id;
	private String type;
	private String anatomicSite;
	private String pathology;
	private Long specimenReq;
	private Long specimenDistributed;
	private double price;
	private String comments;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAnatomicSite() {
		return anatomicSite;
	}

	public void setAnatomicSite(String anatomicSite) {
		this.anatomicSite = anatomicSite;
	}

	public String getPathology() {
		return pathology;
	}

	public void setPathology(String pathology) {
		this.pathology = pathology;
	}

	public Long getSpecimenReq() {
		return specimenReq;
	}

	public void setSpecimenReq(Long specimenReq) {
		this.specimenReq = specimenReq;
	}

	public Long getSpecimenDistributed() {
		return specimenDistributed;
	}

	public void setSpecimenDistributed(Long specimenDistributed) {
		this.specimenDistributed = specimenDistributed;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public RequirementDetail() {
	}

	public RequirementDetail(Long id, String type, String site, String pathology, Long req, Long dis, double price, String comment) {
		this.id = id;
		this.type = type;
		this.anatomicSite = site;
		this.pathology = pathology;
		this.specimenReq = req;
		this.specimenDistributed = dis;
		this.price = price;
		this.comments = comment;
	}
}