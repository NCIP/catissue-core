angular.module('os.administrative.user.addedit', ['os.administrative.models'])
  .controller('UserAddEditCtrl', function($scope, $rootScope, $state, $stateParams,
    user, User, Institute, PvManager) {
 
    function init() {
      $scope.user = user;
      $scope.signedUp = false;
      loadPvs();
    }

    function loadPvs() {
      $scope.domains = PvManager.getPvs('domains');
      $scope.institutes = [];
      if (!$rootScope.currentUser || $rootScope.currentUser.admin) {
        Institute.query().then(
          function(instituteList) {
            angular.forEach(instituteList, function(institute) {
              $scope.institutes.push(institute.name);
            });
          }
        );
      } else {
        User.getInstitute($rootScope.currentUser.id).then(function(institute) {
          $scope.institutes.push(institute.name);
        });
      }

      if (user.instituteName) {
        $scope.loadDepartments(user.instituteName)
      }

    }

    $scope.loadDepartments = function(instituteName) {
      Institute.getByName(instituteName).then(
        function(institute) {
          $scope.departments = [];
          if (institute) {
            angular.forEach(institute.departments, function(department) {
              $scope.departments.push(department.name);
            });
          }

          //This is trick to unset department on selecting institute
          if ($scope.departments.indexOf($scope.user.deptName) == -1) {
            $scope.user.deptName = undefined;
          }
        }
      );
    };
    
    $scope.createUser = function() {
      var user = angular.copy($scope.user);
      user.$saveOrUpdate().then(
        function(savedUser) {
          $state.go('user-detail.overview', {userId: savedUser.id});
        }
      );
    };

    $scope.signup = function() {
      var user = angular.copy($scope.user);
      User.signup(user).then(
        function(resp) {
          if (resp.status == 'ok') {
            $scope.signedUp = true;
          }
        }
      )
    };
     
    init();
  });
