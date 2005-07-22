<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>

<SCRIPT LANGUAGE="JavaScript">
	var ugul = new Array(4);
	ugul[0]="(ml)";
	ugul[1]="(gm)";
	ugul[2]="(cc)";
	ugul[3]="(mg)";

	function changeUnit(listname,unitspan)
	{
		var i = listname.selectedIndex;
		alert(ugul[i]);
		unitspan.innerHTML = ugul[i];  
	}
//-->
</SCRIPT>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	var search1='`';
	var insno=1;

/* section for inner block start */
var str = "";
var search2='`';
var insno1=1
function setStr()
{
		str="";
		str = str + "	<TR>"	
		str = str + "         <td class='tabrightmostcell'>" + insno1  + "</td>"
		str = str + "         <td class='formField'>"
		str = str + "            	<select name='specimenType' size='1' onchange='changeUnit(specimenType)' class='formFieldSized10' id='specimenType'><option value='0'>Fluid Specimen</option>"
		str = str + " 				<option value='1'>Tissue Specimen</option>"
		str = str + " 				<option value='2'>Cell Specimen</option>"
		str = str + " 				<option value='3'>Molecular Specimen</option></select>"
		str = str + "         </td>"
		str = str + "         <td class='formField'>"
		str = str + "            	<select name='specimenSubType' size='1' class='formFieldSized10' id='specimenSubType'><option value='0'>Select Specimen SubType</option>"
		str = str + " 				<option value='Type1'>Blood</option>"
		str = str + " 				<option value='Type2'>Cerum</option></select>"
		str = str + "         </td>"
		str = str + "         <td class='formField'>"
		str = str + "            	<select name='tissueSite' size='1' class='formFieldSized10' id='tissueSite'><option value='Select Tissue Site'>Select Tissue Site</option>"
		str = str + " 				<option value='Adrenal-Cortex'>Adrenal-Cortex</option>"
		str = str + " 				<option value='Adrenal-Medulla'>Adrenal-Medulla</option>"
		str = str + " 				<option value='Adrenal-NOS'>Adrenal-NOS</option></select>"
		str = str + "         <a href='#'>"
		str = str + " 		<img src='images/Tree.gif' border='0' width='26' height='22'></a>"
		str = str + " 		</td>"
		str = str + "         <td class='formField'>"
		str = str + "           	<select name='tissueSide' size='1' class='formFieldSized10' id='tissueSide'><option value='Select Tissue Side'>Select Tissue Side</option>"
		str = str + " 				<option value='Bilateral sites'>Bilateral sites</option>"
		str = str + " 				<option value='Left'>Left</option>"
		str = str + " 				<option value='Right'>Right</option></select>"
		str = str + "         </td>"
		str = str + "         <td class='formField'>"
		str = str + "           	<select name='tissueType' size='1' class='formFieldSized10' id='tissueType'><option value='Select Tissue Type'>Select Tissue Type</option>"
		str = str + " 				<option value='Primary Tumor'>Primary Tumor</option>"
		str = str + " 				<option value='Metastatic Node'>Metastatic Node</option>"
		str = str + " 				<option value='Non-Malignant Tissue'>Non-Malignant Tissue</option></select>"
		str = str + "         </td>"
		str = str + "         <td class='formField'>"
		str = str + "         	<input type='text' name='enrollment' value='' class='formFieldSized5' id='enrollment'>        "
		str = str + "         	<span id='unitspan'>&nbsp;"
		str = str + " 			</span>"
		str = str + " 		</td>"
		str = str + " 	</TR>	"
}
function b21(subdiv,searchChar)
{
//	alert(subdiv);

	newdiv = document.getElementById(subdiv);
	insno1=insno1+1;
	setStr();
	y = str;
	newdiv = document.getElementById(subdiv).innerHTML+str;
} // b21



/* section for inner block end */



function addDiv(div,adstr)
{
	var x = div.innerHTML;
	div.innerHTML = div.innerHTML +adstr;
}


</script>

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

<%
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
        
<html:errors />
<html:form action="<%=Constants.COLLECTIONPROTOCOL_ADD_ACTION%>">

