<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="edu.wustl.catissuecore.util.global.Constants,edu.wustl.catissuecore.actionForm.SpecimenArrayTypeForm"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="edu.wustl.common.beans.NameValueBean"%>
<%@ page import="java.util.HashMap"%>

<%
        String operation = (String) request.getAttribute(Constants.OPERATION);
		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);
        String formName;
//		String submittedFor=(String)request.getAttribute(Constants.SUBMITTED_FOR);

//        boolean readOnlyValue;
        if (operation.equals(Constants.EDIT))
        {
            formName = Constants.SPECIMENARRAYTYPE_EDIT_ACTION;
//            readOnlyValue = false;
        }
        else
        {
            formName = Constants.SPECIMENARRAYTYPE_ADD_ACTION;
//            readOnlyValue = false;
        }
        %>
<html:messages id="messageKey" message="true" header="messages.header" footer="messages.footer">
	<%=messageKey%>
</html:messages>
<html:errors/>
<script type="text/javascript">

<%
	List specimenClassList = (List) request.getAttribute(Constants.SPECIMEN_CLASS_LIST);
	HashMap specimenTypeMap = (HashMap) request.getAttribute(Constants.SPECIMEN_TYPE_MAP);
	SpecimenArrayTypeForm form = (SpecimenArrayTypeForm)request.getAttribute("arrayTypeForm");
//	String operation = (String)request.getAttribute(Constants.OPERATION);
	System.out.println(" Form ::  " + form);
%>

<%
	int classCount=0;
	for(classCount=1;classCount<specimenClassList.size();classCount++)
	{
		String keyObj = (String)((NameValueBean)specimenClassList.get(classCount)).getName() ;
		List subList = (List)specimenTypeMap.get(keyObj);
		String arrayData = "";
		for(int listSize=0;listSize<subList.size();listSize++ )
		{
			if(listSize == subList.size()-1 )
				arrayData = arrayData + "\"" + ((NameValueBean)subList.get(listSize)).getName() + "\"";
			else
    			arrayData = arrayData + "\"" + ((NameValueBean)subList.get(listSize)).getName() + "\",";   
		}
%>
		var <%=keyObj%>Array = new Array(<%=arrayData%>);
<%	    		
	}
%>

		function typeChange(specArrayTypeArr)
		{ 
			var specimenTypeCombo = "type";
			ele = document.getElementById(specimenTypeCombo);
			//To Clear the Combo Box
			ele.options.length = 0;

		    if (specArrayTypeArr != null) {
				specArrayTypeArr.sort();
				//ele.options[0] = new Option('-- Select --','-1');
				var j=0;
				//Populating the corresponding Combo Box
				for(i=0;i<specArrayTypeArr.length;i++)
				{
						ele.options[j++] = new Option(specArrayTypeArr[i],specArrayTypeArr[i]);
				}
		    } else {
				ele.options[0] = new Option('-- Select --','-1');
			}
		}
			
		function onClassChange(element)
		{
			
			if(element.value == "Tissue")
			{
				typeChange(TissueArray);
			}
			else if(element.value == "Fluid")
			{
				typeChange(FluidArray);
			}
			else if(element.value == "Cell")
			{
				typeChange(CellArray);
			}
			else if(element.value == "Molecular")
			{
				typeChange(MolecularArray);
			} else {
				typeChange(null);	
			}
		}
		
		
</script>

