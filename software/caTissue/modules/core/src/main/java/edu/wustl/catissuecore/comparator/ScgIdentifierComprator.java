/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.comparator;

import java.util.Comparator;

import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;


public class ScgIdentifierComprator implements Comparator <SpecimenCollectionGroup> 
{
	public int compare(SpecimenCollectionGroup arg0, SpecimenCollectionGroup arg1)
	{
		Long id1=arg0.getId();
		Long id2=arg1.getId();
		int  returnId=0;
		
		if(arg0.getEncounterTimestamp()==null && arg1.getEncounterTimestamp()==null)
		{
			if (id1 != null && id2 != null)
			{
				returnId = id1.compareTo(id2);
			}
			else if (id1 == null && id2 == null)
			{
				returnId = 0;
			}
			else if (id1 == null)
			{
				returnId = 1;
			}
			else if (id2 == null)
			{
				returnId = -1;
			}
		}
		return returnId;
		
	}
}
