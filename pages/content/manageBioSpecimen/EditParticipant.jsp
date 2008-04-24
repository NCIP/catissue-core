

<%
	String[] activityStatusList = (String[])request.getAttribute(Constants.ACTIVITYSTATUSLIST);
%>

<%if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
	{
	strCheckStatus= "checkActivityStatus(this,'" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
}%>


<script language="JavaScript">
function participantRegRow(subdivtag)
		{
			var collectionProtocolRegistrationVal = parseInt(document.forms[0].collectionProtocolRegistrationValueCounter.value);
			collectionProtocolRegistrationVal = collectionProtocolRegistrationVal + 1;
			document.forms[0].collectionProtocolRegistrationValueCounter.value = collectionProtocolRegistrationVal;
			
			var rows = new Array(); 
			rows = document.getElementById(subdivtag).rows;
			var cprSize = rows.length;
			var row = document.getElementById(subdivtag).insertRow(cprSize);
			
			// First Cell
			var cprTitle=row.insertCell(0);
			cprTitle.className="formFieldWithoutBorder";
			sname="";
			var name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_CollectionProtocol_id)";
			var keyValue = name;
			sname = sname +"<select name='" + name + "' size='1' class='formFieldSized15' id='" + name + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
			<%
				if(collectionProtocolList!=null)
				{
					Iterator iterator = collectionProtocolList.iterator();
					while(iterator.hasNext())
					{
						NameValueBean bean = (NameValueBean)iterator.next();
			%>
						sname = sname + "<option value=\"<%=bean.getValue()%>\"><%=bean.getName()%></option>";
			<%		}
				}
			%>
			sname = sname + "</select>";
			var collectionProtocolTitleValue = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_CollectionProtocol_shortTitle)";
			sname = sname + "<input type='hidden' name='" + collectionProtocolTitleValue + "' value='' id='" + collectionProtocolTitleValue + "'>";
			cprTitle.innerHTML="" + sname;
			
			//Second Cell
			var cprParticipantId=row.insertCell(1);
			cprParticipantId.className="formFieldWithoutBorder";
			sname="";
			name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_protocolParticipantIdentifier)";
			sname="<input type='text' name='" + name + "' maxlength='30'  class='formFieldSized7' id='" + name + "'>";
			cprParticipantId.innerHTML="" + sname;
			
			<%
				String registrationDate = Utility.parseDateToString(Calendar.getInstance().getTime(), Constants.DATE_PATTERN_MM_DD_YYYY);
    		%>
    		
			//Third Cell
			var cprRegistrationDate=row.insertCell(2);
			cprRegistrationDate.className="formFieldWithoutBorder";
			cprRegistrationDate.colSpan=2;
			sname="";
			var name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_registrationDate)";
			//sname = "<input type='text' name='" + name + "' class='formFieldSized15' id='" + name + "' value = 'MM-DD-YYYY or MM/DD/YYYY' onclick = \"this.value = ''\" onblur = \"if(this.value=='') {this.value = 'MM-DD-YYYY or MM/DD/YYYY';}\" onkeypress=\"return titliOnEnter(event, this, document.getElementById('" + name + "'))\">";
			sname = "<input type='text' name='" + name + "' class='formFieldSized7' id='" + name + "' value = '<%=registrationDate%>'>";
			cprRegistrationDate.innerHTML=sname;
			
			//Fourth Cell
			var cprActivityStatus=row.insertCell(3);
			cprActivityStatus.className="formFieldWithoutBorder";
			sname="";
			var name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) +"_activityStatus)";
			sname = sname +"<select name='" + name + "' size='1' class='formFieldSized7' id='" + name + "' disabled='disabled' onmouseover=showTip(this.id) onmouseout=hideTip(this.id) >";
			<%
				for(int i=0 ; i<activityStatusList.length; i++)
				{
					String selected= "";
					if(i==1)
					{
						selected="selected='selected'";
					}
			%>
					sname = sname + "<option value='<%=activityStatusList[i]%>' <%=selected%> ><%=activityStatusList[i]%></option>";
			<%	
				}
			%>
			sname = sname + "</select>";
			cprActivityStatus.innerHTML=sname;
											
			//Fifth Cell
			var consent=row.insertCell(4);
			consent.className="formFieldWithoutBorder";
			sname="";
			
			var spanTag=document.createElement("span");
			var consentCheckStatus="consentCheckStatus_"+(cprSize+1);
			spanTag.setAttribute("id",consentCheckStatus);
				
			var name = "CollectionProtocolConsentChk_"+ (cprSize+1);
			var anchorTagKey = "ConsentCheck_"+ (cprSize+1);
			var collectionProtocolValue = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_CollectionProtocol_id)";
			var collectionProtocolTitleValue = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_CollectionProtocol_shortTitle)";
			var anchorTag = document.createElement("a");
			anchorTag.setAttribute("id",anchorTagKey);
			spanTag.innerHTML="<%=Constants.NO_CONSENTS_DEFINED%>"+"<input type='hidden' name='" + name + "' value='Consent' id='" + name + "'>";
			spanTag.appendChild(anchorTag);
			consent.appendChild(spanTag);
			document.getElementById(keyValue).onchange=function(){getConsent(name,collectionProtocolValue,collectionProtocolTitleValue,(cprSize+1),anchorTagKey,consentCheckStatus)};
			
			
			//sixth Cell
			var cprCheckb=row.insertCell(5);
			cprCheckb.className="formFieldWithoutBorder";
			sname="";
			
			var identifier = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) +"_id)";
			sname = sname + "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			
			var name = "CollectionProtocolRegistrationChk_"+(cprSize+1);
			sname = sname +"<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteParticipantRegistrationValue,document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_')\">";
			cprCheckb.innerHTML=""+sname;
		}


		function setSubmittedForParticipant(submittedFor,forwardTo)
		{
			document.forms[0].submittedFor.value = submittedFor;
			document.forms[0].forwardTo.value    = forwardTo;
			
			<%if(request.getAttribute(Constants.SUBMITTED_FOR)!=null && request.getAttribute(Constants.SUBMITTED_FOR).equals("AddNew")){%>
				document.forms[0].submittedFor.value = "AddNew";
			<%}%>			
			<%if(request.getAttribute(Constants.SPREADSHEET_DATA_LIST)!=null && dataList.size()>0){%>	

				if(document.forms[0].radioValue.value=="Add")
				{
					document.forms[0].action="<%=Constants.PARTICIPANT_ADD_ACTION%>";
					<%
					if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
					{
							if(operation.equals(Constants.ADD))
							{
						%>
							document.forms[0].action="<%=Constants.CP_QUERY_PARTICIPANT_ADD_ACTION%>";
						<%
							}
						else
							{ 
						%>
							document.forms[0].action="<%=Constants.CP_QUERY_PARTICIPANT_EDIT_ACTION%>";
						<%
							}
					}
					%>
				}
				else
				{
					if(document.forms[0].radioValue.value=="Lookup")
					{
						document.forms[0].action="<%=Constants.PARTICIPANT_LOOKUP_ACTION%>";
						<%if(pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY))
						{%>
							document.forms[0].action="<%=Constants.CP_QUERY_PARTICIPANT_LOOKUP_ACTION%>";
						<%}%>												
						document.forms[0].submit();
					}
				}		
			<%}%>	
	setCollectionProtocolTitle();		
	if((document.forms[0].activityStatus != undefined) && (document.forms[0].activityStatus.value == "Disabled"))
   	{
	    var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
		if (go==true)
		{
			document.forms[0].submit();
		}
	} 
	else
	{
			checkActivityStatusForCPR();	
	}
}

	function setCollectionProtocolTitle()
	{
		var collectionProtocolRegistrationVal = parseInt(document.forms[0].collectionProtocolRegistrationValueCounter.value);
		for(i = 1 ; i <= collectionProtocolRegistrationVal ; i++)
		{
			var collectionProtocolId = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_CollectionProtocol_id)";
			collectionProtocolIdValue=document.getElementById(collectionProtocolId).value;
			var collectionProtocolTitle;
			<%
			if(collectionProtocolList!=null)
			{
				Iterator iterator = collectionProtocolList.iterator();
				while(iterator.hasNext())
				{
					NameValueBean bean = (NameValueBean)iterator.next();
			%>
					if(collectionProtocolIdValue =="<%=bean.getValue()%>")
					{
						collectionProtocolTitle = "<%=bean.getName()%>";
					}
			<%		
				}
			}
			%>
			var collectionProtocolTitleKey = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_CollectionProtocol_shortTitle)";
			document.getElementById(collectionProtocolTitleKey).value = collectionProtocolTitle;
		}
	}

	function checkActivityStatusForCPR()
		{
			var collectionProtocolRegistrationVal = parseInt(document.forms[0].collectionProtocolRegistrationValueCounter.value);
			var isAllActive = true;
			for(i = 1 ; i <= collectionProtocolRegistrationVal ; i++)
			{
				var name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_activityStatus)";
				if((document.getElementById(name) != undefined) && document.getElementById(name).value=="Disabled")
				{
					isAllActive = false;
					var go = confirm("Disabling any data will disable ALL its associated data also. Once disabled you will not be able to recover any of the data back from the system. Please refer to the user manual for more details. \n Do you really want to disable?");
					if (go==true)
					{
						document.forms[0].submit();
					}
					else
					{
						break;
					}
				}
			}
			
			if (isAllActive==true)
			{
				document.forms[0].submit();
			}
		}
    function showMessage(titleMessage)
		{
     		 Tip(titleMessage,BGCOLOR,'#FFFFFF',BORDERCOLOR,'#000000',FONTCOLOR,'#000000',WIDTH,'30',FOLLOWMOUSE,'FALSE');
    	}
    function setSize()
	    {
			var container = document.getElementById("tablecontainer");
			var tempWidth =document.body.clientWidth;
		    var tempHeight=document.body.clientHeight;
            container.style.height=tempHeight-50;
            container.style.width=tempWidth-50;
	   		container = document.getElementById("identifier");
		    container.style.width=tempWidth-100;
	    }
