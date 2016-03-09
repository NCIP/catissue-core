angular.module('os.administrative.order.detail', ['os.administrative.models'])
  .controller('OrderDetailCtrl', function($scope, order, Util) {
  
    function init() {
      $scope.order = order;
      $scope.rptTmplConfigured = false;
      if (!!order.distributionProtocol.report && !!order.distributionProtocol.report.id) {
        $scope.rptTmplConfigured = true;
      }
    }

    $scope.downloadReport = function() {
      Util.downloadReport(order, 'orders');
    };

    init();
  });
