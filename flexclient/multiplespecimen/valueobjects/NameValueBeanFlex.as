package valueobjects 
{
	import flash.utils.IDataOutput;
	import flash.utils.IDataInput;
	import flash.utils.IExternalizable;
	import mx.controls.Alert;
	    
    [Bindable]
	[RemoteClass(alias="edu.wustl.common.beans.NameValueBean")]
	
	public class NameValueBeanFlex// implements IExternalizable
	{		
		public var name:String = '';
		public var val:String = '';
		
		public function NameValueBeanFlex(name:String='',val:String = '') 
		{
			Alert.show("Hey Here");
			this.name = name;
			this.val = val;
		}
		
		public function writeExternal(output:IDataOutput) :void 
		{
			Alert.show("CLIENT IN writeExternal");
			output.writeUTF(name);
			output.writeUTF(val);
    	}
        
    	public function readExternal(input:IDataInput):void
    	{
    		Alert.show("CLIENT IN readExternal");
    		
			name = input.readUTF();
			val = input.readUTF();
       }
	}
}