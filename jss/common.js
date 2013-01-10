function doInitCalendar(objName, defaultDate) {
	var date = document.getElementById(objName).value;

	cal = new dhtmlxCalendarObject(objName, true, {
		isMonthEditable : true,
		isYearEditable : true
	});
	cal.setYearsRange(1901, 2500);
	cal.setDateFormat('%m-%d-%Y');
	cal.setSkin("dhx_skyblue");
	if (date.length > 0) {
		cal.setDate(date);
	}
	else if (defaultDate)
	{
	cal.setDate (new Date());
	}
	return cal;
}