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

import java.util.HashMap;
import java.util.Map;

import edu.wustl.common.domain.UIObject;

public class SpecimenArrayUIObject implements UIObject
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * aliquot.
	 */
	private boolean aliquot;

	/**
	 * aliquotCount.
	 */
	private int aliquotCount;

	/**
	 * HashMap containing aliqoutMap.
	 */
	private Map aliqoutMap = new HashMap();

	/**
	 * @return Returns the aliqoutMap.
	 */
	public Map getAliqoutMap()
	{
		return this.aliqoutMap;
	}

	/**
	 * @param aliqoutMap The aliqoutMap to set.
	 */
	public void setAliqoutMap(Map aliqoutMap)
	{
		this.aliqoutMap = aliqoutMap;
	}

	/**
	 * @return Returns the aliquotCount.
	 */
	public int getAliquotCount()
	{
		return this.aliquotCount;
	}

	/**
	 * @param aliquotCount The aliquotCount to set.
	 */
	public void setAliquotCount(int aliquotCount)
	{
		this.aliquotCount = aliquotCount;
	}

	/**
	 * @return Returns the aliquot.
	 */
	public boolean isAliquot()
	{
		return this.aliquot;
	}

	/**
	 * @param aliquot The aliquot to set.
	 */
	public void setAliquot(boolean aliquot)
	{
		this.aliquot = aliquot;
	}
}
