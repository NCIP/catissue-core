
angular.module('os.administrative.dp.history', ['os.administrative.models'])
  .controller('DpHistoryCtrl', function($scope, distributionProtocol) {
    $scope.exportUrl = distributionProtocol.historyExportUrl();
  
    function loadOrders() {
      distributionProtocol.getOrderHistory().then(function(orders) {
        $scope.orders = orders;
      });
    }
    
    loadOrders();
  });
