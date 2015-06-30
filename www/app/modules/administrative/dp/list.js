
angular.module('os.administrative.dp.list', ['os.administrative.models'])
  .controller('DpListCtrl', function($scope, $state, DistributionProtocol, Util) {

    function init() {
      $scope.dpFilterOpts = {includeStats: true};
      loadDps($scope.dpFilterOpts);
      Util.filter($scope, 'dpFilterOpts', filter);
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

    init();
  });
