<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<tr>
    <td class="subMenuPrimaryTitle" height="22"></td>
  </tr>
  <tr>
    <td class="subMenuPrimaryItems">
      <div>
      	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" />
      		<a class="subMenuPrimary" href="#">
      			<bean:message key="app.simpleSearch" />
      		</a>
      </div>
      <div>
      	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" />
      		<a class="subMenuPrimary" href="#">
      			<bean:message key="app.advancedSearch" />
      		</a>
      </div>
      <div>
	  	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" />
	  		<a class="subMenuPrimary" href="QueryResults.do">
	  			<bean:message key="app.queryResultView" />
	  		</a>
	  </div>
	  <div>
	  	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" />
	  		<a class="subMenuPrimary" href="#">
	  			<bean:message key="app.viewStoredQueries" />
	  		</a>
	  </div>
	  <div>
	  	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" />
	  		<a class="subMenuPrimary" href="#">
	  			<bean:message key="app.viewShoppingCart" />
	  		</a>
	  </div>
    </td>
  </tr>