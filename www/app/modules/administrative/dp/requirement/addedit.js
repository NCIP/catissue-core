angular.module('os.administrative.dp.requirement.addedit', ['os.administrative.models'])
  .controller('DpRequirementsAddEditCtrl', function($scope, $state, $q, PvManager, Util, requirement, OrderRequirement) {
    $scope.specimenTypes = [];
    $scope.requirement = requirement;
    $scope.requirement.specimenDistributed = 0;

    function loadAllSpecimenTypes() {
      return PvManager.loadPvsByParent('specimen-class', undefined, true).then(
        function(specimenTypes) {
          angular.forEach(specimenTypes, function(type){
            if($scope.specimenTypes.indexOf(type.value) < 0){
              $scope.specimenTypes.push(type.value);
            }
          });
        }
      );
    }
  
    function loadPvs() {
      loadAllSpecimenTypes();
      $scope.anatomicSites = PvManager.getLeafPvs('anatomic-site');
      $scope.pathologyStatuses = PvManager.getPvs('pathology-status');
    }

    $scope.cancel = function() {
      $state.go('req-list');
    }
    
    $scope.save = function() {
      OrderRequirement.saveUpdate($scope.requirement).then(function(resp) {
        if(resp) {
          $state.go('req-list');
        }
      });
    }
    
    loadPvs();
  });
