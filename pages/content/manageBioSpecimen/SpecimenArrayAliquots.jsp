<!-- 
	This JSP page is to create SpecimenArrayAliquots from/of Parent Specimen Array.
-->

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%@ page import="edu.wustl.catissuecore.actionForm.SpecimenArrayAliquotForm"%>
<%@ page import="edu.wustl.common.util.tag.ScriptGenerator" %>
<%@ page import="java.util.*"%>

<head>
	<script src="jss/Hashtable.js" type="text/javascript"></script>
	<script language="JavaScript" type="text/javascript" src="jss/CustomListBox.js"></script>
	<script language="JavaScript" type="text/javascript" src="jss/javaScript.js"></script>
	<script language="JavaScript">
		function onSubmit()
		{
			var action = '<%=Constants.SPECIMEN_ARRAY_CREATE_ALIQUOT_ACTION%>';
			document.forms[0].action = action + "?pageOf=" + '<%=Constants.PAGEOF_SPECIMEN_ARRAY_CREATE_ALIQUOT%>' + "&buttonClicked=submit&menuSelected=20";
			document.forms[0].submit();
		}
		
		function onRadioButtonClick(element)
		{
			if(element.value == 1)
			{
				document.forms[0].parentSpecimenArrayLabel.disabled = false;
				document.forms[0].barcode.disabled = true;
			}
			else
			{
				document.forms[0].barcode.disabled = false;
				document.forms[0].parentSpecimenArrayLabel.disabled = true;
			}
		}
		
		function onCreate()
		{
			var action = '<%=Constants.SPECIMEN_ARRAY_CREATE_ALIQUOT_ACTION%>';
			document.forms[0].submittedFor.value = "ForwardTo";
			document.forms[0].action = action + "?pageOf=" + '<%=Constants.PAGEOF_SPECIMEN_ARRAY_CREATE_ALIQUOT%>' + "&operation=add&menuSelected=20&buttonClicked=create";
		    document.forms[0].submit();
		}


		function onArrayAliquotStorageChange(element)
		{
			var index1 =  element.name.lastIndexOf('_');
			var index2 =  element.name.lastIndexOf(')');
			//rowNumber of the element
			var rowNo = (element.name).substring(index1+1,index2);
			var autoDiv = document.getElementById("auto_"+rowNo);
			var manualDiv = document.getElementById("manual_"+rowNo);
			if(element.value == 1)
			{
				manualDiv.style.display='none';
				autoDiv.style.display = 'block';
			}
			else
			{
				autoDiv.style.display = 'none';
				manualDiv.style.display = 'block';
			}
		}

function mapButtonClickedInAliquot(frameUrl,count)
	{
	   	var storageContainer = document.getElementById("container_" + count + "_0").value;
		frameUrl+="&storageContainerName="+storageContainer;
		//Patch ID: Bug#4116_4
		openPopupWindow(frameUrl,'aliquotPage');
    }	
		
	</script>
</head>
<%
	String pageOf = (String)request.getAttribute(Constants.PAGEOF);
	String buttonKey = "";
	String exceedsMaxLimit = (String)request.getAttribute(Constants.EXCEEDS_MAX_LIMIT);
	if(Constants.PAGEOF_SPECIMEN_ARRAY_ALIQUOT.equals(pageOf))
	{
		buttonKey = "buttons.submit";
	}
	else if(Constants.PAGEOF_SPECIMEN_ARRAY_CREATE_ALIQUOT.equals(pageOf))
	{
		buttonKey = "buttons.resubmit";
	}
	System.out.println("pageOf---"+pageOf);
%>


<html:form action="<%=Constants.SPECIMEN_ARRAY_ALIQUOT_ACTION%>">
<html:hidden property="id"/>					
					<html:hidden property="submittedFor"/>
					<html:hidden property="specimenArrayId"/>
