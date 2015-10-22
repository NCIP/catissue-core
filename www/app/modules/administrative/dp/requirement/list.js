angular.module('os.administrative.dp.requirement.list', ['os.administrative.models'])
  .controller('DprListCtrl', function($scope, distributionProtocol, DistributionProtocolRequirement, DeleteUtil) {
  
    function init() {
      $scope.input = {targets: []};
      loadRequirements();
    }
  
    function loadRequirements() {
      var params = {
        dpId : distributionProtocol.$id(),
        includeDistQty: true
      };
      DistributionProtocolRequirement.query(params).then(
        function(data) {
          $scope.dprs = data;
          calcTarget();
        }
      );
    }
    
    function calcTarget() {
      var dprs = $scope.dprs;
      var targets = $scope.input.targets;
      angular.forEach(dprs, function(dpr) {
        targets.push(dpr.specimenCount * dpr.quantity);
      });
    }
    
    $scope.deleteDpr = function(dpr) {
    
      DeleteUtil.confirmDelete({
        entity: dpr,
        templateUrl: 'modules/administrative/dp/requirement/delete.html',
        delete : function() {
          dpr.$remove().then(
            function() {
              var index = $scope.dprs.indexOf(dpr);
              $scope.dprs.splice(index, 1);
              $scope.input.targets.splice(index, 1);
            }
          );
        }
      });
      
    }
    
    init();
  });

