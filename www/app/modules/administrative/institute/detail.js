angular.module('os.administrative.institute.detail', ['os.administrative.models'])
  .controller('InstituteDetailCtrl', function($scope, institute) {
    $scope.institute = institute;

    $scope.getDepartmentText = function(department) {
      return department.name;
    }
  });