<!-- table 1 -->
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="810">
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
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="97%">
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
							<label for="principalinvestigator">
								<bean:message key="collectionprotocol.principalinvestigator" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:select property="principalinvestigator" styleClass="formFieldSized" styleId="principalinvestigator" size="1">
							 	<html:option value="Rakesh">Nagarajan, Rakesh</html:option>
								<html:option value="Mark">Watson, Mark</html:option>
								<html:option value="Kapil">Kaveeshwar, Kapil</html:option>
								<html:option value="Srikant">Adiga, Srikant</html:option>
								<html:option value="Mandar">Deshmukh, Mandar</html:option>
							</html:select>
							<html:link page="User.do?operation=add">
							 <bean:message key="collectionprotocol.addinvestigator" />
							 </html:link>
						</td>
					</tr>

<!-- protocol coordinators -->	
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="protocolcoordinator">
								<bean:message key="collectionprotocol.protocolcoordinator" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:select property="protocolcoordinator" styleClass="formFieldSized" styleId="protocolcoordinator" size="4" multiple="true">
							 	<html:option value="Rakesh">Nagarajan, Rakesh</html:option>
								<html:option value="Mark">Watson, Mark</html:option>
								<html:option value="Kapil">Kaveeshwar, Kapil</html:option>
								<html:option value="Srikant">Adiga, Srikant</html:option>
								<html:option value="Mandar">Deshmukh, Mandar</html:option>
							</html:select>
							<html:link page="User.do?operation=add">
							 <bean:message key="collectionprotocol.addcoordinator" />
							 </html:link>
						</td>
					</tr>

<!-- title -->						
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="protocoltitle">
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
							<label for="shorttitle">
								<bean:message key="collectionprotocol.shorttitle" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="shorttitle" property="shorttitle" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- irb id -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="irbid">
								<bean:message key="collectionprotocol.irbid" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="irbid" property="irbid" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- startdate -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="startdate">
								<bean:message key="collectionprotocol.startdate" />
							</label>
						</td>
			
						 <td class="formField" colspan=2>
						 <div id="startdateDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 <html:text styleClass="formDateSized" size="35" styleId="startDate" property="startDate" readonly="true"/>
							<a href="javascript:show_calendar('collectionProtocolForm.startDate','','','MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
						 </td>
					</tr>

<!-- enddate -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="enddate">
								<bean:message key="collectionprotocol.enddate" />
							</label>
						</td>
			
						 <td class="formField" colspan=2>
						 <div id="enddateDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 <html:text styleClass="formDateSized" size="35" styleId="endDate" property="endDate" readonly="true"/>
							<a href="javascript:show_calendar('collectionProtocolForm.endDate','','','MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
						 </td>
					</tr>

<!-- no of participants -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="participants">
								<bean:message key="collectionprotocol.participants" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="participants" property="participants" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- descriptionurl -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="descriptionurl">
								<bean:message key="collectionprotocol.descriptionurl" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:text styleClass="formFieldSized" size="30" styleId="descriptionurl" property="descriptionurl" readonly="<%=readOnlyValue%>" />
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

<!--  outer table for CPE -->
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="810">
<tr><td>
<table summary="" cellpadding="3" cellspacing="0" border="0" width=97%>
	<tr>
	<td class="formTitle">
			<b><bean:message key="collectionprotocol.eventtitle" /></b>
	</td>
	<td align="right" class="formTitle">		
			<html:button property="addCollectionProtocolEvents" styleClass="actionButton" onclick="replaceSpeChar(outerdiv,d1,search1)">Add More</html:button>
	</td>
	</tr>
</table>
</td></tr>
</table>

