
angular.module('os.administrative.user.list', ['os.administrative.models'])
  .controller('UserListCtrl', function($scope, $state, User, PvManager, Util) {
    function init() {
      $scope.userFilterOpts = {};
      loadUsers();
      loadPvs();
      Util.filter($scope, 'userFilterOpts', loadUsers);
    }

    function loadPvs() {
      PvManager.loadPvs('activity-status').then(
        function(result) {
          $scope.activityStatuses = result;

          var idx = $scope.activityStatuses.indexOf('Disabled');
          if (idx != -1) {
            $scope.activityStatuses.splice(idx, 1);
          }
        }
      );
    }

    function loadUsers(filterOpts) {
      User.query(filterOpts).then(function(result) {
        $scope.users = result; 
      });
    };
    
    $scope.showUserOverview = function(user) {
      $state.go('user-detail.overview', {userId:user.id});
    };

    init();
  });
