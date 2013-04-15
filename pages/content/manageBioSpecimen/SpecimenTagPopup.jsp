<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/multiSelectUsingCombo.tld" prefix="mCombo" %>
<%@ page import="java.util.Map,java.util.List,java.util.ListIterator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>


<script>var imgsrc="/images/de/";</script>
<script language="JavaScript" type="text/javascript" src="javascripts/de/scr.js"></script>
<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />
<script language="JavaScript" type="text/javascript" src="javascripts/de/ajax.js"></script>
<script type='text/JavaScript' src='jss/advQuery/scwcalendar.js'></script>
<link rel="stylesheet" type="text/css" href="css/advQuery/tag-popup.css" />
<script language="JavaScript" type="text/javascript"	src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/ext-all.js"></script>

<script>
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
			ajaxShareTagFunctionCall("ShareTagAction.do","Select at least one existing list.") 
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
		if (this.getAt(0) != null && this.getAt(0).get('excerpt')) 
		{
			combo.typeAheadDelay=50;
		} else {combo.typeAheadDelay=60000}
		});});
		
		
function setHeader(isSpecimenChecked)
{
	if(isSpecimenChecked == true){		 
		document.getElementById("poupHeader").textContent ="Assign the specimen(s) to list";
		document.getElementById("poupHeader").innerText ="Assign the specimen(s) to list";
	}else{
		document.getElementById("poupHeader").textContent ="Share the list(s) with user";
		document.getElementById('protocolCoordinatorIds').style.marginLeft= "15px";
		document.getElementById('addButton_coord').style.marginLeft= "23px";
		document.getElementById('removeButton_coord').style.marginLeft= "23px";
		popupmygrid.setColumnHidden(2,true);
		document.getElementById("poupHeader").innerText ="Share the list(s) with user";
	} 
}

function assignTargetCallFunc()
{
	if(document.getElementById('assignTargetCall')!=null)
	{
		eval(document.getElementById('assignTargetCall').value);
	}
}
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
					<a onclick="popup('popUpDiv')"><img style="float: right; cursor:pointer;"
						height='23' width='24' title="Close" src='images/advQuery/close_button.gif'
						border='0'> </a>
					 
					<table class=" manage tags" width="100%" cellspacing="0"
						cellpadding="0" border="0">
							<tr valign="center" height="35" bgcolor="#d5e8ff">
								<td width="27%" align="left"
									style="font-size: 1em; font-family: verdana;">
									<p>
									     <div id="poupHeader"><b> Specimen Lists </b></div>
									</p>
								</td>
							</tr>
							<tr>
								<td align="left">
									<div id="treegridbox"
											style="width: 530px; height: 170px; background-color: white;"></div>
								</td>
							</tr>
							<tr>
								<td  align="left">
									<p>
											<label id="newTagLabel" width="28%" align="left"
											style="margin-left :20px; font-size: .82em; font-family: verdana;"><b>List Name 
											: </b> </label> <input type="text" id="newTagName" name="newTagName"
											size="17" onclick="this.value='';" maxlength="50" />
										    <label id="shareLabel" width="28%" align="left"
											style="margin-left :20px; font-size: .82em; font-family: verdana;"><b> Share to users :
					 						</b> </label>
									</p>
								</td>
							</tr>
							<tr>
								<td  align="left">
									<div id="multiSelectId" class="black_ar_new" style="display:none; margin-left:35px">
											&nbsp&nbsp&nbsp<mCombo:multiSelectUsingCombo identifier="coord" styleClass="black_ar_new"  size="20" addButtonOnClick="moveOptions('coord','protocolCoordinatorIds', 'add')" removeButtonOnClick="moveOptions('protocolCoordinatorIds','coord', 'edit')" selectIdentifier="protocolCoordinatorIds" collection="<%=(List)request.getAttribute("selectedCoordinators")%>"/>
								 
									</div>
								</td>
							</tr>
							<tr>
								<td height="10px;"> </td>
							</tr>
							<tr>
								<td>	
									<p> 			
 										&nbsp&nbsp&nbsp
										<input type="button" id="assignButton" value="ASSIGN" title="Assign" onclick="assignTargetCallFunc()"
												onkeydown="assignTargetCallFunc()"  class="btn3">
				 
 										&nbsp&nbsp&nbsp
										<input type="button"  id="shareButton" value="SHARE LIST" title="Share List (List will be visible to the users you choose)" onclick="checkForValidation()"
												onkeydown="checkForValidation()" style="display:none; width:120px; display:none" class="btn3">
									
										<img id="loadingImg" style="float:left; padding-left:5px; display:none;"
												height='25px' width='120px' src='images/advQuery/loading_circle.gif'
										border='0'>
									</p>
								</td>
							</tr>
						</table>
				</div>
			</div>
</body>
</html>
