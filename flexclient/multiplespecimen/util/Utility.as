package util
{
	import mx.binding.utils.BindingUtils;
	
	public class Utility
	{
		public static function bindData(uiObj:Object, uiProperty:String, dataObj:Object, dataProperty:String, biDirectional:Boolean=true):void
		{
			BindingUtils.bindProperty(uiObj, uiProperty, dataObj, dataProperty);
			if(biDirectional)
				BindingUtils.bindProperty(dataObj, dataProperty, uiObj, uiProperty);
		}
	}
}