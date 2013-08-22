<script language="JavaScript" type="text/javascript" src="jss/highCharts/jquery-1.9.1.js"></script>

<script src="jss/highCharts/highstock.js"></script>
<script src="jss/highCharts/exporting.js"></script>
<link rel="STYLESHEET" type="text/css" href="dhtmlx_suite/css/dhtmlxtabbar.css">
	<script  src="dhtmlx_suite/js/dhtmlxcommon.js"></script>
	<script  src="dhtmlx_suite/js/dhtmlxtabbar.js"></script>
	<script src="dhtmlx_suite/js/dhtmlxcontainer.js" type="text/javascript"></script>
<body onload="onGraphPageLoad()">	
<div id="containerGraphtabbar" style = "width:100%;height:100%;">
<div id="specimenCountTab" name="Specimen Count">
	<!--div id="containerSpecimenGraphDiv" style="width: 100%; height: 600px;"></div-->
</div>
<div id="percentTab" name="Percent Utilization">
	<!--div id="containerPercentGraphDiv" style="width: 100%; height: 600px;"></div-->
</div>
</div>
</body>
<script>
	var siteName = getQueryParam("siteName");
	function loadGraph(){
				Highcharts.setOptions({
									global: {
										useUTC: false
									}
							});
				var options = {
					chart: {
						 type: 'spline',
						zoomType: 'x'
					},
					legend: {
						enabled :true
					},
					rangeSelector: {
						buttons: [{
						type: 'day',
						count: 3,
						text: '3d'
						}, {
						type: 'week',
						count: 1,
						text: '1w'
						}, {
						type: 'month',
						count: 1,
						text: '1m'
						}, {
						type: 'month',
						count: 6,
						text: '6m'
						}, {
						type: 'year',
						count: 1,
						text: '1y'
						}, {
						type: 'all',
						text: 'All'
						}],
						selected: 4
					},
					title: {
						text: 'Daywise Storage Container Usage Chart'
					},
					xAxis: {
						title: {
							text: 'Date'
						},
						type: 'datetime',
						dateTimeLabelFormats: {
							day: '%b-%e<br/>%Y'
						}
						
					}
					
				};
				$.ajax({
						type: "POST",
						url: "NewStorageContainerAjaxAction.do?type=getStorageContainerDataForGraph&graphType=specimenCount&siteName="+siteName,
						dataType: 'json',
						success: function(returnData){
							var xdata = eval('('+returnData.data+')');
							var cnt =0;
							options.series=[];
							options.subtitle = {
								text: 'Number Of Specimens v/s Date Stored'
							};
							options.yAxis= {
								title: {
									text: 'Specimen Count'
								},
								lineWidth: 2};
							while(xdata[cnt]!=undefined){
								var dataObj = {
									name: xdata[cnt].name,
									data: xdata[cnt].data,
									pointStart: Date.UTC(2004, 3, 1),
									pointInterval: 3600 * 1000,
									tooltip: {
										valueDecimals: 1
									}
								}
								options.series[cnt]=dataObj;
								cnt++;
							}
						
						$('#containerSpecimenGraphDiv').highcharts('StockChart', options);
						tabbar.setTabActive('specimenCountTab');
						
						}
					});
				


				$.ajax({
						type: "POST",
						url: "NewStorageContainerAjaxAction.do?type=getStorageContainerDataForGraph&graphType=percentage&siteName="+siteName,
						dataType: 'json',
						success: function(returnData){
							var data = eval('('+returnData.data+')');
							var cnt =0;
							options.series=[];
							options.subtitle = {
								text: 'Percentage Utilization v/s Date'
							};
							options.yAxis= [{
								title: {
									text: 'Percentage'
								},
								min:0,max:100,
								plotLines : [ {
									value : returnData.redLineValue,
									color : 'red',
									dashStyle : 'shortdash',
									width : 2,
									label : {
										text : '90% Utilization'
									}
								}],
								lineWidth: 2}];
							while(data[cnt]!=undefined){
								var dataObj = {
									name: data[cnt].name,
									data: data[cnt].data,
									pointStart: Date.UTC(2004, 3, 1),
									pointInterval: 3600 * 1000,
									tooltip: {
										valueDecimals: 1
									}
								}
								options.series[cnt]=dataObj;
								cnt++;
							}
						
						$('#containerPercentGraphDiv').highcharts('StockChart', options);
						
						
						}
					});
			
		
	  }  
	function getQueryParam(param) {
		var result =  window.location.search.match(
			new RegExp("(\\?|&)" + param + "(\\[\\])?=([^&]*)")
		);

		return result ? result[3] : null;
	}

    var tabbar=new dhtmlXTabBar("containerGraphtabbar","top");
	function onGraphPageLoad(){
		tabbar.setSkin("dhx_skyblue");
		tabbar.setImagePath("dhtmlx_suite/imgs/");
		tabbar.addTab("specimenCountTab","Specimen Count","120px");
		tabbar.addTab("percentTab","Percent Utilization","150px");
		tabbar.setContentHTML("specimenCountTab",'<div style="overflow-y: scroll;width: 100%; height: 100%;"><div id="containerSpecimenGraphDiv" style="width: 100%;"></div></div>');
		tabbar.setContentHTML("percentTab",'<div style="overflow-y: scroll;width: 100%; height: 100%;"><div id="containerPercentGraphDiv" style="width: 100%;"></div></div>');
		tabbar.enableScroll(true);
	  //  tabbar.setTabActive('specimenCountTab');
		loadGraph();
	}
</script>