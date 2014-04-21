// calendar functions

var calformname;
var calformelement;
var calpattern;
var calweekstart;
var shouldUseTime='false';
var currentDivId=null;

/**
Changes done by Jitendra on 18/09/2006 to fix the bug when more than one DateTimeComponent tag included in the Jsp.<b>
Now each function is getting one extra parameter called id which is used to generate unique id for each component.<b>
*/

function printCalendar(id, day, month, year)
{
    printCal(id,"Sun","Mon","Tue","Wed","Thu","Fri","Sat",1,"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec", day, month, year, 'false');
    //printMonthAndYearCal(id,"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",year);
    //printYearOnlyCal(id, year);
}

function printMonthYearCalendar(id, month, year)
{
    printMonthAndYearCal(id,"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",year);
}

function printYearCalendar(id, year)
{
    printYearOnlyCal(id, year);
}

function printMonthAndYearCal(id,month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12, year)
{
    document.write('<table cellpadding="0" cellspacing="0" border="0" width="150" height="20" bgcolor="#BDDBF3">');
    document.write('<tr>');
    document.write('    <td class="CALENDARBORDER" width="5"></td>');
    //document.write('    <td class="CALENDARTITLE" width="10" align="right"><img src="' + imgsrc + 'previous2.gif"></td>');
    //document.write('    <td class="CALENDARTITLE" align="left" width="10"><img src="' + imgsrc + 'previous.gif"></td>');
    document.write('    <td align="center" width="*" class="CALENDARTITLE">');
    document.write('<select id="calmois'+id+'" name="calmois'+id+'"><OPTION selected>Jan</OPTION><OPTION>Feb</OPTION><OPTION>Mar</OPTION><OPTION>Apr</OPTION><OPTION>May</OPTION><OPTION>Jun</OPTION><OPTION>Jul</OPTION><OPTION>Aug</OPTION><OPTION>Sep</OPTION><OPTION>Oct</OPTION><OPTION>Nov</OPTION><OPTION>Dec</OPTION>');
    document.write('</select>');
    document.write('</td>');
    document.write('    <td align="center" width="10" class="CALENDARTITLE" nowrap>');
    document.write('<select id="calyear'+id+'" name="calyear'+id+'" onload="showYears(id)" onChange="hideMonthAndYearCal('+"'"+id+"'"+')"><OPTION selected>1900</OPTION>');
    document.write('</select>');
    document.write('</td>');
    //document.write('    <td class="CALENDARTITLE"  width="10" align="right"><img src="' + imgsrc + 'next2.gif"></td>');
    //document.write('    <td class="CALENDARTITLE" width="10" align="left" width="40"><img src="' + imgsrc + 'next.gif"></td>');
    document.write('    <td class="CALENDARBORDER" width="5"></td>');
    document.write('    <td class="CALENDARTITLE" width="10" align="right"><img src="' + imgsrc + 'close.gif" onclick="hideMonthAndYearCal('+"'"+id+"'"+')"></td>');
    document.write('</tr>');
    document.write('</table>');
}

function printYearOnlyCal(id, year)
{
    document.write('<table cellpadding="0" cellspacing="0" border="0" width="100" height="20" bgcolor="#BDDBF3">');
    document.write('<tr>');
    document.write('    <td class="CALENDARBORDER" width="2"></td>');
    //document.write('    <td class="CALENDARTITLE" width="10" align="right"><img src="' + imgsrc + 'previous2.gif"></td>');
    //document.write('    <td class="CALENDARTITLE" align="left" width="10"><img src="' + imgsrc + 'previous.gif"></td>');
    document.write('    <td align="center" width="*" class="CALENDARTITLE">');
    document.write('</td>');
    document.write('    <td align="center" width="*" class="CALENDARTITLE" nowrap>');
    document.write('<select id="calyear'+id+'" name="calyear'+id+'" onload="showYears(id)" onChange="hideYearOnlyCal('+"'"+id+"'"+')"><OPTION selected>1900</OPTION>');
    document.write('</select>');
    document.write('</td>');
    //document.write('    <td class="CALENDARTITLE"  width="10" align="right"><img src="' + imgsrc + 'next2.gif"></td>');
    //document.write('    <td class="CALENDARTITLE" width="10" align="left" width="40"><img src="' + imgsrc + 'next.gif"></td>');
    document.write('    <td class="CALENDARBORDER" width="2"></td>');
    document.write('    <td class="CALENDARTITLE" width="10" align="right"><img src="' + imgsrc + 'close.gif" onclick="hideYearOnlyCal('+"'"+id+"'"+')"></td>');
    document.write('</tr>');
    document.write('</table>');
}

function showYears(id) {
    var j=0;
    var opts;
    for(prop=1900;prop<2020;prop++) {
        document.getElementById(id).options[j++] = new Option(prop, prop);
    }
}

