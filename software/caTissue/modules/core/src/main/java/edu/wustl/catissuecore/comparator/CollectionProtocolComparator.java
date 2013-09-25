/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.catissuecore.comparator;

import java.util.Comparator;

import edu.wustl.catissuecore.domain.CollectionProtocol;


/**
 * @author atul_kaushal
 *
 */
public class CollectionProtocolComparator implements Comparator<CollectionProtocol>
{

	public int compare(CollectionProtocol arg0, CollectionProtocol arg1)
	{
		int result = 0;
		if (arg0.getClass().getName().equals(arg1.getClass().getName()))
		{
			final CollectionProtocol collectionProtocol = (CollectionProtocol) arg1;
			result = arg0.getId().compareTo(collectionProtocol.getId());
		}
		return result;
	}

}
