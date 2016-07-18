
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
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getOrdersCount, recordsPerPage: 50});
      $scope.filterOpts = {maxResults: pagerOpts.recordsPerPage + 1};

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
      $scope.filterOpts = {maxResults: pagerOpts.recordsPerPage + 1};
    }

    $scope.showOrderOverview = function(order) {
      $state.go('order-detail.overview', {orderId: order.id});
    }

    init();
  });
