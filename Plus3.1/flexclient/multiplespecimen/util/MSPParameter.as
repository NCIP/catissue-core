package util
{	
	public class MSPParameter
	{
		public static const MODE_PARAM:String = "MODE";
		public static const MODE_PARAM_VAL_ADD:String = "add";
		public static const MODE_PARAM_VAL_EDIT:String = "edit";
		
		public static const PARENT_PARAM:String = "PARENT";
		public static const PARENT_TYPE_PARAM_VAL_SCG:String = "New_Specimen";
		public static const PARENT_TYPE_PARAM_VAL_SP:String = "Derived_Specimen";
		
		public static const PARENT_NAME_PARAM:String = "PARENT_NAME";
		public static const SP_COUNT_PARAM:String = "SP_COUNT";

		
		public var mode:String = "";
		public var parentType:String = "";
		public var parentName:String = "";
		public var spCount:String = "";
		public var showParentSelection:String="";
		public var showLabel:String="";
		public var showBarcode:String="";
		public var dateFormat:String="";
		
		public function toString():String
		{
			return  "mode "+mode+"\n"+
					"parent "+parentType+"\n"+
					"parentName "+parentName+"\n"+
					"spCount "+spCount+"\n" +
					"showLabel "+showLabel+"\n"+
					"showBArcode "+showBarcode;
		}
		
		public function validate():String
		{
			var msg:String = "SUCCESS";
			
			if(!checkForMode())
			{
				return "MODE can either be ADD OR EDIT";
			}
			
			if(mode==MODE_PARAM_VAL_ADD)
			{
				if(!checkForParentType())
				{
					return "MODE can either be SCG OR SP";
				}
				
				if(!checkForSPCount())
				{
					return "Invalid SP count";
				}
			}
			else
			{
				//TODO
			}
			
			return msg;
		}
		
		private function checkForMode():Boolean
		{
			if(mode==MODE_PARAM_VAL_ADD || mode==MODE_PARAM_VAL_EDIT)
				return true;
			return false;
		}

		private function checkForParentType():Boolean
		{
			if(parentType==PARENT_TYPE_PARAM_VAL_SCG || parentType==PARENT_TYPE_PARAM_VAL_SP)
				return true;
			return false;
		}
		
		private function checkForSPCount():Boolean
		{
			if(spCount!=null)
			{
				return true;
			}
				
			return false;
		}
	}
}