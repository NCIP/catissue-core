
package edu.wustl.catissuecore.domain;

public class AuditEvent extends EventParameters implements java.io.Serializable
{

	private static final long serialVersionUID = 1234567890L;

	protected String ipAddress;

	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	private java.util.Collection auditEventLogCollection = new java.util.HashSet();

	public java.util.Collection getAuditEventLogCollection()
	{

		return auditEventLogCollection;
	}

	public void setAuditEventLogCollection(java.util.Collection auditEventLogCollection)
	{
		this.auditEventLogCollection = auditEventLogCollection;
	}

}