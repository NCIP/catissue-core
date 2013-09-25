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

import edu.wustl.common.domain.UIObject;

public class DisposalEventParametersUIObject implements UIObject
{
	/**
	 * activityStatus.
	 */
	protected String activityStatus;

	/**
	 * Get Activity Status.
	 * @return String.
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * Set Activity Status.
	 * @param activityStatus String.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}
}