</script>


 <body onload="setSize()">
  <div style="width:100%">
	<table summary="" cellpadding="0" cellspacing="0" border="0"  id="tablecontainer" >
	   		   
			
		<!-- If operation is equal to edit or search but,the page is for query the identifier field is not shown -->
		<%--logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.ADD%>">
			<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.QUERY%>">
			<!-- ENTER IDENTIFIER BEGINS-->	
			  <br/>	
  	    	  <tr>
    		    <td>
			 	 <table summary="" cellpadding="3" cellspacing="0" border="0" >
			 	 
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
		<tr><td>
			 <table summary="" cellpadding="3" cellspacing="0" border="0">
				 <tr>
					<td>
						<input type="hidden" name="participantId" value="<%=participantId%>"/>
						<input type="hidden" name="cpId" id="cpId"/>
						<input type="hidden" name="radioValue"/>
						<html:hidden property="<%=Constants.OPERATION%>" value="<%=operation%>"/>
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
						<html:hidden property="forwardTo" value="<%=forwardTo%>"/>
					</td>
					<td><html:hidden property="valueCounter"/></td>
					<td><html:hidden property="collectionProtocolRegistrationValueCounter"/></td>
					<td><html:hidden property="onSubmit" /></td>
					<td><html:hidden property="id" /><html:hidden property="redirectTo"/></td>
					<td><html:hidden property="pageOf" value="<%=pageOf%>"/></td>
				 </tr>
				 
				<logic:notEqual name="<%=Constants.OPERATION%>" value="<%=Constants.SEARCH%>">


				 <%--<tr>
				     <td class="formTitle" height="20" colspan="6">
				     <%title = "participant."+pageView+".title";%>
				     <bean:message key="participant.add.title"/>
					<%
						if(pageView.equals("edit"))
						{
					%>
				     &nbsp;<bean:message key="for.identifier"/>&nbsp;<bean:write name="participantForm" property="id" />
					<%
						}
					%>
				     
				 </tr>--%>
				 
				 <tr>
				 	<% 
				 		if (pageView.equals("add"))
				 		{
				 	%>
				 			<td class="formTitle" height="20" colspan="6">
						     <%title = "participant."+pageView+".title";%>
						     <bean:message key="participant.add.title"/>
						     </td>
				 	<%	}
				 		else
				 		{
				 	%>
				 		<br>
				 	<%
				 		}
				 	%>
				</tr>
				 
				 <%-- <tr>
				 	<td class="formTitle" height="20" colspan="7">
					 <logic:equal name="operation" value="<%=Constants.ADD%>">
						<bean:message key="participant.add.title"/>
					</logic:equal>
					<logic:equal name="operation" value="<%=Constants.EDIT%>">
						<bean:message key="participant.edit.title"/>
					</logic:equal>
					</td>
				</tr>--%>
				
				
				 <tr>
					 <td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
					 <td class="formFieldNoBordersSimple">
				     	<label for="socialSecurityNumber">
				     		<bean:message key="participant.socialSecurityNumber"/>
				     	</label>
				     </td>
				     <td class="formFieldNoBordersSimple" colspan="5">
				     	<html:text styleClass="formFieldSized2" maxlength="3" styleId="socialSecurityNumberPartA" property="socialSecurityNumberPartA" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);moveToNext(this,this.value,'socialSecurityNumberPartB');"/>
				     	-
				     	<html:text styleClass="formFieldSized1" maxlength="2" styleId="socialSecurityNumberPartB" property="socialSecurityNumberPartB" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);moveToNext(this,this.value,'socialSecurityNumberPartC');"/>
				     	-
				     	<html:text styleClass="formFieldSized3" maxlength="4" styleId="socialSecurityNumberPartC" property="socialSecurityNumberPartC" readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);" onchange="intOnly(this);" onkeyup="intOnly(this);"/>
				     </td>
				 </tr>
				 	
				 <tr>
					<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple" >
						<table summary="" cellpadding="3" cellspacing="0" border="0">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td class="formFieldNoBordersSimple">
									<label for="Name">
										<bean:message key="participant.Name"/>
									</label>
								</td>
							</tr>
						 </table>
					 </td>
				    <td class="formFieldNoBordersSimple" colspan="5">
					 <table summary="" cellpadding="3" cellspacing="0" border="0">
						<tr>
							<td class="formFieldNoBordersSimple">
								<label for="lastName">
									<bean:message key="participant.lastName"/>
								</label>
							 </td>
							 <td class="formFieldNoBordersSimple">
								<label for="firstName">
									<bean:message key="participant.firstName"/>
								</label>
							  </td>
							<td class="formFieldNoBordersSimple">
								<label for="middleName">
									<bean:message key="participant.middleName"/>
								</label>
							 </td>
						</tr>
					 	<tr>
							 <td class="formFieldNoBordersSimple">
								<html:text styleClass="formFieldSized10" maxlength="255" styleId="lastName" name="participantForm" property="lastName" readonly="<%=readOnlyForAll%>" onkeyup="moveToNext(this,this.value,'firstName')"/>
							 </td>
					     	 <td class="formFieldNoBordersSimple">
					     		<html:text styleClass="formFieldSized10" maxlength="255" styleId="firstName" property="firstName" readonly="<%=readOnlyForAll%>" onkeyup="moveToNext(this,this.value,'middleName')"/>
							</td>
					     	 <td class="formFieldNoBordersSimple">
					     		<html:text styleClass="formFieldSized10" maxlength="255" styleId="middleName" property="middleName" readonly="<%=readOnlyForAll%>"/>
							</td>
						</tr>
					 </table>
				    </td>
				 </tr>
				 
				 <tr>
					<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple">
						<label for="birthDate">
							<bean:message key="participant.birthDate"/>
						</label>
					</td>
					 
					 <td class="formFieldNoBordersSimple" colspan="5">
