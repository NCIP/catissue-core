
package edu.wustl.catissuecore.flex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Calendar;
import java.util.Date;

import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.common.util.global.CommonUtilities;

public class EventParamtersBean implements Externalizable
{

	private static final long serialVersionUID = 1L;

	public Long eventId = -5L;
	public String userName = "";
	public Date eventdDate = new Date();
	public String eventHour = "";
	public String eventMinute = "";
	public String collectionProcedure = "";
	public String container = "";
	public String receivedQuality = "";
	public String comment = "";

	public EventParamtersBean()
	{
		this.userName = "-- Select --";
		this.eventHour = "";
		this.eventMinute = "";
		this.collectionProcedure = "Not Specified";
		this.container = "Not Specified";
		this.receivedQuality = "Not Specified";
		this.comment = "";
		final Calendar calender = Calendar.getInstance();
		//calender.setTime(new Date().getT);
		this.eventHour = CommonUtilities.toString(Integer.toString(calender
				.get(Calendar.HOUR_OF_DAY)));
		this.eventMinute = CommonUtilities
				.toString(Integer.toString(calender.get(Calendar.MINUTE)));
	}

	public void copy(SpecimenEventParameters event)
	{
//		if (event instanceof CollectionEventParameters)
//		{
//			final CollectionEventParameters collEvent = (CollectionEventParameters) event;
//			this.collectionProcedure = collEvent.getCollectionProcedure();
//			this.container = collEvent.getContainer();
//		}
//		else if (event instanceof ReceivedEventParameters)
//		{
//			final ReceivedEventParameters recEvent = (ReceivedEventParameters) event;
//			this.receivedQuality = recEvent.getReceivedQuality();
//		}
		//this.userName = event.getUser().getLoginName();
		this.userName = event.getUser().getLastName() + ", " + event.getUser().getFirstName();
		if (event.getComment() != null)
		{
			this.comment = event.getComment();
		}
		final Calendar calender = Calendar.getInstance();
		calender.setTime(event.getTimestamp());
		this.eventdDate = event.getTimestamp();
		this.eventHour = CommonUtilities.toString(Integer.toString(calender
				.get(Calendar.HOUR_OF_DAY)));
		this.eventMinute = CommonUtilities
				.toString(Integer.toString(calender.get(Calendar.MINUTE)));

	}

	public void writeExternal(ObjectOutput out) throws IOException
	{
		System.out.println("SERVER IN writeExternal START");
		// Write out the client properties from the server representation
		out.writeInt(this.eventId.intValue());
		out.writeUTF(this.userName);
		out.writeObject(this.eventdDate);
		out.writeUTF(this.eventHour);
		out.writeUTF(this.eventMinute);
		out.writeUTF(this.collectionProcedure);
		out.writeUTF(this.container);
		out.writeUTF(this.receivedQuality);
		out.writeUTF(this.comment);
		System.out.println("SERVER IN writeExternal DONE");
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		System.out.println("SERVER IN readExternal");

		this.eventId = new Long(in.readInt());
		this.userName = in.readUTF();
		this.eventdDate = (Date) in.readObject();
		this.eventHour = in.readUTF();
		this.eventMinute = in.readUTF();
		this.collectionProcedure = in.readUTF();
		this.container = in.readUTF();
		this.receivedQuality = in.readUTF();
		this.comment = in.readUTF();
	}
}
