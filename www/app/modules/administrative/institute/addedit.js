angular.module('os.administrative.institute.addedit',['os.administrative.models'])
  .controller('InstituteAddEditCtrl', function($scope, $state, Institute) {

    var init = function() {
      $scope.institute = new Institute();
      $scope.institute.departments = [];
      $scope.institute.addDepartment($scope.institute.newDepartment());
    }

    $scope.addDepartmentIfLast = function(idx) {
      if (idx == $scope.institute.departments.length - 1) {
        $scope.institute.addDepartment($scope.institute.newDepartment());
      }
    }

    $scope.removeDepartment = function(department) {
      $scope.institute.removeDepartment(department);
      if($scope.institute.departments.length == 0) {
        $scope.institute.addDepartment($scope.institute.newDepartment());
      }
    }

    $scope.save = function() {
      var institute = angular.copy($scope.institute);
      institute.$saveOrUpdate().then(
        function(savedInstitute) {
          $state.go('institute-detail.overview', {instituteId: savedInstitute.id});
        }
      );
    }

    init();
  });
