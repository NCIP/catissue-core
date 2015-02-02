
angular.module('os.administrative.user.list', ['os.administrative.models'])
  .controller('UserListCtrl', function($scope, $state, User) {
    var loadUsers = function() {
      User.query().then(function(result) {
        $scope.users = result; 
      });
    };
    
    $scope.showUserOverview = function(user) {
      $state.go('user-detail.overview', {userId:user.id});
    };    
 
    loadUsers();
  });
