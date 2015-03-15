angular.module('os.administrative.institute.detail', ['os.administrative.models'])
  .controller('InstituteDetailCtrl', function($scope, $state, $modal, institute, Institute) {
    $scope.institute = institute;

    $scope.getDepartmentText = function(department) {
      return department.name;
    }

    $scope.deleteInstitute = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/administrative/institute/delete.html',
        controller: 'InstituteDeleteCtrl',
        resolve: {
          institute: function () {
            return $scope.institute;
          },
          instituteDependencies: function() {
            return Institute.getDependencies($scope.institute.id);
          }
        }
      });

      modalInstance.result.then(function() {
        $state.go('institute-list');
      });
    }

  });
