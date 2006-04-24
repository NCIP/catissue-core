<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="java.util.List,java.util.Iterator"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.ParticipantForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>

<script src="jss/script.js" type="text/javascript"></script>
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<% 
		List siteList = (List)request.getAttribute(Constants.SITELIST);
		
		String operation = (String)request.getAttribute(Constants.OPERATION);
		String formName, pageView=operation,editViewButton="buttons."+Constants.EDIT;
		boolean readOnlyValue=false,readOnlyForAll=false;
		String pageOf = (String)request.getAttribute(Constants.PAGEOF);
		if(operation.equals(Constants.EDIT))
		{
			editViewButton="buttons."+Constants.VIEW;
			formName = Constants.PARTICIPANT_EDIT_ACTION;
			readOnlyValue=true;
			if(pageOf.equals(Constants.QUERY))
				formName = Constants.QUERY_PARTICIPANT_EDIT_ACTION + "?pageOf="+pageOf;
		}
		else
		{
			formName = Constants.PARTICIPANT_ADD_ACTION;
			readOnlyValue=false;
		}


		Object obj = request.getAttribute("participantForm");
		int noOfRows=0;
		Map map = null;

		if(obj != null && obj instanceof ParticipantForm)
		{
			ParticipantForm form = (ParticipantForm)obj;
			map = form.getValues();
			noOfRows = form.getCounter();
		}
%>

<head>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
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
			var identifier = "value(ParticipantMedicalIdentifier:" + (q+1) +"_systemIdentifier)";
			sname = sname + "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			spreqno.innerHTML="" + sname;

			//Second Cell
			var spreqtype=x.insertCell(1);
			spreqtype.className="formField";
			sname="";

			var name = "value(ParticipantMedicalIdentifier:" + (q+1) + "_Site_systemIdentifier)";
// Mandar : 434 : for tooltip 
			sname="<select name='" + name + "' size='1' class='formFieldSized15' id='" + name + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
			<%
				if(siteList!=null)
				{
					Iterator iterator = siteList.iterator();
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
			sname="<input type='text' name='" + name + "' size='30' maxlength='50'  class='formFieldSized15' id='" + name + "'>";
			spreqsubtype.innerHTML="" + sname;
			
			//Fourth Cell
			var checkb=x.insertCell(3);
			checkb.className="formField";
			checkb.colSpan=2;
			sname="";
			var name = "chk_"+(q+1);
			sname="<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')\">";
			checkb.innerHTML=""+sname;
		}
		
		function textLimit(field) 
		{
			if(field.value.length>0) 
				field.value = field.value.replace(/[^\d]+/g, '');
				
			/*if (element.value.length > maxlen + 1)
				alert('your input has been truncated!');*/
			/*if (field.value.length > maxlen)
			{
				//field.value = field.value.substring(0, maxlen);
				field.value = field.value.replace(/[^\d]+/g, '');
			}*/
		}
		function intOnly(field) 
		{
			if(field.value.length>0) 
			{
				field.value = field.value.replace(/[^\d]+/g, ''); 
			}
		}
		
			
	</script>
</head>

