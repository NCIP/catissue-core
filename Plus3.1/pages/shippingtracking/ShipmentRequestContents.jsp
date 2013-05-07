
<c:set var="noOfSpecimens" value="${requestScope.specimenCountArr}"/>
<c:set var="noOfContainers" value="${requestScope.containerCountArr}"/>

<jsp:useBean id="noOfSpecimens" type="java.lang.Integer[]"/>
<jsp:useBean id="noOfContainers" type="java.lang.Integer[]"/>

<c:set var="specimenLabelArr" value="${requestScope.specimenLabelArr}"/>
<c:set var="containerLabelArr" value="${requestScope.containerLabelArr}"/>  

<jsp:useBean id="specimenLabelArr" type="java.lang.String[]"/>
<jsp:useBean id="containerLabelArr" type="java.lang.String[]"/>

<% int specimenCounter=0;
	int containerCounter=0;
%>

<table width="100%">
	<c:set var="siteCount" value="${requestScope.siteCount}"/>
	<jsp:useBean id="siteCount" type="java.lang.Integer"/>
	
	<c:forEach var="siteNumber" begin="0" end="${siteCount-1}">
	<jsp:useBean id="siteNumber" type="java.lang.Integer"/>
		
		<tr class="tableheading">
            <td height="25" colspan="2" align="left" class="black_ar_b">&nbsp;&nbsp;<bean:message key="shipment.site" bundle="msg.shippingtracking"/> <c:out value="${siteNumber+1}"/></td>
        </tr>
        <tr>
            <td width="49%" align="left" valign="top">
            	<c:set var="specimenCount"><%=noOfSpecimens[siteNumber]%></c:set>
				<c:if test="${specimenCount>0}">  
					<table width="100%" border="0" cellpadding="3" cellspacing="0">
						<tr class="">
					    	<td height="25" colspan="3" align="left" valign="middle" class="">
					    		<span class="black_ar_b">
					    			<bean:message key="shipment.contentsSpecimens" bundle="msg.shippingtracking"/>
					    		</span>
					    	</td>
						</tr>
					    <tr class="addl_table_head">
						    <td align="left" valign="middle" class="black_ar"><b class="black_ar_s">&nbsp;&nbsp; </b></td>
							<td height="25" align="left" valign="middle" class="black_ar"><b class="black_ar_s"> <bean:message key="shipment.contentsLabelOrBarcode" bundle="msg.shippingtracking"/> </b></td>
							<td width="25%" align="left" valign="middle" class="black_ar">&nbsp;</td>
					    </tr>
					    <!-- Iterate for each specimen -->
					</table>
					<table cellpadding="3">
						<tbody id="specimenRowsContainer">
							
							    <c:forEach var="specimenNumber" begin="1" end="${specimenCount}">
							    	<tr>
									    <td align="left" valign="top" class="black_ar">
									    	&nbsp;
									   	</td>
									    <td height="30" align="left" valign="top" class="formField" width="100%">
									    	<label>	
									    		<c:set var="specimenlabelName">specimenDetails_<c:out value="${siteNumber}"/>_<c:out value="${specimenNumber}"/></c:set>
												<jsp:useBean id="specimenlabelName" type="java.lang.String"/>
												
									    		<html:hidden styleId="<%=specimenlabelName%>" property="<%=specimenlabelName%>" value="<%=specimenLabelArr[specimenCounter]%>"/>	 
												<%=specimenLabelArr[specimenCounter++]%>
										    </label>
										</td>
									    <td align="left" valign="top">&nbsp;</td>
								    </tr>
							    </c:forEach>
					    </tbody>
				    </table>
				    </c:if>  
					<!-- End of iterate for each specimen -->
            </td>
            <td width="51%" align="left" valign="top">
            	<c:set var="containerCount"><%=noOfContainers[siteNumber]%></c:set>
				<c:if test="${containerCount>0}"> 
				<table width="100%" border="0" cellpadding="3" cellspacing="0">
					<tr class="">
						<td height="25" colspan="3" align="left" valign="middle" class=""><span class="black_ar_b">
							<bean:message key="shipment.contentsContainers" bundle="msg.shippingtracking"/></span>
						</td>
					</tr>
					<tr class="">
						<td align="left" valign="middle" class="black_ar"><b class="black_ar_s">&nbsp;&nbsp; </b></td>
						<td height="25" align="left" valign="middle" class="black_ar"><b class="black_ar_s"> <bean:message key="shipment.contentsNameOrBarcode" bundle="msg.shippingtracking"/> </b></td>
						<td width="25%" align="left" valign="middle" class="black_ar">&nbsp;</td>
					</tr>
					<!-- Iterate for each container -->
					</table>
					<table cellpadding="3">
						<tbody id="containerRowsContainer">
							    <c:forEach var="containerNumber" begin="1" end="${containerCount}">
							    	<tr>
									    <td align="left" valign="top">
									    	&nbsp;
									   	</td>
									    <td height="30" align="left" valign="top" class="formField" width="100%">
									    	<label>	
									    		<c:set var="containerlabelName">containerDetails<c:out value="${siteNumber}"/>_<c:out value="containerNumber"/></c:set>
												<jsp:useBean id="containerlabelName" type="java.lang.String"/>
												
									    		<html:hidden styleId="<%=containerlabelName%>" property="<%=containerlabelName%>" value="<%=containerLabelArr[containerCounter]%>"/>	    	
									    		<%=containerLabelArr[containerCounter++]%>
										    </label>
										</td>
									    <td align="left" valign="top">&nbsp;</td>
								    </tr>
							    </c:forEach>
					    </tbody>
				    </table>
				   </c:if>
					<!-- End of iterate for each container -->
            </td>
        </tr>
	</c:forEach>
</table>