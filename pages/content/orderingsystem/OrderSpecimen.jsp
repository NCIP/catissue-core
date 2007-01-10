<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<!-- Include external css and js files-->
<LINK REL=StyleSheet HREF="css/styleSheet.css" TYPE="text/css">
<script language="JavaScript" type="text/javascript" src="jss/script.js"></script>

<html:form action="/ShoppingCart.do">
<table summary="" cellpadding="0" cellspacing="0" border="0" height="30"  class="tabPage" width="100%">
			
			<tr style="background-color:#AAAAAB">
				<td height="30" class="tabMenuItemSelected" nowrap>				
					<bean:message key="tab.orderingSystem.biospecimen"/>				
				</td>
				
				<td height="30" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="featureNotSupported()" nowrap>				 
					<bean:message key="tab.orderingSystem.pathologicalCase"/>			
				</td>

				<td height="30" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="featureNotSupported()">				 
					<bean:message key="tab.orderingSystem.biospecimenArray"/>				
				</td>
				
				<td width="10%" class="tabMenuSeparator">
					<input type="submit" name="btnSubmit" value="Define Array" onclick="featureNotSupported()"/> 
				</td>

			    
			 </tr>
			 
			 <tr><td colspan='4' class="tabField"> <br> </td></tr>
			
			<tr>
				  <td height="20" class="formTitle" colspan='4' >					
						<bean:message key="label.orderingSystem.requestDetails"/>
		   		  </td>
			</tr>
										  
			<tr>
			  	<td style="border-left:1px solid #5C5C5C" colspan="6">
					<table summary="" cellpadding="0" cellspacing="0" border="0">		
						<tr>
							<td colspan='4'  class="formMessage">	
								<bean:message key="requiredfield.message"/>
							</td>
					    </tr>	
						<tr>
							<td>
									<!-- Include logic here done by deepti -->
							</td>
						</tr>
					</table>								  
			   
				</td>
			 </tr>

			 <tr>
				<td align="right" colspan='4' class="tabField">
						<br><br><br><br><br>
						<br><br><br><br><br>
						<br><br><br><br><br>
						<br><br><br><br><br>
						<br><br><br><br><br>
						<br><br><br><br><br>
						<BR>		
				  	<html:submit styleClass="actionButton" value="OrderToList" />
				</td>		 
			</tr>		  			  
</table>

</html:form>

