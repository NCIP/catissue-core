<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.bean.CollectionProtocolBean"%>

<head>
<%
    String access = (String)session.getAttribute("Access");
	String pageOf = request.getParameter("pageOf");
	String operation = "add";
	String operationType = null;
	boolean disabled = false;
	HttpSession newSession = request.getSession();
	CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean)newSession.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
	operationType = collectionProtocolBean.getOperation();
	if(operationType!=null && operationType.equals("update"))
	{
		disabled = true;
		operation="edit";
	}
	request.setAttribute(Constants.PAGE_OF,pageOf);
	boolean mac = false;
    Object os = request.getHeader("user-agent");
    if(os!=null && os.toString().toLowerCase().indexOf("mac")!=-1)
    {
         mac = true;
    }
	
	String frame1Ysize = "100%";
	String frame2Ysize = "200%";
	String frame3Ysize = "150%";
	String cpAndParticipantViewFrameHeight="50%";
	if(access != null && access.equals("Denied"))
	{
		cpAndParticipantViewFrameHeight="15%";
	}
	
	if(mac)
	{
		frame1Ysize = "180";
		frame2Ysize = "180";
		frame3Ysize = "600";
		if(access != null && access.equals("Denied"))
		{
			frame1Ysize = "80";
			frame2Ysize = "320";
		}
	}
%>

	<script src="jss/script.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script>
		function collectionProtocolPage()
		{
			var action ="CollectionProtocol.do?operation=<%=operation%>&pageOf=pageOfCollectionProtocol&invokeFunction=initCollectionProtocolPage";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
		function consentPage()
		{
			var action ="CollectionProtocol.do?operation=<%=operation%>&pageOf=pageOfCollectionProtocol&invokeFunction=initCollectionProtocolPage&tab=consentTab";
			document.forms[0].action = action;
			document.forms[0].submit();
		}
		
		function addNewEvent()
		{
			window.parent.frames['SpecimenRequirementView'].location="ProtocolEventsDetails.do?pageOf=newEvent&operation=add";
		}
		
		function viewSummary()
		{
			var action="DefineEvents.do?Event_Id=dummyId&pageOf=ViewSummary";
			document.forms[0].action=action;
			document.forms[0].submit();
		}

		function defineEvents()
		{
			var action="DefineEvents.do?pageOf=pageOfDefineEvents";
			document.forms[0].action=action;
			document.forms[0].submit();
		}
		
	</script>

</head>
<body>
<html:form action="CollectionProtocol.do">
	<table summary="" cellpadding="1" cellspacing="0" border="0" height="20" class="tabPage" width="700">
		<tr>
	 		<td height="20" width="10%" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" id="consentTab" onclick="collectionProtocolPage()">
		     <bean:message key="cpbasedentry.collectionprotocol" />
	        </td>

	        <td height="20" width="10%" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" id="consentTab" onclick="consentPage()">
			    <bean:message key="consents.consents" />        
	        </td>
			<logic:equal name="<%=Constants.PAGE_OF%>" value="<%=Constants.VIEW_SUMMARY%>">
					<td height="20" width="10%" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" 	onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="defineEvents()">
						<bean:message key="cpbasedentry.defineevents" />					
					</td>

					<td height="20" width="9%" nowrap class="tabMenuItemSelected">
						 <bean:message key="cpbasedentry.viewsummary" />
					</td>
			</logic:equal>
			<logic:notEqual name="<%=Constants.PAGE_OF%>" value="<%=Constants.VIEW_SUMMARY%>">
				<td height="20" width="9%" nowrap class="tabMenuItemSelected" id="collectionProtocolTab">
					<bean:message key="cpbasedentry.defineevents" />
				</td>
				
				<td height="20" width="10%" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" 	onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" id="consentTab" onclick="viewSummary()">
					 <bean:message key="cpbasedentry.viewsummary" />
				</td>
			</logic:notEqual>
						
	
	     <td width="600" class="tabMenuSeparator" colspan="1">&nbsp;</td>
	   </tr>
	   <tr>
		 <td class="tabField" colspan="6">
			<table border="0" height="100%" width="100%" cellpadding="0" cellspacing="0">
				<tr height="100%">
					<td width="22%" valign="top">
						<table border="0" width="250px" height="100%">
							<tr>
								<td align="left" valign="top">		
								<iframe id="SpecimenEvents" name="SpecimenEvents" src="ShowCollectionProtocol.do" scrolling="no" frameborder="0" width="250px" height="450px" marginheight=0 marginwidth=0 valign="top">
									Your Browser doesn't support IFrames.
								</iframe>

								</td>
							</tr>
							<tr>
								<td align="left" valign="top" nowrap>
									<html:button styleClass="actionButton" property="submitPage" onclick="defineEvents()" disabled = "<%=disabled%>">
										 <bean:message key="cpbasedentry.addnewevent" />
									</html:button>
									<logic:equal name="<%=Constants.PAGE_OF%>" value="<%=Constants.VIEW_SUMMARY%>">
										<html:button styleClass="actionButton" property="submitPage" onclick="defineEvents()">
											<< <bean:message key="cpbasedentry.defineevents" />
										</html:button>
									</logic:equal>
								</td>
							</tr>
                        </table>	
					</td>
					<!--P.G. - Start 24May07:Bug 4291:Added source as initial action for blank screen-->
					<logic:equal name="<%=Constants.PAGE_OF%>" value="<%=Constants.VIEW_SUMMARY%>">
					<td width="100%" height="100%" valign="top">
						<iframe name="SpecimenRequirementView" src="GenericSpecimenSummary.do?Event_Id=dummyId" scrolling="auto" frameborder="0" width="800px" height="500px">
							Your Browser doesn't support IFrames.
						</iframe>
					</td>
					</logic:equal>
					<logic:notEqual name="<%=Constants.PAGE_OF%>" value="<%=Constants.VIEW_SUMMARY%>">
						<td width="100%" height="100%" valign="top">
							<iframe name="SpecimenRequirementView" src="ProtocolEventsDetails.do?&operation=add&pageOf=newEvent" scrolling="auto" frameborder="0" width="800px" height="500px">
								Your Browser doesn't support IFrames.
							</iframe>
						</td>
						<tr>
							<td align="left" colspan="2">
								<html:button styleClass="actionButton" property="submitPage" onclick="consentPage()"> << <bean:message key="consent.addconsents" />	</html:button>

								<html:button styleClass="actionButton" property="submitPage" onclick="viewSummary()"> <bean:message key="cpbasedentry.viewsummary" /> >> </html:button>
							</td>
						</tr>
					</logic:notEqual>
					
					<!--P.G. - End -->
				</tr>
				
			</table>
		</td>
	</tr>
</table>
</html:form>
</body>