
angular.module('os.administrative.dp.history', ['os.administrative.models'])
  .controller('DpHistoryCtrl', function($scope, $sce, distributionProtocol, DistributionProtocol) {
    $scope.exportUrl = $sce.trustAsResourceUrl(distributionProtocol.historyExportUrl());
  
    function loadOrders() {
      var opts = {
        dpId: distributionProtocol.id,
        groupBy: 'specimenType,anatomicSite,pathologyStatus'
      };
      
      DistributionProtocol.getOrders(opts).then(
        function(orders) {
          $scope.orders = orders;
        }
      );
    }
    
    loadOrders();
  });
