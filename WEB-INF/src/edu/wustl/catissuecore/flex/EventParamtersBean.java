package edu.wustl.catissuecore.flex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;


public class EventParamtersBean implements Externalizable
{
	private static final long serialVersionUID = 1L;
	
	public Long eventId = -5L;
	public String userName="";
	public Date eventdDate = new Date();
	public String eventHour="";
	public String eventMinute="";
	public String collectionProcedure="";
	public String container="";
	public String receivedQuality="";
	public String comment="";
	
	public EventParamtersBean()
	{
		this.userName = "";
		this.eventHour = "";
		this.eventMinute = "";
		this.collectionProcedure = "";
		this.container = "";
		this.receivedQuality = "";
		this.comment = "";
	}
	public void writeExternal(ObjectOutput out) throws IOException 
	{
		System.out.println("SERVER IN writeExternal START");
        // Write out the client properties from the server representation
		out.writeInt(eventId.intValue());
		out.writeUTF(userName);
		out.writeObject(eventdDate);
		out.writeUTF(eventHour);
		out.writeUTF(eventMinute);
        out.writeUTF(collectionProcedure);
        out.writeUTF(container);
        out.writeUTF(receivedQuality);
        out.writeUTF(comment);
         System.out.println("SERVER IN writeExternal DONE");
    }
    
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		System.out.println("SERVER IN readExternal");
		
		eventId =  new Long(in.readInt());
		userName = in.readUTF();
		eventdDate = (Date)in.readObject();
		eventHour = in.readUTF();
		eventMinute = in.readUTF();
		collectionProcedure = in.readUTF();
		container = in.readUTF();
		receivedQuality = in.readUTF();
		comment = in.readUTF();
	}
}
