package edu.wustl.catissuecore.flex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Calendar;
import java.util.Date;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.util.global.AppUtility;


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
		this.userName = "-- Select --";
		this.eventHour = "";
		this.eventMinute = "";
		this.collectionProcedure = "Not Specified";
		this.container = "Not Specified";
		this.receivedQuality = "Not Specified";
		this.comment = "";
		Calendar calender = Calendar.getInstance();
		//calender.setTime(new Date().getT);
		this.eventHour = AppUtility.toString(Integer.toString(calender.get(Calendar.HOUR_OF_DAY)));
		this.eventMinute = AppUtility.toString(Integer.toString(calender.get(Calendar.MINUTE)));
	}
	public void copy(SpecimenEventParameters event)
	{
		if(event instanceof CollectionEventParameters)
		{
			CollectionEventParameters collEvent = (CollectionEventParameters) event;
			this.collectionProcedure = collEvent.getCollectionProcedure();
			this.container = collEvent.getContainer();
		}
		else if(event instanceof ReceivedEventParameters)
		{
			ReceivedEventParameters recEvent=  (ReceivedEventParameters) event;
			this.receivedQuality = recEvent.getReceivedQuality();
		} 
		//this.userName = event.getUser().getLoginName();
		this.userName = event.getUser().getLastName() + ", " + event.getUser().getFirstName();
		if(event.getComment() != null)
			this.comment = event.getComment();
		Calendar calender = Calendar.getInstance();
		calender.setTime(event.getTimestamp());
		this.eventdDate = event.getTimestamp();
		this.eventHour = AppUtility.toString(Integer.toString(calender.get(Calendar.HOUR_OF_DAY)));
		this.eventMinute = AppUtility.toString(Integer.toString(calender.get(Calendar.MINUTE)));
		
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
