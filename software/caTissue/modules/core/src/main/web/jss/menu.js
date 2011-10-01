function expand(s)
{
	//var s = document.getElementById(s1);
  var td = s;
  var d = td.getElementsByTagName("div").item(0);

  td.className = "menuHover";
  d.className = "menuHover";
}
function collapse(s)
{
	//var s = document.getElementById(s1);
  var td = s;
  
  var d = td.getElementsByTagName("div").item(0);

  td.className = "formTitle";
  d.className = "menuNormal";
}
