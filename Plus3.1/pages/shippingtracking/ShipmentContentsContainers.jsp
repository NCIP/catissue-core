<style>
	#addMoreContainer a:link {
	text-decoration: none;
		border-bottom: #ffffff;
		border-top: #ffffff;
		border-left: #ffffff;
		border-right: #ffffff;
}
#addMoreContainer a:visited {
	text-decoration: #ffffff;
		border-bottom: #ffffff;
		border-top: #ffffff;
		border-left: #ffffff;
		border-right: #ffffff;
}
#addMoreContainer a:hover {
	text-decoration: #ffffff;
		border-bottom: #ffffff;
		border-top: #ffffff;
		border-left: #ffffff;
		border-right: #ffffff;
}
#addMoreContainer a:active {
	text-decoration: #ffffff;
		border-bottom: #ffffff;
		border-top: #ffffff;
		border-left: #ffffff;
		border-right: #ffffff;
}
</style>
<script language="JavaScript" type="text/javascript" src="jss/shippingtracking/addMoreScript.js"></script>
<script>
	function addMoreContainerDetails()
	{
		var specimenCounter=parseInt(document.getElementById("containerCounter").value);
		var tbodyObject=document.getElementById("containerRowsContainer");
		var rows=tbodyObject.rows;
		var previousRowCount=rows.length;
		var newRow=tbodyObject.insertRow(rows.length);
		var name="",id="";
		var thisRowNum=previousRowCount+1;
		var cellInnerHTML="";
		
		var checkBoxCell=newRow.insertCell(0);
		name="containerDetails(containerCheckbox_"+thisRowNum+")";
		id="containerCheckbox_"+thisRowNum;
		cellInnerHTML="<input type='checkbox' name='"+name+"' id='"+id+"'>";
		checkBoxCell.innerHTML=cellInnerHTML;
		
		var checkBoxCell=newRow.insertCell(1);
		name="containerDetails(containerLabel_"+thisRowNum+")";
		id="containerLabel_"+thisRowNum;
		var disabled=document.getElementById("ContainerLabel").checked;
		if(disabled)
		{
			cellInnerHTML="<input type='text' name='"+name+"' id='"+id+"' size='30' class='black_ar' onkeydown=\"return addMoreRows(this,event,'containerRowsContainer','containerCounter')\">";
		}
		else
		{
			cellInnerHTML="<input type='text' name='"+name+"' id='"+id+"' size='30' class='black_ar' disabled='"+(!disabled)+"' onkeydown=\"return addMoreRows(this,event,'containerRowsContainer','containerCounter')\">";
		}
		checkBoxCell.innerHTML=cellInnerHTML;	

		var operation=document.getElementById("operation").value;
		if(operation=='edit')
		{
			var checkBoxCell=newRow.insertCell(2);
			name="containerDetails(containerBarcode_"+thisRowNum+")";
			id="containerBarcode_"+thisRowNum;
			disabled=document.getElementById("ContainerBarcode").checked;
			if(disabled)
			{
				cellInnerHTML="<input type='text' name='"+name+"' id='"+id+"' size='30' class='black_ar' disabled='' onkeydown=\"return addMoreRows(this,event,'containerRowsContainer','containerCounter')\">"
			}
			else
			{
				cellInnerHTML="<input type='text' name='"+name+"' id='"+id+"' size='30' class='black_ar' disabled='"+(!disabled)+"' onkeydown=\"return addMoreRows(this,event,'containerRowsContainer','containerCounter')\">";
			}
			checkBoxCell.innerHTML=cellInnerHTML;	
		}
		
		document.getElementById("containerCounter").value=thisRowNum;
		document.getElementById(id).focus();
	}

	function changeContainerLabelChoice(clickedObj)
	{
		var operation=document.getElementById("operation").value;
		if(operation=='edit')
		{
			if(clickedObj.checked==true)
			{
				var numOfControls=parseInt(document.getElementById("containerCounter").value);
				//Enable the corresponding choice textboxes
				if(clickedObj.value=='ContainerLabel')
				{
					enableControls(true,"containerLabel_",numOfControls);
					enableControls(false,"containerBarcode_",numOfControls);
				}
				else if(clickedObj.value=='ContainerBarcode')
				{
					enableControls(false,"containerLabel_",numOfControls);
					enableControls(true,"containerBarcode_",numOfControls);
				}
			}
			else
			{
				//Disnable the corresponding choice textboxes			
			}
		}
	}
