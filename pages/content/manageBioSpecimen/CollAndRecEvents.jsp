<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%
	// Patch_Id: Improve_Space_Usability_On_Specimen_Page_2
	// variable to get the current display style of collect recieve event table
	String crDispType1=request.getParameter("crDispType");
%>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" id="collectionEvent">					
					<!-- Mandar AutoEvents start -->		

					<tr>
					<!--/**
						* Name : Vijay_Pande
						* Reviewer : Santosh_Chandak
						* Bug ID: Improve_Space_Usability_On_Specimen_Page
						* Patch ID: Improve_Space_Usability_On_Specimen_Page_2
						* See also: 1-5
						* Description: To improve space usability on specimen page, the table which are rarely used are kept invisible by default. 
 						* Following image is provided to toggle between the show and hide the table (Collected/recieved Events table).
						*/   -->							
						<td class="formFieldAllBorders" align="right" width="1%">
							<a id="imageCR" style="text-decoration:none" href="javascript:switchStyle('imageCR','crDispType','collRecTable');">  
							<img src="images/nolines_minus.gif" border="0" width="18" height="18"/>
							</a>
							<input type="hidden" name="crDispType" value="<%=crDispType1%>" id="crDispType" />
						</td> 
						<!-- Patch ends here -->
						<td colspan="3" width="550" class="formTitle">
							<bean:message key="specimen.collectedevents.title"/>	
						</td>
						<td colspan="2" width="450" class="formTitle">
							<bean:message key="specimen.receivedevents.title"/>						
						</td>
					</tr>
					<!-- User -->
					<tr>	
						<td colspan="6" width="100%" >
						<!-- Patch_Id: Improve_Space_Usability_On_Specimen_Page_2
							 Description: Table is seperated from its title to collapse/expand   -->
							<table border="0" cellpadding="3" cellspacing="0"   width="100%" id="collRecTable">
							<!-- Patch ends here -->

						<!-- CollectionEvent fields -->	
						
						<html:hidden property="collectionEventId" />
						<html:hidden property="collectionEventSpecimenId" />
						<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
	 					<td width="130" class="formFieldNoBordersSimple"> 
							<label for="user">
								<b><bean:message key="specimen.collectedevents.username"/></b> 
							</label>
						</td>						
						<td width="340" class="formFieldNoBordersSimple">
						
							<autocomplete:AutoCompleteTag property="collectionEventUserId"
										  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
										  initialValue="<%=form.getCollectionEventUserId()%>"
        								  staticField="false"
							/>		
							
						</td>		

						<!-- RecievedEvent fields -->
						<html:hidden property="receivedEventId" />
						<html:hidden property="receivedEventSpecimenId" />
						<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
						<td width="110" class="formFieldNoBordersSimple">
							<label for="type">
								<b><bean:message key="specimen.receivedevents.username"/></b> 
							</label>
						</td>
						<td width="220" class="formFieldNoBordersSimple">
						<autocomplete:AutoCompleteTag property="receivedEventUserId"
										  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
										  initialValue="<%=form.getReceivedEventUserId()%>"
										  staticField="false"
					    />	
						</td>
						
					</tr>
					
					<!-- date -->
					<tr>
						
						<!-- CollectionEvent fields -->	
						<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
						<td class="formFieldNoBordersSimple" nowrap> 
							<label for="date">
								<b><bean:message key="eventparameters.dateofevent"/></b>															
							</label>
							<!-- <BR><small><bean:message key="page.dateFormat" /></small>  -->
						</td>
						
						<td class="formFieldNoBordersSimple">
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
										  styleClass="formDateSized10"
												/>
							<% 
								}
								else
								{  
							 %>
							<ncombo:DateTimeComponent name="collectionEventdateOfEvent"
										  id="collectionEventdateOfEvent"
										  formName="<%=formNameForCal %>"
										  styleClass="formDateSized10"
												/>
							<% 
								} 
							%>
							<small><bean:message key="scecimen.dateformat" /></small>	
						</td>	

						<!-- RecievedEvent fields -->
						<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
						<td class="formFieldNoBordersSimple" nowrap>
							<label for="date">
								<b><bean:message key="eventparameters.dateofevent"/></b> 
							</label>
							<!-- <BR><small><bean:message key="scecimen.dateformat" /></small> -->
						</td>						
						<td class="formFieldNoBordersSimple">
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
											  styleClass="formDateSized10"
													/>
								<% 
									}
									else
									{  
								 %>
								<ncombo:DateTimeComponent name="receivedEventDateOfEvent"
											  id="receivedEventDateOfEvent"
											  formName="<%=formNameForCal %>"
											  styleClass="formDateSized10"
													/>
								<% 
									} 
								%> 
								<small><bean:message key="scecimen.dateformat" /></small>	
						</td>	
					</tr>	
					
					<!-- hours & minutes -->		
					<tr>
						<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
						<td class="formFieldNoBordersSimple">
							<label for="eventparameters.time">
								<b><bean:message key="eventparameters.time"/></b>
							</label>
						</td>
						<td class="formFieldNoBordersSimple">
						
					
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
						
						<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
						<td class="formFieldNoBordersSimple">
							<label for="eventparameters.time">
								<b><bean:message key="eventparameters.time"/></b>
							</label>
						</td>
						<td class="formFieldNoBordersSimple">
						
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
						
						<!-- CollectionEvent fields -->	
						<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
						<td class="formFieldNoBordersSimple">
							<label for="collectionprocedure">
								<b><bean:message key="collectioneventparameters.collectionprocedure"/></b> 
							</label>
						</td>
						<td class="formFieldNoBordersSimple">
								<autocomplete:AutoCompleteTag property="collectionEventCollectionProcedure"
										  optionsList = "<%=request.getAttribute(Constants.PROCEDURE_LIST)%>"
										  initialValue="<%=form.getCollectionEventCollectionProcedure()%>"
						/>							
						</td>						
						
						<!-- RecievedEvent fields -->
						<td class="formFieldNoBordersSimple" width="5"rowspan="2"><b>*</b></td>
						<td class="formFieldNoBordersSimple"rowspan="2"> 
							<label for="quality">
								<b><bean:message key="receivedeventparameters.receivedquality"/></b> 
							</label>
						</td>						
						<!-- receivedeventparameters.receivedquality -->
						<td class="formFieldNoBordersSimple"rowspan="2">
							<autocomplete:AutoCompleteTag property="receivedEventReceivedQuality"
										  optionsList = "<%=request.getAttribute(Constants.RECEIVED_QUALITY_LIST)%>"
										  initialValue="<%=form.getReceivedEventReceivedQuality()%>"
						/>
						</td>
					</tr>
					
					<!-- CollectionEvent fields -->	
					<tr>							
							<td class="formFieldNoBordersSimple" width="5"><b>*</b></td>
							<td class="formFieldNoBordersSimple">
								<label for="container">
									<b><bean:message key="collectioneventparameters.container"/></b> 
								</label>
							</td>
							<td class="formFieldNoBordersSimple">
								<autocomplete:AutoCompleteTag property="collectionEventContainer"
										  optionsList = "<%=request.getAttribute(Constants.CONTAINER_LIST)%>"
										  initialValue="<%=form.getCollectionEventContainer()%>"
						    />
							</td>
					</tr>	
					
					<!-- comments -->
					<tr>	
						<!-- CollectionEvent fields -->	
						<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
						<td class="formFieldNoBordersSimple">
							<label for="comments">
								<bean:message key="eventparameters.comments"/> 
							</label>
						</td>
						<td class="formFieldNoBordersSimple">
							<html:textarea styleClass="formFieldSized20"  styleId="collectionEventComments" property="collectionEventComments" />
						</td>						
						
						<!-- RecievedEvent fields -->
						<td class="formFieldNoBordersSimple" width="5">&nbsp;</td>
						<td class="formFieldNoBordersSimple">
							<label for="comments">
								<bean:message key="eventparameters.comments"/> 
							</label>
						</td>						
						<td class="formFieldNoBordersSimple">
							<html:textarea styleClass="formFieldSized20"  styleId="receivedEventComments" property="receivedEventComments" />
						</td>						
						</table>
					</td>
				 	</tr>

				
				<!-- Mandar: 11-July-06 AutoEvents end  -->
				</table>	
				
