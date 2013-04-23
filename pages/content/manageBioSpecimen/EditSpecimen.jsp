<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page language="java" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="edu.wustl.catissuecore.dto.SpecimenDTO"%>

<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxcombo.css">
<link href="css/catissue_suite.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="css/alretmessages.css"/>
<link rel="stylesheet" type="text/css" href="css/tag-popup.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/dhtml_pop/css/dhtmlXTree.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/dhtml_pop/css/dhtmlXGrid.css" />
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/dhtml_pop/css/dhtmlxgrid_dhx_skyblue.css" />
<link rel="stylesheet" type="text/css" href="css/tag-popup.css" />
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxwindows.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/skins/dhtmlxwindows_dhx_skyblue.css">
<link rel="stylesheet" type="text/css" href="dhtmlx_suite/css/dhtmlxtree.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxgrid.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/ext/dhtmlxgrid_pgn_bricks.css">
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/skins/dhtmlxtoolbar_dhx_blue.css">

<script language="JavaScript" type="text/javascript" src="jss/newSpecimen.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript"	src="jss/javaScript.js"></script>
<script language="JavaScript" type="text/javascript"	src="jss/caTissueSuite.js"></script>
<script src="jss/ajax.js" type="text/javascript"></script>
<script src="jss/json2.js" type="text/javascript"></script>
<script  src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script  src="dhtmlx_suite/js/dhtmlxcombo.js"></script>
<script	src="dhtmlx_suite/ext/dhtmlxcombo_whp.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/specimen.js"></script>
<script type="text/javascript" src="jss/tag-popup.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXCommon.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlx.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTree.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmXTreeCommon.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXGridCell.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
<script src="dhtmlx_suite/js/dhtmlxcontainer.js"></script>
<script src="dhtmlx_suite/js/dhtmlxwindows.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/dhtmlDropDown.js"></script>
<script src="dhtmlx_suite/js/dhtmlxtree.js"></script>
<script src="dhtmlx_suite/ext/dhtmlxtree_li.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgrid.js"></script>
<script src="dhtmlx_suite/dhtml_pop/js/dhtmlXTreeGrid.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxgridcell.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/connector.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_filter.js"></script>
<script type="text/javascript" src="dhtmlx_suite/ext/dhtmlxgrid_pgn.js"></script>
<script type="text/javascript" src="dhtmlx_suite/js/dhtmlxtoolbar.js"></script>

<script>
	var imgsrc="images/";
	window.dhx_globalImgPath = "dhtmlx_suite/imgs/";
</script>
<!----------------------------------------------------------------------->

<html:form action="NewSpecimenEdit.do">