<%
	 if(currentBirthDate.trim().length() > 0)
	{
			Integer birthYear = new Integer(Utility.getYear(currentBirthDate ));
			Integer birthMonth = new Integer(Utility.getMonth(currentBirthDate ));
			Integer birthDay = new Integer(Utility.getDay(currentBirthDate ));
%>
			<ncombo:DateTimeComponent name="birthDate"
									  id="birthDate"
 									  formName="participantForm"	
									  month= "<%=birthMonth %>"
									  year= "<%=birthYear %>"
									  day= "<%= birthDay %>" 
									  value="<%=currentBirthDate %>"
									  styleClass="formDateSized10"
											 />		
<% 
	}
	else
	{  
 %>
			<ncombo:DateTimeComponent name="birthDate"
									  id="birthDate"
 									  formName="participantForm"	
									  styleClass="formDateSized10" 
											 />		
<%
	}
%>
<bean:message key="page.dateFormat" />&nbsp;
					 </td>
				 </tr>			
				 
				 <tr>
					<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple">
				     	<label for="vitalStatus">
				     		<bean:message key="participant.vitalStatus"/>
				     	</label>
				     </td>
				     <td class="formFieldNoBordersSimple" colspan="5">
<!-- Mandar : 434 : for tooltip -->
						<%--
				     	<html:select property="vitalStatus" styleClass="formFieldSized" styleId="vitalStatus" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.VITAL_STATUS_LIST%>" labelProperty="name" property="value"/>
						</html:select>--%>
						<logic:iterate id="nvb" name="<%=Constants.VITAL_STATUS_LIST%>">
						<%	NameValueBean nameValueBean=(NameValueBean)nvb;%>
						<html:radio property="vitalStatus" onclick="onVitalStatusRadioButtonClick(this)" value="<%=nameValueBean.getValue()%>"><%=nameValueBean.getName()%> </html:radio>
						</logic:iterate>
						
		        	  </td>
				 </tr>
				 
				
				<%-- added by chetan for death date --%>
 <tr>
	<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
	<td class="formFieldNoBordersSimple">
		<label for="deathDate">
			<bean:message key="participant.deathDate"/>
		</label>
	</td>	 
	<td class="formFieldNoBordersSimple" colspan="5">
