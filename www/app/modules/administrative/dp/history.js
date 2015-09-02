
angular.module('os.administrative.dp.history', ['os.administrative.models'])
  .controller('DpHistoryCtrl', function($scope, DistributionOrder, dpId) {
    $scope.orders = [];
    
    var opts = {
      includeStats: true,
      dpId: dpId
    };

    DistributionOrder.query(opts).then(
      function(orders){
        $scope.orders = orders;
      }
    )
  });
