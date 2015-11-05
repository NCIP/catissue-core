
angular.module('os.administrative.shippingorder.list', ['os.administrative.models'])
  .controller('ShippingOrderListCtrl', function(
    $scope, ShippingOrder, Institute, Util) {
  
    function init() {
      $scope.filterOpts = {};

      loadInstitutes();
      loadOrders($scope.filterOpts);
      Util.filter($scope, 'filterOpts', loadOrders);
    }

    function loadOrders(filterOpts) {
      ShippingOrder.query(filterOpts).then(
        function(result) {
          $scope.orders = result; 
        }
      );
    }
 
    function loadInstitutes() {
      Institute.query().then(
        function(institutes) {
          $scope.instituteNames = Institute.getNames(institutes);
        }
      );
    }

    $scope.clearFilters = function() {
      $scope.filterOpts = {};
    }

    init();
  });
