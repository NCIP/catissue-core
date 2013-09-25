/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.processingprocedure;

import java.util.Comparator;

import edu.wustl.catissuecore.domain.processingprocedure.Action;

public class SPPActionComparator implements Comparator<Action>
{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Action action1, Action action2)
	{
		int returnValue;
		if(action1.getActionOrder()<action2.getActionOrder())
		{
			returnValue = -1;
		}
		else if(action1.getActionOrder()>action2.getActionOrder())
		{
			returnValue = 1;
		}
		else
		{
			returnValue = 0;
		}
		return returnValue;
	}
}
