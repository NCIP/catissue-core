
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
	private transient Logger logger = Logger.getCommonLogger(BiohazardBean.class);
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
        return id;
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
        return name;
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
        return type;
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
            BiohazardForm form 	= (BiohazardForm) abstractForm;
            this.comment = form.getComments();
            this.name = form.getName().trim() ;
            this.type = form.getType();
        }
        catch (Exception excp)
        {
        	logger.debug(excp.getMessage(), excp);
        	logger.error(excp.getMessage());
        }
    }

	
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		System.out.println("SERVER IN writeExternal Biohazard START");
        // Write out the client properties from the server representation
		if(id!=null)
		{
			out.writeInt(id.intValue());
		}
		else
		{
			out.writeInt(-1);
		}
			
		out.writeUTF(type);
        out.writeUTF(name);
        System.out.println("SERVER IN writeExternal BioHazard DONE");
    }
    
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		System.out.println("SERVER IN readExternal BioHazard");
		
		id = new Long(in.readInt());
		type = in.readUTF();
		name = in.readUTF();
		
		System.out.println(toString());
	}
	
	public String toString()
	{
		return "BioHazard{"+
				"id "+id+"\t"+
				"Type "+type+"\t"+
				"Name "+name+
				"}";
	}
}