package Components
{
	[Bindable]
	public class Association
	{
		public var associatedNode:String;
		public var associatedLink:String;
		public var operatorIndex:int;
		public var order:int=0;
		
		public function Association(associatedNode:String,associatedLink:String,operatorIndex:int,order:int)
		{
			this.associatedLink = associatedLink;
			this.associatedNode = associatedNode;
			this.operatorIndex  = operatorIndex;
			this.order=order;
		}
		
	}
}