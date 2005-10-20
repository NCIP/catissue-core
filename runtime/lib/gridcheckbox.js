/**
This methid will be called whenever a check box in a grid is clicked.
description: If check box is checked then myData is updated.
i.e. corresponding html input tag in myData is replaced with
its same contents and string ' checked>' is added.if check box 
is unchecked then corresponding html input tab in mydata is 
altered such that 'checked>' is replaced by '>'.
Also last column in myData which contains flag 0/1 is toggled.
*/
function changeData(element)
{
	//if check box is checked
	if(element.checked==true)
	{
		var tag=''+myData[element.id][0];
		tag = tag.substring(0,tag.indexOf('>',0));
		//append 'hecked>'
		tag= tag + ' checked>';
		myData[element.id][0]=tag;
		//toggle flag
		myData[element.id][myData[element.id].length-1]='0';
	}
	else
	{
		var tag=''+myData[element.id][0];
		//remove 'checked>'
		tag = tag.substring(0,tag.indexOf('checked',0));
		//append '>'
		tag= tag+ ' >';
		myData[element.id][0]=tag;
		//append flag
		myData[element.id][myData[element.id].length-1]='1';
	}
}

/*
This method will be called when "Select All.." is clicked. 
Method will toggle all the check box in grid. Also corresponding 
myData is changed. i.e. either 'checked>' is added or 'checked>'
is replaced by '>'. Last column containing flag 0/1 is also toggled.
*/
function checkUncheck(element)
{

	var i;
	var noOfColumns = myData[0].length-1;
	
	for(i=0;i<myData.length;i++)
	{
		var tag = myData[i][0];
		//is select all is ckecked
		if(element.checked==true)
		{
			//if check box in grid is not checked 
			//then make checked = true and
			//insert 'checked>'
			if(myData[i][noOfColumns]=='1')
			{
				tag = tag.substring(0,tag.indexOf('>',0));
				//append 'hecked>'
				tag= tag + ' checked>';
				myData[i][0]=tag;
				//toggle flag
				myData[i][noOfColumns]='0';
				//get check box object and check 
				var checkBox = document.getElementById(i);
				checkBox.checked=true;
			}
			
		}
		else
		{
			//if check box in grid is checked
			//then make checked=false
			//remove 'checked>' append '>'
			
			if(myData[i][noOfColumns]=='0')
			{
				//remove 'checked>'
				tag = tag.substring(0,tag.indexOf('checked',0));
				//append '>'
				tag= tag+ ' >';
				myData[i][0]=tag;
				//append flag
				myData[i][noOfColumns]='1';
				//get check box object and uncheck 
				var checkBox = document.getElementById(i);
				checkBox.checked=false;			
			}			
		}		
	}
}