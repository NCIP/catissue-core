angular.module('os.administrative.dp.requirement.list', ['os.administrative.models'])
  .controller('DprListCtrl', function($scope, distributionProtocol, DistributionProtocolRequirement) {
    function loadRequirements() {
      var params = {dpId : distributionProtocol.$id()};
      DistributionProtocolRequirement.query(params).then(
        function(data) {
          $scope.dprs = data;
        }
      );
    }
    
    $scope.deleteDpr = function(dpr) {
      dpr.$remove().then(
        function() {
          var index = $scope.dprs.indexOf(dpr);
          $scope.dprs.splice(index, 1);
        }
      );
    }
    
    loadRequirements();
  });

