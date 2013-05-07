package util
{
	import mx.binding.utils.BindingUtils;
	import mx.messaging.ChannelSet;
	import mx.core.Application;
	import mx.messaging.channels.AMFChannel;
	import mx.messaging.channels.SecureAMFChannel;
	
	public class Utility
	{
		public static function bindData(uiObj:Object, uiProperty:String, dataObj:Object, dataProperty:String, biDirectional:Boolean=true):void
		{
			BindingUtils.bindProperty(uiObj, uiProperty, dataObj, dataProperty);
			if(biDirectional)
				BindingUtils.bindProperty(dataObj, dataProperty, uiObj, uiProperty);
		}
		
		
		private static function getContextRootUrl():String 
	 	{
			 var urlStr:Array = Application.application.url.split("/");
			 var contextRootUrl:String = urlStr[0]+"//"+urlStr[2]+"/"+urlStr[3];
	  	   return contextRootUrl;
		}
		private static function getProtocolType():String
		{
			 var urlStr:Array = Application.application.url.split(":");
			if(urlStr != null)
			{
				if(urlStr.length >0)
				{
					return urlStr[0];
				}
			}
			return null;
		}
  		public static function getChannelSet() : ChannelSet
  		{
 			var myChannelSet:ChannelSet = new ChannelSet();
 		
  			var protocolType:String = getProtocolType();
			if(protocolType !=null)
  			{
  				if(protocolType == "http" || protocolType == "HTTP") 
  				{
					var amfChannel:AMFChannel = new AMFChannel("my-amf", getContextRootUrl() + "/messagebroker/amf");
					 amfChannel.pollingEnabled = false;
					 myChannelSet = new ChannelSet();
					 myChannelSet.addChannel(amfChannel);
				 }
				 else if(protocolType == "https" || protocolType == "HTTPS")
				 {
				 	var secureAmfChannel:SecureAMFChannel = new SecureAMFChannel("my-amf", getContextRootUrl() + "/messagebroker/amfsecure");
					 secureAmfChannel.pollingEnabled = false;
					 myChannelSet = new ChannelSet();
					 myChannelSet.addChannel(secureAmfChannel);
				 	
				 }
			 
  			}
  			return myChannelSet;
  		}
	}
}