<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:form action="<%=formName%>">
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
		   		   
			
		<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
		<%--logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
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
			  </logic:notEqual--%>
			  
			   	
		  <!-- NEW PARTICIPANT REGISTRATION BEGINS-->
				<tr>
				<td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0">
				 <tr>
					<td><html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/></td>
					<td><html:hidden property="counter"/></td>
					<td><html:hidden property="onSubmit"/></td>
					<td><html:hidden property="systemIdentifier" /><html:hidden property="redirectTo"/></td>
					<td><html:hidden property="pageOf" value="<%=pageOf%>"/></td>
				 </tr>
				 
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">
				 		<tr>
				     		<td class="formMessage" colspan="3">* indicates a required field</td>
				 		</tr>

				 <tr>
				     <td class="formTitle" height="20" colspan="4">
				     <%String title = "participant."+pageView+".title";%>
				     <bean:message key="<%=title%>"/>
					<%
						if(pageView.equals("edit"))
						{
					%>
				     &nbsp;<bean:message key="for.identifier"/>&nbsp;<bean:write name="participantForm" property="systemIdentifier" />
					<%
						}
					%>
				     </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="lastName">
				     		<bean:message key="user.lastName"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
				     <html:text styleClass="formFieldSized" maxlength="50" size="30" styleId="lastName" property="lastName" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				  <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="firstName">
				     		<bean:message key="user.firstName"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
				     	<html:text styleClass="formFieldSized" maxlength="50" size="30" styleId="firstName" property="firstName" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="middleName">
				     		<bean:message key="participant.middleName"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
				     <html:text styleClass="formFieldSized" maxlength="50" size="30" styleId="middleName" property="middleName" readonly="<%=readOnlyForAll%>"/>
				     </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="birthDate">
							<bean:message key="participant.birthDate"/>
						</label>
					</td>
					 
					 <td class="formField" colspan="2">
					 <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
					 <html:text styleClass="formDateSized10" maxlength="10"  size="10" styleId="birthDate" property="birthDate" disabled="<%=readOnlyForAll%>"/>
					 &nbsp;<bean:message key="page.dateFormat" />&nbsp;
						<a href="javascript:show_calendar('participantForm.birthDate',null,null,'MM-DD-YYYY');">
							<img src="images\calendar.gif" width=24 height=22 border=0></a>
					 </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="gender"><bean:message key="participant.gender"/></label>
				     </td>
				     <td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="gender" styleClass="formFieldSized" styleId="gender" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.GENDER_LIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
						<label for="genotype"><bean:message key="participant.genotype"/></label>
					</td>
				     <td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="genotype" styleClass="formFieldSized" styleId="genotype" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.GENOTYPE_LIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
					     <label for="race"><bean:message key="participant.race"/></label>
				     </td>
				     <td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="race" styleClass="formFieldSized" styleId="race" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.RACELIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel">
				     	<label for="ethnicity">
				     		<bean:message key="participant.ethnicity"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="ethnicity" styleClass="formFieldSized" styleId="ethnicity" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.ETHNICITY_LIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
					 <td class="formRequiredNotice" width="5">&nbsp;</td>
				     <td class="formLabel">
				     	<label for="socialSecurityNumber">
				     		<bean:message key="participant.socialSecurityNumber"/>
				     	</label>
				     </td>
				     <td class="formField" colspan="2">
				     	<html:text styleClass="formFieldSized2" maxlength="3" styleId="socialSecurityNumberPartA" property="socialSecurityNumberPartA" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);"/>
				     	-
				     	<html:text styleClass="formFieldSized1" maxlength="2" styleId="socialSecurityNumberPartB" property="socialSecurityNumberPartB" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);"/>
				     	-
				     	<html:text styleClass="formFieldSized3" maxlength="4" styleId="socialSecurityNumberPartC" property="socialSecurityNumberPartC" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);"/>
				     </td>
				 </tr>
				 
				 <!-- activitystatus -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel" >
						<label for="activityStatus">
							<bean:message key="participant.activityStatus" />
						</label>
					</td>
					<td class="formField" colspan="2">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1" onchange="<%=strCheckStatus%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select>
					</td>
				</tr>
				</logic:equal>
				
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
				    <td class="formTitle" align="Right">
						<html:button property="deleteValue" styleClass="actionButton" onclick="deleteChecked('addMore','Participant.do?operation=<%=operation%>&pageOf=pageOfParticipant&status=true',document.forms[0].counter,'chk_',false)"  disabled="true">
							<bean:message key="buttons.delete"/>
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
					<td class="formRightSubTableTitle">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
						</td>
				 </tr>
				 
				 <tbody id="addMore">
				<%
				for(int i=1;i<=noOfRows;i++)
				{
					String siteName = "value(ParticipantMedicalIdentifier:"+i+"_Site_systemIdentifier)";
					String medicalRecordNumber = "value(ParticipantMedicalIdentifier:"+i+"_medicalRecordNumber)";
					String identifier = "value(ParticipantMedicalIdentifier:" + i +"_systemIdentifier)";
					String check = "chk_"+i;
				%>
				 <tr>
				 	<td class="formSerialNumberField" width="5"><%=i%>.
				 		<html:hidden property="<%=identifier%>" />
				 	</td>
				    <td class="formField">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="<%=siteName%>" styleClass="formFieldSized15" styleId="<%=siteName%>" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>		
						</html:select>
					</td>
				    <td class="formField">
				     	<html:text styleClass="formFieldSized15" maxlength="50" size="30" styleId="<%=medicalRecordNumber%>" property="<%=medicalRecordNumber%>" readonly="<%=readOnlyForAll%>"/>
				    </td>
				    <%
							String key = "ParticipantMedicalIdentifier:" + i +"_systemIdentifier";
							boolean bool = Utility.isPersistedValue(map,key);
							String condition = "";
							if(bool)
								condition = "disabled='disabled'";

						%>
						<td class="formField" width="5">
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=condition%> onClick="enableButton(document.forms[0].deleteValue,document.forms[0].counter,'chk_')">		
						</td>
				    
				 </tr>
				 <%
				}
				%>
				 </tbody>
				 
				 <!-- Medical Identifiers End here -->
				 	<tr>
				  		<td align="right" colspan="4">
							<%
								String changeAction = "setFormAction('"+formName+"')";
				 			%>
							<!-- action buttons begins -->
							<table cellpadding="4" cellspacing="0" border="0">
								<tr>
									<td rowspan=2 class="formFieldNoBorders" nowrap>
										<label for="proceedWith">
											<bean:message key="proceedWith"/>
										</label>
									</td>

								<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
						   			<td nowrap class="formFieldNoBorders">
										<html:radio styleClass="" property="forwardTo" value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][1]%>">
										<label for="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>">
											<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>
										</label>
										</html:radio>
									</td>
								</logic:notEqual>	
						   			<td>
						   				<html:submit styleClass="actionButton" disabled="true">
						   					<bean:message key="buttons.getClinicalData"/>
						   				</html:submit>
						   			</td>
						   			
						   			<td>
						   			<%
						   				String action = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
						   			%>
						   				<html:button styleClass="actionButton" property="submitPage" onclick="<%=action%>">
						   					<bean:message key="buttons.submit"/>
						   				</html:button>
							   		</td>
									<td colspan="3">
										<html:reset styleClass="actionButton">
											<bean:message key="buttons.reset"/>
										</html:reset>
									</td> 
								<tr>
									<td nowrap class="formFieldNoBorders">
									<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
										<html:radio styleClass=""  property="forwardTo" value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[1][1]%>">
										<label for="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[1][0]%>">
											<%=Constants.PARTICIPANT_FORWARD_TO_LIST[1][0]%>
										</label>
										</html:radio>
									</logic:notEqual>	
									</td>
								</tr>
							</table>
							<!-- action buttons end -->
				  		</td>
				 	</tr>
				 
				</logic:notEqual>
				</table>
			  </td>
			 </tr>
			 
			 <!-- NEW PARTICIPANT REGISTRATION ends-->
			
		</table>
	 </html:form>	
		