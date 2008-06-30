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
      <tr>
       <td width="24%" align="left" class="black_ar" style="padding-top:20px; padding-bottom:10px;">
		<bean:message key="consent.unsignedformurl" />
	  </td>
       <td width="76%" align="left" style="padding-top:20px; padding-bottom:10px;"><label>
        <html:text styleClass="black_ar" maxlength="50" size="42" styleId="unsignedConsentURLName" property="unsignedConsentURLName"/>
      </label></td>
    </tr>    
     <tr>
      <td colspan="2" align="left" class="tr_bg_blue1">
		<span class="blue_ar_b">	<bean:message key="consent.consenttiers" /></span></td>
    </tr>
    <tr>
      <td colspan="2" align="left" class="black_ar" style="padding-top:20px; padding-bottom:10px;"><table width="100%" border="0" cellspacing="0" cellpadding="3" id="innertable">
        <tr>
         <%-- Title CheckBox --%>
									<logic:equal name="operation" value='${requestScope.edit}'>										
      							    <td width="4%" align="center" class="tableheading">
										<div align="center">
											<input type=checkbox class="black_ar" name="selectAll" onclick="checkAll(this)" disabled="disabled"/>
										</div>	
									</td>
									</logic:equal>
									<logic:notEqual name="operation" value='${requestScope.edit}'>
									<td class="tableheading" width="4%" align="center">
										<div align="center">
											<input type=checkbox name="selectAll" class="black_ar" onclick="checkAll(this)"/>
										</div>	
									</td>	
									</logic:notEqual>
          <td width="96%" class="tableheading">
			<html:hidden property="consentTierCounter"/>
										<bean:message key="consent.statements" />
			</td>
        </tr>

						<c:forEach var="counter" begin="0" end='${requestScope.noOfConsents}' step="1">
								<c:set var="consentName" value="consentValue(ConsentBean:${counter}_statement)" scope="request"/>
								<c:set var="consentKey" value="consentValue(ConsentBean:${counter}_consentTierID)" scope="request" />
								<c:set var="readonly" value="false" scope="page"/>
        <tr>
          <td align="center" class="black_ar">
						 <logic:equal name="operation" value='${requestScope.edit}'>
									<c:set var="readonly" value="true" scope="page"/>
									
									<input type="checkbox" name="consentcheckBoxs" Id="check1" disabled="disabled"/>
									</logic:equal>
									<logic:notEqual name="operation" value='${requestScope.edit}'>
									<input type="checkbox" name="consentcheckBoxs" Id="check1"/>
									</logic:notEqual>
		  </td>
		  <html:hidden property='${requestScope.consentKey}'/>
          <td class="link"><label>
<html:textarea styleClass="formFieldSized"  style="width:90%;" rows="2" property='${requestScope.consentName}' readonly='${pageScope.readonly}'/>
          </label></td>
        </tr>
		</c:forEach>

      </table></td>
    </tr>
    <tr>
      <td colspan="2" class="buttonbg"><html:button property="addButton" styleClass="blue_ar_b" onclick="addConsentTier()" value="Add More"/>
      &nbsp;|
      <html:button property="removeButton" styleClass="blue_ar_b" onclick="deleteSelected()" value="Delete"/></td>
    </tr>
  </form>
</table>
<%-- Main table End --%>