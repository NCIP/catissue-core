angular.module('os.administrative.dp.requirement.list', ['os.administrative.models'])
  .controller('DprListCtrl', function($scope, requirements, DeleteUtil) {
  
    function init() {
      $scope.dprs = requirements;
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

