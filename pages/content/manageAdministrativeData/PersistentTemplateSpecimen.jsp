    <tr>
		<td class="cp_tabtable">
			<br>
				<table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">

					<tr>
                      <td colspan="3" align="left">
						 <table width="100%" border="0" cellpadding="3" cellspacing="0">
							
							<tr>
		                      <td align="center" class="black_ar"><span class="blue_ar_b"></span></span></td>
                              <td align="left" class="black_ar"><bean:message key="cpbasedentry.specimenReqTitle"/></td>
                              <td align="left" class="black_ar"><%if(Constants.ALIQUOT.equalsIgnoreCase(form.getLineage())){%>${createSpecimenTemplateForm.specimenReqTitle}<%}else{%>
                              <html:text styleClass="black_ar" size="23" styleId="specimenLabel" maxlength="255" property="specimenReqTitle"/><%}%>
						          </td>
						          
						</tr> 
							
							
							<tr>
								<td width="1%" align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
							   <td width="22%" align="left" class="black_ar"><bean:message key="specimen.type"/> </td>
							   <td width="33%" align="left"  class="black_ar">
								<%
									String classValue = (String)form.getClassName();
									specimenTypeList = (List)specimenTypeMap.get(classValue);
									boolean subListEnabled = false;
									if(specimenTypeList == null)
									{
										specimenTypeList = new ArrayList();
										specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
									}
									if(Constants.ALIQUOT.equals(form.getLineage()))
									{
										specimenTypeList = new ArrayList();
										specimenTypeList.add(new NameValueBean(form.getType(),form.getType()));
									}
									pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
									String subTypeFunctionName ="onSubTypeChangeUnit('className',this,'unitSpan')";
									String readOnlyForAliquot = "false";
									String readOnlyForSpecimen = "false";
									if(Constants.ALIQUOT.equals(form.getLineage())&&operation.equals(Constants.EDIT))
									{
										  readOnlyForAliquot = "true";
									}
									if(!Constants.DERIVED_SPECIMEN.equals(form.getLineage())&&operation.equals(Constants.EDIT))
									{
										  readOnlyForSpecimen = "true";
									}

								%>
										<autocomplete:AutoCompleteTag property="className"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_CLASS_LIST)%>"
										  initialValue="<%=form.getClassName()%>"
										  readOnly="false"
										  onChange="onTypeChange(this);clearTypeCombo()"
										  styleClass="black_ar"
										  size="20"
										/>

                                <td width="1%" align="center"><span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
							    <td width="15%" align="left"><label for="type" class="black_ar"><bean:message key="specimen.subType"/></label></td>
   									<td width="28%" align="left" class="black_ar">
									<div id="specimenTypeId">
										<autocomplete:AutoCompleteTag property="type"
										  optionsList = "<%=request.getAttribute(Constants.SPECIMEN_TYPE_MAP)%>"
										  initialValue="<%=form.getType()%>"
										  onChange="<%=subTypeFunctionName%>"
										  readOnly="false"
										  dependsOn="<%=form.getClassName()%>"
										  styleClass="black_ar"
										  size="20"
										/>
										</div>
									</td>
                        </tr>
                        <tr>
                           <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                           <td align="left" class="black_ar"><bean:message key="specimen.tissueSite"/></td>
						  		 <td width="30%" align="left" class="black_new" >
									<autocomplete:AutoCompleteTag property="tissueSite"
									  optionsList = "<%=request.getAttribute(Constants.TISSUE_SITE_LIST)%>"
									  initialValue="<%=form.getTissueSite()%>"
									  readOnly="false"
									  styleClass="black_ar"
									  size="20"
								/>

								<span class="black_ar">
				<%
					String url = "TissueSiteTree.do?pageOf=pageOfTissueSite&propertyName=tissueSite&cdeName=Tissue Site";
				%>
									<a href="#" onclick="javascript:NewWindow('<%=url%>','name','360','525','no');return false">
										<img src="images/uIEnhancementImages/ic_cl_diag.gif" border="0" width="16" height="16" title='Tissue Site Selector' alt="Clinical Diagnosis"></a></span></td>
									<td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>


						   <td align="left" class="black_ar"><bean:message key="specimen.tissueSide"/></td>
                        		
								<td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="tissueSide"
										optionsList = "<%=request.getAttribute(Constants.TISSUE_SIDE_LIST)%>"
									    initialValue="<%=form.getTissueSide()%>"
									    readOnly="false"
										styleClass="black_ar"
										size="20"
								    />
								</td>
				</tr>

                              <tr>
                                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                                <td align="left" class="black_ar"><bean:message key="specimen.pathologicalStatus"/> </td>
				                                <td width="30%" align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="pathologicalStatus"
									  optionsList = "<%=request.getAttribute(Constants.PATHOLOGICAL_STATUS_LIST)%>"
									  initialValue="<%=form.getPathologicalStatus()%>"
									  readOnly="false"
									  styleClass="black_ar"
									  size="20"
									/>
								</td>
                               <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                               <td align="left" class="black_ar"><bean:message key="cpbasedentry.storagelocation"/></td>
                                				<td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="storageLocationForSpecimen"
									  optionsList = "${requestScope.storageContainerList}"
									  initialValue="<%=form.getStorageLocationForSpecimen()%>"
									  styleClass="black_ar"
									  size="20"
									/>
								</td>
                              </tr>
                              <tr>
                                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                                <td align="left" class="black_ar"><bean:message key="specimen.quantity"/></td>
                                 	 <td width="28%" align="left" class="black_ar">
					 <html:text styleClass="black_ar" size="10" maxlength="10"styleId="quantity" property="quantity" style="text-align:right"/>
					<span id="unitSpan">&nbsp;<%=unitSpecimen%></span><html:hidden property="unit"/></td>
                                <td align="center" class="black_ar">&nbsp;</td>

                                <td align="left" class="black_ar"><bean:message key="specimen.concentration"/></td>

							  		<td align="left" class="black_ar_s">
									<%
										boolean concentrationDisabled = true;
										if(form.getClassName().equals("Molecular") && !Constants.ALIQUOT.equals(form.getLineage()))
										concentrationDisabled = false;
									%>
     									<html:text styleClass="black_ar" maxlength="10"  size="10"	styleId="concentration" property="concentration"  readonly="<%=readOnlyForAll%>" disabled="false" style="text-align:right"/>
								&nbsp;<bean:message key="specimen.concentrationUnit" />
								</td>
                              </tr>
                              <tr>
									<html:hidden property="collectionEventId" />
									<html:hidden property="collectionEventSpecimenId" />
                                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
								<td align="left" class="black_ar"><bean:message key="specimen.collectedevents.username"/></td>
							          <td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="collectionEventUserId"
									  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
									  initialValue="<%=new Long(form.getCollectionEventUserId())%>"
									  staticField="false"
									  styleClass="black_ar"
									  size="20"
									/>
								</td>
							  	<html:hidden property="receivedEventId" />
									<html:hidden property="receivedEventSpecimenId" />
                               <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                               <td align="left" class="black_ar"><bean:message key="specimen.receivedevents.username"/></td>
								<td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="receivedEventUserId"
									  optionsList = "<%=request.getAttribute(Constants.USERLIST)%>"
									  initialValue="<%=new Long(form.getReceivedEventUserId())%>"
									  staticField="false"
									  styleClass="black_ar"
									  size="20"
									/>
								</td>
                              </tr>
                              <tr>
                               <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                              <td align="left" class="black_ar"><bean:message key="cpbasedentry.collectionprocedure"/></td>
				                                <td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="collectionEventCollectionProcedure"
									  optionsList = "<%=request.getAttribute(Constants.PROCEDURE_LIST)%>"
									  initialValue="<%=form.getCollectionEventCollectionProcedure()%>"
									  styleClass="black_ar"
									  size="20"
									/>
								</td>
                                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                                <td align="left" class="black_ar"><label for="institutionId"><bean:message key="cpbasedentry.receivedquality"/></label></td>
				                                <td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="receivedEventReceivedQuality"
									  optionsList = "<%=request.getAttribute(Constants.RECEIVED_QUALITY_LIST)%>"
									  initialValue="<%=form.getReceivedEventReceivedQuality()%>"
									  styleClass="black_ar"
									  size="20"
									/>
								</td>
                              </tr>
                              <tr>
							  <!-- CollectionEvent fields -->
                                <td align="center" class="black_ar"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span></td>
                                <td align="left" class="black_ar"><label for="departmentId"><bean:message key="cpbasedentry.collectioncontainer"/></label></td>
								<td align="left" class="black_ar">
									<autocomplete:AutoCompleteTag property="collectionEventContainer"
									  optionsList = "<%=request.getAttribute(Constants.CONTAINER_LIST)%>"
									  initialValue="<%=form.getCollectionEventContainer()%>"
									  styleClass="black_ar"
									  size="20"
								    />
								</td>
  							   
							   <%
								if(Variables.isTemplateBasedLblGeneratorAvl)
								{
							%>
                                <td align="center" class="black_ar">&nbsp;</td>

                                <td align="left" class="black_ar">Label Format</td>

									<td class="black_ar" >
								<span class="black_ar" >

								<html:text styleClass="black_ar"property="labelFormat" maxlength="255" styleId="labelFormat" size="23"/></span>

								</td>
								<%}
								else
								{
								%>
								<td colspan="3"/>
								<%}%>
                              </tr>
                            </table>
                            <br>

					</td>
                   </tr>
                   <tr onclick="javascript:showHide('derive_specimen')">
					 <td width="97%" align="left" class="tr_bg_blue1">
						<span class="blue_ar_b">
							<bean:message key="cpbasedentry.derivespecimens"/>
						</span>
					  </td>
                      <td width="3%" align="right" class="tr_bg_blue1">
						<a href="#" id="imgArrow_derive_specimen">
							<img src="images/uIEnhancementImages/dwn_arrow1.gif" alt="Show Details" border="0" width="80" height="9" hspace="10" vspace="0"/>
						</a>
					  </td>
                     </tr>
                       <td colspan="2" class="showhide1">
							<div id="derive_specimen" style="display:none" >
								<table width="100%" border="0" cellspacing="0" cellpadding="4">
									<tr>
                              <td width="2%" class="tableheading"><span class="black_ar_b">
                                <label for="delete" align="center"><bean:message key="addMore.delete" /></label>
                              </span></td>
                              <td width="10%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /> </span><bean:message key="collectionprotocol.specimenclass" /> </span></td>
                              <td width="12%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span> <bean:message key="collectionprotocol.specimetype" /> </span></td>
                              <td width="15%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span> <bean:message key="cpbasedentry.storagelocation"/></span></td>
                              <td width="10%" class="tableheading"><span class="black_ar_b"><bean:message key="collectionprotocol.quantity" /></span></td>
                              <td width="10%" class="tableheading"><span class="black_ar_b"><span class="blue_ar_b"><img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" /></span> <bean:message key="cpbasedentry.concentration"/></span></td>
			     <td width="18%" class="tableheading"><span class="black_ar_b">Title</span></td>	 
