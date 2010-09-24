<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="java.util.List,java.util.Map"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page language="java" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/multiSelectUsingCombo.tld" prefix="mCombo" %>
<head>
	<script>var imgsrc="catissuecore/images/de/";</script>
	<script language="JavaScript" type="text/javascript" src="/catissuecore/javascripts/de/ajax.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/multiselectUsingCombo.js"></script>

	<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	<link rel="stylesheet" type="text/css" href="css/catissue_suite.css"  >
	<script>
		Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Tissue';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_tissue',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'tissue'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});});
	</script>

	<script>Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Fluid';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_fluid',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'fluid'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7 || Ext.isSafari3){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});});</script>

	<script>Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Cell';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_cell',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'cell'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7 ||  Ext.isSafari3){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});});</script>

	<script>Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Molecular';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_molecular',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'molecular'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7 || Ext.isSafari3){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});});</script>

	<script type="text/javascript">
	//Dimension 2 capacity label
	function capacityChanged(element)
	{
		var elementValue = element.value;
		if(elementValue.length>0)
		{
			try
			{
				var num = parseInt(elementValue);
				col1= document.getElementById("col1");
				col2= document.getElementById("col2");
				if(num>1)
				{
					col1.innerHTML='<span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span>';
					col2.className="black_ar";
				}
				else
				{
					col1.innerHTML="&nbsp;";
					col2.className="formLabel";
				}
			}
			catch(err)
			{
				//alert("Please enter a valid number.");
			}
		}
	}

	function selectSpType()
	{
		var tissue = document.getElementById('holdsTissueSpType');
		var fluid  = document.getElementById('holdsFluidSpType');
		var cell   = document.getElementById('holdsCellSpType');
		var mol    = document.getElementById('holdsMolSpType');
		if (tissue != null)
		{
			for (i = tissue.options.length-1; i >= 0; i--)
			{
				tissue.options[i].selected=true;
			}
		}

		if (fluid != null)
		{
			for (i = fluid.options.length-1; i >= 0; i--)
			{
				fluid.options[i].selected=true;
			}
		}

		if (cell != null)
		{
			for (i = cell.options.length-1; i >= 0; i--)
			{
				cell.options[i].selected=true;
			}
		}

		if (mol != null)
		{
			for (i = mol.options.length-1; i >= 0; i--)
			{
				mol.options[i].selected=true;
			}
		}
	}

	function validate(submittedFor,forwardTo)
	{
		document.forms[0].submittedFor.value = submittedFor;
		document.forms[0].forwardTo.value    = forwardTo;
		if(validateAny(document.forms[0].holdsStorageTypeIds)==false)
		{
			alert("<bean:message key="errmsg.storagetype"/>");
		}
		else
		{
			if(validateAny(document.forms[0].holdsSpecimenClassTypes)==false)
			{
				alert("<bean:message key="errmsg.storagetype"/>");
			}
			else
			{
				document.forms[0].submit();
			}
		}

	}

	function onRadioButtonClick(element)
	{
		var specimenClass = document.getElementById("holdSpecimenClassTypeIds");
		var specimenArray = document.getElementById("holdsSpecimenArrTypeIds");
		if(element == "Specimen")
		{
			specimenClass.disabled = false;
			specimenArray.disabled = true;
			var len = specimenArray.length;
			for (var i = 0; i < len; i++)
			{
				specimenArray.options[i].selected = false;
			}
		}
		if(element == "SpecimenArray")
		{
			specimenClass.disabled = true;
			specimenArray.disabled = false;
			var len = specimenClass.length;
			for (var i = 0; i < len; i++)
			{
				specimenClass.options[i].selected = false;
			}

		}
	}
	function calltest()
	{
		document.getElementById('sp_type').style.display='none';
		showHide('sp_type');
	}
	</script>
