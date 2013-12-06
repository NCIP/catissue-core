
package krishagni.catissueplus.throttling;

import java.util.HashMap;
import java.util.Map;

public enum ThrottlingResourceEnum {
	LOGIN_MODULE("/Login", "Login"), SPECIMEN_MODULE("/Specimen", "Specimen");

	private String code;
	private String resource;

	/**
	 * A mapping between the code and its corresponding resource to facilitate lookup by code.
	 */
	private static Map<String, ThrottlingResourceEnum> codeToResourceMapping;

	private ThrottlingResourceEnum(String code, String description)
	{
		this.code = code;
		this.resource = description;
	}

	public static ThrottlingResourceEnum getStatus(String i)
	{
		if (codeToResourceMapping == null)
		{
			initMapping();
		}
		return codeToResourceMapping.get(i);
	}

	private static void initMapping()
	{
		codeToResourceMapping = new HashMap<String, ThrottlingResourceEnum>();
		for (ThrottlingResourceEnum s : values())
		{
			codeToResourceMapping.put(s.code, s);
		}
	}

	public String getCode()
	{
		return code;
	}

	public String getResource()
	{
		return resource;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("Status");
		sb.append("{code=").append(code);
		sb.append(", description='").append(resource).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
