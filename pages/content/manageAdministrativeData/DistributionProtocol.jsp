<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.DistributionProtocolForm"%>
<%@ page import="java.util.List"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>

<head>

<%
	List specimenClassList = (List) request.getAttribute(Constants.SPECIMEN_CLASS_LIST);
	
	List specimenTypeList = (List) request.getAttribute(Constants.SPECIMEN_TYPE_LIST);
	
	List tissueSiteList = (List) request.getAttribute(Constants.TISSUE_SITE_LIST);

	List pathologyStatusList = (List) request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST);
	
    String operation = (String) request.getAttribute(Constants.OPERATION);
    String formName;

    boolean readOnlyValue;
    if (operation.equals(Constants.EDIT))
    {
        formName = Constants.DISTRIBUTIONPROTOCOL_EDIT_ACTION;
        readOnlyValue = false;
    }
    else
    {
        formName = Constants.DISTRIBUTIONPROTOCOL_ADD_ACTION;
        readOnlyValue = false;
    }
    
		Object obj = request.getAttribute("distributionProtocolForm");
		int noOfRows=1;

		if(obj != null && obj instanceof DistributionProtocolForm)
		{
			DistributionProtocolForm form = (DistributionProtocolForm)obj;
			noOfRows = form.getCounter();
		}

%>

<SCRIPT LANGUAGE="JavaScript">
	var ugul = new Array(4);
	ugul[0]=" ";
	ugul[1]="<%=Constants.UNIT_ML%>";
	ugul[2]="<%=Constants.UNIT_GM%>";
	ugul[3]="<%=Constants.UNIT_CC%>";
	ugul[4]="<%=Constants.UNIT_MG%>";

	function changeUnit(listname,unitspan)
	{
//		var i = document.getElementById(listname).selectedIndex;
		var list = document.getElementById(listname);
		var selectedOption = list.options[list.selectedIndex].text;

		if(selectedOption == "-- Select --")
			document.getElementById(unitspan).innerHTML = ugul[0];
		if(selectedOption == "Fluid")
			document.getElementById(unitspan).innerHTML = ugul[1];
		if(selectedOption == "Tissue")
			document.getElementById(unitspan).innerHTML = ugul[2];
		if(selectedOption == "Cell")
			document.getElementById(unitspan).innerHTML = ugul[3];
		if(selectedOption == "Molecular")
			document.getElementById(unitspan).innerHTML = ugul[4];
	}
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
	var insno=0;
</script>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<SCRIPT LANGUAGE="JavaScript">
<!--
	// functions for add more


//  function to insert a row in the inner block
function insRow(subdivtag)
{
		var val = parseInt(document.forms[0].counter.value);
		val = val + 1;
		document.forms[0].counter.value = val;
	var sname = "";

	var r = new Array(); 
	r = document.getElementById(subdivtag).rows;
	var q = r.length;
//	var x=document.getElementById(subdivtag).insertRow(q);
	var x=document.getElementById(subdivtag).insertRow(1);
	
	var subdivname = ""+ subdivtag;

	// srno
	var spreqno=x.insertCell(0)
	spreqno.className="tabrightmostcell";
	var rowno=(q);
	var identifier = "value(SpecimenRequirement:" + rowno +"_systemIdentifier)";
	var cell1 = "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
	spreqno.innerHTML="" + rowno+"." + cell1;
	
	//type
	var spreqtype=x.insertCell(1)
	spreqtype.className="formField";
	sname="";
	objname = "value(SpecimenRequirement:" + rowno + "_specimenClass)";
//value(SpecimenRequirement:`_quantityIn)	
	
	var objunit = "value(SpecimenRequirement:"+rowno+"_unitspan)";
	
	sname = "<select name='" + objname + "' size='1' onchange=changeUnit('" + objname + "','" + objunit +"') class='formFieldSized10' id='" + objname + "'>";
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
	spreqsubtype.className="formField";
	sname="";
	objname = "value(SpecimenRequirement:"+rowno+"_specimenType)";
	
	sname= "<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "'>";
	<%for(int i=0;i<specimenTypeList.size();i++)
	{
		String specimenTypeLabel = "" + ((NameValueBean)specimenTypeList.get(i)).getName();
		String specimenTypeValue = "" + ((NameValueBean)specimenTypeList.get(i)).getValue();		
	%>
		sname = sname + "<option value='<%=specimenTypeValue%>'><%=specimenTypeLabel%></option>";
	<%}%>
	sname = sname + "</select>"
	
	spreqsubtype.innerHTML="" + sname;
	
	//tissuesite
	var spreqtissuesite=x.insertCell(3)
	spreqtissuesite.className="formField";
	sname="";
	objname = "value(SpecimenRequirement:"+rowno+"_tissueSite)";
	
	sname = "<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "'>";
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
	spreqpathologystatus.className="formField";
	
	sname="";
	objname = "value(SpecimenRequirement:"+rowno+"_pathologyStatus)";
	
	sname="<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "'>";
	<%for(int i=0;i<pathologyStatusList.size();i++)
	{%>
		sname = sname + "<option value='<%=((NameValueBean)pathologyStatusList.get(i)).getValue()%>'><%=((NameValueBean)pathologyStatusList.get(i)).getName()%></option>";
	<%}%>
	sname = sname + "</select>";
	
	spreqpathologystatus.innerHTML="" + sname;
	
	//qty
	var spreqqty=x.insertCell(5)
	spreqqty.className="formField";
	sname="";
	objname = "value(SpecimenRequirement:"+rowno+"_quantityIn)";

	sname="<input type='text' name='" + objname + "' value='' class='formFieldSized5' id='" + objname + "'>"        	
	sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"
					
	spreqqty.innerHTML="" + sname;
}

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
<html:form action="<%=Constants.DISTRIBUTIONPROTOCOL_ADD_ACTION%>">

