////////////////////////////////////////////////////////////////////////////////
//
// @file: MessageBoxHelper
// @author: Jignesh Wala
// @date: 11-08-2008
// @description: customized alerts with predefined icons, title etc.
//
////////////////////////////////////////////////////////////////////////////////
package Components
{	
	public class MessageBoxHelper
	{
	
        [Embed(source="../alert_info.gif")]
		public static var INFO_ICON:Class;		
		
        [Embed(source="../alert_error.gif")]
        public static var ERROR_ICON:Class;
		
		[Embed(source="../alert_confirm.gif")]		       
        public static var WARNING_ICON:Class; 

		public static var INFO_TITLE:String = "Info";
		public static var ERROR_TITLE:String = "Error";
		public static var WARNING_TITLE:String = "Warning";

		public static var SERVICE_ERROR_TITLE:String = "Service Error";
		public static var SERVICE_WARNING_TITLE:String = "Service Warning";

		public function MessageBoxHelper()
		{
			throw new Error("This class is static");
		}
	}
}
