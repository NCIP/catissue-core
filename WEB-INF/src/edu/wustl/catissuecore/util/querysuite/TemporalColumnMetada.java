
package edu.wustl.catissuecore.util.querysuite;

import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

public class TemporalColumnMetada
{
	private int columnIndex;
	private TermType termType;
	private IDateLiteral PHIDate;
	private boolean birthDate;
	private TimeInterval<?> timeInterval;

	/**
	 * Get the column index.
	 * @return columnIndex columnIndex
	 */
	public int getColumnIndex()
	{
		return columnIndex;
	}

	/**
	 * Set column index.
	 * @param columnIndex columnIndex
	 */
	public void setColumnIndex(int columnIndex)
	{
		this.columnIndex = columnIndex;
	}

	/**
	 * Get the term type.
	 * @return termType termType
	 */
	public TermType getTermType()
	{
		return termType;
	}

	/**
	 * Set the term type.
	 * @param termType termType
	 */
	public void setTermType(TermType termType)
	{
		this.termType = termType;
	}

	/**
	 * Get the PHI date.
	 * @return PHIDate PHIDate
	 */
	public IDateLiteral getPHIDate()
	{
		return PHIDate;
	}

	/**
	 * Set the PHI date..
	 * @param date PHIDate PHIDate
	 */
	public void setPHIDate(IDateLiteral date)
	{
		PHIDate = date;
	}

	/**
	 * Get the birthDate value.
	 * @return isBirthDate isBirthDate
	 */
	public boolean isBirthDate()
	{
		return birthDate;
	}

	/**
	 * Set the birthDate value.
	 * @param isBirthDate isBirthDate
	 */
	public void setBirthDate(boolean isBirthDate)
	{
		this.birthDate = isBirthDate;
	}

	/**
	 * Get the time interval.
	 * @return timeInterval
	 */
	public TimeInterval<?> getTimeInterval()
	{
		return timeInterval;
	}

	/**
	 * Set the time interval.
	 * @param timeInterval timeInterval
	 */
	public void setTimeInterval(TimeInterval<?> timeInterval)
	{
		this.timeInterval = timeInterval;
	}
}
