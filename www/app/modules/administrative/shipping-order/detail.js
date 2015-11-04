angular.module('os.administrative.shippingorder.detail', ['os.administrative.models'])
  .controller('ShippingOrderDetailCtrl', function($scope, order) {

    function init() {
      $scope.order = order;
    }

    init();
  });
