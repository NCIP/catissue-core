
angular.module('os.administrative.order.list', ['os.administrative.models'])
  .controller('OrderListCtrl', function(
    $scope, $state, $filter, 
    DistributionOrder, DistributionProtocol, Institute, Util) {

    var pvsLoaded = false;

    function init() {
      $scope.orders = [];
      $scope.dps = [];
      $scope.instituteNames = [];
      $scope.filterOpts = {};

      loadOrders($scope.filterOpts);
      Util.filter($scope, 'filterOpts', loadOrders);
    }

    function loadOrders(filterOpts) {
      var opts = {
        includeStats: true,
        query: filterOpts.title,
        receivingInstitute: filterOpts.receivingInstitute
      };

      if (!!filterOpts.executionDate) {
        opts.executionDate = $filter('date')(filterOpts.executionDate, 'yyyy-MM-dd');
      }

      if (!!filterOpts.requestor) {
        opts.requestorId = filterOpts.requestor.id;
      }

      if (!!filterOpts.dp) {
        opts.dpId = filterOpts.dp.id;
      }

      DistributionOrder.query(opts).then(
        function(orders) {
          $scope.orders = orders;
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
