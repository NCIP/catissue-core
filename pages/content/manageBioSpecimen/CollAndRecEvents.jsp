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
  <tr >
    <td width="50%" align="left" class="tr_bg_blue1" ><span class="blue_ar_b">&nbsp;
							<bean:message key="specimen.collectedevents.title"/>
							<input type="hidden" name="crDispType" value="<%=crDispType1%>" id="crDispType" />
							</span>
	</td>
    <td align="centre" width=-"48%" class="tr_bg_blue1" ><span class="blue_ar_b">&nbsp;
							<bean:message key="specimen.receivedevents.title"/>	
							</span>
	</td>
    <td align="right" class="tr_bg_blue1" width="2%">
	</td>
  </tr>
  
  <tr>
    <td>
	<table width="100%" border="0" cellspacing="0" cellpadding="3">
      <tr>
        <html:hidden property="collectionEventId" />
						<html:hidden property="collectionEventSpecimenId" />
		<td width="1%" align="center" >
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
		</td>
        <td align="left" class="black_ar">
							<label for="user">
								<bean:message key="specimen.collectedevents.username"/>
							</label>
		</td>
        <td align="left" class="black_new">
						
							<autocomplete:AutoCompleteTag property="collectionEventUserId"
										  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
										  initialValue="<%=new Long(form.getCollectionEventUserId())%>"
        								  staticField="false"
										  styleClass="formFieldSized12"
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
								Integer collectionYear = new Integer(Utility.getYear(currentCollectionDate ));
								Integer collectionMonth = new Integer(Utility.getMonth(currentCollectionDate ));
								Integer collectionDay = new Integer(Utility.getDay(currentCollectionDate ));
							%>
							<ncombo:DateTimeComponent name="collectionEventdateOfEvent"
										  id="collectionEventdateOfEvent"
										  formName="<%=formNameForCal%>"
										  month= "<%= collectionMonth %>"
										  year= "<%= collectionYear %>"
										  day= "<%= collectionDay %>"
										  value="<%=currentCollectionDate %>"
										  styleClass="black_ar"
												/>
							<% 
								}
								else
								{  
							 %>
							<ncombo:DateTimeComponent name="collectionEventdateOfEvent"
										  id="collectionEventdateOfEvent"
										  formName="<%=formNameForCal %>"
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
        <td class="black_new">
						
					
							<autocomplete:AutoCompleteTag property="collectionEventTimeInHours"
										  optionsList = "<%=request.getAttribute(Constants.HOUR_LIST)%>"
										  initialValue="<%=form.getCollectionEventTimeInHours()%>"
										  styleClass="formFieldSized5"
										  staticField="false"
					    />	
							
							&nbsp;
							<label for="eventparameters.timeinhours">
								<bean:message key="eventparameters.timeinhours"/>&nbsp; 
							</label>
							
							<autocomplete:AutoCompleteTag property="collectionEventTimeInMinutes"
										  optionsList = "<%=request.getAttribute(Constants.MINUTES_LIST)%>"
										  initialValue="<%=form.getCollectionEventTimeInMinutes()%>"
										  styleClass="formFieldSized5"
										  staticField="false"
					    />	

							<label for="eventparameters.timeinminutes">
								&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
							</label>
		</td>
      </tr>
      <tr>
        <td width="1%" align="center">
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
		</td>
        <td align="left" class="black_ar">
							<label for="collectionprocedure">
								<bean:message key="collectioneventparameters.collectionprocedure"/>
							</label>
		</td>
        <td class="black_new">
								<autocomplete:AutoCompleteTag property="collectionEventCollectionProcedure"
										  optionsList = "<%=request.getAttribute(Constants.PROCEDURE_LIST)%>"
										  initialValue="<%=form.getCollectionEventCollectionProcedure()%>"
										  styleClass="formFieldSized12"
						/>	
		</td>
      </tr>
      <tr>
        <td width="1%" align="center" >
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
		</td>
        <td align="left" class="black_ar" >
								<label for="container">
									<bean:message key="collectioneventparameters.container"/>
								</label>
		</td>
        <td class="black_new">
								<autocomplete:AutoCompleteTag property="collectionEventContainer"
										  optionsList = "<%=request.getAttribute(Constants.CONTAINER_LIST)%>"
										  initialValue="<%=form.getCollectionEventContainer()%>"
										   styleClass="formFieldSized12"
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
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
		</td>
        <td align="left" class="black_ar">
							<label for="type">
								<bean:message key="specimen.receivedevents.username"/>
							</label>
		</td>
        <td align="left" class="black_new">
						<autocomplete:AutoCompleteTag property="receivedEventUserId"
										  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
										  initialValue="<%=new Long(form.getReceivedEventUserId())%>"
										  staticField="false"
										  styleClass="formFieldSized12"
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
									Integer receivedYear = new Integer(Utility.getYear(currentReceivedDate ));
									Integer receivedMonth = new Integer(Utility.getMonth(currentReceivedDate ));
									Integer receivedDay = new Integer(Utility.getDay(currentReceivedDate ));
								%>
								<ncombo:DateTimeComponent name="receivedEventDateOfEvent"
											  id="receivedEventDateOfEvent"
											  month= "<%= receivedMonth %>"
											  year= "<%= receivedYear %>"
											  day= "<%= receivedDay %>"
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
        <td class="black_new">
						
						<autocomplete:AutoCompleteTag property="receivedEventTimeInHours"
										  optionsList = "<%=request.getAttribute(Constants.HOUR_LIST)%>"
										  initialValue="<%=form.getReceivedEventTimeInHours()%>"
										  styleClass="formFieldSized5"
										  staticField="false"
					    />	


							<label for="eventparameters.timeinhours">
								<bean:message key="eventparameters.timeinhours"/>&nbsp; 
							</label>
							
							<autocomplete:AutoCompleteTag property="receivedEventTimeInMinutes"
										  optionsList = "<%=request.getAttribute(Constants.MINUTES_LIST)%>"
										  initialValue="<%=form.getReceivedEventTimeInMinutes()%>"
										  styleClass="formFieldSized5"
										  staticField="false"
					    />	

							<label for="eventparameters.timeinminutes">
								&nbsp;<bean:message key="eventparameters.timeinminutes"/> 
							</label>
		</td>
      </tr>
      <tr>
        <td  width="1%" align="center" >
							<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
		</td>
        <td align="left" class="black_ar" >
							<label for="quality">
								<bean:message key="receivedeventparameters.receivedquality"/>
							</label>
		</td>
        <td class="black_new">
							<autocomplete:AutoCompleteTag property="receivedEventReceivedQuality"
										  optionsList = "<%=request.getAttribute(Constants.RECEIVED_QUALITY_LIST)%>"
										  initialValue="<%=form.getReceivedEventReceivedQuality()%>"
										   styleClass="formFieldSized12"
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
<!---------------------------------------------------->
					
						
						
						
						
						
						
						
					
					
					