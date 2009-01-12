
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
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	
	public void writeExternal(ObjectOutput out) throws IOException 
	{
        // Write out the client properties from the server representation
		if(id!=null)
		{
			out.writeInt(id.intValue());
		}
		else
		{
			out.writeInt(-1);
		}
		if(name==null)
		{
			name="";
		}
		out.writeUTF(name);
		if(value==null)
		{
			value="";
		}
        out.writeUTF(value);
    }
    
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{	
		id = new Long(in.readInt());
		name = in.readUTF();
		value = in.readUTF();
	}
	
	public String toString()
	{
		return "EI{"+
				"id "+id+"\t"+
				"Name "+name+"\t"+
				"Value "+value+
				"}";
	}
	
}
