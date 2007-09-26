package Components
{
	import flash.events.Event;
	
	// simple event that can hold a VO data payload
	public class ClickEvent extends Event
	{
		public static var CLICK:String = "CHECK_CLICK";
		
		public var vo:*;
		
		public function ClickEvent(a_vo:*)
		{
			// pass type and set bubbles (2nd param) to true
			super(CLICK, true, true);
			vo = a_vo;
		}
	}
}