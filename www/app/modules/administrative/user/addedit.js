angular.module('os.administrative.user.addedit', ['os.administrative.models'])
  .controller('UserAddEditCtrl', function($scope, $rootScope, $state, $stateParams,
    user, User, Institute, AuthDomain) {
 
    function init() {
      $scope.user = user;
      $scope.signedUp = false;
      loadPvs();
    }

    function loadPvs() {
      $scope.domains = [];
      AuthDomain.getDomainNames().then(
        function(domains) {
          $scope.domains = domains;
          if (!$scope.user.id && $scope.domains.length == 1) {
            $scope.user.domainName = $scope.domains[0];
          }
        }
      );

      $scope.institutes = [];
      Institute.query().then(
        function(result) {
          angular.forEach(result, function(institute) {
            $scope.institutes.push(institute.name);
          });

          if (!$scope.user.id && $scope.institutes.length == 1) {
            $scope.user.instituteName = $scope.institutes[0];
          }

          if ($scope.user.instituteName) {
            $scope.loadDepartments($scope.user.instituteName);
          }
        }
      );
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
