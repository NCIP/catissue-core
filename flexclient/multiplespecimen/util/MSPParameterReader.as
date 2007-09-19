package util
{
	import mx.controls.Alert;
	
	public class MSPParameterReader
	{
		public static function processParam(parameter:Object):MSPParameter
		{
			var mspParameter:MSPParameter = new MSPParameter();
			mspParameter.mode = parameter.MODE;
			mspParameter.parentType = parameter.PARENT_TYPE;
			mspParameter.parentName = parameter.PARENT_NAME;
			mspParameter.spCount = parameter.SP_COUNT;
			mspParameter.showParentSelection = parameter.SHOW_PARENT_SELECTION;

			//Alert.show(mspParameter.toString());
			
			return mspParameter;
		} 
		
	}
}