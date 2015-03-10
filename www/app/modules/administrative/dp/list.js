
angular.module('os.administrative.dp.list', ['os.administrative.models'])
  .controller('DpListCtrl', function($scope, $state, DistributionProtocol) {

    function init() {
      $scope.dpFilterOpts = {};
      loadDps();
    }
    
    function loadDps() {
      DistributionProtocol.query().then(function(result) {
        $scope.distributionProtocols = result; 
      });
    };

    $scope.showDpOverview = function(distributionProtocol) {
      $state.go('dp-detail.overview', {dpId:distributionProtocol.id});
    };

    $scope.filter = function(dpFilterOpts) {
      DistributionProtocol.list(dpFilterOpts).then(
        function(result) {
          $scope.distributionProtocols = result;
        }
      )
    }
   
    init();
  });
