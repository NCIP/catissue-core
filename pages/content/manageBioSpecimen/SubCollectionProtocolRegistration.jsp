 <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Variables"%>
<%@ page import="edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.AppUtility"%>
<%@ page import="java.util.*"%>	  
<%@ include file="/pages/content/common/BioSpecimenCommonCode.jsp" %>
<script src="jss/script.js" type="text/javascript"></script>
<!-- Mandar 11-Aug-06 : For calendar changes -->
<script src="jss/calendarComponent.js"></script>

<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<script language="javascript">
		
	refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','1');					
		</script>
<!-- Mandar 11-Aug-06 : calendar changes end -->
<%
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	   		Object obj = request.getAttribute("collectionProtocolRegistrationForm");
	CollectionProtocolRegistrationForm form =null;
	Integer registrationYear = null,registrationMonth=null,registrationDay=null;
	String currentRegistrationDate = "";

	String selectProperty="";
	if(obj != null && obj instanceof CollectionProtocolRegistrationForm)
	{
		form = (CollectionProtocolRegistrationForm)obj;
		currentRegistrationDate = form.getRegistrationDate();  
	
		if(currentRegistrationDate == null)
		{
			currentRegistrationDate = "";
		}
		else
		{
			registrationYear = new Integer(AppUtility.getYear(currentRegistrationDate ));
			registrationMonth = new Integer(AppUtility.getMonth(currentRegistrationDate ));
			registrationDay = new Integer(AppUtility.getDay(currentRegistrationDate ));
		}
		
			
	}
	String cpId = null,participantId=null;
	
	if(request.getAttribute(Constants.CP_SEARCH_CP_ID) != null)
	{
		cpId = (String)request.getAttribute(Constants.CP_SEARCH_CP_ID);
	}
	if(request.getAttribute(Constants.CP_SEARCH_PARTICIPANT_ID) != null)
	{
		participantId = (String)request.getAttribute(Constants.CP_SEARCH_PARTICIPANT_ID);
	}
		
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
		boolean isAddNew = false;
		
	    String operation = (String) request.getAttribute(Constants.OPERATION);
		        String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
		        String appendingPath = "/CollectionProtocolRegistration.do?operation=add&pageOf=pageOfCollectionProtocolRegistration";
		if (reqPath != null)
			appendingPath = reqPath + "|/CollectionProtocolRegistration.do?operation=add&pageOf=pageOfCollectionProtocolRegistration";
		        
		        String formName;

		        boolean readOnlyValue;
		        if (operation.equals(Constants.EDIT))
		        {						
		            formName = Constants.SUB_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION;
			if(pageOf.equals(Constants.PAGE_OF_COLLECTION_PROTOCOL_REGISTRATION_CP_QUERY))
			{
				formName = Constants.CP_QUERY_SUB_COLLECTION_PROTOCOL_REGISTRATION_EDIT_ACTION + "?pageOf="+pageOf+"&cpSearchCpId="+cpId+"&cpSearchParticipantId="+participantId;
			}
		            readOnlyValue = false;
		        }
		        else
		        {
		            formName = Constants.SUB_COLLECTIONP_ROTOCOL_REGISTRATION_ADD_ACTION;
			if(pageOf.equals(Constants.PAGE_OF_COLLECTION_PROTOCOL_REGISTRATION_CP_QUERY))
			{
				formName = Constants.CP_QUERY_SUB_COLLECTION_PROTOCOL_REGISTRATION_ADD_ACTION + "?pageOf="+pageOf+"&cpSearchCpId="+cpId+"&cpSearchParticipantId="+participantId;
			}
		            readOnlyValue = false;
		        }
%>

