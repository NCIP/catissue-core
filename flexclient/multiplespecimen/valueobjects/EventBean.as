package valueobjects
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	
	import mx.controls.Alert;
	import mx.validators.StringValidator;
	import mx.events.CalendarLayoutChangeEvent;
	
	[Bindable]
	[RemoteClass(alias="edu.wustl.catissuecore.flex.EventParamtersBean")]
	public class EventBean implements IExternalizable
	{
		public var id:Number=0;
		public var userName:String="Hello";
		public var eventDate:Date = new Date();

		public var eventHour:String;
		public var eventMinute:String;
		
		public var collectionProcedure:String ;
		public var container:String ="-- Select --";
		
		public var receivedQuality:String = "-- Select --";
		public var comment:String;

		
		public function EventBean()
		{

			this.id = 0;
			this.userName = "-- Select --";
			this.eventDate = new Date();
			this.eventHour = eventDate.getHours().toString();
			this.eventMinute = eventDate.getMinutes().toString();
			this.collectionProcedure = "Not Specified";
			this.container = "Not Specified";
			this.receivedQuality = "Not Specified";
			this.comment = "Hey My Comment";
			
		}

		
		public function writeExternal(output:IDataOutput) :void 
		{
			Alert.show("CLIENT IN writeExternal of eventBean");
			
			output.writeInt(id);
			output.writeUTF(userName);
			output.writeObject(eventDate);
			output.writeUTF(eventHour);
			output.writeUTF(eventMinute);
			output.writeUTF(collectionProcedure);
			output.writeUTF(container);
			output.writeUTF(receivedQuality);
			output.writeUTF(comment);
    	}
        
    	public function readExternal(input:IDataInput):void
    	{
    		Alert.show("CLIENT IN readExternal of EventBean");
    		
			id = input.readInt();
			userName = input.readUTF();
			eventDate = input.readObject() as Date;
			eventHour = input.readUTF();
			eventMinute = input.readUTF();
			collectionProcedure=input.readUTF();
			container=input.readUTF();
			receivedQuality = input.readUTF();
			comment=input.readUTF();
       }
       
       public function toString():String
       {
       		return 	"id "+id+"\n"+
       				"userName "+userName+"\n"+
       				"eventdDate "+eventDate+"\n"+
       				"eventTime "+eventHour+":"+eventMinute+"\n"+
       				"eventProcedure "+collectionProcedure+"\n"+
       				"container "+container+"\n"+
       				"comment "+comment;
       }
	}
}