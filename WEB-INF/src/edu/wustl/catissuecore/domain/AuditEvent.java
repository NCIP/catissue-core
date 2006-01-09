
package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;
/**
 * @hibernate.class table="CATISSUE_AUDIT_EVENT"
 **/
public class AuditEvent extends EventParameters implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	protected String ipAddress;
	
	protected Collection auditEventLogCollection = new HashSet();

	/**
     * Returns System generated unique systemIdentifier.
     * @return System generated unique systemIdentifier.
     * @see #setSystemIdentifier(Integer)
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_AUDIT_EVENT_PARAM_SEQ" 
     */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}
	/**
	 * @hibernate.property name="ipAddress" type="string"
     * column="IP_ADDRESS" length="20" 
	 **/
	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	/**
	 * @hibernate.set name="auditEventLogCollection" table="CATISSUE_AUDIT_EVENT_LOG"
	 * @hibernate.collection-key column="AUDIT_EVENT_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.AuditEventLog"
	 */
	public Collection getAuditEventLogCollection()
	{
		return auditEventLogCollection;
	}

	public void setAuditEventLogCollection(Collection auditEventLogCollection)
	{
		this.auditEventLogCollection = auditEventLogCollection;
	}
}