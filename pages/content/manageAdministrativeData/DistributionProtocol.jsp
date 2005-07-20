<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<%
    String operation = (String) request.getAttribute(Constants.OPERATION);
    String formName;
    String searchFormName = new String(Constants.DISTRIBUTIONPROTOCOL_SEARCH_ACTION);

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
%>
        
<html:errors />
<html:form action="<%=Constants.DISTRIBUTIONPROTOCOL_ADD_ACTION%>">

<!-- table 1 -->
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	<logic:notEqual name="operation" value="<%=Constants.ADD%>">
		<!-- ENTER IDENTIFIER BEGINS-->
		<br />
		<tr>
			<td>
			<!-- table 2 -->
				<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="distributionprotocol.searchTitle" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="identifier">
								<bean:message key="distributionprotocol.identifier" />
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


		<!-- NEW DISTRIBUTIONPROTOCOL ENTRY BEGINS-->
		<tr>
		<td colspan="3">
		<!-- table 4 -->
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td><html:hidden property="operation" value="<%=operation%>" /></td>
				</tr>

				<logic:notEqual name="operation" value="<%=Constants.SEARCH%>">
					<tr>
						<td class="formMessage" colspan="3">* indicates a required field</td>
					</tr>
<!-- page title -->					
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<bean:message key="distributionprotocol.title" />
						</td>
					</tr>
					
<!-- principal investigator -->	
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="principalinvestigator">
								<bean:message key="distributionprotocol.principalinvestigator" />
							</label>
						</td>
						<td class="formField">
							<html:select property="principalinvestigator" styleClass="formFieldSized" styleId="principalinvestigator" size="1">
							 	<html:option value="Rakesh">Rakesh</html:option>
								<html:option value="Mark">Mark</html:option>
								<html:option value="Kapil">Kapil</html:option>
								<html:option value="Srikant">Srikant</html:option>
								<html:option value="Mandar">Mandar</html:option>
							</html:select>
							<html:link page="User.do?operation=add">
							 <bean:message key="distributionprotocol.addinvestigator" />
							 </html:link>
						</td>
					</tr>
					
<!-- protocol coordinators -->	
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="protocolcoordinator">
								<bean:message key="distributionprotocol.protocolcoordinator" />
							</label>
						</td>
						<td class="formField">
							<html:select property="protocolcoordinator" styleClass="formFieldSized" styleId="protocolcoordinator" size="4" multiple="true">
							 	<html:option value="Rakesh">Rakesh</html:option>
								<html:option value="Mark">Mark</html:option>
								<html:option value="Kapil">Kapil</html:option>
								<html:option value="Srikant">Srikant</html:option>
								<html:option value="Mandar">Mandar</html:option>
							</html:select>
							<html:link page="User.do?operation=add">
							 <bean:message key="distributionprotocol.addcoordinator" />
							 </html:link>
						</td>
					</tr>

<!-- title -->						
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="protocoltitle">
								<bean:message key="distributionprotocol.protocoltitle" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="title" property="title" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- short title -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="shorttitle">
								<bean:message key="distributionprotocol.shorttitle" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="shorttitle" property="shorttitle" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- irb id -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="irbid">
								<bean:message key="distributionprotocol.irbid" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="irbid" property="irbid" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- startdate -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="startdate">
								<bean:message key="distributionprotocol.startdate" />
							</label>
						</td>
			
						 <td class="formField">
						 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 <html:text styleClass="formDateSized" size="35" styleId="startDate" property="startDate" readonly="true"/>
							<a href="javascript:show_calendar('distributionprotocolForm.startDate');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
						 </td>
					</tr>

<!-- enddate -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="enddate">
								<bean:message key="distributionprotocol.enddate" />
							</label>
						</td>
			
						 <td class="formField">
						 <div id="overDiv1" style="position:absolute; visibility:hidden; z-index:1000;"></div>
						 <html:text styleClass="formDateSized" size="35" styleId="endDate" property="endDate" readonly="true"/>
							<a href="javascript:show_calendar('distributionprotocolForm.endDate');">
								<img src="images\calendar.gif" width=24 height=22 border=0>
							</a>
						 </td>
					</tr>

<!-- no of participants -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="participants">
								<bean:message key="distributionprotocol.participants" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="participants" property="participants" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>

<!-- descriptionurl -->						
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="descriptionurl">
								<bean:message key="distributionprotocol.descriptionurl" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" size="30" styleId="descriptionurl" property="descriptionurl" readonly="<%=readOnlyValue%>" />
						</td>
					</tr>
<!-- activitystatus -->						
				<%
				if (formName == Constants.DISTRIBUTIONPROTOCOL_EDIT_ACTION)
				{
				%>
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formRequiredLabel">
							<label for="activityStatus">
								<bean:message key="distributionprotocol.activitystatus" />
							</label>
						</td>
						<td class="formField">
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

<!-- to insert the div tag -->
<!-- specimen requirement -->
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="650">
<tr><td>
			<table summary="" cellpadding="3" cellspacing="0" border="0" width=100% >
			    <tr>
			        <td colspan="6" class="formTitle">
			        	<b><bean:message key="distributionprotocol.specimenreq" /></b>
					</TD>
					<TD class="formTitle">	
			     	   <html:button property="addSpecimenReq" styleClass="actionButton">Add More</html:button>
			        </td>
			    </tr>
			    
			    <TR> <!-- SUB TITLES -->
			        <td  class="tabrightmostcell">
		        		<bean:message key="distributionprotocol.specimennumber" />
			        </td>
			        <td class="formRequiredLabel">
			        	<bean:message key="distributionprotocol.specimentype" />
			        </td>
			        <td class="formRequiredLabel">
			        	<bean:message key="distributionprotocol.specimensite" />
				    </td>
			        <td class="formRequiredLabel">
				        <bean:message key="distributionprotocol.specimenside" />
				    </td>
			        <td  class="formRequiredLabel">
			    		<bean:message key="distributionprotocol.specimenstatus" />
				    </td>
			        <td class="formRequiredLabel">
			        	<bean:message key="distributionprotocol.quantity" />
			        </td>
			        <td  class="formRequiredLabel">
			        	<bean:message key="distributionprotocol.unit" />
			        </td>
			    </TR><!-- SUB TITLES END -->
				
				<TR>	<!-- SPECIMEN REQ DATA -->
			        <td  class="tabrightmostcell">1.</td>
			        <td class="formField">
			           	<html:select property="specimenType" styleClass="formFieldSized10" styleId="specimenType" size="1">
				        	<html:option value="0">Select Specimen Type</html:option>
							<html:option value="Type1">Fluid Specimen</html:option>
							<html:option value="Type2">Tissue Specimen</html:option>
							<html:option value="Cell Specimen">Cell Specimen</html:option>
							<html:option value="Molecular Specimen">Molecular Specimen</html:option>
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
			        <td class="formField">ug, ul</td>
				</TR>	<!-- SPECIMEN REQ DATA END -->
			</TABLE>
</table>
<table width=83%>		
	<!-- to keep -->
		<tr>
			<td align="right" colspan="3">
				<%
					String changeAction = "setFormAction('" + formName + "');";
		        %> 
				
				<!-- action buttons begins -->
				<!-- table 6 -->
				<table cellpadding="4" cellspacing="0" border="0">
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

	<!-- NEW DISTRIBUTIONPROTOCOL ENTRY ends-->
</table>
</html:form>