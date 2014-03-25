<script language="JavaScript" type="text/javascript" src="jss/highCharts/jquery-1.9.1.js"></script>

<script src="jss/highCharts/highstock.js"></script>
<script src="jss/highCharts/highcharts.js"></script>
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

<div id="sitUtilizationGraph" style = "width:100%;height:100%;">
<div id="specimenCountForSiteTab" name="Specimen Count">
	<!--div id="containerSpecimenGraphDiv" style="width: 100%; height: 600px;"></div-->
</div>
</div>

</body>
<script>
	var siteName = getQueryParam("siteName");
	function loadGraph(){
	if(siteName != undefined && siteName != null){
	document.getElementById("containerGraphtabbar").style.display = "block";
	document.getElementById("sitUtilizationGraph").style.display = "none";
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
						url: "NewStorageContainerGraphAjaxAction.do?type=getStorageContainerDataForGraph&graphType=specimenCount&siteName="+siteName,
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
						
						$('#containerSpecimenGraphDiv').highcharts( options);
						tabbar.setTabActive('specimenCountTab');
						
						}
					});
				


				$.ajax({
						type: "POST",
						url: "NewStorageContainerGraphAjaxAction.do?type=getStorageContainerDataForGraph&graphType=percentage&siteName="+siteName,
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
						
						$('#containerPercentGraphDiv').highcharts( options);
						
						
						}
					});
			
		}else{
			document.getElementById("containerGraphtabbar").style.display = "none";
			document.getElementById("sitUtilizationGraph").style.display = "block";
			
			
				$.ajax({
						type: "POST",
						url: "NewStorageContainerGraphAjaxAction.do?type=getStorageContainerDataForGraph&graphType=percentage&siteName=",
						dataType: 'json',
						success: function(returnData){
							var data = eval('('+returnData.data+')');
							$(function () {
								$('#specimenCountForSiteTab').highcharts({
									chart: {
										type: 'column'
									},
									title: {
										text: 'Site Utilization Chart'
									},
									xAxis: {
										categories: data.siteName
									},
									yAxis: {
										min: 0,
										title: {
											text: 'Specimen Count'
										},
										stackLabels: {
											enabled: true,
											style: {
												fontWeight: 'bold',
												color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
											}
										}
									},
									legend: {
										align: 'right',
										x: -70,
										verticalAlign: 'top',
										y: 20,
										floating: true,
										backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColorSolid) || 'white',
										borderColor: '#CCC',
										borderWidth: 1,
										shadow: false
									},
									tooltip: {
										formatter: function() {
											return '<b>'+ this.x +'</b><br/>'+
												this.series.name +': '+ this.y +'<br/>'+
												'Total: '+ this.point.stackTotal;
										}
									},
									plotOptions: {
										column: {
											stacking: 'normal',
											dataLabels: {
												enabled: true,
												color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
												style: {
													textShadow: '0 0 3px black, 0 0 3px black'
												}
											}
										}
									},
									series: [{
										name: 'Free Positions',
										//color:'#C8CDCF',
										data: data.availablePosition
									}, {
										name: 'Occupied Positions',
										//color:'#8E9191',
										data: data.ocupiedPosition
									}]
								});
							});
						
							
							
						}});
	
			
	}

			
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