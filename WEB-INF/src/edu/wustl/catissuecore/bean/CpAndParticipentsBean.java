package edu.wustl.catissuecore.bean;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class CpAndParticipentsBean implements Externalizable
{
	private static final long serialVersionUID = 1L;
	
	
    private String name;
	
	private String value;
	
	/*
	 * Default Constructor
	 */
	public CpAndParticipentsBean()
	{
		
	}
	
	public CpAndParticipentsBean(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	
	
	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
        System.out.println("The name written is:"+name); 
		this.name = name;
	}

	
	/**
	 * @return Returns the value.
	 */
	public String getValue()
	{
		return value;
	}

	
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
	
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException 
	{
		name=in.readUTF();
	}
 	
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeUTF(name);
		out.writeUTF(value);
		
	}
	
	public String toString()
	{
		return new String("name:" + name.toString() + " value:" + value.toString());
	}
}
