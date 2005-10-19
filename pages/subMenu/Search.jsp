<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<tr>
    <td class="subMenuPrimaryTitle" height="22">
		<a href="#content">
    		<img src="images/shim.gif" alt="Skip Menu" width="1" height="1" border="0" />
    	</a>
	</td>
  </tr>
  
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		<div>
      		<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfSimpleQueryInterface">
      			<bean:message key="app.simpleSearch" />
      		</a>
      </div>
	</td>
</tr>

<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
	  <div>
      		<a class="subMenuPrimary" href="AdvanceQueryInterface.do?pageOf=pageOfAdvanceQueryInterface">
      			<bean:message key="app.advancedSearch" />
      		</a>
      </div>
	</td>
</tr>

<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
	  <div>
      		<a class="subMenuPrimary" href="ShowFramedPage.do?pageOf=pageOfStorageLocation">
	  			<bean:message key="app.queryResultView" />
	  		</a>
      </div>
	</td>
</tr>

<%--tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
	  <div>
      		<a class="subMenuPrimary" href="#">
	  			<bean:message key="app.viewStoredQueries" />
	  		</a>
      </div>
	</td>
</tr--%>

<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
	  <div>
      		<a class="subMenuPrimary" href="ShoppingCart.do">
	  			<bean:message key="app.viewShoppingCart" />
	  		</a>
      </div>
	</td>
</tr>