<html:form action="<%=formName%>">
	<html:hidden property="operation" value="<%=operation%>" />
	<html:hidden property="id" />
	<html:hidden property="submittedFor" value="<%=submittedFor%>"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="newMaintable">
  <tr>
    <td class="td_color_bfdcf3"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="whitetable_bg">

      <tr>
        <td width="100%" colspan="2" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="3" valign="top" class="td_color_bfdcf3"><table width="20%" border="0" cellpadding="0" cellspacing="0" background="images/uIEnhancementImages/table_title_bg.gif">
                  <tr>
                    <td width="82%"><span class="wh_ar_b">&nbsp;&nbsp;&nbsp;<bean:message key="SpecimenArrayType.header" /> </span></td>
                    <td width="18%" align="right"><img src="images/uIEnhancementImages/table_title_corner2.gif" width="31" height="24" /></td>
                  </tr>

						  </table>
						 </td>
		            </tr>
					<tr>
		              <td width="1%" valign="top" class="td_color_bfdcf3">&nbsp;
					  </td>
		              <td width="9%" valign="top" class="td_tab_bg">&nbsp;
					  </td>
		              <td width="90%" valign="bottom" class="td_color_bfdcf3" style="padding-top:4px;">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
		                  <tr>
				            <td width="4%" class="td_tab_bg" >&nbsp;
							</td>
					<!-- for tabs selection -->
					<%
						if(operation.equals(Constants.ADD))
						{ 
					%>
		                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><img src="images/uIEnhancementImages/tab_add_user.jpg" alt="Add" width="57" height="22" /></td>

                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><html:link page="/SimpleQueryInterface.do?pageOf=pageOfSpecimenArrayType&amp;aliasName=SpecimenArrayType&amp;menuSelected=21"><img src="images/uIEnhancementImages/tab_edit_user.jpg" alt="Edit" width="59" height="22" border="0" /></html:link></td>
                    <td valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>
					<% 
						}
						   if(operation.equals(Constants.EDIT))
						{
					%>
		                   <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif" ><html:link page="/SpecimenArrayType.do?operation=add&amp;pageOf=pageOfSpecimenArrayType&amp;menuSelected=21"><img src="images/uIEnhancementImages/tab_add_user1.jpg" alt="Add" width="57" height="22" border="0" /> </html:link></td>

                    <td width="6%" valign="bottom" background="images/uIEnhancementImages/tab_bg.gif"><img src="images/uIEnhancementImages/tab_edit_user1.jpg" alt="Edit" width="59" height="22" /></td>
                    <td valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">&nbsp;</td>
						<%
							}
						%>
		                    <td valign="bottom" background="images/uIEnhancementImages/tab_bg.gif">&nbsp;
							</td>
		                    <td width="1%" align="left" valign="bottom" class="td_color_bfdcf3" >&nbsp;
							</td>
		                  </tr>
				      </table>
					</td>
	            </tr>
		    </table>
		</td>
     </tr>
     <tr>
        <td colspan="2" class="td_color_bfdcf3" style="padding-left:10px; padding-right:10px; padding-bottom:10px;">
			<table width="100%" border="0" cellpadding="3" cellspacing="0" bgcolor="#FFFFFF">
				<tr>
	              <td colspan="3" align="left">
					<span class=" grey_ar_s">
						<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />		<bean:message key="commonRequiredField.message" />
					</span>
				  </td>
	            </tr>
				
		        <tr>
				    <td align="left" class="tr_bg_blue1">
						<span class="blue_ar_b">
			<logic:equal name="operation" value="<%=Constants.ADD%>">
							<bean:message key="arrayType.title"/>
			</logic:equal>
			<logic:equal name="operation" value="<%=Constants.EDIT%>">
							<bean:message key="arrayType.editTitle"/>
			</logic:equal>
						</span>
					</td>
	                <td align="right" class="tr_bg_blue1">&nbsp;
					</td>
	            </tr>
		        <tr>
			      <td colspan="3" align="left" style="padding-top:10px; padding-bottom:15px;">
					<div id="part_det" >
	                  <table width="100%" border="0" cellpadding="3" cellspacing="0">
                         <tr>
		                    <td width="1%" align="left" class="black_ar">
								<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</span>
							</td>
	                        <td width="16%" align="left" class="black_ar">
								<label for="name">
									<bean:message key="arrayType.name" />
								</label>
							</td>
							<td width="82%" align="left">
								<label>
		                          <html:text styleClass="black_ar"  maxlength="255"  size="30" styleId="name" property="name"/>
				                </label>
							</td>
	                      </tr>
		                  <tr>
			                <td align="left" class="black_ar">
								<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</span>
							</td>
	                        <td align="left" class="black_ar">
								<label for="specimenClass">
									<bean:message key="arrayType.specimenClass" />
								</label>
							</td>
	                        <td align="left" class="black_new">
								<html:select property="specimenClass" styleClass="formFieldSizedNew" styleId="className" size="1" onchange="onClassChange(this)">
									<%
										String classValue = form.getSpecimenClass();
										if((operation != null) && (operation.equals(Constants.EDIT)))
										{
									%>
										<html:option value="<%=classValue%>"><%=classValue%></html:option>
									<%
										}else{
									%>
										<html:options collection="<%=Constants.SPECIMEN_CLASS_LIST%>" labelProperty="name" property="value"/>
									<%
										}
									%>
								</html:select>
						    </td>
						</tr>
							<%
								String classValue = (String)form.getSpecimenClass();
								List specimenTypeList = (List)specimenTypeMap.get(classValue);
								
								if(specimenTypeList == null)
								{
									specimenTypeList = new ArrayList();
									specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
								}
								pageContext.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
							%>
						<tr>
							<td align="left" class="black_ar">
								<span class="blue_ar_b">
									<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
								</span>
							</td>
	                        <td align="left" class="black_ar">
								<label for="specimenType">
									<bean:message key="arrayType.specimenType" />
								</label>
							</td>
							<td align="left" class="black_new">
								<html:select property="specimenTypes" styleClass="formFieldSizedNew" styleId="type" size="4" multiple="true">
									<html:options collection="<%=Constants.SPECIMEN_TYPE_LIST%>" labelProperty="name" property="value"/>
								</html:select>
							</td>
	                      </tr>
		                  <tr>
			                <td align="left" class="black_ar">&nbsp;</td>
				            <td align="left" class="black_ar">
								<label for="comments">
									<bean:message key="arrayType.comments"/>
								</label>
							</td>
	                        <td align="left">
								<html:textarea styleClass="black_ar_s" cols="35" rows="4" styleId="comment" property="comment"/>
							</td>
						</tr>
                  </table>
              </div></td>
            </tr>

            <tr onclick="javascript:showHide('add_id')">
              <tr onclick="javascript:showHide('add_id')">
										<td align="left" class="tr_bg_blue1"><span
										class="blue_ar_b"><bean:message
										key="arrayType.capacity" /></span>
									</td>
										<td align="right" class="tr_bg_blue1"><a href="#"><img
											src="images/uIEnhancementImages/dwn_arrow1.gif" width="7" height="8" hspace="10"
											border="0" class="tr_bg_blue1" /></a>
										</td>
									</tr>
            <tr>
              <td colspan="3" valign="top" style="padding-top:10px;">
				<div id="add_id" style="display:none" >
                    <table width="100%" border="0" cellpadding="2" cellspacing="0">
                      <tr>
                        <td width="1%" height="25" align="left" class="black_ar">
							<span class="blue_ar_b">
								<img src="images/uIEnhancementImages/star.gif" alt="Mandatory" width="6" height="6" hspace="0" vspace="0" />
							</span>
						</td>
                        <td width="16%" align="left" class="black_ar">
							<label for="oneDimensionCapacity">
								<bean:message key="arrayType.oneDimensionCapacity" />
							</label>
						</td>
                        <td width="83%" colspan="4" align="left" valign="top">
							<html:text styleClass="black_ar" maxlength="10"  size="30" styleId="oneDimensionCapacity" property="oneDimensionCapacity"/>
						</td>
                      </tr>
                      <tr>
                        <td height="25" align="left" class="black_ar">&nbsp;
						</td>
                        <td align="left" class="black_ar">
							<label for="twoDimensionCapacity">
								<bean:message key="arrayType.twoDimensionCapacity" />
							</label>
						</td>
                        <td colspan="4" align="left" valign="top">
							<label>
	                          <html:text styleClass="black_ar" maxlength="10"  size="30" styleId="twoDimensionCapacity" property="twoDimensionCapacity"/>
		                    </label>
						</td>
                      </tr>
                    </table>
                 </td>
            </tr>
            <tr class="td_color_F7F7F7">
              <td colspan="3">&nbsp;
			  </td>
            </tr>
            <tr  class="td_color_F7F7F7">
		<!-- action buttons begins -->
              <td colspan="3" class="buttonbg">
					<html:submit styleClass="blue_ar_b">
						<bean:message  key="buttons.submit" />
					</html:submit>
			        &nbsp;| 
					<span class="cancellink">
						<html:link page="/ManageAdministrativeData.do" styleClass="blue_ar_s_b">
						  <bean:message key="buttons.cancel" />
						</html:link>
					</span>
				</td>
		<!-- actio buttons ends -->
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
</html:form>