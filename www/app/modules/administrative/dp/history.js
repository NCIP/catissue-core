
angular.module('os.administrative.dp.history', ['os.administrative.models'])
  .controller('DpHistoryCtrl', function($scope, distributionProtocol) {
    $scope.orders = [];
  
    distributionProtocol.getOrderHistory().then(function(orders){
      $scope.orders = orders;
    })
  });
