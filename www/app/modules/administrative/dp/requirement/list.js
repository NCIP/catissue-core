angular.module('os.administrative.dp.requirement.list', ['os.administrative.models'])
  .controller('RequirementListCtrl', function($scope, distributionProtocol, OrderRequirement) {
    function loadRequirements() {
      OrderRequirement.query(distributionProtocol.$id()).then(function(data) {
        $scope.requirements = data;
      });
    }
    
    $scope.delete = function(requirement) {
      requirement.$remove().then(
        function() {
          var index = $scope.requirements.indexOf(requirement);
          $scope.requirements.splice(index, 1);
        }
      );
    }
    
    loadRequirements();
  });
