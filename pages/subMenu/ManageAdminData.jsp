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
			<a class="subMenuPrimary" href="Institution.do?operation=add&pageOf=pageOfInstitution"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfInstitution&aliasName=Institution" >
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
		<a class="subMenuPrimary" href="Department.do?operation=add&pageOf=pageOfDepartment"><bean:message key="app.add" /></a> | 
		<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfDepartment&aliasName=Department">
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
			<a class="subMenuPrimary" href="CancerResearchGroup.do?operation=add&pageOf=pageOfCancerResearchGroup">
				<bean:message key="app.add" />
			</a> | 
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfCancerResearchGroup&aliasName=CancerResearchGroup" ><bean:message key="app.edit" /></a>
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
			<a class="subMenuPrimary" href="Site.do?operation=add&pageOf=pageOfSite"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfSite&aliasName=Site" >
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
			<a class="subMenuPrimary" href="StorageType.do?operation=add&pageOf=pageOfStorageType"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfStorageType&aliasName=StorageType" >
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
			<a class="subMenuPrimary" href="StorageContainer.do?operation=add&pageOf=pageOfStorageContainer"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfStorageContainer&aliasName=StorageContainer" >
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
			<a class="subMenuPrimary" href="Biohazard.do?operation=add&pageOf=pageOfBioHazard"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfBioHazard&aliasName=BioHazard" >
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
			<a class="subMenuPrimary" href="CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfCollectionProtocol&aliasName=CollectionProtocol" >
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
			<a class="subMenuPrimary" href="DistributionProtocol.do?operation=add&pageOf=pageOfDistributionProtocol"><bean:message key="app.add" /></a> | 
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfDistributionProtocol&aliasName=DistributionProtocol" >
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
