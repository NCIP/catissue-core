package valueobjects
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	
	import mx.controls.Alert;
	
	[Bindable]
	[RemoteClass(alias="edu.wustl.catissuecore.flex.ExternalIdentifierBean")]	
	public class ExternalIdentifierBean implements IExternalizable
	{
		public var isSelected:Boolean;
		
		public var id:Number = -1;
		public var identifierName:String = '';
		public var identifierNameErrStr:String = '';
		public var identifierValue:String = null;
		public var identifierValueErrStr:String = null;
		public function ExternalIdentifierBean(isSelected:Boolean=false, id:Number=-1, identifierName:String="",identifierValue:String="")
		{
			init(isSelected, id, identifierName,identifierValue);
		}
		
		private function init(isSelected:Boolean=false, id:Number=-1, identifierName:String="",identifierValue:String=""):void
		{
			this.isSelected = isSelected;
			this.id = id;
			this.identifierName = identifierName;
			this.identifierValue = identifierValue			
		}
		
		public function copy(exIdBean:ExternalIdentifierBean):void
		{
			init(exIdBean.isSelected,exIdBean.id,exIdBean.identifierName,exIdBean.identifierValue);
		}
		
		public function writeExternal(output:IDataOutput) :void 
		{
			//Alert.show("CLIENT IN writeExternal of ExternalIdentifierBean");
			
			output.writeInt(id);
			output.writeUTF(identifierName);
			output.writeUTF(identifierValue);
    	}
        
    	public function readExternal(input:IDataInput):void
    	{
    		//Alert.show("CLIENT IN readExternal of ExternalIdentifierBean");
    		
			id = input.readInt();
			identifierName = input.readUTF();
			identifierValue = input.readUTF();
       }
       
       public function validate():Boolean
       {
       		var isValid:Boolean = true;
	       	if(this.identifierName == "" && this.identifierValue != "")
			{
				
				this.identifierNameErrStr = "Please enter name";
				isValid = false && isValid;
			} 
			else
			{
				this.identifierNameErrStr = null;
				isValid = true && isValid;
			}
			
			if(this.identifierValue == "" && this.identifierName != "")
			{
				this.identifierValueErrStr = "Please enter value";
				isValid = false && isValid;
			} 
			else
			{
				this.identifierValueErrStr = null;
				isValid = true && isValid;
			}
			return isValid;
       	 
       }
	}
}