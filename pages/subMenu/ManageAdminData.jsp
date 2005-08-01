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
				<bean:message key="app.user" />
		</div>		
		<div>&nbsp;&nbsp;
			<a class="subMenuPrimary" href="User.do?operation=add&amp;pageOf=">
				<bean:message key="app.add" />
			</a> &nbsp;
			<a class="subMenuPrimary" href="User.do?operation=search&amp;pageOf=">
				<bean:message key="app.edit" />
			</a>
		</div>
		
		<div>
			<img src="images/subMenuArrow.gif" width="7" height="7" alt="" />
				<a class="subMenuPrimary" href="ApproveUserShow.do?pageNum=1"> 
					<bean:message key="app.approveUser" />
				</a>
		</div>
		
		
		<div>
			<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
				<bean:message key="app.institution" />					
		</div>
		<div>&nbsp;&nbsp;
			<a class="subMenuPrimary" href="Institution.do?operation=add">
				<bean:message key="app.add" />
			</a> &nbsp;
			<a class="subMenuPrimary" href="Institution.do?operation=search">
				<bean:message key="app.edit" />
			</a>
		</div>
		
		
		<div>
			<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
				<bean:message key="app.department" />					
		</div>
		<div>&nbsp;&nbsp;
		<a class="subMenuPrimary" href="Department.do?operation=add">
			<bean:message key="app.add" />
		</a> &nbsp;
		<a class="subMenuPrimary" href="Department.do?operation=search">
			<bean:message key="app.edit" />
		</a>
		</div>


		<div>
			<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
				<bean:message key="app.cancerResearchGroup" />
		</div>
		<div>&nbsp;&nbsp;
			<a class="subMenuPrimary" href="CancerResearchGroup.do?operation=add">
				<bean:message key="app.add" />
			</a> &nbsp;
			<a class="subMenuPrimary" href="CancerResearchGroup.do?operation=search">
				<bean:message key="app.edit" />
			</a>
		</div>
		
		<div>
			<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
				<bean:message key="app.site" />
		</div>
		<div>&nbsp;&nbsp;
			<a class="subMenuPrimary" href="Site.do?operation=add">
				<bean:message key="app.add" />
			</a> &nbsp;
			<a class="subMenuPrimary" href="Site.do?operation=search">
				<bean:message key="app.edit" />
			</a>
		</div>
		
		
		<div>
			<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
				<bean:message key="app.storagetype" />
		</div>
		<div>&nbsp;&nbsp;
			<a class="subMenuPrimary" href="StorageType.do?operation=add">
						<bean:message key="app.add" />
			</a> &nbsp;
			<a class="subMenuPrimary" href="StorageType.do?operation=search">
				<bean:message key="app.edit" />
			</a>
		</div>
		

		<div>
			<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
				<bean:message key="app.storageContainer" />
		</div>
		<div>&nbsp;&nbsp;
			<a class="subMenuPrimary" href="StorageContainer.do?operation=add">
				<bean:message key="app.add" />
			</a> &nbsp;
			<a class="subMenuPrimary" href="StorageContainer.do?operation=search">
				<bean:message key="app.edit" />
			</a>
		</div>
		
		
		<div>
			<img src="images/subMenuArrow.gif" width="7" height="7" alt="" /> 
				<bean:message key="app.biohazard" />
		</div>
		<div>&nbsp;&nbsp;
			<a class="subMenuPrimary" href="Biohazard.do?operation=add">
				<bean:message key="app.add" />
			</a> &nbsp;
			<a class="subMenuPrimary" href="Biohazard.do?operation=search">
				<bean:message key="app.edit" />
			</a>
		</div>
		
		<div>
			<img src="images/subMenuArrow.gif" width="7" height="7" alt=""/> 
				<bean:message key="app.collectionProtocol" />
		</div>
		<div>&nbsp;&nbsp;
					<a class="subMenuPrimary" href="CollectionProtocol.do?operation=add">
						<bean:message key="app.add" /> 
					</a> &nbsp;
					<a class="subMenuPrimary" href="CollectionProtocol.do?operation=search">
						<bean:message key="app.edit" /> 
					</a>
		</div>
		
		<div>
			
			<img src="images/subMenuArrow.gif" width="7" height="7" alt=""/> 
				<bean:message key="app.distributionProtocol" />
		</div>
		<div>&nbsp;&nbsp;
					<a class="subMenuPrimary" href="DistributionProtocol.do?operation=add">
						<bean:message key="app.add" /> 
					</a> &nbsp;
					<a class="subMenuPrimary" href="DistributionProtocol.do?operation=search">
						<bean:message key="app.edit" /> 
					</a>
		</div>
		
		<div>
			<img src="images/subMenuArrow.gif" width="7" height="7" alt="" />
				<a class="subMenuPrimary" href="ReportedProblemShow.do?pageNum=1">
					<bean:message key="app.reportedProblems" />
				</a>
		</div>

	</td>
</tr>