<!-- 
	This is the aliquot summary page.
	Author : Aniruddha Phadnis
	Date   : Jul 12, 2006
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.actionForm.AliquotForm"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.wustl.catissuecore.domain.Specimen"%>

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script language="JavaScript" >
		//Set last refresh time
		if(window.parent!=null)
		{
			if(window.parent.lastRefreshTime!=null)
			{
				window.parent.lastRefreshTime = new Date().getTime();
			}
		}	
</script>
<%
	String formName = Constants.ALIQUOT_SUMMARY_ACTION;
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String CPQuery = (String)request.getAttribute(Constants.CP_QUERY);
	if(CPQuery != null)
	{
		formName = Constants.CP_QUERY_ALIQUOT_SUMMARY_ACTION;
	String nodeId="Specimen_";
	if(request.getAttribute(Constants.PARENT_SPECIMEN_ID) != null )
	{
		String parentSPId = (String) request.getAttribute(Constants.PARENT_SPECIMEN_ID);
		nodeId = nodeId+parentSPId;%>
    
		<script language="javascript">
			refreshTree('<%=Constants.CP_AND_PARTICIPANT_VIEW%>','<%=Constants.CP_TREE_VIEW%>','<%=Constants.CP_SEARCH_CP_ID%>','<%=Constants.CP_SEARCH_PARTICIPANT_ID%>','<%=nodeId%>');			
		</script>
	<%}}%>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
<script>
	var newWindow;
	function showNewPage(action)
	{
	   	if(newWindow!=null)
		{
		   newWindow.close();
		}
	     newWindow = window.open(action,'','scrollbars=yes,status=yes,resizable=yes,width=860, height=600');
	     newWindow.focus(); 
	
    }
    
    function CPQueryAliquot(action)
	{
		window.parent.frames[1].location = action;
	}	
		
</script>
<html:form action="<%=formName%>">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<%
	AliquotForm form = (AliquotForm)request.getAttribute("aliquotForm");
	String unit = "";
	String conc = "";
	if(form.getConcentration()!=null)
	{
		conc = form.getConcentration();
	}

	if(form != null)
	{
		unit = Utility.getUnit(form.getSpecimenClass(),form.getType());
	}
%>

<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
	
	 						<!-- 							
							 * Patch ID: 3835_1_12
							 * See also: 1_1 to 1_5
							 * Description : Changed readonly text to labels
							-->	 
	
		
	<tr>
		<td class="formTitle" height="20" colspan="3">
			<bean:message key="aliquots.summaryTitle"/>
		</td>
	</tr>
	
	<%--tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="spCollectionGroupId">
				<bean:message key="specimen.specimenCollectionGroupId"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="spCollectionGroupId" property="spCollectionGroupId" readonly="true"/>
		</td>
	</tr--%>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="type">
				<bean:message key="specimen.type"/> 
			</label>
		</td>

		<td class="formField"> <%=form.getSpecimenClass()%>
			<html:hidden styleId="specimenClass" property="specimenClass" />
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="subType">
				<bean:message key="specimen.subType"/> 
			</label>
		</td>
		<td class="formField"> <%=form.getType()%>
			<html:hidden styleId="type" property="type" />
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="tissueSite">
				<bean:message key="specimen.tissueSite"/> 
			</label>
		</td>

		<td class="formField"> <%=form.getTissueSite()%>
			<html:hidden styleId="tissueSite" property="tissueSite" />
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="tissueSide">
				<bean:message key="specimen.tissueSide"/> 
			</label>
		</td>
		<td class="formField"> <%=form.getTissueSide()%>
			<html:hidden styleId="tissueSide" property="tissueSide" />
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="pathologicalStatus">
				<bean:message key="specimen.pathologicalStatus"/> 
			</label>
		</td>
		<td class="formField"> <%=form.getPathologicalStatus()%>
			<html:hidden styleId="pathologicalStatus" property="pathologicalStatus" />
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="availableQuantity">
				<bean:message key="specimen.availableQuantity"/> 
			</label>
		</td>
		<td class="formField"><%=form.getAvailableQuantity()%>&nbsp; <%=unit%>
			<html:hidden styleId="availableQuantity" property="availableQuantity" />
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="concentration">
				<bean:message key="specimen.concentration"/> 
			</label>
		</td>
		<td class="formField">
		<bean:write name="aliquotForm" property="concentration"/> &nbsp;
		<bean:message key="specimen.concentrationUnit"/>
			<html:hidden styleId="concentration" property="concentration" />		
		</td>
	</tr>
	 						<!-- 							
							 * Patch ID: 3835_1_13
							 * See also: 1_1 to 1_5
							 * Description : Added <TR> for createdOn date field .				 
							-->	 


	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="createdDate">
				<bean:message key="specimen.createdDate"/>
			</label>
		</td>
		<td class="formField"><%=form.getCreatedDate()%>&nbsp;<bean:message key="scecimen.dateformat"/>
			<html:hidden styleId="createdDate" property="createdDate" />	
			<html:hidden styleId="spCollectionGroupId" property="spCollectionGroupId" />
			<html:hidden styleId="scgName" property="scgName" />
		</td>
	</tr>
