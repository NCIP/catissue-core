
angular.module('os.administrative.order.list', ['os.administrative.models'])
  .controller('OrderListCtrl', function($scope, $state, DistributionOrder) {

    function init() {
      $scope.orders = [];
      DistributionOrder.query({includeStats: true}).then(
        function(orders) {
          $scope.orders = orders;
        }
      );
    }
    
    init();
  });
