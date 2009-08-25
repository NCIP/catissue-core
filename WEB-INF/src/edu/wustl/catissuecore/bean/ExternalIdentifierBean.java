
package edu.wustl.catissuecore.bean;

import java.util.Map;

import edu.wustl.common.util.Utility;

public class ExternalIdentifierBean
{

	int xtrnId;
	Map map;

	String exName = "";
	String exValue = "";
	String exIdentifier = "";
	String check = "";
	String exCondition = "";

	private void processData()
	{
		this.exName = "externalIdentifierValue(ExternalIdentifier:" + this.xtrnId + "_name)";
		this.exValue = "externalIdentifierValue(ExternalIdentifier:" + this.xtrnId + "_value)";
		this.exIdentifier = "externalIdentifierValue(ExternalIdentifier:" + this.xtrnId + "_id)";
		this.check = "chk_ex_" + this.xtrnId;
		final String exKey = "ExternalIdentifier:" + this.xtrnId + "_id";
		final boolean exBool = Utility.isPersistedValue(this.map, exKey);
		if (exBool)
		{
			this.exCondition = "disabled='disabled'";
		}
	}

	public ExternalIdentifierBean()
	{
		super();
	}

	public ExternalIdentifierBean(int xid, Map map)
	{
		super();
		this.xtrnId = xid;
		this.map = map;
		this.processData();
	}

	public String getCheck()
	{
		return this.check;
	}

	public String getExCondition()
	{
		return this.exCondition;
	}

	public String getExIdentifier()
	{
		return this.exIdentifier;
	}

	public String getExName()
	{
		return this.exName;
	}

	public String getExValue()
	{
		return this.exValue;
	}

	public int getXtrnId()
	{
		return this.xtrnId;
	}

}
