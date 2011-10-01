package valueobjects
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	
	import mx.controls.Alert;
	import mx.validators.StringValidator;
	import mx.events.CalendarLayoutChangeEvent;
	import util.Constants;
	
	[Bindable]
	[RemoteClass(alias="edu.wustl.catissuecore.flex.EventParamtersBean")]
	public class EventBean implements IExternalizable
	{
		public var id:Number=0;
		public var userName:String="Hello";
		public var userNameErrStr:String=null;
		public var eventDate:Date = new Date();
		public var eventDateErrStr:String = null;
		public var eventHour:String;
		public var eventMinute:String;

		
		public var collectionProcedure:String ;
		public var collectionProcedureErrStr:String = null;
		public var container:String ="-- Select --";
		public var containerErrStr:String = null;
		public var receivedQuality:String = "-- Select --";
		public var receivedQualityErrStr:String = null;
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
			this.comment = "";
			
		}

		
		public function writeExternal(output:IDataOutput) :void 
		{
			//Alert.show("CLIENT IN writeExternal of eventBean");
			
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
    		//Alert.show("CLIENT IN readExternal of EventBean");
    		
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
       
       public function validate():Boolean
       {
       		var isValid:Boolean = true;
       		if(this.userName == Constants.SELECT)
			{
				this.userNameErrStr = "Please select user";
				isValid = false && isValid;
			} 
			else
			{
				this.userNameErrStr = null;
				isValid = true && isValid;
			}
			if(this.collectionProcedure == Constants.SELECT)
			{
				this.collectionProcedureErrStr = "Please select collection procedure";
				isValid = false && isValid;
			} 
			else
			{
				this.collectionProcedureErrStr = null;
				isValid = true && isValid;
			}
			
			if(this.container == Constants.SELECT)
			{
				this.containerErrStr = "Please select container";
				isValid = false && isValid;
			} 
			else
			{
				this.containerErrStr = null;
				isValid = true && isValid;
			}
			if(this.receivedQuality == Constants.SELECT)
			{
				this.receivedQualityErrStr = "Please select quality";
				isValid = false && isValid;
			} 
			else
			{
				this.receivedQualityErrStr = null;
				isValid = true && isValid;
			}
			return isValid;
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