<%
								if(Variables.isTemplateBasedLblGeneratorAvl)
								{
							%>

							  <td width="23%" class="tableheading"><span class="black_ar_b">Label format</span></td>
							  <%}%>
                            </tr>
						  <script> document.forms[0].noOfDeriveSpecimen.value = <%=noOfDeriveSpecimen%> </script>

										<TBODY id="DeriveSpecimenBean">
			<%

				for(int rowno=1;rowno<=noOfDeriveSpecimen;rowno++)
				{
					String id = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_id)";
					String collectionEventKey = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_collectionEventId)";
					String receivedEventKey = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_receivedEventId)";
					String idKey = "DeriveSpecimenBean:" + rowno + "_id";
					String specimenClass = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenClass)";
					String classKey = "DeriveSpecimenBean:" + rowno + "_specimenClass";
					String unit = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_unit)";
					String specimenUnit = "DeriveSpecimenBean:" + rowno + "_unit";
					String specimenType = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_specimenType)";
					String srSubTypeKeyName = "DeriveSpecimenBean:" + rowno + "_specimenType";
					String storageLocation = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_storageLocation)";
					String storageLocationKey = "DeriveSpecimenBean:" + rowno + "_storageLocation";
					String quantity = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_quantity)";
					String quantityvalue = "DeriveSpecimenBean:" + rowno + "_quantity";
					String concentration = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_concentration)";
					String MolecularConc = "DeriveSpecimenBean:" + rowno + "_concentration";
					String chk = "checkBox_" + rowno;
					String changeClass = "changeUnit('"+specimenClass+"','"+unit+"','"+concentration+"','"+specimenType+"')";
					String changeType = "onSubTypeChangeUnitforCP('"+specimenClass+"','" + unit+ "')";
					
					String requirementLabel = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_requirementLabel)";
					String requirementLabelKey = "DeriveSpecimenBean:" + rowno + "_requirementLabel";
					//String labelType = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_labelGenType)";
					//String labelTypeKey = "DeriveSpecimenBean:" + rowno + "_labelGenType";
					String labelFormat = "deriveSpecimenValue(DeriveSpecimenBean:" + rowno + "_labelFormat)";
					//String labelFormatKey = "DeriveSpecimenBean:" + rowno + "_labelFormat";
					//String changeLabelGenType = "labelGenTypechangedWithId('"+labelType+"','"+labelFormat+"')";
			%>
