package util
{
	import mx.collections.ArrayCollection;
	
	/**
	 * Class to holds all required metadata to build the page. The class contains only those data which are static in nature. For example
	 * Tissue Site, Side, Pathological status. The Data is being downloaded only once and same data is used for each specimen UI (BLOCK).
	 * 
	 * This class is designed to be singleton. 
	 */
	[Bindable]	
	public class MetadataModel
	{
		public static var metaModel:MetadataModel = new MetadataModel();
		public function MetadataModel()
		{			
		}
		
		public static function getInstance():MetadataModel
		{
			return metaModel;
		}

		public var scgNameArray:Array = ['scg1','scg2','scg3', 'scg4','scg5','scg6','scg7', 'scg8','scg9','scg10'];		
		public var spNameArray:Array = ['sp1','sp2','sp3'];
		
		public var tissueSidePVList:ArrayCollection = new ArrayCollection();
		public var tissueSitePVList:ArrayCollection = new ArrayCollection();
		public var pathologicalStatusPVList:ArrayCollection = new ArrayCollection();
		public var specimenClassPVList:ArrayCollection = new ArrayCollection();
		public var specimenTypePVArrayColl:ArrayCollection = new ArrayCollection();
		
		
		public var userList:ArrayCollection = new ArrayCollection();
		public var procedureList:ArrayCollection = new ArrayCollection();
		public var containerList:ArrayCollection = new ArrayCollection();
		
		public var storageList:Array=['Virtual','Auto','Manual'];
		
		//For received Event
		public var receivedQualityList:ArrayCollection = new ArrayCollection();
		
		public var hourListArray:Array = Constants.HOUR_ARRAY;
		public var miniuteListArray:Array = Constants.MINIUTE_ARRAY;
		
		
		//biohazard
		public var biohazardTypeList:ArrayCollection = new ArrayCollection();
		public var biohazardNameList:ArrayCollection = new ArrayCollection();
		
		/**
		 * Returns specimen type based on class.
		 */
		public function getSpecimenTypeCollection(spClass:String): ArrayCollection
		{
			var spTypeColl:ArrayCollection = new ArrayCollection();
			
			if( specimenTypePVArrayColl == null || 
				specimenTypePVArrayColl.length==0 )
			{
				return spTypeColl;
			}
			
			if(spClass == Constants.FLUID)
			{
				spTypeColl = ArrayCollection(specimenTypePVArrayColl.getItemAt(0));
			}
			else if(spClass == Constants.TISSUE)
			{
				spTypeColl = ArrayCollection(specimenTypePVArrayColl.getItemAt(1));
			}
			else if(spClass == Constants.MOLECULAR)
			{
				spTypeColl = ArrayCollection(specimenTypePVArrayColl.getItemAt(2));
			}
			else if(spClass == Constants.CELL)
			{
				spTypeColl = ArrayCollection(specimenTypePVArrayColl.getItemAt(3));
			}
			return spTypeColl;
		}
		
		public function getBiohazardNameList(index:int):ArrayCollection
		{
			if(index>0)
			{
			return biohazardNameList.getItemAt(index) as ArrayCollection;
			}
			else
			{
				var bohazardDefNameList:ArrayCollection = new ArrayCollection();
				bohazardDefNameList.addItem("-- Select --");
				return bohazardDefNameList;
			}
		}
	}
}