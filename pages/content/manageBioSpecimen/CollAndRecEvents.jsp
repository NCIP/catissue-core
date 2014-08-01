<%@ page import="edu.wustl.common.util.global.CommonServiceLocator"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%
	// Patch_Id: Improve_Space_Usability_On_Specimen_Page_2
	// variable to get the current display style of collect recieve event table
	String crDispType1=request.getParameter("crDispType");
%><link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
  <link href="css/styleSheet.css" rel="stylesheet" type="text/css" />

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>

<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlxSuite_v35/dhtmlxCalendar/codebase/skins/dhtmlxcalendar_dhx_skyblue.css" />
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxGrid/codebase/dhtmlxcommon.js"></script>
<script type="text/javascript" src="dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.js"></script>

<table width="100%" border="0" cellspacing="0" cellpadding="0" id="collectionEvent">
	<tr onclick="showHide('event')">
	<td width="50%" align="left" class="tr_bg_blue1" ><span class="blue_ar_b">&nbsp;Collection and Received Events</span></td>
	<td width="50%"align="right" class="tr_bg_blue1" ><a href="#" id="imgArrow_event" ><img src="images/uIEnhancementImages/up_arrow.gif" alt="Show Details" width="80" height="9" hspace="10" border="0"/></a></td>
	</tr>
	<tr>
	   <td colspan="2" class="showhide1">
	   <div id="event" style="display:block" >
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
    <tr>
    <td width="50%" style="padding-top:2px">
	<table width="100%" border="0" cellspacing="0" cellpadding="3">
      <tr class="tr_alternate_color_lightGrey">
        <html:hidden property="collectionEventId" />
						<html:hidden property="collectionEventSpecimenId" />
		<td class="black_ar" align="right" width="40%">
			<logic:equal name="showStar" value="false">
				&nbsp;
			</logic:equal>
			<logic:notEqual name="showStar" value="false">
			<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
			</logic:notEqual>
			<label for="user">
			<b>	<bean:message key="specimen.collectedevents.username"/> </b>
			</label>
		</td>
        <td align="left" width="60%" class="align_left_style1">
						<html:select property="collectionEventUserId"
							             styleClass="black_ar" styleId="collectionEventUserId" size="1">
							       <html:options collection="userList" labelProperty="name" property="value" />
					    </html:select>
		</td>
      </tr>
      <tr>
         <td class="black_ar" align="right" width="40%">
		 
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
							<label for="date">
								<b><bean:message key="scg.CollectionDate"/></b>							
							</label>
		</td>
        <td class="black_ar align_left_style1" align="left" width="60%">
		<table style="border-collapse:collapse;">
		<td>
							<html:text property="collectionEventdateOfEvent" styleClass="black_ar"
							     styleId="collectionEventdateOfEvent" size="10" value="<%=currentCollectionDate%>" 
                               onclick="doInitCalendar('collectionEventdateOfEvent',false,'${uiDatePattern}');" /> 
		</td>
		<td  style="padding-left:4px">								
						<html:select property="collectionEventTimeInHours" styleClass="black_ar" styleId="collectionEventTimeInHours" size="1">
								<logic:iterate name="hourList" id="listhoursId">
									<html:option  value="${listhoursId}"> ${listhoursId} </html:option>
							    </logic:iterate>
						</html:select> 
		</td>
		<td  style="padding-left:4px">
						<html:select property="collectionEventTimeInMinutes" styleClass="black_ar" styleId="collectionEventTimeInMinutes" size="1">
								<logic:iterate name="minutesList" id="listminutesId">
									<html:option  value="${listminutesId}"> ${listminutesId} </html:option>
							    </logic:iterate>
					    </html:select> 
		</td>
		</table>

		 </td>
      </tr>
	  
       <tr class="tr_alternate_color_lightGrey">
         <td class="black_ar" align="right" width="40%">
			<logic:equal name="showStar" value="false">
				&nbsp;
			</logic:equal>
			<logic:notEqual name="showStar" value="false">
				<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
			</logic:notEqual>
					<label for="collectionprocedure">
					<b>	<bean:message key="collectioneventparameters.collectionprocedure"/> </b>
					</label>
		 </td>
        <td class="black_ar align_left_style1" align="left" width="60%">
					
				<html:select property="collectionEventCollectionProcedure"
							             styleClass="black_ar" styleId="collectionEventCollectionProcedure" size="1">
							       <html:options collection="procedureList" labelProperty="name" property="value" />
			   </html:select>
		</td>
      </tr>
      <tr>
        <td class="black_ar" align="right" width="40%">
			<logic:equal name="showStar" value="false">
			&nbsp;
			</logic:equal>
			<logic:notEqual name="showStar" value="false">
				<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
			</logic:notEqual>
								<label for="container">
								<b>	<bean:message key="collectioneventparameters.container"/> </b>
								</label>
		</td>
        <td class="black_ar align_left_style1" align="left" width="60%">
					<html:select property="collectionEventContainer"
							             styleClass="black_ar" styleId="collectionEventContainer" size="1">
							       <html:options collection="containerList" labelProperty="name" property="value" />
					</html:select>
		</td>
      </tr>
	  <tr>
		<td class="black_ar" align="right" width="40%">
							<label for="comments">
							<b>	<bean:message key="eventparameters.comments"/> </b> 
							</label>
		</td>
		<td class="black_ar align_left_style1"  align="left" width="60%">
			<html:textarea styleClass="black_ar" style="width: 68%" rows="2" styleId="collectionEventComments" property="collectionEventComments" />
		</td>		
    </table></td>
    <td width="50%">
		<table width="100%" border="0" cellspacing="0" cellpadding="3">
		<tr class="tr_alternate_color_lightGrey">
        <html:hidden property="receivedEventId" />
						<html:hidden property="receivedEventSpecimenId" />
	    <td width="40%" align="right" class="black_ar">
			<logic:equal name="showStar" value="false">
				&nbsp;
			</logic:equal>
			<logic:notEqual name="showStar" value="false">
				<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
			</logic:notEqual>
			
			<label for="type">
			<b>	<bean:message key="specimen.receivedevents.username"/> </b>
			</label>
		</td>
        <td width="60%" align="left" class="black_ar align_left_style1">
				<html:select property="receivedEventUserId"
								 styleClass="black_ar" styleId="receivedEventUserId" size="1">
						   <html:options collection="userList" labelProperty="name" property="value" />
				</html:select>		
		</td>
      </tr>
      <tr>
        <td width="40%" align="right" class="black_ar">
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
							<label for="date">
							<b>	<bean:message key="scg.receiveDate"/> </b>
							</label>
		</td>
        <td  width="60%" class="black_ar align_left_style1">
		<table style="border-collapse:collapse;">
			<td>
								
						<html:text property="receivedEventDateOfEvent" styleClass="black_ar"
							       styleId="receivedEventDateOfEvent" size="10"  value="<%=currentReceivedDate %>" 
                                   onclick="doInitCalendar('receivedEventDateOfEvent',false,'${uiDatePattern}');" />	
			</td>
			<td style="padding-left:4px">
						<html:select property="receivedEventTimeInHours" styleClass="black_ar" styleId="receivedEventTimeInHours" size="1">
								<logic:iterate name="hourList" id="listHoursId">
									<html:option  value="${listHoursId}"> ${listHoursId} </html:option>
							    </logic:iterate>
					    </html:select>
		   </td>
		   <td style="padding-left:4px">
					   <html:select property="receivedEventTimeInMinutes" styleClass="black_ar" styleId="receivedEventTimeInMinutes" size="1">
								<logic:iterate name="minutesList" id="listMinutesId">
									<html:option  value="${listMinutesId}"> ${listMinutesId} </html:option>
							    </logic:iterate>
						</html:select>
			</td>
		 </table>  
		</td>
			
		</td>
      </tr>
 
      <tr class="tr_alternate_color_lightGrey">
          <td width="40%" align="right" class="black_ar" >
			<logic:equal name="showStar" value="false">
				&nbsp;
			</logic:equal>
			<logic:notEqual name="showStar" value="false">
				<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
			</logic:notEqual>
							<label for="quality">
							<b>	<bean:message key="receivedeventparameters.receivedquality"/> </b>
							</label>
		 </td>
        <td  width="60%" class="black_ar align_left_style1">
		
					    <html:select property="receivedEventReceivedQuality"
							             styleClass="black_ar" styleId="receivedEventReceivedQuality" size="1">
							       <html:options collection="receivedQualityList" labelProperty="name" property="value" />
					    </html:select>
		</td>
      </tr>

	 
      <tr>
		<td width="40%" class="black_ar" align="right">
			<label for="comments">
				<b><bean:message key="eventparameters.comments"/> </b>
			</label>
		</td>						
		<td width="60%" class="black_ar align_left_style1">
			<html:textarea styleClass="black_ar" style="width: 68%;" rows="2"  styleId="receivedEventComments" property="receivedEventComments" />
		</td>	
      </tr>
	   <tr>
	  <td colspan="3">&nbsp;</td>
	  </tr>
    </table></td> 
  </tr>
 </table>
 </div>
</td>
</tr>
</table>
<!---------------------------------------------------->