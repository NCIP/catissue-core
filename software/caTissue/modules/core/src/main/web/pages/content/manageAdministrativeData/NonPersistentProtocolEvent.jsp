<%@ taglib uri="/WEB-INF/multiSelectUsingCombo.tld" prefix="mCombo" %>
<script>var imgsrc="images/de/";</script>

<script language="JavaScript" type="text/javascript" src="javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript" src="javascripts/de/prototype.js"></script>
<script language="JavaScript" type="text/javascript" src="javascripts/de/combobox.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript" src="jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript" src="javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="/jss/multiselectUsingCombo.js"></script>

<LINK href="css/catissue_suite.css" type=text/css rel=stylesheet>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="css/clinicalstudyext-all.css" />



<script>

		
Ext.onReady(function(){var myUrl= 'SPPData.do?';var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'}, [{name: 'id', mapping: 'id'},{name: 'excerpt', mapping: 'field'}])});var combo = new Ext.form.ComboBox({store: ds,hiddenName: 'CB_spp',displayField:'excerpt',valueField: 'id',typeAhead: 'false',pageSize:15,forceSelection: 'true',queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay:500,lazyInit:true,emptyText:'--Select--',valueNotFoundText:'',selectOnFocus:'true',applyTo: 'spp'});combo.on("expand", function() {if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});ds.on('load',function(){if (this.getAt(0) != null && this.getAt(0).get('excerpt').toLowerCase().startsWith(combo.getRawValue().toLowerCase())) {combo.typeAheadDelay=50;} else {combo.typeAheadDelay=60000}});});
</script>
<script>
Ext.onReady(function(){
   Ext.QuickTips.init();

   var collectionProtocolId;
   

  var myUrl= 'ClinincalStatusComboAction.do';
  var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: myUrl}),
        reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'
        }, [{name: 'excerpt', mapping: 'field'}])
    });
      var combo = new Ext.form.ComboBox({
        store: ds,displayField:'excerpt',typeAhead: false,width: 200, pageSize:15,forceSelection: true ,
		queryParam : 'query',mode: 'remote',triggerAction: 'all',minChars : 3,queryDelay : 300,
		typeAheadDelay : 900,selectOnFocus:true,tpl: '<tpl for="."><div ext:qtip="{excerpt}" class="x-combo-list-item">{excerpt}</div></tpl>',
        applyTo: 'clinicalStatus',fields: ['id_cp'],id: 'idjs_cp'
   });
 combo.on("expand", function(){if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "250");combo.innerList.setStyle("width", "250");}else{combo.list.setStyle("width", "auto");combo.innerList.setStyle("width", "auto");}}, {single: true});


});