function hideMonthAndYearCal(id) {
    var divId = "slcalcod"+id;

    var monthID = "calmois"+id;
    var yearID = "calyear"+id;

    var SelectedMonth = document.getElementById(monthID).selectedIndex;

    var SelectedMonthOption = document.getElementById(monthID).options[SelectedMonth].innerHTML;

	if (SelectedMonthOption == "Jan")
	{
		SelectedMonthOption = "01";
	}
	else if (SelectedMonthOption == "Feb")
	{
		SelectedMonthOption = "02";
	}
	else if (SelectedMonthOption == "Mar")
	{
		SelectedMonthOption = "03";
	}
	else if (SelectedMonthOption == "Apr")
	{
		SelectedMonthOption = "04";
	}
	else if (SelectedMonthOption == "May")
	{
		SelectedMonthOption = "05";
	}
	else if (SelectedMonthOption == "Jun")
	{
		SelectedMonthOption = "06";
	}
	else if (SelectedMonthOption == "Jul")
	{
		SelectedMonthOption = "07";
	}
	else if (SelectedMonthOption == "Aug")
	{
		SelectedMonthOption = "08";
	}
	else if (SelectedMonthOption == "Sep")
	{
		SelectedMonthOption = "09";
	}
		else if (SelectedMonthOption == "Oct")
	{
		SelectedMonthOption = "10";
	}
		else if (SelectedMonthOption == "Nov")
	{
		SelectedMonthOption = "11";
	}
		else if (SelectedMonthOption == "Dec")
	{
		SelectedMonthOption = "12";
	}

    var SelectedYear = document.getElementById(yearID).selectedIndex;

    var SelectedYearOption = document.getElementById(yearID).options[SelectedYear].innerHTML;

    var combined = formatMonthAndYearDate(SelectedMonthOption, SelectedYearOption);

    document.forms[calformname].elements[calformelement].value = combined;

    if(document.all) {
        // IE.
        //alert(document.getElementById(monthID).option);
        //alert(document.getElementById(yearID).value);
        document.getElementById(divId).style.visibility="hidden";
        document.getElementById(divId).style.visibility="hidden";

        showElement("SELECT");
    } else if(document.layers) {
        // Netspace 4
        document.getElementById(divId).visibility="hidden";
    } else {
        // Mozilla
        var calendrier = document.getElementById(divId);
        calendrier.style.visibility="hidden";
    }
     document.getElementById(id).style.color="black";
	 isDataChanged();
}

function formatMonthAndYearDate(month, year, id) {
    var date = "";
    var pos = 0;
    var pattern;
    var previousPattern;
    var patternLength = 0;
    if (calpattern!=null && calpattern.length>0) {
        previousPattern = calpattern.charAt(0);
        while (pos <= calpattern.length) {
            if (pos < calpattern.length) {
                pattern = calpattern.charAt(pos);
            }  else {
                pattern = "";
            }
            if (pattern != previousPattern) {
                switch (previousPattern) {
                    case 'y':
                        date += padYear(year, patternLength);
                        break;
                    case 'M':
                        date += padNumber(month, patternLength);
                        break;
                    case '\'':
                        // PENDING
                        break;
                    default:
                        date += previousPattern;
                }
                previousPattern = pattern;
                patternLength = 0;
            }
            patternLength++;
            pos++;
        }
    }

	 return date;
}

function hideYearOnlyCal(id) {
    var divId = "slcalcod"+id;

    var yearID = "calyear"+id;

    var SelectedYear = document.getElementById(yearID).selectedIndex;

    var SelectedYearOption = document.getElementById(yearID).options[SelectedYear].innerHTML;

    var combined = SelectedYearOption;

    document.forms[calformname].elements[calformelement].value = combined;

    if(document.all) {
        // IE.
        //alert(document.getElementById(monthID).option);
        //alert(document.getElementById(yearID).value);
        document.getElementById(divId).style.visibility="hidden";
        document.getElementById(divId).style.visibility="hidden";

        showElement("SELECT");
    } else if(document.layers) {
        // Netspace 4
        document.getElementById(divId).visibility="hidden";
    } else {
        // Mozilla
        var calendrier = document.getElementById(divId);
        calendrier.style.visibility="hidden";
    }
     document.getElementById(id).style.color="black";
	 isDataChanged();
}


function printTimeCalendar(id,day, month, year, time_hh, time_mm )
{
    printCal(id,"Sun","Mon","Tue","Wed","Thu","Fri","Sat",1,"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec", day, month, year, 'true', 10, 12);
}
/**
 * Static code included one time in the page.
 *
 * a {text-decoration: none; color: #000000;}");
 * TD.CALENDRIER {background-color: #C2C2C2; font-weight: bold; text-align: center; font-size: 10px; }");
 *
 * bgColor => #000000, #C9252C,
 */
