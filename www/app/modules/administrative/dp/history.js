
angular.module('os.administrative.dp.history', ['os.administrative.models'])
  .controller('DpHistoryCtrl', function($scope, $sce, distributionProtocol) {
    $scope.exportUrl = $sce.trustAsResourceUrl(distributionProtocol.historyExportUrl());
  
    function loadOrders() {
      distributionProtocol.getOrderHistory().then(function(orders) {
        $scope.orders = orders;
      });
    }
    
    loadOrders();
  });
