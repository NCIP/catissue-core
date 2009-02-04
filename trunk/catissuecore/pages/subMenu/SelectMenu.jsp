<%@ page import="edu.wustl.catissuecore.util.global.Constants"%>
<%
	String selectMenu = (String) request.getAttribute(Constants.MENU_SELECTED);
	int selectMenuID = 0;
	if(selectMenu != null && !selectMenu.equals(""))
	{
		selectMenuID = Integer.parseInt(selectMenu);
	}
	String selectedMenuClass = "subMenuPrimaryItemsHover";
	String normalMenuClass = "subMenuPrimaryItemsWithBorder";
	String hoverMenuClass = "subMenuPrimaryItemsWithBorderOver";
	String strMouseOut ="";
%>