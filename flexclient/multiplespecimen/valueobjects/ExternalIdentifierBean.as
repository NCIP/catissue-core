package valueobjects
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	
	import mx.controls.Alert;
	
	[Bindable]
	[RemoteClass(alias="edu.wustl.catissuecore.domain.ExternalIdentifier")]	
	public class ExternalIdentifierBean implements IExternalizable
	{
		public var isSelected:Boolean;
		
		public var id:Number;
		public var identifierName:String = '';
		public var identifierValue:String = '';

		public function ExternalIdentifierBean(isSelected:Boolean=false, id:Number=0, identifierName:String="",identifierValue:String="")
		{
			init(isSelected, id, identifierName,identifierValue);
		}
		
		private function init(isSelected:Boolean=false, id:Number=0, identifierName:String="",identifierValue:String=""):void
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
	}
}