<tr>

					<html:hidden property="<%=collectionEventKey%>" />
					<html:hidden property="<%=receivedEventKey%>" />
					<html:hidden property="<%=id%>" />
			<%
					String requirementLabelValue = (String)form.getDeriveSpecimenValue(requirementLabelKey);
			        String idKeyValue = (String)form.getDeriveSpecimenValue(idKey);
					String className = (String)form.getDeriveSpecimenValue(classKey);
					String typeclassValue = (String)form.getDeriveSpecimenValue(srSubTypeKeyName);
					String storageLocationValue = (String)form.getDeriveSpecimenValue(storageLocationKey);
					specimenTypeList = (List)specimenTypeMap.get(className);
					boolean subListEnabled1 = false;
					if(specimenTypeList == null)
					{
						specimenTypeList = new ArrayList();
						specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
						specimenClassList.add(0,new NameValueBean(Constants.SELECT_OPTION,"-1"));
					}
					pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
					pageContext.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
					if("-1".equals(className) || "-1".equals(typeclassValue))
					{
						pageContext.setAttribute("isPersistentValue", false);
					}
					else
					{
						if("-1".equals(idKeyValue))
						{
							pageContext.setAttribute("isPersistentValue", false);
						}
						else
						{
							pageContext.setAttribute("isPersistentValue", true);
						}
					}
				%>

								
								<logic:equal name="isPersistentValue" value="true">
								<td class="black_ar">
									<label>
										<input type=checkbox name="<%=chk%>" id="<%=chk%>" disabled>
	                                </label>
								</td>
								</logic:equal>
  							    <logic:notEqual name="isPersistentValue" value="true">
								<td class="black_ar">
									<label>
										<input type=checkbox name="<%=chk%>" id="<%=chk%>" >
		                            </label>
								</td>
  							   </logic:notEqual>



							   <logic:equal name="isPersistentValue" value="true">
								<td class="black_ar">
									<label>
										<%=className%>
	                                </label>
								</td>
								</logic:equal>
  							    <logic:notEqual name="isPersistentValue" value="true">
								<td class="black_ar" >
									<html:select property= "<%=specimenClass%>"
									styleClass="formFieldSized8"
									styleId="<%=specimenClass%>" size="1"
									onchange="<%=changeClass%>"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
									</html:select>
								</td>
								 </logic:notEqual>

								<logic:equal name="isPersistentValue" value="true">
								<td class="black_ar">
									<label>
										<%=typeclassValue%>
	                                </label>
								</td>
								</logic:equal>
  							    <logic:notEqual name="isPersistentValue" value="true">

	                                <td class="black_ar" >
									<html:select property="<%=specimenType%>"
									styleClass="addRow_s_new"
									styleId="<%=specimenType%>"  size="1"
									onchange="<%=changeType%>"
									onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
										<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
									</html:select>
								</td>

								</logic:notEqual>
									<td class="black_ar" >
										<html:select property="<%=storageLocation%>"
										styleClass="formFieldSized8"
										styleId="<%=storageLocation%>" size="1"
										onmouseover="showTip(this.id)" onmouseout="hideTip(this.id)">
									<html:options collection= "storageContainerList" labelProperty="name" property="value"/>
										</html:select>
									</td>
                                <td class="black_ar">
			<%

					String qtyValue = (String)form.getDeriveSpecimenValue(quantityvalue);
					String concValue = (String)form.getDeriveSpecimenValue(MolecularConc);
					String strHiddenUnitValue = "" + changeUnit(className,typeclassValue);
					if(qtyValue == null || qtyValue.equals(""))
					{
						qtyValue="0";
					}
					if(concValue == null || concValue.equals(""))
					{
						concValue="0";
					}

				%>
										<html:text styleClass="black_ar" size="3"  maxlength="10"
										styleId="<%=quantity%>" property="<%=quantity%>"
										value="<%=qtyValue%>" style="text-align:right"/>
									<span id="<%=unit%>">
										<%=strHiddenUnitValue%>
									</span>

								</td>
								<td class="black_ar">
										<html:text styleClass="black_ar" size="3"  maxlength="10"
											styleId="<%=concentration%>" property="<%=concentration%>"
											disabled="<%=concReadOnly%>" value="<%=concValue%>" style="text-align:right"/>
								&nbsp;<bean:message key="specimen.concentrationUnit" />

								</td>
								
								<td class="black_ar" >
								
								<html:text styleClass="black_ar" maxlength="255" property="<%=requirementLabel%>" styleId="<%=requirementLabel%>"  size="20"/>				


								</td>
