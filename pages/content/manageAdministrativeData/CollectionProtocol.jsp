<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>

<head>

<%
	List tissueSiteList = (List) request.getAttribute(Constants.TISSUE_SITE_LIST);

	List pathologyStatusList = (List) request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST);
	
    String operation = (String) request.getAttribute(Constants.OPERATION);
    String formName;

		String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
		String appendingPath = "/CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol";
		if (reqPath != null)
			appendingPath = reqPath + "|/CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol";
	
	   	if(!operation.equals("add") )
	   	{
	   		Object obj = request.getAttribute("collectionProtocolForm");
			if(obj != null && obj instanceof CollectionProtocolForm)
			{
				CollectionProtocolForm form = (CollectionProtocolForm)obj;
		   		appendingPath = "/CollectionProtocolSearch.do?operation=search&pageOf=pageOfCollectionProtocol&systemIdentifier="+form.getSystemIdentifier() ;
		   	}
	   	}
		System.out.println("CP JSP : ----- " + appendingPath);
		

    boolean readOnlyValue;
    if (operation.equals(Constants.EDIT))
    {
        formName = Constants.COLLECTIONPROTOCOL_EDIT_ACTION;
        readOnlyValue = false;
    }
    else
    {
        formName = Constants.COLLECTIONPROTOCOL_ADD_ACTION;
        readOnlyValue = false;
    }
%>

<%@ include file="/pages/content/common/CommonScripts.jsp" %>

<SCRIPT LANGUAGE="JavaScript">

	var win = null;
		function NewWindow(mypage,myname,w,h,scroll)
		{
			LeftPosition = (screen.width) ? (screen.width-w)/2 : 0;
			TopPosition = (screen.height) ? (screen.height-h)/2 : 0;

			settings =
				'height='+h+',width='+w+',top='+TopPosition+',left='+LeftPosition+',scrollbars='+scroll+',resizable'
			win = open(mypage,myname,settings)
			if (win.opener == null)
				win.opener = self;
		}
		
//code for units end
</SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
	var search1='`';
</script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<SCRIPT LANGUAGE="JavaScript">
<!--
	// functions for add more

	
//var subDivRowCount = new Array(10);		// array to hold the row count of the inner block

//subDivRowCount[0] = 1;

// variable to count the oter blocks
var insno=1;
var tblColor = "#123456";
function addBlock(div,d0)
{
var val = parseInt(document.forms[0].outerCounter.value);
		val = val + 1;
		document.forms[0].outerCounter.value = val;
		
		if(val%2 == 0)
			tblColor = "<%=Constants.EVEN_COLOR%>";
		else
			tblColor = "<%=Constants.ODD_COLOR%>";
//	alert("tblcolor : " + tblColor + " || val : "+ val  );	
	var y = div.innerHTML;
	var z = d0.innerHTML;
						//	insno =insno + 1;
	insno =val;
	var mm = z.indexOf('`');
	for(var cnt=0;cnt<mm;cnt++)
	{
		z = z.replace('`',insno);
		mm = z.indexOf('`');
	}
//	div.innerHTML = div.innerHTML +z;
	div.innerHTML = z + div.innerHTML ;
	
	var tb = document.getElementById("itable_"+val);
	tb.bgColor = tblColor;
}

function addDiv(div,adstr)
{
	
	var x = div.innerHTML;
	div.innerHTML = div.innerHTML +adstr;
}

