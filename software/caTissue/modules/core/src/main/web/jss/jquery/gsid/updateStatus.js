var gsidIntervalId = 0;
var $j = jQuery.noConflict();
$j(function(){
	var locked=$j('input:hidden[name="islocked"]').attr('value')+'';	
	if(locked=='true')
	{		
		gsidIntervalId=setInterval("updateStatus()",3000);
	}	
});

function updateStatus()
{	
	if($j('#gsidPercentage').html()==100)
	{		
		clearInterval(gsidIntervalId);
		return;
	}
	$j.ajax(
	{
		url:'gsidBatchUpdateStatus.do',	 
		cache:false,
        type: "POST",       
        success: function(html)
        {  
        
        	var percentage= $j(html).children('#percentage').first().html();
        	var error=$j(html).children('#error').first().html()+'';        	
        	if(error=='true')
    		{        		
        		window.location='gsidBatchUpdate.do';
        		clearInterval (gsidIntervalId);
    		}
    		if(html)
			{
				$j('#gsidPercentage').html(percentage);
				if(html==100)
				clearInterval (gsidIntervalId);			
    		}    	   
        },
        error: function()
        {
        	alert('Error Occured');        	
        	clearInterval (gsidIntervalId);
        }
	});
}