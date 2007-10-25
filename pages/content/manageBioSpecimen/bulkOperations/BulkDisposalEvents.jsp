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
				var fields = document.getElementsByTagName("select");
				var i =0;
				var text="";
				var valueToSet = "";
				var isFirstField = true;
				for (i=0; i<fields.length;i++)
				{
					text = fields[i].name;
					if(text.indexOf("_STATUS")>=0)
					{
						if(isFirstField)
						{
							valueToSet = fields[i].value;
							isFirstField = false;
						}
						fields[i].value = valueToSet;
					}
				}
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
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel" width="25%">
						<label for="type">
							<bean:message key="disposaleventparameters.reason" /> 
						</label>
					</td>
					<td class="formField">
						<html:textarea styleClass="formFieldSized"  styleId="comments" property="fieldValue(ID_ALL_REASON)" />
					</td>
				</tr>
					
				
				<tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formLabel" width="25%">
						Specimen Labels
					</td>
					<td class="formLabel">
						<%
							String specimenList = "";
							String specimenLabelField = "";
							String commaString = "";
							
						%>
						<logic:iterate id="specimenId" name="<%=Constants.SPECIMEN_ID_LIST%>" scope="request" indexId="id">
							<%
								specimenList = "specimenId("+specimenId+")";
								specimenLabelField = "fieldValue(ID_"+specimenId+"_LABEL)";
								if( id != 0 )
								{
									commaString = ", ";
								}
							%>
							<html:hidden property="<%=specimenList%>" />
							<%=commaString%>
							<label for="type">
								<bean:write name="bulkEventOperationsForm" property="<%=specimenLabelField%>" />
							</label>
						</logic:iterate>
						
					</td>
				</tr>
				
				<tr>
									<td class="formRequiredNotice" width="5">&nbsp;</td>
									<td class="formLabel" width="25%">
										Status
									</td>
									<td class="formLabel">
										<html:select styleId="comments" property="fieldValue(ID_ALL_STATUS)" styleClass="formFieldSized4" size="1">
												<html:options name="<%=Constants.ACTIVITYSTATUSLIST%>" />
										</html:select>
									</td>
				</tr>
			</table>
	</table>
	
	<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="60%">
		<tr>
		<td>
		<table summary="" cellpadding="3" cellspacing="0" border="0" width="100%">
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
		</td>
				
		</tr>
		
	</table>
</html:form>