//  function to insert a row in the inner block
function insRow(subdivtag,iCounter,blockCounter)
{
	var cnt = document.getElementById(iCounter);
	var val = parseInt(cnt.value);
	val = val + 1;
	cnt.value = val;

	var sname = "";
	
	var r = new Array(); 
	r = document.getElementById(subdivtag).rows;
	var q = r.length;
//	var x=document.getElementById(subdivtag).insertRow(q);
	var x=document.getElementById(subdivtag).insertRow(1);
	
	//setSubDivCount(subdivtag);
	var subdivname = ""+ subdivtag;

	// srno
	var spreqno=x.insertCell(0)
	spreqno.className="tabrightmostcellAddMore";
	var rowno=(q);
	var srIdentifier = subdivname + "_SpecimenRequirement:" + rowno + "_systemIdentifier)";
	var cell1 = "<input type='hidden' name='" + srIdentifier + "' value='' id='" + srIdentifier + "'>";
	spreqno.innerHTML="" + rowno+"." + cell1;
	
	//type
	var spreqtype=x.insertCell(1)
	spreqtype.className="formFieldAddMore";
	sname="";
	objname = subdivname + "_SpecimenRequirement:" + rowno + "_specimenClass)";
	
	var objunit = subdivname + "_SpecimenRequirement:"+rowno+"_unitspan)";
	var specimenClassName = objname;
	var objsubtype = subdivname + "_SpecimenRequirement:"+rowno+"_specimenType)";
	sname = "<select name='" + objname + "' size='1' onchange=changeUnit('" + objname + "','" + objunit +"','"+ objsubtype +"') class='formFieldSized10' id='" + objname + "'>";
	<%for(int i=0;i<specimenClassList.size();i++)
	{
		String specimenClassLabel = "" + ((NameValueBean)specimenClassList.get(i)).getName();
		String specimenClassValue = "" + ((NameValueBean)specimenClassList.get(i)).getValue();
	%>
		sname = sname + "<option value='<%=specimenClassValue%>'><%=specimenClassLabel%></option>";
	<%}%>
	sname = sname + "</select>";
	 
	spreqtype.innerHTML="" + sname;
	
	//subtype
	var spreqsubtype=x.insertCell(2)
	spreqsubtype.className="formFieldAddMore";
	sname="";
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_specimenType)";
	var functionName = "onSubTypeChangeUnit('" + specimenClassName + "',this,'" + objunit + "')" ;
	
	sname= "<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "' onChange=" + functionName + " >";
	
	sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";

	sname = sname + "</select>"
	
	spreqsubtype.innerHTML="" + sname;
	
	//tissuesite
	var spreqtissuesite=x.insertCell(3)
	spreqtissuesite.className="formFieldAddMore";
	sname="";
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_tissueSite)";
	
	sname = "<select name='" + objname + "' size='1' class='formFieldSized35' id='" + objname + "'>";
	<%for(int i=0;i<tissueSiteList.size();i++)
	{%>
		sname = sname + "<option value='<%=((NameValueBean)tissueSiteList.get(i)).getValue()%>'><%=((NameValueBean)tissueSiteList.get(i)).getName()%></option>";
	<%}%>
	sname = sname + "</select>"
	var url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName="+objname;			
	sname = sname + "<a href='#' onclick=javascript:NewWindow('" + url + "','name','250','330','no');return false>";
	sname = sname + "<img src='images\\Tree.gif' border='0' width='26' height='22'></a>";
	
	spreqtissuesite.innerHTML="" + sname;
	
	//pathologystatus
	var spreqpathologystatus=x.insertCell(4)
	spreqpathologystatus.className="formFieldAddMore";
	
	sname="";
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_pathologyStatus)";
	
	sname="<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "'>";
	<%for(int i=0;i<pathologyStatusList.size();i++)
	{%>
		sname = sname + "<option value='<%=((NameValueBean)pathologyStatusList.get(i)).getValue()%>'><%=((NameValueBean)pathologyStatusList.get(i)).getName()%></option>";
	<%}%>
	sname = sname + "</select>";
	
	spreqpathologystatus.innerHTML="" + sname;
	
	//qty
	var spreqqty=x.insertCell(5)
	spreqqty.className="formFieldAddMore";
	sname="";
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_quantityIn)";

	sname="<input type='text' name='" + objname + "' value='' class='formFieldSized5' id='" + objname + "'>"        	
	sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"
					
	spreqqty.innerHTML="" + sname;
	
	//Fourth Cell
	var checkb=x.insertCell(6);
	checkb.className="formFieldAddMore";
	checkb.colSpan=2;
	sname="";
	var name = "chk_spec_"+ blockCounter +"_"+rowno;
	sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C'>"
	checkb.innerHTML=""+sname;
	
}
/*

// function to set the row count in the array 
function setSubDivCount(subdivtag)
{
	var ind = subdivtag.indexOf('_');
	var x = subdivtag.substr(ind+1);
	var p = parseInt(x);
	subDivRowCount[p-1] = subDivRowCount[p-1]+1;
}

// function to get the row count of the inner block
function getSubDivCount(subdivtag)
{
	var ind = subdivtag.indexOf('_');
	var x = subdivtag.substr(ind+1);
	var p = parseInt(x);
	return subDivRowCount[p-1];
}
*/
//-->
</SCRIPT>

<style>
	div#d1
	{
	 display:none;
	}
	div#d999
	{
	 display:none;
	}
