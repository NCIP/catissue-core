<!-- RequestOrder.jsp For making Request for the order-->
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>

<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>	
<html:form action="/RequestToOrderSubmit.do">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="orderingSystem.header" /></span></td>
        <td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Biospecimen Order" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
	 <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
	
		<tr>
          <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="100%" valign="top"><table border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><img src="images/uIEnhancementImages/no1A.gif" alt="Number1" width="25" height="21"></td>
                  <td class="black_ar" style="border-bottom:1px solid #f47c28;">&nbsp;<strong><bean:message key="orderingSystem.createOrder" /></strong> &nbsp; </td>
                  <td><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="30" height="1"></td>
                  <td><img src="images/uIEnhancementImages/no2.gif" alt="Number2" width="25" height="21"></td>
                  <td class="black_ar" style="border-bottom:1px solid #bababa;">&nbsp;<bean:message key="requestdetails.name" /> &nbsp; </td>
                </tr>
              </table></td>
              <td valign="top"><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="3" height="1"></td>
             
            </tr>
			<tr>
              <td valign="bottom" class="bottomtd"></td>
			  <td valign="bottom" class="bottomtd"></td>
			 </tr>
            <tr>
			<td>
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
			<table summary="" cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td class="black_ar_t"><strong><bean:message key="errors.ordering.note.heading" /></strong>
						</td>
						<td class="black_ar_t">
							<bean:message key="errors.ordering.note"/>
						</td>
					</tr>
					<tr>
						<td class="black_ar" colspan="2"> 
							<bean:message key="errors.ordering.note1"/>
						</td>
					</tr>
					<tr>
						<td class="black_ar" colspan="2">
							<bean:message key="errors.ordering.a"/>
						</td>
					<tr>
					<tr>
						<td class="black_ar" colspan="2">
							<bean:message key="errors.ordering.b"/>
						</td>
					</tr>
					<tr>
						<td class="black_ar" colspan="2">
							<bean:message key="errors.ordering.c"/>
						</td>
					</tr>
					<tr>
						<td class="black_ar" colspan="2">
							<bean:message key="errors.ordering.d"/>
						</td>
						
					</tr>
				</table>
				</td>
				</tr>
            <tr>
              <td valign="bottom" class="cp_tabbg">&nbsp;</td>
			  <td valign="bottom" >&nbsp;</td>
			 </tr>
			 <tr>
              <td valign="top" class="cp_tabtable"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td class="black_ar">
					  <table width="100%" border="0" cellpadding="3" cellspacing="0">
                <tr>
                  <td colspan="3" align="center" class="bottomtd"></td>
                  </tr>
                <tr>
                  <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td width="22%" align="left" class="black_ar">
					<label for="orderRequestName">
								<bean:message key="orderingSystem.orderListTitle" />
					</label>
					</td>
                  <td width="77%" align="left">
					<html:text styleClass="black_ar" maxlength="50" size="30" styleId="orderRequestName" property="orderRequestName"/>
				</td>
				</tr>
                <tr>
                  <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" valign="top" class="black_ar">
					<label for="distributionProtocol">
								<bean:message key="orderingsystem.label.distributionProtocol"/>
							</label>
					</label></td>
                  <td align="left" class="black_new">
						<html:select property="distributionProtocol" styleClass="formFieldSizedNew" styleId="distributionProtocol" 
									size="1" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="<%=Constants.DISTRIBUTIONPROTOCOLLIST%>" labelProperty="name" property="value"/>
						</html:select>
						</td>
						</tr>
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar_t">
						<label for="comments">
								<bean:message key="orderingsystem.label.comments"/>
							</label>
					</td>
                  <td align="left">
					<html:textarea styleClass="black_ar" rows="3" cols="90" styleId="comments" property="comments"/>
					</td>
                </tr>
                <tr>
                  <td colspan="3" class="black_ar">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="3" align="right" class="buttonbg">
						<html:submit styleClass="blue_ar_b" title="<%=ApplicationProperties.getValue("orderingsystem.tooltip.button.submit")%>">
											<bean:message  key="orderingsystem.button.submit"/>
										</html:submit>
					&nbsp;</td>
                </tr>
              </table></td>
            </tr>
            
          </table></td>
        </tr>
      </table>
    </td>
  </tr>
</table>

</html:form>