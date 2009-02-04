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

<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>

<html:errors/>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
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
		window.parent.frames[2].location = action;
	}	
		
</script>
<html:form action="<%=formName%>">
<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="600">

<%
	AliquotForm form = (AliquotForm)request.getAttribute("aliquotForm");
	String unit = "";

	if(form != null)
	{
		unit = Utility.getUnit(form.getSpecimenClass(),form.getType());
	}
%>

<tr>
<td>
	<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
		
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
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="specimenClass" property="specimenClass" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="subType">
				<bean:message key="specimen.subType"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="type" property="type" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="tissueSite">
				<bean:message key="specimen.tissueSite"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="tissueSite" property="tissueSite" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="tissueSide">
				<bean:message key="specimen.tissueSide"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="tissueSide" property="tissueSide" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="pathologicalStatus">
				<bean:message key="specimen.pathologicalStatus"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="pathologicalStatus" property="pathologicalStatus" readonly="true"/>
		</td>
	</tr>
	
	<tr>
		<td class="formRequiredNotice" width="5">&nbsp;</td>
		<td class="formRequiredLabel">
			<label for="availableQuantity">
				<bean:message key="specimen.availableQuantity"/> 
			</label>
		</td>
		<td class="formField">
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="availableQuantity" property="availableQuantity" readonly="true"/>
			&nbsp; <%=unit%>
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
			<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="concentration" property="concentration" readonly="true"/>
			&nbsp;<bean:message key="specimen.concentrationUnit"/>
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
		Map aliquotMap = new HashMap();
		int counter=0;

		if(form != null)
		{
			counter = Integer.parseInt(form.getNoOfAliquots());
			aliquotMap = form.getAliquotMap();
		}

		for(int i=1;i<=counter;i++)
		{
			String labelKey = "value(Specimen:" + i + "_label)";
			String qtyKey = "value(Specimen:" + i + "_quantity)";
			String barKey = "value(Specimen:" + i + "_barcode)";
			String containerKey = "value(Specimen:" + i + "_StorageContainer_id)";
			String containerNameKey = "value(Specimen:" + i + "_stContainerName)";
			String pos1Key = "value(Specimen:" + i + "_positionDimensionOne)";
			String pos2Key = "value(Specimen:" + i + "_positionDimensionTwo)";
			String idKey = "Specimen:" + i + "_id";
			String specimenLabelKey = "Specimen:" + i + "_label";
			String virtuallyLocatedKey = "Specimen:" + i + "_virtuallyLocated";
			String id = Utility.toString(aliquotMap.get(idKey));
			String onClickSpecimenFunction = "showNewPage('SearchObject.do?pageOf=pageOfNewSpecimen&operation=search&id=" + id + "')";
			if(CPQuery != null)
			{
				onClickSpecimenFunction = "CPQueryAliquot('QuerySpecimenSearch.do?pageOf=pageOfNewSpecimenCPQuery&id=" + id + "')";
			}
	%>
		<tr>
			<td class="formSerialNumberField" width="5">
		     	<%=i%>.
		    </td>
		    <td class="formField" width="5">
			
				<html:link href="#" styleId="label" onclick="<%=onClickSpecimenFunction%>">
				<%=aliquotMap.get(specimenLabelKey)%>			
				</html:link>
			     <html:hidden property="<%=labelKey%>"/>	
		    </td>
		    <td class="formField" nowrap>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="quantity" property="<%=qtyKey%>" readonly="true"/>
				&nbsp; <%=unit%>
			</td>
			<td class="formField">
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" styleId="barcodes" property="<%=barKey%>" readonly="true"/>
			</td>
			<td class="formField" nowrap>
				<%if(aliquotMap.get(virtuallyLocatedKey) != null){%>
				Virtually Located
				<%}else{%>
				<html:text styleClass="formFieldSized10"  maxlength="50"  size="30" property="<%=containerNameKey%>" readonly="true"/>
					&nbsp;
				<html:text styleClass="formFieldSized5"  maxlength="50"  size="30" property="<%=pos1Key%>" readonly="true"/>
					&nbsp;
				<html:text styleClass="formFieldSized5"  maxlength="50"  size="30" property="<%=pos2Key%>" readonly="true"/>
				<%}%>
			</td>
		</tr>
	<%
		} //For
	%>
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
						String distributeSubmitFuntionName= "setSubmittedFor('ForwardTo','" + Constants.SPECIMEN_FORWARD_TO_LIST[4][1]+"'),"; 			
						String addMoreSubmit = addMoreSubmitFunctionName + ","+confirmDisableFuncName;
						String submitAndDistribute = distributeSubmitFuntionName + confirmDisableFuncName;					
					%>						
					<td align="right" class="formFieldNoBorders" nowrap>	
						<html:button
							styleClass="actionButton" property="submitAndDistributeButton"
							title="<%=Constants.SPECIMEN_BUTTON_TIPS[3]%>"
							value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[3][0]%>"
							onclick="<%=addMoreSubmit%>">
						</html:button>
						
						<html:button
							styleClass="actionButton" property="submitAndDistributeButton"
							title="<%=Constants.SPECIMEN_BUTTON_TIPS[4]%>"
							value="<%=Constants.SPECIMEN_FORWARD_TO_LIST[4][0]%>"
							onclick="<%=submitAndDistribute%>">
						</html:button>
					</td>
				<tr>
			</table>
		</td>
</tr>
</table>
</html:form>