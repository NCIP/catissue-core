	<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
	<%@ page import="java.util.List"%>
	<%@ taglib uri="/WEB-INF/multiSelectUsingCombo.tld" prefix="mCombo" %>
	<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
	<%@ page import="edu.wustl.common.util.global.ApplicationProperties" %>

    <script language="JavaScript" type="text/javascript" src="jss/ext-base.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/ext-all.js"></script>
	<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />

	<script>Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Tissue';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_tissue',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'tissue'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});</script>
	<script>Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Fluid';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_fluid',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'fluid'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});</script>
	<script>Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Cell';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_cell',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'cell'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});</script>
	<script>Ext.onReady(function(){var myUrl= 'SpecimenTypeDataAction.do?method=Molecular';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_molecular',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'molecular'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});</script>


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