function printCal(id,day1, day2, day3, day4, day5, day6, day7, first, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12, day, month, year, displayTime, time_hh, time_mm)
{
	var calHeight=20;
	if(document.all)
	{
      //I.E
		calHeight=20;
    }
	else if(document.layers)
	{
        // Netspace 4
		calHeight=20;
    }
	else
	{
        // Mozilla
		if(displayTime=='true')
		{
			calHeight=70;
		}
		else
		{
			calHeight=35;
		}
    }
	var calendar ='<div id="caltitre" style="z-index:10;height:'+calHeight+'">';
	calendar += '<div id="caltitre" style="z-index:10;height:'+calHeight+'">';
	calendar +='<table cellpadding="0" cellspacing="0" border="0" width="267" height:20" bgcolor="#BDDBF3">';
//  calendar +='<form>';
	calendar +='<tr><td colspan="15"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td></tr>';
	calendar +='<tr>';
	calendar +='    <td class="CALENDARBORDER" width="1"></td>';
	calendar +='    <td class="CALENDARTITLE"  colspan="3" align="right"><img src="' + imgsrc + 'previous2.gif" onclick="cal_previous_year('+ '\''+id +'\''+ ',' + day + ');"></td>';
	calendar +='    <td class="CALENDARTITLE" colspan="1" align="left" width="40"><img src="' + imgsrc + 'previous.gif" onclick="cal_before('+ '\''+id +'\''+ ',' + day + ');"></td>';
	calendar +='    <td colspan=6 align="right" class="CALENDARTITLE" nowrap>';

    // month
	calendar +='<select id="calmois'+id+'" name="calmois'+id+'" onchange="cal_chg('+ '\''+id +'\''+ ',' + day + ');"><option value=0>...</option>';

    // use the good day for week start.
    // store the day the week start for later.
    calweekstart = first;
    // compute an array of the days, starting from Sunday.
    caldays = new Array(7);
    caldays[0] = day1;
    caldays[1] = day2;
    caldays[2] = day3;
    caldays[3] = day4;
    caldays[4] = day5;
    caldays[5] = day6;
    caldays[6] = day7;
    // compute an array of the days, starting at the good day.
    computedcaldays = new Array(7);
    for (i=0; i<7; i++) {
        computedcaldays[(i+1-calweekstart+7)%7] = caldays[i];
    }

    for(i=1;i<=12;i++) {
        var str='<option value=' + i + '>';
        monthIndex = i-1;
        switch (monthIndex) {
            case 0: str += month1; break;
            case 1: str += month2; break;
            case 2: str += month3; break;
            case 3: str += month4; break;
            case 4: str += month5; break;
            case 5: str += month6; break;
            case 6: str += month7; break;
            case 7: str += month8; break;
            case 8: str += month9; break;
            case 9: str += month10; break;
            case 10: str += month11; break;
            case 11: str += month12; break;
        }
        calendar +=str;
    }

    calendar +='</select>';

    // year
    calendar +='<select id="calyear'+id+'" name="calyear'+id+'" onchange="cal_chg('+ '\''+id +'\''+ ',' + day + ');">';
    calendar +="</select>";

    calendar +='    </td>';
    //KK
    calendar +='    <td class="CALENDARTITLE" align="right"><img src="' + imgsrc + 'next.gif" onclick="cal_after('+ '\''+id +'\''+ ',' + day + ');"></td>';
    calendar +='    <td class="CALENDARTITLE" align="left"><img src="' + imgsrc + 'next2.gif" onclick="cal_after_year('+ '\''+id +'\''+ ',' + day + ');"></td>';

    calendar +='    <td class="CALENDARTITLE" align="right"><img src="' + imgsrc + 'close.gif" onclick="hideCalendar('+"'"+id+"'"+')"></td>';
    calendar +='    <td class="CALENDARBORDER" width=1><img src="' + imgsrc + 'calBackground.gif" width="1" height="1"></td>';
    calendar +='</tr>';

    // to display time
    if(shouldUseTime == 'false')
	{
		shouldUseTime = displayTime;
	}
    if(displayTime=='true')
    {
    	calendar +='<tr>';
    	calendar +='    <td colspan="5" class="CALENDARBORDER" align="right">Time &nbsp; </td>';
    	calendar +='<td colspan="5" class="CALENDARBORDER" align="left"><select id="timeHrs'+id+'" name="time_hh'+id+'" size="1">';
        var str="";
        for (i = 0; i <= 23; i++)
        {
            str += '<option value=' + i + '>' + i + '</option>';
        }
        calendar +=str;
        calendar +='    </select>';
        calendar +='    &nbsp;:&nbsp; <select id="timeMin'+id+'" name="time_mm'+id+'" size="1">';
        str="";
        for (i = 0; i <= 59; i++)
        {
            str += '<option value=' + i + '>' + i + '</option>';
        }
        calendar +=str;
        calendar +='    </select></td>';

        calendar +='    <td colspan=5 class="CALENDARBORDER" align="left">[HH : MM]</td>';
        calendar +='</tr>';
    }


    calendar +='<tr><td colspan=15 class="CALENDARBORDER"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td></tr>';
    calendar +='<tr>';
    calendar +='    <td class="CALENDARBORDER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calendar +='    <td class="CALENDRIER" width="38" align="center">' + computedcaldays[0] + '</td>';
    calendar +='    <td class="CALENDRIER" width="50"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calendar +='    <td class="CALENDRIER" width="10">'+"  " + computedcaldays[1] + '</td>';
    calendar +='    <td class="CALENDRIER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calendar +='    <td class="CALENDRIER" width="10">' + computedcaldays[2] + '</td>';
    calendar +='    <td class="CALENDRIER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calendar +='    <td class="CALENDRIER" width="38">' + computedcaldays[3] + '</td>';
    calendar +='    <td class="CALENDRIER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calendar +='    <td class="CALENDRIER" width="38">' + computedcaldays[4] + '</td>';
    calendar +='    <td class="CALENDRIER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calendar +='    <td class="CALENDRIER" width="38">' + computedcaldays[5] + '</td>';
    calendar +='    <td class="CALENDRIER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calendar +='    <td class="CALENDRIER" width="38">' + computedcaldays[6] + '</td>';
    calendar +='    <td class="CALENDARBORDER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calendar +='</tr>';

    calendar +='<tr><td colspan=15 class="CALENDARBORDER"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td></tr>';
//  calendar +='</form>';
    calendar +='</table>';
    calendar +='</div>';
//  calendar +='<div id="caljour" style="position:absolute; left:0px; top:45px; width:253; height:130; z-index:10;"></div>';
    calendar +='<div id="caljour'+id+'" style="z-index:10;"></div>';
    calendar +='</div>';

    if(document.getElementById('slcalcod' + id))
    {
      document.getElementById('slcalcod' + id).innerHTML = calendar;
    }
     else
    {
  	document.write(calendar);
    }
}