</style>
</head>
<body>


        
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="<%=formName%>">

<!-- table 1 -->
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
<!-- NEW COLLECTIONPROTOCOL ENTRY BEGINS-->
		<tr>
		<td colspan="3">
		<!-- table 4 -->
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td><html:hidden property="operation" value="<%=operation%>" /></td>
				</tr>
				
				<tr>
					<td><html:hidden property="systemIdentifier" />
					<html:hidden property="redirectTo" value="<%=reqPath%>"/></td>
				</tr>

					<tr>
						<td class="formMessage" colspan="4">* indicates a required field</td>
					</tr>
<!-- page title -->					
					<tr>
						<td class="formTitle" height="20" colspan="4">
							<logic:equal name="operation" value="<%=Constants.ADD%>">
								<bean:message key="collectionprotocol.title"/>
							</logic:equal>
							<logic:equal name="operation" value="<%=Constants.EDIT%>">
								<bean:message key="collectionprotocol.editTitle"/>&nbsp;<bean:message key="for.identifier"/>&nbsp;<bean:write name="collectionProtocolForm" property="systemIdentifier" />
							</logic:equal>
						</td>
					</tr>
					
<!-- principal investigator -->	
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="principalInvestigatorId">
								<bean:message key="collectionprotocol.principalinvestigator" />
							</label>
						</td>
						
						<td class="formField">
							<html:select property="principalInvestigatorId" styleClass="formFieldSized" styleId="principalInvestigatorId" size="1">
								<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
							<%
								String urlToGo = "/User.do?operation=add&pageOf=pageOfUserAdmin";
								String onClickPath = "changeUrl(this,'"+appendingPath+"')";
								System.out.println("CP URL TO GO  :-- " + urlToGo);
								System.out.println("onClickPath : " + onClickPath);
							%>
							<html:link page="<%=urlToGo%>" onclick="<%=onClickPath%>">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>
						</td>
					</tr>

<!-- protocol coordinators -->	
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="protocolCoordinatorIds">
								<bean:message key="collectionprotocol.protocolcoordinator" />
							</label>
						</td>
						
						<td class="formField">
							<html:select property="protocolCoordinatorIds" styleClass="formFieldSized" styleId="protocolCoordinatorIds" size="4" multiple="true">
								<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
							<html:link page="<%=urlToGo%>" onclick="<%=onClickPath%>">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>
						</td>
					</tr>

<!-- title -->						
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="title">
								<bean:message key="collectionprotocol.protocoltitle" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="title" property="title" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- short title -->						
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="shortTitle">
								<bean:message key="collectionprotocol.shorttitle" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="shortTitle" property="shortTitle" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					
<!-- irb id -->						
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="irbID">
								<bean:message key="collectionprotocol.irbid" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="irbID" property="irbID" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- startdate -->						
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="startDate">
								<bean:message key="collectionprotocol.startdate" />
							</label>
						</td>
			
						<td class="formField" colspan=2>
							<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 	<html:text styleClass="formDateSized15" size="15" styleId="startDate" property="startDate" />
						 	&nbsp;<bean:message key="page.dateFormat" />&nbsp;
							<a href="javascript:show_calendar('collectionProtocolForm.startDate',null,null,'MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0></a>
						</td>
					</tr>

<!-- enddate -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="endDate">
								<bean:message key="collectionprotocol.enddate" />
							</label>
						</td>
			
						 <td class="formField" colspan=2>
						 <div id="enddateDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 <html:text styleClass="formDateSized15" size="35" styleId="endDate" property="endDate" />
						 &nbsp;<bean:message key="page.dateFormat" />&nbsp;
							<a href="javascript:show_calendar('collectionProtocolForm.endDate',null,null,'MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0></a>
						 </td>
					</tr>

<!-- no of participants -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="enrollment">
								<bean:message key="collectionprotocol.participants" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="enrollment" property="enrollment" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- descriptionurl -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="descriptionURL">
								<bean:message key="collectionprotocol.descriptionurl" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="descriptionURL" property="descriptionURL" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- activitystatus -->	
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel" >
							<label for="activityStatus">
								<bean:message key="site.activityStatus" />
							</label>
						</td>
						<td class="formField">
							<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1">
								<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
							</html:select>
						</td>
					</tr>
					</logic:equal>
							
				</table> 	<!-- table 4 end -->
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr> <!-- SEPARATOR -->
</table>


