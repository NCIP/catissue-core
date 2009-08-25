
package edu.wustl.catissuecore.flex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

public class BiohazardBean implements Externalizable
{

	private transient final Logger logger = Logger.getCommonLogger(BiohazardBean.class);
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * Name of the biohazardous agent.
	 */
	protected String name;

	/**
	 * Comment about the biohazard.
	 */
	protected String comment;

	/**
	 * Type of biohazard (Infectious, Radioactive, Toxic, Carcinogen, Mutagen).
	 */
	protected String type;

	/**
	 *boolean for checking persisted Biohazard persisted value
	 *
	 */

	//Default Constructor
	public BiohazardBean()
	{
	}

	public Long getId()
	{
		return this.id;
	}

	/**
	 * Sets the system generated unique id.
	 * @param id the system generated unique id.
	 * @see #getId()
	 * */
	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the name of the biohazardous agent.
	 * @param name the name of the biohazardous agent.
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return this.type;
	}

	/**
	 * Sets the type of biohazard (Infectious, Radioactive, Toxic, Carcinogen, Mutagen).
	 * @param type the type of biohazard (Infectious, Radioactive, Toxic, Carcinogen, Mutagen).
	 * @see #getType()
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * This function Copies the data from an BiohazardForm object to a Biohazard object.
	 * @param siteForm An SiteForm object containing the information about the site.  
	 * */
	public void setAllValues(IValueObject abstractForm)
	{
		try
		{
			final BiohazardForm form = (BiohazardForm) abstractForm;
			this.comment = form.getComments();
			this.name = form.getName().trim();
			this.type = form.getType();
		}
		catch (final Exception excp)
		{
			this.logger.debug(excp.getMessage(), excp);
			this.logger.error(excp.getMessage());
		}
	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		System.out.println("SERVER IN writeExternal Biohazard START");
		// Write out the client properties from the server representation
		if (this.id != null)
		{
			out.writeInt(this.id.intValue());
		}
		else
		{
			out.writeInt(-1);
		}

		out.writeUTF(this.type);
		out.writeUTF(this.name);
		System.out.println("SERVER IN writeExternal BioHazard DONE");
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		System.out.println("SERVER IN readExternal BioHazard");

		this.id = new Long(in.readInt());
		this.type = in.readUTF();
		this.name = in.readUTF();

		System.out.println(this.toString());
	}

	@Override
	public String toString()
	{
		return "BioHazard{" + "id " + this.id + "\t" + "Type " + this.type + "\t" + "Name "
				+ this.name + "}";
	}
}