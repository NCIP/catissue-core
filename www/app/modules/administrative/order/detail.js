angular.module('os.administrative.order.detail', ['os.administrative.models'])
  .controller('OrderDetailCtrl', function($scope, order, QueryExecutor, Alerts) {
  
    function init() {
      $scope.order = order;
      $scope.rptTmplConfigured = false;
      if (!!order.distributionProtocol.report && !!order.distributionProtocol.report.id) {
        $scope.rptTmplConfigured = true;
      }
    }

    $scope.downloadReport = function() {
      var alert = Alerts.info('orders.report_gen_initiated', {}, false);
      order.generateReport().then(
        function(result) {
          Alerts.remove(alert);
          if (result.completed) {
            Alerts.info('orders.downloading_report');
            QueryExecutor.downloadDataFile(result.dataFile, order.name + '.csv');
          } else if (result.dataFile) {
            Alerts.info('orders.report_will_be_emailed');
          }
        },

        function() {
          Alerts.remove(alert);
        }
      );
    };

    init();
  });