<html:hidden name="specimenDTO" property="generateLabel"/>
<html:hidden name="specimenDTO" property="operation"/>
<html:hidden name="specimenDTO" property="parentSpecimenId"/>
<html:hidden name="specimenDTO" property="id" styleId="id"/>
<html:hidden name="specimenDTO" property="specimenCollectionGroupId" styleId="scgId"/>
<html:hidden name="specimenDTO" property="label" styleId="label"/>
<html:hidden name="specimenDTO" property="barcode" styleId="barcode"/>
								
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">

	<tr>
		<td class="tablepadding">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td class="td_tab_bg" >
					<img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1">
				</td>
				<td valign="bottom"><a onclick="newspecimenPage()" id="specimenDetailsTab" href="#"><img src="images/uIEnhancementImages/tab_specimen_details1.gif" alt="Specimen Details"  width="126" height="22" border="0"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_events2.gif" alt="Events" width="56" height="22" onclick="showEvent('${specimenDTO.id}');" border="0"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_surgical2.gif" alt="View Surgical Pathology Report" width="216" height="22" border="0" onclick="viewSPR('${identifiedReportId}','pageOfNewSpecimenCPQuery','${specimenDTO.id}');"></a></td><td valign="bottom"><a href="#"><img src="images/uIEnhancementImages/tab_view_annotation2.gif" alt="View Annotation" width="116" height="22" border="0" onClick="viewSpecimenAnnotation('${specimenRecordEntryEntityId}','${specimenDTO.id}','${entityName}')"></a></td><td align="left" valign="bottom" class="td_color_bfdcf3" ><a id="consentViewTab" href="#" onClick="newConsentTab('${specimenDTO.id}','${identifiedReportId}','${specimenRecordEntryEntityId}','${entityName}')"><img src="images/uIEnhancementImages/tab_consents2.gif" alt="Consents" width="76" border="0" height="22" ></a></td><td align="left" valign="bottom" class="td_color_bfdcf3" ><a id="imageViewTab" href="#" onClick="newImageTab('${specimenDTO.id}')"><img src="images/uIEnhancementImages/tab_image2.gif" alt="Images" width="110" border="0" height="22" ></a></td>
				
				<td width="90%" align="left" valign="bottom" class="td_tab_bg" >&nbsp;
				</td>
				</tr>
		    </table>
	
		    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">
			<tr>
			<td>
				<div id="mainTable"style="display:block">
				<table width="100%"  border="0" cellpadding="3" cellspacing="0" >
				<tr>
					<td>
						<div id="error" class="alert alert-error" style="display:none">
							<strong>Error!</strong> <span id="errorMsg">Change a few things up and try submitting again.</span>
						</div>
						<div id="print-error" class="alert alert-error" style="display:none">
							Change a few things up and try submitting again.
						</div>
						<div class="alert alert-success" id="success" style="display:none">
						   Specimen Updated Sucessfully.
						</div>
						<div class="alert alert-success" id="print-success" style="display:none">
						   Specimen Label Printed successfully.
						</div>
						
					</td>
				</tr>
				<tr>
		          <td align="left" class="showhide">
					<table width="100%" border="0" cellpadding="3" cellspacing="0" >
						<!-- NEW SPECIMEN REGISTRATION BEGINS-->
						<tr class="tr_alternate_color_lightGrey">
				<logic:empty name="specimenDTO" property="parentSpecimenName">
				<logic:notEmpty name="specimenDTO" property="specimenCollectionGroupName">
		                 <td width="20%" class="black_ar align_right_style">
							<label for="specimenCollectionGroupName">
								<bean:message key="newSpecimen.groupName"/>
							</label>
						 </td>
						 <td width="30%" align="left" class="black_ar">
							<html:hidden name="specimenDTO" property="specimenCollectionGroupName" styleId="specimenCollectionGroupName"/>
								<label for="specimenCollectionGroupName">
									<bean:write name="specimenDTO" property="specimenCollectionGroupName" scope="request"/>
								</label>
						 </td>
				</logic:notEmpty>
				</logic:empty>
				
				<logic:notEmpty name="specimenDTO" property="parentSpecimenName">
						  <td width="20%" class="black_ar align_right_style">
								<label for="parentSpecimenId">
									<bean:message key="newSpecimen.parentLabel"/>
								</label>
    					  </td>
 			        	  <td width="30%" align="left" class="black_ar">
						   		
								<html:hidden name="specimenDTO" property="parentSpecimenName"/>
								<label for="parentSpecimenId">
									<bean:write name="specimenDTO" property="parentSpecimenName" scope="request"/>
								</label>

 			        	  </td>
				</logic:notEmpty>

						  <td width="20%"  class="black_ar align_right_style">
								<label for="lineage">
									<bean:message key="specimen.lineage"/>
								</label>
						  </td>
						  <td width="30%" align="left" class="black_ar">
							<label for="lineage">
								<bean:write name="specimenDTO" property="lineage" scope="request"/>
								<html:hidden name="specimenDTO" property="lineage"/>
							</label>
						  </td>
					</tr>
						<c:choose>
							<c:when test="${isSpecimenLabelGeneratorAvl=='false' && isSpecimenBarcodeGeneratorAvl=='false'}">
								<tr class="tr_alternate_color_white">
									<td width="20%" class="black_ar align_right_style">
										<label for="label">
											<bean:message key="specimen.label"/>
										</label>
									</td>
									<td align="left" width="30%">
										<html:text styleClass="black_ar" size="30" maxlength="255"  styleId="label" name="specimenDTO" property="label" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)"/>
									</td>

									<td width="20%" class="black_ar align_right_style">
										<label for="barcode">
											<bean:message key="specimen.barcode"/>
										</label>
									</td>
								
									<td width="30%" align="left" class="black_ar">
										<label for="barcode">
											<html:text name="specimenDTO" 
													   styleClass="black_ar" maxlength="255" size="30"
													   styleId="barcode" property="barcode" onblur="processData(this)"/>
										</label>
									</td>
								</tr>
							</c:when>
							<c:otherwise>
							<c:if test="${isSpecimenLabelGeneratorAvl=='true' && isSpecimenBarcodeGeneratorAvl=='true' && specimenDTO.collectionStatus=='Collected'}">
								<tr class="tr_alternate_color_white">
									<td width="20%" class="black_ar align_right_style">
										<label for="label">
											<bean:message key="specimen.label"/>
										</label>
									</td>
									<td align="left" width="30%">
										<html:text styleClass="black_ar" size="30" maxlength="255"  styleId="label" name="specimenDTO" property="label" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)"/>
									</td>

									<td width="20%" class="black_ar align_right_style">
										<label for="barcode">
											<bean:message key="specimen.barcode"/>
										</label>
									</td>
								
									<td width="30%" align="left" class="black_ar">
										<label for="barcode">
											<html:text name="specimenDTO" 
													   styleClass="black_ar" maxlength="255" size="30"
													   styleId="barcode" property="barcode" onblur="processData(this)"/>
										</label>
									</td>
								</tr>
							</c:if>
								<c:if test="${isSpecimenLabelGeneratorAvl=='false' && isSpecimenBarcodeGeneratorAvl=='true' && specimenDTO.collectionStatus!='Collected'}">
									<tr class="tr_alternate_color_white">
										<td width="20%" class="black_ar align_right_style">
											<label for="label">
												<bean:message key="specimen.label"/>
											</label>
										</td>
										<td align="left" width="30%">
											<html:text styleClass="black_ar" size="30" maxlength="255"  styleId="label" name="specimenDTO" property="label" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onblur="processData(this)"/>
										</td>
									</tr>
								</c:if>
								<c:if test="${isSpecimenLabelGeneratorAvl=='true' && isSpecimenBarcodeGeneratorAvl=='false' && specimenDTO.collectionStatus!='Collected'}">
									<tr class="tr_alternate_color_white">
										<td></td><td></td>
										<td width="20%" class="black_ar align_right_style">
											<label for="barcode">
												<bean:message key="specimen.barcode"/>
											</label>
										</td>
										<td width="30%" align="left" class="black_ar">
											<label for="barcode">
												<html:text name="specimenDTO" 
														   styleClass="black_ar" maxlength="255" size="30"
														   styleId="barcode" property="barcode" onblur="processData(this)"/>
											</label>
										</td>
									</tr>
								</c:if>
								
							</c:otherwise>
						</c:choose>
						<tr class="tr_alternate_color_lightGrey">
							<td  width="20%" class="black_ar align_right_style">
								<label for="className">
								    <bean:message key="specimen.type"/>
						     	</label>
							</td>
							<td width="30%" align="left" class="black_new">
							<html:select property="className" name="specimenDTO" 
										 styleClass="formFieldSized" styleId="className" size="1"
										 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="onSpecimenTypeChange(this)">
								<html:options collection="specimenClassList"
									labelProperty="name" property="value" />
						   </html:select>
							</td>

							<td width="20%" class="black_ar align_right_style">
								<label for="type">
								     <bean:message key="specimen.subType"/>
						     	</label>
							</td>
							<td width="30%" align="left" class="black_new">
							<html:select property="type" name="specimenDTO" 
							styleClass="formFieldSized" styleId="type" size="1"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" onchange="onSpecimenSubTypeChange(this)">
							<html:options collection="specimenTypeList"
								labelProperty="name" property="value" />
							</html:select>
						
							  </td>
							</tr>
							
							<tr class="tr_alternate_color_white">
								<td width="20%" class="black_ar align_right_style">
									<label for="tissueSite">
										<bean:message key="specimen.tissueSite"/>
									</label>
								</td>

								<td>
								<table style="border-collapse: collapse;">
									<tr>
									<td>	
									<html:select property="tissueSite" name="specimenDTO" 
									styleClass="formFieldSized" styleId="tissueSite" size="1"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<html:options collection="tissueSiteList"
										labelProperty="name" property="value" />
									</html:select> 
									</td>
									</tr>
									</table>
								</td>

								<td width="20%" class="black_ar align_right_style">
									<label for="tissueSide">
										<bean:message key="specimen.tissueSide"/>
									</label>
								</td>
								<td width="30%" align="left" class="black_new">
								<html:select property="tissueSide" name="specimenDTO" 
							styleClass="formFieldSized" styleId="tissueSide" size="1"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" >
							<html:options collection="tissueSideList"
								labelProperty="name" property="value" />
						</html:select>
								</td>
							</tr>
							
							<tr class="tr_alternate_color_lightGrey">
								<td width="20%" class="black_ar align_right_style">
									<label for="pathologicalStatus">
										<bean:message key="specimen.pathologicalStatus"/>
									</label>
								</td>
							
								<td width="30%" align="left" class="black_new">
								<html:select property="pathologicalStatus" name="specimenDTO" 
							styleClass="formFieldSized" styleId="pathologicalStatus" size="1"
							onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" >
							<html:options collection="pathologicalStatusList"
								labelProperty="name" property="value" />
						</html:select>
								</td>
							
								<td width="20%" class="black_ar align_right_style">
									<label for="createdDate">
										<bean:message key="specimen.createdDate"/>
									</label>
								</td>
								<td width="30%" class="black_ar" >
								<html:text property="createdDate" styleClass="black_ar"
							       styleId="createdDate" size="10" name="specimenDTO" onblur="processData(this)"/>
							   	<span class="grey_ar_s capitalized">[<bean:message key="date.pattern" />]</span>&nbsp;
								</td>
							</tr>
							
							<tr class="tr_alternate_color_white">
								<td width="20%" class="black_ar align_right_style">
									<label for="quantity">
										<bean:message key="specimen.quantity"/>
									</label>
								</td>
								<td width="30%" align="left" class="black_ar">
									<html:text styleClass="black_ar" size="10" maxlength="10"  styleId="quantity" property="quantity" name="specimenDTO"  style="text-align:right" onblur="processData(this)"/>
								     <span id="unitSpan">
				
									 </span>
								     <html:hidden property="unit"/>
								</td>
				                <td width="20%" class="black_ar align_right_style">
									<label for="concentration">
										<bean:message key="specimen.concentration"/>
									</label>
								</td>
								<td  width="30%" align="left" class="black_ar">
										<html:text styleClass="black_ar" maxlength="10"  size="10" styleId="concentration" property="concentration" style="text-align:right" name="specimenDTO" onblur="processData(this)"
							     		 disabled="false"/>
										<bean:message key="specimen.concentrationUnit"/>
								</td>
							</tr>
			
							<tr class="tr_alternate_color_lightGrey">
								 <td width="20% class="black_ar">&nbsp;
								 </td>
								 <td width="30%" align="left" valign="top" >
									<html:checkbox property="available" onblur="processData(this)">
									</html:checkbox>
									
									<span class="black_ar" style="padding-bottom:7px">
										<label for="available">
											<bean:message key="specimen.available" />
										</label>
									</span>
								</td>
								<td width="20%" class="black_ar align_right_style">
									<label for="availableQuantity">
											<bean:message key="specimen.availableQuantity" />
									</label>
								</td>
								<td width="30%" align="left" class="black_ar">
									<html:text styleClass="black_ar" maxlength="10"  size="10" styleId="availableQuantity" property="availableQuantity"		name="specimenDTO" style="text-align:right" onblur="processData(this)"/>
									<span id="unitSpan1">
			
									</span>
								</td>
							</tr>
						
							<tr class="tr_alternate_color_white">
								<td width="20%" class="black_ar align_right_style">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />	
									<label for="collectionStatus">
									<bean:message key="specimenCollectionGroup.collectionStatus" />

									</label>
								</td>
								<td width="30%" class="black_new">
								<html:select property="collectionStatus" name="specimenDTO" 
											 styleClass="formFieldSized" styleId="collectionStatus" size="1"
											 onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
								<html:options collection="collectionStatusList"
											  labelProperty="name" property="value" />
								</html:select>
								</td>

							    <td width="20%" class="black_ar align_right_style">
									<label for="activityStatus">
										<bean:message key="participant.activityStatus" />
									</label>
								</td>
								<td width="30%" align="left" class="black_ar">
									<label for="activityStatus">
										<bean:write name="specimenDTO" property="activityStatus" scope="request"/>
									</label>
									<html:hidden name="specimenDTO" property="activityStatus"/>
								</td>
							</tr>
						
							<tr class="tr_alternate_color_lightGrey">
								<td width="20%" class="black_ar align_right_style">
									<label for="className">
									   	<bean:message key="specimen.positionInStorageContainer"/>
								    </label>
								</td>
																
								<td colspan="3" class="black_ar">
					<!-------Select Box Begins----->
					
								<logic:equal name="specimenDTO" property="isVirtual" value="true">
									
									<input type="text" size="30" maxlength="255"  class="black_ar tr_alternate_color_lightGrey"  value='Virtually Located' readonly style="border:0px;" id="storageContainerPosition" />
								</logic:equal>
								<logic:equal name="specimenDTO" property="isVirtual" value="false">
								<input type="text" size="30" maxlength="255"  class="black_ar"  value='<bean:write name="specimenDTO" property="containerName" scope="request"/>:(<bean:write name="specimenDTO" property="pos1" scope="request"/>,<bean:write name="specimenDTO" property="pos2" scope="request"/>)' readonly style="border:0px" id="storageContainerPosition" />
									
								</logic:equal>
								<logic:equal name="specimenDTO" property="collectionStatus" value="Collected">
									<input type="button" class="blue_ar_b" value="Edit" onclick="loadDHTMLXWindowForTransferEvent()" />
								</logic:equal>
								<logic:equal name="specimenDTO" property="collectionStatus" value="Pending">
									<input type="button" class="blue_ar_b" value="Select Container" onclick="loadDHTMLXWindowForTransferEvent()" />
								</logic:equal>
								
								<html:hidden name="specimenDTO" property="isVirtual" styleId="isVirtual"/>
								<html:hidden name="specimenDTO" property="containerName" styleId="containerName"/>
								<html:hidden name="specimenDTO" property="pos1" styleId="pos1"/>
								<html:hidden name="specimenDTO" property="pos2" styleId="pos2"/>
								<html:hidden name="specimenDTO" property="containerId" />
								</td>
							</tr>

							<tr class="tr_alternate_color_white">
								<td width="20%" valign="top" class="black_ar align_right_style">
									<label for="comments">
										<bean:message key="specimen.comments"/>
									</label>
								</td>
								<td align="left" valign="top" colspan="3">
									<html:textarea styleClass="black_ar_s"  rows="3" cols="90" name="specimenDTO" styleId="comments" property="comments" onblur="processData(this)"/>
								</td>
							</tr>
								
							<tr class="tr_alternate_color_lightGrey">
								<td width="20%"  class="black_ar align_right_style">
									<bean:message key="specimen.externalIdentifier"/>
								</td>
								<td width="30%">
									<a id="addExternalId" title="Add New External Identifier" class="link" onclick="showAddExternalIdDiv()">Add New</a>
								</td>
								<td  width="50%" class="black_ar" colspan="2">
									<div id="addExternalIdDiv" style="display:none;">
										<table width="100%" border="0" cellpadding="0" cellspacing="0">
											<tr valign="bottom">
												<td width="44%"> 
													<input id="extIdName" name="extIdName" type="text" class="black_ar" size="20" maxlength="255" class="black_ar" />
												</td>
												<td width="44%">
													<input id="extIdValue" type="text" class="black_ar" size="20" maxlength="255" style="text-align:right;" class="black_ar" />
												</td>
												<td width="44%">
													<input id="addEditExtIdButton" name="addEditExtIdButton" type="button" value="Add" class="black_ar" onclick="addEditExtIdTag(this)" />
												</td>
											</tr>
									      </table>
									</div>
								</td>
							</tr>
							
							<tr class="tr_alternate_color_lightGrey">
								<td width="20%" > &nbsp;	</td>
								<td colspan="3" align="left"  valign="middle">
									<ul id="externalIDList" class="tagEditor">
											<c:if test="${not empty specimenDTO.externalIdentifiers}">
													<c:forEach var="externalId" items="${specimenDTO.externalIdentifiers}">
														<c:if test="${not empty externalId.name}">
														<li id="li${externalId.id}" title="Edit">
															<span id="Ext_${externalId.id}" name="ExtIds" onclick="editTag(this)">${externalId.name} - ${externalId.value}</span>
															<a title="Delete" onclick="deleteTag(this)">X</a>
															<input type="hidden" name="Ext_${externalId.id}Status" id="Ext_${externalId.id}Status" value=${externalId.status}>
														</li>
														</c:if>
													</c:forEach>		
											</c:if>
									</ul>
								 </td>
							</tr>
								
								<tr class="tr_alternate_color_white">
									<td width="20%" class="black_ar align_right_style">
										<bean:message key="specimen.biohazards"/>
									</td>

									<td width="30%">
										<a id="addBioHazard" title="Add New BioHazard" class="link" onclick="showAddBioHazardDiv()">Add New</a>
									</td>
									<td  width="50%" class="black_ar" colspan="2">
										<div id="addBioHazardDiv" style="display:none;">
											<table width="100%" border="0" cellpadding="0" cellspacing="0">
											<tr valign="bottom">
												<td width="44%"> 
													<div id="biohazardTypeSelect"></div>
												</td>
												<td width="44%">
													<div id="biohazardSelect"></div>
												</td>
												<td width="44%">
													<input id="addEditBioHazButton" name="addEditBioHazButton" type="button" value="Add" class="black_ar" onclick="addEditBioHazTag(this)" />
												</td>
											</tr>
											</table>
										</div>
									</td>
								</tr>
								
								<tr class="tr_alternate_color_white">
									<td width="20%"> &nbsp;</td>	
									<td align="left" colspan="3" valign="middle">
										<ul id="bioHazardList" class="tagEditor">
											<c:if test="${not empty specimenDTO.bioHazards}">
													<c:forEach var="biohazard" items="${specimenDTO.bioHazards}">
														<li id="li${biohazard.id}" title="Edit">
															<span id="Bio_${biohazard.id}" name="Biohazards" onclick="editBiohazardTag(this)">${biohazard.type} - ${biohazard.name}</span>
															<a title="Delete" onclick="deleteTag(this)">X</a>
															<input type="hidden" name="Bio_${biohazard.id}Status" id="Bio_${biohazard.id}Status" value=${biohazard.status}>
														</li>
													</c:forEach>		
											</c:if>
										</ul>
									</td>
								</tr>
						<tr>
						  <td colspan="4" valign="middle" class="tr_bg_blue1">
							<span class="blue_ar_b">&nbsp;<bean:message key="childSpecimen.label" /></span>
						  </td>
						</tr>
								
							</table>
						  </td>
						</tr>
						
					
						
							<!-- collectionstatus -->
						</table>
					</div>
				</td>
			</tr>
		    
		    <tr>
								<!--
          <td valign="middle" class="black_ar" >
		  -->
		  <td valign="top">
		  <table width="100%" border="0" cellpadding="3" cellspacing="0">
		       <tr>
                  <td width="17%" align="left" nowrap class="black_ar" colspan="2">
						<input type="radio" value="1" id="aliquotCheck" name="specimenChild" onclick="onCheckboxButtonClick(this)" checked="true"/>
								<bean:message key="app.none" />&nbsp;
						<input type="radio" value="2" id="aliquotChk" name="specimenChild" onclick="onCheckboxButtonClick(this)"/>
								<bean:message key="aliquots.title"/>
								&nbsp;
						<input type="radio" value="3" id="deriveChk" name="specimenChild" onclick="onCheckboxButtonClick(this)"/>
								<bean:message key="specimen.derivative" />
								&nbsp;
						<!-- 11706 S Desctiption : Remove equal check for Edit operation only....-->
						<input type="radio" value="4" id="createCpChildCheckBox" name="specimenChild" onclick="onCheckboxButtonClick(this)"/>
								<bean:message key="create.CpChildSp"/>
						<!-- 11706 E -->
					</td>
				</tr>

				<!--specimenPageButton-->
				<tr><td colspan="2"></td></tr>
				<tr>
					<td class="black_ar" width="18%" nowrap>
							 <div style="display:none" id="derivedDiv">
							 <bean:message key="summary.page.count" />&nbsp;
							<html:text styleClass="black_ar" styleId="numberOfSpecimens" size="10" property="numberOfSpecimens" style="text-align:right"/></div>
							<div style="display:block" id="aliquotDiv"><bean:message key="summary.page.count" />&nbsp;
							<html:text styleClass="black_ar" styleId="noOfAliquots" size="10" property="noOfAliquots" disabled="true" style="text-align:right"/></div>
							</td>
							<td class="black_ar" width="75%">
							<bean:message key="aliquots.qtyPerAliquot"/>&nbsp;

							<html:text styleClass="black_ar" styleId="quantityPerAliquot" size="10" property="quantityPerAliquot" disabled="true" style="text-align:right"/>

					</td>
				</tr>
				 <tr>
								<td class="dividerline" colspan="3"><span class="black_ar"></td>
								</tr>
								<tr>
								

											<td colspan="1" valign="center">
													<html:checkbox styleId="printCheckbox" property="printCheckbox" value="true">
														<span class="black_ar">
															<bean:message key="print.checkboxLabel"/>
														</span>
														</html:checkbox>
											</td>

								
	<!--  Added for displaying  printer type and location -->
								  <td>
					   			     
			 				        </td>

			 				</tr>
				     							
				<tr>
					<td align="left" colspan="2" class="buttonbg">
						<table cellpadding="4" cellspacing="0" border="0" id="specimenPageButton" width="100%"> 
							<tr>
								<td class="buttonbg">
									<input type="button" value="Submit" onclick="submitTabData()" class="blue_ar_b"/>
									<c:if test="${specimenDTO.collectionStatus=='Collected'}">
										| <input type="button" value="Add To Specimen List"
											onclick="organizeTarget()" class="blue_ar_b" />
									</c:if>
										
									<input type="checkbox" name="objCheckbox"  id="objCheckbox" style="display:none" value="team" checked="true"/>
								</td>
							</tr>
						</table>
						 <input type="hidden" id="assignTargetCall" name="assignTargetCall" value="giveCall('AssignTagAction.do?entityTag=SpecimenListTag&entityTagItem=SpecimenListTagItem&objChkBoxString=${specimenDTO.id}','Select at least one existing list or create a new list.','No specimen has been selected to assign.','${specimenDTO.id}')"/>
						<%@ include file="/pages/content/manageBioSpecimen/SpecimenTagPopup.jsp" %>
					</td>
				</tr>

