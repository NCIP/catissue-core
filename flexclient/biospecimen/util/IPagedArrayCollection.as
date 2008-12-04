////////////////////////////////////////////////////////////////////////////////
//
// @file: IPagedArrayCollection
// @author: Jignesh Wala
// @date: 27-11-2008
// @description: interface for pagedArrayCollection.
//
////////////////////////////////////////////////////////////////////////////////
package util
{
	public interface IPagedArrayCollection
	{
		function get currentPage() : Number
		function set currentPage( value:Number ) : void;
		
		function get numberOfPages() : Number;
		
		function get pageSize() : Number;
		function set pageSize( value:Number ) : void;
		
		function get lengthTotal() : Number; 	
	}
}