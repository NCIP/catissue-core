
angular.module('os.administrative.dp.addedit', ['os.administrative.models'])
  .controller('DpAddEditCtrl', function($scope, $state, distributionProtocol, DistributionProtocol) {
    
    var init = function() {
      $scope.distributionProtocol = distributionProtocol;
    }
    
    $scope.createDp = function() {
      var dp = angular.copy($scope.distributionProtocol);
      dp.$saveOrUpdate().then(
        function(savedDp) {
          $state.go('dp-detail.overview', {dpId: savedDp.id});
        }
      );
    };
    
    init();
  });
