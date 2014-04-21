<!DOCTYPE html>

<html>
<head>
<link rel="stylesheet" href="css/auditcss/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="jss/angular/choosen/chosen.css" />
<link rel="stylesheet" type="text/css" href="css/auditcss/audit.css" />

<script src="jss/angular/jquery-1.10.2.js"></script>
<script type='text/javascript' src='ngweb/external/jquery/chosen.jquery.min.js'></script>
<script type="text/javascript" src="jss/auditjs/audit.js"> </script>
<script type="text/javascript" src="ngweb/external/jquery/jquery-ui.min.js"> </script>
</head>

<body onload="javascript : onAuditPageLoad()">
  <div class="panel panel-primary">
		<div class="panel-heading header" >
                  <h5 align="left" class="headerText" >Audit Report</h5>
                </div>

	        <div class="panel-body panel-primary div_class" style="background-color: rgb(255, 255, 255)">
			
					<div class="label"><p>User Name(s)</p> </div>
			
					<div class="field">
					<select tabindex="4" name="user_name" id="userId" class ="select_class" multiple></select>
					</div>
					<div style="clear: both;"></div>
					<div class="label"> <p > Object Name(s) </p> </div>		
							
					<div class="field">
					<select name="object" id="object_type" class ="select_class" multiple></select>
					</div>
		      <div style="clear: both;"></div>
		          <div class="label">
					<p> Operation(s) </p>
		      	  </div>
			  
		     	<div class="field">
				<select name="object" id="operation" class ="select_class" multiple>
				</select>
		    	</div>
			  <div style="clear: both;"></div>
		
		       <div class="label">
			    <p > Date From </p>
		       </div>
		  
		      <div class="field">
			  <input type="text" id="datefrom" class ="input_class"  />
		      </div>
			<div style="clear: both;"></div>
     			<div class="label">
	    		<p > Date To </p>
		    	</div>
			 
			<div class="field" class ="">
			<input type="text" id="dateto" class ="input_class" />
			</div>
			<div style="clear: both;"></div>
			<div style="height:50px"></div>
			<div  >
			<button class="btn btn-primary button_class" onclick="javascript : exportData()">Export</button>
			</div>
			
			<div id="error_msg" title="Basic dialog" style = "display : none">Please Contact to Administrator</div>
	  </div>
</body>
</html>