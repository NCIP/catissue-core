package Components
{
	[Bindable]
	public class Association
	{
		public var associatedNode:String;
		public var associatedLink:String;
		public var operatorIndex:int;
		
		public function Association(associatedNode:String,associatedLink:String,operatorIndex:int)
		{
			this.associatedLink = associatedLink;
			this.associatedNode = associatedNode;
			this.operatorIndex  = operatorIndex;
		}
		
	}
}