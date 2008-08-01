<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- 
	 @author Virender Mehta 
	 @version 1.1	
	 Jsp name: DefineConsent.jsp
	 Description: This jsp is associted with CollectionProtocol.jsp and it is for entering participant Consents	
	 Company: Washington University, School of Medicine, St. Louis.
-->						

<%-- Main table Start --%>
	<table style="display:none;"  width="100%" border="0" cellpadding="3" cellspacing="0" id="consentTierTable">
        <tr><td width="24%" align="left" class="black_ar">&nbsp;&nbsp;&nbsp;<bean:message key="consent.unsignedformurl" /></td>
            <td width="76%" align="left" valign="top"><label><html:text styleClass="black_ar" maxlength="50" size="42" styleId="unsignedConsentURLName" property="unsignedConsentURLName"/></label></td>
        </tr>    
		 <tr>
            <td colspan="2" align="left" class="toptd"></td>
         </tr>
     <tr>
          <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> <bean:message key="consent.consenttiers" /></span></td>
     </tr>
    <tr>
         <td colspan="2" align="left" class="black_ar showhide" >
		 <table width="100%" border="0" cellspacing="0" cellpadding="3" id="innertable">
         <tr>
         <%-- Title CheckBox --%>
		 	<logic:equal name="isParticipantReg" value="true">
			<td width="4%" align="center" class="tableheading">
			<label>
			<input type=checkbox class="black_ar" name="selectAll" onclick="checkAll(this)" disabled="disabled"/>
			</label>
			</td>
			</logic:equal>
			
			<logic:notEqual name="isParticipantReg" value="true"><td width="4%" align="center" class="tableheading">
			<label>
				<input type=checkbox name="selectAll" class="black_ar" onclick="checkAll(this)"/>
			</label>
			</td>
			</logic:notEqual>
          <td width="96%" class="tableheading"><strong><html:hidden property="consentTierCounter"/><bean:message key="consent.statements" /></strong></td>
        </tr>
	
	<logic:notEqual name="noOfConsents" value="0">
		<c:forEach var="counter" begin="0" end='${requestScope.noOfConsents -1}' step="1">
			<c:set var="consentName" value="consentValue(ConsentBean:${counter}_statement)" scope="request"/>
			<c:set var="consentKey" value="consentValue(ConsentBean:${counter}_consentTierID)" scope="request" />
			<c:set var="readonly" value="false" scope="page"/>
	        <tr>
		          <td align="center" class="black_ar">
		          	<logic:equal name="operation" value='${requestScope.edit}'>
		          		<c:set var="readonly" value="true" scope="page"/>
						<logic:equal name="isParticipantReg" value="true">
								<input type="checkbox" name="consentcheckBoxs" Id="check1" disabled="disabled"/>
							  </logic:equal>
							  <logic:notEqual name="isParticipantReg" value="true">
								<input type="checkbox" name="consentcheckBoxs" Id="check1"/>
							  </logic:notEqual>
					</logic:equal>
					<logic:notEqual name="operation" value='${requestScope.edit}'>
						<input type="checkbox" name="consentcheckBoxs" Id="check1" class="black_ar"/>
					</logic:notEqual>
	 			 </td>
		         <td class="link">
			  <label>
				<logic:equal name="isParticipantReg" value="true">
					<html:textarea styleClass="formFieldSized"  style="width:90%;" rows="2" property='${requestScope.consentName}' readonly='${pageScope.readonly}'/>
				</logic:equal>

				<logic:notEqual name="isParticipantReg" value="true">
					<html:textarea styleClass="formFieldSized"  style="width:90%;" rows="2" property='${requestScope.consentName}' />
				</logic:notEqual>
			  </label>
		  </td>
	        </tr>
		</c:forEach>
</logic:notEqual>
      </table></td>
    </tr>
    <tr>
      <td colspan="2" class="buttonbg">
      	<html:button property="addButton" styleClass="blue_ar_b" onclick="addConsentTier()" value="Add More" accesskey="A"/>&nbsp;|
      	<html:button property="removeButton" styleClass="blue_ar_b" onclick="deleteSelected()" value="Delete"/>
	</td>
    </tr>
  </form>
</table>
<%-- Main table End --%>