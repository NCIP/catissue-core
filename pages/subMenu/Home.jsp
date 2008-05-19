<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>



<table summary="" cellpadding="0" cellspacing="0" border="0" width="100%" height="100%">
<logic:notEmpty scope="session" name="<%=Constants.SESSION_DATA%>">
<%--<tr>
	<td class="subMenuPrimaryTitle" height="22">
		<a href="#content">
    		<img src="images/shim.gif" alt="Skip Menu" width="1" height="1" border="0" />
    	</a>
	</td>
</tr>--%>

<tr>
	<td colspan="3" class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /--> 
				<b> <bean:message key="app.userProfile" /> </b>
		</div>		
		
		<div>
			<a class="subMenuPrimary" href="UserSearch.do?pageOf=pageOfUserProfile&id=<bean:write name="<%=Constants.SESSION_DATA%>" property="userId" scope="session"/>" >
				<bean:message key="app.edit" />
			</a> |  
			<a class="subMenuPrimary" href="ChangePassword.do?operation=edit&amp;pageOf=pageOfChangePassword" >
					<bean:message key="user.changePassword" />
			</a>			
		</div>
	</td>
</tr>
<tr>
	<td colspan="3" class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		<div>
			<b> <bean:message key="app.Privileges" /> </b>
		</div>
		
		<div>
	  		<a class="subMenuPrimary" href="AssignPrivilegesPage.do?pageOf=pageOfAssignPrivilegesPage">
	  			<bean:message key="app.assign" />
	  		</a>
	  	</div>
	</td>
</tr>


</logic:notEmpty>
<tr height="1%">
	<td colspan="3" >&nbsp;
	</td>
</tr>
<tr>
	<td >
		&nbsp;
	</td>
	<td valign="top">
		<table cellpadding="0" cellspacing="0" width="100%">
		   <tr>
				 <td class="td_boxborder"   valign="top">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
						<tr class="wh_ar_b" height="34%">
							<td align="left" class="td_table_head">
								<img src="images/uIEnhancementImages/spacer.gif" width="10" height="10" align="absmiddle" />		<bean:message key="app.quickLinks" />
							</td>
							<td align="right" class="td_table_head" ><img src="images/uIEnhancementImages/table_title_corner.gif" width="31" height="24" /></td>
							<td width="20">&nbsp;</td>
						</tr>
						<tr height="66%">
							<td colspan="3" align="left" height="100%" >
								<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="4">
									 <tr height="50%">
										<td>&nbsp;<a href="https://cabig.nci.nih.gov/" target="_blank" class="blue"><span class="wh_ar_b"></span><bean:message key="app.cabigHome" /></a></td>
									 </tr>
									 <tr height="50%">
										<td>&nbsp;<a href="http://ncicb.nci.nih.gov/" target="_blank" class="blue"><span class="wh_ar_b"></span><bean:message key="app.ncicbHome" /></a></td>
									 </tr>
								 </table>
							</td>
						</tr>
					</table>
				</td>
				<td width="5" height="100%" background="images/uIEnhancementImages/shadow_right.gif" valign="top"></td>
					
			</tr>
			<tr>
				  
				<td valign="top" background="images/uIEnhancementImages/shadow_down.gif" ></td>
				<td height="5"><img src="images/uIEnhancementImages/shadow_corner.gif" width="5" height="5" /></td>
				
			</tr>
		</table>
	</td>
	<td >
		&nbsp;
	</td>
</tr>

<tr>
	<td colspan="3" height="175">
	&nbsp;
	</td>
</tr>

<tr >
		  <td colspan="3" valign="bottom" height="20"  align="left" height="100%">
			<table width="95%" border="0" cellpadding="0" cellspacing="0" height="100%">
			  <tr height="100%">

				<td height="178" align="center" background="images/uIEnhancementImages/box_bg.gif" style="background-repeat:no-repeat; background-position:center;">
					<table width="100%" border="0" cellspacing="0" cellpadding="4">
						<tr>
						  <td width="50%" height="72" align="center"><p><a href="http://www.cancer.gov/"><img src="images/uIEnhancementImages/logo1.gif" width="57" height="50" border="0" /></a><br />
						  </p></td>
						  <td width="50%" align="center"><a href="http://www.nih.gov/"><img src="images/uIEnhancementImages/logo2.gif" width="56" height="50" border="0" /></a></td>
						</tr>
						<tr>
						  <td align="center"><a href="http://www.dhhs.gov/"><img src="images/uIEnhancementImages/logo3.gif" width="54" height="50" border="0" /></a></td>
						  <td align="center"><a href="http://www.firstgov.gov/"><img src="images/uIEnhancementImages/logo4.gif" width="92" height="50" border="0" /></a></td>

						</tr>
					</table>
				</td>
			  </tr>
		   </table>
	     </td>
</tr>
	</table> 
	 







     
     
    
         
    
     
     
   