<!-- table 1 -->
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
<!-- NEW DISTRIBUTIONPROTOCOL ENTRY BEGINS-->
		<tr>
		<td colspan="3">
		<!-- table 4 -->
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="96%">
				<tr>
					<td><html:hidden property="operation" value="<%=operation%>" /></td>
				</tr>
				
				<tr>
					<td><html:hidden property="systemIdentifier" /></td>
				</tr>
				<logic:notEqual name="operation" value="<%=Constants.SEARCH%>">
					<tr>
						<td class="formMessage" colspan="4">* indicates a required field</td>
					</tr>
<!-- page title -->					
					<tr>
						<td class="formTitle" height="20" colspan="4">
							<bean:message key="distributionprotocol.title" />
						</td>
					</tr>
					
<!-- principal investigator -->	
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="principalInvestigatorId">
								<bean:message key="distributionprotocol.principalinvestigator" />
							</label>
						</td>
						
						<td class="formField">
							<html:select property="principalInvestigatorId" styleClass="formFieldSized" styleId="principalInvestigatorId" size="1">
								<html:options collection="<%=Constants.USERLIST%>" labelProperty="name" property="value"/>
							</html:select>
							&nbsp;
							<html:link page="/User.do?operation=add&pageOf=">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>
						</td>
					</tr>

<!-- title -->						
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="title">
								<bean:message key="distributionprotocol.protocoltitle" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="title" property="title" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- short title -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="shortTitle">
								<bean:message key="distributionprotocol.shorttitle" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="shortTitle" property="shortTitle" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
					
<!-- irb id -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="irbID">
								<bean:message key="distributionprotocol.irbid" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="irbID" property="irbID" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- startdate -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="startDate">
								<bean:message key="distributionprotocol.startdate" />
							</label>
						</td>
			
						<td class="formField" colspan=2>
							<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 	<html:text styleClass="formDateSized" size="35" styleId="startDate" property="startDate" readonly="true"/>
							<a href="javascript:show_calendar('distributionProtocolForm.startDate',null,null,'MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
						</td>
					</tr>

<!-- enddate -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="endDate">
								<bean:message key="distributionprotocol.enddate" />
							</label>
						</td>
			
						 <td class="formField" colspan=2>
						 <div id="enddateDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 <html:text styleClass="formDateSized" size="35" styleId="endDate" property="endDate" readonly="true"/>
							<a href="javascript:show_calendar('distributionProtocolForm.endDate',null,null,'MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
						 </td>
					</tr>

<!-- no of participants -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="enrollment">
								<bean:message key="distributionprotocol.participants" />
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
								<bean:message key="distributionprotocol.descriptionurl" />
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


