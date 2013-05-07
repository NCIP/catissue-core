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
			mspParameter.showLabel = parameter.SHOW_LABEL;
			mspParameter.showBarcode = parameter.SHOW_BARCODE;
			mspParameter.dateFormat= parameter.DATE_FORMAT;
			return mspParameter;
		} 
		
	}
}