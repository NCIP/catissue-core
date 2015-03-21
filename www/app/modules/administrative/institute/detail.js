angular.module('os.administrative.institute.detail', ['os.administrative.models'])
  .controller('InstituteDetailCtrl', function($scope, $state, $modal, institute, Institute) {
    $scope.institute = institute;

    $scope.getDepartmentText = function(department) {
      return department.name;
    }

    $scope.deleteInstitute = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/common/delete/delete-entity-template.html',
        controller: 'entityDeleteCtrl',
        resolve: {
          entityProps: function() {
            return {
              entity: $scope.institute,
              name: $scope.institute.name,
            }
          },
          entityDependencyStat: function() {
            return Institute.getDependencyStat($scope.institute.id);
          }
        }
      });

      modalInstance.result.then(function() {
        $state.go('institute-list');
      });
    }

  });