/**
 * Show the calendar
 */
function showCalendar(id,year, month, day, pattern, formName, formProperty, event, startYear, endYear) {
    //alert("year ="+year);
    //alert("month ="+month);
    //alert("day ="+day);
    //alert("id = "+id);
	//Suhas : bugid: 8180 : - Two date pickers are opening without getting closed automatically.
    if (currentDivId!=null)
    {
		hideCalendar(currentDivId);
		currentDivId=id;
    }
    else
    {
    	currentDivId=id;
    }
    var divId ="slcalcod"+id;
    var calmoisId="calmois"+id;
    var calyearId="calyear"+id;
    var dataEntryFormDiv=document.getElementById("dataEntryFormDiv");
    if (document.forms[formName].elements[formProperty].disabled) {
            return;
    }

    if (startYear!=null) {
        //alert("here, month!=0 && day!=0");
        //alert("calyearId ="+calyearId);
        var calyear = document.getElementById(calyearId);
        for (i = startYear; i <= endYear; i++) {
            calyear.options[i - startYear] = new Option(i,i);
        }
        calyear.options.length = endYear - startYear + 1;
    }

    if(document.all) {
        // IE.
        if(dataEntryFormDiv!=null)
    	{
    		var ofy=dataEntryFormDiv.scrollTop;
            var ofx=dataEntryFormDiv.scrollLeft;
    	}
    	else
    	{
    		var ofy=document.body.scrollTop;
            var ofx=document.body.scrollLeft;
    	}
        document.getElementById(divId).style.left = event.clientX+ofx+10;
        document.getElementById(divId).style.top = event.clientY+ofy+10;
        document.getElementById(divId).style.visibility="visible";
        if (month!=0)
        {
            document.getElementById(calmoisId).selectedIndex= month;
        }
        //hideElement("SELECT",id);
    } else if(document.layers) {
        // Netspace 4
        document.elements[divId].left = e.pageX+10;
        document.elements[divId].top = e.pageY+10;
        document.elements[divId].visibility="visible";
        if (month!=0)
        {
            document.elements[divId].document.caltitre.document.forms[0].calmois.selectedIndex=month;
        }
    } else {
        // Mozilla
        var calendrier = document.getElementById(divId);
        if(dataEntryFormDiv!=null)
    	{
        	var ofy=dataEntryFormDiv.scrollTop;
            var ofx=dataEntryFormDiv.scrollLeft;
    	}
    	else
    	{
    		var ofy=document.body.scrollTop;
            var ofx=document.body.scrollLeft;
    	}
        calendrier.style.left = event.clientX+ofx+10;
        calendrier.style.top = event.clientY+ofy+10;
        calendrier.style.visibility="visible";
        if (month!=0)
        {
            document.getElementById(calmoisId).selectedIndex=month;
        }
    }
    if (document.forms[formName].elements[formProperty].stlayout) {
        var lc_day = document.forms[formName].elements[formProperty].stlayout.day;
        var lc_month = document.forms[formName].elements[formProperty].stlayout.month;
        var lc_year = parseInt(document.forms[formName].elements[formProperty].stlayout.year);

        if (month!=0 && day!=0)
        {
            cal_chg(id,lc_day, lc_month, lc_year);
        }
    } else {
        if (month!=0 && day!=0)
        {
            cal_chg(id,day, month, year);
        }
    }
    calformname = formName;
    calformelement = formProperty;
    calpattern = pattern;
}

/**
 * Redraw the calendar for the current date and a selected month
 */
