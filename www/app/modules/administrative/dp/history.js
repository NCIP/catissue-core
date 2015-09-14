
angular.module('os.administrative.dp.history', ['os.administrative.models'])
  .controller('DpHistoryCtrl', function($scope, $sce, distributionProtocol) {
    $scope.exportUrl = $sce.trustAsResourceUrl(distributionProtocol.historyExportUrl());
  
    function loadOrders() {
      var opts = {
        specimenType: true,
        anatomicSite: true,
        pathologyStatus: true
      };
      distributionProtocol.getOrderHistory(opts).then(function(orders) {
        $scope.orders = orders;
      });
    }
    
    loadOrders();
  });