</table>
	
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
	<tr>
		<td class="formLeftSubTableTitle" width="5">
	     	#
	    </td>
	    <td class="formLeftSubTableTitle" width="5">
	     	<bean:message key="specimen.label"/>
	    </td>
	    <td class="formRightSubTableTitle">
			<bean:message key="specimen.quantity"/>
		</td>
		<td class="formRightSubTableTitle">
			<bean:message key="specimen.barcode"/>
		</td>
		<td class="formRightSubTableTitle">
			<bean:message key="aliquots.location"/>
		</td>
	</tr>
	
	<%
		int counter=0;
	%>
		<logic:iterate name="aliquotForm" property="specimenList" id="aliquot">
		<bean:define id="specimenId" name="aliquot" property="id"/> 
			<% 
				String onClickSpecimenFunction = "showNewPage('SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&id=" + specimenId + "')";
				if(CPQuery != null)
				{
					onClickSpecimenFunction = "CPQueryAliquot('QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&id=" + specimenId + "')";
				}
			%>
			
		<tr>
			<td class="formSerialNumberField" width="5">
		     	<%=++counter%>.
		    </td>
		    <td class="formField" width="5">
				<html:link href="#" styleId="label" onclick="<%=onClickSpecimenFunction%>">
					<bean:write name="aliquot" property="label"/>
					<html:hidden styleId="label" property="createdDate" />
				</html:link>
		    </td>
		    <td class="formField" nowrap>
				<bean:write name="aliquot" property="initialQuantity"/>		
					&nbsp; <%=unit%>
			</td>
			<td class="formField">
				<bean:write name="aliquot" property="barcode"/>			&nbsp;					
			</td>
			<td class="formField" nowrap>
				<logic:empty name="aliquot" property="positionDimensionOne">
					Virtually Located 
				</logic:empty>
				
				<logic:notEmpty name="aliquot" property="positionDimensionOne">
					<bean:define id="sc" name="aliquot" property="storageContainer"/> &nbsp;
					<bean:write name="sc" property="name"/> &nbsp;
					<bean:write name="aliquot" property="positionDimensionOne"/> &nbsp;
					<bean:write name="aliquot" property="positionDimensionTwo"/>
				</logic:notEmpty>
			</td>
		</tr>
	</logic:iterate>
</table>
</td>
</tr>
<tr colspan="5">
		<td align="right">
			<table width="100%">
				<tr>									
					<html:hidden property="submittedFor" value=""/>				
					<html:hidden property="forwardTo" value=""/>					
					<html:hidden property="noOfAliquots"/>					
					<%

						String confirmDisableFuncName = "confirmDisable('" + formName +"',document.forms[0].activityStatus)";			
						String addMoreSubmitFunctionName= "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[3][1]+"')";	
						String addMoreSubmit = addMoreSubmitFunctionName + ","+confirmDisableFuncName;
					%>						
					<td align="right" class="formFieldNoBorders" nowrap>	
						<html:button
							styleClass="actionButton" property="submitAndDistributeButton"
							title="<%=Constants.SPECIMEN_BUTTON_TIPS[3]%>"
							value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[3][0]%>"
							onclick="<%=addMoreSubmit%>">
						</html:button>
					</td>
				<tr>
			</table>
		</td>
</tr>
</table>
</html:form>