<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="97%">
	<tr><td>
		<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
			<tr>
				<td class="formTitle">
						<b>SPECIMEN REQUIREMENTS</b>
						<html:hidden property="counter"/>
				</td>
				<td align="right" class="formTitle">		
						<html:button property="addDistributionProtocolEvents" styleClass="actionButton" onclick="insRow('SpecimenRequirementData')">Add More</html:button>
				</td> 
			</tr>
		</table>	
			<!-- SUB TITLES -->
	<tr><td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
			<tbody id="SpecimenRequirementData">			
			<TR> 
				<td class="formLeftSubTitle" ><!-- width="14" -->
					<bean:message key="distributionprotocol.specimennumber" />
		        </td>
				
				<td class="formLeftSubTitle">* <!--  width="182" -->
					<bean:message key="distributionprotocol.specimenclass" />
		        </td>
		        
		        <td class="formLeftSubTitle">* <!-- width="180" -->
			        <bean:message key="distributionprotocol.specimentype" />
		        </td>
		        
		        <td class="formLeftSubTitle">* <!--  width="211" -->
		        	<bean:message key="distributionprotocol.specimensite" />
			    </td>
		        
		        <td class="formLeftSubTitle">* <!--  width="208" -->
			        <bean:message key="distributionprotocol.pathologystatus" />
			    </td>
			    
			    <td class="formLeftSubTitle">* <!--  width="117" -->
			    	<b><bean:message key="distributionprotocol.quantity" /></b>
		        </td>
			</tr>	

			<%
				int maxcount=1;
				for(int counter=noOfRows;counter>=1;counter--)
				{		
					String objname = "value(SpecimenRequirement:" + counter + "_specimenClass)";
					String objunit = "value(SpecimenRequirement:"+ counter +"_unitspan)";
					String identifier = "value(SpecimenRequirement:"+ counter +"_systemIdentifier)";
			%>
			<TR> 
				<td class="tabrightmostcell">
					<%=counter%>
					<html:hidden property="<%=identifier%>" />				
		        </td>
			<% String functionName ="changeUnit('" + objname + "',' " + objunit + "')"; %>	
				<td class="formField">
					<html:select property="<%=objname%>" styleClass="formFieldSized10" styleId="<%=objname%>" size="1" onchange="<%=functionName%>" >
						<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
					</html:select>
		        </td>
		        
		        <td class="formField">
					<%
						objname="";
						objname = "value(SpecimenRequirement:" + counter + "_specimenType)";
					%>
					<html:select property="<%=objname%>" styleClass="formFieldSized10" styleId="<%=objname%>" size="1" >
						<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
					</html:select>
		        </td>
		        
		        <td class="formField">
					<%
						objname="";
						objname = "value(SpecimenRequirement:" + counter + "_tissueSite)";
					%>
					<html:select property="<%=objname%>" styleClass="formFieldSized10" styleId="<%=objname%>" size="1" >
						<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
					</html:select>
					<%
						String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName="+objname;			
					%>
				    <a href="#" onclick="javascript:NewWindow('<%=url%>','name','250','330','no');return false">
						<img src="images\Tree.gif" border="0" width="26" height="22">
					</a>
			    </td>
		        
		        <td class="formField">
					<%
						objname="";
						objname = "value(SpecimenRequirement:" + counter + "_pathologyStatus)";
					%>
					<html:select property="<%=objname%>" styleClass="formFieldSized10" styleId="<%=objname%>" size="1" >
						<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
					</html:select>
			    </td>
			    
			    <td class="formField">
			    	<%
						objname="";
						objname = "value(SpecimenRequirement:"+ counter +"_quantityIn)";
					%>
			    
					<html:text styleClass="formFieldSized5" styleId="<%=objname%>" property="<%=objname%>" readonly="<%=readOnlyValue%>" />
						&nbsp;<span id=' <%= objunit %>'>&nbsp;</span>
		        </td>
			</tr>	
							
			<%
				} // for 
			%>
			
			</tbody>
			<!-- SUB TITLES END -->		 
		</table>
	</td></tr>
</table>


<table width="100%">		
	<!-- to keep -->
		<tr>
			<td align="right" colspan="3">
				<%
					String changeAction = "setFormAction('" + formName + "');";
		        %> 
				
				<!-- action buttons begins -->
				<!-- table 6 -->
				<table cellpadding="4" cellspacing="0" border="0" >
					<tr>
						<td>
							<html:submit styleClass="actionButton" value="Submit" onclick="<%=changeAction%>" />
						</td>
						<td>
							<html:reset styleClass="actionButton" />
						</td>
					</tr>
				</table>  <!-- table 6 end -->
				<!-- action buttons end -->
			</td>
		</tr>
	</logic:notEqual>

</table>
</html:form>
</body>