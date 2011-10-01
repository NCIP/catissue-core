package edu.wustl.catissuecore.sop;

import java.util.Comparator;

import edu.wustl.catissuecore.domain.sop.Action;

public class SOPActionComparator implements Comparator<Action>
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
