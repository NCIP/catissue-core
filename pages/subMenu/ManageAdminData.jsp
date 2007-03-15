<td class="subMenuPrimaryItemsWithBorder" onmouseover="changeMenuStyle(this,'subMenuPrimaryItemsWithBorderOver')" onmouseout="changeMenuStyle(this,'subMenuPrimaryItemsWithBorder')">
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

<!-- menu id : 1 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 1, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b> <bean:message key="app.user" /> </b>
		</div>
		<div>
			<a class="subMenuPrimary" href="User.do?operation=add&amp;pageOf=pageOfUserAdmin&menuSelected=1"><bean:message key="app.add" /></a> |
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfUserAdmin&aliasName=User&menuSelected=1" >
				<bean:message key="app.edit" />
			</a> |
			<a class="subMenuPrimary" href="ApproveUserShow.do?pageNum=1&menuSelected=1">
					<bean:message key="app.approveUser" />
			</a>
		</div>
	</td>
</tr>
<!-- menu id : 2 -->
<%
	strMouseOut ="";
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 2, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b> <bean:message key="app.institution" /> </b>
		</div>
		<div>
			<a class="subMenuPrimary" href="Institution.do?operation=add&pageOf=pageOfInstitution&menuSelected=2"><bean:message key="app.add" /></a> |
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfInstitution&aliasName=Institution&menuSelected=2" >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
<!-- menu id : 3 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 3, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b> <bean:message key="app.department" /></b>
		</div>
		<div>
		<a class="subMenuPrimary" href="Department.do?operation=add&pageOf=pageOfDepartment&menuSelected=3"><bean:message key="app.add" /></a> |
		<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfDepartment&aliasName=Department&menuSelected=3">
			<bean:message key="app.edit" />
		</a>
		</div>
	</td>
</tr>
<!-- menu id : 4 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 4, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b><bean:message key="app.cancerResearchGroup" /> </b>
		</div>
		<div>
			<a class="subMenuPrimary" href="CancerResearchGroup.do?operation=add&pageOf=pageOfCancerResearchGroup&menuSelected=4">
				<bean:message key="app.add" />
			</a> |
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfCancerResearchGroup&aliasName=CancerResearchGroup&menuSelected=4" ><bean:message key="app.edit" /></a>
		</div>
	</td>
</tr>
<!-- menu id : 5 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 5, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b><bean:message key="app.site" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="Site.do?operation=add&pageOf=pageOfSite&menuSelected=5"><bean:message key="app.add" /></a> |
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfSite&aliasName=Site&menuSelected=5" >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
<!-- menu id : 6 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 6, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b><bean:message key="app.storagetype" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="StorageType.do?operation=add&pageOf=pageOfStorageType&menuSelected=6"><bean:message key="app.add" /></a> |
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfStorageType&aliasName=StorageType&menuSelected=6" >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
<!-- menu id : 7 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 7, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b><bean:message key="app.storageContainer" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="StorageContainer.do?operation=add&pageOf=pageOfStorageContainer&menuSelected=7"><bean:message key="app.add" /></a> |
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfStorageContainer&aliasName=StorageContainer&menuSelected=7" >
				<bean:message key="app.edit" />
			</a> |
			<a class="subMenuPrimary" href="#" onclick="javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimen&storageType=-1','name','810','320','yes');return false" >
				<bean:message key="app.viewmap" />
			</a>
		</div>
	</td>
</tr>

<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 21, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
		<%=strMouseOut%>
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b><bean:message key="app.newSpecimenArrayType" /> </b>
		</div>
		<div>
			<a class="subMenuPrimary" href="SpecimenArrayType.do?operation=add&amp;pageOf=pageOfSpecimenArrayType&amp;menuSelected=21">
				<bean:message key="app.add" />
			</a> |
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfSpecimenArrayType&amp;aliasName=SpecimenArrayType&amp;menuSelected=21">
				<bean:message key="app.edit"/>
			</a>
		</div>
	</td>
</tr>

<!-- menu id : 8 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 8, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b><bean:message key="app.biohazard" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="Biohazard.do?operation=add&pageOf=pageOfBioHazard&menuSelected=8"><bean:message key="app.add" /></a> |
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfBioHazard&aliasName=Biohazard&menuSelected=8" >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
<!-- menu id : 9 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 9, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>
			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b><bean:message key="app.collectionProtocol" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol&menuSelected=9"><bean:message key="app.add" /></a> |
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfCollectionProtocol&aliasName=CollectionProtocol&menuSelected=9" >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
<!-- menu id : 10 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 10, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>

			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b> <bean:message key="app.distributionProtocol" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="DistributionProtocol.do?operation=add&pageOf=pageOfDistributionProtocol&menuSelected=10"><bean:message key="app.add" /></a> |
			<a class="subMenuPrimary" href="SimpleQueryInterface.do?pageOf=pageOfDistributionProtocol&aliasName=DistributionProtocol&menuSelected=10" >
				<bean:message key="app.edit" />
			</a>
		</div>
	</td>
</tr>
<!-- menu id : 11 -->
<%
	strMouseOut = Utility.setSelectedMenuItem(selectMenuID, 11, normalMenuClass , selectedMenuClass , hoverMenuClass);
%>
<tr>
	<%=strMouseOut%>
		<div>

			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b><bean:message key="app.reportedProblems" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="ReportedProblemShow.do?pageNum=1&menuSelected=11">
				<bean:message key="app.view" />
			</a>
		</div>
	</td>
</tr>
<tr>
	<%=strMouseOut%>
		<div>

			<!--img src="images/subMenuArrow.gif" width="7" height="7" alt="" /-->
				<b><bean:message key="app.localExtn" /></b>
		</div>
		<div>
			<a class="subMenuPrimary" href="DefineAnnotations.do">
					<bean:message key="anno.define" />
			</a>
		</div>
	</td>
</tr>