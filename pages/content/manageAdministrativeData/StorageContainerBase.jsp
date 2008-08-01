<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
var selectedTabName="infoTabSelected";
var selectedNodeId=null;
var activityStatus=null;
var isConatinerChanged='no';
function selectTab(operation)
{
	var treeFrame = document.getElementById('SCTreeView');	
	var addEditTabRow = document.getElementById('addEditTabRow');
	var cellSpace="<td  width=4% class=td_tab_bg><img src=images/spacer.gif alt=spacer width=50 height=1></td>";
   
    if(operation == "add" && selectedTabName != "viewMapTab" && treeFrame == null)
    {
		var cellAddImage="<td  valign=bottom><link><img src=images/uIEnhancementImages/tab_add_selected.jpg alt=Add width=57 height=22 /></link></td>";

		var cellEditImage="<td  valign=bottom><a href=# onclick=switchToEditTab()><link><img src=images/uIEnhancementImages/tab_edit_notSelected.jpg alt=Edit width=59 height=22 border=0 /></link></a></td>";
	
		var cellViewImage="<td valign=bottom><a href=# onclick=switchToViewMapTab()><img src=images/uIEnhancementImages/view_map2.gif alt=View Map width=76 height=22 border=0 /></a></td>";
	}
	else if(operation == "edit" && selectedTabName != "viewMapTab" && treeFrame == null)
    {
		var cellAddImage="<td  valign=bottom><a href=# onclick=switchToAddTab()><link><img src=images/uIEnhancementImages/tab_add_notSelected.jpg alt=Add width=57 height=22 /></link></a></td>";

		var cellEditImage="<td valign=bottom><link><img src=images/uIEnhancementImages/tab_edit_selected.jpg alt=Edit width=59 height=22 border=0/></link></td>";
	
		var cellViewImage="<td valign=bottom><a href=# onclick=switchToViewMapTab()><img src=images/uIEnhancementImages/view_map2.gif alt=View Map width=76 height=22 border=0 /></a></td>";	
	}
	else 
	{
		var cellAddImage="<td  valign=bottom><a href=#  onclick=switchToAddTab()><link>	<img src=images/uIEnhancementImages/tab_add_notSelected.jpg alt=Add width=57 height=22 /></link></a></td>";

		var cellEditImage="<td valign=bottom><a href=# onclick=switchToEditTab()><link><img src=images/uIEnhancementImages/tab_edit_notSelected.jpg alt=Edit width=59 height=22 border=0/></link></a></td>";
	
		var cellViewImage="<td valign=bottom><img src=images/uIEnhancementImages/view_map.gif alt=View Map width=76 height=22 border=0 /></td>";	

	}
    var addCell= "<td width=90% valign=bottom class=td_tab_bg>&nbsp;</td>";

	addEditTabRow.innerHTML=cellSpace+cellAddImage+cellEditImage+cellViewImage+addCell;
}
function switchToAddTab()
{
	window.location.href ="OpenStorageContainer.do?operation=add&pageOf=pageOfStorageContainer";
}
function switchToEditTab()
{
	window.location.href="SimpleQueryInterface.do?pageOf=pageOfStorageContainer&aliasName=StorageContainer";
}
function switchToViewMapTab()
{
	window.location.href="OpenStorageContainer.do?operation=showEditAPageAndMap&pageOf=pageOfStorageContainer";
}
function containerChangedTrue()
{
	isConatinerChanged ='yes';
}
function isChanged()
{
  return isConatinerChanged;
}
function getActivityStatus(activityStatus){
    activityStatus=activityStatus;
}
function setActivityStatus(){
  return selectedTabName;
}
function tabSelected(tab){
    selectedTabName=tab;
}
function getSelectedTab(){
  return selectedTabName;
}

</script>
<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="maintable">
    <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="storageContainer.header" /></span></td>
        <td align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title" width="31" height="24" /></td>
      </tr>
    </table>
	</td>
  </tr>
  <tr>
      <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr id="addEditTabRow">
        <td  width="4%"class="td_tab_bg" ><img src="images/spacer.gif" alt="spacer" width="50" height="1"></td>
		<logic:equal parameter="operation"	value='add'>
        <td valign="bottom" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /><a href="#"></a></td>
        <td valign="bottom"><a href="#" onclick="switchToEditTab()"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0"/></a></td>
		<td valign="bottom"><a href="#" onclick="switchToViewMapTab()"><img src="images/uIEnhancementImages/view_map2.gif" alt="View Map" width="76" height="22" border="0" /></a></td>
        </logic:equal>
		<logic:equal parameter="operation"	value='edit'>
		<td valign="bottom" ><a href="#"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" onclick="switchToAddTab()"/></a></td>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
		<td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/view_map2.gif" alt="View Map" width="76" height="22" border="0" onclick="switchToViewMapTab()"/></a></td>
		</logic:equal>
		<logic:equal parameter="operation"	value='showEditAPageAndMap'>
		<td valign="bottom" ><a href="#"  onclick="switchToAddTab()"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22"/></a></td>
        <td valign="bottom"><a href="#" onclick="switchToEditTab()"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></a></td>
		<td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/view_map.gif" alt="View Map" width="76" height="22" border="0" /></a></td>
		</logic:equal>
		<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr></table>
	 <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td colspan="2" align="left" class=" grey_ar_s">&nbsp;<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />&nbsp;<bean:message key="commonRequiredField.message" /></td>
        </tr>
        <tr>
							<logic:equal parameter="operation"	value='showEditAPageAndMap'>
							<td width="25%"  valign="top">
								<iframe id="SCTreeView" src="ShowFramedPage.do?pageOf=pageOfStorageContainer&storageType=-1&operation=${requestScope.operation}" scrolling="auto" frameborder="0" width="100%" name="SCTreeView" height="450" >
									Your Browser doesn't support IFrames.
								</iframe>
							 </td>
							 </logic:equal>
							 <td width="75%" valign="top">
								<logic:equal parameter="operation"	value='add'>
							 	<iframe name="StorageContainerView"	id="StorageContainerView" src="StorageContainer.do?operation=add&pageOf=pageOfStorageContainer" scrolling="auto" frameborder="0" width="100%" height="450" >
									Your Browser doesn't support IFrames.
								</iframe>
								</logic:equal>
								<logic:equal parameter="operation"	value='edit'>
								<iframe name="StorageContainerView"	id="StorageContainerView" src="StorageContainer.do?operation=edit&pageOf=pageOfStorageContainer" scrolling="auto" frameborder="0" width="100%" height="450" >
									Your Browser doesn't support IFrames.
								</iframe>
								</logic:equal>
								<logic:equal parameter="operation"	value='showEditAPageAndMap'>
								 <iframe name="StorageContainerView"	id="StorageContainerView" src="storageContainerEditMessageScreen.do" scrolling="auto" frameborder="0" width="100%" height="450" >
									Your Browser doesn't support IFrames.
								</iframe>
								</logic:equal>
							 </td>
						</tr>	
          </table>
	  </td>
   </tr>
 </table>

