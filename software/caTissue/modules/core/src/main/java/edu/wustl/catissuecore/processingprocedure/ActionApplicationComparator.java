package edu.wustl.catissuecore.processingprocedure;

import java.util.Comparator;

import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.domain.processingprocedure.ActionApplication;

public class ActionApplicationComparator implements Comparator<ActionApplication>
{

	@Override
	public int compare(ActionApplication actionApp1, ActionApplication actionApp2)
	{
		int returnValue;
		if(((Action)actionApp1.getApplicationRecordEntry().getFormContext()).getActionOrder()
				<((Action)actionApp2.getApplicationRecordEntry().getFormContext()).getActionOrder())
		{
			returnValue = -1;
		}
		else if(((Action)actionApp1.getApplicationRecordEntry().getFormContext()).getActionOrder()
				>((Action)actionApp2.getApplicationRecordEntry().getFormContext()).getActionOrder())
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
