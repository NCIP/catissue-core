// Arrays for nodes and icons
var nodes			= new Array();
var openNodes	= new Array();
var icons			= new Array(6);

// Loads all icons that are used in the tree
function preloadIcons() 
{
	icons[0] = new Image();
	icons[0].src = "images/plus.gif";
	icons[1] = new Image();
	icons[1].src = "images/plusbottom.gif";
	icons[2] = new Image();
	icons[2].src = "images/minus.gif";
	icons[3] = new Image();
	icons[3].src = "images/minusbottom.gif";
	
}
// Create the tree
function createTree(arrName, startNode, openNode) 
{
	TreeNodes = arrName;
	if (TreeNodes.length > 0) 
	{
		preloadIcons();
		if (startNode == null) startNode = 0;
		if (openNode != 0 || openNode != null) setOpenNodes(openNode);
	
		if (startNode !=0) 
		{
			var nodeValues = TreeNodes[getArrayId(startNode)].split("|");
			var treeItem = document.getElementById('tree');
			//treeItem.innerHTML = "<a href=\"" + nodeValues[3] + "\" onmouseover=\"window.status='" + nodeValues[2] + "';return true;\" onmouseout=\"window.status=' ';return true;\"><img src=\"img/folderopen.gif\" align=\"absbottom\" alt=\"\" />" + nodeValues[2] + "</a><br />";
			document.write("<a href=\"" + nodeValues[3] + "\" onmouseover=\"window.status='" + nodeValues[2] + "';return true;\" onmouseout=\"window.status=' ';return true;\"><img src=\"img/folderopen.gif\" align=\"absbottom\" alt=\"\" />" + nodeValues[2] + "</a><br />");
		} //else document.write("<img src=\"img/base.gif\" align=\"absbottom\" alt=\"\" />Website<br />");
	
		var recursedNodes = new Array();
		
		addNode(startNode, recursedNodes);
		
	}
}
// Returns the position of a node in the array
function getArrayId(node) 
{
	for (i=0; i<TreeNodes.length; i++) 
	{
		var nodeValues = TreeNodes[i].split("|");
		if (nodeValues[0]==node) return i;
	}
}
// Puts in array nodes that will be open
function setOpenNodes(openNode) 
{
	for (i=0; i<TreeNodes.length; i++) 
	{
		var nodeValues = TreeNodes[i].split("|");
		if (nodeValues[0]==openNode) 
		{
			openNodes.push(nodeValues[0]);
			setOpenNodes(nodeValues[1]);
		}
	} 
}
// Checks if a node is open
function isNodeOpen(node) 
{
	for (i=0; i<openNodes.length; i++)
		if (openNodes[i]==node) return true;
	return false;
}
// Checks if a node has any children
function hasChildNode(parentNode) 
{
	for (i=0; i< TreeNodes.length; i++) 
	{
		var nodeValues = TreeNodes[i].split("|");
		if (nodeValues[1] == parentNode) return true;
	}
	return false;
}
// Checks if a node is the last sibling
function lastSibling (node, parentNode) 
{
	var lastChild = 0;
	for (i=0; i< TreeNodes.length; i++) 
	{
		var nodeValues = TreeNodes[i].split("|");
		if (nodeValues[1] == parentNode)
			lastChild = nodeValues[0];
	}
	if (lastChild == node) return true;
	return false;
}

