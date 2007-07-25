package valueobjects
{
	import flash.utils.IDataOutput;
	import flash.utils.IDataInput;
	import flash.utils.IExternalizable;
	import mx.controls.Alert;
	
	/*[Bindable]
	[RemoteClass(alias="edu.wustl.common.beans.SpecimenBean")]
	*/
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
			/*Alert.show("CLIENT IN writeExternal");
			output.writeUTF(scgName);
			output.writeUTF(parentSpecimenName);
			output.writeUTF(specimenLabel);
			output.writeUTF(specimenBarcode);
			output.writeUTF(specimenClass);
			output.writeUTF(specimenType);
			output.writeUTF(tissueSite);
			output.writeUTF(tissueSide);
			output.writeUTF(pathologicalStatus);
			//output.writeDate(scgName);
			output.writeDouble(quantity);
			output.writeDouble(concentration);
			*/
    	}
        
    	public function readExternal(input:IDataInput):void
    	{
    		/*
    		Alert.show("CLIENT IN readExternal");
    		
			scgName = input.readUTF();
			parentSpecimenName = input.readUTF();
			specimenLabel = input.readUTF();
			specimenBarcode = input.readUTF();
			specimenClass = input.readUTF();
			specimenType = input.readUTF();
			tissueSite = input.readUTF();
			tissueSide = input.readUTF();
			pathologicalStatus = input.readUTF();
			//output.writeDate(scgName);
			quantity = input.readDouble();
			concentration = input.readDouble();
			*/
       }
	}
}