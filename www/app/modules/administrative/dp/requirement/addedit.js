angular.module('os.administrative.dp.requirement.addedit', ['os.administrative.models'])
  .controller('DprAddEditCtrl', function($scope, $state, distributionProtocol, dpr, PvManager) {
    $scope.dpr = dpr;

    function loadPvs() {
      loadAllSpecimenTypes();
      $scope.anatomicSites = PvManager.getLeafPvs('anatomic-site');
      $scope.pathologyStatuses = PvManager.getPvs('pathology-status');
    }

    function loadAllSpecimenTypes() {
      $scope.specimenTypes = [];
      
      return PvManager.loadPvsByParent('specimen-class', undefined, true).then(
        function(specimenTypes) {
          angular.forEach(specimenTypes, function(type) {
            if ($scope.specimenTypes.indexOf(type.value) < 0) {
              $scope.specimenTypes.push(type.value);
            }
          });
        }
      );
    }
  
    $scope.cancel = function() {
      $state.go('req-list');
    }
    
    $scope.save = function() {
      var dpr = angular.copy($scope.dpr);
      dpr.dp = {id : distributionProtocol.id};
      dpr.$saveOrUpdate().then(
        function(saveReq) {
          $state.go('req-list');
        }
      );
    }
    
    loadPvs();
  });

