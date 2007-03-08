<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="edu.wustl.catissuecore.util.global.Utility"%>
<%@ include file="/pages/subMenu/SelectMenu.jsp" %>

<tr>
    <td class="subMenuPrimaryTitle" height="22">
		<a href="#content">
    		<img src="images/shim.gif" alt="Skip Menu" width="1" height="1" border="0" />
    	</a>
	</td>
  </tr>
  
<!-- menu id : 17 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 17, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>	
		<div>
			<b> <bean:message key="app.search" /> </b>
		</div>		
		
		<div>
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfSimpleQueryInterface&menuSelected=17">
				<bean:message key="app.simpleSearch" />
			</a> |  
			<a class="subMenuPrimary" href="AdvanceQueryInterfaceDefaultPage.do?pageOf=pageOfAdvanceQueryInterface&menuSelected=17">
					<bean:message key="app.advancedSearch" />
			</a> |
			<a class="subMenuPrimary" href="QueryWizard.do?currentPage=QueryWizard">
				<bean:message key="query.name" />
			</a>	
		</div>
	</td>
</tr>

<%--tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
	  <div>
      		<a class="subMenuPrimary" href="ShowFramedPage.do?pageOf=pageOfStorageLocation">
	  			<bean:message key="app.queryResultView" />
	  		</a>
      </div>
	</td>
</tr>

<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
	  <div>
      		<a class="subMenuPrimary" href="#">
	  			<bean:message key="app.viewStoredQueries" />
	  		</a>
      </div>
	</td>
</tr--%>

<!-- menu id : 18 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 18, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>	
		<div>
			<b> <bean:message key="app.shoppingCart" /> </b>
		</div>		
		
		<div>
      		<a class="subMenuPrimary" href="ShoppingCart.do">
	  			<bean:message key="app.view" />
	  		</a>
      </div>
	</td>
</tr>