<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
<tr><td>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
	<tr>
	<td class="formTitle">
			<b><bean:message key="collectionprotocol.eventtitle" /></b>
	</td>
	<td align="right" class="formTitle">		
			<html:button property="addCollectionProtocolEvents" styleClass="actionButton" onclick="addBlock(outerdiv,d1)">Add More</html:button>
			<html:hidden property="outerCounter"/>	
	</td>
	<td class="formTitle" align="Right">
		<html:button property="deleteCollectionProtocolEvents" styleClass="actionButton" onclick="deleteChecked('outerdiv','/catissuecore/CollectionProtocol.do?operation=<%=operation%>&pageOf=pageOfCollectionProtocol&status=true&button=deleteCollectionProtocolEvents',document.forms[0].outerCounter,'chk_proto_',true)">
			<bean:message key="buttons.delete"/>
		</html:button>
	</td>
	</tr>
</table>
</td></tr>
</table>


<!--  outermostdiv start --><!-- outer div tag  for entire block -->


<div id="outerdiv"> 
<%! Map map; %>
<%
		int maxCount=1;
		int maxIntCount=1;
				
		CollectionProtocolForm colForm = null;
		
		Object obj = request.getAttribute("collectionProtocolForm");
		//Map map = null;
		
		if(obj != null && obj instanceof CollectionProtocolForm)
		{
			colForm = (CollectionProtocolForm)obj;
			maxCount = colForm.getOuterCounter();
			map = colForm.getValues();
			
		}

		for(int counter=maxCount;counter>=1;counter--)
		{
			String commonLabel = "value(CollectionProtocolEvent:" + counter;
			String commonName = "CollectionProtocolEvent:" + counter;
			String cid = "ivl(" + counter + ")";
			String functionName = "insRow('" + commonLabel + "','" + cid +"'," + counter+ ")";
			String cpeIdentifier= commonLabel + "_systemIdentifier)";
			String check = "chk_proto_"+ counter;
			String tableId = "table_" + counter;
		
			if(colForm!=null)
			{
				Object o = colForm.getIvl(""+counter);
				if(o!=null)
					maxIntCount = Integer.parseInt(o.toString());
			}
		String tblColor = Constants.ODD_COLOR;
		if(counter%2 == 0)
			tblColor = Constants.EVEN_COLOR;
						
%>
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" id="<%=tableId%>">
<tr><td>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" bgColor="<%=tblColor%>"  id="i<%=tableId%>">
	<tr>
		<td rowspan=2 class="tabrightmostcellAddMore"><%=counter%></td>
		<td class="formFieldAddMore">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formRequiredNoticeAddMore" width="5">*
						<html:hidden property="<%=cpeIdentifier%>" />
					</td>
					<td class="formRequiredLabelAddMore" width="32%">
					<%
						String fldName = commonLabel + "_clinicalStatus)";
					%>
						<label for="<%=fldName%>">
							<bean:message key="collectionprotocol.clinicalstatus" />
						</label>
					</td>
					
					<td class="formFieldAddMore" colspan=2>
						<html:select property="<%=fldName%>" styleClass="formField" styleId="<%=fldName%>" size="1">
							<html:options collection="<%=Constants.CLINICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
					</td>
				</tr>
				
			    <tr>
					<td class="formRequiredNoticeAddMore" width="5">*</td>
					<%
						fldName="";
						fldName = commonLabel + "_studyCalendarEventPoint)";
						String keyStudyPoint = commonName + "_studyCalendarEventPoint";
						String valueStudyPoint = (String)colForm.getValue(keyStudyPoint);
						
					
						if(valueStudyPoint == null)
							valueStudyPoint = "1";
						
					%>

			        <td colspan="1" class="formRequiredLabelAddMore">
			        	<label for="<%=fldName%>">
							<bean:message key="collectionprotocol.studycalendartitle" />
						</label>
			        </td>
			        
			        <td colspan="2" class="formFieldAddMore">
			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="<%=fldName%>" 
			        			property="<%=fldName%>" 
			        			readonly="<%=readOnlyValue%>"
			        			value="<%=valueStudyPoint%>" /> 
			        	<bean:message key="collectionprotocol.studycalendarcomment"/>
					</td>
					<%
							String outerKey = "CollectionProtocolEvent:" + counter + "_systemIdentifier";
							boolean outerBool = Utility.isPersistedValue(map,outerKey);
							//boolean bool = false;
							String outerCondition = "";
							if(outerBool)
								outerCondition = "disabled='disabled'";

						%>
						
			    </tr>
			</TABLE>
		</td>
		<td rowspan=2 class="tabrightmostcellAddMore">
			<input type=checkbox name="<%=check%>" id="<%=check %>" <%=outerCondition%>>		
		</td>
	</tr>

	<!-- 2nd row -->
	<tr>
		<td class="formFieldAddMore">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width=100%>
			    <tr>
			        <td colspan="5" class="formTitle">
			        	<b><bean:message key="collectionprotocol.specimenreq"/></b>
			        </td>
			        <td class="formTitle">	
			     		<html:button property="addSpecimenReq" styleClass="actionButton" value="Add More" onclick="<%=functionName%>"/>
			     		
			     		<html:hidden styleId="<%=cid%>" property="<%=cid%>" value="<%=""+maxIntCount%>"/>
			        </td>
			        <td class="formTitle" align="Right">
			        		<% String temp = "deleteChecked('";
			        			temp = temp + commonLabel+"',";
			        			temp = temp + "'/catissuecore/CollectionProtocol.do?operation="+operation+"&pageOf=pageOfCollectionProtocol&status=true&button=deleteSpecimenReq&blockCounter="+counter+"',";
			        			temp = temp +"'"+ cid + "'" +",";
			        			temp = temp + "'chk_spec_"+ counter +"_',false)";
			        			
			        		%> 
							<html:button property="deleteSpecimenReq" styleClass="actionButton" onclick="<%=temp%>">
								<bean:message key="buttons.delete"/>
							</html:button>
					</td>
			    </tr>
			    
			    <TBODY id="<%=commonLabel%>">
			    <TR> <!-- SUB TITLES -->
			        <td class="formLeftSubTitle">
		        		<bean:message key="collectionprotocol.specimennumber" />
			        </td>
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimenclass" />
			        </td>
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimetype" />
			        </td>
			        
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimensite" />
				    </td>
			        <td  class=formLeftSubTitle>* 
			    		<bean:message key="collectionprotocol.specimenstatus" />
				    </td>
			        <td class=formLeftSubTitle>&nbsp;
			        	<bean:message key="collectionprotocol.quantity" />
			        </td>
			        <td class="formRightSubTableTitle">
						<label for="delete" align="center">
							<bean:message key="addMore.delete" />
						</label>
					</td>
			    </TR><!-- SUB TITLES END -->
				
				<%
					for(int innerCounter=maxIntCount;innerCounter>=1;innerCounter--)
					{
				%>
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcellAddMore"><%=innerCounter%>.</td>
			        <%
						String cName="";
						int iCnt = innerCounter;
						cName = commonLabel + "_SpecimenRequirement:" + iCnt ;
						String srCommonName = commonName + "_SpecimenRequirement:" + iCnt ;
						
						String fName = cName + "_specimenClass)";
						String srFname = srCommonName + "_specimenClass";
						String srSubTypeKeyName = srCommonName + "_specimenType";
						String sName = cName + "_unitspan)";
						String srIdentifier = cName + "_systemIdentifier)";
						String tmpSubTypeName = cName + "_specimenType)";
						
						String innerCheck = "chk_spec_" + counter + "_"+ iCnt;
						
					%>
			        
			        <td class="formFieldAddMore">
			        	<html:hidden property="<%=srIdentifier%>" />	
			        	<%
			        		String onChangeFun = "changeUnit('" + fName + "','" + sName + "','" + tmpSubTypeName + "')";
			        		String subTypeFunctionName ="onSubTypeChangeUnit('" + fName + "',this,'" + sName + "')"; 
			        		
			        	%>
			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized10" 
										styleId="<%=fName%>" size="1"
										onchange="<%=onChangeFun%>">
							<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
						<%
								String classValue = (String)colForm.getValue(srFname);
								specimenTypeList = (List)specimenTypeMap.get(classValue);
								
								boolean subListEnabled = false;
								if(classValue != null && classValue.equals("Cell"))
									subListEnabled = true;
								
								if(specimenTypeList == null)
								{
									specimenTypeList = new ArrayList();
									specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
								}
								pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
								fName="";
								 fName = cName + "_specimenType)";
						%>
			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized10" 
										styleId="<%=fName%>" size="1"
										   onchange="<%=subTypeFunctionName%>" 
										   disabled="<%=subListEnabled%>" >
							<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
						<%
								fName="";
								 fName = cName + "_tissueSite)";
						%>

			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized35" 
										styleId="<%=fName%>" size="1">
							<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        	<%
							String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName="+fName;			
						%>
				        <a href="#" onclick="javascript:NewWindow('<%=url%>','name','250','330','no');return false">
							<img src="images\Tree.gif" border="0" width="26" height="22">
						</a>
					</td>
					
			        <td class="formFieldAddMore">
						<%
								fName="";
								 fName = cName + "_pathologyStatus)";
						%>

			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized10" 
										styleId="<%=fName%>" size="1">
							<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
						<%
								fName="";
								 fName = cName + "_quantityIn)";
								 
								 String typeclassValue = (String)colForm.getValue(srSubTypeKeyName);
								 String strHiddenUnitValue = "" + changeUnit(classValue,typeclassValue);
								 
						%>

			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="<%=fName%>" 
			        			property="<%=fName%>" 
			        			readonly="<%=readOnlyValue%>" />
		        			
			        	<span id="<%=sName%>">
			        		<%=strHiddenUnitValue%>
						</span>
					</td>
					<%
							String innerKey = "CollectionProtocolEvent:"+counter+"_SpecimenRequirement:"+iCnt+"_systemIdentifier";
							boolean innerBool = Utility.isPersistedValue(map,innerKey);
							String innerCondition = "";
							if(innerBool)
								innerCondition = "disabled='disabled'";

						%>
						<td class="formFieldAddMore" width="5">
							<input type=checkbox name="<%=innerCheck%>" id="<%=innerCheck %>" <%=innerCondition%>>		
						</td>
				</TR>	<!-- SPECIMEN REQ DATA END -->
				<%
					} // inner for block
				%>
				</TBODY>
			</TABLE>
		</td>
	</tr>
</table> <!-- outer table for CPE ends -->
</td></tr>
</table>

<%
} // outer for
%>
</div>	<!-- outermostdiv  -->

