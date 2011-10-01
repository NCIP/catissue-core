package util
{
    import mx.validators.Validator;
    import mx.validators.ValidationResult;

    public class ComboBoxValidator extends Validator {

        // Define Array for the return value of doValidation().
        private var results:Array;


		public function ComboBoxValidator () {
            super();
       
        }
        
        
        override protected function doValidation(value:Object):Array {
        
            var str:String = value as String;

            // Clear results Array.
            results = [];

            // Call base class doValidation().
            results = super.doValidation(str);        
            // Return if there are errors.
            if (results.length > 0)
                return results;

            // Check first name field. 
            if (str == "-- Select --" ) {
            
              	results.push(new ValidationResult(true, 
                    "select", "noSelectedValue", requiredFieldError));
             
                return results;
            }
                        
            return results;
        }
        

    }
}
