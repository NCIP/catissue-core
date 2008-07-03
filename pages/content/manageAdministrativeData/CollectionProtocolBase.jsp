<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="newMaintable">
  <tr>
    <td class="td_color_bfdcf3"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
      <tr>
        <td width="100%" colspan="2" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="3" valign="top" class="td_color_bfdcf3"><table width="18%" border="0" cellpadding="0" cellspacing="0" background="images/uIEnhancementImages/table_title_bg.gif">
                  <tr>
				                    <td width="82%">
										<span class="wh_ar_b">&nbsp;&nbsp;&nbsp;
											<bean:message key="CollectionProtocol.header" />
										</span>
									 </td>
				                     <td width="18%" align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Heading bg" width="31" height="24" /></td>
				                   </tr>
								</table>
							</td>
						  </tr>
				          <tr>
				             <td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;
							 </td>
				             <td width="9%" valign="top" class="td_tab_bg">&nbsp;
							 </td>
				             <td width="90%" valign="bottom" class="td_color_bfdcf3" style="padding-top:4px;">
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
				                  <tr>
									 <td width="4%" class="td_tab_bg" >&nbsp;
									 </td>
						<logic:equal parameter="operation"	value='add'>
				                     <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><img src="images/uIEnhancementImages/tab_add_user.jpg" alt="Add" width="57" height="22" /></td>
                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfCollectionProtocol&aliasName=CollectionProtocol"><img src="images/uIEnhancementImages/tab_edit_user.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
					</logic:equal>
					<logic:equal parameter="operation"	value='edit'>
					<td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><html:link page="/OpenCollectionProtocol.do?pageOf=pageOfmainCP&operation=add"><img src="images/uIEnhancementImages/tab_add_user1.jpg" alt="Add" width="57" height="22" /></html:link></td>
                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
					</logic:equal>
                    <td valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>

                    <td width="1%" align="left" valign="bottom" class="td_color_bfdcf3" >&nbsp;</td>
									
				                  </tr>
					           </table>
							</td>
			            </tr>
			        </table>
				</td>
		     </tr>
			
		     <tr>
				<td colspan="2" class="td_color_bfdcf3" style="padding-left:10px; padding-right:10px;padding-bottom:10px; ">
					<table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">
						
						 <tr>
							<td colspan="2" align="left"><span class=" grey_ar_s"><img	src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /><bean:message key="commonRequiredField.message" />
						 </span></td>
						</tr>
						<tr>
							<td width="20%"  valign="top" height="100%">
								<iframe id="CPTreeView" src="ShowCollectionProtocol.do?operation=${requestScope.operation}" scrolling="auto" frameborder="0" width="100%" name="CPTreeView" height="410" >
									Your Browser doesn't support IFrames.
								</iframe>
							 </td>
							 <td width="80%" colspan="3" valign="top" height="100%">
							 <logic:equal name="operation" value="add">
								<iframe name="SpecimenRequirementView"	src="CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol" scrolling="auto" frameborder="0" width="100%" height="410" >
									Your Browser doesn't support IFrames.
								</iframe>
							</logic:equal>
							 <logic:equal name="operation" value="edit">
								<iframe name="SpecimenRequirementView"	src="CollectionProtocol.do?operation=edit&pageOf=pageOfCollectionProtocol&invokeFunction=cp" scrolling="auto" frameborder="0" width="100%" height="410" >
									Your Browser doesn't support IFrames.
								</iframe>
							 </logic:equal>	
							 </td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</td>
 </tr>
</table>
