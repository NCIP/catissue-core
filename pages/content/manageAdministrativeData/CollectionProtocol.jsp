<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolForm"%>
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
    String searchFormName = new String(Constants.COLLECTIONPROTOCOL_SEARCH_ACTION);

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

	function changeUnit(listname,unitspan)
	{
		var i = document.getElementById(listname).selectedIndex;
		document.getElementById(unitspan).innerHTML = ugul[i];
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

	//subDivRowCount[insno] = 1;
	insno =insno + 1;
	//alert("insno "+z);
	var mm = z.indexOf('`');
	for(var cnt=0;cnt<mm;cnt++)
	{
		z = z.replace('`',insno);
		mm = z.indexOf('`');
	}
	div.innerHTML = div.innerHTML +z;
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
	var x=document.getElementById(subdivtag).insertRow(q);
	
	//setSubDivCount(subdivtag);
	var subdivname = ""+ subdivtag;

	// srno
	var spreqno=x.insertCell(0)
	spreqno.className="tabrightmostcell";
	var rowno=(q);
	spreqno.innerHTML="" + rowno+".";
	
	//type
	var spreqtype=x.insertCell(1)
	spreqtype.className="formField";
	sname="";
	objname = subdivname + "_SpecimenRequirement:" + rowno + "_specimenClass)";
	
	var objunit = subdivname + "_SpecimenRequirement:"+rowno+"_unitspan)";
	
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
	objname = subdivname + "_SpecimenRequirement:"+rowno+"_tissueSite)";
	
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
	<logic:notEqual name="operation" value="<%=Constants.ADD%>">
		<!-- ENTER IDENTIFIER BEGINS-->
		<br />
		<tr>
			<td>
			<!-- table 2 -->
				<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="collectionprotocol.searchTitle" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="identifier">
								<bean:message key="collectionprotocol.identifier" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="identifier" property="identifier" />
						</td>
					</tr>
					<%
        				String changeAction = "setFormAction('" + searchFormName
                							  + "');setOperation('" + Constants.SEARCH + "');";
			        %>
					<tr>
						<td align="right" colspan="3">
						<!-- table 3 -->
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
									<html:submit styleClass="actionButton" value="Search" onclick="<%=changeAction%>" />
								</td>
							</tr>
						</table>  <!-- table 3 end -->
						</td>
					</tr>

				</table>  <!-- table 2 end -->
				</td>
			</tr>
			<!-- ENTER IDENTIFIER ENDS-->
		</logic:notEqual>


		<!-- NEW COLLECTIONPROTOCOL ENTRY BEGINS-->
		<tr>
		<td colspan="3">
		<!-- table 4 -->
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td><html:hidden property="operation" value="<%=operation%>" /></td>
				</tr>

				<logic:notEqual name="operation" value="<%=Constants.SEARCH%>">
					<tr>
						<td class="formMessage" colspan="4">* indicates a required field</td>
					</tr>
<!-- page title -->					
					<tr>
						<td class="formTitle" height="20" colspan="4">
							<bean:message key="collectionprotocol.title" />
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
							<html:link page="/User.do?operation=add&pageOf=">
		 						<bean:message key="buttons.addNew" />
	 						</html:link>
						</td>
					</tr>

<!-- protocol coordinators -->	
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="protocolCoordinatorIds">
								<bean:message key="collectionprotocol.protocolcoordinator" />
							</label>
						</td>
						
						<td class="formField">
							<html:select property="protocolCoordinatorIds" styleClass="formFieldSized" styleId="protocolCoordinatorIds" size="4" multiple="true">
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
								<bean:message key="collectionprotocol.protocoltitle" />
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
								<bean:message key="collectionprotocol.shorttitle" />
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
								<bean:message key="collectionprotocol.irbid" />
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
					<%
						if(formName == Constants.COLLECTIONPROTOCOL_EDIT_ACTION)
						{
					%>
							<tr>
								<td class="formRequiredNotice" width="5">&nbsp;</td>
								<td class="formLabel">
									<label for="activityStatus">
										<bean:message key="collectionprotocol.activitystatus" />
									</label>
								</td>
								<td class="formField" colspan=2>
										<html:select property="activityStatus" styleClass="formFieldSized" styleId="activityStatus" size="1">
								        	<html:option value="Type1">Activity Status</html:option>
										</html:select>
								</td>
							</tr>
					<%
						}
					%>
							
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

		for(int counter=1;counter<=maxCount;counter++)
		{
			String commonLabel = "value(CollectionProtocolEvent:" + counter;
			
			String cid = "ivl(" + counter + ")";
			String functionName = "insRow('" + commonLabel + "','" + cid +"')";
		
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
					<td class="formRequiredNotice" width="5">*</td>
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
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<%
						fldName="";
						fldName = commonLabel + "_studyCalendarEventPoint)";
					%>

			        <td colspan="1" class="formLabel">
			        	<label for="<%=fldName%>">
							<bean:message key="collectionprotocol.studycalendartitle" />
						</label>
			        </td>
			        
			        <td colspan="2" class="formField">
			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="<%=fldName%>" 
			        			property="<%=fldName%>" 
			        			readonly="<%=readOnlyValue%>" /> 
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
			        <td class=formLeftSubTitle>* 
			        	<bean:message key="collectionprotocol.quantity" />
			        </td>
			    </TR><!-- SUB TITLES END -->
				
				<%
					for(int innerCounter=1;innerCounter<=maxIntCount;innerCounter++)
					{
				%>
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcell"><%=innerCounter%>.</td>
			        <%
						String cName="";
						int iCnt = innerCounter;
						cName = commonLabel + "_SpecimenRequirement:" + iCnt ;
						String fName = cName + "_specimenClass)";
						String sName = cName + "_unitspan)";
					%>
			        
			        <td class="formField">		
			        <%
			        	String onChangeFun = "changeUnit('" + fName + "','" + sName + "')";
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
								fName="";
								 fName = cName + "_specimenType)";
						%>
			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized10" 
										styleId="<%=fName%>" size="1">
							<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formField">
						<%
								fName="";
								 fName = cName + "_tissueSite)";
						%>

			        	<html:select property="<%=fName%>" 
										styleClass="formFieldSized10" 
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
						%>

			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="<%=fName%>" 
			        			property="<%=fName%>" 
			        			readonly="<%=readOnlyValue%>" />
			        	<span id="<%=sName%>">
			        		&nbsp;
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
	</logic:notEqual>

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
					<td class="formRequiredNotice" width="5">*</td>
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
					<td class="formRequiredNotice" width="5">&nbsp;</td>
			        <td colspan="1" class="formLabel">
			        	<label for="value(CollectionProtocolEvent:`_studyCalendarEventPoint)">
							<bean:message key="collectionprotocol.studycalendartitle" />
						</label>
			        </td>
			        
			        <td colspan="2" class="formField">
			        	<html:text styleClass="formFieldSized5" size="30" 
			        			styleId="value(CollectionProtocolEvent:`_studyCalendarEventPoint)" 
			        			property="value(CollectionProtocolEvent:`_studyCalendarEventPoint)" 
			        			readonly="<%=readOnlyValue%>" /> 
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
			        <td class="formLeftSubTableTitle">
		        		<bean:message key="collectionprotocol.specimennumber" />
			        </td>
			        <td class="formLeftSubTableTitle">* 
			        	<bean:message key="collectionprotocol.specimenclass" />
			        </td>
			        <td class="formLeftSubTableTitle">* 
			        	<bean:message key="collectionprotocol.specimetype" />
			        </td>
			        
			        <td class="formLeftSubTableTitle">* 
			        	<bean:message key="collectionprotocol.specimensite" />
				    </td>
			        <td  class=formLeftSubTableTitle>* 
			    		<bean:message key="collectionprotocol.specimenstatus" />
				    </td>
			        <td class=formLeftSubTableTitle>* 
			        	<bean:message key="collectionprotocol.quantity" />
			        </td>
			    </TR><!-- SUB TITLES END -->
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcell">1.</td>
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
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_specimenType)" size="1">
							<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
			        </td>
			        
			        <td class="formField">
			        	<html:select property="value(CollectionProtocolEvent:`_SpecimenRequirement:1_tissueSite)" 
										styleClass="formFieldSized10" 
										styleId="value(CollectionProtocolEvent:`_SpecimenRequirement:1_tissueSite)" size="1">
							<html:options collection="<%=Constants.TISSUE_SITE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
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