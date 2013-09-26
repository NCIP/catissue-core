/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.uiobject;

import java.util.Map;

import edu.wustl.common.domain.UIObject;

public class StorageContainerUIObject implements UIObject {

	/**
	 * startNo of containers.
	 */
	private Integer startNo;

	/**
	 * Number of containers.
	 */
	private Integer noOfContainers;

	/**
	 * Map of similarContainerMap.
	 */
	private Map<String, String> similarContainerMap;

	/**
	 * Boolean isParentChanged.
	 */
	private boolean isParentChanged;

	/**
	 * positionChanged.
	 */
	private Boolean positionChanged=Boolean.FALSE;

	/**
	 * @return Returns the isParentChanged.
	 */
	public boolean isParentChanged() {
		return this.isParentChanged;
	}

	/**
	 * @param isParentChanged
	 *            The isParentChanged to set.
	 */
	public void setParentChanged(boolean isParentChanged) {
		this.isParentChanged = isParentChanged;
	}

	/**
	 * @return Returns the noOfContainers.
	 */
	public Integer getNoOfContainers() {
		return this.noOfContainers;
	}

	/**
	 * @param noOfContainers
	 *            The noOfContainers to set.
	 */
	public void setNoOfContainers(Integer noOfContainers) {
		this.noOfContainers = noOfContainers;
	}

	/**
	 * @return Returns the similarContainerMap.
	 */
	public Map<String, String> getSimilarContainerMap() {
		return this.similarContainerMap;
	}

	/**
	 * @param similarContainerMap
	 *            The similarContainerMap to set.
	 */
	public void setSimilarContainerMap(Map<String, String> similarContainerMap) {
		this.similarContainerMap = similarContainerMap;
	}

	/**
	 * @return Returns the startNo.
	 */
	public Integer getStartNo() {
		return this.startNo;
	}

	/**
	 * @param startNo
	 *            The startNo to set.
	 */
	public void setStartNo(Integer startNo) {
		this.startNo = startNo;
	}

	/**
	 * @return Returns the positionChanged.
	 */
	public Boolean isPositionChanged() {
		return this.positionChanged;
	}

	/**
	 * @param positionChanged
	 *            The positionChanged to set.
	 */
	public void setPositionChanged(Boolean positionChanged) {
		this.positionChanged = positionChanged;
	}

}
