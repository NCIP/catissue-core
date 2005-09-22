<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolForm"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>

<head>
<%!
	private String changeUnit(String specimenType,String subTypeValue)
	{
		if (specimenType == null)
			return "";
		if(specimenType.equals("Fluid"))
			return Constants.UNIT_ML;
		else if(specimenType.equals("Tissue"))
		{
			if(subTypeValue.equals("Slide") || subTypeValue.equals("Paraffin Block") || subTypeValue.equals("Frozen Block"))
				return " ";
			else	
				return Constants.UNIT_GM;
		}
		else if(specimenType.equals("Cell"))
			return Constants.UNIT_CC;
		else if(specimenType.equals("Molecular"))
			return Constants.UNIT_MG;
		else
			return " ";
			
	}
%>

<%
	List specimenClassList = (List) request.getAttribute(Constants.SPECIMEN_CLASS_LIST);

	List specimenTypeList = (List) request.getAttribute(Constants.SPECIMEN_TYPE_LIST);

	HashMap specimenTypeMap = (HashMap) request.getAttribute(Constants.SPECIMEN_TYPE_MAP);
	
	List tissueSiteList = (List) request.getAttribute(Constants.TISSUE_SITE_LIST);

	List pathologyStatusList = (List) request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST);
	
    String operation = (String) request.getAttribute(Constants.OPERATION);
    String formName;


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

<SCRIPT LANGUAGE="JavaScript">
	var ugul = new Array(4);
	ugul[0]="";
	ugul[1]="<%=Constants.UNIT_ML%>";
	ugul[2]="<%=Constants.UNIT_GM%>";
	ugul[3]="<%=Constants.UNIT_CC%>";
	ugul[4]="<%=Constants.UNIT_MG%>";

<%

	    	Iterator specimenTypeIterator = specimenTypeMap.keySet().iterator();
	    	int classCount=0;
	    	for(classCount=1;classCount<specimenClassList.size();classCount++  )
	    	{
	    		String keyObj = (String)((NameValueBean)specimenClassList.get(classCount)).getName() ;
	    		List subList = (List)specimenTypeMap.get(keyObj);
	    		String arrayData = "";
	    		for(int listSize=0;listSize<subList.size();listSize++ )
	    		{
	    			if(listSize == subList.size()-1 )
	    				arrayData = arrayData + "\"" + ((NameValueBean)subList.get(listSize)).getName() + "\"";
	    			else
		    			arrayData = arrayData + "\"" + ((NameValueBean)subList.get(listSize)).getName() + "\",";   
	    		}
%>
			var <%=keyObj%>Array = new Array(<%=arrayData%>);
<%	    		
	    	}