<%

	ParticipantForm form = (ParticipantForm) request.getAttribute("participantForm");
	Boolean deathDisable = new Boolean("false");
	if(!form.getVitalStatus().trim().equals("Dead"))
	{
		deathDisable = new Boolean("true");
	}
	 if(currentDeathDate.trim().length() > 0)
	{
			Integer deathYear = new Integer(Utility.getYear(currentDeathDate ));
			Integer deathMonth = new Integer(Utility.getMonth(currentDeathDate ));
			Integer deathDay = new Integer(Utility.getDay(currentDeathDate ));
%>
			<ncombo:DateTimeComponent name="deathDate"
									  id="deathDate"
 									  formName="participantForm"	
									  month= "<%=deathMonth %>"
									  year= "<%=deathYear %>"
									  day= "<%= deathDay %>" 
									  value="<%=currentDeathDate %>"
									  styleClass="formDateSized10"
									  disabled="<%=deathDisable%>"
											 />		
<% 
	}
	else
	{  
 %>
			<ncombo:DateTimeComponent name="deathDate"
									  id="deathDate"
 									  formName="participantForm"	
									  styleClass="formDateSized10" 
									  disabled="<%=deathDisable%>"
											 />		
<%
	}
%>
<bean:message key="page.dateFormat" />&nbsp;
	</td>
</tr> 
				 
				 <tr>
					<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple">
				     	<label for="gender"><bean:message key="participant.gender"/></label>
				     </td>
				     <td class="formFieldNoBordersSimple" colspan="5">
