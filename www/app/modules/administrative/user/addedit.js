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

        }
      );
    }

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
