var search1='`';
var search2='~';
function insertStr(mainString,insertpos,strToInsert)
{
	var tmpstr = new String(mainString);
	var str1 = tmpstr.substr(0,insertpos);
	var str2 = tmpstr.substring(insertpos+1,tmpstr.length);

	var returnStr = str1+strToInsert+str2;

	//return returnStr;
	return mainString;
} // insertStr

var insno=0;
var subdivarray = new Array(10);

/* function b1(div,d1,searchChar)
{
	var y = d1.innerHTML;
//	insno=insno+1;
alert(insno);
	var starpos = y.indexOf(searchChar); // pos of 1st `.  ie: No.

//		alert(starpos);
		pos = starpos;
		adstr = insertStr(y,pos,insno);
		y = adstr;

	alert("ADSTR  : "+adstr);
	addDiv(div,adstr);
} // b1
*/

function b1(div,d1,searchChar)
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

//	alert("ADSTR  : "+adstr);
	addDiv(div,adstr);
}

var insno1=1;
function b2(div,d1_1,searchChar)
{
	var y = d1_1.innerHTML;
	insno1=insno1+1;
	var starpos = y.indexOf(searchChar); // pos of 1st `.  ie: No.
	var adstr="";
	var c=0;
	for(c=0;c<starpos;c++)
	{
		adstr = y.replace(searchChar,insno);
		starpos = y.indexOf(searchChar); 
		y = adstr;
	}
//	alert("ADSTR  : "+adstr);
	addDiv(div,adstr);
} // b2

function b21(subdiv,searchChar)
{
//	alert("click");
	alert(subdiv);
	var y = d1_1.innerHTML;
//	alert(y);
	newdiv = document.getElementById(subdiv);
	insno1=insno1+1;

	var starpos = y.indexOf(searchChar); // pos of 1st `.  ie: No.
	var adstr="";
	var c=0;
	for(c=0;c<starpos;c++)
	{
		adstr = y.replace(searchChar,insno1);
		starpos = y.indexOf(searchChar); 
		y = adstr;
	}
	alert("ADSTR  : "+adstr);
	addDiv(newdiv,adstr);


} // b21


function addDiv(div,adstr)
{
	var x = div.innerHTML;
//	alert(x);
	div.innerHTML = div.innerHTML +adstr;
	alert(div.innerHTML);
}
