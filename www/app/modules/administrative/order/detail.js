angular.module('os.administrative.order.detail', ['os.administrative.models'])
  .controller('OrderDetailCtrl', function($scope, order) {
  
    function init() {
      $scope.order = order;
    }

    init();
  });
