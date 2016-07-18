
angular.module('os.administrative.dp.list', ['os.administrative.models'])
  .controller('DpListCtrl', function($scope, $state, DistributionProtocol, Util, PvManager, ListPagerOpts) {

    var pagerOpts;

    function init() {
      pagerOpts = $scope.pagerOpts = new ListPagerOpts({listSizeGetter: getDpsCount});
      $scope.dpFilterOpts = {includeStats: true, maxResults: pagerOpts.recordsPerPage + 1};
      loadDps($scope.dpFilterOpts);
      Util.filter($scope, 'dpFilterOpts', loadDps);
      loadActivityStatuses();
    }
    
    function loadDps(filterOpts) {
      DistributionProtocol.query(filterOpts).then(
        function(result) {
          $scope.distributionProtocols = result;
          pagerOpts.refreshOpts(result);
        }
      );
    }

    function getDpsCount() {
      return DistributionProtocol.getCount($scope.dpFilterOpts);
    }

    $scope.showDpOverview = function(distributionProtocol) {
      $state.go('dp-detail.overview', {dpId: distributionProtocol.id});
    };
    
    function loadActivityStatuses () {
      PvManager.loadPvs('activity-status').then(
        function (result) {
          $scope.activityStatuses = [];
          angular.forEach(result, function (status) {
            if (status != 'Disabled' && status != 'Pending') {
              $scope.activityStatuses.push(status);
            }
          });
        }
      );
    }

    init();
  });