<head>
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<%if(pageOf.equals(Constants.PAGE_OF_COLLECTION_PROTOCOL_REGISTRATION_CP_QUERY))
	{

	%>
		<script language="javascript">

			
			//Changes made by Baljeet for Flexxxx related
		    top.frames["cpAndParticipantView"].refreshCpParticipants();
		    function showhide()
			{
				toggleLayer('wait'); 
				toggleLayer('summary');
			}
			function toggleLayer( whichLayer )
			{
			  var elem, vis;
			  if( document.getElementById ) // this is the way the standards work
			    elem = document.getElementById( whichLayer );
			  else if( document.all ) // this is the way old msie versions work
		    	  elem = document.all[whichLayer];
			  else if( document.layers ) // this is the way nn4 works
		  		  elem = document.layers[whichLayer];
		 	 vis = elem.style;
			  // if the style.display value is blank we try to figure it out here
			  if(vis.display==''&&elem.offsetWidth!=undefined&&elem.offsetHeight!=undefined)
			    vis.display = (elem.offsetWidth!=0&&elem.offsetHeight!=0)?'block':'none';
			  vis.display = (vis.display==''||vis.display=='block')?'none':'block';
		}
		 function registrationDateChange(newOffsetObject)
		 {
		  var originalDateOfRegistration= <%=registrationMonth.intValue()%> +"/"+<%=registrationDay.intValue()%> +"/"+<%=registrationYear.intValue()%>;
		var newRegistrationDate=dateChange(newOffsetObject,<%=form.getOffset()%>,originalDateOfRegistration);
		document.getElementById("registrationDate").value=newRegistrationDate;
		 }
		</script>
	<%}%>

	
</head>
<%
%>

<html:form action="<%=formName%>"  onsubmit="showhide()">
	
	<%
			String normalSubmitFunctionName = "setSubmittedFor('" + submittedFor+ "','" + Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[0][1]+"')";
			String forwardToSubmitFuctionName = "setSubmittedFor('ForwardTo','" + Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[1][1]+"')";									
			String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";
			String normalSubmit = normalSubmitFunctionName + ","+"showhide()"+","+confirmDisableFuncName;
			String forwardToSubmit = forwardToSubmitFuctionName + ","+confirmDisableFuncName;
	%>	
	
	<div id="summary">	
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">
	
		<!-- NEW Collection Protocol Registration ENTRY BEGINS-->
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td>
						<html:hidden property="operation" value="<%=operation%>" />
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>
						<html:hidden property="forwardTo" value=""/>
						<html:hidden property="participantID" />
						<html:hidden property="withdrawlButtonStatus"/>
						<html:hidden property="collectionProtocolID"/>
												
					</td>
					<td><html:hidden property="id"/>
					<td><html:hidden property="onSubmit"/></td>
					<html:hidden property="redirectTo" value="<%=reqPath%>"/>
					
				</tr>

				<tr>
					<td colspan="3"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
				</tr>
					
				<tr>
					<td class="formTitle" height="20" colspan="3">
						<logic:equal name="operation" value="<%=Constants.ADD%>">
							<bean:message key="collectionProtocolReg.add.title"/>
						</logic:equal>
						<logic:equal name="operation" value="<%=Constants.EDIT%>">
							<bean:message key="collectionProtocolReg.edit.title"/>
						</logic:equal>

					</td>
				</tr>
					
				<tr>
  			    	<td class="formRequiredNotice" width="5">*</td>					
				   	<td class="formRequiredLabel">
						<label for="name">
							<bean:message key="subcollectionProtocolReg.protocolTitle" />
						</label>
				   	</td>
					<td class="formField">
						<bean:write name="collectionProtocolRegistrationForm" property="collectionProtocolShortTitle"/>

					</td>
				</tr>
					
				<tr id="row0">				
			    	<td class="formRequiredNotice" width="5">&nbsp;</td>					
      	 	       	<td class="formLabel" nowrap>
                   		
							<label for="participantID">
								<bean:message key="collectionProtocolReg.participantName" />
							</label>
						
					</td>
					<td class="formField">
						<%--<html:text styleClass="formFieldSized" maxlength="10"  size="30" styleId="participantName" 
					     		property="participantName" disabled="true"/>	--%>

				
						&nbsp;<bean:write name="collectionProtocolRegistrationForm" property="participantName"/>
					</td>
				</tr>

				<tr id="row1">					
						
						<td class="formRequiredNotice" width="5">&nbsp;</td>					
						<td class="formLabel">
						
						<label for="name">
							<bean:message key="collectionProtocolReg.participantProtocolID" />
						</label>
					</td>
					<td class="formField">
						<html:hidden property="participantProtocolID"/>
						&nbsp;<bean:write name="collectionProtocolRegistrationForm" property="participantProtocolID"/>
					</td>
				</tr>
				<tr id="row2">					
						
						<td class="formRequiredNotice" width="5">&nbsp;</td>					
						<td class="formLabel">
						<label for="name">
							<bean:message key="collectionprotocol.studycalendartitle" />
						</label>
					</td>
					<td class="formField">
						&nbsp;<bean:write name="collectionProtocolRegistrationForm" property="studyCalEvtPoint"/>
					</td>
				</tr>
				<tr id="row3">					
						
						<td class="formRequiredNotice" width="5">&nbsp;</td>					
						<td class="formLabel">
						<label for="name">
							<bean:message key="subprotocolreg.offset" />
						</label>
					</td>
					
					<td class="formField">
					<html:text styleClass="formFieldSized" maxlength="25"  size="5" styleId="offset" property="offset" onblur="registrationDateChange(this)"/>
					</td>
				</tr>
 				<tr >
            		<td class="formRequiredNotice" width="5">*</td>					
					<td class="formRequiredLabel">
					    
						<label for="name">
							<bean:message key="collectionProtocolReg.participantRegistrationDate" />
						</label>
					</td>
					<td class="formField">
					<%
						 if(currentRegistrationDate.trim().length() > 0)
						 {
							
					%>
					<ncombo:DateTimeComponent name="registrationDate"
									  id="registrationDate"
 									  formName="collectionProtocolRegistrationForm"	
									  month= "<%=registrationMonth %>"
									  year= "<%=registrationYear %>"
									  day= "<%= registrationDay %>" 
									  pattern="<%=Variables.dateFormat%>"
									  value="<%=currentRegistrationDate %>"
									  styleClass="formDateSized10"
											 />		
					<% 
						}
						else
						{  
					 %>
					<ncombo:DateTimeComponent name="registrationDate"
									  id="registrationDate"
 									  formName="collectionProtocolRegistrationForm"	
 									  pattern="<%=Variables.dateFormat%>"
									  styleClass="formDateSized10" 
											 />		
					<%
						}
					%>
					<bean:message key="page.dateFormat" />&nbsp;

				 	</td>
				</tr>
			
			<!-- activitystatus -->	
				<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
				<tr>
					<td class="formRequiredNotice" width="5">*</td>
					<td class="formRequiredLabel" >
						<label for="activityStatus">
							<bean:message key="collectionprotocolregistration.activityStatus" />
						</label>
					</td>
					<td class="formField">
