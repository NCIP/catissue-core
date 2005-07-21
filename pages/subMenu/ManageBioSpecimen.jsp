<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<tr>
    <td class="subMenuPrimaryTitle" height="22">
		<a href="#content">
    		<img src="images/shim.gif" alt="Skip Menu" width="1" height="1" border="0" />
    	</a>
	</td>
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
      		<a class="subMenuPrimary" href="NewSpecimen.do?operation=add&amp;pageOf=">
      			<bean:message key="app.newSpecimen" />
      		</a>
      </div>
      <div>
      	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" />
      		<a class="subMenuPrimary" href="#">
      			<bean:message key="app.createSpecimen" />
      		</a>
      </div>
      <div>
      	<img src="images/subMenuArrow.gif" width="7" height="7" alt="" />
      		<a class="subMenuPrimary" href="SpecimenCollectionGroup.do?operation=add">
      			<bean:message key="app.specimenCollectionGroup" />
      		</a>
      </div>
    </td>
  </tr>