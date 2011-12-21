/***
 * author: Srikalyan
 */
var $j = jQuery.noConflict();
jQuery(document).ready(
		$j(function()
		{
			var gsid=$j('form input[name="globalSpecimenIdentifer"]').attr('value');
			if (gsid) {
				//$j('#updateGSID').remove();   
				//alert ("disabling...");
				$j('#updateGSID').remove();   
				//$j('#updateGSID').attr('disabled', 'disabled');	
			}
		})
)
		
$j(function(){
	
	$j('#updateGSID').click(function(){
		$j('#globalSpecimenIdentifer').val("");

		var specimen_id=$j('form input[name="id"]').attr('value');
		
		var speed=600;
		var noOftimes=4;
		if(specimen_id == 0){
			alert ("Please save the Specimen before assigning a Global Specimen Identifier.");
			return false;
		}
		if(specimen_id)
		{
			$j('<div id="overlay" class="overlay"></div>').appendTo("body").fadeIn('fast');
			$j.ajax({
				url:'updateSpecimensGSID.do',
			    cache: false,
                type: "POST",
                data: {
                	'id':specimen_id
                },
                success: function(html)
                {               	
                	if(html)
            		{     
                		var noError;
                		if($j.trim(html)!='GSID service might be down')
            			{
                			$j('#updateGSID').remove();   
                			//$j('#updateGSID').attr('disabled', 'disabled');	
                			$j('#globalSpecimenIdentifer').val(html);
                			//$j('label[for="globalSpecimenIdentifierValue"]').html(html);                			
            			}
                		else
            			{
                			noError=true;
                			//$j('#gsidErrorLabel').remove();             	
                        	$j('label[for="globalSpecimenIdentifierValue"]').append("<span id='gsidErrorLabel' style='color:red'> Error: GSID service might be down.</span>");
                        	$j('#gsidErrorLabel').effect('shake',{distance:10,times:noOftimes},speed);
            			}
            		}
            	    $j('#overlay').fadeOut('400',function(){
            	    	$j(this).remove();
                    });            
            	    if(!noError)
            	    {
            	    	$j('label[for="globalSpecimenIdentifierValue"]').css('color','green').effect('pulsate',{times:noOftimes},speed);
            	    }
            	    
                },
                error: function(){   
                	//$j('#gsidErrorLabel').remove();             	
                	$j('label[for="globalSpecimenIdentifierValue"]').append("<span id='gsidErrorLabel' style='color:red'> Error: GSID service might be down.</span>");
                	$j('#gsidErrorLabel').effect('shake',{distance:10,times:noOftimes},speed);
                	$j('#overlay').fadeOut('400',function(){
            	    	$j(this).remove();
                    });
                }
                
			});
			
		} 
		
		return false;
	});	
});