</head>
<body onload="javascript:calltest()">
<link href="css/catissue_suite.css" rel="stylesheet" type="text/css" />
<!-- Contents Starts here -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<html:form action='${requestScope.formName}'method="post">
		<html:hidden property="operation"/>
		<html:hidden property="submittedFor"/>
		<html:hidden property="forwardTo"/>
		<html:hidden property="id" />
		<html:hidden property="redirectTo"/>
  <tr>
    <td class="td_color_bfdcf3"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_table_head"><span class="wh_ar_b"><bean:message key="storageType.name" /></span></td>
        <td><img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - StorageType" width="31" height="24" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="tablepadding"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
	<!-- for tabs selection -->
					<logic:equal name="operation" value='${requestScope.operationAdd}'>
        <td valign="bottom" ><img src="images/uIEnhancementImages/tab_add_selected.jpg" alt="Add" width="57" height="22" /></td>
        <td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfStorageType&aliasName=StorageType"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
		</logic:equal>
		<logic:equal name="operation" value='${requestScope.operationEdit}'>
		<td valign="bottom" ><html:link page="/StorageType.do?operation=add&pageOf=pageOfStorageType"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add" width="57" height="22" /></html:link></td>
        <td valign="bottom"><img src="images/uIEnhancementImages/tab_edit_selected.jpg" alt="Edit" width="59" height="22" border="0" /></td>
		</logic:equal>

        <td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
      </tr>
    </table>
      <table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
        <tr>
          <td colspan="2" align="left" class="bottomtd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
        </tr>
        <tr>
          <td colspan="2" align="left" class="tr_bg_blue1"><span class="blue_ar_b"> &nbsp;<logic:equal name="operation" value='${requestScope.operationAdd}'><bean:message key="storageType.title"/></logic:equal><logic:equal name="operation" value='${requestScope.operationEdit}'><bean:message key="storageType.editTitle"/></logic:equal></span></td>
        </tr>
        <tr>
          <td colspan="2" align="left" class="showhide"><table width="100%" border="0" cellpadding="3" cellspacing="0">

                <tr>
                  <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar"><label for="type">
														<bean:message key="storageType.type"/>
									    			</label></td>
                  <td colspan="3" align="left">
                    <html:text styleClass="black_ar"  maxlength="255"  size="30" styleId="type" property="type"/>
                  </td>
                </tr>
                <tr>
                  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" class="black_ar"><label for="defaultTemperature">
														<bean:message key="storageType.defaultTemperature"/>
													</label></td>
                  <td align="left"><html:text styleClass="black_ar"  maxlength="10"  size="20" styleId="defaultTemperature" property="defaultTemperature" style="text-align:right"/>
													<span class="black_ar">
														&nbsp;<sup>0</sup>C
													</span></td>
                </tr>

				<tr>
				 <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
				 <td align="left" class="black_ar"><bean:message key="storageType.oneDimensionLabel"/></td>
                 <td align="left" valign="top" class="black_ar"><html:text styleClass="black_ar" maxlength="255" size="20" styleId="oneDimensionLabel" property="oneDimensionLabel"/>
					&nbsp;&nbsp;&nbsp;&nbsp;
                  <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
                  <label for="oneDimensionCapacity">
						<bean:message key="storageType.oneDimensionCapacity"/>
		   		  </label>
				  &nbsp;&nbsp;&nbsp;&nbsp;
					<html:text styleClass="black_ar"  maxlength="10"  size="20" styleId="oneDimensionCapacity" property="oneDimensionCapacity" style="text-align:right"/></td>
				</tr>
				 <td width="1%" align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                  <td align="left" class="black_ar">
					  <label for="twoDimensionLabel">
						<bean:message key="storageType.twoDimensionLabel"/>
					  </label>
				  </td>

                  <td align="left" valign="top" class="black_ar"><html:text styleClass="black_ar"  maxlength="255" size="20" styleId="twoDimensionLabel" property="twoDimensionLabel"/>
                  &nbsp;&nbsp;&nbsp;&nbsp;
				  <img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				  <label for="twoDimensionCapacity">
						<bean:message key="storageType.twoDimensionCapacity"/>
				  </label>
				  &nbsp;&nbsp;&nbsp;&nbsp;
				  <html:text styleClass="black_ar"  maxlength="10"  size="20" styleId="twoDimensionCapacity" property="twoDimensionCapacity" onkeyup="capacityChanged(this)" style="text-align:right" /></td>
                </tr>
				<tr><td>&nbsp;</td></tr>
                <tr>

				  <td align="center" class="black_ar">&nbsp;</td>
                  <td align="left" valign="top" class="black_ar_t"><bean:message key="storageType.holds" /> </td>
                  <td colspan="2" align="left"><table width="100%" border="0" cellspacing="0" cellpadding="2">
                      <tr>
                        <td width="25%" align="left" class="tabletd1">&nbsp;<bean:message key="storageType.name" /></td>
						<td width="25%" align="left" class="tabletd1"><label>
                          <html:radio property="specimenOrArrayType" value="Specimen" onclick="onRadioButtonClick('Specimen')"/>
                          </label>
                          <bean:message key="storageContainer.specimenClass" /></td>
                        <td width="25%" align="left" class="tabletd1"><label>
                          <html:radio property="specimenOrArrayType" value="SpecimenArray" onclick="onRadioButtonClick('SpecimenArray')"/>
                          </label>
                          <bean:message key="storageContainer.specimenArrayType" /></td>
	                    </tr>
                      <tr>
                        <td align="left" valign="top" class="tabletd1" style="padding-bottom:5px; padding-left:5px;">
                          <html:select property="holdsStorageTypeIds" styleClass="formFieldSized12" styleId="holdStorageTypeIds" size="5" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
														<html:options collection='${requestScope.holds_List_1}' labelProperty="name" property="value"/>
													</html:select>
                        </td>
						<td align="left" valign="top" class="tabletd1">

								<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="Specimen">
									<html:select property="holdsSpecimenClassTypes" styleClass="formFieldSized12" styleId="holdSpecimenClassTypeIds" size="5" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<html:options collection='${requestScope.holds_List_2}' labelProperty="name" property="value"/>
									</html:select>
								</logic:equal>
								<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="SpecimenArray">
										<html:select property="holdsSpecimenClassTypes" styleClass="formFieldSized12" styleId="holdSpecimenClassTypeIds" size="5" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
											<html:options collection='${requestScope.holds_List_2}' labelProperty="name" property="value"/>
										</html:select>
								</logic:equal>
						</td>
                        <td align="left" valign="top" class="tabletd1"><logic:equal name="storageTypeForm" property="specimenOrArrayType" value="SpecimenArray">
													<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldSized12" styleId="holdsSpecimenArrTypeIds" size="5" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
														<html:options collection='${requestScope.holds_List_3}' labelProperty="name" property="value"/>
													</html:select>
				</logic:equal>
				<logic:equal name="storageTypeForm" property="specimenOrArrayType" value="Specimen">
													<html:select property="holdsSpecimenArrTypeIds" styleClass="formFieldSized12" styleId="holdsSpecimenArrTypeIds" size="5" multiple="true" onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)" disabled="true">
														<html:options collection='${requestScope.holds_List_3}' labelProperty="name" property="value"/>
													</html:select>
				</logic:equal></td>
                      </tr>
                  </table></td>
                </tr>

          </table></td>
        </tr>

		<tr onclick="javascript:showHide('sp_type')">
          <td width="96%" align="left" class="tr_bg_blue1"><span class="blue_ar_b">&nbsp;<bean:message key="storageType.holdsSpecimenClass"/></span></td>
          <td width="4%" align="right" class="tr_bg_blue1">
			  <a id="imgArrow_sp_type">
				  <img src="images/uIEnhancementImages/up_arrow.gif" width="80" height="9" hspace="10" border="0"/>
			  </a>
		  </td>
		</tr>
		<tr>
			<td colspan="2" valign="top" class="showhide1"><div id="sp_type" >
				<table width="100%" border="0" cellpadding="2" cellspacing="0">
					 <tr>
						<td width="1%" align="left" class="tabletd1">&nbsp;</td>
						<td class="tabletd1">
							<label for="holdsTissueSpType">
								<bean:message key="specimenclass.tissue" />
							</label>
						</td>
						<td width="50%" class="tabletd1">
							<mCombo:multiSelectUsingCombo identifier="tissue" styleClass="tabletd1"
								addNewActionStyleClass="tabletd1" size="20"
								addButtonOnClick="moveOptions('tissue','holdsTissueSpType', 'add')"
								removeButtonOnClick="moveOptions('holdsTissueSpType','tissue', 'edit')"
								selectIdentifier="holdsTissueSpType"
								collection="<%=(List) request.getAttribute(Constants.TISSUE_SPECIMEN)%>" numRows="5"/>
						</td>
					</tr>

					<tr>
						<td width="1%" align="left" class="tabletd1">&nbsp;</td>
						<td class="tabletd1">
							<label for="holdsFluidSpType">
								<bean:message key="specimenclass.fluid" />
							</label>
						</td>
						<td width="50%" class="tabletd1">
							<mCombo:multiSelectUsingCombo identifier="fluid" styleClass="tabletd1"
								addNewActionStyleClass="tabletd1" size="20"
								addButtonOnClick="moveOptions('fluid','holdsFluidSpType', 'add')"
								removeButtonOnClick="moveOptions('holdsFluidSpType','fluid', 'edit')"
								selectIdentifier="holdsFluidSpType"
								collection="<%=(List) request.getAttribute(Constants.FLUID_SPECIMEN)%>" numRows="5"/>
						</td>
					</tr>
					<tr>
						<td width="1%" align="left" class="tabletd1">&nbsp;</td>
						<td class="tabletd1">
							<label for="holdsCellSpType">
								<bean:message key="specimenclass.cell" />
							</label>
						</td>
						<td width="50%" class="tabletd1">
							<mCombo:multiSelectUsingCombo identifier="cell" styleClass="tabletd1"
								addNewActionStyleClass="tabletd1" size="20"
								addButtonOnClick="moveOptions('cell','holdsCellSpType', 'add')"
								removeButtonOnClick="moveOptions('holdsCellSpType','cell', 'edit')"
								selectIdentifier="holdsCellSpType"
								collection="<%=(List) request.getAttribute(Constants.CELL_SPECIMEN)%>" numRows="5"/>
						</td>
					</tr>
					<tr>
						<td width="1%" align="left" class="tabletd1">&nbsp;</td>
						<td class="tabletd1">
							<label for="holdsMolSpType">
								<bean:message key="specimenclass.molecular" />
							</label>
						</td>
						<td width="50%" class="tabletd1">
							<mCombo:multiSelectUsingCombo identifier="molecular" styleClass="tabletd1"
								addNewActionStyleClass="tabletd1" size="20"
								addButtonOnClick="moveOptions('molecular','holdsMolSpType', 'add')"
								removeButtonOnClick="moveOptions('holdsMolSpType','molecular', 'edit')"
								selectIdentifier="holdsMolSpType"
								collection="<%=(List)request.getAttribute(Constants.MOLECULAR_SPECIMEN)%>" numRows="5"/>
						</td>
					</tr>
		     </table>
			</td>
        </tr>

        <tr>
          <td colspan="2" class="bottomtd"></td>
        </tr>
        <tr>
          <td colspan="2" class="buttonbg"><html:button styleClass="blue_ar_b" property="submitPage" title="Submit Only"
										value='${requestScope.submit}' onclick='selectSpType(),${requestScope.normalSubmit}'>
									</html:button>
            &nbsp;
            |&nbsp;
                      <html:button styleClass="blue_ar_b" property="storageContainerPage" title="Submit and Add Container" value='${requestScope.addContainer}'
					  					onclick='selectSpType(),${requestScope.forwardToSubmit}' accesskey="Enter">
									</html:button>
           </td>
        </tr>
      </table></td>
  </tr>
  </html:form>
</table>