%>	

		function typeChange(element,arrayName)
		{ 
			var i = (element.name).lastIndexOf("_");
			var combo = (element.name).substring(0,i);
			var specimenTypeCombo = combo + "_specimenType)";
			ele = document.getElementById(specimenTypeCombo);
			//To Clear the Combo Box
			ele.options.length = 0;
			
			//ele.options[0] = new Option('-- Select --','-1');
			var j=0;
			//Populating the corresponding Combo Box
			for(i=0;i<arrayName.length;i++)
			{
					ele.options[j++] = new Option(arrayName[i],arrayName[i]);
			}
		}

	function onSubTypeChangeUnit(typeList,element,unitspan)
	{
		var classList = document.getElementById(typeList);
		var className = classList.options[classList.selectedIndex].text;
		var selectedOption = element.options[element.selectedIndex].text;
	
		if(className == "Tissue" && (selectedOption == "Slide" || selectedOption == "Paraffin Block" || selectedOption == "Frozen Block"))
		{
			document.getElementById(unitspan).innerHTML = ugul[0];
		}	
		else 
		{
			if(className == "Tissue")
			{
				document.getElementById(unitspan).innerHTML = ugul[2];
			}	
		}
			
	}

	function changeUnit(listname,unitspan)
	{
//		var i = document.getElementById(listname).selectedIndex;
		var list = document.getElementById(listname);
		var selectedOption = list.options[list.selectedIndex].text;

		if(selectedOption == "-- Select --")
			document.getElementById(unitspan).innerHTML = ugul[0];
		if(selectedOption == "Fluid")
		{
			document.getElementById(unitspan).innerHTML = ugul[1];
			typeChange(list,FluidArray);
		}
		if(selectedOption == "Tissue")
		{
			document.getElementById(unitspan).innerHTML = ugul[2];
			typeChange(list,TissueArray);
		}
		if(selectedOption == "Cell")
		{
			document.getElementById(unitspan).innerHTML = ugul[3];
			typeChange(list,CellArray);
		}
		if(selectedOption == "Molecular")
		{
			document.getElementById(unitspan).innerHTML = ugul[4];
			typeChange(list,MolecularArray);
		}
			
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
</script>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>

<SCRIPT LANGUAGE="JavaScript">
<!--
	// functions for add more

	
//var subDivRowCount = new Array(10);		// array to hold the row count of the inner block

//subDivRowCount[0] = 1;

// variable to count the oter blocks
var insno=1;

function addBlock(div,d0)
{
var val = parseInt(document.forms[0].outerCounter.value);
		val = val + 1;
		document.forms[0].outerCounter.value = val;
		
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
}

function addDiv(div,adstr)
{
	
	var x = div.innerHTML;
	div.innerHTML = div.innerHTML +adstr;
}

//  function to insert a row in the inner block
function insRow(subdivtag,iCounter)
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
	spreqno.className="tabrightmostcell";
	var rowno=(q);
	var srIdentifier = subdivname + "_SpecimenRequirement:" + rowno + "_systemIdentifier)";
	var cell1 = "<input type='hidden' name='" + srIdentifier + "' value='' id='" + srIdentifier + "'>";
	spreqno.innerHTML="" + rowno+"." + cell1;
	
	//type
	var spreqtype=x.insertCell(1)
	spreqtype.className="formField";
	sname="";
	objname = subdivname + "_SpecimenRequirement:" + rowno + "_specimenClass)";
	
	var objunit = subdivname + "_SpecimenRequirement:"+rowno+"_unitspan)";
	var specimenClassName = objname;
	
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
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_specimenType)";
	var functionName = "onSubTypeChangeUnit('" + specimenClassName + "',this,'" + objunit + "')" ;
	
	sname= "<select name='" + objname + "' size='1' class='formFieldSized10' id='" + objname + "' onChange=" + functionName + " >";
	
	sname = sname + "<option value='-1'><%=Constants.SELECT_OPTION%></option>";

	sname = sname + "</select>"
	
	spreqsubtype.innerHTML="" + sname;
	
	//tissuesite
	var spreqtissuesite=x.insertCell(3)
	spreqtissuesite.className="formField";
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
	spreqpathologystatus.className="formField";
	
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
	spreqqty.className="formField";
	sname="";
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_quantityIn)";

	sname="<input type='text' name='" + objname + "' value='' class='formFieldSized5' id='" + objname + "'>"        	
	sname = sname + "&nbsp;<span id='" + objunit + "'>&nbsp;</span>"
					
	spreqqty.innerHTML="" + sname;
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
<html:form action="<%=Constants.COLLECTIONPROTOCOL_ADD_ACTION%>">

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
					<td><html:hidden property="systemIdentifier" /></td>
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
								<bean:message key="collectionprotocol.editTitle"/>
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
							<html:link page="/User.do?operation=add&pageOf=pageOfUserAdmin">
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
							<html:link page="/User.do?operation=add&pageOf=pageOfUserAdmin">
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
						 	<html:text styleClass="formDateSized" size="35" styleId="startDate" property="startDate" readonly="true"/>
							<a href="javascript:show_calendar('collectionProtocolForm.startDate',null,null,'MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
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
						 <html:text styleClass="formDateSized" size="35" styleId="endDate" property="endDate" readonly="true"/>
							<a href="javascript:show_calendar('collectionProtocolForm.endDate',null,null,'MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
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
	</tr>
