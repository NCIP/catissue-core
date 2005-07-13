<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<tr>
    <td class="subMenuPrimaryTitle" height="22"></td>
  </tr>
  <tr>
    <td class="subMenuPrimaryItems">
      <div>
      	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
      		<bean:message key="app.participant" />
      		<a class="subMenuPrimary" href="Participant.do?operation=add">
      			<bean:message key="app.add" /> 
      		</a> | 
      		<a class="subMenuPrimary" href="Participant.do?operation=search">
      			<bean:message key="app.edit" /> 
      		</a>
      </div>
      <div>
      	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
      		<bean:message key="app.accession" />
      		<a class="subMenuPrimary" href="Accession.do?operation=add&pageOf=">
      			<bean:message key="app.add" /> 
      		</a> | 
      		<a class="subMenuPrimary" href="Accession.do?operation=search&pageOf=">
      			<bean:message key="app.edit" /> 
      		</a>
      </div>
	  <div>
	  	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
	  	<bean:message key="app.specimen" />
	  	<a class="subMenuPrimary" href="#">
	  		<bean:message key="app.add" /> 
	  	</a> | 
	  	<a class="subMenuPrimary" href="#">
	  		<bean:message key="app.edit" /> 
	  	</a>
	  </div>
	  <div>
	  	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
	  		<bean:message key="app.segment" />
	  		<a class="subMenuPrimary" href="#">
	  			<bean:message key="app.add" /> 
	  		</a> | 
	  		<a class="subMenuPrimary" href="#">
	  			<bean:message key="app.edit" /> 
	  		</a>
	  </div>
	  <div>
	  	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
	  	<bean:message key="app.sample" />
	  	<a class="subMenuPrimary" href="#">
	  		<bean:message key="app.add" /> 
	  	</a> | 
	  	<a class="subMenuPrimary" href="#">
	  		<bean:message key="app.edit" /> 
	  	</a>
	  </div>
    </td>
  </tr>