</script>
<html:hidden property="containerCounter" styleId="containerCounter"/>
<table width="100%" border="0" cellpadding="3" cellspacing="0">
	<tr class="addl_table_head">
		<td height="25" colspan="3" align="left" valign="middle" class="tr_bg_border"><span class="black_ar_b">Containers</span></td>
	</tr>
	<tr class="addl_table_head">
    	<td height="25" colspan="3" align="left" valign="middle" class="tr_bg_border">
			<c:if test="${operation=='add' or operation=='edit'}">
				<bean:message key="createShipment.container.labelchoicequestion" bundle="msg.shippingtracking"/> &nbsp;&nbsp;
				<html:radio property="containerLabelChoice" styleId="ContainerLabel" value="ContainerLabel" onclick="changeContainerLabelChoice(this)"/> <bean:message key="createShipment.container.name" bundle="msg.shippingtracking"/>
				<html:radio property="containerLabelChoice" styleId="ContainerBarcode" value="ContainerBarcode" onclick="changeContainerLabelChoice(this)"/> <bean:message key="createShipment.name" bundle="msg.shippingtracking"/>
				<script>
						var contBarcodeRadio=document.getElementById("ContainerBarcode");
						var contLabelRadio=document.getElementById("ContainerLabel");
						if(contLabelRadio.checked==false && contBarcodeRadio.checked==false)
						{
								contLabelRadio.checked=true;
						}
				</script>
			</c:if>
			<logic:equal name="operation" value='view'>
				<html:hidden property="containerLabelChoice" styleId="specimenLabelChoice"/>
			</logic:equal>
    	</td>
	</tr>
	<tr class="addl_table_head">
		 <c:if test="${operation=='add' or operation=='edit'}">
			<td align="left" valign="middle" class="black_ar"><b class="black_ar_s"><bean:message key="shipment.contentsSelect" bundle="msg.shippingtracking"/>&nbsp;&nbsp; </b></td>
		</c:if>
		<td height="25" align="left" valign="middle" class="black_ar"><b class="black_ar_s"> <bean:message key="shipment.contentsNameOrBarcode" bundle="msg.shippingtracking"/> </b></td>
		<td width="25%" align="left" valign="middle" class="black_ar">&nbsp;</td>
	</tr>
	<!-- Iterate for each container -->
	
		<jsp:useBean id="noOfContainers" type="java.lang.Integer"/>
			<c:if test="${noOfContainers==0}">
					<c:set var="noOfContainers" value="1"/>
			</c:if>

			<script>
					var containerCount=parseInt(document.getElementById("containerCounter").value);
					if(containerCount==0)
							containerCount=1;

					document.getElementById("containerCounter").value=containerCount;
			</script>
	</table>
	<table cellpadding="3">
		<tbody id="containerRowsContainer">
		    <c:forEach var="containerNumber" begin="1" end="${noOfContainers}">
		    	<tr>
		    		<c:if test="${operation=='add' or operation=='edit'}">
					    <td align="left" valign="top" class="black_ar">
					    		<c:set var="containerCheckBoxName">containerDetails(containerCheckbox_<c:out value="${containerNumber}"/>)</c:set>
								<jsp:useBean id="containerCheckBoxName" type="java.lang.String"/>
					    		
								<c:set var="containerCheckBoxId">containerCheckbox_<c:out value="${containerNumber}"/></c:set>
								<jsp:useBean id="containerCheckBoxId" type="java.lang.String"/>
								
					    		<html:checkbox  styleId="<%=containerCheckBoxId%>" property="<%=containerCheckBoxName%>"/>
					   	</td>
                       </c:if>
					
				    <td height="30" align="left" valign="top">
				    	<label>	
				    		<c:set var="containerLabelName">containerDetails(containerLabel_<c:out value="${containerNumber}"/>)</c:set>
							<jsp:useBean id="containerLabelName" type="java.lang.String"/>
							
							<c:set var="containerLabelId">containerLabel_<c:out value="${containerNumber}"/></c:set>
							<jsp:useBean id="containerLabelId" type="java.lang.String"/>

							<logic:notEqual name="operation" value='viewNonEditable'>					
				    		<html:text styleClass="black_ar" size="30" styleId="<%=containerLabelId%>" property="<%=containerLabelName%>" onkeydown="return addMoreRows(this,event,'containerRowsContainer','containerCounter')"/>
							</logic:notEqual>
							
							<logic:equal name="operation" value='viewNonEditable'>
							<html:text styleClass="black_ar" size="30" readonly="true" styleId="<%=containerLabelId%>" property="<%=containerLabelName%>" onkeydown="return addMoreRows(this,event,'containerRowsContainer','containerCounter')"/>
							</logic:equal>
					    </label>
					</td>
				    <td align="left" valign="top">
							<c:if test="${operation=='edit' or operation=='view'}">
							<label>	
								<c:set var="containerBarcodeName">containerDetails(containerBarcode_<c:out value="${containerNumber}"/>)</c:set>
								<jsp:useBean id="containerBarcodeName" type="java.lang.String"/>
								
								<c:set var="containerBarcodeId">containerBarcode_<c:out value="${containerNumber}"/></c:set>
								<jsp:useBean id="containerBarcodeId" type="java.lang.String"/>
								
								<logic:notEqual name="operation" value='viewNonEditable'>	
								<html:text styleClass="black_ar" size="30" styleId="<%=containerBarcodeId%>" property="<%=containerBarcodeName%>" onkeydown="return addMoreRows(this,event,'containerRowsContainer','containerCounter')"/>
								</logic:notEqual>
							</label>
						</c:if>
						<c:if test="${operation!='edit'}">	
							&nbsp;
						</c:if>
					</td>
			    </tr>
		    </c:forEach>
	    </tbody>
    </table>
	<!-- End of iterate for each container -->
	<c:if test="${operation=='add' or operation=='edit'}">
		<table cellpadding="3">
			<tr>
				<td height="40" colspan="4" class="addMoreContainer">			
					<html:link href="javascript:addMoreContainerDetails()" styleId="addMoreContainer">
						<img src="images/shippingtracking/ic_add.gif" alt="Add More"  align="absmiddle">
					</html:link>
			    	<img src="images/shippingtracking/sep.gif" width="1" height="19" hspace="5" align="absmiddle">
			    	<html:link href="javascript:deleteCheckedRows('containerRowsContainer','#',document.forms[0].containerCounter,'containerCheckbox_',false,'',false);" styleId="deleteContainer">
				    	<img src="images/shippingtracking/ic_delete.gif" alt="Delete"  align="absmiddle">
				    </html:link>
				</td>
			</tr>
		</table>
	 </c:if>