<!----------New Code Begins------------->
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
  <tr>
    <td class="td_color_bfdcf3">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="td_table_head">
					<span class="wh_ar_b">
						<bean:message key="specimenArrayAliquots.title" />
					</span>
				</td>
		        <td>
				<img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Specimen Array Aliquot" width="31" height="24" />
				</td>
			</tr>
		</table>
	</td>
  </tr>
  <tr>
    <td class="tablepadding">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="td_tab_bg"><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50"  height="1"></td>
		<td valign="bottom"><html:link page="/SpecimenArray.do?operation=add&amp;pageOf=pageOfSpecimenArray"><img src="images/uIEnhancementImages/tab_add_notSelected.jpg" alt="Add Specimen Array" width="57" height="22" /></html:link></td>
		<td valign="bottom"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfSpecimenArray&aliasName=SpecimenArray"><img src="images/uIEnhancementImages/tab_edit_notSelected.jpg" alt="Edit Specimen Array" width="59" height="22" border="0" /></html:link></td>
			<td valign="bottom"><img src="images/uIEnhancementImages/tab_aliquot.gif" alt="Aliquot Specimen Array" width="66" height="22" /></td>
				<td width="90%" valign="bottom" class="td_tab_bg">&nbsp;</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
			<tr>
				<td align="left" class="bottomtd"><%@ include file="/pages/content/common/ActionErrors.jsp" %></td>
		    </tr>
			<tr>
				<td width="61%" align="left" class="tr_bg_blue1">
					<span class="blue_ar_b">&nbsp;
						<bean:message key="specimenArrayAliquots.createTitle" />
					</span>
				</td>
	        </tr>
		    <tr>
			  <td align="left" class="showhide">
				<table width="100%" border="0" cellpadding="3" cellspacing="0">
					<tr>
		              <td width="1%" align="center" class="black_ar">
						<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
					   </td>
		               <td width="17%" align="left" class="black_ar">
						<bean:message key="specimenArrayAliquots.parent" />
					   </td>
		              <td colspan="5" align="left" >
						<table width="55%" border="0" cellspacing="0" cellpadding="0" >
			                <tr class="groupElements">
						      <td valign="middle" nowrap="nowrap">
								<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="1" onclick="onRadioButtonClick(this)">	&nbsp;						
								</html:radio>
								<span class="black_ar">
									Label&nbsp;
								</span>
				<logic:equal name="specimenArrayAliquotForm" property="checkedButton" value="1">
								<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="parentSpecimenArrayLabel" property="parentSpecimenArrayLabel" disabled="false"/>
				</logic:equal>
						
				<logic:equal name="specimenArrayAliquotForm" property="checkedButton" value="2">
								<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="parentSpecimenArrayLabel" property="parentSpecimenArrayLabel" disabled="true"/>
				</logic:equal>
								&nbsp;&nbsp; 
								</span>
							</td>
							<td valign="middle" nowrap="nowrap">
								<html:radio styleClass="" styleId="checkedButton" property="checkedButton" value="2" onclick="onRadioButtonClick(this)">							
								</html:radio>
								<span class="black_ar">
									 Barcode&nbsp;
								</span>
				 <logic:equal name="specimenArrayAliquotForm" property="checkedButton" value="1">
								<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="barcode" property="barcode" disabled="true"/>
				</logic:equal>
						
				<logic:equal name="specimenArrayAliquotForm" property="checkedButton" value="2">
								<html:text styleClass="black_ar"  maxlength="50"  size="20" styleId="barcode" property="barcode"/>
				</logic:equal>
								</span>
							</td>
		                </tr>
			      </table>
				</td>
              </tr>
			  <tr>
				<td align="center" class="black_ar">
					<span class="blue_ar_b">
						<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
					</span>
				</td>
				<td align="left" class="black_ar">
					<label for="AliquotCount">
						<bean:message key="specimenArrayAliquots.noOfAliquots"/>
					</label>
				</td>
				<td width="19%" align="left" nowrap>
					<html:text styleClass="black_ar"  maxlength="50"  size="15" styleId="aliquotCount" property="aliquotCount" style="text-align:right"/>
				</td>
				<td width="10%" align="center" class="black_ar">&nbsp;</td>
				<td width="1%" align="center" class="black_ar">&nbsp;</td>
				<td width="17%" align="left" class="black_ar">&nbsp;</td>
				<td width="35%" align="left">&nbsp;</td>
              </tr>

			  <tr>
				<td align="center" class="black_ar">&nbsp;</td>
				<td align="left" >
					<html:button styleClass="blue_ar_b" property="submitPage" onclick="onSubmit()">
						<bean:message key="<%=buttonKey%>"/>
					</html:button>
				</td>
				<td align="left">&nbsp;</td>
				<td align="center" class="black_ar">&nbsp;</td>
				<td align="center" class="black_ar">&nbsp;</td>
				<td align="left" class="black_ar">&nbsp;</td>
				<td align="left" valign="top">&nbsp;</td>
            </tr>
		</table>
	</td>
   </tr>
		<%	
	SpecimenArrayAliquotForm form = (SpecimenArrayAliquotForm)request.getAttribute("specimenArrayAliquotForm");
	if(!Constants.PAGEOF_SPECIMEN_ARRAY_ALIQUOT.equals(pageOf))
	{
	%>
	<tr>
        <td width="61%" align="left" class="tr_bg_blue1">
			<span class="blue_ar_b">&nbsp;
				<bean:message key="specimenArrayAliquots.title"/>
			</span>
		</td>
    </tr>
	<tr>
       <td align="left" class="showhide1">
			<table width="100%" border="0" cellpadding="3" cellspacing="0">
              <tr>
                <td width="1%" align="center" class="black_ar">
					<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				</td>
                <td width="17%" align="left" class="black_ar">
					<label for="specimenArrayType">
						<bean:message key="specimenArrayAliquots.specimenArrayType"/> 
					</label>
				</td>
				<td width="82%" align="left" nowrap>
					<html:text styleClass="black_ar"  maxlength="50"  size="25" styleId="specimenArrayType" property="specimenArrayType" readonly="true"/>
				</td>
              </tr>
              <tr>
                <td align="center" class="black_ar">
					<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				</td>
                <td align="left" class="black_ar">
					<label for="specimenClass">
						<bean:message key="specimenArrayAliquots.specimenClass"/> 
					</label>
				</td>
                <td align="left">
					<html:text styleClass="black_ar"  maxlength="50"  size="25" styleId="specimenClass" property="specimenClass" readonly="true"/>
				</td>
			</tr>
            <tr>
                <td align="center" class="black_ar">
					<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
				</td>
                <td align="left" class="black_ar">
					<label for="specimenType">
						<bean:message key="specimenArrayAliquots.specimenType"/> 
					</label>
				</td>
                <td align="left" class="black_new">
					<html:select property="specimenTypes" styleClass="formFieldSized11" styleId="state" size="4" multiple="true" disabled="true">
							<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
					</html:select>
				</td>
             </tr>
         </table>
	  </td>
    </tr>
	<tr>
       <td class="showhide1">
		  <table width="100%" border="0" cellspacing="0" cellpadding="4">
            <tr class="tableheading">
              <td width="2%" align="left" class="black_ar_b">
				#
			  </td>
              <td width="16%" align="left" nowrap="nowrap" class="black_ar_b">
				<span class=" grey_ar_s">
					<img src="images/uIEnhancementImages/star.gif" alt="Mandatory Field" width="6" height="6" hspace="0" vspace="0"/>
				</span>&nbsp;
				<bean:message key="specimenArrayAliquots.label"/>
			  </td>
              <td width="16%" class="black_ar_b">
				<bean:message key="specimenArrayAliquots.barcode"/>
			  </td>
              <td width="66%" class="black_ar_b">
				<bean:message key="specimenArrayAliquots.location"/>
			  </td>
            </tr>
           
			<%=ScriptGenerator.getJSForOutermostDataTable()%>
	
				<%
					Map aliquotMap = new HashMap();
					int counter=0;

					if(form != null)
					{
						counter = Integer.parseInt(form.getAliquotCount());
						aliquotMap = form.getSpecimenArrayAliquotMap();
					}

					/* Retrieving a map of available containers */
					Map dataMap = (Map) request.getAttribute(Constants.AVAILABLE_CONTAINER_MAP);
					String[] labelNames = Constants.STORAGE_CONTAINER_LABEL;

					for(int i=1;i<=counter;i++)
					{
						String labelKey = "value(SpecimenArray:" + i + "_label)";						
						String barKey = "value(SpecimenArray:" + i + "_barcode)";						
						
						String containerKey = "value(SpecimenArray:" + i + "_StorageContainer_id)";
						String pos1Key = "value(SpecimenArray:" + i + "_positionDimensionOne)";
						String pos2Key = "value(SpecimenArray:" + i + "_positionDimensionTwo)";						
						
						//Preparing data for custom tag
						String[] attrNames = {containerKey, pos1Key, pos2Key};
						String[] tdStyleClassArray = { "formFieldSized15", "customFormField", "customFormField"}; 

						String[] initValues = new String[3];
						initValues[0] = (String)aliquotMap.get("SpecimenArray:" + i + "_StorageContainer_id");
						initValues[1] = (String)aliquotMap.get("SpecimenArray:" + i + "_positionDimensionOne");
						initValues[2] = (String)aliquotMap.get("SpecimenArray:" + i + "_positionDimensionTwo");

						String rowNumber = String.valueOf(i);
						String noOfEmptyCombos = "3";
						String styClass = "formFieldSized5";
						String tdStyleClass = "customFormField";
						String onChange = "onCustomListBoxChange(this)";

						String containerStyleId = "customListBox_" + rowNumber + "_0";
						String pos1StyleId = "customListBox_" + rowNumber + "_1";
						String pos2StyleId = "customListBox_" + rowNumber + "_2";
						
							//Keys for container if selected from Map
							String containerMap = "value(mapButton_" + i + ")";
		                    String containerMapStyle = "mapButton_" + i ;
							String containerIdFromMapKey = "value(SpecimenArray:" + i + "_StorageContainer_id_fromMap)";
							String containerNameFromMapKey = "value(SpecimenArray:" + i + "_StorageContainer_name_fromMap)";
							String pos1FromMapKey = "value(SpecimenArray:" + i + "_positionDimensionOne_fromMap)";
							String pos2FromMapKey = "value(SpecimenArray:" + i + "_positionDimensionTwo_fromMap)";
							String stContSelection = "value(radio_" + i + ")";
							String containerStyle = "container_" + i + "_0";
							String containerIdStyle = "containerId_" + i + "_0";
							String pos1Style = "pos1_" + i + "_1";
							String pos2Style = "pos2_" + i + "_2";
							String rbKey = "radio_" + i ;
						   
						   if(aliquotMap.get(rbKey)==null)
						   {
            			    aliquotMap.put(rbKey,"1");
						   }
						   
					 	int radioSelected = Integer.parseInt(aliquotMap.get(rbKey).toString());
					    boolean dropDownDisable = false;
					    boolean textBoxDisable = false;
						String autoDivDisplayStyle=null;
						String manualDivDisplayStyle=null;

						if(radioSelected == 1)
						{	
							autoDivDisplayStyle="display:block";
							manualDivDisplayStyle="display:none";
						}
						else if(radioSelected == 2)
						{
							autoDivDisplayStyle="display:none";
							manualDivDisplayStyle="display:block";
						}		
						   
						   
						   
							String arrayTypeId = "" + request.getAttribute(Constants.STORAGE_TYPE_ID);
				         
						 String frameUrl = "ShowFramedPage.do?pageOf=pageOfAliquot&amp;containerStyleId=" + containerIdStyle + "&amp;xDimStyleId=" + pos1Style + "&amp;yDimStyleId=" + pos2Style
			            + "&amp;containerStyle=" + containerStyle 						
			            + "&amp;" + Constants.CAN_HOLD_SPECIMEN_ARRAY_TYPE+"="+arrayTypeId;
			           /* + "&amp;" + Constants.CAN_HOLD_COLLECTION_PROTOCOL +"=" + collectionProtocolId ;*/
						
		    	System.out.println("##########################frameUrl:"+frameUrl);				
	     	  String buttonOnClicked = "mapButtonClickedInAliquot('"+frameUrl+"','"+i+"')";		
				
  	     	//	String buttonOnClicked = "javascript:NewWindow('ShowFramedPage.do?pageOf=pageOfSpecimenArray&amp;containerStyleId=" + containerIdStyle + "&amp;xDimStyleId=" + pos1Style + "&amp;containerStyle=" + containerStyle + "&amp;yDimStyleId=" + pos2Style + "','name','800','600','no');return false";
				%>
				<%=ScriptGenerator.getJSEquivalentFor(dataMap,rowNumber)%>
			<tr>
				<td align="left" class="black_ar" >
					<%=i%>.
				</td>
				<td class="black_ar">
					<html:text styleClass="black_ar"  maxlength="50"  size="25" styleId="label" property="<%=labelKey%>" disabled="false"/>
				</td>
				<td class="black_ar">
					<html:text styleClass="black_ar"  maxlength="50"  size="25" styleId="barcodes" property="<%=barKey%>" disabled="false"/>
				</td>
				<td class="black_ar">
					<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td width="20%">
								<html:select property="<%=stContSelection%>" styleClass="black_new"
									styleId="<%=stContSelection%>" size="1"	onmouseover="showTip(this.id)"
									onmouseout="hideTip(this.id)" onchange="onArrayAliquotStorageChange(this)">
									<html:options collection="storagePositionListForSpecimenArrayAliquot"
										labelProperty="name" property="value" />
								</html:select> 
								<html:hidden styleId="<%=containerIdStyle%>" property="<%=containerIdFromMapKey%>"/>
							</td>
							<td width="80%" >
								<div Style="<%=autoDivDisplayStyle%>" id="auto_<%=i%>" >
									<ncombo:nlevelcombo dataMap="<%=dataMap%>" 
										attributeNames="<%=attrNames%>" 
										initialValues="<%=initValues%>"  
										styleClass = "black_new" 
										tdStyleClass = "black_new" 
										tdStyleClassArray="<%=tdStyleClassArray%>"
										labelNames="<%=labelNames%>" 
										rowNumber="<%=rowNumber%>" 
										onChange = "<%=onChange%>"
										formLabelStyle="nComboGroup"
										disabled = "false"
										noOfEmptyCombos = "<%=noOfEmptyCombos%>"/>
								    	</tr>
										</table>
								</div>
								<div style="<%=manualDivDisplayStyle%>" id="manual_<%=i%>"  >
									<table cellpadding="0" cellspacing="0" border="0" >
										<tr>
											<td class="groupelements">
												<html:text styleClass="black_ar"  size="20" styleId="<%=containerStyle%>" property="<%=containerNameFromMapKey%>" />
											</td>
											<td class="groupelements">
												<html:text styleClass="black_ar"  size="2" styleId="<%=pos1Style%>" property="<%=pos1FromMapKey%>" />
											</td>
											<td class="groupelements">
												<html:text styleClass="black_ar"  size="2" styleId="<%=pos2Style%>" property="<%=pos2FromMapKey%>" />
											</td>
											<td class="groupelements">
												<html:button styleClass="black_ar" styleId = "<%=containerMapStyle%>" property="<%=containerMap%>" onclick="<%=buttonOnClicked%>">
													<bean:message key="buttons.map"/>
												</html:button>
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		<logic:equal name="exceedsMaxLimit" value="true">
			<tr>
				<td colspan="4" class="black_ar">
					<bean:message key="container.maxView"/>
				</td>
			</tr>
		</logic:equal>
				<%
					} //For
				%>	

<!----------New Code Ends--------------->

					
		</table>
	</td>
   </tr>
   <tr>
       <td class="bottomtd"></td>
   </tr>
	<tr>
		<td colspan="2" class="buttonbg">
				<html:button styleClass="blue_ar_b" property="submitButton" onclick="onCreate()" accesskey="Enter">
						<bean:message key="buttons.create"/>
				</html:button>
				| <html:link page="/ManageAdministrativeData.do" styleClass="cancellink">
					<bean:message key="buttons.cancel" />
				  </html:link>
		</td>				  
	</tr>				
</table>	
</td>
</tr>	
	<%	
	}
	%>	
</table>
</html:form>
