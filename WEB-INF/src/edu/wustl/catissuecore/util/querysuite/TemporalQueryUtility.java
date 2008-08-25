/**
 * 
 */
package edu.wustl.catissuecore.util.querysuite;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.querysuite.queryobject.DSInterval;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.TimeInterval;
import edu.wustl.common.querysuite.queryobject.YMInterval;


/**
 * @author supriya_dankh
 *
 */
public class TemporalQueryUtility
{

	public static List<String> getRelationalOperators()
	{
		/**
		 * Getting relational operators excluding those deals with Strings
		 */
		 List <String>relationalOperatorsList = new ArrayList<String>(); 
		for(RelationalOperator operator : RelationalOperator.values())
		{
			if((!operator.getStringRepresentation().equals(Constants.Contains)) &&
					(!operator.getStringRepresentation().equals(Constants.STRATS_WITH)) &&
					(!operator.getStringRepresentation().equals(Constants.ENDS_WITH)) && 
					(!operator.getStringRepresentation().equals(Constants.In)) &&
					(!operator.getStringRepresentation().equals(Constants.Between)) &&
					(!operator.getStringRepresentation().equals(Constants.Not_In)) &&
					(!operator.getStringRepresentation().equalsIgnoreCase(Constants.IS_NULL)) &&
					(!operator.getStringRepresentation().equalsIgnoreCase(Constants.IS_NOT_NULL)))
			{
				relationalOperatorsList.add(operator.getStringRepresentation());	
			}
		}
		return relationalOperatorsList;
	}
	
	public static List<String> getTimeIntervals()
	{
		List <String>timeIntervalList = new ArrayList<String>(); 
		/**
		 * Getting all days time Intervals
		 */
		for(DSInterval timeInterval : DSInterval.values())
		{
			timeIntervalList.add(timeInterval.name()+"s");
		}
		
		for(YMInterval timeInterval1 : YMInterval.values())
		{
			timeIntervalList.add(timeInterval1.name()+"s");
		}
		return timeIntervalList;
	}
	
	public static TimeInterval getTimeInterval(String timeIntervalValue)
	{
		TimeInterval timeInterval = null;
		for(TimeInterval time: TimeInterval.values())
		{
			if(timeIntervalValue.equals(time.name() + "s"))
			{
			    timeInterval = time;
				break;
			}
		}
		return timeInterval;
	}
	
	public static RelationalOperator getRelationalOperator(String relationalOp)
	{
		RelationalOperator relOp = null;
		for(RelationalOperator operator : RelationalOperator.values())
		{
			if((operator.getStringRepresentation().equals(relationalOp)))
			{
				relOp = operator;
				break;
			}
		}
		return relOp;
	}
	
}
