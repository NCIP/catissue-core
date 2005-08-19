<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List,java.util.ListIterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ParticipantForm"%>

<%
	List siteList = (List)request.getAttribute(Constants.SITELIST);
	ListIterator iterator=null;
%>
<head>
	<script language="JavaScript">
		//function to insert a row in the inner block
		function insRow(subdivtag)
		{
			var val = parseInt(document.forms[0].counter.value);
			val = val + 1;
			document.forms[0].counter.value = val;
			
			var r = new Array(); 
			r = document.getElementById(subdivtag).rows;
			var q = r.length;
			var x=document.getElementById(subdivtag).insertRow(q);
			
			// First Cell
			var spreqno=x.insertCell(0);
			spreqno.className="formSerialNumberField";
			sname=(q+1);
			spreqno.innerHTML="" + sname;

			//Second Cell
			var spreqtype=x.insertCell(1);
			spreqtype.className="formField";
			sname="";

			var name = "value(ParticipantMedicalIdentifier:" + (q+1) + "_Site_systemIdentifier)";
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='" + name + "'>";
			<%
				if(siteList!=null)
				{
					iterator = siteList.listIterator();
					while(iterator.hasNext())
					{
						NameValueBean bean = (NameValueBean)iterator.next();
			%>
						sname = sname + "<option value='<%=bean.getValue()%>'><%=bean.getName()%></option>";
			<%		}
				}
			%>
			sname = sname + "</select>";
			spreqtype.innerHTML="" + sname;
		
			//Third Cellvalue(ParticipantMedicalIdentifier:1_medicalRecordNumber)
			var spreqsubtype=x.insertCell(2);
			spreqsubtype.className="formField";
			sname="";
		
			name = "value(ParticipantMedicalIdentifier:" + (q+1) + "_medicalRecordNumber)";
			sname= "";
			sname="<input type='text' name='" + name + "' size='30'  class='formFieldSized15' id='" + name + "'>";
			spreqsubtype.innerHTML="" + sname;
		}
	</script>
</head>
<% 
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName,pageView=operation,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;

		if(operation.equals(Constants.EDIT))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.PARTICIPANT_EDIT_ACTION;
			readOnlyValue=true;
		}
		else
		{
			formName = Constants.PARTICIPANT_ADD_ACTION;
			readOnlyValue=false;
		}

		if (operation.equals(Constants.VIEW))
		{
			readOnlyForAll=true;
		}

		String pageOf = (String)request.getAttribute(Constants.PAGEOF);

		Object obj = request.getAttribute("participantForm");
		int noOfRows=0;

		if(obj != null && obj instanceof ParticipantForm)
		{
			ParticipantForm form = (ParticipantForm)obj;
			noOfRows = form.getCounter();
		}
