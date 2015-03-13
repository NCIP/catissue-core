
angular.module('os.administrative.user.list', ['os.administrative.models'])
  .controller('UserListCtrl', function($scope, $state, User, PvManager) {
    function init() {
      $scope.userFilterOpts = {};
      loadUsers();
      $scope.activityStatuses = PvManager.getPvs('activity-status');
    }

    var loadUsers = function(filterOpts) {
      User.query(filterOpts).then(function(result) {
        $scope.users = result; 
      });
    };
    
    $scope.showUserOverview = function(user) {
      $state.go('user-detail.overview', {userId:user.id});
    };

    $scope.filter = function() {
      loadUsers($scope.userFilterOpts);
    }
 
    init();
  });