// Adds a new node 
function addNode(parentNode, recursedNodes) 
{
	var nodeCount = TreeNodes.length;
	
	var j = 1;
	//var treeItem = document.getElementById('tree');
			
	for (var i = 0; i < TreeNodes.length; i++) 
	{

		var nodeValues = TreeNodes[i].split("|");
		if (nodeValues[1] == parentNode) 
		{
			var ls	= lastSibling(nodeValues[0], nodeValues[1]);  //1|0
			var hcn	= hasChildNode(nodeValues[0]);
			var ino = isNodeOpen(nodeValues[0]);
			
			if(parentNode == 0)
			{
				//treeItem.innerHTML = "<tr><td class='tdTitle' colspan='4'>Rule#"+j+"</td></tr>";
				document.write("<tr>");
				document.write("<td class='tdTitle' colspan='5'>");
				//Bug#1305- Rule numbers not necessary
				//document.write("Rule#"+j);
				document.write("</td>");
				document.write("</tr>");
				j++;
			}
			
			// Write out line & empty icons
			if(i == 0 || (i%2 == 0))
				//treeItem.innerHTML = "<tr><td class='tdoneColor' colspan='4'><img src='images/"+nodeValues[3]+".GIF' alt='"+nodeValues[3]+"' />&nbsp;";
				document.write("<tr valign='middle'><td class='tdoneColor' colspan='5'><img src='images/"+ nodeValues[3] +".GIF' alt='"+nodeValues[3]+"' />&nbsp;");
			else
				//treeItem.innerHTML = "<tr><td class='tdsecondColor' colspan='4'><img src='images/"+nodeValues[3]+".GIF' alt='"+nodeValues[3]+"' />&nbsp;";
				document.write("<tr valign='middle'><td class='tdsecondColor' colspan='5'><img src='images/"+ nodeValues[3] +".GIF' alt='"+nodeValues[3]+"' />&nbsp;");
				
			for (g=0; g<recursedNodes.length; g++) 
			{
				if (recursedNodes[g] == 1) 
					//treeItem.innerHTML = "<img src=\"images/line.gif\" align=\"absbottom\" alt=\"\" />";
					document.write("<img src=\"images/line.gif\" align=\"absbottom\" alt=\"\" />");
				else 
					//treeItem.innerHTML = "<img src=\"images/empty.gif\" align=\"absbottom\" alt=\"\" />";
					 document.write("<img src=\"images/empty.gif\" align=\"absbottom\" alt=\"\" />");
			}
			
			// put in array line & empty icons
			if (ls) 
				recursedNodes.push(0);
			else 
				recursedNodes.push(1);

			/** Code for + and - join used for opening tree's nodes **/
			
			// Write out join icons
			//if (hcn) {
				//if(i == 0) {
				/*if (ls) {
					 
					document.write("<a href=\"javascript: oc(" + nodeValues[0] + ", 1);\"><img id=\"join" + nodeValues[0] + "\" src=\"images/");
					
							if (ino) document.write("minus");
							else document.write("plus");
							document.write("bottom.gif\" align=\"absbottom\" alt=\"Open/Close node\" /></a>");
					
				} else {
					
					document.write("<a href=\"javascript: oc(" + nodeValues[0] + ", 0);\"><img id=\"join" + nodeValues[0] + "\" src=\"images/");
					
						//if(i == 0){
						if (ino) document.write("minus");
						else document.write("plus");
						document.write(".gif\" align=\"absbottom\" alt=\"Open/Close node\" /></a>");

						
				}*/
				
			//} else {
				
				if (ls)
					//treeItem.innerHTML = "<img src=\"images/joinbottom.gif\" align=\"absbottom\" alt=\"\" />";
					 document.write("<img src=\"images/joinbottom.gif\" align=\"absbottom\" alt=\"\" />");
				else 
					//treeItem.innerHTML = "<img src=\"images/join.gif\" align=\"absbottom\" alt=\"\" />";
					document.write("<img src=\"images/join.gif\" align=\"absbottom\" alt=\"\" />");
			//}
			
			if(nodeValues[4] != "default")
			{
				
				if(nodeValues[4] == "true")
				{ 
				//document.write("<img src='images/lineH.gif' align='absbottom' alt='Add' />");
					document.write("<img src='images/point.gif' alt='Add' />");
				}
				else
				{
					document.write("<img src='images/graydot.gif' alt='Add' />");
				}
			}

			var checkb = nodeValues[3] + "_" + nodeValues[0];
			//treeItem.innerHTML = "<input type=checkbox name='"+checkb+"' id='"+ checkb + "' onClick = checkNum('"+ checkb +"','"+nodeValues[3]+"',"+nodeCount+")" +">";
			document.write("<input type=checkbox name='"+checkb+"' id='"+ checkb + "' onClick = checkNum("+ nodeCount+ ") ");//onClick = checkNum('"+ checkb +"',"+nodeValues[0]+","+nodeCount+ ") ");
			if(i == (TreeNodes.length - 1))
			{
			 document.write("checked='true'>");
				
			}
			else
				document.write(">");
			//treeItem.innerHTML = "&nbsp;"+nodeValues[2];
			
			document.write("&nbsp;"+nodeValues[2]);
			//treeItem.innerHTML = "</td></tr>";
			document.write("</td></tr>");
			
			// If node has children write out divs and go deeper
			if (hcn) 
			{
				//treeItem.innerHTML = "<div id=\"div" + nodeValues[0] + "\"";
				document.write("<div id=\"div" + nodeValues[0] + "\"");
				if (!ino)
					//treeItem.innerHTML = " style=\"display: none;\">";
					document.write(" style=\"display: none;\"");
				
				document.write(">");
				addNode(nodeValues[0], recursedNodes);
				//treeItem.innerHTML = "</div>";
				document.write("</div>");
			}
			
			// remove last line or empty icon 
			recursedNodes.pop();
		}
	}
}
// Opens or closes a node
function oc(node, bottom) 
{
	var theDiv = document.getElementById("div" + node);
	var theJoin	= document.getElementById("join" + node);
	
	if (theDiv.style.display == 'none') 
	{
		if (bottom==1)
			 theJoin.src = icons[3].src;
		else 
			theJoin.src = icons[2].src;
			
		theDiv.style.display = '';
	}
	 else 
	{
		if (bottom==1)
			theJoin.src = icons[1].src;
		else 
			theJoin.src = icons[0].src;
		
		theDiv.style.display = 'none';
	}
}
// Push and pop not implemented in IE
if(!Array.prototype.push)
 {
	function array_push() 
	{
		for(var i=0;i<arguments.length;i++)
			this[this.length]=arguments[i];
		return this.length;
	}
	Array.prototype.push = array_push;
}
if(!Array.prototype.pop) 
{
	function array_pop()
	{
		lastElement = this[this.length-1];
		this.length = Math.max(this.length-1,0);
		return lastElement;
	}
	Array.prototype.pop = array_pop;
}

