<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>

<script language="JavaScript">
	function onRadioButtonClick(element)
	{
		var action="AdvanceQueryInterface.do?pageOf=pageOfAdvanceQueryInterface&objectName=";
		if(element.value == 1)
		{
			action="AdvanceQueryInterface.do?pageOf=pageOfAdvanceQueryInterface&selectedNode=-1";
		}
		else if(element.value == 2)
		{
			action="AdvanceQueryInterface.do?pageOf=pageOfAdvanceQueryInterface&selectedNode=-1&objectName=" + "<%=Constants.PARTICIPANT%>";
		}
		else if(element.value == 3)
		{
			action="AdvanceQueryInterface.do?pageOf=pageOfAdvanceQueryInterface&selectedNode=-1&objectName=" + "<%=Constants.COLLECTION_PROTOCOL%>";
		}
		else if(element.value == 4)
		{
			action="AdvanceQueryInterface.do?pageOf=pageOfAdvanceQueryInterface&selectedNode=-1&objectName=" + "<%=Constants.SPECIMEN_COLLECTION_GROUP%>";
		}
		else
			return;
		document.forms[0].action=action;
	}
</script>

<table summary="" cellpadding="0" cellspacing="0" border="0" class="contentPage" width="620">
<html:form action="AdvanceQueryInterface.do?pageOf=pageOfAdvanceQueryInterface&selectedNode=-1" method="post">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table cellpadding="3" cellspacing="0" border="0" width="100%">
				<tr>
					 <td class="formTitle" height="20" colspan="3">
						<bean:message key="advanceQuery.title"/>
					 </td>
				</tr>
				<tr>
					<td class="formRequiredNotice" width="5">&nbsp;</td>
					<td class="formRequiredLabel">
						<label for="msg">
							<bean:message key="advanceQuery.defaultMessage"/>
						</label>
					</td>
					<td class="formField">
						<table>
							<tr>
								<td class="standardText">
									<input value="1" checked="checked" onclick="onRadioButtonClick(this)" class="" name="checkedButton" id="checkedButton" type="radio"/><%=Constants.PARTICIPANT%>
								</td>
							</tr>
							<tr>
								<td class="standardText">
									<input value="2" onclick="onRadioButtonClick(this)" class="" name="checkedButton" id="checkedButton" type="radio"/><%=Constants.MENU_COLLECTION_PROTOCOL%>
								</td>
							</tr>
							<tr>
								<td class="standardText">
									<input value="3" onclick="onRadioButtonClick(this)" class="" name="checkedButton" id="checkedButton" type="radio"/><%=Constants.MENU_SPECIMEN_COLLECTION_GROUP%>
								</td>
							</tr>
							<tr>
								<td class="standardText">
									<input value="4" onclick="onRadioButtonClick(this)" class="" name="checkedButton" id="checkedButton" type="radio"/><%=Constants.SPECIMEN%>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="right" colspan="3">
						<table cellpadding="4" cellspacing="0" border="0">
							<tr>
								<td>
									<html:submit styleClass="actionButton">
										<bean:message  key="buttons.submit"/>
									</html:submit>
								<td>
							</tr>
						</table>
					</td>
				</tr>			
			</table>
	 	</td>
	</tr>
</html:form>
</table>