<!--  outermostdiv start --><!-- outer div tag  for entire block -->
<div id="outerdiv"> 
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
<tr><td>
<table summary="" cellpadding="3" cellspacing="0" border="0" >
	<tr>
		<td rowspan=2 class="tabrightmostcell">1</td>
		<td class="formField">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel" width="32%">
						<label for="clinicalstatus">
					    	<bean:message key="collectionprotocol.clinicalstatus" />
						</label>
					</td>
				    <td class="formField" colspan=2>
				    	<html:select property="clinicalStatus" styleClass="formField" styleId="clinicalStatus" size="1">
				        	<html:option value="Type1">Pre-Opt</html:option>
							<html:option value="Type1">Pre-Opt</html:option>
							<html:option value="Type2">Post-Opt</html:option>
						</html:select>
				    </td>
				</tr>
			    <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
			        <td colspan="1" class="formLabel">
			        	<bean:message key="collectionprotocol.studycalendartitle" />
			        </td>
			        <td colspan="2" class="formField">
			        	<html:text styleClass="formFieldSized5" size="30" styleId="studycalendartitle" property="studycalendartitle" readonly="<%=readOnlyValue%>" />
			        	<bean:message key="collectionprotocol.studycalendarcomment" />
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
			        <td colspan="6" class="formTitle">
			        	<b><bean:message key="collectionprotocol.specimenreq" /></b>
			        </td>
			        <td class="formTitle">	
			     	   <html:button property="addSpecimenReq" styleClass="actionButton">Add More</html:button>
			        </td>
			    </tr>
			    <div id="subdiv1">
			    <TR> <!-- SUB TITLES -->
			        <td class="formLeftSubTableTitle">
		        		<bean:message key="collectionprotocol.specimennumber" />
			        </td>
			        <td class="formLeftSubTableTitle">
			        	<bean:message key="collectionprotocol.specimentype" />
			        </td>
			        <td class="formLeftSubTableTitle">
			        	<bean:message key="collectionprotocol.specimensubtype" />
			        </td>
			        
			        <td class="formLeftSubTableTitle">
			        	<bean:message key="collectionprotocol.specimensite" />
				    </td>
			        <td class=formLeftSubTableTitle>
				        <bean:message key="collectionprotocol.specimenside" />
				    </td>
			        <td  class=formLeftSubTableTitle>
			    		<bean:message key="collectionprotocol.specimenstatus" />
				    </td>
			        <td class=formLeftSubTableTitle>
			        	<bean:message key="collectionprotocol.quantity" />
			        </td>

			    </TR><!-- SUB TITLES END -->
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcell">1.</td>
			        <td class="formField">
			           	<html:select property="specimenType" styleClass="formFieldSized10" styleId="specimenType" size="1" onchange="changeUnit(specimenType,unitspan)">
							<html:option value="0">Fluid Specimen</html:option>
							<html:option value="1">Tissue Specimen</html:option>
							<html:option value="2">Cell Specimen</html:option>
							<html:option value="3">Molecular Specimen</html:option>
						</html:select>
			        </td>
			        <td class="formField">
			           	<html:select property="specimenSubType" styleClass="formFieldSized10" styleId="specimenSubType" size="1">
				        	<html:option value="0">Select Specimen SubType</html:option>
							<html:option value="Type1">Blood</html:option>
							<html:option value="Type2">Cerum</html:option>
						</html:select>
			        </td>
			        <td class="formField">
			           	<html:select property="tissueSite" styleClass="formFieldSized10" styleId="tissueSite" size="1">
				        	<html:option value="Select Tissue Site">Select Tissue Site</html:option>
							<html:option value="Adrenal-Cortex">Adrenal-Cortex</html:option>
							<html:option value="Adrenal-Medulla">Adrenal-Medulla</html:option>
							<html:option value="Adrenal-NOS">Adrenal-NOS</html:option>
						</html:select>
			        <a href="#">
					<img src="images/Tree.gif" border="0" width="26" height="22"></a>
					</td>
			        <td class="formField">
			          	<html:select property="tissueSide" styleClass="formFieldSized10" styleId="tissueSide" size="1">
				        	<html:option value="Select Tissue Side">Select Tissue Side</html:option>
							<html:option value="Bilateral sites">Bilateral sites</html:option>
							<html:option value="Left">Left</html:option>
							<html:option value="Right">Right</html:option>
						</html:select>
			        </td>
			        <td class="formField">
			          	<html:select property="tissueType" styleClass="formFieldSized10" styleId="tissueType" size="1">
				        	<html:option value="Select Tissue Type">Select Tissue Type</html:option>
							<html:option value="Primary Tumor">Primary Tumor</html:option>
							<html:option value="Metastatic Node">Metastatic Node</html:option>
							<html:option value="Non-Malignant Tissue">Non-Malignant Tissue</html:option>
						</html:select>
			        </td>
			        <td class="formField">
			        	<html:text styleClass="formFieldSized5" styleId="enrollment" property="enrollment" readonly="<%=readOnlyValue%>" />        
					       &nbsp;
			          	<span id="unitspan">&nbsp;
						</span>
					</td>
				</TR>	<!-- SPECIMEN REQ DATA END -->
				</div>
				
			</TABLE>
		</td>
	</tr>
</table> <!-- outer table for CPE ends -->
</td></tr>
</table>

</div>	<!-- outermostdiv  -->


<table width="95%">		
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

