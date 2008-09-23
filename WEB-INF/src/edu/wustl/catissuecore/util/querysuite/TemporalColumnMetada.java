package edu.wustl.catissuecore.util.querysuite;

import edu.wustl.common.querysuite.queryobject.IDateLiteral;
import edu.wustl.common.querysuite.queryobject.ITimeIntervalEnum;
import edu.wustl.common.querysuite.queryobject.TermType;
import edu.wustl.common.querysuite.queryobject.TimeInterval;

public class TemporalColumnMetada 
{
	private int columnIndex;
	private TermType termType;
	private IDateLiteral PHIDate;
	private boolean isBirthDate;
	private TimeInterval<?> timeInterval ;

	public int getColumnIndex() 
	{
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) 
	{
		this.columnIndex = columnIndex;
	}

	public TermType getTermType() 
	{
		return termType;
	}

	public void setTermType(TermType termType) 
	{
		this.termType = termType;
	}

	public IDateLiteral getPHIDate() 
	{
		return PHIDate;
	}

	public void setPHIDate(IDateLiteral date) 
	{
		PHIDate = date;
	}

	public boolean isBirthDate() {
		return isBirthDate;
	}

	public void setBirthDate(boolean isBirthDate) {
		this.isBirthDate = isBirthDate;
	}

	public TimeInterval<?> getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(TimeInterval<?> timeInterval) {
		this.timeInterval = timeInterval;
	}
	
}
