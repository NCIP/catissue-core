package valueobjects
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	import mx.controls.Alert;
	import util.Constants;
	[Bindable]
	[RemoteClass(alias="edu.wustl.catissuecore.flex.BiohazardBean")]	

	public class BiohazardBean implements IExternalizable
	{
		public var isSelected:Boolean;
		
		public var id:Number;
		public var type:String = '-- Select --';
		public var typeErrStr:String = null;
		public var name:String = '-- Select --';
		public var nameErrStr:String = null;
		public function BiohazardBean(isSelected:Boolean=false, id:Number=0, type:String="-- Select --",name:String="-- Select --")
		{
			init(isSelected, id, type,name);
		}
		
		private function init(isSelected:Boolean=false, id:Number=0, type:String="-- Select --",name:String="-- Select --"):void
		{
			this.isSelected = isSelected;
			this.id = id;
			this.type = type;
			this.name = name;
		}
		
		public function copy(biohazardBean:BiohazardBean):void
		{
			init(biohazardBean.isSelected,biohazardBean.id,biohazardBean.type,biohazardBean.name);
		}
		
		public function writeExternal(output:IDataOutput) :void 
		{
			//Alert.show("CLIENT IN writeExternal of ExternalIdentifierBean");
			
			output.writeInt(id);
			output.writeUTF(type);
			output.writeUTF(name);
    	}
        
    	public function readExternal(input:IDataInput):void
    	{
    		//Alert.show("CLIENT IN readExternal of ExternalIdentifierBean");
    		
			id = input.readInt();
			type = input.readUTF();
			name = input.readUTF();
       }
       
       public function toString():String
       {
			return this.type+"\n"+this.name;
       }
       
       public function validate():Boolean
       {
       		var isValid:Boolean = true;
	       	if(this.type == Constants.SELECT && this.name != Constants.SELECT)
			{
				this.typeErrStr = "Please select type";
				isValid = false && isValid;
			} 
			else
			{
				this.typeErrStr = null;
				isValid = true && isValid;
			}
			
			if(this.name == Constants.SELECT && this.type != Constants.SELECT)
			{
				this.nameErrStr = "Please select value";
				isValid = false && isValid;
			} 
			else
			{
				this.nameErrStr = null;
				isValid = true && isValid;
			}	
			return isValid;
       	 
       }
	}
}