<table width="100%">		
	<!-- to keep -->
		<tr>
			<td align="right" colspan="3">
				<!-- action buttons begins -->
				<!-- table 6 -->
				<table cellpadding="4" cellspacing="0" border="0">
					<tr>
						<td>
							<html:submit styleClass="actionButton">
								<bean:message  key="buttons.submit" />
							</html:submit>
							</td>
							<td>
							<html:reset styleClass="actionButton" >
								<bean:message  key="buttons.reset" />
							</html:reset>
						</td>
					</tr>
				</table>  
				<!-- table 6 end -->
				<!-- action buttons end -->
			</td>
		</tr>

	<!-- NEW COLLECTIONPROTOCOL ENTRY ends-->
</table>
</html:form>


<html:form action="DummyCollectionProtocol.do">
<div id="d1">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%" id="table_`">
<tr><td>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%" id="itable_`">
	<tr>
		<td rowspan=2 class="tabrightmostcellAddMore">`</td>
		<td class="formFieldAddMore">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formRequiredNoticeAddMore" width="5">*
						<html:hidden property="value(CollectionProtocolEvent:`_systemIdentifier)" />
					</td>
					<td class="formRequiredLabelAddMore" width="32%">
						<label for="value(CollectionProtocolEvent:`_clinicalStatus)">
							<bean:message key="collectionprotocol.clinicalstatus" />
						</label>
					</td>
					
					<td class="formFieldAddMore" colspan=2>
						<html:select property="value(CollectionProtocolEvent:`_clinicalStatus)" 
										styleClass="formField" styleId="value(CollectionProtocolEvent:`_clinicalStatus)" size="1">
							<html:options collection="<%=Constants.CLINICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
					</td>
				</tr>
				
			    <tr>
					<td class="formRequiredNoticeAddMore" width="5">*</td>
			        <td colspan="1" class="formRequiredLabelAddMore">
			        	<label for="value(CollectionProtocolEvent:`_studyCalendarEventPoint)">
							<bean:message key="collectionprotocol.studycalendartitle" />
						</label>
			        </td>
			        
			        <td colspan="2" class="formFieldAddMore">
			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="value(CollectionProtocolEvent:`_studyCalendarEventPoint)" 
			        			property="value(CollectionProtocolEvent:`_studyCalendarEventPoint)" 
			        			readonly="<%=readOnlyValue%>"
			        			value="1" /> 
			        	<bean:message key="collectionprotocol.studycalendarcomment"/>
					</td>
					
			    </tr>
			</TABLE>
		</td>
		
			<td rowspan=2 class="tabrightmostcellAddMore">
				<input type=checkbox name="chk_proto_`" id="chk_proto_`">		
			</td>
	</tr>

	<!-- 2nd row -->
	<tr>
		<td class="formFieldAddMore">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width=100%>
			    <tr>
			        <td colspan="5" class="formTitle">
			        	<b><bean:message key="collectionprotocol.specimenreq"/></b>
			        </td>
			        <td class="formTitle">	
			        <%
				        String hiddenCounter = "ivl(`)";
			        %>
			     		<html:button property="addSpecimenReq" styleClass="actionButton" value="Add More" onclick="insRow('value(CollectionProtocolEvent:`','ivl(`)','`')"/>
			     		
			     		<html:hidden styleId="<%=hiddenCounter%>" property="<%=hiddenCounter%>" value="1"/>
			        </td>
			        <td class="formTitle" align="Right">
							<html:button property="deleteSpecimenReq" styleClass="actionButton" onclick="deleteChecked('value(CollectionProtocolEvent:`','/catissuecore/CollectionProtocol.do?operation=<%=operation%>&pageOf=pageOfCollectionProtocol&status=true&button=deleteSpecimenReq&blockCounter=`','ivl(`)','chk_spec_`_',false)">
								<bean:message key="buttons.delete"/>
							</html:button>
					</td>
			    </tr>
			    
			    <TBODY id="value(CollectionProtocolEvent:`">
			    <TR> <!-- SUB TITLES -->
			        <td class="formLeftSubTitle">
		        		<bean:message key="collectionprotocol.specimennumber" />
			        </td>
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimenclass" />
			        </td>
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimetype" />
			        </td>
			        
			        <td class="formLeftSubTitle">* 
			        	<bean:message key="collectionprotocol.specimensite" />
				    </td>
			        <td  class=formLeftSubTitle>* 
			    		<bean:message key="collectionprotocol.specimenstatus" />
				    </td>
			        <td class=formLeftSubTitle>* 
			        	<bean:message key="collectionprotocol.quantity" />
			        </td>
			        <td class="formRightSubTableTitle">
						<label for="delete" align="center">
							<bean:message key="addMore.delete" />
						</label>
					</td>
			    </TR><!-- SUB TITLES END -->
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcellAddMore">1.
			        	<html:hidden property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_systemIdentifier)" />
			        </td>
			        <td class="formFieldAddMore">		
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)" 
										styleClass="formFieldSized10" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)" size="1"
										onchange="changeUnit('value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)',
			           						'value(CollectionProtocolEvent:`_SpecimenRequirement:1_unitspan)',
			           						'value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenType)')">
							<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenType)" 
										styleClass="formFieldSized10" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenType)" size="1"
										onchange="onSubTypeChangeUnit('value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)',
			           						this,'value(CollectionProtocolEvent:`_SpecimenRequirement:1_unitspan)')"
										>
							<html:option value="-1"><%=Constants.SELECT_OPTION%></html:option>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_tissueSite)" 
										styleClass="formFieldSized35" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_tissueSite)" size="1">
							<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			<!-- ****************************************  -->
				        <a href="#" onclick="javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=value(CollectionProtocolEvent:`_SpecimenRequirement:1_tissueSite)','name','250','330','no');return false">
							<img src="images\Tree.gif" border="0" width="26" height="22">
						</a>
				     <%--   <a href="#">
							<img src="images\Tree.gif" border="0" width="26" height="22"></a>   --%>
					</td>
					
			        <td class="formFieldAddMore">
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_pathologyStatus)" 
										styleClass="formFieldSized10" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_pathologyStatus)" size="1">
							<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formFieldAddMore">
			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_quantityIn)" 
			        			property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_quantityIn)" 
			        			readonly="<%=readOnlyValue%>" />
			        	<span id="value(CollectionProtocolEvent:`_SpecimenRequirement:1_unitspan)">
			        		&nbsp;
						</span>
					</td>
					
						<td class="formFieldAddMore" width="5">
							<input type=checkbox name="chk_spec_`_1" id="chk_spec_`_1">		
						</td>
					
				</TR>	<!-- SPECIMEN REQ DATA END -->
				</TBODY>
			</TABLE>
		</td>
	</tr>
</table> <!-- outer table for CPE ends -->
</td></tr>
</table>
</div>
</html:form>
</body>