<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">		
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
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /--> 
				<b> <bean:message key="app.user" /> </b>
		</div>		
		<div>
			<a class="subMenuPrimary" href="User.do?operation=add&amp;pageOf=pageOfUserAdmin"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" <%--href="User.do?operation=search&amp;pageOf="--%> >
				<bean:message key="app.edit" />
			</a> | 
			<a class="subMenuPrimary" href="ApproveUserShow.do?pageNum=1"> 
					<bean:message key="app.approveUser" />
			</a>
		</div>
	</td>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /--> 
				<b> <bean:message key="app.institution" /> </b>
		</div>
		<div>
			<a class="subMenuPrimary" href="Institution.do?operation=add"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" <%--href="Institution.do?operation=search"--%> >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /--> 
				<b> <bean:message key="app.department" /></b>
		</div>
		<div>
		<a class="subMenuPrimary" href="Department.do?operation=add"><bean:message key="app.add" /></a> | 
		<a class="subMenuPrimary" <%--href="Department.do?operation=search"--%> >
			<bean:message key="app.edit" />
		</a>
		</div>
	</td>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">		

		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /--> 
				<b><bean:message key="app.cancerResearchGroup" /> </b>
		</div>
		<div>
			<a class="subMenuPrimary" href="CancerResearchGroup.do?operation=add">
				<bean:message key="app.add" />
			</a> | 
			<a class="subMenuPrimary" <%--href="CancerResearchGroup.do?operation=search"--%> ><bean:message key="app.edit" /></a>
		</div>
	</td>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /--> 
				<b><bean:message key="app.site" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="Site.do?operation=add"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" <%--href="Site.do?operation=search"--%> >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /--> 
				<b><bean:message key="app.storagetype" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="StorageType.do?operation=add"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" <%--href="StorageType.do?operation=search"--%> >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				

		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /--> 
				<b><bean:message key="app.storageContainer" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="StorageContainer.do?operation=add"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" <%--href="StorageContainer.do?operation=search"--%> >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /--> 
				<b><bean:message key="app.biohazard" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="Biohazard.do?operation=add"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" <%--href="Biohazard.do?operation=search"--%> >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->  
				<b><bean:message key="app.collectionProtocol" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="CollectionProtocol.do?operation=add"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" <%--href="CollectionProtocol.do?operation=search"--%> >
				<bean:message key="app.edit" /> 
			</a>
		</div>
	</td>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		<div>
			
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->  
				<b> <bean:message key="app.distributionProtocol" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="DistributionProtocol.do?operation=add"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" <%--href="DistributionProtocol.do?operation=search"--%> >
				<bean:message key="app.edit" /> 
			</a>
		</div>
	</td>
</tr>
<tr>
	<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">				
		<div>
			
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->  
				<b><bean:message key="app.reportedProblems" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="ReportedProblemShow.do?pageNum=1">
				<bean:message key="app.view" />
			</a> 
		</div>
	</td>
</tr>
