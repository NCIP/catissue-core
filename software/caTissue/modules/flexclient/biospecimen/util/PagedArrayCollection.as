////////////////////////////////////////////////////////////////////////////////
//
// @file: PagedArrayCollection
// @author: Jignesh Wala
// @date: 27-11-2008
// @description: class pagedArrayCollection which in implements IPagedArrayCollection.
//
////////////////////////////////////////////////////////////////////////////////
package util
{
	import mx.collections.ArrayCollection;
	import mx.events.CollectionEvent;

	public class PagedArrayCollection extends ArrayCollection implements IPagedArrayCollection
	{
		private var _currentPage:Number = 1;
		private var _numberOfPages:Number = 1;
		private var _pageSize:Number = 10;
		
		public function PagedArrayCollection(source:Array=null)
		{
			super( source );
			filterFunction = filterData;
			addEventListener( CollectionEvent.COLLECTION_CHANGE, onChange );
		}		
		
		/**
		 * Adds an item to the collection at the specified index.
		 * 
		 * @param item Item to be added
		 * @param index Index of the item to be added
		 * 
		 * Note: Needs to be overridden in order to trigger refresh. AddItem eventually calls this function so its not needed to override addItem 
		 */		
		override public function addItemAt( item:Object, index:int ) : void
		{
			super.addItemAt( item, index );
			refresh();
		}

		/**
		 * Removes the item from the collection at the specified index
		 * 
		 * @param index Index of the item to be removed
		 * @return The item removed
		 * 
		 * Note: Needs to be overridden in order to trigger refresh 
		 */		 			
		override public function removeItemAt( index:int ) : Object
		{
			var removedItem:Object = super.removeItemAt( index );
			refresh();
			return removedItem;
		}
				
		protected function onChange( event:CollectionEvent ) : void
		{
			if( _numberOfPages != numberOfPages )
			{
				_numberOfPages = numberOfPages;
				onPagingChange( PagedCollectionEventKind.NUMBEROFPAGES_CHANGE );
			}		
		}
		
		protected function onPagingChange( kind:String ) : void
		{
			dispatchEvent( new CollectionEvent( CollectionEvent.COLLECTION_CHANGE, false, false, kind ) );
		}		
		
		[ChangeEvent("collectionChange")]
		public function get currentPage() : Number
		{
			return _currentPage;
		}
		public function set currentPage( value:Number ) : void
		{
			_currentPage = value;
			refresh();
			onPagingChange( PagedCollectionEventKind.CURRENTPAGE_CHANGE );
		}
		
		[ChangeEvent("collectionChange")]
		public function get numberOfPages() : Number
		{
			var result:Number = source.length / pageSize;
			result = Math.ceil( result );
			return result;
		}		
		
		[ChangeEvent("collectionChange")]
		public function get pageSize() : Number
		{
			return _pageSize;
		}
		public function set pageSize( value:Number ) : void
		{
			_pageSize = value;
			refresh();
			onPagingChange( PagedCollectionEventKind.PAGESIZE_CHANGE );
		}

		[ChangeEvent("collectionChange")]
		public function get lengthTotal() : Number
		{
			return source.length;
		}
				
		private function filterData( item:Object ) : Boolean
		{
			var dataWindowCeiling:Number = pageSize * currentPage;
			var dataWindowFloor:Number = dataWindowCeiling - pageSize;			
			
			var itemIndex:Number = getItemIndex( item );
			
			var result:Boolean = dataWindowFloor <= itemIndex && itemIndex < dataWindowCeiling;			
			return result;
		}
	}
}