package pagination.utils
{
	import mx.collections.ArrayCollection;
	import mx.containers.Canvas;
	import pagination.components.PageButton;
	
	 /**
	 * 
     * The Pagination class is used to organizing information in a view. 
     * For instance, data in a datagrid may be paginated such that 20 records appear only at one time.
     *
     */
	
	public class PaginateData
	{
		private var masterCollection:ArrayCollection;
		private var btnContainer:*;
		private var limit:int;
		private var pageButton:PageButton;
		private var lowLimit:int = 0;
		private var highLimit:int;
		private var pageButtonCollection:ArrayCollection;
		
		 /**
	     * Sets intial references for Pagination
	     *
	     * @param   collection: An Array Collection containing data for Pagination
	     * @param   container:  The container to add button instances to.
	     * @param   pageLimit:  The limit of data to be displayed at one time
	     */
		public function paginate(collection:ArrayCollection, container:*, pageLimit:int):void
		{
			masterCollection = collection;
			btnContainer = container;
			limit = pageLimit;
			
			//Reset Values
			pageButtonCollection = new ArrayCollection();
			btnContainer.removeAllChildren();
			
			//Set Intial
			highLimit = limit;
			
			var collLengthSubHigh:int = masterCollection.length - highLimit;
			var numberOfTimes:int = masterCollection.length / highLimit;
			var extraData:int = masterCollection.length - (highLimit * numberOfTimes);
			
			
			//Adds Btns to Container and sets highLimit/lowLimts vars in each PageButton Instance
			for(var i:int = 0; i < numberOfTimes; i++)
			{
				pageButton = new PageButton();
				var a:int;
				var b:int;
				var c:int;
				
				if(numberOfTimes <= 1)
				{
					a = (highLimit / numberOfTimes);
					b = a * i;
					c = b + a -1;
				}
				else if(numberOfTimes > 1)
				{
					a = (lowLimit + highLimit);
					b = a * i;
					c = b + a -1;
				}
				
				pageButton.lowLimit = b;
				pageButton.highLimit = c;
				
				if(b == c){
					pageButton.label = (c + 1).toString();
				}else{
					pageButton.label = (b + 1) + " - " + (c + 1);
				}
				pageButtonCollection.addItem({component:pageButton});
			}
			
			//Grab Extra integers
			if(extraData != 0)
			{
				var pageButtonExtra:PageButton = new PageButton();
				if(c!=0)
				{
					pageButtonExtra.lowLimit = c + 1;
					pageButtonExtra.highLimit = masterCollection.length - 1;
					if((c+2) == masterCollection.length){
						pageButtonExtra.label = masterCollection.length.toString();
					}
					else{
						pageButtonExtra.label = (c + 2) + " - " + masterCollection.length;
					}
				}
				else
				{	
					pageButtonExtra.lowLimit = 0;
					pageButtonExtra.highLimit = masterCollection.length - 1;
					if(masterCollection.length == 1)
					{
						pageButtonExtra.label = masterCollection.length.toString();
					}
					else
					{
						pageButtonExtra.label = (c+1) + " - " +masterCollection.length;
					}
				}
				pageButtonCollection.addItem({component:pageButtonExtra});
			}
			
			createButtonComponents();
		}
		
		
		/**
	     * Creates Buttons in container from Collection
	     *
	     */
		private function createButtonComponents():void
		{
			
			for(var j:int = 0; j < pageButtonCollection.length; j++)
			{	
			   var item:Object = pageButtonCollection.getItemAt(j);
			   btnContainer.addChild(item.component);
			}
		}
		
		 /**
	     * Loops through Collection specified by high low values and Displays Corresponding Data
	     *
	     * @param   collection: The main collection to get paginated data from
	     * @param   lowLimit:  the start of the displayed data
	     * @param   highLimit: the end of the displayed data
	     * @return  ArrayCollection: An ArrayCollection of filtered data
	     */
		public function getPaginatedData(collection:ArrayCollection, lowLimit:int, highLimit:int):ArrayCollection
		{
			var temp:ArrayCollection = new ArrayCollection ();
			// 
			for(var k:int = lowLimit; k <= highLimit; k++)
			{
				if(collection[k] != null)
				{
					temp.addItem(collection[k]);
				}
			}
			
			return temp;
			
		}	
		
	}
}