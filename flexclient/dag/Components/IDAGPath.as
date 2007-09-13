	package Components
{
	import flash.utils.IExternalizable;
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import mx.controls.Alert;
	
   [Bindable]
   [RemoteClass(alias="edu.wustl.catissuecore.flex.dag.DAGPath")]
	public class IDAGPath implements IExternalizable 
	{
		
		public var name:String;
		public var id:String;
		public var isSelected:Boolean=false;
		public var sourceExpId:int = 0;
		public var destinationExpId:int=0;		
		public var operatorIndex:int=0;
		
		public function readExternal(input:IDataInput):void {
			name = input.readUTF();
			id = input.readUTF();
			isSelected = input.readBoolean();
			sourceExpId=input.readInt();
			destinationExpId= input.readInt();
		}
		
		public function writeExternal(out:IDataOutput):void {
			out.writeUTF(name);
			out.writeUTF(id);
			out.writeBoolean(isSelected);
			out.writeInt(sourceExpId);
			out.writeInt(destinationExpId);
		}
	}
}