<!-- Mandar : 434 : for tooltip -->
						<html:select property="activityStatus" styleClass="formFieldSized10" styleId="activityStatus" size="1" onchange="<%=strCheckStatus%>"
						 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
							<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" labelName="<%=Constants.ACTIVITYSTATUSLIST%>" />
						</html:select>
					</td>
				</tr>
				</logic:equal>
				
				<tr>
					<td align="right" colspan="3">
					<%
    					String changeAction = "setFormAction('" + formName + "');";
			        %> 
				</tr>
			</table>
	

		</td>
		</tr>

		<!-- NEW Collection Protocol Registration ENTRY ends-->
	</table>
				
	 <table cellpadding="4" cellspacing="0" border="0">
	
	<tr>
		<td nowrap class="formFieldNoBorders">

		
			<html:button styleClass="actionButton" 
					property="submitPage" 
					title="Submit Only"
					value="<%=Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[0][0]%>" 
					onclick="<%=normalSubmit%>">				  				     	    
	     	</html:button>
	    
		</td>

		<td nowrap class="formFieldNoBorders"> 
			<html:button styleClass="actionButton"  
					property="submitPage" 
					title="Submit and Add Specimen collection group"
					value="<%=Constants.PROTOCOL_REGISTRATION_FORWARD_TO_LIST[1][0]%>"
					disabled="<%=isAddNew%>" 
					onclick="<%=forwardToSubmit%>">
	     	</html:button>
	     	
		</td>

	</tr>
</table>
</div>
<div id="wait" style="display:none;" >
	<logic:equal name="operation" value="<%=Constants.ADD%>">
		<bean:message key="subCollectionProtocolReg.add.msg"/>
	</logic:equal>
	<logic:equal name="operation" value="<%=Constants.EDIT%>">
		<bean:message key="subCollectionProtocolReg.edit.msg"/>
	</logic:equal>
</div>

</html:form>
