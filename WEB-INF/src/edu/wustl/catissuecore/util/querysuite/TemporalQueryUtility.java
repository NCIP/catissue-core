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
	/**
	 * Empty constructor.
	 */
	private TemporalQueryUtility() {}
	/**
	 * Returns the list of Relational Operators.
	 * @return relationalOperatorsList list of Relational Operators
	 */
	public static List<String> getRelationalOperators()
	{
		/**
		 * Getting relational operators excluding those deals with Strings
		 */
		List<String> relationalOperatorList = new ArrayList<String>();
		for (RelationalOperator operator : RelationalOperator.values())
		{
			if ((!operator.getStringRepresentation().equals(Constants.Contains))
				&& (!operator.getStringRepresentation().equals(Constants.STRATS_WITH))
				&& (!operator.getStringRepresentation().equals(Constants.ENDS_WITH))
				&& (!operator.getStringRepresentation().equals(Constants.In))
				&& (!operator.getStringRepresentation().equals(Constants.Between))
				&& (!operator.getStringRepresentation().equals(Constants.Not_In))
				&& (!operator.getStringRepresentation().equalsIgnoreCase(Constants.IS_NULL))
				&& (!operator.getStringRepresentation().equalsIgnoreCase(Constants.IS_NOT_NULL))
				&& (!operator.getStringRepresentation().equalsIgnoreCase(Constants.NOT_BETWEEN)))

			{
				relationalOperatorList.add(operator.getStringRepresentation());
			}
		}
		return relationalOperatorList;
	}

	/**
	 * @return timeIntervalList time interval list
	 */
	public static List<String> getTimeIntervals()
	{
		List<String> timeIntervalList = new ArrayList<String>();
		/**
		 * Getting all days time Intervals
		 */
		for (DSInterval timeInterval : DSInterval.values())
		{
			timeIntervalList.add(timeInterval.name() + "s");
		}

		for (YMInterval timeInterval1 : YMInterval.values())
		{
			timeIntervalList.add(timeInterval1.name() + "s");
		}
		return timeIntervalList;
	}

	/**
	 * @param timeIntervalValue The timeIntervalValue
	 * @return timeInterval The timeInterval
	 */
	public static TimeInterval getTimeInterval(String timeIntervalValue)
	{
		TimeInterval timeInterval = null;
		for (TimeInterval time : TimeInterval.values())
		{
			if (timeIntervalValue.equals(time.name() + "s"))
			{
				timeInterval = time;
				break;
			}
		}
		return timeInterval;
	}

	/**
	 * Get the relational operator.
	 * @param relationalOp The relational operator
	 * @return relOp The relational operator
	 */
	public static RelationalOperator getRelationalOperator(String relationalOp)
	{
		RelationalOperator relOp = null;
		for (RelationalOperator operator : RelationalOperator.values())
		{
			if ((operator.getStringRepresentation().equals(relationalOp)))
			{
				relOp = operator;
				break;
			}
		}
		return relOp;
	}
}