</table>
</td></tr>
</table>


<!--  outermostdiv start --><!-- outer div tag  for entire block -->
<div id="outerdiv"> 
<%
		int maxCount=1;
		int maxIntCount=1;
				
		CollectionProtocolForm colForm = null;
		
		Object obj = request.getAttribute("collectionProtocolForm");
		
		if(obj != null && obj instanceof CollectionProtocolForm)
		{
			colForm = (CollectionProtocolForm)obj;
			maxCount = colForm.getOuterCounter();
		}

		for(int counter=maxCount;counter>=1;counter--)
		{
			String commonLabel = "value(CollectionProtocolEvent:" + counter;
			String commonName = "CollectionProtocolEvent:" + counter;
			String cid = "ivl(" + counter + ")";
			String functionName = "insRow('" + commonLabel + "','" + cid +"')";
			String cpeIdentifier= commonLabel + "_systemIdentifier)";
		
			if(colForm!=null)
			{
				Object o = colForm.getIvl(""+counter);
				if(o!=null)
					maxIntCount = Integer.parseInt(o.toString());
			}

						
%>
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
<tr><td>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
	<tr>
		<td rowspan=2 class="tabrightmostcell"><%=counter%></td>
		<td class="formField">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formRequiredNotice" width="5">*
						<html:hidden property="<%=cpeIdentifier%>" />
					</td>
					<td class="formRequiredLabel" width="32%">
					<%
						String fldName = commonLabel + "_clinicalStatus)";
					%>
						<label for="<%=fldName%>">
							<bean:message key="collectionprotocol.clinicalstatus" />
						</label>
					</td>
					
					<td class="formField" colspan=2>
						<html:select property="<%=fldName%>" styleClass="formField" styleId="<%=fldName%>" size="1">
							<html:options collection="<%=Constants.CLINICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
					</td>
				</tr>
				
			    <tr>
					<td class="formRequiredNotice" width="5">*</td>
					<%
						fldName="";
						fldName = commonLabel + "_studyCalendarEventPoint)";
						String keyStudyPoint = commonName + "_studyCalendarEventPoint";
						String valueStudyPoint = (String)colForm.getValue(keyStudyPoint);
					
						if(valueStudyPoint == null)
							valueStudyPoint = "1";
						
					%>

			        <td colspan="1" class="formRequiredLabel">
			        	<label for="<%=fldName%>">
							<bean:message key="collectionprotocol.studycalendartitle" />
						</label>
			        </td>
			        
			        <td colspan="2" class="formField">
			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="<%=fldName%>" 
			        			property="<%=fldName%>" 
			        			readonly="<%=readOnlyValue%>"
			        			value="<%=valueStudyPoint%>" /> 
			        	<bean:message key="collectionprotocol.studycalendarcomment"/>
					</td>
			    </tr>
			</TABLE>
		</td>
	</tr>

	<!-- 2nd row -->
	<tr>
		<td class="formField">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width=100%>
			    <tr>
			        <td colspan="5" class="formTitle">
			        	<b>SPECIMEN REQUIREMENTS</b>
			        </td>
			        <td class="formTitle">	
			     		<html:button property="addSpecimenReq" styleClass="actionButton" value="Add More" onclick="<%=functionName%>"/>
			     		
			     		<html:hidden styleId="<%=cid%>" property="<%=cid%>" value="<%=""+maxIntCount%>"/>
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
			    </TR><!-- SUB TITLES END -->
				
				<%
					for(int innerCounter=maxIntCount;innerCounter>=1;innerCounter--)
					{
				%>
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcell"><%=innerCounter%>.</td>
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
					%>
			        
			        <td class="formField">
			        	<html:hidden property="<%=srIdentifier%>" />	
			        	<%
			        		String onChangeFun = "changeUnit('" + fName + "','" + sName + "')";
			        		String subTypeFunctionName ="onSubTypeChangeUnit('" + fName + "',this,'" + sName + "')"; 
			        		
			        	%>
			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized10" 
										styleId="<%=fName%>" size="1"
										onchange="<%=onChangeFun%>">
							<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formField">
						<%
								String classValue = (String)colForm.getValue(srFname);
								specimenTypeList = (List)specimenTypeMap.get(classValue);
								
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
										   onchange="<%=subTypeFunctionName%>" >
							<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formField">
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
					
			        <td class="formField">
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
			        
			        <td class="formField">
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

	<!-- NEW COLLECTIONPROTOCOL ENTRY ends-->
</table>
</html:form>


<html:form action="DummyCollectionProtocol.do">
<div id="d1">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
<tr><td>
<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
	<tr>
		<td rowspan=2 class="tabrightmostcell">`</td>
		<td class="formField">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formRequiredNotice" width="5">*
						<html:hidden property="value(CollectionProtocolEvent:`_systemIdentifier)" />
					</td>
					<td class="formRequiredLabel" width="32%">
						<label for="value(CollectionProtocolEvent:`_clinicalStatus)">
							<bean:message key="collectionprotocol.clinicalstatus" />
						</label>
					</td>
					
					<td class="formField" colspan=2>
						<html:select property="value(CollectionProtocolEvent:`_clinicalStatus)" 
										styleClass="formField" styleId="value(CollectionProtocolEvent:`_clinicalStatus)" size="1">
							<html:options collection="<%=Constants.CLINICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
					</td>
				</tr>
				
			    <tr>
					<td class="formRequiredNotice" width="5">*</td>
			        <td colspan="1" class="formRequiredLabel">
			        	<label for="value(CollectionProtocolEvent:`_studyCalendarEventPoint)">
							<bean:message key="collectionprotocol.studycalendartitle" />
						</label>
			        </td>
			        
			        <td colspan="2" class="formField">
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
	</tr>

	<!-- 2nd row -->
	<tr>
		<td class="formField">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width=100%>
			    <tr>
			        <td colspan="5" class="formTitle">
			        	<b>SPECIMEN REQUIREMENTS</b>
			        </td>
			        <td class="formTitle">	
			        <%
				        String hiddenCounter = "ivl(`)";
			        %>
			     		<html:button property="addSpecimenReq" styleClass="actionButton" value="Add More" onclick="insRow('value(CollectionProtocolEvent:`','ivl(`)')"/>
			     		
			     		<html:hidden styleId="<%=hiddenCounter%>" property="<%=hiddenCounter%>" value="1"/>
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
			    </TR><!-- SUB TITLES END -->
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcell">1.
			        	<html:hidden property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_systemIdentifier)" />
			        </td>
			        <td class="formField">		
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)" 
										styleClass="formFieldSized10" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)" size="1"
										onchange="changeUnit('value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)',
			           						'value(CollectionProtocolEvent:`_SpecimenRequirement:1_unitspan)')">
							<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formField">
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenType)" 
										styleClass="formFieldSized10" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenType)" size="1"
										onchange="onSubTypeChangeUnit('value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenClass)',
			           						this,'value(CollectionProtocolEvent:`_SpecimenRequirement:1_unitspan)')"
										>
							<html:option value="-1"><%=Constants.SELECT_OPTION%></html:option>
						</html:select>
			        </td>
			        
			        <td class="formField">
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
					
			        <td class="formField">
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_pathologyStatus)" 
										styleClass="formFieldSized10" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_pathologyStatus)" size="1">
							<html:options collection="<%=Constants.PATHOLOGICAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formField">
			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_quantityIn)" 
			        			property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_quantityIn)" 
			        			readonly="<%=readOnlyValue%>" />
			        	<span id="value(CollectionProtocolEvent:`_SpecimenRequirement:1_unitspan)">
			        		&nbsp;
						</span>
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