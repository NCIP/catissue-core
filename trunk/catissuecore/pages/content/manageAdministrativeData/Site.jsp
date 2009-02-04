<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SiteForm"%>


<%@ include file="/pages/content/common/AdminCommonCode.jsp" %>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
		String pageOf = (String) request.getAttribute(Constants.PAGEOF);

		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);

        String formName;

        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.SITE_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.SITE_ADD_ACTION;
            readOnlyValue = false;
        }
		SiteForm form = new SiteForm();
		Object obj = request.getAttribute("siteForm");
		if(obj != null && obj instanceof SiteForm)
			{
				form = (SiteForm)obj;
		}
    
//****************  Delete below commented code later  ***********************
    // ----------- add new    
       	//String reqPath = (String)request.getAttribute(Constants.REQ_PATH);
		//String appendingPath = "/Site.do?operation=add&pageOf=pageOfSite";
		//if (reqPath != null)
		//	appendingPath = reqPath + "|/Site.do?operation=add&pageOf=pageOfSite";
	
	   	//if(!operation.equals("add") )
	   	//{
	   	//	Object obj = request.getAttribute("siteForm");
		//	if(obj != null && obj instanceof SiteForm)
		//	{
		//		SiteForm form = (SiteForm)obj;
		//  		appendingPath = "/SiteSearch.do?operation=search&pageOf=pageOfSite&id="+form.getId() ;
		//   		System.out.println("---------- Site JSP appendingPath -------- : "+ appendingPath);
		//   	}
	   	//}
		
        
%>

<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>
<script>

    /***  code using ajax :gets the emailAddress of the coordinator without refreshing the whole page  ***/
	function onCoordinatorChange()
	{
		var request = newXMLHTTPReq();
		var handlerFunction = getReadyStateHandler(request,onResponseUpdate,true);
		
		//no brackets after the function name and no parameters are passed because we are assigning a reference to the function and not actually calling it
		request.onreadystatechange = handlerFunction;
		var action = "operation="+document.forms[0].operation.value+"&pageOf=pageOfSite&isOnChange=true&coordinatorId="+document.getElementById("coordinatorId").value;
		
		//Open connection to servlet
		request.open("POST","Site.do",true);	
		request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");	
		
		//send data to ActionServlet
		request.send(action);
	}

	function onResponseUpdate(emailAddress) 
	{
		document.getElementById("emailAddress").value = emailAddress;
	}

	/*** code using ajax  ***/
	
</script>
<script language="JavaScript" src="jss/script.js" type="text/javascript"></script>
<!-- Mandar : 434 : for tooltip -->
<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<html:errors />
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>


