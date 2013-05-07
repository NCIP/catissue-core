<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="edu.wustl.common.util.global.ApplicationProperties"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="maintable">
<tr>
		<td class="td_color_bfdcf3">
			<table border="0" cellpadding="0" cellspacing="0">
		      <tr>
				<td class="td_table_head">
					<span class="wh_ar_b">
						Keyword Search Results
					</span>
				</td>
		        <td>
					<img src="images/uIEnhancementImages/table_title_corner2.gif" alt="Page Title - Search Results" width="31" height="24" hspace="0" vspace="0" />
				</td>
		      </tr>
		    </table>
	<tr>
	  <td class="tablepadding">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		  <td class="td_tab_bg" ><img src="images/uIEnhancementImages/spacer.gif" alt="spacer" width="50" height="1"></td>
	</tr>
	</table>

	<table width="100%" border="0" cellpadding="3" cellspacing="0" class="whitetable_bg">
      <tr>
        <td align="left" class="toptd"></td>
      </tr>
		<td align="left" class="tr_bg_blue1"><span class="blue_ar_b"><img src="images/uIEnhancementImages/alert-icon.gif"> &nbsp;
				<bold><%=ApplicationProperties.getValue("keywordSearch.maxLimit.exceeded")%></bold>&nbsp;
			</span>
			</td>
	</table>
</td>
</tr>
</table>
</body>
</html>