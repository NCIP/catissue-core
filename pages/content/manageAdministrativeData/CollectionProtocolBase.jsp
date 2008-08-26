<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
	function openEventPage()
	{
		var formId=window.frames['SpecimenRequirementView'].document.getElementById('CollectionProtocolForm');
		if(formId!=null)
		{
		    var action="DefineEvents.do?pageOf=pageOfDefineEvents&operation=add";
		}
		else
		{
			formId=window.frames['SpecimenRequirementView'].document.getElementById('protocolEventDetailsForm');
			var action = "SaveProtocolEvents.do?pageOf=newEvent&operation=add";
			if(formId==null)
			{
				var formId=window.frames['SpecimenRequirementView'].document.getElementById('createSpecimenTemplateForm');
				var action = "CreateSpecimenTemplate.do?pageOf=newEvent&operation=add";
			}
		}
	    formId.action=action;
	    formId.submit();
	}

	function submitCP()
	{
		var formId=window.frames['SpecimenRequirementView'].document.getElementById('CollectionProtocolForm');
		if(formId!=null)
		{
			var action="DefineEvents.do?Event_Id=dummyId&pageOf=submitSpecimen&operation=${requestScope.operation}";
		}
		else
		{
			var formId=window.frames['SpecimenRequirementView'].document.getElementById('protocolEventDetailsForm');
			if(formId==null)
			{
				var formId=window.frames['SpecimenRequirementView'].document.getElementById('createSpecimenTemplateForm');
			}
			var action="SubmitCollectionProtocol.do?operation=${requestScope.operation}";
		}
		formId.target = '_top';
		formId.action=action;
        formId.submit();
	}


</script>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
 <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="app.collectionProtocol"/></span></td>
        <td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td ><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" height="1"></td>
        <logic:equal parameter="operation"	value='add'>
				      <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfCollectionProtocol&aliasName=CollectionProtocol"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
					</logic:equal>
					<logic:equal parameter="operation"	value='edit'>
					<td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><html:link page="/OpenCollectionProtocol.do?pageOf=pageOfmainCP&operation=add"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" /></html:link></td>
                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
					</logic:equal>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>


		
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
	  <logic:equal name="isParticipantReg" value="true">
        <tr>
          <td colspan="2" align="left">
          
			<span class="messagetexterror">
				User can not Add/edit Events and Specimen Requirements
			</span>
		
				<%@ include file="/pages/content/common/ActionErrors.jsp" %>
		  </td>
        </tr>
		</logic:equal>
       		<tr>
				<td width="20%"  valign="top">
					<iframe id="CPTreeView" src="ShowCollectionProtocol.do?operation=${requestScope.operation}" scrolling="auto" frameborder="0" width="100%" name="CPTreeView" height="400" >
							<bean:message key="errors.browser.not.supports.iframe"/>
					</iframe>
				</td>
							 <td width="80%" valign="top" >
							 <logic:equal name="operation" value="add">
								<iframe name="SpecimenRequirementView"	id="SpecimenRequirementView" src="CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol" scrolling="auto" frameborder="0" width="100%" height="400" >
									<bean:message key="errors.browser.not.supports.iframe"/>
								</iframe>
							</logic:equal>
							 <logic:equal name="operation" value="edit">
								<iframe name="SpecimenRequirementView"	src="CollectionProtocol.do?operation=edit&pageOf=pageOfCollectionProtocol&invokeFunction=cp" scrolling="auto" frameborder="0" width="100%" height="400" >
									<bean:message key="errors.browser.not.supports.iframe"/>
								</iframe>
							 </logic:equal>	
							 </td>
						</tr>
					
		
 <tr>
		 <td colspan="2">
		    <table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr >
					<td class="buttonbg" >
					<logic:equal name="isParticipantReg" value="true">
						   &nbsp;
					</logic:equal>
					<logic:notEqual name="isParticipantReg" value="true">
						
						<html:button styleClass="blue_ar_b" property="forwardPage" onclick="openEventPage()" >
							Add Events >>
						</html:button>
						&nbsp;|&nbsp;
					</logic:notEqual>
					
					
					 <html:button styleClass="blue_ar_b" property="forwardPage" value="Save Collection Protocol" onclick="submitCP()">
					</html:button>
				   </td>
				   </tr>
				   </table>
				   </td>
				</tr>
				</table>
	</td>
 </tr>
</table>