<!-- NEW SPECIMEN REGISTRATION ends-->
				</table>
			</td>
		</tr>
		<tr>
			<td height="*">&nbsp;</td>
		</tr>
		</table>
	 </td>
   </tr>
</table>
</html:form>

<script>
var nodeId= "Specimen_"+document.getElementById("id").value;
refreshTree(null,null,null,null,nodeId);

var tabDataJSON = {};
tabDataJSON["id"] = document.getElementById("id").value; 
//alert(tabDataJSON);

//initialization of clinicalstudytab page
function initialize(startDateObj,endDateObj)
{
	var startDate = calendar.init(startDateObj,"%m-%d-%Y");
	var endDate = calendar.init(endDateObj,"%m-%d-%Y");
}


//creates json string for the changed fields and will validate the fields too
function processData(obj)
{//alert(obj.value);
tabDataJSON["id"] = document.getElementById("id").value; 
tabDataJSON[obj.name] = obj.value; //after rendering struts html tag the 'property' attribute becomes 'name' attribute.
}

function processComboData(objName,objValue)
{
	tabDataJSON[objName] = objValue;
}


//submits changed data
function submitTabData()
{
	
	
	var extidJSON = createExtIdJSON();
	var biohazardJSON = createBioHazardJSON();
	
	
	var isVirtual = document.getElementById("isVirtual").value;
	
	if(isVirtual == 'false')
	{
		tabDataJSON["containerName"]= document.getElementById("containerName").value;
		tabDataJSON["pos1"]= document.getElementById("pos1").value;
		tabDataJSON["pos2"]= document.getElementById("pos2").value;
	}
	tabDataJSON["isVirtual"] = isVirtual; 
	var printFlag = false;
	if(document.getElementById('printCheckbox').checked == true)
	{
		printFlag=true;
	}
	
	var loader = dhtmlxAjax.postSync("CatissueCommonAjaxAction.do","type=updateSpecimen&dataJSON="+JSON.stringify(tabDataJSON)+"&extidJSON="+JSON.stringify(extidJSON)+"&biohazardJSON="+JSON.stringify(biohazardJSON)+"&printLabel="+printFlag);
	
	if(loader.xmlDoc.responseText != null)
	{
		var response = eval('('+loader.xmlDoc.responseText+')')
		if(response.success == "success")
		{
			document.getElementById('print-error').style.display='none';
			document.getElementById('print-success').style.display='none';
			document.getElementById('error').style.display='none';
			document.getElementById('success').style.display='block';
			forwardToChildSpecimen();
		}
		else
		{
			document.getElementById('print-error').style.display='none';
			document.getElementById('print-success').style.display='none';
			document.getElementById('success').style.display='none';
			document.getElementById('errorMsg').innerHTML = response.msg;
			document.getElementById('error').style.display='block';
		}
		if(printFlag)
		{
			if(response.printLabel == "success")
			{
				document.getElementById('print-error').style.display='none';
			document.getElementById('print-success').innerHTML = response.printLabelSuccess;
			document.getElementById('print-success').style.display='block';
				
			}
			else
			{
					document.getElementById('print-success').style.display='none';
			document.getElementById('print-error').innerHTML = response.printLabelError;
			document.getElementById('print-error').style.display='block';
			}
		}
	}
}