function cal_chg(id,day, month, year){
    var str='',j;
    var calmoisId = "calmois"+id;
    var calyearId = "calyear"+id;
    champMonth = document.getElementById(calmoisId);
    if (month==null) {
        month = champMonth.options[champMonth.selectedIndex].value;
    } else {
        champMonth.selectedIndex = month;
    }

    champYear = document.getElementById(calyearId);
    if (year==null) {
        year = champYear.options[champYear.selectedIndex].value;
    } else {
        index = year - champYear.options[0].value;
        if (index >= 0 && index < champYear.options.length) {
            champYear.selectedIndex = index;
        } else {
            // the initial year is not in the calendar allowed years.
            year = champYear.options[0].value;
        }
    }

    if(month>0) {
        j=1;
        weekEnd1Pos = (1 - calweekstart + 7) % 7;
        weekEnd2Pos = (7 - calweekstart + 7) % 7;

        str+='<table cellpadding=0 cellspacing=0 border=0 width=267 bgcolor="#BDDBF3">\n';
        for(u=0;u<6;u++){
            str+='  <tr>\n';
            for(i=0;i<7;i++){
                ldt=new Date(year,month-1,j);
                str+='      <td class="CALENDARBORDER" width=1><img src="' + imgsrc + 'calBackground.gif" width=1 height=20></td>\n';

                str+='      <td class="CALENDAR';
                if(ldt.getDate()==j && j==day) {
                    str+='SELECTED';
                } else if(i==weekEnd1Pos || i==weekEnd2Pos) {
                    str+='WEEKEND';
                } else {
                    str+='WEEK';
                }
                str+='" width="38" align="center">';
                if ((ldt.getDay()+1-calweekstart+7)%7==i && ldt.getDate()==j) {
                    str+='<a class="CALENDRIER" href="javascript://" class="CALENDRIER" onmousedown="dtemaj(\''+ id +'\',\'' + j + '\',\'' + month + '\',\'' + year +'\');">'+j+'</a>';
                    j++;
                } else {
                    str+='&nbsp;';
                }
                str+='</td>\n';
            }
            str+='      <td class="CALENDARBORDER" width=1><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>\n';
            str+='  </tr>\n';
            str+='  <tr><td colspan=15 class="CALENDARBORDER"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td></tr>\n';
        }
        str+='</table>\n';
    }

    var caljourId = "caljour"+id;

    if(document.all) {
        document.getElementById(caljourId).innerHTML=str;
    }
    if(document.layers) {
        obj=document.calendrier.document.caljour;
        obj.top=48;
        obj.document.write(str);
        obj.document.close();
    }
    if (!document.all && document.getElementById) {
        document.getElementById(caljourId).innerHTML = str;
    }
}

/**
 * Display the previous month
 */
function cal_before(id,day, month, year) {
    var champMonth, champYear;
    var calmoisId= "calmois"+id;
    var calyearId ="calyear"+id;
    champMonth = document.getElementById(calmoisId);
    champYear = document.getElementById(calyearId);

    if (champMonth.selectedIndex>1) {
        champMonth.selectedIndex--;
    } else if (champYear.selectedIndex>0) {
        champYear.selectedIndex--;
        champMonth.selectedIndex = champMonth.options.length - 1;
    }
    cal_chg(id,day, champMonth.options[champMonth.selectedIndex].value, champYear.options[champYear.selectedIndex].value);
}

/**
 * Display the previous year
 */
function cal_previous_year(id,day, month, year)
{
    var champMonth, champYear;
    var calmoisId= "calmois"+id;
    var calyearId ="calyear"+id;
    champMonth = document.getElementById(calmoisId);
    champYear = document.getElementById(calyearId);

    if (champYear.selectedIndex>=1)
    {
        champYear.selectedIndex--;
    }
    else
    {
        champYear.selectedIndex = champYear.options.length - 1;
    }
    cal_chg(id,day, champMonth.options[champMonth.selectedIndex].value, champYear.options[champYear.selectedIndex].value);
}

/**
 * Display the next month
 */
function cal_after(id,day, month, year)
{
    // get required objects
    var champMonth, champYear;
    var calmoisId ="calmois"+id;
    var calyearId ="calyear"+id;
    champMonth = document.getElementById(calmoisId);
    champYear = document.getElementById(calyearId);
    if (champMonth.selectedIndex < champMonth.options.length - 1) {
        champMonth.selectedIndex++;
    } else if (champYear.selectedIndex < champYear.options.length - 1) {
        champYear.selectedIndex++;
        champMonth.selectedIndex = 1;
    }
    cal_chg(id,day, champMonth.options[champMonth.selectedIndex].value, champYear.options[champYear.selectedIndex].value);
}


/**
 * Display the next month
 */
function cal_after_year(id,day, month, year)
{
    // get required objects
    var champMonth, champYear;
    var calmoisId = "calmois"+id;
    var calyearId = "calyear"+id;
    champMonth = document.getElementById(calmoisId);
    champYear = document.getElementById(calyearId);
    if (champYear.selectedIndex < champYear.options.length - 1)
    {
        champYear.selectedIndex++;
    }
    else
    {
        champYear.selectedIndex = 0;
    }
    cal_chg(id,day, champMonth.options[champMonth.selectedIndex].value, champYear.options[champYear.selectedIndex].value);
}

/**
 * Update the date in the input field and hide the calendar.
 * PENDING: find a way to make the format customable.
 */
function dtemaj(id,jour, mois, annee){
    document.forms[calformname].elements[calformelement].value = formatDate(jour, mois, annee, id);
    document.forms[calformname].elements[calformelement].stlayout = new Object();
    document.forms[calformname].elements[calformelement].stlayout.day = jour;
    document.forms[calformname].elements[calformelement].stlayout.month = mois;
    document.forms[calformname].elements[calformelement].stlayout.year = annee;
    hideCalendar(id);
}


