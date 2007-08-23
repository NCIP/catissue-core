	package Components
{
	import flash.utils.IExternalizable;
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	
   [Bindable]
   [RemoteClass(alias="edu.wustl.catissuecore.flex.dag.DAGPath")]
	public class IDAGPath implements IExternalizable 
	{
		
		public var name:String;
		public var id:Number;
		public var isSelected:Boolean=false;
		
		public function readExternal(input:IDataInput):void {
			name = input.readUTF();
			id = input.readDouble();
			isSelected = input.readBoolean();
		}
		
		public function writeExternal(out:IDataOutput):void {
			out.writeUTF(name);
			out.writeDouble(id);
			out.writeBoolean(isSelected);
		}
	}
}