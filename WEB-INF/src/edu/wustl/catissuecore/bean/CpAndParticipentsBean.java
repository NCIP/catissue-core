
package edu.wustl.catissuecore.bean;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author janhavi_hasabnis
 */
public class CpAndParticipentsBean implements Externalizable
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * name.
	 */
	private String name;
	/**
	 * value.
	 */
	private String value;

	/**
	 * Default Constructor.
	 */
	public CpAndParticipentsBean()
	{

	}

	/**
	 * @param nameParam - nameParam
	 * @param valueParam - valueParam
	 */
	public CpAndParticipentsBean(String nameParam, String valueParam)
	{
		this.name = nameParam;
		this.value = valueParam;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @param nameParam The name to set.
	 */
	public void setName(String nameParam)
	{
		//System.out.println("The name written is:"+name);
		this.name = nameParam;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue()
	{
		return this.value;
	}

	/**
	 * @param valueParam The value to set.
	 */
	public void setValue(String valueParam)
	{
		this.value = valueParam;
	}

	/**
	 * @param in - ObjectInput
	 * @throws IOException - IOException
	 * @throws ClassNotFoundException - ClassNotFoundException
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.name = in.readUTF();
	}

	/**
	 * @param out - ObjectOutput
	 * @throws IOException - IOException
	 */
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeUTF(this.name);
		out.writeUTF(this.value);

	}

	/**
	 * @return String
	 */
	@Override
	public String toString()
	{
		return new String("name:" + this.name.toString() + " value:" + this.value.toString());
	}
}
