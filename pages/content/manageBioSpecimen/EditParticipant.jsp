<link rel="stylesheet" type="text/css" href="css/catissue_suite.css" />

<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<script language="JavaScript" src="jss/script.js" type="text/javascript"></script>
<%
	String[] activityStatusList = (String[]) request.getAttribute(Constants.ACTIVITYSTATUSLIST);
%>

<%
		if (pageOf.equals(Constants.PAGE_OF_PARTICIPANT_CP_QUERY)) {
		strCheckStatus = "checkActivityStatus(this,'"
		+ Constants.CP_QUERY_BIO_SPECIMEN + "')";
	}
%>


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
			//first Cell
			var cprCheckb=row.insertCell(0);
			cprCheckb.className="black_ar";
			sname="";
			
			var identifier = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) +"_id)";
			sname = sname + "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			
			var name = "CollectionProtocolRegistrationChk_"+(cprSize+1);
			sname = sname +"<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteParticipantRegistrationValue,document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_')\">";
			cprCheckb.innerHTML=""+sname;
			
			// Second Cell
			var cprTitle=row.insertCell(1);
			cprTitle.className="black_ar_s";
			sname="";
			var name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_CollectionProtocol_id)";
			var keyValue = name;
			sname = sname +"<select name='" + name + "' size='1' class='formFieldSized12' id='" + name + "' onmouseover=showTip(this.id) onmouseout=hideTip(this.id)>";
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
			
			//third Cell
			var cprParticipantId=row.insertCell(2);
			cprParticipantId.className="black_ar";
			sname="";
			name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_protocolParticipantIdentifier)";
			sname="<input type='text' name='" + name + "' maxlength='30' size='15' class='black_ar' id='" + name + "'>";
			cprParticipantId.innerHTML="" + sname;
			
			<%
				String registrationDate = Utility.parseDateToString(Calendar.getInstance().getTime(), Constants.DATE_PATTERN_MM_DD_YYYY);
    		%>
    		
			//fourth Cell
			var cprRegistrationDate=row.insertCell(3);
			cprRegistrationDate.className="black_ar";
			cprRegistrationDate.colSpan=1;
			sname="";
			var name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_registrationDate)";
			//sname = "<input type='text' name='" + name + "' class='formFieldSized15' id='" + name + "' value = 'MM-DD-YYYY or MM/DD/YYYY' onclick = \"this.value = ''\" onblur = \"if(this.value=='') {this.value = 'MM-DD-YYYY or MM/DD/YYYY';}\" onkeypress=\"return titliOnEnter(event, this, document.getElementById('" + name + "'))\">";
			sname = "<input type='text' name='" + name + "' maxlength='30' size='10' class='black_ar' id='" + name + "' value = '<%=registrationDate%>'>";
			cprRegistrationDate.innerHTML=sname;
			
			//Fifth Cell
			var cprActivityStatus=row.insertCell(4);
			cprActivityStatus.className="black_ar_s";
			sname="";
			var name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) +"_activityStatus)";
			sname = sname +"<select name='" + name + "' size='1' class='formFieldSized8' id='" + name + "' disabled='disabled' onmouseover=showTip(this.id) onmouseout=hideTip(this.id) >";
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
											
			//sixth Cell
			var consent=row.insertCell(5);
			consent.className="black_ar";
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
   
</script>



<script language="JavaScript" type="text/javascript"
	src="jss/javaScript.js"></script>
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />

