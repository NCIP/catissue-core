/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.metadata;


public class AttributeDiscriminator
{
	private String name;
	private String value;
	
	/**
	 * @return the discriminatorName
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @param discriminatorName the discriminatorName to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @return the discriminatorValue
	 */
	public String getValue()
	{
		return value;
	}
	
	/**
	 * @param discriminatorValue the discriminatorValue to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
}
