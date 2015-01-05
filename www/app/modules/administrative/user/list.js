
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
 
    var today = new Date();
    $scope.date = today.getDate() + "-" + (today.getMonth() + 1) + "-" + today.getFullYear() 
      + "  " + today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    
    loadUsers();
  });
