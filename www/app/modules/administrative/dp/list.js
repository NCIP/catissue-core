
angular.module('os.administrative.dp.list', ['os.administrative.models'])
  .controller('DpListCtrl', function($scope, $state, DistributionProtocol, Util, PvManager) {

    function init() {
      $scope.dpFilterOpts = {includeStats: true};
      loadDps($scope.dpFilterOpts);
      Util.filter($scope, 'dpFilterOpts', filter);
      loadActivityStatuses();
    }
    
    function loadDps(filterOpts) {
      DistributionProtocol.query(filterOpts).then(
        function(result) {
          $scope.distributionProtocols = result; 
        }
      );
    }

    function filter(filterOpts) {
      var dpFilterOpts = angular.copy(filterOpts);
      if (dpFilterOpts.pi) {
        dpFilterOpts.piId = dpFilterOpts.pi.id;
        delete dpFilterOpts.pi;
      }

      loadDps(dpFilterOpts);
    }

    $scope.showDpOverview = function(distributionProtocol) {
      $state.go('dp-detail.overview', {dpId:distributionProtocol.id});
    };
    
    function loadActivityStatuses () {
      $scope.activityStatuses = PvManager.loadPvs('activity-status').then(
        function (result) {
          $scope.activityStatuses = [].concat(result);
          var idx = $scope.activityStatuses.indexOf('Disabled');
          if (idx != -1) {
            $scope.activityStatuses.splice(idx, 1);
          }
          idx = $scope.activityStatuses.indexOf('Pending');
          if (idx != -1) {
            $scope.activityStatuses.splice(idx, 1);
          }
        }
      );
    }

    init();
  });
