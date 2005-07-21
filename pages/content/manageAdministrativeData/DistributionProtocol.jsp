<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<head>
<SCRIPT LANGUAGE="JavaScript">
	var search1='`';
	var search2='~';
	var insno=0;
	var insno1=1;

	var ugul = new Array(4);
	ugul[0]="ML";
	ugul[1]="GM";
	ugul[2]="CC";
	ugul[3]="MG";

	function changeUnit(listname)
	{
		var i = listname.selectedIndex;
		unitspan.innerHTML =ugul[i];  
	}
//-->
</SCRIPT>
<script type="text/javascript" language="javascript" src="../../../javaScript.js">
</script>
<style>
	div#d1
	{
	 display:none;
	}
	div#d1_1
	{
	 display:none;
	}
</style>
</head>


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
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="875">
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
			<table summary="" cellpadding="3" cellspacing="0" border="0" width="96%">
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
							 	<html:option value="Rakesh">Rakesh</html:option>
								<html:option value="Mark">Mark</html:option>
								<html:option value="Kapil">Kapil</html:option>
								<html:option value="Srikant">Srikant</html:option>
								<html:option value="Mandar">Mandar</html:option>
							</html:select>
							<html:link page="User.do?operation=add">
							 <bean:message key="collectionprotocol.addinvestigator" />
							 </html:link>
						</td>
					</tr>
					
<!-- protocol coordinators -->	
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="protocolcoordinator">
								<bean:message key="collectionprotocol.protocolcoordinator" />
							</label>
						</td>
						<td class="formField" colspan=2>
							<html:select property="protocolcoordinator" styleClass="formFieldSized" styleId="protocolcoordinator" size="4" multiple="true">
							 	<html:option value="Rakesh">Rakesh</html:option>
								<html:option value="Mark">Mark</html:option>
								<html:option value="Kapil">Kapil</html:option>
								<html:option value="Srikant">Srikant</html:option>
								<html:option value="Mandar">Mandar</html:option>
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
						<td class="formRequiredLabel">
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
						<td class="formRequiredLabel">
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
						<td class="formRequiredLabel">
							<label for="startdate">
								<bean:message key="collectionprotocol.startdate" />
							</label>
						</td>
			
						 <td class="formField" colspan=2>
						 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 <html:text styleClass="formDateSized" size="35" styleId="startDate" property="startDate" readonly="true"/>
							<a href="javascript:show_calendar('collectionProtocolForm.startDate','','','MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
						 </td>
					</tr>

<!-- enddate -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="enddate">
								<bean:message key="collectionprotocol.enddate" />
							</label>
						</td>
			
						 <td class="formField" colspan=2>
						 <div id="overDiv1" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 <html:text styleClass="formDateSized" size="35" styleId="endDate" property="endDate" readonly="true"/>
							<a href="javascript:show_calendar('collectionProtocolForm.endDate','','','MM-DD-YYYY');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
						 </td>
					</tr>

<!-- no of participants -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
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
						<td class="formRequiredLabel">
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
								<td class="formRequiredLabel">
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

<!-- to insert the div tag -->
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="96%">
	<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width=100%>
			    <tr>
			        <td colspan="7" class="formTitle">
			        	<b><bean:message key="collectionprotocol.specimenreq" /></b>
			        </td>
			        <td class="formTitle">	
			     	   <html:button property="addSpecimenReq" styleClass="actionButton">Add More</html:button>
			        </td>
			    </tr>
			    
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
			        <td class=formLeftSubTableTitle>
			        	<bean:message key="collectionprotocol.unit" />
			        </td>
			    </TR><!-- SUB TITLES END -->
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td class="tabrightmostcell">1.</td>
			        <td class="formField">
			           	<html:select property="specimenType" styleClass="formFieldSized10" styleId="specimenType" size="1" onchange="changeUnit(specimenType)">
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
			        </td>
			        <td class="formField">
			        	<span id="unitspan">&nbsp;
						</span>
					</td>
				</TR>	<!-- SPECIMEN REQ DATA END -->
			</TABLE>
		</td>
	</tr>
</table> <!-- outer table for CPE ends -->



<table width="96%">		
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