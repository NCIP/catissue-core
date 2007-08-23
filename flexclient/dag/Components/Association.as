package Components
{
	[Bindable]
	public class Association
	{
		public var associatedNode:String;
		public var associatedLink:String;
		
		public function Association(associatedNode:String,associatedLink:String)
		{
			this.associatedLink=associatedLink;
			this.associatedNode=associatedNode;
		}
		
	}
}