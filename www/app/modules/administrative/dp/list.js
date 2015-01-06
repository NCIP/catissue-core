
angular.module('os.administrative.dp.list', ['os.administrative.models'])
  .controller('DpListCtrl', function($scope, $state, DistributionProtocol) {
    
    var loadDps = function() {
      DistributionProtocol.query().then(function(result) {
        $scope.distributionProtocols = result; 
      });
    };

    $scope.showDpOverview = function(distributionProtocol) {
      $state.go('dp-detail.overview', {dpId:distributionProtocol.id});
    };    
   
    loadDps();
  });