<div id="d1">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="100%">
<tr><td>
<table summary="" cellpadding="3" cellspacing="0" border="0" >
	<tr>
		<td rowspan=2 class="tabrightmostcell">`</td>
		<td class="formField">
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel" width="32%">
						<label for="clinicalstatus">
					    	Clinical Status_`
						</label>
					</td>
				    <td class="formField" colspan=2>
				    	<select name="clinicalStatus" size="1" class="formField" id="clinicalStatus"><option value="Type1">Pre-Opt</option>
							<option value="Type1">Pre-Opt</option>
							<option value="Type2">Post-Opt</option></select>
				    </td>
				</tr>
			    <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
			        <td colspan="1" class="formLabel">
			        	Study Calendar Event Point
			        </td>
			        <td colspan="2" class="formField">
			        	<input type="text" name="studycalendartitle" size="30" value="" class="formFieldSized5" id="studycalendartitle">
			        	Days
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
			        <td colspan="6" class="formTitle">
			        	<b>SPECIMEN REQUIREMENTS</b>
			        </td>
			        <td class="formTitle">	
			     	   <input type="button" name="addSpecimenReq" value="Add More" class="actionButton">
			        </td>
			    </tr>
			    <div id="subdiv`">
			    <TR> <!-- SUB TITLES -->
			        <td class="formLeftSubTableTitle">
		        		#
			        </td>
			        <td class="formLeftSubTableTitle">
			        	Type
			        </td>
			        <td class="formLeftSubTableTitle">
			        	SubType
			        </td>
			        
			        <td class="formLeftSubTableTitle">
			        	Tissue Site
				    </td>
			        <td class=formLeftSubTableTitle>
				        Tissue Side
				    </td>
			        <td  class=formLeftSubTableTitle>
			    		Pathology Status
				    </td>
			        <td class=formLeftSubTableTitle>
			        	Quantity(Unit)
			        </td>
<!--			        <td class=formLeftSubTableTitle>
			        	Unit
			        </td>
-->
			    </TR><!-- SUB TITLES END -->
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcell">1.</td>
			        <td class="formField">
			           	<select name="specimenType_`" size="1" onchange="changeUnit(specimenType_`,unitspan_`)" class="formFieldSized10" id="specimenType"><option value="0">Fluid Specimen</option>
							<option value="1">Tissue Specimen</option>
							<option value="2">Cell Specimen</option>
							<option value="3">Molecular Specimen</option></select>
			        </td>
			        <td class="formField">
			           	<select name="specimenSubType" size="1" class="formFieldSized10" id="specimenSubType"><option value="0">Select Specimen SubType</option>
							<option value="Type1">Blood</option>
							<option value="Type2">Cerum</option></select>
			        </td>
			        <td class="formField">
			           	<select name="tissueSite" size="1" class="formFieldSized10" id="tissueSite"><option value="Select Tissue Site">Select Tissue Site</option>
							<option value="Adrenal-Cortex">Adrenal-Cortex</option>
							<option value="Adrenal-Medulla">Adrenal-Medulla</option>
							<option value="Adrenal-NOS">Adrenal-NOS</option></select>
			        <a href="#">
					<img src="images/Tree.gif" border="0" width="26" height="22"></a>
					</td>
			        <td class="formField">
			          	<select name="tissueSide" size="1" class="formFieldSized10" id="tissueSide"><option value="Select Tissue Side">Select Tissue Side</option>
							<option value="Bilateral sites">Bilateral sites</option>
							<option value="Left">Left</option>
							<option value="Right">Right</option></select>
			        </td>
			        <td class="formField">
			          	<select name="tissueType" size="1" class="formFieldSized10" id="tissueType"><option value="Select Tissue Type">Select Tissue Type</option>
							<option value="Primary Tumor">Primary Tumor</option>
							<option value="Metastatic Node">Metastatic Node</option>
							<option value="Non-Malignant Tissue">Non-Malignant Tissue</option></select>
			        </td>
			        <td class="formField">
			        	<input type="text" name="enrollment" value="" class="formFieldSized5" id="enrollment">        
			        	<span id="unitspan_`">&nbsp;
						</span>
					</td>
				</TR>	<!-- SPECIMEN REQ DATA END -->
				</div>
			</TABLE>
		</td>
	</tr>
</table> <!-- outer table for CPE ends -->
</td></tr>
</table>
</div>





</body>




