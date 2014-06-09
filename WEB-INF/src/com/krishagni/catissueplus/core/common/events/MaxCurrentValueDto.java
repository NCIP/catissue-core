
package com.krishagni.catissueplus.core.common.events;

public class MaxCurrentValueDto {

	private Long currentValue;

	private Long maxValue;

	public Long getCurrentValue() {
		return currentValue;
	}

	public Long getMaxValue() {
		return maxValue;
	}

	public void setCurrentValue(Long currentValue) {
		this.currentValue = currentValue;
	}

	public void setMaxValue(Long maxValue) {
		this.maxValue = maxValue;
	}

	public MaxCurrentValueDto(Long currentValue, Long maxValue) {
		this.currentValue = currentValue;
		this.maxValue = maxValue;
	}

	public void incrementCurrentValue() {
		this.currentValue++;
	}

}
