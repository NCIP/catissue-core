<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<% 
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;

		if(operation.equals(Constants.EDIT))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.ACCESSION_EDIT_ACTION;
			readOnlyValue=true;
		}
		else
		{
			formName = Constants.ACCESSION_ADD_ACTION;
			readOnlyValue=false;
		}

		if (operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);
%>

	<html:errors />

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
			
		   <html:form action="<%=Constants.ACCESSION_ADD_ACTION%>">	
		   
		   		   <logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
		   	<tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
		   			<tr>
				  	<td align="right" colspan="3">
					<%
						String changeAction = "setFormAction('MakeAccessionEditable.do?"+Constants.EDITABLE+"="+!readOnlyForAll+"')";
				 	%>
					<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
						   	<td>
						   		<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   			<bean:message key="<%=editViewButton%>"/>
						   		</html:submit>
						   	</td>
							<td>
								<html:reset styleClass="actionButton">
									<bean:message key="buttons.export"/>
								</html:reset>
							</td>
						</tr>
					</table>
					<!-- action buttons end -->
				  </td>
				  </tr>
				</table>
			   </td>
			</tr>
			</logic:equal>		 
		   		
		   
		   <!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
			<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
				<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
			<!-- ENTER IDENTIFIER BEGINS-->	
			  <br/>	
  	    	  <tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
				  <tr>
				     <td class="formTitle" height="20" colspan="3">
				     	<bean:message key="user.searchTitle"/>
				     </td>
				  </tr>	
				  
				  <tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="identifier">
								<bean:message key="user.identifier"/>
							</label>
						</td>
					    <td class="formField">
					    	<html:text styleClass="formFieldSized" size="30" styleId="identifier" property="identifier" readonly="<%=readOnlyForAll%>"/>
					    </td>
				  </tr>	
				  <%
					String changeAction = "setFormAction('"+Constants.ACCESSION_SEARCH_ACTION+"');setOperation('"+Constants.SEARCH+"');";
				  %>
				  <tr>
				   <td align="right" colspan="3">
					 <table cellpadding="4" cellspacing="0" border="0">
						 <tr>
						    <td>
						    	<html:submit styleClass="actionButton" value="Search" onclick="<%=changeAction%>"/>
						    </td>
						 </tr>
					 </table>
				   </td>
				  </tr>

				 </table>
			    </td>
			  </tr>
			  <!-- ENTER IDENTIFIER ENDS-->	
			  	</logic:notEqual>	
			  </logic:notEqual> 
			  
			  <!-- NEW ACCESSION REGISTRATION BEGINS-->
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
				 <tr>
					<td>
						<html:hidden property="operation"  value="<%=operation%>"/>
					</td>
				 </tr>
				 
				 <logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
				 	<tr>
						<td>
							<html:hidden property="identifier"/>
						</td>
				 	</tr>
				 </logic:equal>
				 
				 <logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
					<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">				 
				 		<tr>
				     		<td class="formMessage" colspan="3">* indicates a required field</td>
				 		</tr>
				 	</logic:notEqual>
				 <tr>
				     <td class="formTitle" height="20" colspan="3">
				     	<%String title = "accession."+pageView+".title";%>
				     	<bean:message key="<%=title%>"/>
				     </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
				     	<label for="participant">
				     		<bean:message key="accession.participant"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:select property="participant" styleClass="formFieldSized" styleId="participant" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="participantIdList" labelName="participantList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
				     	<label for="protocol">
				     		<bean:message key="accession.protocol"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:select property="protocol" styleClass="formFieldSized" styleId="protocol" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="protocolList" labelName="protocolList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel">
				     	<label for="protocolNumber">
				     		<bean:message key="accession.protocolNumber"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="protocolNumber" property="protocolNumber" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				  <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel">
				     	<label for="medicalRecordNumber">
				     		<bean:message key="accession.medicalRecordNumber"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="medicalRecordNumber" property="medicalRecordNumber" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel">
				     	<label for="medicalReportURL">
				     		<bean:message key="accession.medicalReportURL"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="medicalReportURL" property="medicalReportURL" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel">
				     	<label for="clinicalDiagnosis">
				     		<bean:message key="accession.clinicalDiagnosis"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="clinicalDiagnosis" property="clinicalDiagnosis" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel"><label for="diseaseStatus"><bean:message key="accession.diseaseStatus"/></label></td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="diseaseStatus" property="diseaseStatus" readonly="<%=readOnlyForAll%>"/></td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel"><label for="surgicalPathologyNumber"><bean:message key="accession.surgicalPathologyNumber"/></label></td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="surgicalPathologyNumber" property="surgicalPathologyNumber" readonly="<%=readOnlyForAll%>"/></td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel"><label for="surgicalPathologyReportURL"><bean:message key="accession.surgicalPathologyReportURL"/></label></td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="surgicalPathologyReportURL" property="surgicalPathologyReportURL" readonly="<%=readOnlyForAll%>"/></td>
				 </tr>
				 <tr>
				     <td class="formTitle" height="20" colspan="3"><bean:message key="accession.collectionInformation"/></td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="state"><bean:message key="accession.collectionDate"/></label></td>
					 
					 <td class="formField">
					 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
					 <html:text styleClass="formDateSized" size="15" styleId="collectionDate" property="collectionDate" readonly="true"/>
						<a href="javascript:show_calendar('accessionForm.collectionDate');">
							<img src="images\calendar.gif" width=24 height=22 border=0>
						</a>
					 </td>
				 </tr>			
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="collectionTimeHour"><bean:message key="accession.collectionTime"/></label></td>
				     <td class="formField">
				     	<html:select property="collectionTimeHour" styleClass="formDropDownSized" styleId="collectionTimeHour" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="timeHourList" labelName="timeHourList"/>		
						</html:select>
		        	    <html:select property="collectionTimeMinutes" styleClass="formDropDownSized" styleId="collectionTimeMinutes" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="timeMinutesList" labelName="timeMinutesList"/>		
						</html:select>
						<html:select property="collectionTimeAMPM" styleClass="formDropDownSized" styleId="collectionTimeAMPM" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="timeAMPMList" labelName="timeAMPMList"/>		
						</html:select>
					  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="collectionSite"><bean:message key="accession.collectionSite"/></label></td>
				     <td class="formField">
				     	<html:select property="collectionSite" styleClass="formFieldSized" styleId="collectionSite" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="collectionSiteList" labelName="collectionSiteList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="collectedBy"><bean:message key="accession.collectedBy"/></label></td>
				     <td class="formField">
				     	<html:select property="collectedBy" styleClass="formFieldSized" styleId="collectedBy" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="collectedByList" labelName="collectedByList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formTitle" height="20" colspan="3"><bean:message key="accession.receivingInformation"/></td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="state"><bean:message key="accession.receivedDate"/></label></td>
					 
					 <td class="formField">
					 <html:text styleClass="formDateSized" size="15" styleId="receivedDate" property="receivedDate" readonly="true"/>
						<a href="javascript:show_calendar('accessionForm.receivedDate');">
							<img src="images\calendar.gif" width=24 height=22 border=0>
						</a>
					 </td>
				 </tr>		
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="receivedTimeHour"><bean:message key="accession.receivedTime"/></label></td>
				     <td class="formField">
				     	<html:select property="receivedTimeHour" styleClass="formDropDownSized" styleId="receivedTimeHour" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="timeHourList" labelName="timeHourList"/>		
						</html:select>
		        	    <html:select property="receivedTimeMinutes" styleClass="formDropDownSized" styleId="receivedTimeMinutes" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="timeMinutesList" labelName="timeMinutesList"/>		
						</html:select>
						<html:select property="receivedTimeAMPM" styleClass="formDropDownSized" styleId="receivedTimeAMPM" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="timeAMPMList" labelName="timeAMPMList"/>		
						</html:select>
					 </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="receivedBy"><bean:message key="accession.receivedBy"/></label></td>
				     <td class="formField">
				     	<html:select property="receivedBy" styleClass="formFieldSized" styleId="receivedBy" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="receivedByList" labelName="receivedByList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="receivedSite"><bean:message key="accession.receivedSite"/></label></td>
				     <td class="formField">
				     	<html:select property="receivedSite" styleClass="formFieldSized" styleId="receivedSite" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="receivedSiteList" labelName="receivedSiteList"/>		
						</html:select>
		        	  </td>
				 </tr>	
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="receivedMode"><bean:message key="accession.receivedMode"/></label></td>
				     <td class="formField">
				     	<html:select property="receivedMode" styleClass="formFieldSized" styleId="receivedMode" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="receivedModeList" labelName="receivedModeList"/>		
						</html:select>
		        	  </td>
				 </tr>	
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="receivedTrackNumber"><bean:message key="accession.receivedTrackNumber"/></label></td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="receivedTrackNumber" property="receivedTrackNumber" readonly="<%=readOnlyForAll%>"/></td>
				 </tr>
				 
				 <logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">
				 <tr>
				  <td align="right" colspan="3">
				 <%
					String changeAction = "setFormAction('"+formName+"')";
				  %>	
			<!-- action buttons begins -->
					<table cellpadding="4" cellspacing="0" border="0">
						<tr>
						   	<td>
						   		<html:submit styleClass="actionButton" value="Submit" onclick="<%=changeAction%>"/>
						   	</td>
							<td>
								<html:reset styleClass="actionButton"/>
							</td> 
						</tr>
					</table>
			<!-- action buttons end -->

				  </td>
				 </tr>
				 </logic:notEqual> 
				 
				</logic:notEqual>
				</table>
				
			  </td>
			 </tr>

			 <!-- NEW ACCESSION REGISTRATION ends-->
			 </html:form>
			 </table>