function formatDate(day, month, year, id) {
    var date = "";
    var pos = 0;
    var pattern;
    var previousPattern;
    var patternLength = 0;
    if (calpattern!=null && calpattern.length>0) {
        previousPattern = calpattern.charAt(0);
        while (pos <= calpattern.length) {
            if (pos < calpattern.length) {
                pattern = calpattern.charAt(pos);
            }  else {
                pattern = "";
            }
            if (pattern != previousPattern) {
                switch (previousPattern) {
                    case 'y':
                        date += padYear(year, patternLength);
                        break;
                    case 'M':
                        date += padNumber(month, patternLength);
                        break;
                    case 'd':
                        date += padNumber(day, patternLength);
                        break;
                    case '\'':
                        // PENDING
                        break;
                    default:
                        date += previousPattern;
                }
                previousPattern = pattern;
                patternLength = 0;
            }
            patternLength++;
            pos++;
        }
    }

	if (shouldUseTime =='true')
	{
		var selectedHrs = document.getElementById('timeHrs'+id);
		var selectedMin = document.getElementById('timeMin'+id);

		if(selectedHrs!= null && selectedMin !=null && selectedHrs.style.visibility!="hidden" && selectedMin.style.visibility!="hidden")
		{
		   var selHours = padNumber(selectedHrs.value, 2);
		   var selMin = padNumber(selectedMin.value, 2);
		   // date += " " + selectedHrs.value + ":" + selectedMin.value;
		   date += " " + selHours + ":" + selMin;
		}
	}

    return date;
}

function padYear(year, patternLength) {
    if (patternLength==2 && year.length==4) {
        return year.substring(2);
    } else {
        return year;
    }
}

function padNumber(number,length) {
    var str = '' + number;
    while (str.length < length)
        str = '0' + str;
    return str;
}

function hideCalendar(id) {
	currentDivId=null;
    var divId = "slcalcod"+id;
    if(document.all) {
        // IE.
        document.getElementById(divId).style.visibility="hidden";
        showElement("SELECT");
    } else if(document.layers) {
        // Netspace 4
        document.getElementById(divId).visibility="hidden";
    } else {
        // Mozilla
        var calendrier = document.getElementById(divId);
        calendrier.style.visibility="hidden";
    }
    document.getElementById(id).style.color="black";
	isDataChanged();
}

/**
 * Fix IE bug
 */
function hideElement(elmID,id)
{
    var divId = "slcalcod"+id;
    var calmoisId = "calmois"+id;
    var calyearId = "calyear"+id;
    if (!document.all) {
        return;
    }
    x = parseInt(document.getElementById(divId).style.left);
    y = parseInt(document.getElementById(divId).style.top);
    var node = event.srcElement;
    while(node.tagName != "DIV") {
        node = node.parentNode;
        if (node.tagName == 'HTML') break;
    }
    if(node.tagName == "DIV"){
        x+= node.scrollLeft;
        y+=node.scrollTop;
    }
    xxx = 253; // document.all.slcalcod.offsetWidth;
    yyy = 145; // document.all.slcalcod.offsetHeight;

    for (i = 0; i < document.all.tags(elmID).length; i++)
    {
        obj = document.all.tags(elmID)[i];
        if (! obj || ! obj.offsetParent || obj.id==calmoisId || obj.id==calyearId)
            continue;

        // Find the element's offsetTop and offsetLeft relative to the BODY tag.
        objLeft   = obj.offsetLeft;
        objTop    = obj.offsetTop;
        objParent = obj.offsetParent;
        if(obj.style.visibility != "hidden"){
            while (objParent.tagName.toUpperCase() != "BODY")
            {
                objLeft  += objParent.offsetLeft;
                objTop   += objParent.offsetTop;
                objParent = objParent.offsetParent;
            }
        }
        obj.statusVisibility = obj.style.visibility;

        // Adjust the element's offsetTop relative to the dropdown menu
        //objTop = objTop - y;

        if (x > (objLeft + obj.offsetWidth) || objLeft > (x + xxx))
            ;
        else if (objTop > y + yyy)
            ;
        else if (y > (objTop + obj.offsetHeight))
            ;
        else
             if(obj.statusVisibility != "hidden"){
                  obj.style.visibility = "hidden";
             }
    }
}

/**
 * Fix IE bug
 */
function showElement(elmID)
{
    if (!document.all) {
        return;
    }
    for (i = 0; i < document.all.tags(elmID).length; i++)
    {
        obj = document.all.tags(elmID)[i];
        if (! obj || ! obj.offsetParent)
            continue;

        if(obj.statusVisibility != "hidden")
                  obj.style.visibility = "";
    }
}

function processAjaxCalScript(id, day, month, year, imgPath)
{
	var content = processAjaxCalanderScript(id,"Sun","Mon","Tue","Wed","Thu","Fri","Sat",1,"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec", day, month, year, 'false', imgPath);
	return content;
}

function printTimeCalendarForAjax(id,day, month, year, time_hh, time_mm, imgPath)
{
	var content = processAjaxCalanderScript(id,"Sun","Mon","Tue","Wed","Thu","Fri","Sat",1,"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec", day, month, year, 'true',imgPath, 10, 12);
	return content;
}

function printMonthYearCalendarForAjax(id, month, year, imgPath)
{
	var content = processMonthAndYearCalForAjax(id,"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec",year, imgPath);
	return content;
}

function printYearCalendarForAjax(id, year, imgPath)
{
	var content = processYearOnlyCalForAjax(id, year,imgPath);
	return content;
}

/**
 * Static code included one time in the page.
 *
 * a {text-decoration: none; color: #000000;}");
 * TD.CALENDRIER {background-color: #C2C2C2; font-weight: bold; text-align: center; font-size: 10px; }");
 *
 * bgColor => #000000, #C9252C,
 */
