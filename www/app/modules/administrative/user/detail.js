
angular.module('os.administrative.user.detail', ['os.administrative.models'])
  .controller('UserDetailCtrl', function($scope, $q, $state, $translate, user, User, PvManager, Alerts) {
    $scope.user = user;
    $scope.domains = PvManager.getPvs('domains');
    $scope.sites = PvManager.getSites();

    $scope.editUser = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.activate = function() {
      User.activate($scope.user.id).then(
        function(user) {
          $scope.user = user;
          $translate('user.user_request_approved').then(function(msg) {
            Alerts.success(msg);
          })
        }
      );
    }

    $scope.delete = function() {
      $scope.user.$remove().then(
        function() {
          $state.go('user-list');
        }
      );
    }
  });
