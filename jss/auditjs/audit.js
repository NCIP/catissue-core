
/**
 * this methods use to get audit data from server before loading the page
 */
function onAuditPageLoad() 
{
	getUsers();
	getObjects(); 
	getEvents();
	initChosen()
    initDatePicker();
}
/*
 * set user name and id to Select Search-Box
 */

function getUsers()
{
    $.ajax({
            type: "POST",
            url: "rest/ng/auditreport/users",
            processData: true,
            async: false,
            success: function (jsonObject, status, jqXHR) { 
            setUsers(jsonObject);	
 	    	             
           },
           error: function (xhr) {
               alert("error :"+xhr.responseText);
           }
    });
	
}

function setUsers(userDetails)
{
	 for (var j = 0; j < userDetails.length; j++) {
	   	var newOpt = $('<option>');
		newOpt.attr('value', userDetails[j].identifier);
		newOpt.text(userDetails[j].firstName + " " + userDetails[j].lastName);
		$('#userId').append(newOpt);
		$('</option>');
		}
}
/*
 * set Object Type to Select Search-Box
 */
function getObjects()
{
	$.ajax({
         type: "POST",
         url: "rest/ng/auditreport/objects",
         processData: true,
         async: false,
         success: function (objectList, status, jqXHR) {
         
			if(objectList != null)
			{
			   setObjects(objectList);
			}
       },
       error: function (xhr) {
           alert("error :"+xhr.responseText);
       }
    });
	
}

function setObjects(objectList)
{
	$.each(objectList, function(key, value) {
   		var newOpt = $('<option>');
		newOpt.attr('value', value);
		newOpt.text(key);
		$('#object_type').append(newOpt);
		$('</option>')
});
	
}

/*
 * set Event Type to Select Search-Box
 */
function getEvents()
{
	$.ajax({
            type: "POST",
            url: "rest/ng/auditreport/events",
            processData: true,
            async: false,
            success: function (eventTypes, status, jqXHR) 
                 { 
					if(eventTypes != null)
                 	{
						setEvents(eventTypes);
                 	}
                 		
                 },
                 error: function (xhr) {
                     alert("error :"+xhr.responseText);
                 }
             });
}

function setEvents(eventTypes)
{
	$.each(eventTypes, function(key, value) {
   		var newOpt = $('<option>');
		newOpt.attr('value', value);
		newOpt.text(value);
		$('#operation').append(newOpt);
		$('</option>')
   });
}
function initChosen()
{
	jQuery('#userId').chosen({"search_contains": true});
	jQuery('#object_type').chosen({"search_contains": true});
	jQuery('#operation').chosen({"search_contains": true});
}

function initDatePicker()
{
	$('#datefrom , #dateto').datepicker(
     {
         changeMonth: true,
         changeYear: true,
         dateFormat: 'yy-mm-dd'
     });
}

/**
 * this method use to generate csv file
 */
function exportData() 
{
	try{
		
	var jsonObject = new Object();
	if($('#userId').val() != null)
	{
		jsonObject.userIds = $('#userId').val();
	}
	if($('#object_type').val() != null)
	{
		jsonObject.objectTypes = $('#object_type').val();
	}
	if($('#operation').val() != null)
	{
		jsonObject.operations = $('#operation').val();
	}
	if($('#datefrom').val() != "")
	{
		jsonObject.startDate = $('#datefrom').val();
	}
	if($('#dateto').val() != "")
	{
		jsonObject.endDate = $('#dateto').val();
	}
	if(jQuery.isEmptyObject(jsonObject))
	{
		alert("Please select at least one condition");
	}
	else
	{
		window.open("rest/ng/auditreport?json="+ JSON.stringify(jsonObject));
	}

	}
	catch(e)
	{ 
	    $(function() {
	    $( "#error_msg" ).dialog("there was some error");
	    });
	}
	 
}