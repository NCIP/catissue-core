/* section for outer block start */
function replaceSpeChar(div,d1,searchChar)
{
	var y = d1.innerHTML;
	insno=insno+1;
	var starpos = y.indexOf(searchChar); // pos of 1st `.  ie: No.
	var adstr="";
	var c=0;
	for(c=0;c<starpos;c++)
	{
		adstr = y.replace(searchChar,insno);
		starpos = y.indexOf(searchChar); 
		y = adstr;
	}
	addDiv(div,adstr);
}

/* section for outer block end */

function addDiv(div,adstr)
{
	var x = div.innerHTML;
	div.innerHTML = div.innerHTML +adstr;
	alert(div.innerHTML);
}

function  deleteChecked(subdivtag,action,countElement,checkName)
{
	var r = new Array(); 
	var element = document.getElementById(subdivtag);
	var pNode = element.parentNode;
	var counts = countElement.value;
	if(counts == undefined){
		var cnt = document.getElementById(countElement);
		counts = cnt.value;
	}
	var delCounts = 0;
	for(i=1;i <= counts;i++)
	{

		itemCheck = checkName+i;
		var chk = document.getElementById(itemCheck);
		if(document.all[itemCheck].checked==true){
			var currentRow = chk.parentNode.parentNode;
			var k = currentRow.rowIndex;
			pNode.deleteRow(k);
			delCounts++;
		}
	}
	
	if(countElement.value == undefined){
		cnt.value = counts - delCounts;
	}
	else
		countElement.value = (countElement.value - delCounts);
	
	document.forms[0].action = action;
	document.forms[0].submit();
		
		
}
		