function processAjaxCalanderScript(id,day1, day2, day3, day4, day5, day6, day7, first, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12, day, month, year, displayTime,imgsrc, time_hh, time_mm)
{
	var calDiv;
	//var imgsrc ="./../de/images/";
    calDiv ='<div id="caltitre" style="z-index:10;height:20">';
    calDiv = calDiv +'<table cellpadding="0" cellspacing="0" border="0" width="267" height:20" bgcolor="#BDDBF3">';
    calDiv = calDiv +'<tr><td colspan="15"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td></tr>';
    calDiv = calDiv +'<tr>';
    calDiv = calDiv +'    <td class="CALENDARBORDER" width="1"></td>';
    calDiv = calDiv +'    <td class="CALENDARTITLE"  colspan="3" align="right"><img src="' + imgsrc + 'previous2.gif" onclick="cal_previous_year('+ '\''+id +'\''+ ',' + day + ');"></td>';
    calDiv = calDiv +'    <td class="CALENDARTITLE" colspan="1" align="left" width="40"><img src="' + imgsrc + 'previous.gif" onclick="cal_before('+ '\''+id +'\''+ ',' + day + ');"></td>';
    calDiv = calDiv +'    <td colspan=6 align="right" class="CALENDARTITLE" nowrap>';

    // month
   calDiv = calDiv +'<select id="calmois'+id+'" name="calmois'+id+'" onchange="cal_chg('+ '\''+id +'\''+ ',' + day + ');"><option value=0>...</option>';

    // use the good day for week start.
    // store the day the week start for later.
    calweekstart = 1;
    // compute an array of the days, starting from Sunday.
    caldays = new Array(7);
    caldays[0] = "Sun";
    caldays[1] = "Mon";
    caldays[2] = "Tue";
    caldays[3] = "Wed";
    caldays[4] = "Thu";
    caldays[5] = "Fri";
    caldays[6] = "Sat";
    // compute an array of the days, starting at the good day.
    computedcaldays = new Array(7);
    for (i=0; i<7; i++) {
        computedcaldays[(i+1-calweekstart+7)%7] = caldays[i];
    }
    for(i=1;i<=12;i++) {
        var str='<option value=' + i + '>';
        monthIndex = i-1;
        switch (monthIndex) {
            case 0: str += "Jan"; break;
            case 1: str += "Feb"; break;
            case 2: str += "Mar"; break;
            case 3: str += "Apr"; break;
            case 4: str += "May"; break;
            case 5: str += "Jun"; break;
            case 6: str += "Jul"; break;
            case 7: str += "Aug"; break;
            case 8: str += "Sep"; break;
            case 9: str += "Oct"; break;
            case 10: str += "Nov"; break;
            case 11: str += "Dec"; break;
        }
        calDiv = calDiv +str;
    }

    calDiv = calDiv +'</select>';

    // year
    calDiv = calDiv +'<select id="calyear'+id+'" name="calyear'+id+'" onchange="cal_chg('+ '\''+id +'\''+ ',' + day + ');">';
    calDiv = calDiv +"</select>";

    calDiv = calDiv +'    </td>';
    //KK
    calDiv = calDiv +'    <td class="CALENDARTITLE" align="right"><img src="' + imgsrc + 'next.gif" onclick="cal_after('+ '\''+id +'\''+ ',' + day + ');"></td>';
    calDiv = calDiv +'    <td class="CALENDARTITLE" align="left"><img src="' + imgsrc + 'next2.gif" onclick="cal_after_year('+ '\''+id +'\''+ ',' + day + ');"></td>';

    calDiv = calDiv +'    <td class="CALENDARTITLE" align="right"><img src="' + imgsrc + 'close.gif" onclick="hideCalendar('+"'"+id+"'"+')"></td>';
    calDiv = calDiv +'    <td class="CALENDARBORDER" width=1><img src="' + imgsrc + 'calBackground.gif" width="1" height="1"></td>';
    calDiv = calDiv +'</tr>';

    // to display time
    if(shouldUseTime == 'false')
	{
		shouldUseTime = displayTime;
	}
    if(displayTime=='true')
    {
        calDiv = calDiv +'<tr>';
        calDiv = calDiv +'    <td colspan="5" class="CALENDARBORDER" align="right">Time &nbsp; </td>';
        calDiv = calDiv +'<td colspan="5" class="CALENDARBORDER" align="left"><select id="timeHrs'+id+'" name="time_hh'+id+'" size="1">';
        var str="";
        for (i = 0; i <= 23; i++)
        {
            str += '<option value=' + i + '>' + i + '</option>';
        }
        calDiv = calDiv +str;
        calDiv = calDiv +'    </select>';
        calDiv = calDiv +'    &nbsp;:&nbsp; <select id="timeMin'+id+'" name="time_mm'+id+'" size="1">';
        str="";
        for (i = 0; i <= 59; i++)
        {
            str += '<option value=' + i + '>' + i + '</option>';
        }
        calDiv = calDiv +str;
        calDiv = calDiv +'    </select></td>';

        calDiv = calDiv +'    <td colspan=5 class="CALENDARBORDER" align="left">[HH : MM]</td>';
        calDiv = calDiv +'</tr>';
    }


    calDiv = calDiv +'<tr><td colspan=15 class="CALENDARBORDER"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td></tr>';
    calDiv = calDiv +'<tr>';
    calDiv = calDiv +'    <td class="CALENDARBORDER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="38" align="center">' + computedcaldays[0] + '</td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="50"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="10">'+"  " + computedcaldays[1] + '</td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="10">' + computedcaldays[2] + '</td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="38">' + computedcaldays[3] + '</td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="38">' + computedcaldays[4] + '</td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="38">' + computedcaldays[5] + '</td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calDiv = calDiv +'    <td class="CALENDRIER" width="38">' + computedcaldays[6] + '</td>';
    calDiv = calDiv +'    <td class="CALENDARBORDER" width="1"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td>';
    calDiv = calDiv +'</tr>';

    calDiv = calDiv +'<tr><td colspan=15 class="CALENDARBORDER"><img src="' + imgsrc + 'calBackground.gif" width=1 height=1></td></tr>';
//  calDiv = calDiv +'</form>);
    calDiv = calDiv +'</table>';
    calDiv = calDiv +'</div>';
//  calDiv = calDiv +'<div id="caljour" style="position:absolute; left:0px; top:45px; width:253; height:130; z-index:10;"></div>');
    calDiv = calDiv +'<div id="caljour'+id+'" style="z-index:10;"></div>';
	return calDiv
}

