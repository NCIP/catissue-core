angular.module('os.administrative.dp.requirement.list', ['os.administrative.models'])
  .controller('DprListCtrl', function($scope, distributionProtocol, DistributionProtocolRequirement) {
    function loadRequirements() {
      DistributionProtocolRequirement.query(distributionProtocol.$id()).then(
        function(data) {
          $scope.dprs = data;
        }
      );
    }
    
    $scope.delete = function(dpr) {
      dpr.$remove().then(
        function() {
          var index = $scope.dprs.indexOf(dpr);
          $scope.dprs.splice(index, 1);
        }
      );
    }
    
    loadRequirements();
  });
