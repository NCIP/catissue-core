angular.module('os.administrative.institute.detail', ['os.administrative.models'])
  .controller('InstituteDetailCtrl', function($scope, $state, $modal, institute) {
    $scope.institute = institute;

    $scope.getDepartmentText = function(department) {
      return department.name;
    }

    $scope.deleteInstitute = function() {
      DeleteUtil.delete($scope.institute, {
        onDeleteState: 'institute-list',
        entityNameProp: $scope.institute.name,
        entityTypeProp: 'Institute'
      }, $modal, $state);
    }

  });