<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="newMaintable">
	<tr>
		<td><input type="hidden" name="participantId"
			value="<%=participantId%>" /> <input type="hidden" name="cpId"
			id="cpId" /> <input type="hidden" name="radioValue" /> <html:hidden
			property="<%=Constants.OPERATION%>" value="<%=operation%>" /> <html:hidden
			property="submittedFor" value="<%=submittedFor%>" /> <html:hidden
			property="forwardTo" value="<%=forwardTo%>" /></td>
		<td><html:hidden property="valueCounter" /></td>
		<td><html:hidden
			property="collectionProtocolRegistrationValueCounter" /></td>
		<td><html:hidden property="onSubmit" /></td>
		<td><html:hidden property="id" /> <html:hidden
			property="redirectTo" /></td>
		<td><html:hidden property="pageOf" value="<%=pageOf%>" /></td>
	</tr>
	<tr>
		<td class="td_color_bfdcf3">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="whitetable_bg">
			<logic:equal name="operation" value="add">
				<tr>
					<td width="100%" colspan="2" valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="3" valign="top" class="td_color_bfdcf3">
							<table width="15%" border="0" cellpadding="0" cellspacing="0"
								background="images/uIEnhancementImages/table_title_bg.gif">
								<tr>
									<td width="74%"><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
									<bean:message key="app.participant" /> </span></td>
									<td width="26%" align="right"><img
										src="images/uIEnhancementImages/table_title_corner2.gif"
										width="31" height="24" /></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;</td>
							<td width="9%" valign="top" class="td_tab_bg">&nbsp;</td>
							<td width="90%" valign="bottom" class="td_color_bfdcf3"
								style="padding-top:4px;">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="4%" class="td_tab_bg">&nbsp;</td>
									<!-- for tabs selection -->
									<logic:equal name="operation" value="add">
										<td width="6%" valign="bottom"
											background="images/uIEnhancementImages/tab_bg.gif"><img
											src="images/uIEnhancementImages/tab_add_selected.jpg"
											alt="Add" width="57" height="22" /></td>
										<td width="6%" valign="bottom"
											background="images/uIEnhancementImages/tab_bg.gif"><html:link
											page="/SimpleQueryInterface.do?pageOf=pageOfParticipant&aliasName=Participant&menuSelected=5">
											<img
												src="images/uIEnhancementImages/tab_edit_notSelected.jpg"
												alt="Edit" width="59" height="22" border="0" />
										</html:link></td>
										<td width="15%" valign="bottom"
											background="images/uIEnhancementImages/tab_bg.gif">&nbsp;
										</td>
									</logic:equal>
									<logic:equal name="operation" value="edit">
										<td width="6%" valign="bottom"
											background="images/uIEnhancementImages/tab_bg.gif"><html:link
											page="/Site.do?operation=add&pageOf=pageOfSite&menuSelected=5">
											<img src="images/uIEnhancementImages/tab_add_notSelected.jpg"
												alt="Add" width="57" height="22" border="0" />
										</html:link></td>
										<td width="6%" valign="bottom"
											background="images/uIEnhancementImages/tab_bg.gif"><img
											src="images/uIEnhancementImages/tab_edit_selected.jpg"
											alt="Edit" width="59" height="22" /></td>
										<td width="15%" valign="bottom"
											background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>
									</logic:equal>
									<td width="65%" valign="bottom" class="td_tab_bg">&nbsp;</td>
									<td width="1%" align="left" valign="bottom"
										class="td_color_bfdcf3">&nbsp;</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</logic:equal>
			<tr>
				<td colspan="2" class="td_color_bfdcf3"
					style="padding-left:10px; padding-right:10px; padding-bottom:10px;">
				<table width="100%" border="0" cellpadding="3" cellspacing="0"
					bgcolor="#FFFFFF">
					<tr>
						<td height="15" colspan="2" align="left"></td>
					</tr>
					<tr>
						<td height="25" align="left" class="tr_bg_blue1"><span
							class="blue_ar_b">&nbsp;<bean:message
							key="participant.details" /></span></td>
						<td height="25" align="left" class="tr_bg_blue1">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2" align="left"
							style="padding-top:10px; padding-bottom:15px; padding-left:6px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="3">
							<tr>
								<td class=" grey_ar_s" width="1%">&nbsp;</td>
								<td valign="middle" width="17%"><label
									for="socialSecurityNumber" class="black_ar"> <bean:message
									key="participant.socialSecurityNumber" /> </label></td>
								<td width="82%"><html:text styleClass="black_ar" size="3"
									maxlength="3" styleId="socialSecurityNumberPartA"
									property="socialSecurityNumberPartA"
									readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);"
									onchange="intOnly(this);"
									onkeyup="intOnly(this);moveToNext(this,this.value,'socialSecurityNumberPartB');" />
								- <html:text styleClass="black_ar" size="3" maxlength="2"
									styleId="socialSecurityNumberPartB"
									property="socialSecurityNumberPartB"
									readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);"
									onchange="intOnly(this);"
									onkeyup="intOnly(this);moveToNext(this,this.value,'socialSecurityNumberPartC');" />
								- <html:text styleClass="black_ar" size="3" maxlength="4"
									styleId="socialSecurityNumberPartC"
									property="socialSecurityNumberPartC"
									readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);"
									onchange="intOnly(this);" onkeyup="intOnly(this);" /></td>
							</tr>
							<tr>
								<td class=" grey_ar_s" width="1%">&nbsp;</td>
								<td valign="middle"><label for="Name" class="black_ar">
								<bean:message key="participant.Name" /> </label></td>
								<td>
								<table border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="90" align="center" class="black_ar"><bean:message
											key="participant.lastName" /></td>

										<td width="10" align="center" class="black_ar"></td>
										<td width="90" align="center" class="black_ar"><bean:message
											key="participant.firstName" /></td>
										<td width="10" align="left" class="black_ar"></td>
										<td width="90" align="center" class="black_ar"><bean:message
											key="participant.middleName" /></td>
									</tr>
									<tr>
										<td><html:text styleClass="black_ar" maxlength="255"
											size="15" styleId="lastName" name="participantForm"
											property="lastName" readonly="<%=readOnlyForAll%>"
											onkeyup="moveToNext(this,this.value,'firstName')" /></td>
										<td width="1"></td>
										<td><html:text styleClass="black_ar" maxlength="255"
											size="15" styleId="firstName" property="firstName"
											readonly="<%=readOnlyForAll%>"
											onkeyup="moveToNext(this,this.value,'middleName')" /></td>
										<td width="1"></td>
										<td><html:text styleClass="black_ar" maxlength="255"
											size="15" styleId="middleName" property="middleName"
											readonly="<%=readOnlyForAll%>" /></td>
									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td class=" grey_ar_s" width="1%">&nbsp;</td>
								<td valign="middle"><label for="birthDate" class="black_ar">
								<bean:message key="participant.birthDate" /> </label></td>
								<td align="left" valign="top">
								<%
										if (currentBirthDate.trim().length() > 0) {
										Integer birthYear = new Integer(Utility
										.getYear(currentBirthDate));
										Integer birthMonth = new Integer(Utility
										.getMonth(currentBirthDate));
										Integer birthDay = new Integer(Utility.getDay(currentBirthDate));
								%> <ncombo:DateTimeComponent name="birthDate" id="birthDate"
									formName="participantForm" month="<%=birthMonth %>"
									year="<%=birthYear %>" day="<%= birthDay %>"
									value="<%=currentBirthDate %>" styleClass="black_ar" /> <%
									 } else {
								%> <ncombo:DateTimeComponent name="birthDate" id="birthDate"
									formName="participantForm" styleClass="black_ar" /> <%
								 }
								 %> <span class="grey_ar_s"> <bean:message
									key="page.dateFormat" /> </span>&nbsp;</td>
							</tr>
							<tr>
								<td class=" grey_ar_s" width="1%">&nbsp;</td>
								<td valign="middle"><label for="vitalStatus"
									class="black_ar"> <bean:message
									key="participant.vitalStatus" /> </label></td>
								<td valign="middle" class="black_ar"><logic:iterate
									id="nvb" name="<%=Constants.VITAL_STATUS_LIST%>">
									<%
										NameValueBean nameValueBean = (NameValueBean) nvb;
										%>
									<html:radio property="vitalStatus"
										onclick="onVitalStatusRadioButtonClick(this)"
										value="<%=nameValueBean.getValue()%>">
										<%=nameValueBean.getName()%>
									</html:radio>
								</logic:iterate></td>
							</tr>
							<tr>
								<td class=" grey_ar_s" width="1%">&nbsp;</td>
								<td valign="middle"><span class="black_ar"> <bean:message
									key="participant.deathDate" /> </span></td>
								<td valign="middle">
								<%
									ParticipantForm form = (ParticipantForm) request
											.getAttribute("participantForm");
									Boolean deathDisable = new Boolean("false");
									if (!form.getVitalStatus().trim().equals("Dead")) {
										deathDisable = new Boolean("true");
									}
									if (currentDeathDate.trim().length() > 0) {
										Integer deathYear = new Integer(Utility
										.getYear(currentDeathDate));
										Integer deathMonth = new Integer(Utility
										.getMonth(currentDeathDate));
										Integer deathDay = new Integer(Utility.getDay(currentDeathDate));
								%> <ncombo:DateTimeComponent name="deathDate" id="deathDate"
									formName="participantForm" month="<%=deathMonth %>"
									year="<%=deathYear %>" day="<%= deathDay %>"
									value="<%=currentDeathDate %>" styleClass="black_ar"
									disabled="<%=deathDisable%>" /> <%
									 } else {
									 %> <ncombo:DateTimeComponent name="deathDate" id="deathDate"
									formName="participantForm" styleClass="black_ar"
									disabled="<%=deathDisable%>" /> <%
									 }
								  %> <span class="grey_ar_s"> <bean:message
									key="page.dateFormat" /> </span>&nbsp;</td>
							</tr>
							<tr>
								<td class=" grey_ar_s" width="1%">&nbsp;</td>
								<td valign="middle"><label for="gender" class="black_ar">
								<bean:message key="participant.gender" /> </label></td>
								<td valign="middle" class="black_ar"><logic:iterate
									id="nvb" name="<%=Constants.GENDER_LIST%>">
									<%
										NameValueBean nameValueBean = (NameValueBean) nvb;
										%>
									<html:radio property="gender"
										value="<%=nameValueBean.getValue()%>">
										<%=nameValueBean.getName()%>
									</html:radio>
								</logic:iterate></td>
							</tr>
							<tr>
								<td class=" grey_ar_s" width="1%">&nbsp;</td>
								<td valign="middle"><label for="genotype" class="black_ar">
								<bean:message key="participant.genotype" /> </label></td>
								<td valign="middle" class="black_ar_s"><autocomplete:AutoCompleteTag
									property="genotype"
									optionsList="<%=request.getAttribute(Constants.GENOTYPE_LIST)%>"
									initialValue="<%=form.getGenotype()%>"
									styleClass="formFieldSized12" /></td>
							</tr>
							<tr>
								<td class=" grey_ar_s" width="1%">&nbsp;</td>
								<td valign="middle"><label for="race" class="black_ar">
								<bean:message key="participant.race" /> </label></td>
								<td valign="middle" class="black_ar"><html:select
									property="raceTypes" styleClass="formFieldSized18"
									styleId="race" size="4" multiple="true"
									disabled="<%=readOnlyForAll%>" onmouseover="showTip(this.id)"
									onmouseout="hideTip(this.id)">
									<html:options collection="<%=Constants.RACELIST%>"
										labelProperty="name" property="value" />
								</html:select></td>
							</tr>
							<tr>
								<td class="grey_ar_s" width="1%">&nbsp;</td>
								<td valign="middle"><label for="ethnicity"
									class="black_ar"> <bean:message
									key="participant.ethnicity" /> </label></td>
								<td valign="middle" class="black_ar_s"><autocomplete:AutoCompleteTag
									property="ethnicity"
									optionsList="<%=request.getAttribute(Constants.ETHNICITY_LIST)%>"
									initialValue="<%=form.getEthnicity()%>"
									styleClass="formFieldSized12" /></td>
							</tr>

							<!-- activitystatus -->
							<logic:equal name="<%=Constants.OPERATION%>"
								value="<%=Constants.EDIT%>">
								<tr>
									<td class="grey_ar_s" width="1%"><img
										src="images/uIEnhancementImages/star.gif" alt="Mandatory"
										width="6" height="6" hspace="0" vspace="0" /></td>
									<td valign="middle"><label for="activityStatus"
										class="black_ar"> <b><bean:message
										key="participant.activityStatus" /></b> </label></td>
									<td class="black_ar_s"><html:select
										property="activityStatus" styleClass="formFieldSized12"
										styleId="activityStatus" size="1"
										onchange="<%=strCheckStatus%>" onmouseover="showTip(this.id)"
										onmouseout="hideTip(this.id)">
										<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>"
											labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
									</html:select></td>
								</tr>
							</logic:equal>
						</table>
						</td>
					</tr>
					<!-- Medical Identifiers Begin here -->
					<tr onclick="javascript:showHide('add_medical_identifier')">
						<td height="25" align="left" class="tr_bg_blue1"><span
							class="blue_ar_b">&nbsp; <bean:message
							key="participant.medicalIdentifier" /> </span></td>
						<td height="25" align="right" class="tr_bg_blue1"><a href="#"
							id="imgArrow_add_medical_identifier"> <img
							src="images/uIEnhancementImages/dwn_arrow1.gif" width="7"
							height="8" hspace="10" border="0" class="tr_bg_blue1" /> </a></td>
					</tr>
					<tr>
						<td colspan="2" style="padding-top:10px;">
						<div id="add_medical_identifier" style="display:none">
						<table width="100%" border="0" cellspacing="0" cellpadding="3">
							<tr class="tableheading">
								<td width="9%" align="left" class="black_ar_b"><bean:message
									key="app.select" /></td>
								<td width="23%" align="left" class="black_ar_b"><bean:message
									key="medicalrecord.source" /></td>
								<td class="black_ar_b"><bean:message
									key="medicalrecord.number" /></td>
							</tr>

							<script> document.forms[0].valueCounter.value = <%=noOfRows%> </script>
							<tbody id="addMore">
								<%
										for (int i = 1; i <= noOfRows; i++) {
										String siteName = "value(ParticipantMedicalIdentifier:" + i
										+ "_Site_id)";
										String medicalRecordNumber = "value(ParticipantMedicalIdentifier:"
										+ i + "_medicalRecordNumber)";
										String identifier = "value(ParticipantMedicalIdentifier:" + i
										+ "_id)";
										String check = "chk_" + i;
								%>
								<tr>
									<%
											String key = "ParticipantMedicalIdentifier:" + i + "_id";
											boolean bool = Utility.isPersistedValue(map, key);
											String condition = "";
											if (bool)
												condition = "disabled='disabled'";
									%>
									<td class="black_ar" width="5"><html:hidden
										property="<%=identifier%>" /> <input type=checkbox
										name="<%=check %>" id="<%=check %>" <%=condition%>
										onClick="enableButton(document.forms[0].deleteMedicalIdentifierValue,document.forms[0].valueCounter,'chk_')">
									</td>
									<td class="black_ar_s"><html:select
										property="<%=siteName%>" styleClass="formFieldSized12"
										styleId="<%=siteName%>" size="1"
										disabled="<%=readOnlyForAll%>" onmouseover="showTip(this.id)"
										onmouseout="hideTip(this.id)">
										<html:options collection="<%=Constants.SITELIST%>"
											labelProperty="name" property="value" />
									</html:select></td>
									<td class="black_ar"><html:text styleClass="black_ar"
										size="15" maxlength="50" styleId="<%=medicalRecordNumber%>"
										property="<%=medicalRecordNumber%>"
										readonly="<%=readOnlyForAll%>" /></td>
								</tr>
								<%
								}
								%>
							</tbody>

							<!-- Medical Identifiers End here -->


							<tr>
								<td align="left" class="black_ar"><html:button
									property="addKeyValue" styleClass="blue_ar_b"
									onclick="insRow('addMore')">
									<bean:message key="buttons.addMore" />
								</html:button></td>
								<td class="black_ar"><html:button
									property="deleteMedicalIdentifierValue" styleClass="blue_ar_b"
									onclick="deleteCheckedNoSubmit('addMore','Participant.do?operation=<%=operation%>&pageOf=<%=pageOf%>&status=true',document.forms[0].valueCounter,'chk_',false)"
									disabled="true">
									<bean:message key="buttons.delete" />
								</html:button></td>
								<td class="black_ar">&nbsp;</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr class="td_color_F7F7F7">
						<td height="20" colspan="2"></td>

					</tr>
					<tr class="td_color_F7F7F7"
						onclick="javascript:showHide('add_participant_registeration')">
						<td height="25" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;
						<bean:message key="participant.collectionProtocolReg" /> </span></td>
						<td align="right" class="tr_bg_blue1"><a href="#"
							id="imgArrow_add_participant_registeration"><img
							src="images/uIEnhancementImages/dwn_arrow1.gif" width="7"
							height="8" hspace="10" border="0" class="tr_bg_blue1" /> </a></td>
					</tr>
					<tr class="td_color_F7F7F7">
						<td colspan="2" style="padding-top:10px;">
						<div id="add_participant_registeration" style="display:none">
						<table width="100%" border="0" cellspacing="0" cellpadding="3">
							<tr class="tableheading">
								<td width="9%" align="left" class="black_ar_b">Select</td>
								<td width="23%" align="left" class="black_ar_b"><bean:message
									key="participant.collectionProtocolReg.protocolTitle" /></td>
								<td width="18%" align="left" class="black_ar_b"><bean:message
									key="participant.collectionProtocolReg.participantProtocolID" />
								</td>
								<td width="15%" align="left" class="black_ar_b"><bean:message
									key="participant.collectionProtocolReg.participantRegistrationDate" />
								</td>
								<td width="15%" align="left" class="black_ar_b"><bean:message
									key="participant.activityStatus" /></td>
								<td width="20%" align="left" class="black_ar_b"><bean:message
									key="participant.collectionProtocolReg.consent" /></td>
							</tr>

							<script> document.forms[0].collectionProtocolRegistrationValueCounter.value = <%=noOrRowsCollectionProtocolRegistration%> </script>
							<tbody id="addMoreParticipantRegistration">
								<%
										for (int i = 1; i <= noOrRowsCollectionProtocolRegistration; i++) {
										String collectionProtocolId = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"
										+ i + "_CollectionProtocol_id)";
										String collectionProtocolTitle = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"
										+ i + "_CollectionProtocol_shortTitle)";
										String collectionProtocolParticipantId = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"
										+ i + "_protocolParticipantIdentifier)";
										String collectionProtocolRegistrationDate = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"
										+ i + "_registrationDate)";
										String collectionProtocolIdentifier = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"
										+ i + "_id)";
										String collectionProtocolRegistrationActivityStatus = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"
										+ i + "_activityStatus)";
										String collectionProtocolCheck = "CollectionProtocolRegistrationChk_"
										+ i;
										String key = "CollectionProtocolRegistration:" + i + "_id";
										String collectionProtocolConsentCheck = "CollectionProtocolConsentChk_"
										+ i;
										String anchorTagKey = "ConsentCheck_" + i;
										String consentCheckStatus = "consentCheckStatus_" + i;

										String consentResponseDisplay = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"
										+ i + "_isConsentAvailable)";
										String consentResponseDisplayKey = "CollectionProtocolRegistration:"
										+ i + "_isConsentAvailable";
										String consentResponseDisplayValue = (String) form
										.getCollectionProtocolRegistrationValue(consentResponseDisplayKey);

										String collectionProtocolTitleKey = "CollectionProtocolRegistration:"
										+ i + "_CollectionProtocol_shortTitle";
										String collectionProtocolTitleValue = (String) form
										.getCollectionProtocolRegistrationValue(collectionProtocolTitleKey);

										String collectionProtocolIdKey = "CollectionProtocolRegistration:"
										+ i + "_CollectionProtocol_id";
										String collectionProtocolIdValue = (String) form
										.getCollectionProtocolRegistrationValue(collectionProtocolIdKey);

										String collectionProtocolRegIdValue = (String) form
										.getCollectionProtocolRegistrationValue("CollectionProtocolRegistration:"
												+ i + "_id");

										if (consentResponseDisplayValue == null) {
											consentResponseDisplayValue = Constants.NO_CONSENTS_DEFINED;
										}
										boolean CollectionProtocolRegConditionBoolean = Utility
										.isPersistedValue(mapCollectionProtocolRegistration,
												key);
										boolean activityStatusCondition = false;
										if (!CollectionProtocolRegConditionBoolean)
											activityStatusCondition = true;

										String onChangeFun = "getConsent('" + collectionProtocolCheck
										+ "','" + collectionProtocolId + "', '"
										+ collectionProtocolTitleKey + "','" + i + "','"
										+ anchorTagKey + "','" + consentCheckStatus + "')";
								%>

								<tr>
									<%
											String CollectionProtocolRegCondition = "";
											if (CollectionProtocolRegConditionBoolean)
												CollectionProtocolRegCondition = "disabled='disabled'";
									%>
									<td class="black_ar" width="5"><html:hidden
										property="<%=collectionProtocolIdentifier%>" /> <input
										type=checkbox name="<%=collectionProtocolCheck %>"
										id="<%=collectionProtocolCheck %>"
										<%=CollectionProtocolRegCondition%>
										onClick="javascript:enableButton(document.forms[0].deleteParticipantRegistrationValue,document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_')">
									</td>
									<td class="black_ar_s">
									<%
									if (CollectionProtocolRegConditionBoolean) {
									%> <html:text styleClass="black_ar" maxlength="50"
										styleId="<%=collectionProtocolTitle%>"
										property="<%=collectionProtocolTitle%>"
										readonly="<%=readOnlyValue%>" /> <input type="hidden"
										id="<%=collectionProtocolId%>"
										name="<%=collectionProtocolId%>"
										value="<%=collectionProtocolIdValue%>" /> <%
 } else {
 %> <html:select property="<%=collectionProtocolId%>"
										styleClass="formFieldSized12"
										styleId="<%=collectionProtocolId%>"
										onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)"
										onchange="<%=onChangeFun%>">
										<html:options collection="<%=Constants.PROTOCOL_LIST%>"
											labelProperty="name" property="value" />
									</html:select> <input type="hidden" id="<%=collectionProtocolTitle%>"
										name="<%=collectionProtocolTitle%>"
										value="<%=collectionProtocolTitleValue%>" /> <%
 }
 %>
									</td>
									<td class="black_ar"><html:text styleClass="black_ar"
										size="15" maxlength="50"
										styleId="<%=collectionProtocolParticipantId%>"
										property="<%=collectionProtocolParticipantId%>" /></td>
									<td class="black_ar"><html:text styleClass="black_ar"
										size="10" maxlength="50"
										styleId="<%=collectionProtocolRegistrationDate%>"
										property="<%=collectionProtocolRegistrationDate%>" /></td>
									<td class="black_ar_s"><html:select
										property="<%=collectionProtocolRegistrationActivityStatus%>"
										styleClass="formFieldSized8"
										styleId="<%=collectionProtocolRegistrationActivityStatus%>"
										size="1" disabled='<%=activityStatusCondition%>'
										onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>"
											labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
									</html:select></td>
									<td class="black_ar"><span id="<%=consentCheckStatus%>">
									<%
											if (!consentResponseDisplayValue
											.equals(Constants.NO_CONSENTS_DEFINED)) {
												if (operation.equals(Constants.EDIT)) {
											consentResponseDisplayValue = Constants.PARTICIPANT_CONSENT_EDIT_RESPONSE;
												}
									%> <a id="<%=anchorTagKey%>"
										href="javascript:openConsentPage('<%=collectionProtocolId%>','<%=i%>','<%=consentResponseDisplayValue%>','<%=collectionProtocolRegIdValue%>')">
									<%=consentResponseDisplayValue%><br>
									<input type='hidden' name="<%=collectionProtocolConsentCheck%>"
										value='Consent' id="<%=collectionProtocolConsentCheck%>">
									<input type='hidden' name="<%=consentResponseDisplay%>"
										value="<%=consentResponseDisplayValue%>"
										id="<%=consentResponseDisplay%>"> </a> <%
										 } else {
										 %> <%=consentResponseDisplayValue%> <input type='hidden'
										name="<%=collectionProtocolConsentCheck%>" value='Consent'
										id="<%=collectionProtocolConsentCheck%>"> <input
										type='hidden' name="<%=consentResponseDisplay%>"
										value="<%=consentResponseDisplayValue%>"
										id="<%=consentResponseDisplay%>"> <%
										 }
										 %> </span></td>

								</tr>
								<%
								}
								%>
							</tbody>
							<tr>
								<td align="left" class="black_ar"><html:button
									property="addKeyValue" styleClass="blue_ar_b"
									onclick="participantRegRow('addMoreParticipantRegistration')">
									<bean:message key="buttons.addMore" />
								</html:button></td>
								<td class="black_ar"><html:button
									property="deleteParticipantRegistrationValue"
									styleClass="blue_ar_b"
									onclick="deleteCheckedNoSubmit('addMoreParticipantRegistration','Participant.do?operation=<%=operation%>&pageOf=<%=pageOf%>&status=true&deleteRegistration=true',document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_',false)"
									disabled="true">
									<bean:message key="buttons.delete" />
								</html:button></td>
								<td class="black_ar">&nbsp;</td>
								<td class="black_ar">&nbsp;</td>
								<td class="black_ar">&nbsp;</td>
								<td class="black_ar">&nbsp;</td>
							</tr>
							<!-- Participant Registration End here -->
						</table>
						</td>
					</tr>
					<tr class="td_color_F7F7F7">
						<td height="20" colspan="2"></td>
					</tr>



					<!---Following is the code for Data Grid. Participant Lookup Data is displayed-->
					<%
								if (request.getAttribute(Constants.SPREADSHEET_DATA_LIST) != null
								&& dataList.size() > 0) {
							isRegisterButton = true;
							if (request.getAttribute(Constants.SUBMITTED_FOR) != null
							&& request.getAttribute(Constants.SUBMITTED_FOR)
									.equals("AddNew")) {
								isRegisterButton = false;
							}
					%>


					<tr>
						<td colspan="2">
						<table summary="" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td class="black_ar_b" height="25"><bean:message
									key="participant.lookup" /></td>
							</tr>
							<tr height=110 valign=top>
								<td valign=top class="formFieldAllBorders"><!--  **************  Code for New Grid  *********************** -->
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
			</script> <%@ include file="/pages/content/search/AdvanceGrid.jsp"%>
								<!--  **************  Code for New Grid  *********************** -->

								</td>
							</tr>
							<tr>
								<td align="center" colspan="7" class="formFieldWithNoTopBorder">
								<INPUT TYPE='RADIO' NAME='chkName' value="Add"
									onclick="CreateNewClick()"> <font size="2">Ignore
								matches and create new participant </font> </INPUT>&nbsp;&nbsp; <INPUT
									TYPE='RADIO' NAME='chkName' value="Lookup"
									onclick="LookupAgain()" checked=true> <font size="2">Lookup
								again </font> </INPUT></td>
							</tr>
						</table>
						</td>
					</tr>
					<%
					}
					%>
					<!--Participant Lookup end-->

					<!-----action buttons-->
					<tr>
						<td colspan="2" class="buttonbg" style="padding-left:10px;">
						<%
						String changeAction = "setFormAction('" + formName + "')";
						%> <!-- action buttons begins -->

						<table cellpadding="4" cellspacing="0" border="0">
							<logic:equal name="<%=Constants.SUBMITTED_FOR%>" value="AddNew">
								<%
								isAddNew = true;
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
											String normalSubmitFunctionName = "setSubmittedForParticipant('"
											+ submittedFor + "','"
											+ Constants.PARTICIPANT_FORWARD_TO_LIST[0][1] + "')";
									String forwardToSubmitFunctionName = "setSubmittedForParticipant('ForwardTo','"
											+ Constants.PARTICIPANT_FORWARD_TO_LIST[3][1] + "')";
									String forwardToSCGFunctionName = "setSubmittedForParticipant('ForwardTo','"
											+ Constants.PARTICIPANT_FORWARD_TO_LIST[2][1] + "')";
									String normalSubmit = normalSubmitFunctionName;
									String forwardToSubmit = forwardToSubmitFunctionName;
									String forwardToSCG = forwardToSCGFunctionName;
								%>

								<!-- PUT YOUR COMMENT HERE -->


								<logic:equal name="<%=Constants.PAGEOF%>"
									value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
									<td nowrap><html:button styleClass="blue_ar_b"
										property="registratioPage" title="Register Participant"
										value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>"
										onclick="<%=forwardToSubmit%>">
									</html:button>&nbsp;&nbsp;</td>
								</logic:equal>

								<logic:notEqual name="<%=Constants.PAGEOF%>"
									value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
									<td nowrap><html:button styleClass="blue_ar_b"
										property="registratioPage" title="Submit Only"
										value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>"
										onclick="<%=normalSubmit%>">
									</html:button>&nbsp;&nbsp;|&nbsp; <span class="cancellink"> <html:link
										page="/ManageAdministrativeData.do" styleClass="blue_ar_s_b">
										<bean:message key="buttons.cancel" />
									</html:link> </span></td>
								</logic:notEqual>

								<logic:equal name="<%=Constants.PAGEOF%>"
									value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
									<td nowrap><html:button styleClass="blue_ar_b"
										property="registratioPage"
										value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[2][0]%>"
										onclick="<%=forwardToSCG%>"
										onmouseover="showMessage('Create additional Specimen Collection Group to collect specimens which were  not anticipated as per protocol')">
									</html:button>&nbsp;&nbsp;</td>
								</logic:equal>
							</tr>
						</table>
						<!-- action buttons end --></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</body>