<!-- Mandar : 434 : for tooltip -->
				     	<%--<html:select property="gender" styleClass="formFieldSized" styleId="gender" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.GENDER_LIST%>" labelProperty="name" property="value"/>
						</html:select>--%>
						<logic:iterate id="nvb" name="<%=Constants.GENDER_LIST%>">
						<%	NameValueBean nameValueBean=(NameValueBean)nvb;%>
						<html:radio property="gender" value="<%=nameValueBean.getValue()%>"><%=nameValueBean.getName()%> </html:radio>
						</logic:iterate>
						
		        	  </td>
				 </tr>
				 <tr>
					<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple">
						<label for="genotype"><bean:message key="participant.genotype"/></label>
					</td>
				     <td class="formFieldNoBordersSimple" colspan="5">
					 
					  <autocomplete:AutoCompleteTag property="genotype"
										  optionsList = "<%=request.getAttribute(Constants.GENOTYPE_LIST)%>"
										  initialValue="<%=form.getGenotype()%>"
										  styleClass="formFieldSized"
									    />

		        	  </td>
				 </tr>
				 <tr>
					<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple">
					     <label for="race"><bean:message key="participant.race"/></label>
				     </td>
				     <td class="formFieldNoBordersSimple" colspan="5">
<!-- Mandar : 434 : for tooltip -->
				     	<html:select property="raceTypes" styleClass="formFieldSized" styleId="race" size="4" multiple="true" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.RACELIST%>" labelProperty="name" property="value"/>
						</html:select>
		        	  </td>
				 </tr>
				 <tr>
					<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
					<td class="formFieldNoBordersSimple">
				     	<label for="ethnicity">
				     		<bean:message key="participant.ethnicity"/>
				     	</label>
				     </td>
				     <td class="formFieldNoBordersSimple" colspan="5">
					 
					   <autocomplete:AutoCompleteTag property="ethnicity"
										  optionsList = "<%=request.getAttribute(Constants.ETHNICITY_LIST)%>"
										  initialValue="<%=form.getEthnicity()%>"
										  styleClass="formFieldSized"
									    />

		        	  </td>
				 </tr>
				 
				 <!-- activitystatus -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<tr>
					<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
					<td class="formFieldNoBordersSimple" >
						<label for="activityStatus">
							<b><bean:message key="participant.activityStatus" /></b>
						</label>
					</td>
					<td class="formFieldNoBordersSimple" colspan="5">
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
				 	<td align="left" colspan="7" valign="top">
					<table summary="" cellpadding="3" cellspacing="0" border="0" id="identifier">
				 <tr>
				     <td class="formTitle" height="20" colspan="5">
				     	<bean:message key="participant.medicalIdentifier"/>
				     </td>
				     <td class="formButtonField">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="insRow('addMore')">
						<bean:message key="buttons.addMore"/>
						</html:button>
				    </td>
				    <td class="formTitle" align="Right">
						<html:button property="deleteMedicalIdentifierValue" styleClass="actionButton" onclick="deleteChecked('addMore','Participant.do?operation=<%=operation%>&pageOf=<%=pageOf%>&status=true',document.forms[0].valueCounter,'chk_',false)"  disabled="true">
							<bean:message key="buttons.delete"/>
						</html:button>
					</td>
				  </tr>
				 <tr>
					<td class="formSubTitleWithoutBorder">
						<bean:message key="medicalrecord.source"/>
					</td>
				    <td class="formSubTitleWithoutBorder" colspan="3">
						<bean:message key="medicalrecord.number"/>
					</td>
					<td class="formSubTitleWithoutBorder" colspan="3">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
						</td>
				 </tr>
				 <script> document.forms[0].valueCounter.value = <%=noOfRows%> </script>
				 
				 <tbody id="addMore">
				<%
				for(int i=1;i<=noOfRows;i++)
				{
					String siteName = "value(ParticipantMedicalIdentifier:"+i+"_Site_id)";
					String medicalRecordNumber = "value(ParticipantMedicalIdentifier:"+i+"_medicalRecordNumber)";
					String identifier = "value(ParticipantMedicalIdentifier:" + i +"_id)";
					String check = "chk_"+i;
				%>
				 <tr>
					<td class="formFieldNoBordersSimple">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="<%=siteName%>" styleClass="formFieldSized10" styleId="<%=siteName%>" size="1" disabled="<%=readOnlyForAll%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.SITELIST%>" labelProperty="name" property="value"/>		
						</html:select>
					</td>
				    <td class="formFieldNoBordersSimple" colspan="3">
				     	<html:text styleClass="formFieldSized10" maxlength="50" size="30" styleId="<%=medicalRecordNumber%>" property="<%=medicalRecordNumber%>" readonly="<%=readOnlyForAll%>"/>
				    </td>
				    	<%
							String key = "ParticipantMedicalIdentifier:" + i +"_id";
							boolean bool = Utility.isPersistedValue(map,key);
							String condition = "";
							if(bool)
								condition = "disabled='disabled'";

						%>
						<td class="formFieldNoBordersSimple" width="5" colspan="3">
							<html:hidden property="<%=identifier%>" />
							<input type=checkbox name="<%=check %>" id="<%=check %>" <%=condition%> onClick="enableButton(document.forms[0].deleteMedicalIdentifierValue,document.forms[0].valueCounter,'chk_')">		
						</td>
				    
				 </tr>
				 <%
				}
				%>
				 </tbody>
				  					
<!-- Medical Identifiers End here -->
				 
				 
				 
