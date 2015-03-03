angular.module('os.administrative.institute.addedit',['os.administrative.models'])
  .controller('InstituteAddEditCtrl', function($scope, $state, institute, Institute) {

    var init = function() {
      $scope.institute = institute; 
      $scope.institute.departments = institute.departments || [];
      $scope.institute.addDepartment($scope.institute.newDepartment());
    }

    $scope.addDepartmentIfLast = function(idx) {
      if (idx == $scope.institute.departments.length - 1) {
        $scope.institute.addDepartment($scope.institute.newDepartment());
      }
    }

    $scope.removeDepartment = function(department) {
      var institute = $scope.institute;
     
      if (department == undefined) {
        department = institute.departments[institute.departments.length - 1];
        if (department && !!department.name) {
          return;
        }
      }

      institute.removeDepartment(department);
      if (institute.departments.length == 0) {
        institute.addDepartment($scope.institute.newDepartment());
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
