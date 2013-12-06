
package krishagni.catissueplus.throttling;

public class Event
{

	private Long userId;
	private String ipAddress;
	private String resource;
	private String operation;

	public Event(Long userId, String ipAddress, String resource, String operation)
	{
		super();
		this.userId = userId;
		this.ipAddress = ipAddress;
		this.resource = resource;
		this.operation = operation;
	}

	public Event(String ipAddress, ThrottlingResourceEnum throttlingResourceEnum)
	{
		super();
		this.ipAddress = ipAddress;
		this.resource = throttlingResourceEnum.getResource();
	}

	public Long getUserId()
	{
		return userId;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public String getResource()
	{
		return resource;
	}

	public String getOperation()
	{
		return operation;
	}

}