<%
								if(Variables.isTemplateBasedLblGeneratorAvl)
								{
							%>
								<td class="black_ar" >

								<html:text styleClass="black_ar" maxlength="255" property="<%=labelFormat%>" styleId="<%=labelFormat%>"  size="20"/>

								</td>
								<%}%>
                              </tr>


			<%
				}
			%>
							</tbody>
							<%
							String deleteSpecimenRequirements = "deleteChecked('DeriveSpecimenBean','CreateSpecimenTemplate.do?operation="+operation+"&pageOf=delete',document.forms[0].noOfDeriveSpecimen,'checkBox_',false)";
			%>
							<tr>
                                <td colspan="6">
									<html:button property="addSpecimenReq" styleClass="black_ar" value="Add More" onclick="insRow('DeriveSpecimenBean')"/>

									<html:button property="deleteSpecimenReq" styleClass="black_ar" onclick="<%=deleteSpecimenRequirements %>" ><bean:message key="buttons.delete"/>
									</html:button>
								</td>
                              </tr>
                          </table>
						</div>
					</td>
                  </tr>
                   <tr>
                      <td colspan="2" class="bottomtd"></td>
                    </tr>
                  <tr onclick="javascript:showHide('aliquot')">
                     <td align="left" class="tr_bg_blue1"><span class="blue_ar_b"><bean:message key="cpbasedentry.aliquots"/></span></td>
                    <td align="right" class="tr_bg_blue1">
						<a href="#" id="imgArrow_aliquot">
							<img src="images/uIEnhancementImages/dwn_arrow1.gif" alt="Show Details" border="0" width="80" height="9" hspace="10" vspace="0"/>
						</a>
					</td>
                 </tr>
                 <tr>
                    <td colspan="2" align="left" class="showhide1">
						<div id="aliquot" style="display:none" >
							<table width="100%" border="0" cellspacing="0" cellpadding="4">
								<tr>
									<td width="15%" class="black_ar" >
									<bean:message key="aliquots.noOfAliquots"/>
									</td>
									<td width="20%" class="black_ar">
									<bean:message key="aliquots.qtyPerAliquot"/>
									</td>
									<td width="30%" class="black_ar">
									<bean:message key="cpbasedentry.storagelocation"/>
									</td>
