
package edu.wustl.catissuecore.domain;

public class AuditEventLog implements java.io.Serializable
{

	private static final long serialVersionUID = 1234567890L;

	protected Integer systemIdentifier;

	public Integer getSystemIdentifier()
	{
		return systemIdentifier;
	}

	public void setSystemIdentifier(Integer systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	protected Long objectIdentifier;

	public Long getObjectIdentifier()
	{
		return objectIdentifier;
	}

	public void setObjectIdentifier(Long objectIdentifier)
	{
		this.objectIdentifier = objectIdentifier;
	}

	protected String ObjectName;

	public String getObjectName()
	{
		return ObjectName;
	}

	public void setObjectName(String ObjectName)
	{
		this.ObjectName = ObjectName;
	}

	protected String eventType;

	public String getEventType()
	{
		return eventType;
	}

	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}

	private edu.wustl.catissuecore.domain.AuditEvent audit;

	public edu.wustl.catissuecore.domain.AuditEvent getAudit()
	{

		return audit;
	}

	public void setAudit(edu.wustl.catissuecore.domain.AuditEvent audit)
	{
		this.audit = audit;
	}

	private java.util.Collection auditEventDetailsCollcetion = new java.util.HashSet();

	public java.util.Collection getAuditEventDetailsCollcetion()
	{

		return auditEventDetailsCollcetion;
	}

	public void setAuditEventDetailsCollcetion(java.util.Collection auditEventDetailsCollcetion)
	{
		this.auditEventDetailsCollcetion = auditEventDetailsCollcetion;
	}

}