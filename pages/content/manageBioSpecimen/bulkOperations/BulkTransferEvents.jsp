<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ include file="/pages/content/common/AutocompleterCommon.jsp" %> 
<head>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
</head>

<script language="JavaScript" type="text/javascript">
	
		function ApplyToAll()
		{
			if(document.getElementById("chk").checked)
			{
				var fields = document.getElementsByTagName("input");
				var i =0;
				var text="";
				var valueToSet = "";
				var isFirstField = true;
				var isFirstFieldDisabled = false;
				for (i=0; i<fields.length;i++)
				{
					text = fields[i].name;
					if(text.indexOf("_TOSCLABEL")>=0)
					{
						if(isFirstField)
						{
							if(!fields[i].disabled)
							{
								valueToSet = fields[i].value;
							}
							else
							{
								valueToSet = "";
								isFirstFieldDisabled = true;								
							}
							isFirstField = false;
						}
						if(isFirstFieldDisabled)
						{
							fields[i].disabled = true;
						}
						else 
						{
							fields[i].disabled = false;
						}
						
						fields[i].value = valueToSet;
						
						
					}
					else if(text.indexOf("_TOVirLoc")>=0)
					{
						if(isFirstFieldDisabled)
						{
							fields[i].checked = true;
						}
						else
						{
							fields[i].checked = false;
						}
					}
				}
			}
		}
		
		function virtualLocationSelChanged(specimenId)
		{
			if(document.getElementById("VirLocChk"+specimenId).checked)
			{
				document.getElementById("SelCont"+specimenId).value = "";
				document.getElementById("SelCont"+specimenId).disabled = true;
			}
			else
			{
				document.getElementById("SelCont"+specimenId).value = "";
				document.getElementById("SelCont"+specimenId).disabled = false;
			}
		}
	
</script>
<html:form action="BulkTransferEventsSubmit.do" >
	<jsp:include page="/pages/content/manageBioSpecimen/bulkOperations/BulkEventsCommonAttributes.jsp" />
	
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="60%">
		<tr>
		<td>
		<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
			<tr>
				
				<td class="formRightSubTableTitleWithBorder">
					Label
				</td>
				<td class="formRightSubTableTitleWithBorder">
					From Location
				</td>
				<td class="formRightSubTableTitleWithBorder">
					To Location <input id="chk" type="checkbox" onClick="ApplyToAll()"/> Apply to all
				</td>
				
			</tr>
			
			<logic:iterate id="specimenId" name="<%=Constants.SPECIMEN_ID_LIST%>" scope="request" indexId="id">
				<%
					String specimenLabelField = "fieldValue(ID_"+specimenId+"_LABEL)";
					String specimenFromLocField = "fieldValue(ID_"+specimenId+"_FROMLOC)";
					String specimenFromLocIDField = "fieldValue(ID_"+specimenId+"_FROMLOCID)";
					String specimenFromLocPos1Field = "fieldValue(ID_"+specimenId+"_FROMLOCPOS1)";
					String specimenFromLocPos2Field = "fieldValue(ID_"+specimenId+"_FROMLOCPOS2)";
					String specimenToSCLabelField = "fieldValue(ID_"+specimenId+"_TOSCLABEL)";
					String specimenToSCIDField = "fieldValue(ID_"+specimenId+"_TOSCID)";
					String specimenToSCPos1Field = "fieldValue(ID_"+specimenId+"_TOSCPOS1)";
					String specimenToSCPos2Field = "fieldValue(ID_"+specimenId+"_TOSCPOS2)";
					
					String containerId = "Cont"+specimenId;
					String selContainerId = "SelCont"+specimenId;
					String pos1Id = "Pos1Id"+specimenId;
					String pos2Id = "Pos2Id"+specimenId;
					
					String specimenList = "specimenId("+specimenId+")";
					String specimenToVirLocField = "fieldValue(ID_"+specimenId+"_TOVirLoc)";
					String methodCall = "virtualLocationSelChanged("+specimenId+")";
					String virLoc = "VirLocChk"+specimenId;
										
				%>
				<tr>
					
					<html:hidden property="<%=specimenFromLocIDField%>" />
					<html:hidden property="<%=specimenFromLocPos1Field%>" />
					<html:hidden property="<%=specimenFromLocPos2Field%>" />
					<html:hidden property="<%=specimenList%>" />
					<html:hidden property="<%=specimenFromLocField%>" />
				
					<td class="formLabelWithLeftBorder">
						<label for="type">
							<bean:write name="bulkEventOperationsForm" property="<%=specimenLabelField%>" />
						</label>
					</td>
					<td class="formLabel">
						<label for="type">
							<bean:write name="bulkEventOperationsForm" property="<%=specimenFromLocField%>" />
						</label>
						<!--html:text styleId="<%=containerId%>" property="<%=specimenFromLocField%>" readonly="true" /-->
					</td>
					
					<!-- To Container Field starts -->
					
					
					<td class="formField">
						<logic:equal name="bulkEventOperationsForm" property="<%=specimenToVirLocField%>" value="true" >
							<html:text styleId="<%=selContainerId%>" property="<%=specimenToSCLabelField%>" disabled="true" />			
						</logic:equal>
						<logic:notEqual name="bulkEventOperationsForm" property="<%=specimenToVirLocField%>" value="true" >
							<html:text styleId="<%=selContainerId%>" property="<%=specimenToSCLabelField%>" disabled="false" />			
						</logic:notEqual>
						<html:checkbox styleId="<%=virLoc%>" property="<%=specimenToVirLocField%>" onclick="<%=methodCall%>" value="true"/> Is virtually located
					</td>					
					<!-- To Container Field ends -->
					
					<!--td>
						<html:text styleId="<%=pos1Id%>" property="<%=specimenToSCPos1Field%>" disabled="false" />			
					</td>
					<td>
						<html:text styleId="<%=pos2Id%>" property="<%=specimenToSCPos2Field%>" disabled="false" />
					</td>
					<td>
	
					<%String className = (String) request.getAttribute(Constants.SPECIMEN_CLASS_NAME);
						if (className==null)
							className="";
	
						String collectionProtocolId =(String) request.getAttribute(Constants.COLLECTION_PROTOCOL_ID);
						if (collectionProtocolId==null)
							collectionProtocolId=""; 
						String url = "ShowFramedPage.do?pageOf=pageOfSpecimen&amp;selectedContainerName="+specimenToSCIDField+"&amp;pos1="+pos1Id+"&amp;pos2="+pos2Id+"&amp;containerId="+containerId
								+ "&" + Constants.CAN_HOLD_SPECIMEN_CLASS+"="+Constants.TISSUE
								+ "&" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=1";		
	
						String buttonOnClicked = "mapButtonClickedOnSpecimen('"+url+"','transferEvents','"+selContainerId+"')";	%>
						<html:button styleClass="actionButton" property="containerMap" onclick="<%=buttonOnClicked%>">
							<bean:message key="buttons.map"/>
						</html:button>
					</td-->
				
				</tr>
			</logic:iterate>
		</table>
		</td>
				
		</tr>
		<tr>
			<td align="right"> 
				<table border="0" cellpadding="4" cellspacing="0">
				
					<tbody><tr>
						<td>
							<html:submit styleClass="actionButton"/>
						</td>

					</tr>
				</tbody></table>
			</td>
		</tr>
	</table>
</html:form>