<!-- Participant Registration Begin here -->
				
				   <tr>
				     <td class="formTitle" height="20" colspan="5">
				     	<bean:message key="participant.collectionProtocolReg"/>
				     </td>
				     <td class="formButtonField">
						<html:button property="addKeyValue" styleClass="actionButton" onclick="participantRegRow('addMoreParticipantRegistration')">
						<bean:message key="buttons.addMore"/>
						</html:button>
				    </td>
				    <td class="formTitle" align="Right">
						<html:button property="deleteParticipantRegistrationValue" styleClass="actionButton" onclick="deleteChecked('addMoreParticipantRegistration','Participant.do?operation=<%=operation%>&pageOf=<%=pageOf%>&status=true&deleteRegistration=true',document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_',false)"  disabled="true">
							<bean:message key="buttons.delete"/>
						</html:button>
					</td>
				  </tr>
				 <tr>
					<td class="formSubTitleWithoutBorder">
						<bean:message key="participant.collectionProtocolReg.protocolTitle"/>
					</td>
				    <td class="formSubTitleWithoutBorder">
						<bean:message key="participant.collectionProtocolReg.participantProtocolID"/>
					</td>
					<td class="formSubTitleWithoutBorder" colspan="2">
						<bean:message key="participant.collectionProtocolReg.participantRegistrationDate"/>
					</td>
					<td class="formSubTitleWithoutBorder">
						<bean:message key="participant.activityStatus" />
					</td>
					<td class="formSubTitleWithoutBorder">
						<bean:message key="participant.collectionProtocolReg.consent"/>
					</td>
					<td class="formSubTitleWithoutBorder">
							<label for="delete" align="center">
								<bean:message key="addMore.delete" />
							</label>
					</td>
				 </tr>
				<script> document.forms[0].collectionProtocolRegistrationValueCounter.value = <%=noOrRowsCollectionProtocolRegistration%> </script>
				 
				<tbody id="addMoreParticipantRegistration">
				<%
				for(int i=1;i<=noOrRowsCollectionProtocolRegistration;i++)
				{
					String collectionProtocolId = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"+i+"_CollectionProtocol_id)";
					String collectionProtocolTitle = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"+i+"_CollectionProtocol_shortTitle)";
					String collectionProtocolParticipantId = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"+i+"_protocolParticipantIdentifier)";
					String collectionProtocolRegistrationDate = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_registrationDate)";
					String collectionProtocolIdentifier = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_id)";
					String collectionProtocolRegistrationActivityStatus = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_activityStatus)";
					String collectionProtocolCheck = "CollectionProtocolRegistrationChk_"+i;
					String key = "CollectionProtocolRegistration:" + i +"_id";
					String collectionProtocolConsentCheck = "CollectionProtocolConsentChk_"+i;
					String anchorTagKey = "ConsentCheck_"+i;
					String consentCheckStatus="consentCheckStatus_"+i;
					
					String consentResponseDisplay = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + i +"_isConsentAvailable)";
					String consentResponseDisplayKey = "CollectionProtocolRegistration:" + i +"_isConsentAvailable";
					String consentResponseDisplayValue = (String)form.getCollectionProtocolRegistrationValue(consentResponseDisplayKey);
					
					String collectionProtocolTitleKey = "CollectionProtocolRegistration:"+i+"_CollectionProtocol_shortTitle";
					String collectionProtocolTitleValue = (String)form.getCollectionProtocolRegistrationValue(collectionProtocolTitleKey);

					String collectionProtocolIdKey = "CollectionProtocolRegistration:"+i+"_CollectionProtocol_id";
					String collectionProtocolIdValue = (String)form.getCollectionProtocolRegistrationValue(collectionProtocolIdKey);
					//Bug:5935 
					String collectionProtocolRegIdValue = 	(String)form.getCollectionProtocolRegistrationValue("CollectionProtocolRegistration:" + i +"_id");					
					
					if(consentResponseDisplayValue ==null)
					{
						consentResponseDisplayValue = Constants.NO_CONSENTS_DEFINED;
					}
					boolean CollectionProtocolRegConditionBoolean = Utility.isPersistedValue(mapCollectionProtocolRegistration,key);
					boolean activityStatusCondition=false;
					if(!CollectionProtocolRegConditionBoolean)
						activityStatusCondition = true;
					
					String onChangeFun ="getConsent('"+collectionProtocolCheck+"','"+collectionProtocolId+"', '"+collectionProtocolTitleKey+"','"+i+"','"+anchorTagKey+"','"+consentCheckStatus+"')";					
				%>
					
				 <tr>
				 <td class="formFieldWithoutBorder">
					<%
					 if(CollectionProtocolRegConditionBoolean)
					 {
					 %>
					 	<html:text styleClass="formFieldSized15" maxlength="50"  styleId="<%=collectionProtocolTitle%>" property="<%=collectionProtocolTitle%>" readonly="<%=readOnlyValue%>"/>
				 		<input type="hidden" id="<%=collectionProtocolId%>" name="<%=collectionProtocolId%>" value="<%=collectionProtocolIdValue%>" />
					<%}else{ %>
						<html:select property="<%=collectionProtocolId%>" styleClass="formFieldSized15" styleId="<%=collectionProtocolId%>" size="1" 
				 		onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="<%=onChangeFun%>">
						    <html:options collection="<%=Constants.PROTOCOL_LIST%>" labelProperty="name" property="value"/>															
					    </html:select>
					    <input type="hidden" id="<%=collectionProtocolTitle%>" name="<%=collectionProtocolTitle%>" value="<%=collectionProtocolTitleValue%>" />
							
					<%} %>
				</td>
				    <td class="formFieldWithoutBorder">
						<html:text styleClass="formFieldSized7" maxlength="30"  styleId="<%=collectionProtocolParticipantId%>" property="<%=collectionProtocolParticipantId%>" />
					</td>
				    <td class="formFieldWithoutBorder" colspan="2">
				    	<!-- <html:text styleClass="formFieldSized15" maxlength="50"  styleId="<%=collectionProtocolRegistrationDate%>" property="<%=collectionProtocolRegistrationDate%>" onclick = "this.value = ''" onblur = "if(this.value=='') {this.value = 'MM-DD-YYYY or MM/DD/YYYY';}" onkeypress="return titliOnEnter(event, this, document.getElementById('<%=collectionProtocolRegistrationDate%>'))"/> -->
				    	<html:text styleClass="formFieldSized7" maxlength="30"  styleId="<%=collectionProtocolRegistrationDate%>" property="<%=collectionProtocolRegistrationDate%>" />
				    </td>
					<td class="formFieldWithoutBorder">
						<html:select property="<%=collectionProtocolRegistrationActivityStatus%>" styleClass="formFieldSized7" styleId="<%=collectionProtocolRegistrationActivityStatus%>" size="1" disabled='<%=activityStatusCondition%>' onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select>
					</td>
				    <td class="formFieldWithoutBorder">
				    	<span id="<%=consentCheckStatus%>">
						<%
							
							if(!consentResponseDisplayValue.equals(Constants.NO_CONSENTS_DEFINED))
							{
								if(operation.equals(Constants.EDIT))
								{
									consentResponseDisplayValue = Constants.PARTICIPANT_CONSENT_EDIT_RESPONSE;
								}
						%>
								<a id="<%=anchorTagKey%>" href="javascript:openConsentPage('<%=collectionProtocolId%>','<%=i%>','<%=consentResponseDisplayValue%>','<%=collectionProtocolRegIdValue%>')">
								<%=consentResponseDisplayValue%><br>
								<input type='hidden' name="<%=collectionProtocolConsentCheck%>" value='Consent' id="<%=collectionProtocolConsentCheck%>" >
								<input type='hidden' name="<%=consentResponseDisplay%>" value="<%=consentResponseDisplayValue%>" id="<%=consentResponseDisplay%>" >
								</a>
						<%
							}
							else
							{
						%>
								<%=consentResponseDisplayValue%>
								<input type='hidden' name="<%=collectionProtocolConsentCheck%>" value='Consent' id="<%=collectionProtocolConsentCheck%>" >
								<input type='hidden' name="<%=consentResponseDisplay%>" value="<%=consentResponseDisplayValue%>" id="<%=consentResponseDisplay%>" >
							
						<%
							}
						%>
						</span>
					</td>
				       <%
							String CollectionProtocolRegCondition = "";
							if(CollectionProtocolRegConditionBoolean)
								CollectionProtocolRegCondition = "disabled='disabled'";
						%>
					<td class="formFieldWithoutBorder" width="5">
						<html:hidden property="<%=collectionProtocolIdentifier%>" />
						<input type=checkbox name="<%=collectionProtocolCheck %>" id="<%=collectionProtocolCheck %>" <%=CollectionProtocolRegCondition%> onClick="javascript:enableButton(document.forms[0].deleteParticipantRegistrationValue,document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_')"> 		
					</td>
				 </tr>
				 <%
				}
				%>
				 </tbody>
				 </table>
				</td>
			 	</tr>
				 
				 
				  
