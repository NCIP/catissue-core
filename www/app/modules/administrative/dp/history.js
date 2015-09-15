
angular.module('os.administrative.dp.history', ['os.administrative.models'])
  .controller('DpHistoryCtrl', function($scope, $sce, distributionProtocol, DistributionOrder) {
    $scope.exportUrl = $sce.trustAsResourceUrl(distributionProtocol.historyExportUrl());
  
    function loadOrders() {
      var opts = {
        dpId: distributionProtocol.id,
        specimenType: true,
        anatomicSite: true,
        pathologyStatus: true
      };
      
      DistributionOrder.getOrderSpecifications(opts).then(function(orders) {
        $scope.orders = orders;
      });
    }
    
    loadOrders();
  });
