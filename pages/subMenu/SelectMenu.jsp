<%
	String selectMenu = (String) request.getAttribute("menuSelected");
	int selectMenuID = 0;
	if(selectMenu != null)
	{
		selectMenuID = Integer.parseInt(selectMenu);
	}
	String selectedMenuClass = "subMenuPrimaryItemsHover";
	String normalMenuClass = "subMenuPrimaryItemsWithBorder";
	String hoverMenuClass = "subMenuPrimaryItemsWithBorderOver";
	String strMouseOut ="";
%>