
package edu.wustl.catissuecore.domain;

public class AuditEventDetails implements java.io.Serializable
{

	private static final long serialVersionUID = 1234567890L;

	protected Long systemIdentifier;

	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	protected String elementName;

	public String getElementName()
	{
		return elementName;
	}

	public void setElementName(String elementName)
	{
		this.elementName = elementName;
	}

	protected String previousValue;

	public String getPreviousValue()
	{
		return previousValue;
	}

	public void setPreviousValue(String previousValue)
	{
		this.previousValue = previousValue;
	}

	protected String currentValue;

	public String getCurrentValue()
	{
		return currentValue;
	}

	public void setCurrentValue(String currentValue)
	{
		this.currentValue = currentValue;
	}

}