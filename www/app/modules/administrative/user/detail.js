
angular.module('os.administrative.user.detail', ['os.administrative.models'])
  .controller('UserDetailCtrl', function(
    $scope, $q, $translate, user, 
    User, AuthDomain, PvManager, Alerts, DeleteUtil) {

    function init() {
      $scope.user = user;
      loadPvs();
    }

    function loadPvs() {
      $scope.sites = PvManager.getSites();
      $scope.domains = [];
      AuthDomain.query().then(
        function(domainList) {
          angular.forEach(domainList, function(domain) {
            $scope.domains.push(domain.name);
          });
        }
      );
    }

    $scope.editUser = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.activate = function() {
      User.activate($scope.user.id).then(
        function(user) {
          $scope.user = user;
          Alerts.success('user.user_request_approved');
        }
      );
    }

    $scope.deleteUser = function() {
      DeleteUtil.delete($scope.user, {
        onDeleteState: 'user-list',
        confirmDelete: $scope.user.activityStatus == 'Pending' ? 'user.confirm_reject' : undefined
      });
    }

    init();

  });
