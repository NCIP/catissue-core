
angular.module('os.administrative.order.list', ['os.administrative.models'])
  .controller('OrderListCtrl', function(
    $scope, $state,
    DistributionOrder, DistributionProtocol, Institute, Util, ListPagerOpts) {

    var pvsLoaded = false;
    var pagerOpts;

    function init() {
      $scope.orders = [];
      $scope.dps = [];
      $scope.instituteNames = [];
      $scope.filterOpts = {};
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getOrdersCount});

      loadOrders($scope.filterOpts);
      Util.filter($scope, 'filterOpts', loadOrders);
    }

    function loadOrders(filterOpts) {
      DistributionOrder.list(filterOpts).then(
        function(orders) {
          $scope.orders = orders;
          pagerOpts.refreshOpts(orders);
        }
      );
    }


    function loadSearchPvs() {
      if (pvsLoaded) {
        return;
      }

      loadDps();
      loadInstitutes();
      pvsLoaded = true;
    }

    function loadDps(title) {
      DistributionProtocol.query({query: title}).then(
        function(dps) {
          $scope.dps = dps;
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

    function getOrdersCount() {
      return DistributionOrder.getOrdersCount($scope.filterOpts);
    }
    
    $scope.loadSearchPvs = loadSearchPvs;

    $scope.loadDps = loadDps;

    $scope.clearFilters = function() {
      $scope.filterOpts = {};
    }

    $scope.showOrderOverview = function(order) {
      $state.go('order-detail.overview', {orderId: order.id});
    }

    init();
  });