%>


	<html:errors />

		<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
			
		   <html:form action="<%=Constants.PARTICIPANT_ADD_ACTION%>">
		   		   
		   <logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
		   	<tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
		   			<tr>
				  	<td align="right" colspan="3">
					<%
						String changeAction = "setFormAction('MakeParticipantEditable.do?"+Constants.EDITABLE+"="+!readOnlyForAll+"')";
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
					String changeAction = "setFormAction('"+Constants.PARTICIPANT_SEARCH_ACTION+"');setOperation('"+Constants.SEARCH+"');";
				 %>
 
				  <tr>
				   <td align="right" colspan="3">
					 <table cellpadding="4" cellspacing="0" border="0">
						 <tr>
						    <td>
						    	<html:submit styleClass="actionButton" value="Search" onclick="<%=changeAction%>"/></td>
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
			  
			   	
			  <!-- NEW PARTICIPANT REGISTRATION BEGINS-->
	    	  <tr>
			    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
				 <tr>
					<td><html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/></td>
					<td><html:hidden property="counter"/></td>
				 </tr>
				 
				<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
				 <tr>
					<td><html:hidden property="identifier"/></td>
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
				     <%String title = "participant."+pageView+".title";%>
				     <bean:message key="<%=title%>"/></td>
				 </tr>
				 <tr>
				    <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
				     	<label for="lastName">
				     		<bean:message key="user.lastName"/>
				     	</label>
				     </td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="lastName" property="lastName" readonly="<%=readOnlyForAll%>"/></td>
				 </tr>
				  <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
				     	<label for="firstName">
				     		<bean:message key="user.firstName"/>
				     	</label>
				     </td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="firstName" property="firstName" readonly="<%=readOnlyForAll%>"/></td>
				 </tr>
				 <tr>
				    <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
				     	<label for="middleName">
				     		<bean:message key="participant.middleName"/>
				     	</label>
				     </td>
				     <td class="formField"><html:text styleClass="formFieldSized" size="30" styleId="middleName" property="middleName" readonly="<%=readOnlyForAll%>"/></td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
						<label for="birthDate">
							<bean:message key="participant.birthDate"/>
						</label>
					</td>
					 
					 <td class="formField">
					 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
					 <html:text styleClass="formDateSized" size="25" styleId="birthDate" property="birthDate" readonly="true"/>
						<a href="javascript:show_calendar('participantForm.birthDate',null,null,'MM-DD-YYYY');">
							<img src="images\calendar.gif" width=24 height=22 border=0>
						</a>
					 </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="state"><bean:message key="participant.gender"/></label></td>
				     <td class="formField">
				     	<html:select property="gender" styleClass="formFieldSized" styleId="gender" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="genderList" labelName="genderList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="state"><bean:message key="participant.genotype"/></label></td>
				     <td class="formField">
				     	<html:select property="genotype" styleClass="formFieldSized" styleId="genotype" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="genotypeList" labelName="genotypeList"/>
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel"><label for="race"><bean:message key="participant.race"/></label></td>
				     <td class="formField">
				     	<html:select property="race" styleClass="formFieldSized" styleId="race" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="raceList" labelName="raceList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
				     <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
				     	<label for="ethnicity">
				     		<bean:message key="participant.ethnicity"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:select property="ethnicity" styleClass="formFieldSized" styleId="ethnicity" size="1" disabled="<%=readOnlyForAll%>">
							<html:options name="ethnicityList" labelName="ethnicityList"/>		
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
					 <td class="formRequiredNotice" width="5">
				     	<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">*</logic:notEqual>
				     	<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.VIEW%>">&nbsp;</logic:equal>
				     </td>
				     <td class="formRequiredLabel">
				     	<label for="socialSecurityNumber">
				     		<bean:message key="participant.socialSecurityNumber"/>
				     	</label>
				     </td>
				     <td class="formField">
				     	<html:text styleClass="formFieldSized" size="30" styleId="socialSecurityNumber" property="socialSecurityNumber" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				 
				 <!-- Medical Identifiers Begin here -->
				 <tr>
				     <td class="formTitle" height="20" colspan="2">
				     	<bean:message key="participant.medicalIdentifier"/>
				     </td>
				     <td class="formButtonField">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="insRow('addMore')">
						<bean:message key="buttons.addMore"/>
						</html:button>
				    </td>
				  </tr>
				 <tr>
				 	<td class="formSerialNumberLabel" width="5">
				     	#
				    </td>
				    <td class="formLeftSubTableTitle">
						<bean:message key="medicalrecord.source"/>
					</td>
				    <td class="formRightSubTableTitle">
						<bean:message key="medicalrecord.number"/>
					</td>
				 </tr>
				 
				 <tbody id="addMore">
				<%
				for(int i=1;i<=noOfRows;i++)
				{
					String siteName = "value(ParticipantMedicalIdentifier:"+i+"_Site_systemIdentifier)";
					String medicalRecordNumber = "value(ParticipantMedicalIdentifier:"+i+"_medicalRecordNumber)";
				%>
				 <tr>
				 	<td class="formSerialNumberField" width="5"><%=i%>.</td>
				    <td class="formField">
						<html:select property="<%=siteName%>" styleClass="formFieldSized15" styleId="<%=siteName%>" size="1" disabled="<%=readOnlyForAll%>">
							<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>		
						</html:select>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized15" size="30" styleId="<%=medicalRecordNumber%>" property="<%=medicalRecordNumber%>" readonly="<%=readOnlyForAll%>"/>
				    </td>
				 </tr>
				 <%
				}
				%>
				 </tbody>
				 
				 <!-- Medical Identifiers End here -->
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
						   				<html:submit styleClass="actionButton" onclick="<%=changeAction%>">
						   					<bean:message key="buttons.submit"/>
						   				</html:submit>
						   			</td>
									<td colspan="2">
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.reset"/>
										</html:reset>
									</td> 
									<%--td>
										<html:submit styleClass="actionButton">
											<bean:message key="buttons.submitAndRegisterParticipant"/>
										</html:submit>
									</td--%> 
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
			 
			 <!-- NEW PARTICIPANT REGISTRATION ends-->
			 </html:form>
		</table>