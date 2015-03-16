
angular.module('os.administrative.dp.list', ['os.administrative.models'])
  .controller('DpListCtrl', function($scope, $state, DistributionProtocol) {

    function init() {
      $scope.dpFilterOpts = {};
      loadDps();
    }
    
    function loadDps(filterOpts) {
      DistributionProtocol.query(filterOpts).then(function(result) {
        $scope.distributionProtocols = result; 
      });
    }

    $scope.showDpOverview = function(distributionProtocol) {
      $state.go('dp-detail.overview', {dpId:distributionProtocol.id});
    };

    $scope.filter = function() {
      loadDps($scope.dpFilterOpts);
    }

    $scope.onPiSelect = function(pi) {
      $scope.dpFilterOpts.piId = pi.id;
      $scope.filter();
    }
   
    init();
  });
