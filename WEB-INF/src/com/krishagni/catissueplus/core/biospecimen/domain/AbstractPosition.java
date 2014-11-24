package com.krishagni.catissueplus.core.biospecimen.domain;

public class AbstractPosition {
	private Long id;
	
	private Long positionDimensionOne;
	
	private Long positionDimensionTwo;
	
	private String positionDimensionOneString;
	
	private String positionDimensionTwoString;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPositionDimensionOne() {
		return positionDimensionOne;
	}

	public void setPositionDimensionOne(Long positionDimensionOne) {
		this.positionDimensionOne = positionDimensionOne;
	}

	public Long getPositionDimensionTwo() {
		return positionDimensionTwo;
	}

	public void setPositionDimensionTwo(Long positionDimensionTwo) {
		this.positionDimensionTwo = positionDimensionTwo;
	}

	public String getPositionDimensionOneString() {
		return positionDimensionOneString;
	}

	public void setPositionDimensionOneString(String positionDimensionOneString) {
		this.positionDimensionOneString = positionDimensionOneString;
	}

	public String getPositionDimensionTwoString() {
		return positionDimensionTwoString;
	}

	public void setPositionDimensionTwoString(String positionDimensionTwoString) {
		this.positionDimensionTwoString = positionDimensionTwoString;
	}

}
