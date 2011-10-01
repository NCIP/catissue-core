package util
{
    import mx.controls.Alert;
	import mx.validators.Validator;
	import mx.validators.NumberValidator;
	import mx.events.ValidationResultEvent;
	import util.ComboBoxValidator;

    public class SpecimenValidator
    {
	     public function validateRequiredField(source:Object,property:String,errorStr:String= null):Boolean
		{
			var validator:Validator = new Validator();
			validator.source= source;
			validator.property=property;
			if(errorStr !=null)
			{
				validator.requiredFieldError=errorStr;
			}	
			var result:ValidationResultEvent = validator.validate();
			return checkValidationResult(result);
	
		}

		public function validateNumberField(source:Object,property:String,errorStr:String= null,required:Boolean=true,minVal:Number = 0):Boolean
		{
			var numberV:NumberValidator = new NumberValidator();
			numberV.source= source;
			numberV.property=property;
			numberV.minValue = minVal;
			numberV.required = required;
			if(errorStr !=null)
			{
				numberV.requiredFieldError=errorStr;
			}	
			var result:ValidationResultEvent = numberV.validate();
			return checkValidationResult(result);
		}

		public function validateComboBoxField(source:Object,property:String,errorStr:String= null):Boolean
		{
			var comboFieldV:ComboBoxValidator  = new ComboBoxValidator();
			comboFieldV.source= source;
			comboFieldV.property=property;
			if(errorStr !=null)
			{
				comboFieldV.requiredFieldError=errorStr;
			}
			var result:ValidationResultEvent = comboFieldV.validate();
			return checkValidationResult(result);
		}

		private function checkValidationResult(result:ValidationResultEvent) : Boolean
		{
			if(result.type == ValidationResultEvent.INVALID)
			{
				return false;
			}
			else
			{
				return true;
			}
	    }
	}
}