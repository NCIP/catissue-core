
//set proper href for the text field to be sent to TitliInitialiseSearch.do
function titliSetHREF(searchString, goObject)
{
	if (goObject.href.indexOf("?") != -1)
	{
		goObject.href = goObject.href.substring(0,goObject.href.indexOf('?'));
		
	}

	goObject.href= goObject.href + '?searchString='+searchString.value;
}


//if enter is pressed in the text field, search should be fired with proper URL
function titliOnEnter( evt, searchString, goObject) 
{
	 var keyCode = null;

	 if( evt.which ) 
	 {
		keyCode = evt.which;
	 } 
	 else if( evt.keyCode ) 
	 {
		keyCode = evt.keyCode;
	 }
	 
	 if( 13 == keyCode ) 
	 {
		
		titliSetHREF(searchString, goObject);
		top.location.href = goObject.href;
		
		return false;
	 }
		return true;
}


