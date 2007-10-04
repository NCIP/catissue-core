package pagination.managers
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	public class ModelManager{
	
	private static var instance:ModelManager;
	
	public var collection:ArrayCollection;
	public var filteredCollection:ArrayCollection;
	public var currPage:int;
	public var pageLimit:int;
	
	public function ModelManager(access:Private)
	{		if(access == null){
			throw new Error('Illegal Instantiation');
		}
		
		if(instance == null)
		{
			instance = this;
		}
		
	}
		
		public static function getInstance():ModelManager
		{
			if(instance == null)
			{
				instance = new ModelManager(new Private);
			}
			
			return instance
		}
		
	}
}

class Private{

}