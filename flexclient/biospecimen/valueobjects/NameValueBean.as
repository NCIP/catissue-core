package valueobjects 
{
	import flash.utils.IDataOutput;
	import flash.utils.IDataInput;
	import flash.utils.IExternalizable;
	import mx.controls.Alert;
    
    [Bindable]
	[RemoteClass(alias="edu.wustl.catissuecore.bean.CpAndParticipentsBean")]
	
	public class NameValueBean implements IExternalizable
	{		
		public var name:String = '';
		public var value:String = '';
		
		
		public function NameValueBean() 
		{
			//Alert.show("Hey Here in name value bean class");
			//this.name = name;
			//this.val = val;
		}
		
		public function writeExternal(output:IDataOutput):void 
		{
			//Alert.show("CLIENT IN writeExternal");
			output.writeUTF(name);
			output.writeUTF(value);
    	}
        
    	public function readExternal(input:IDataInput):void
    	{
    		//Alert.show("CLIENT IN readExternal");
    		name = input.readUTF();
			//Alert.show("Name is:"+name);
			value = input.readUTF();
			//Alert.show("Value is:"+value);
       }
	}
}
