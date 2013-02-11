<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/multiSelectUsingCombo.tld" prefix="mCombo" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>


<script>var imgsrc="/images/de/";</script>
<script language="JavaScript" type="text/javascript" src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript" src="javascripts/de/scr.js"></script>
<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />
<script language="JavaScript" type="text/javascript" src="javascripts/de/ajax.js"></script>
<script type='text/JavaScript' src='jss/advQuery/scwcalendar.js'></script>

<script>
window.onload = function(){  
	document.getElementById('protocolCoordinatorIds').style.marginLeft= "40px";
} 

function showHideCombo(){
	if(document.getElementById("multiSelectId").style.display =="block"){		 
		var tdId = "multiSelectId";
		document.getElementById("assignButton").style.display="block"
		document.getElementById("shareButton").style.display="none"
		document.getElementById(tdId).style.display="none";
	}else{
		var tdId = "multiSelectId";
		document.getElementById("assignButton").style.display="none"
		document.getElementById("shareButton").style.display="block"
		document.getElementById(tdId).style.display="block";
	}
}

function checkForValidation()
{
	var tdId = "multiSelectId";
	var displayStyle = 	document.getElementById(tdId).style.display;
	if(displayStyle == "block")
	{
		var coords = document.getElementById('protocolCoordinatorIds');
		if(coords.options.length == 0)
		{
			alert("Please select atleast one user from the dropdown");
		}
		else
		{
			ajaxShareTagFunctionCall("ShareTagAction.do","Select at least one existing list or create a new list.") 
		}
	}
}


Ext.onReady(function(){
	var myUrl= 'ShareQueryAjax.do?';
	var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),
	reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});
	var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_coord',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'coord'});

combo.on("expand", function() {
	if(Ext.isIE || Ext.isIE7)
	{
		combo.list.setStyle("width", "250");
		combo.innerList.setStyle("width", "250");
	}else{
		combo.list.setStyle("width", "250");
		combo.innerList.setStyle("width", "250");
	}
	}, {single: true});
	ds.on('load',function(){
		if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) 
		{
			combo.typeAheadDelay=50;
		} else {combo.typeAheadDelay=60000}
		});});
</script>
 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
</table> 
<!-- action buttons end -->
	<div id="blanket" style="display: none;"></div>
				<div id="popUpDiv" style="display: none; top: 100px; left: 210.5px;">
					<a onclick="popup('popUpDiv')"><img style="float: right;"
						height='23' width='24' title="Close" src='images/advQuery/close_button.gif'
						border='0'> </a>
					 
					<table class=" manage tags" width="100%" cellspacing="0"
						cellpadding="2" border="0">
							<tr valign="center" height="35" bgcolor="#d5e8ff">
								<td width="27%" align="left"
									style="font-size: .82em; font-family: verdana;">
									<p>
										&nbsp&nbsp&nbsp&nbsp<b>Specimen Lists</b>
									</p>
								</td>
							</tr>
							<tr>
								<td align="left">
									<div id="treegridbox"
											style="width: 530px; height: 170px; background-color: white;"></div>
								</td>
							</tr>
						</table>
						<table>
							<tr>
								<td  align="left">
									<p>
											&nbsp&nbsp&nbsp<label width="28%" align="left"
											style="font-size: .82em; font-family: verdana;"><b>List Name 
											: </b> </label> <input type="text" id="newTagName" name="newTagName"
											size="17" onclick="this.value='';" maxlength="50" />
										
											<label id="shareCheckboxId" style="font-size: 0.7em; font-family: verdana;"><b><input type="checkbox"
											property="shareTo" name='shareCheckbox' id="shareToCheckbox" value="users"
											onclick="showHideCombo()" /> Share List</b> </label>&nbsp;<label id ="shareLabelId"
											style="font-size: 0.5em;font-family: verdana;">(This List will be visible to the users you choose)</label>
									</p>
								</td>
							</tr>
							<tr>
								<td id="multiSelectId" nowrap=""  class="black_ar_new" style="display:none;margin-left:20px" align="left">
									<p> 
											&nbsp&nbsp&nbsp<mCombo:multiSelectUsingCombo identifier="coord" styleClass="black_ar_new"  size="19" addButtonOnClick="moveOptions('coord','protocolCoordinatorIds', 'add')" removeButtonOnClick="moveOptions('protocolCoordinatorIds','coord', 'edit')" selectIdentifier="protocolCoordinatorIds" collection="<%=(List)request.getAttribute("selectedCoordinators")%>"/>
									</p>
								</td>
							</tr>
							<tr>
								<td>	
									<p> 			
 										&nbsp&nbsp&nbsp
										<input type="button" id="assignButton" value="ASSIGN" title="Assign" onclick="<%=assignTargetCall%> "
												onkeydown="<%=assignTargetCall%> "  class="btn3">
				 
 										&nbsp&nbsp&nbsp
										<input type="button"  id="shareButton" value="SHARE" title="Share List (List will be visible to the users you choose)" onclick="checkForValidation()"
												onkeydown="checkForValidation()" style="display:none; width:120px; display:none" class="btn3">
									</p>
								</td>
							</tr>
						</table>
				</div>
			</div>
</body>
</html>
