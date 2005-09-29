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

function  deleteChecked(subdivtag,action)
{
	var r = new Array(); 
	var element = document.getElementById(subdivtag);
	var pNode = element.parentNode;
	//var counts = element.rows.length;
	var counts = document.forms[0].counter.value;
	var delCounts = 0;
	for(i=1;i<=counts;i++)//2
	{
		itemCheck="chk_"+i;
		var chk = document.getElementById(itemCheck);
		if(document.all[itemCheck].checked==true){
			var currentRow = chk.parentNode.parentNode;
			var k = currentRow.rowIndex;
			pNode.deleteRow(k);
			delCounts++;
		}
	}
	
	//document.forms[0].counter.value = element.rows.length;
	document.forms[0].counter.value = (document.forms[0].counter.value - delCounts);
	document.forms[0].action = action;
	document.forms[0].submit();
		
		
}
		
