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
			exName = "externalIdentifierValue(ExternalIdentifier:" + xtrnId + "_name)";
			exValue = "externalIdentifierValue(ExternalIdentifier:" + xtrnId + "_value)";
			exIdentifier = "externalIdentifierValue(ExternalIdentifier:" + xtrnId +"_id)";
			check = "chk_ex_"+xtrnId;
			String exKey = "ExternalIdentifier:" + xtrnId +"_id";
			boolean exBool = Utility.isPersistedValue(map,exKey);
			if(exBool)
				exCondition = "disabled='disabled'";
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
		processData();
	}

	public String getCheck() {
		return check;
	}

	public String getExCondition() {
		return exCondition;
	}

	public String getExIdentifier() {
		return exIdentifier;
	}

	public String getExName() {
		return exName;
	}

	public String getExValue() {
		return exValue;
	}

	public int getXtrnId() {
		return xtrnId;
	}

}
