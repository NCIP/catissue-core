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
			var cellNo = 0;
			//first Cell
			var cprCheckb=row.insertCell(cellNo);
			cprCheckb.className="black_ar";
			sname="";
			cellNo +=1;
			
			var identifier = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) +"_id)";
			sname = sname + "<input type='hidden' name='" + identifier + "' value='' id='" + identifier + "'>";
			
			var name = "CollectionProtocolRegistrationChk_"+(cprSize+1);
			sname = sname +"<input type='checkbox' name='" + name +"' id='" + name +"' value='C' onClick=\"enableButton(document.forms[0].deleteParticipantRegistrationValue,document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_')\">";
			cprCheckb.innerHTML=""+sname;
			
			// Second Cell
			var cprTitle=row.insertCell(cellNo);
			cprTitle.className="black_ar";
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
			cellNo +=1;
			
			//third Cell
			var cprParticipantId=row.insertCell(cellNo);
			cprParticipantId.className="black_ar";
			sname="";
			name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_protocolParticipantIdentifier)";
			sname="<input type='text' name='" + name + "' maxlength='30' size='10' class='black_ar' id='" + name + "'>";
			cprParticipantId.innerHTML="" + sname;
			cellNo +=1;
			
			<%if((!Variables.isSpecimenCollGroupBarcodeGeneratorAvl) || operation.equals(Constants.EDIT))
			{%>
						
			//fourth Cell
			var cprBarcode=row.insertCell(cellNo);
			cprBarcode.className="black_ar";
			sname="";
			name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_barcode)";
			sname="<input type='text' name='" + name + "' maxlength='30' size='10' class='black_ar' id='" + name + "'>";
			cprBarcode.innerHTML="" + sname;
			cellNo +=1;
			<%}%>
			
			<%
				String registrationDate = Utility.parseDateToString(Calendar.getInstance().getTime(), Constants.DATE_PATTERN_MM_DD_YYYY);
    		%>
    		
			//Fifth Cell
			var cprRegistrationDate=row.insertCell(cellNo);
			cprRegistrationDate.className="black_ar";
			cprRegistrationDate.colSpan=1;
			sname="";
			var name = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:" + (cprSize+1) + "_registrationDate)";
			//sname = "<input type='text' name='" + name + "' class='formFieldSized15' id='" + name + "' value = 'MM-DD-YYYY or MM/DD/YYYY' onclick = \"this.value = ''\" onblur = \"if(this.value=='') {this.value = 'MM-DD-YYYY or MM/DD/YYYY';}\" onkeypress=\"return titliOnEnter(event, this, document.getElementById('" + name + "'))\">";
			sname = "<input type='text' name='" + name + "' maxlength='30' size='10' class='black_ar' id='" + name + "' value = '<%=registrationDate%>'>";
			cprRegistrationDate.innerHTML=sname;
			cellNo +=1;
			
			//Sixth Cell
			var cprActivityStatus=row.insertCell(cellNo);
			cprActivityStatus.className="black_ar";
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
			cellNo +=1;
											
			//Seventh Cell
			var consent=row.insertCell(cellNo);
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
	class="maintable" height="100%"><!-- Mandar 6Nov08 -->
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


	
		<logic:notEqual name="<%=Constants.PAGEOF%>"
							value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
		<tr>
			<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_table_head"><span class="wh_ar_b"><bean:message
						key="app.participant" /></span></td>
					<td><img
						src="images/uIEnhancementImages/table_title_corner2.gif"
						alt="Page Title - Participant" width="31" height="24" /></td>
				</tr>
			</table>
			</td>
		</tr>
		</logic:notEqual>
	

	<tr height="98%">
		<td class="tablepadding">
		<logic:equal name="operation" value="add">
			<logic:notEqual name="<%=Constants.PAGEOF%>"
							value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_tab_bg"><img
						src="images/uIEnhancementImages/spacer.gif" alt="spacer"
						width="50" height="1"></td>

					<td valign="bottom"><img
						src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add"
						width="57" height="22" /></td>
					<td valign="bottom"><html:link
						page="/SimpleQueryInterface.do?pageOf=pageOfParticipant&aliasName=Participant&menuSelected=5">
						<img src="images/uIEnhancementImages/tab_edit_notSelected.jpg"
							alt="Edit" width="59" height="22" border="0" />
					</html:link></td>
			
					<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
				</tr>
			</table>
				</logic:notEqual>

		</logic:equal>

		<!--for Edit-->
			
			<logic:equal name="operation" value="edit">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="td_tab_bg"><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
					<td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_participant2.gif" alt="Edit Participant" width="116" height="22" border="0"></td>
					<td valign="bottom"><html:link href="#" onclick="viewSPR()"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="View Surgical Pathology Report" width="216" height="22" border="0"></html:link></td>
					<td valign="bottom" ><html:link href="#" onclick="showAnnotations()"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22"  border="0"></html:link></td><td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td></tr></table>
			</logic:equal>
		<table width="100%" border="0" cellpadding="3" cellspacing="0"
			class="whitetable_bg" height="95%">
					
			<tr>
				<td colspan="2" align="left" class="bottomtd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
			</tr>
			
			<tr>
				<td colspan="2" align="left" class="tr_bg_blue1"><span
					class="blue_ar_b">&nbsp;<bean:message
					key="participant.details" /></span></td>
			</tr>
			<tr>
				<td colspan="2" align="left" class="showhide" height="100%">
				<table width="100%" border="0" cellspacing="0" cellpadding="3" height="100%">
					<!-- Added by Geeta -->
					<% if(!Variables.isSSNRemove) {%>
						<tr>
							<td width="1%" align="center" class="black_ar">&nbsp;</td>
							<td width="17%"><label for="socialSecurityNumber"
								class="black_ar"> <bean:message
								key="participant.socialSecurityNumber" /> <br />
							</label></td>
							<td width="82%"><html:text styleClass="black_ar" size="3"
								maxlength="3" styleId="socialSecurityNumberPartA"
								property="socialSecurityNumberPartA"
								readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);"
								onchange="intOnly(this);"
								onkeyup="intOnly(this);moveToNext(this,this.value,'socialSecurityNumberPartB');"
								style="text-align:right" /> - <html:text styleClass="black_ar"
								size="2" maxlength="2" styleId="socialSecurityNumberPartB"
								property="socialSecurityNumberPartB"
								readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);"
								onchange="intOnly(this);"
								onkeyup="intOnly(this);moveToNext(this,this.value,'socialSecurityNumberPartC');"
								style="text-align:right" /> - <html:text styleClass="black_ar"
								size="4" maxlength="4" styleId="socialSecurityNumberPartC"
								property="socialSecurityNumberPartC"
								readonly="<%=readOnlyForAll%>" onkeypress="intOnly(this);"
								onchange="intOnly(this);" onkeyup="intOnly(this);"
								style="text-align:right" /></td>
						</tr>
					<%}%>
					<tr>
						<td width="1%" align="center" class="black_ar">&nbsp;</td>
						<td class="black_ar"><bean:message
							key="participant.Name" /> </td>
						<td>
						<table width="35%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td class="black_ar"><bean:message
											key="participant.lastName" /></br>
								
								<html:text styleClass="black_ar" maxlength="255"
											size="15" styleId="lastName" name="participantForm"
											property="lastName" readonly="<%=readOnlyForAll%>"
											onkeyup="moveToNext(this,this.value,'firstName')" /></td>
								<td width="2">&nbsp;</td>
								<td  class="black_ar"><bean:message
											key="participant.firstName" /></br><html:text styleClass="black_ar" maxlength="255"
											size="15" styleId="firstName" property="firstName"
											readonly="<%=readOnlyForAll%>"
											onkeyup="moveToNext(this,this.value,'middleName')" /></td>
								<td width="2">&nbsp;</td>
								<td  class="black_ar"><bean:message
											key="participant.middleName" /></br><html:text styleClass="black_ar" maxlength="255"
											size="15" styleId="middleName" property="middleName"
											readonly="<%=readOnlyForAll%>" /></td>

							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td width="1%" align="center" class="black_ar">&nbsp;</td>
						<td><label for="birthDate" class="black_ar"><bean:message
							key="participant.birthDate" /></label></td>
						<td>
						<%
										if (currentBirthDate.trim().length() > 0) {
										Integer birthYear = new Integer(Utility
										.getYear(currentBirthDate));
										Integer birthMonth = new Integer(Utility
										.getMonth(currentBirthDate));
										Integer birthDay = new Integer(Utility.getDay(currentBirthDate));
								%> <ncombo:DateTimeComponent name="birthDate" id="birthDate"
							formName="participantForm" month="<%=birthMonth %>"
							year="<%=birthYear %>" day="<%= birthDay %>" pattern="<%=Variables.dateFormat%>"
							value="<%=currentBirthDate %>" styleClass="black_ar" /> <%
									 } else {
								%> <ncombo:DateTimeComponent name="birthDate" id="birthDate"
							formName="participantForm" pattern="<%=Variables.dateFormat%>" styleClass="black_ar" /> <%
								 }
								 %> <span class="grey_ar_s"> <bean:message
							key="page.dateFormat" /> </span>&nbsp;</td>
					</tr>
					<tr>
						<td width="1%" align="center" class="black_ar">&nbsp;</td>
						<td><label for="vitalStatus" class="black_ar"><bean:message
							key="participant.vitalStatus" /></label></td>

						<td class="black_ar"><logic:iterate id="nvb"
							name="<%=Constants.VITAL_STATUS_LIST%>">
							<%
										NameValueBean nameValueBean = (NameValueBean) nvb;
										%>
							<html:radio property="vitalStatus"
								onclick="onVitalStatusRadioButtonClick(this)"
								value="<%=nameValueBean.getValue()%>">
								<%=nameValueBean.getName()%>
							</html:radio>&nbsp;&nbsp;&nbsp;
								</logic:iterate></td>

					</tr>
					<tr>
						<td width="1%" align="center" class="black_ar">&nbsp;</td>
						<td class="black_ar"><bean:message
							key="participant.deathDate" /></td>

						<td>
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
							year="<%=deathYear %>" day="<%= deathDay %>" pattern="<%=Variables.dateFormat%>"
							value="<%=currentDeathDate %>" styleClass="black_ar"
							disabled="<%=deathDisable%>" /> <%
									 } else {
									 %> <ncombo:DateTimeComponent name="deathDate" id="deathDate"
							formName="participantForm"  pattern="<%=Variables.dateFormat%>" styleClass="black_ar"
							disabled="<%=deathDisable%>" /> <%
									 }
								  %> <span class="grey_ar_s"> <bean:message
							key="page.dateFormat" /> </span>&nbsp;</td>
					</tr>
					<tr>
						<td width="1%" align="center" class="black_ar">&nbsp;</td>
						<td class="black_ar"><bean:message
							key="participant.gender" /></td>
						<td class="black_ar"><logic:iterate id="nvb"
							name="<%=Constants.GENDER_LIST%>">
							<%
										NameValueBean nameValueBean = (NameValueBean) nvb;
										%>
							<html:radio property="gender"
								value="<%=nameValueBean.getValue()%>">
								<%=nameValueBean.getName()%>
							</html:radio>&nbsp; &nbsp;
								</logic:iterate></td>
					</tr>
				<%if(!Variables.isSexGenoTypeRemove) {%>
					<tr>
						<td width="1%" align="center" class="black_ar">&nbsp;</td>
						<td class="black_ar"><bean:message
							key="participant.genotype" /> </td>


						<td class="black_ar"><label><autocomplete:AutoCompleteTag
							property="genotype"
							optionsList="<%=request.getAttribute(Constants.GENOTYPE_LIST)%>"
							initialValue="<%=form.getGenotype()%>"
							styleClass="black_ar" size="27"/></label></td>

					</tr>
				<%}%>
				<% if(!Variables.isRaceRemove) {%>
					<tr>
						<td width="1%" align="center" class="black_ar">&nbsp;</td>
						<td class="black_ar_t"><bean:message
							key="participant.race" /></td>

						<td class="black_ar"><html:select property="raceTypes"
							styleClass="formFieldSizedNew" styleId="race" size="4"
							multiple="true" disabled="<%=readOnlyForAll%>"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options collection="<%=Constants.RACELIST%>"
								labelProperty="name" property="value" />
						</html:select></td>
					</tr>
				<%}%>
				<% if(!Variables.isEthnicityRemove){%>
					<tr>
						<td width="1%" align="center" class="black_ar">&nbsp;</td>
						<td><span class="black_ar"><bean:message
							key="participant.ethnicity" /></span></td>
						<td class="black_ar"><label><autocomplete:AutoCompleteTag
							property="ethnicity"
							optionsList="<%=request.getAttribute(Constants.ETHNICITY_LIST)%>"
							initialValue="<%=form.getEthnicity()%>"
							styleClass="black_ar" size="27" /></label></td>
					</tr>
			   <%}%>
					<!-- activitystatus -->
					<logic:equal name="<%=Constants.OPERATION%>"
						value="<%=Constants.EDIT%>">
						<tr>
							<td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
						
							<td valign="middle"><label for="activityStatus"
								class="black_ar"><bean:message
								key="participant.activityStatus" /></label></td>
							<td class="black_ar_s"><html:select
								property="activityStatus" styleClass="formFieldSizedNew"
								styleId="activityStatus" size="1" onchange="<%=strCheckStatus%>"
								onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
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
				<td width="90%" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="participant.medicalIdentifier" /> </span></td>
				<td width="10%" align="right" class="tr_bg_blue1"><a href="#"
					id="imgArrow_add_medical_identifier">				
					<img src="images/uIEnhancementImages/dwn_arrow1.gif" alt="Show Details"
					width="80" height="9" hspace="10" border="0" />					
				</a></td>
			</tr>
			<tr>
				<td colspan="2" class="showhide1">				
				<div id="add_medical_identifier" style="display:none">			
				<table width="100%" border="0" cellspacing="0" cellpadding="3">

					<tr class="tableheading">
						<td width="8%" align="left" class="black_ar_b"><bean:message
							key="app.select" /></td>
						<td width="24%" align="left" class="black_ar_b"><bean:message
							key="medicalrecord.source" /></td>
						<td width="68%" align="left" class="black_ar_b"><bean:message
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
							<td align="left" class="black_ar"><html:hidden
								property="<%=identifier%>" /> <input type="checkbox"
								name="<%=check %>" id="<%=check %>" <%=condition%>
								onClick="enableButton(document.forms[0].deleteMedicalIdentifierValue,document.forms[0].valueCounter,'chk_')">
							</td>
							<td nowrap class="black_ar"><html:select
								property="<%=siteName%>" styleClass="formFieldSized12"
								styleId="<%=siteName%>" size="1" disabled="<%=readOnlyForAll%>"
								onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.SITELIST%>"
									labelProperty="name" property="value" />
							</html:select></td>
							<td class="black_ar"><html:text styleClass="black_ar"
								size="15" styleId="<%=medicalRecordNumber%>"
								property="<%=medicalRecordNumber%>"
								readonly="<%=readOnlyForAll%>"  /></td>
						</tr>
						<%
								}
								%>
					</tbody>

					<!-- Medical Identifiers End here -->
					<tr>
						<td align="left" class="black_ar"><html:button
							property="addKeyValue" styleClass="black_ar"
							onclick="insRow('addMore')">
							<bean:message key="buttons.addMore" />
						</html:button></td>
						<td class="black_ar"><html:button
							property="deleteMedicalIdentifierValue" styleClass="black_ar"
							onclick="deleteChecked('addMore','Participant.do?operation=<%=operation%>&pageOf=<%=pageOf%>&status=true',document.forms[0].valueCounter,'chk_',false)"
							disabled="true">
							<bean:message key="buttons.delete" />
						</html:button></td>
						<td class="black_ar">&nbsp;</td>
					</tr>

				</table>
				</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="bottomtd"></td>
			</tr>

			<tr onclick="javascript:showHide('add_participant_registeration')">
				<td width="90%" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="participant.collectionProtocolReg" /> </span></td>
				<td width="10%" align="right" class="tr_bg_blue1"><a href="#"
					id="imgArrow_add_participant_registeration">
					<logic:equal name="<%=Constants.OPERATION%>"
						value="<%=Constants.EDIT%>">
										
							<img src="images/uIEnhancementImages/dwn_arrow1.gif" alt="Show Details"
					width="80" height="9" hspace="10" border="0" />

					</logic:equal>

					<logic:notEqual name="<%=Constants.OPERATION%>"
						value="<%=Constants.EDIT%>">

						<img src="images/uIEnhancementImages/up_arrow.gif" alt="Hide Details"
					width="80" height="9" hspace="10" border="0" />		
												
					</logic:notEqual>
					
						</a></td>
			</tr>
			<tr>
				<td colspan="2">
				
				<logic:equal name="<%=Constants.OPERATION%>"
						value="<%=Constants.EDIT%>">
						<div id="add_participant_registeration" style="display:none">
				</logic:equal>
				<logic:notEqual name="<%=Constants.OPERATION%>"
						value="<%=Constants.EDIT%>">
						<div id="add_participant_registeration" style="display:block">
				</logic:notEqual>
				<table width="100%" border="0" cellspacing="0" cellpadding="3">


				<logic:equal name="<%=Constants.OPERATION%>"
						value="<%=Constants.EDIT%>">
					<tr class="tableheading">
						<td width="3%" align="left" class="black_ar_b">Select</td>
						<td width="18%" align="left" class="black_ar_b"><bean:message
							key="participant.collectionProtocolReg.protocolTitle" /></td>
						<td width="13%" align="left" class="black_ar_b"><bean:message
							key="participant.collectionProtocolReg.participantProtocolID" />
						</td>
						<%if((!Variables.isSpecimenCollGroupBarcodeGeneratorAvl) || operation.equals(Constants.EDIT))
						{%>
							<td width="13%" align="left" class="black_ar_b"><bean:message
								key="participant.collectionProtocolReg.barcode" />
							</td>
						<%}%>
						<td width="18%" align="left" class="black_ar_b"><bean:message
							key="participant.collectionProtocolReg.participantRegistrationDate" />
						</td>
						<td width="18%" align="left" class="black_ar_b"><bean:message
							key="participant.activityStatus" /></td>
						<td width="16%" align="left" class="black_ar_b"><bean:message
							key="participant.collectionProtocolReg.consent" /></td>
					</tr>
				</logic:equal>
				<logic:notEqual name="<%=Constants.OPERATION%>"
						value="<%=Constants.EDIT%>">
					<tr class="tableheading">
						<td width="3%" align="left" class="black_ar_b">Select</td>
						<td width="25%" align="left" class="black_ar_b"><bean:message
							key="participant.collectionProtocolReg.protocolTitle" /></td>
							
							<td width="13%" align="left" class="black_ar_b"><bean:message
							key="participant.collectionProtocolReg.participantProtocolID" />
							</td>
						
						<td width="25%" align="left" class="black_ar_b"><bean:message
							key="participant.collectionProtocolReg.participantRegistrationDate" />
						</td>
						<td width="25%" align="left" class="black_ar_b"><bean:message
							key="participant.activityStatus" /></td>
						<td width="22%" align="left" class="black_ar_b"><bean:message
							key="participant.collectionProtocolReg.consent" /></td>
					</tr>
				</logic:notEqual>
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
										String barcode = "collectionProtocolRegistrationValue(CollectionProtocolRegistration:"
											+ i + "_barcode)";
										String barcodeKey = "CollectionProtocolRegistration:"
											+ i + "_barcode";
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
							<td align="left" class="black_ar"><html:hidden
								property="<%=collectionProtocolIdentifier%>" /> <input
								type=checkbox name="<%=collectionProtocolCheck %>"
								id="<%=collectionProtocolCheck %>"
								<%=CollectionProtocolRegCondition%>
								onClick="javascript:enableButton(document.forms[0].deleteParticipantRegistrationValue,document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_')">
							</td>
							<td align="left" nowrap class="black_ar">
							<%
									if (CollectionProtocolRegConditionBoolean) {
									%> <html:text styleClass="black_ar" maxlength="50"
								styleId="<%=collectionProtocolTitle%>"
								property="<%=collectionProtocolTitle%>"
								readonly="<%=readOnlyValue%>" /> <input type="hidden"
								id="<%=collectionProtocolId%>" name="<%=collectionProtocolId%>"
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
							<%if((!Variables.isProtocolParticipantIdentifierLabelGeneratorAvl) 
									|| operation.equals(Constants.EDIT) || operation.equals(Constants.ADD))
							{%>
							<td align="left" class="black_ar"><html:text
								styleClass="black_ar" size="10" maxlength="50"
								styleId="<%=collectionProtocolParticipantId%>"
								property="<%=collectionProtocolParticipantId%>" /></td>
							<%}%>
							<%if((!Variables.isCollectionProtocolRegistrationBarcodeGeneratorAvl) 
									|| operation.equals(Constants.EDIT))
								{
									if(operation.equals(Constants.EDIT))
									{%>
										<td align="left" class="black_ar">
										<logic:equal name="participantForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>">	
										<%
										if(form.getCollectionProtocolRegistrationValue(barcodeKey)!=null)
										{%>
											<label for="barcode" >
											<%=form.getCollectionProtocolRegistrationValue(barcodeKey)%>
											</label>									
										<%}
										else
										{%>
											<label for="barcode" >
											</label>
										<%}%>
										<html:hidden property="<%=barcode%>" />
										
										</logic:equal>
										<logic:notEqual name="participantForm" property="isBarcodeEditable" value="<%=Constants.FALSE%>">
										<html:text styleClass="black_ar" size="10" maxlength="50" styleId="<%=barcode%>" property="<%=barcode%>" />
										</logic:notEqual>
										</td>
									<%}
									else
									{%>
										<td align="left" class="black_ar"><html:text styleClass="black_ar" size="10" maxlength="50" styleId="<%=barcode%>" property="<%=barcode%>" /></td>
									<%}
							}%>
							<td align="left" class="black_ar"><html:text
								styleClass="black_ar" size="10" maxlength="50"
								styleId="<%=collectionProtocolRegistrationDate%>"
								property="<%=collectionProtocolRegistrationDate%>" /></td>
							<td align="left" class="black_ar"><html:select
								property="<%=collectionProtocolRegistrationActivityStatus%>"
								styleClass="formFieldSized8"
								styleId="<%=collectionProtocolRegistrationActivityStatus%>"
								size="1" disabled='<%=activityStatusCondition%>'
								onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>"
									labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
							</html:select></td>
							<td align="left" class="black_ar"><span
								id="<%=consentCheckStatus%>"> <%
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
						<td align="left" class="black_ar" colspan="3"><html:button
							property="addKeyValue" styleClass="black_ar"
							onclick="participantRegRow('addMoreParticipantRegistration')">
							<bean:message key="buttons.addMore" />
						</html:button>
						&nbsp;<html:button
							property="deleteParticipantRegistrationValue"
							styleClass="black_ar"
							onclick="deleteChecked('addMoreParticipantRegistration','Participant.do?operation=<%=operation%>&pageOf=<%=pageOf%>&status=true&deleteRegistration=true',document.forms[0].collectionProtocolRegistrationValueCounter,'CollectionProtocolRegistrationChk_',false)"
							disabled="true">
							<bean:message key="buttons.delete" />
						</html:button></td>
						<td class="black_ar" >&nbsp;</td>
						<td class="black_ar">&nbsp;</td>
						<td class="black_ar">&nbsp;</td>
						
					</tr>


				</table>
				</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="bottomtd"></td>
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
					var wdt = getWidth(90);
					if(wdt>1000)wdt=getWidth(63.5);
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
				<td colspan="2" class="buttonbg">
				<% String changeAction = "setFormAction('" + formName + "')";
						%><!-- action buttons begins -->
				<table cellpadding="0" cellspacing="0" border="0">
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
						<td nowrap >
							<html:button
								styleClass="blue_ar_b" property="registratioPage"
								title="Register Participant"
								value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[0][0]%>"
								onclick="<%=forwardToSubmit%>">
							</html:button>
						</td>
						</logic:equal>

						<logic:notEqual name="<%=Constants.PAGEOF%>"
							value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">

							<td>
								<html:button styleClass="blue_ar_b"
								property="registratioPage" title="Submit Only"
								onclick="<%=normalSubmit%>">
								<bean:message key="buttons.submit" />
								</html:button><!-- delete button added for deleting the objects --> 
								<logic:equal name="operation" value="edit">
									<% 	
										String deleteAction="deleteObject('" + formName +"','" + Constants.ADMINISTRATIVE + "')";
									%>
									|&nbsp;<html:button styleClass="blue_ar_c" property="disableRecord"
										title="Delete" value="Delete" onclick="<%=deleteAction%>">
									</html:button>
								</logic:equal>
							</td>
						</logic:notEqual>

						<logic:equal name="<%=Constants.PAGEOF%>"
							value="<%=Constants.PAGE_OF_PARTICIPANT_CP_QUERY%>">

							<td nowrap>&nbsp;|&nbsp;<html:button styleClass="blue_ar_c"
								property="registratioPage"
								value="<%=Constants.PARTICIPANT_FORWARD_TO_LIST[2][0]%>"
								onclick="<%=forwardToSCG%>"
								onmouseover="showMessage('Create additional Specimen Collection Group to collect specimens which were  not anticipated as per protocol')">
							</html:button>
							<logic:equal name="operation" value="edit">
										|<% 	
										String deleteAction="deleteObject('" + formName +"','" + Constants.CP_QUERY_BIO_SPECIMEN + "')";
									%>
								<html:button styleClass="blue_ar_c" property="disableRecord"
									title="Disable Participant" value="Delete" onclick="<%=deleteAction%>">
								</html:button>&nbsp;
								
							 </logic:equal>
							 </td>
						</logic:equal>
					</tr>
				</table>
				<!-- action buttons end --></td>
			</tr>
		</table>
		</td>
	</tr>
</table>