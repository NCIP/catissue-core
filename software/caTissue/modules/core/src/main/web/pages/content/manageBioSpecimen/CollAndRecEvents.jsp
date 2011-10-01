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

<table width="100%" border="0" cellspacing="0" cellpadding="0" id="collectionEvent">
	<tr onclick="showHide('event')">
	<td width="50%" align="left" class="tr_bg_blue1" ><span class="blue_ar_b">&nbsp;Events</span></td>
	<td width="50%"align="right" class="tr_bg_blue1" ><a href="#" id="imgArrow_event" ><img src="images/uIEnhancementImages/up_arrow.gif" alt="Show Details" width="80" height="9" hspace="10" border="0"/></a></td>
	</tr>
	<tr>
	  <td colspan="2" class="showhide1"><div id="event" style="display:block" >
	   <table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="left" valign="top" class="black_ar">&nbsp;&nbsp;&nbsp;&nbsp;<strong>Collected</strong></td>
    <td align="left" valign="middle" class="black_ar" nowrap>&nbsp;&nbsp;&nbsp;&nbsp;<strong>Received</strong></td>
 </tr>
  <tr><td colspan="2" class="bottomtd"></td></tr>
  <tr>
    <td>
	<table width="100%" border="0" cellspacing="0" cellpadding="3">
      <tr>
        <html:hidden property="collectionEventId" />
						<html:hidden property="collectionEventSpecimenId" />
		<td width="1%" align="center" >
		<logic:equal name="showStar" value="false">
			&nbsp;
		</logic:equal>
		<logic:notEqual name="showStar" value="false">
		<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
		</logic:notEqual>
		</td>
        <td align="left" class="black_ar">
							<label for="user">
								<bean:message key="specimen.collectedevents.username"/>
							</label>
		</td>
        <td align="left" class="black_ar">
						
							<autocomplete:AutoCompleteTag property="collectionEventUserId"
										  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
										  initialValue="<%=new Long(form.getCollectionEventUserId())%>"
        								  staticField="false"
										  styleClass="formFieldSizedAutoSCG"
							/>		
		</td>
      </tr>
      <tr>
        <td width="1%" align="center">
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
		</td>
        <td align="left" class="black_ar">
							<label for="date">
								<bean:message key="eventparameters.dateofevent"/>							
							</label>
		</td>
        <td class="black_ar">
							<%
								if(currentCollectionDate.trim().length() > 0)
												{
													Integer collectionYear = new Integer(AppUtility.getYear(currentCollectionDate ));
													Integer collectionMonth = new Integer(AppUtility.getMonth(currentCollectionDate ));
													Integer collectionDay = new Integer(AppUtility.getDay(currentCollectionDate ));
							%>
							<ncombo:DateTimeComponent name="collectionEventdateOfEvent"
										  id="collectionEventdateOfEvent"
										  formName="<%=formNameForCal%>"
										  month= "<%=collectionMonth%>"
										  year= "<%=collectionYear%>"
										  day= "<%=collectionDay%>"
										  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
										  value="<%=currentCollectionDate%>"
										  styleClass="black_ar"
												/>
							<%
								}
													else
													{
							%>
							<ncombo:DateTimeComponent name="collectionEventdateOfEvent"
										  id="collectionEventdateOfEvent"
										  formName="<%=formNameForCal%>"
										  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
										  styleClass="black_ar"
												/>
							<%
								}
							%>
							<span class="grey_ar_s" ><bean:message key="scecimen.dateformat" /></span>
		</td>
      </tr>
      <tr>
        <td width="1%" align="center">
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
		</td>
        <td align="left" class="black_ar">
							<label for="eventparameters.time">
								<bean:message key="eventparameters.time"/>
							</label>
		</td>
        <td  class="black_ar">
						
					
							<autocomplete:AutoCompleteTag property="collectionEventTimeInHours"
										  optionsList = "<%=request.getAttribute(Constants.HOUR_LIST)%>"
										  initialValue="<%=form.getCollectionEventTimeInHours()%>"
										  styleClass="formFieldSizedAutoSCGTime"
										  staticField="false"
					    />	
							
							&nbsp;
							<label for="eventparameters.timeinhours">
								<bean:message key="eventparameters.timeinhours"/>
							</label>
							
							<autocomplete:AutoCompleteTag property="collectionEventTimeInMinutes"
										  optionsList = "<%=request.getAttribute(Constants.MINUTES_LIST)%>"
										  initialValue="<%=form.getCollectionEventTimeInMinutes()%>"
										  styleClass="formFieldSizedAutoSCGTime"
										  staticField="false"
					    />	

							<label for="eventparameters.timeinminutes">
								&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
							</label>
		</td>
      </tr>
      <tr>
        <td width="1%" align="center">
			<logic:equal name="showStar" value="false">
			&nbsp;
			</logic:equal>
			<logic:notEqual name="showStar" value="false">
				<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
			</logic:notEqual>
		</td>
        <td align="left" class="black_ar">
							<label for="collectionprocedure">
								<bean:message key="collectioneventparameters.collectionprocedure"/>
							</label>
		</td>
        <td class="black_ar">
								<autocomplete:AutoCompleteTag property="collectionEventCollectionProcedure"
										  optionsList = "<%=request.getAttribute(Constants.PROCEDURE_LIST)%>"
										  initialValue="<%=form.getCollectionEventCollectionProcedure()%>"
										  styleClass="formFieldSizedAutoSCG"
						/>	
		</td>
      </tr>
      <tr>
        <td width="1%" align="center" >
			<logic:equal name="showStar" value="false">
			&nbsp;
			</logic:equal>
			<logic:notEqual name="showStar" value="false">
				<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
			</logic:notEqual>
		</td>
        <td align="left" class="black_ar" >
								<label for="container">
									<bean:message key="collectioneventparameters.container"/>
								</label>
		</td>
        <td class="black_ar">
								<autocomplete:AutoCompleteTag property="collectionEventContainer"
										  optionsList = "<%=request.getAttribute(Constants.CONTAINER_LIST)%>"
										  initialValue="<%=form.getCollectionEventContainer()%>"
										   styleClass="formFieldSizedAutoSCG"
						    />
		</td>
      </tr>
	  <tr>
		<td class="black_ar" >&nbsp;
		</td>
		<td class="black_ar">
							<label for="comments">
								<bean:message key="eventparameters.comments"/> 
							</label>
		</td>
		<td class="black_ar">
							<html:textarea styleClass="black_ar"  styleId="collectionEventComments" property="collectionEventComments" />
		</td>		
    </table></td>
    <td colspan="2" align="top"><table width="100%" border="0" cellspacing="0" cellpadding="3">
      <tr>
        <html:hidden property="receivedEventId" />
						<html:hidden property="receivedEventSpecimenId" />
		<td width="1%" align="center">
			<logic:equal name="showStar" value="false">
				&nbsp;
			</logic:equal>
			<logic:notEqual name="showStar" value="false">
				<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
			</logic:notEqual>
		</td>
        <td align="left" class="black_ar">
							<label for="type">
								<bean:message key="specimen.receivedevents.username"/>
							</label>
		</td>
        <td align="left" class="black_ar">
						<autocomplete:AutoCompleteTag property="receivedEventUserId"
										  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
										  initialValue="<%=new Long(form.getReceivedEventUserId())%>"
										  staticField="false"
										  styleClass="formFieldSizedAutoSCG"
					    />	
		</td>
      </tr>
      <tr>
        <td width="1%" align="center">
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
		</td>
        <td align="left" class="black_ar">
							<label for="date">
								<bean:message key="eventparameters.dateofevent"/>
							</label>
		</td>
        <td class="black_ar">
								<%
									if(currentReceivedDate.trim().length() > 0)
														{
															Integer receivedYear = new Integer(AppUtility.getYear(currentReceivedDate ));
															Integer receivedMonth = new Integer(AppUtility.getMonth(currentReceivedDate ));
															Integer receivedDay = new Integer(AppUtility.getDay(currentReceivedDate ));
								%>
								<ncombo:DateTimeComponent name="receivedEventDateOfEvent"
											  id="receivedEventDateOfEvent"
											  month= "<%= receivedMonth %>"
											  year= "<%= receivedYear %>"
											  day= "<%= receivedDay %>"
											  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
											  value="<%=currentReceivedDate %>"
											  formName= "<%=formNameForCal %>"
											  styleClass="black_ar"
													/>
								<% 
									}
									else
									{  
								 %>
								<ncombo:DateTimeComponent name="receivedEventDateOfEvent"
											  id="receivedEventDateOfEvent"
											  formName="<%=formNameForCal %>"
											  pattern="<%=CommonServiceLocator.getInstance().getDatePattern()%>"
											  styleClass="black_ar"
													/>
								<% 
									} 
								%> 
								<span class="grey_ar_s" ><bean:message key="scecimen.dateformat" /></span>
		</td>
      </tr>
      <tr>
        <td width="1%" align="center">
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
		</td>
        <td align="left" class="black_ar">
							<label for="eventparameters.time">
								<bean:message key="eventparameters.time"/>
							</label>
		</td>
        <td class="black_ar">
						
						<autocomplete:AutoCompleteTag property="receivedEventTimeInHours"
										  optionsList = "<%=request.getAttribute(Constants.HOUR_LIST)%>"
										  initialValue="<%=form.getReceivedEventTimeInHours()%>"
										  styleClass="formFieldSizedAutoSCGTime"
										  staticField="false"
					    />	


							<label for="eventparameters.timeinhours">
								<bean:message key="eventparameters.timeinhours"/> &nbsp;
							</label>
							
							<autocomplete:AutoCompleteTag property="receivedEventTimeInMinutes"
										  optionsList = "<%=request.getAttribute(Constants.MINUTES_LIST)%>"
										  initialValue="<%=form.getReceivedEventTimeInMinutes()%>"
										  styleClass="formFieldSizedAutoSCGTime"
										  staticField="false"
					    />	

							<label for="eventparameters.timeinminutes">
								&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
							</label>
		</td>
      </tr>
      <tr>
        <td  width="1%" align="center" >
			<logic:equal name="showStar" value="false">
				&nbsp;
			</logic:equal>
			<logic:notEqual name="showStar" value="false">
				<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
			</logic:notEqual>
		</td>
        <td align="left" class="black_ar" >
							<label for="quality">
								<bean:message key="receivedeventparameters.receivedquality"/>
							</label>
		</td>
        <td class="black_ar">
							<autocomplete:AutoCompleteTag property="receivedEventReceivedQuality"
										  optionsList = "<%=request.getAttribute(Constants.RECEIVED_QUALITY_LIST)%>"
										  initialValue="<%=form.getReceivedEventReceivedQuality()%>"
										  styleClass="formFieldSizedAutoSCG"
						/>
		</td>
      </tr>
	 
      <tr>
        <td class="black_ar" width="1%">&nbsp;
		</td>
		<td class="black_ar">
			<label for="comments">
				<bean:message key="eventparameters.comments"/> 
			</label>
		</td>						
		<td class="black_ar">
			<html:textarea styleClass="black_ar"  styleId="receivedEventComments" property="receivedEventComments" />
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