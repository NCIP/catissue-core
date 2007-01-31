/*
Generic function to create ActiveXObject for ajax  
*/



function newXMLHTTPReq() //create ActiveX  or XMLHttpRequest object  
{
	var obj = false;
	try //assuming a microsoft browzer that supports MSXML2 parser
	{
		obj = new ActiveXObject("Msxml2.XMLHTTP");
	}
	catch (e)
	{
		//assuming a microsoft browzer
		try 
		{
			obj = false;
			obj = new ActiveXObject("Microsoft.XMLHTTP");
		
		}
		catch (e1)
		{
			// for Mozilla
			try  
			{    
				obj = false;
				obj = new XMLHttpRequest(); 

			}
			catch (e2)
			{
				obj= false;
			}
		}
	}
	return obj;
}

/*Generic function to return response to ajax
isText : boolean  
isText=true : http_request.responseText will be returned i.e response as a string of text 
isText= false : http_request.responseXML will be returned i.e the response as an XMLDocument object you can traverse  
*/


function getReadyStateHandler(request,responseXmlHandler,isText)
{
	return function ()
	{
		//If the state has value  4 then full server response is received
		if(request.readyState == 4)  
		{
			if(request.status == 200)
			{
				if(isText==true)
				{
					responseXmlHandler(request.responseText);
				}
				else
				{
					responseXmlHandler(request.responseXml);
			        }
		       }
		}
    }
}