function processMonthAndYearCalForAjax(id,month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12, year,imgsrc)
{
	var calDivForMonthYear;
	//var imgsrc ="./../de/images/";
	calDivForMonthYear = '<table cellpadding="0" cellspacing="0" border="0" width="150" height="20" bgcolor="#BDDBF3">';
    calDivForMonthYear = calDivForMonthYear + '<tr>';
    calDivForMonthYear = calDivForMonthYear + '    <td class="CALENDARBORDER" width="5"></td>';
    calDivForMonthYear = calDivForMonthYear + '    <td align="center" width="*" class="CALENDARTITLE">';
    calDivForMonthYear = calDivForMonthYear + '<select id="calmois'+id+'" name="calmois'+id+'"><OPTION selected>Jan</OPTION><OPTION>Feb</OPTION><OPTION>Mar</OPTION><OPTION>Apr</OPTION><OPTION>May</OPTION><OPTION>Jun</OPTION><OPTION>Jul</OPTION><OPTION>Aug</OPTION><OPTION>Sep</OPTION><OPTION>Oct</OPTION><OPTION>Nov</OPTION><OPTION>Dec</OPTION>';
    calDivForMonthYear = calDivForMonthYear + '</select>';
    calDivForMonthYear = calDivForMonthYear + '</td>';
    calDivForMonthYear = calDivForMonthYear + '    <td align="center" width="10" class="CALENDARTITLE" nowrap>';
    calDivForMonthYear = calDivForMonthYear + '<select id="calyear'+id+'" name="calyear'+id+'" onload="showYears(id)" onChange="hideMonthAndYearCal('+"'"+id+"'"+')"><OPTION selected>1900</OPTION>';
    calDivForMonthYear = calDivForMonthYear + '</select>';
    calDivForMonthYear = calDivForMonthYear + '</td>';
    calDivForMonthYear = calDivForMonthYear + '    <td class="CALENDARBORDER" width="5"></td>';
    calDivForMonthYear = calDivForMonthYear + '    <td class="CALENDARTITLE" width="10" align="right"><img src="' + imgsrc + 'close.gif" onclick="hideMonthAndYearCal('+"'"+id+"'"+')"></td>';
    calDivForMonthYear = calDivForMonthYear + '</tr>';
    calDivForMonthYear = calDivForMonthYear + '</table>';
	return calDiv;
}

function processYearOnlyCalForAjax(id, year,imgsrc)
{
	var calDivYearOnly;
	//var imgsrc ="./../de/images/";
	calDivYearOnly = '<table cellpadding="0" cellspacing="0" border="0" width="100" height="20" bgcolor="#BDDBF3">';
	calDivYearOnly = calDivYearOnly + '<tr>';
	calDivYearOnly = calDivYearOnly + '    <td class="CALENDARBORDER" width="2"></td>';
	calDivYearOnly = calDivYearOnly + '    <td align="center" width="*" class="CALENDARTITLE">';
    calDivYearOnly = calDivYearOnly + '</td>';
    calDivYearOnly = calDivYearOnly + '    <td align="center" width="*" class="CALENDARTITLE" nowrap>';
    calDivYearOnly = calDivYearOnly + '<select id="calyear'+id+'" name="calyear'+id+'" onload="showYears(id)" nChange="hideYearOnlyCal('+"'"+id+"'"+')"><OPTION selected>1900</OPTION>';
    calDivYearOnly = calDivYearOnly + '</select>';
    calDivYearOnly = calDivYearOnly + '</td>';
    calDivYearOnly = calDivYearOnly + '    <td class="CALENDARBORDER" width="2"></td>';
    calDivYearOnly = calDivYearOnly + '    <td class="CALENDARTITLE" width="10" align="right"><img src="' + imgsrc + 'close.gif" onclick="hideYearOnlyCal('+"'"+id+"'"+')"></td>';
    calDivYearOnly = calDivYearOnly + '</tr>';
    calDivYearOnly = calDivYearOnly + '</table>';
	return calDiv;
}