function updateHelpURL()
{
	var URL="";
	return URL;
}


function loadDHTMLXWindowForTransferEvent()
{
//alert(document.getElementById('barcode'));
var w =700;
var h =450;
var x = (screen.width / 3) - (w / 2);
var y = 0;
dhxWins = new dhtmlXWindows(); 
dhxWins.createWindow("containerPositionPopUp", x, y, w, h);
var url = "ShowStoragePositionGridView.do?pageOf=pageOfSpecimen&forwardTo=gridView&pos1=<bean:write name='specimenDTO' property='pos1' scope='request'/>&pos2=<bean:write name='specimenDTO' property='pos2' scope='request'/>&holdSpecimenClass=<bean:write name='specimenDTO' property='className' scope='request'/>&holdSpecimenType=<bean:write name='specimenDTO' property='type' scope='request'/>&containerName=<bean:write name='specimenDTO' property='containerName' scope='request'/>&collectionProtocolId=${requestScope.cpId}&collStatus=<bean:write name='specimenDTO' property='collectionStatus' scope='request'/>&isVirtual=<bean:write name='specimenDTO' property='isVirtual' scope='request'/>";
dhxWins.window("containerPositionPopUp").attachURL(url);                     
//url : either an action class or you can specify jsp page path directly here
dhxWins.window("containerPositionPopUp").button("park").hide();
dhxWins.window("containerPositionPopUp").allowResize();
dhxWins.window("containerPositionPopUp").setModal(true);
dhxWins.window("containerPositionPopUp").setText("");    //it's the title for the popup
}
initSpecimenCombo();
initializeSpecimenPage(${biohazardTypeNameListJSON});
prepareSpecimenTypeOptions(${cellTypeListJSON},${molecularTypeListJSON},${tissueTypeListJSON},${fluidTypeListJSON});

</script>