<html:form action="<%=formName%>">
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">	
	<!-- NEW SITE REGISTRATION BEGINS-->
		<tr>
		<td>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
				<tr>
					<td>
						<html:hidden property="operation" value="<%=operation%>" />
						<html:hidden property="submittedFor" value="<%=submittedFor%>"/>	
					</td>
				</tr>
				
				<tr>
					<td><html:hidden property="id" /></td>
					<td><html:hidden property="onSubmit"/></td>
				</tr>

				<tr>
					<td><html:hidden property="pageOf" value="<%=pageOf%>"/></td>
				</tr>

					<tr>
						<td class="formMessage" colspan="3">* indicates a required field</td>
					</tr>
					
					<tr>
						<td class="formTitle" height="20" colspan="3">
							<logic:equal name="operation" value="<%=Constants.ADD%>">
								<bean:message key="site.title"/>
							</logic:equal>
							<logic:equal name="operation" value="<%=Constants.EDIT%>">
								<bean:message key="site.editTitle"/>
							</logic:equal>
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="name">
								<bean:message key="site.name" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized" maxlength="255" size="30" styleId="name" property="name"/>
						</td>
					</tr>
			
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="type">
								<bean:message key="site.type"/>
							</label>
						</td>

						<td class="formField">
						
							 <autocomplete:AutoCompleteTag property="type"
										  optionsList = "<%=request.getAttribute(Constants.SITETYPELIST)%>"
										  initialValue="<%=form.getType()%>"
										  styleClass="formFieldSized"
									    />
							
						</td>
					</tr>
			
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="coordinator">
								<bean:message key="site.coordinator" />
							</label>
						</td>
						<td class="formField">
						
							<html:select property="coordinatorId" styleClass="formFieldSized" styleId="coordinatorId" size="1" onchange="onCoordinatorChange()"
							 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="userList" labelProperty="name" property="value"/>
							</html:select>
						
							
							&nbsp;
							<html:link href="#" styleId="newCoordinator" onclick="addNewAction('SiteAddNew.do?addNewForwardTo=coordinator&forwardTo=site&addNewFor=coordinator')">
								<bean:message key="buttons.addNew" />
							</html:link>
						</td>
					</tr>
		
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="emailAddress">
								<bean:message key="site.emailAddress" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized"  maxlength="255"  size="30" styleId="emailAddress" property="emailAddress" />
						</td>
					</tr>
									
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="street">
								<bean:message key="site.street" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized"  maxlength="50"  size="30" styleId="street" property="street" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="city">
								<bean:message key="site.city" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized"  maxlength="50"  size="30" styleId="city" property="city" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="state">
								<bean:message key="site.state" />
							</label>
						</td>
						<td class="formField">
						
						 <autocomplete:AutoCompleteTag property="state"
										  optionsList = "<%=request.getAttribute(Constants.STATELIST)%>"
										  initialValue="<%=form.getState()%>"
										  styleClass="formFieldSized"
									    />

						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="country">
								<bean:message key="site.country"/>
							</label>
						</td>

						<td class="formField">
						
						 <autocomplete:AutoCompleteTag property="country"
										  optionsList = "<%=request.getAttribute(Constants.COUNTRYLIST)%>"
										  initialValue="<%=form.getCountry()%>"
										  styleClass="formFieldSized"
									    />

						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
						<td class="formRequiredLabel">
							<label for="zipCode">
								<bean:message key="site.zipCode" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized"  maxlength="30"  size="30" styleId="zipCode" property="zipCode" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="phoneNumber">
								<bean:message key="site.phoneNumber" />
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized"  maxlength="50"  size="30" styleId="phoneNumber" property="phoneNumber" />
							<bean:message key="format.phoneNumber" />
						</td>
					</tr>
					
					<tr>
						<td class="formRequiredNotice" width="5">&nbsp;</td>
						<td class="formLabel">
							<label for="faxNumber">
								<bean:message key="site.faxNumber"/>
							</label>
						</td>
						<td class="formField">
							<html:text styleClass="formFieldSized"  maxlength="50"  size="30" styleId="faxNumber" property="faxNumber" />
						</td>
					</tr>
					
					<logic:equal name="<%=Constants.OPERATION%>" value="<%=Constants.EDIT%>">
					<tr>
						<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel">
								<label for="activityStatus">
									<bean:message key="site.activityStatus" />
								</label>
							</td>
						<td class="formField">
						
						<autocomplete:AutoCompleteTag property="activityStatus"
										  optionsList = "<%=request.getAttribute("activityStatusList")%>"
										  initialValue="<%=form.getActivityStatus()%>"
										  styleClass="formFieldSized"
										  onChange="<%=strCheckStatus%>"
									    />

						</td>
					</tr>
					</logic:equal>
					
					<tr>
						<td align="right" colspan="3">
						<!-- action buttons begins -->
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
									<html:submit styleClass="actionButton">
										<bean:message  key="buttons.submit" />
									</html:submit>
								</td>
								<%-- td>
									<html:reset styleClass="actionButton" >
										<bean:message  key="buttons.reset" />
									</html:reset>
								</td --%>
							</tr>
						</table>
						<!-- action buttons end -->
						</td>
					</tr>

			</table>
		</td></tr>
		<!-- NEW SITE REGISTRATION ends-->
</table>
</html:form>