<script language="JavaScript" type="text/javascript" src="jss/highCharts/jquery-1.9.1.js"></script>
<script src="jss/highCharts/highcharts.js"></script>
<script src="jss/highCharts/exporting.js"></script>


<div id="containerSpecimenGraphDiv" style="width: 100%; height: 100%;"></div>

<script>

var siteName = getQueryParam("siteName");

$(document).ready(function() {

		Highcharts.setOptions({
							global: {
								useUTC: false
							}
					});
        
		var options = {
            chart: {
				renderTo: 'containerSpecimenGraphDiv',
                defaultSeriesType: 'line',
				zoomType: 'x'
            },
            title: {
                text: 'Daywise Storage Container Usage Chart'
            },
            subtitle: {
                text: 'Number Of Specimens v/s Date Stored'
            },
            xAxis: {
				title: {
                    text: 'Date'
                },
				maxZoom: 3 * 24 * 3600000,
				showLastLabel: true,
				type: 'datetime',
				tickInterval: 24*3600*1000,
                dateTimeLabelFormats: {
                    day: '%b-%e<br/>%Y'
                },
				labels: {
                    rotation: -45,
                    align: 'right'
                    }
            },
            yAxis: {
                title: {
                    text: 'Specimen Count'
                },
                min: 0
            },
            tooltip: {
                formatter: function() {
                        return '<span style="color:'+this.series.color+';">'+this.series.name+'</span><br/>'+Highcharts.dateFormat('%b-%e-%Y', this.x)+'<br/><b>Specimens: '+ this.y+'</b>';
                }
            },
            
            series: []
        };
		$.ajax({
				type: "POST",
				url: "NewStorageContainerAjaxAction.do?type=getStorageContainerSpecCountDataForGraph&siteName="+siteName,
				dataType: 'json',
				success: function(data){
					options.series=data;
					var chart = new Highcharts.Chart(options); 
					}
				});
    });
    
function getQueryParam(param) {
    var result =  window.location.search.match(
        new RegExp("(\\?|&)" + param + "(\\[\\])?=([^&]*)")
    );

    return result ? result[3] : null;
}

</script>