angular.module('os.administrative.user.password', ['os.administrative.models'])
  .controller('UserPasswordCtrl', function($scope, $rootScope, $state, $stateParams,
    user, User) {
 
    function init() {
      $scope.user = user;
      $scope.passwordDetail = {userId: user.id};
    }

    $scope.updatePassword = function() {
      User.changePassword($scope.passwordDetail).then(
      function(result) {
        if (result) {
          $state.go('user-detail.overview', {userId: $scope.user.id});
        }
      });
    }

    init();
  });
