
package edu.wustl.catissuecore.bean;

import java.util.Map;

import edu.wustl.common.util.Utility;
/**
 *
 * @author virender_mehta
 * @created-on Nov 9, 2009
 */
public class ExternalIdentifierBean
{
	/**
	 * ExternalIdentifier Id.
	 */
	private int xtrnId;
	/**
	 * ExternalIdentifier map.
	 */
	private Map<Object,Object> map;
	/**
	 * ExternalIdentifier name.
	 */
	private String exName = "";
	/**
	 * ExternalIdentifier value.
	 */
	private String exValue = "";
	/**
	 * ExternalIdentifier Id.
	 */
	private String exIdentifier = "";
	/**
	 * check condition.
	 */
	private String check = "";
	/**
	 * ExternalIdentifier condition.
	 */
	private String exCondition = "";
	/**
	 *  generate ExternalIdentifier keys.
	 */
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
	/**
	 * Default constructor.
	 */
	public ExternalIdentifierBean()
	{
		super();
	}

	/**
	 *
	 * @param xid ExternalIdentifier.
	 * @param map ExternalIdentifier map.
	 */
	public ExternalIdentifierBean(int xid, Map<Object,Object> map)
	{
		super();
		this.xtrnId = xid;
		this.map = map;
		this.processData();
	}

	/**
	 *
	 * @return check.
	 */
	public String getCheck()
	{
		return this.check;
	}
	/**
	 *
	 * @return exCondition.
	 */
	public String getExCondition()
	{
		return this.exCondition;
	}
	/**
	 *
	 * @return exIdentifier.
	 */
	public String getExIdentifier()
	{
		return this.exIdentifier;
	}
	/**
	 *
	 * @return exName.
	 */
	public String getExName()
	{
		return this.exName;
	}
	/**
	 *
	 * @return exValue
	 */
	public String getExValue()
	{
		return this.exValue;
	}
	/**
	 *
	 * @return xtrnId
	 */
	public int getXtrnId()
	{
		return this.xtrnId;
	}

}
