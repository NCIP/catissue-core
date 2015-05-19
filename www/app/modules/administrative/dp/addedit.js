
angular.module('os.administrative.dp.addedit', ['os.administrative.models'])
  .controller('DpAddEditCtrl', function(
    $scope, $state, distributionProtocol, DistributionProtocol, Institute, User) {
    
    function init() {
      $scope.distributionProtocol = distributionProtocol;
      $scope.piFilterOpts = {institute: distributionProtocol.instituteName};
      $scope.institutes = [];
      loadInstitutes();
    }

    function loadInstitutes() {
      Institute.query().then(
        function(institutes) {
          $scope.institutes = institutes;
        }
      );
    }
    
    $scope.createDp = function() {
      var dp = angular.copy($scope.distributionProtocol);
      dp.$saveOrUpdate().then(
        function(savedDp) {
          $state.go('dp-detail.overview', {dpId: savedDp.id});
        }
      );
    };

    $scope.loadUsers = function() {
      $scope.distributionProtocol.principalInvestigator = undefined;
      $scope.piFilterOpts = {institute: $scope.distributionProtocol.instituteName};
    }
    
    init();
  });