<!-- Participant Registration End here -->
				  
				  <tr><td colspan=7>&nbsp;</td></tr>
				  
				  <!---Following is the code for Data Grid. Participant Lookup Data is displayed-->
				<%if(request.getAttribute(Constants.SPREADSHEET_DATA_LIST)!=null && dataList.size()>0){
					isRegisterButton=true;
					if(request.getAttribute(Constants.SUBMITTED_FOR)!=null && request.getAttribute(Constants.SUBMITTED_FOR).equals("AddNew")){
						isRegisterButton=false;
					}%>	
				
			
				<tr><td colspan="7"><table summary="" cellpadding="0" cellspacing="0" border="0">
				<tr>
				     <td class="formTitle" height="25">
				     	<bean:message key="participant.lookup"/>
				     </td>
       		    </tr>				
	  			<tr height=110 valign=top>
					<td valign=top class="formFieldAllBorders">
<!--  **************  Code for New Grid  *********************** -->
			<script>
					function participant(id)
					{
						//do nothing
						//mandar for grid
						var cl = mygrid.cells(id,mygrid.getColumnCount()-1);
						var pid = cl.getValue();
						//alert(pid);
						/* 
							 Resolved bug# 4240
	                    	 Name: Virender Mehta
	                    	 Reviewer: Sachin Lale
	                    	 Description: removed URL On  onclick
	                     */
						 document.forms[0].submittedFor.value = "AddNew";
						 var pageOf = "<%=pageOf%>";
						if(pageOf == "<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>")
						{
							window.location.href = 'CPQueryParticipantSelect.do?submittedFor=AddNew&operation=add&participantId='+pid
						}
						else
						{
							window.location.href = 'ParticipantLookup.do?submittedFor=AddNew&operation=add&participantId='+pid
						}						
					} 				

					/* 
						to be used when you want to specify another javascript function for row selection.
						useDefaultRowClickHandler =1 | any value other than 1 indicates you want to use another row click handler.
						useFunction = "";  Function to be used. 	
					*/
					var useDefaultRowClickHandler =2;
					var useFunction = "participant";	
			</script>
			<%@ include file="/pages/content/search/AdvanceGrid.jsp" %>
<!--  **************  Code for New Grid  *********************** -->

					</td>
				  </tr>
				  <tr>
				 	<td align="center" colspan="7" class="formFieldWithNoTopBorder">
						<INPUT TYPE='RADIO' NAME='chkName' value="Add" onclick="CreateNewClick()"><font size="2">Ignore matches and create new participant </font></INPUT>&nbsp;&nbsp;
						<INPUT TYPE='RADIO' NAME='chkName' value="Lookup" onclick="LookupAgain()" checked=true><font size="2">Lookup again </font></INPUT>
					</td>
				</tr>		
				</table></td></tr>
				<%}%>
				<!--Participant Lookup end-->				
								 <!-----action buttons-->
				 <tr>
				 	<td align="right" colspan="7" valign="top">
						<%
							String changeAction = "setFormAction('"+formName+"')";
						%>
						<!-- action buttons begins -->

						<table cellpadding="4" cellspacing="0" border="0">
							<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
							<% 
								isAddNew=true;
							%>
							</logic:equal>
							
							<tr>
								<%--
									String normalSubmitFunctionName = "setSubmittedForParticipant('" + submittedFor+ "','" + Constants.PARTICIPANT_FORWARD_TO_LIST[0][1]+"')";
									String forwardToSubmitFunctionName = "setSubmittedForParticipant('ForwardTo','" + Constants.PARTICIPANT_FORWARD_TO_LIST[1][1]+"')";									
									String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
									String normalSubmit = normalSubmitFunctionName + ","+confirmDisableFuncName;
									String forwardToSubmit = forwardToSubmitFunctionName + ","+confirmDisableFuncName;
								--%>
								<%
								    
									String normalSubmitFunctionName = "setSubmittedForParticipant('" + submittedFor+ "','" + Constants.PARTICIPANT_FORWARD_TO_LIST[0][1]+"')";
									String forwardToSubmitFunctionName = "setSubmittedForParticipant('ForwardTo','" + Constants.PARTICIPANT_FORWARD_TO_LIST[3][1]+"')";
									String forwardToSCGFunctionName = "setSubmittedForParticipant('ForwardTo','" + Constants.PARTICIPANT_FORWARD_TO_LIST[2][1]+"')";
									String normalSubmit = normalSubmitFunctionName ;
									String forwardToSubmit = forwardToSubmitFunctionName ;
									String forwardToSCG = forwardToSCGFunctionName ;

								%>
																
								<!-- PUT YOUR COMMENT HERE -->

								
								<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
								<td nowrap class="formFieldNoBorders">									
									<html:button styleClass="actionButton"  
											property="registratioPage" 
											title="Register Participant"
											value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>"
											onclick="<%=forwardToSubmit%>">
									</html:button>
								</td>
								</logic:equal>
								
								<logic:notEqual name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
								<td nowrap class="formFieldNoBorders">									
									<html:button styleClass="actionButton"  
											property="registratioPage" 
											title="Submit Only"
											value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>"
											onclick="<%=normalSubmit%>">
									</html:button>
								</td>
								</logic:notEqual>
								
								<logic:equal name="<%=Constants.PAGEOF%>" value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
								<td nowrap class="formFieldNoBorders">									
									<html:button styleClass="actionButton"  
											property="registratioPage" 
											value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[2][0]%>"
											onclick="<%=forwardToSCG%>"
											onmouseover="showMessage('Create additional Specimen Collection Group to collect specimens which were  not anticipated as per protocol')">
									</html:button>
								</td>
								</logic:equal>
							
								
								<%--<td>
									<html:submit styleClass="actionButton" disabled="true">
							   		<bean:message key="buttons.getClinicalData"/>
									</html:submit>
								</td>	--%>
							</tr>
						</table>
							<!-- action buttons end -->
			  		</td>
			 	 </tr>
								 
				<!--	extra </logic:notEqual>-->
				
				 <!-- end --> 
			</table>
		</td></tr>
	</table>			
	</div>
</body>