//Added this script to avoid the script error on login page
		onerror = handleError;
		function handleError(msg, url, line) 
		{
		      return true;
		}
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			<%@ include file="/pages/content/common/ActionErrors.jsp" %>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td valign="bottom" id="eventTab"><img src="images/uIEnhancementImages/cp_event.gif" alt="Collection Protocol Details" width="94" height="20" /></td>
                       <td width="90%" valign="bottom" class="cp_tabbg">&nbsp;</td>
                      <td valign="top" class="cp_tabbg">&nbsp;</td>
                    </tr>
                </table>
		</td>
	</tr>
    <tr>
		<td class="cp_tabtable">
			<br>
				<table width="100%" border="0" cellpadding="3" cellspacing="0">

					<html:hidden property="labelFormat" />
					<tr>
                        <td width="1%" align="center" valign="top" class="black_ar"></td>
                        <td width="22%" align="left" class="black_ar"><bean:message key="collectionprotocol.studycalendartitle" /></td>
						<td width="77%" align="left">
							<label>
								<html:text styleClass="black_ar_s" size="12" styleId="studyCalendarEventPoint"  maxlength="10" property="studyCalendarEventPoint" />&nbsp;
									<span class="grey_ar">
										<bean:message key="collectionprotocol.studycalendarcomment"/>
									</span>
							</label>
						</td>
   				    </tr>
                    <tr>
						<td align="center" class="black_ar">
						<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
						</td>
                        <td align="left" class="black_ar"><bean:message key="collectionprotocol.collectionpointlabel" />
						<td align="left">
							<html:text styleClass="black_ar" size="30" styleId="collectionPointLabel" maxlength="255"property="collectionPointLabel"/>
						</td>
                    </tr>
					  <!--
                      <tr>
                        <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                        <td align="left" class="black_ar"><bean:message key="specimenCollectionGroup.clinicalDiagnosis"/></td>
                        <td align="left" class="black_new"><autocomplete:AutoCompleteTag property="clinicalDiagnosis"
						optionsList = "<%=request.getAttribute(Constants.CLINICAL_DIAGNOSIS_LIST)%>"
						initialValue="<%=form.getClinicalDiagnosis()%>" styleClass="black_ar" size="27"/>
						<span class="black_ar">
						<%
							String url = "ShowFramedPage.do?pageOf=pageOfTissueSite&propertyName=clinicalDiagnosis&cdeName=Clinical%20Diagnosis";
						%>
						<a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false">
						<img title='Clinical Diagnosis Selector' src="images/uIEnhancementImages/ic_cl_diag.gif" alt="Clinical Diagnosis" width="16" height="16" border="0"></a></span></td>
                      </tr>
					-->
					 <tr>
                       <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
                       <td align="left" class="black_ar"><bean:message key="specimenCollectionGroup.clinicalDiagnosis"/>
					   <td align="left" class="black_ar">
						<input property="clinicalDiagnosis" type="text" id="clinicaldiagnosis" name="clinicalDiagnosis" value="<%=request.getAttribute("clinicalDiagnosis")%>" onmouseover="showTip(this.id)"/>
						</td>
					</tr>

                      <tr>
                        <td align="center" class="black_ar"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></td>
                        <td align="left" class="black_ar"><bean:message key="specimenCollectionGroup.clinicalStatus"/></td>
                      		<td align="left" class="black_ar">
							<input property="clinicalStatus" type="text" id="clinicalStatus" name="clinicalStatus" value="<%=form.getClinicalStatus()%>" onmouseover="showTip(this.id)"/>
							
							</td>
					 </tr>

					  <tr>
                        <td align="center">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                      </tr>
					  <tr>

					 	  <td align="center" class="black_ar">&nbsp;</td>

						  <td align="left" class="black_ar">
					  		Specimen Processing Procedure
					   	  </td>

						  <td align="left" class="black_ar">
						   <mCombo:multiSelectUsingCombo
								identifier="spp"
								size="31"
								styleClass="black_ar_new"
								addNewActionStyleClass="black_ar_new"
								addButtonOnClick="moveOptions('spp','specimenProcessingProcedure', 'add')"
								removeButtonOnClick="moveOptions('specimenProcessingProcedure','spp', 'edit')"
								selectIdentifier="specimenProcessingProcedure"
								collection="<%=(List)request.getAttribute(Constants.SELECTED_SPP)%>"
								numRows="4" />
					   	  </td>

						</tr>
                     <tr>

					<td class="buttonbg" colspan="3">
						<html:button styleClass="blue_ar_b" property="submitPage" onclick="submitAllEvents()">
							<bean:message key="buttons.submit"/>
						</html:button>
						&nbsp;|
						<html:button styleClass="blue_ar_b" property="submitPage" onclick="specimenRequirements()">
							<bean:message key="cpbasedentry.addspecimenrequirements"/>
						</html:button>

						<logic:equal name="operation" value="edit">
						&nbsp;|
							<html:button styleClass="blue_ar_b" property="submitPage" onclick="deleteEvent()">
									<bean:message key="buttons.delete"/>
							</html:button>
						</logic:equal>
						<logic:notEqual name="operation" value="edit">
							&nbsp;
						</logic:notEqual>
					</td>
				</tr>
             </table>
           </td>
         </tr>
      </table>