<%
								if(Variables.isTemplateBasedLblGeneratorAvl)
								{
							%>
									<td width="35%" align="left" class="black_ar" >Label Format</td>
									<%}%>
								</tr>
								<tr>
		                           <td class="black_ar" >


										 <html:text styleClass="black_ar" styleId="noOfAliquots" size="8" property="noOfAliquots" style="text-align:right" maxlength="50" />
									</td>
		                            <td  class="black_ar">

										 <html:text styleClass="black_ar" styleId="quantityPerAliquot" size="8" property="quantityPerAliquot" style="text-align:right" maxlength="50" />
									</td>
		                            <td class="black_ar">

										<autocomplete:AutoCompleteTag property="storageLocationForAliquotSpecimen"
											    optionsList = "${requestScope.storageContainerList}"
												initialValue="<%=form.getStorageLocationForAliquotSpecimen()%>"
												styleClass="black_ar"
												size="20"
												/>
									</td>
<%
								if(Variables.isTemplateBasedLblGeneratorAvl)
								{
							%>
									<td align="left" class="black_ar" >

								<html:text styleClass="black_ar" maxlength="255" property="labelFormatForAliquot" styleId="labelFormatForAliquot"  size="25"/>


								</td>
								<%}%>
								</tr>
                          </table>
						</div>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="2" class="bttomtd"></td>
                    </tr>
                    <tr>
             			   <td colspan="2" class="buttonbg">
								<html:button styleClass="blue_ar_b" property="submitPage" onclick="saveSpecimens()"	>
								<bean:message key="cpbasedentry.savespecimenrequirements"/>
								</html:button>
								<logic:equal name="isPersistent" value="true">									
									<% if("New".equals(form.getLineage()))
										    {
										%>	
											&nbsp;|
											<html:button styleClass="blue_ar_b" property="submitPage" onclick="createDuplicateSpecimens()">
													<bean:message key="cpbasedentry.createduplicatespecimen"/>
											</html:button>
									<% } else	{%>
									&nbsp;
									<% } %>
									&nbsp;|
									<html:button styleClass="blue_ar_b" property="submitPage" onclick="deleteEvent()">
											<bean:message key="buttons.delete"/>
									</html:button>	
								</logic:equal>
								<logic:notEqual name="isPersistent" value="true">
									<logic:equal name="operation" value="edit">
										<% if("New".equals(form.getLineage()))
										    {
										%>	
											&nbsp;|
											<html:button styleClass="blue_ar_b" property="submitPage" onclick="createDuplicateSpecimens()">
													<bean:message key="cpbasedentry.createduplicatespecimen"/>
											</html:button>
										<% } 	%>
									&nbsp;|
									<html:button styleClass="blue_ar_b" property="submitPage" onclick="deleteEvent()">
											<bean:message key="buttons.delete"/>
									</html:button>
									</logic:equal>
									<logic:notEqual name="operation" value="edit">
										&nbsp;
									</logic:notEqual>
								</logic:notEqual>
							</td>

                    </tr>
             </table>
		</td>
	</tr>