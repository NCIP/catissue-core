
package edu.wustl.catissuecore.flex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ExternalIdentifierBean implements Externalizable
{

	private static final long serialVersionUID = 1234567890L;

	protected Long id;

	/**
	 * Name of the legacy id.
	 */
	protected String name;

	/**
	 * Value of the legacy id.
	 */
	protected String value;

	public ExternalIdentifierBean()
	{

	}

	public ExternalIdentifierBean(ExternalIdentifierBean externalIdentifier)
	{
		this.name = externalIdentifier.getName();
		this.value = externalIdentifier.getValue();
	}

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return this.value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		// Write out the client properties from the server representation
		if (this.id != null)
		{
			out.writeInt(this.id.intValue());
		}
		else
		{
			out.writeInt(-1);
		}
		if (this.name == null)
		{
			this.name = "";
		}
		out.writeUTF(this.name);
		if (this.value == null)
		{
			this.value = "";
		}
		out.writeUTF(this.value);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.id = new Long(in.readInt());
		this.name = in.readUTF();
		this.value = in.readUTF();
	}

	@Override
	public String toString()
	{
		return "EI{" + "id " + this.id + "\t" + "Name " + this.name + "\t" + "Value " + this.value
				+ "}";
	}

}
