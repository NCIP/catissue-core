angular.module('os.administrative.user.password', ['os.administrative.models'])
  .controller('UserPasswordCtrl', function(
    $scope, $rootScope, $state, $stateParams, user,
    User, Setting) {
 
    function init() {
      $scope.user = user;
      $scope.passwordDetail = {userId: user.id};
      $scope.passwdCtx = {};
      loadPasswdSettings();
    }

    $scope.updatePassword = function() {
      User.changePassword($scope.passwordDetail).then(
      function(result) {
        if (result) {
          $state.go('user-detail.overview', {userId: $scope.user.id});
        }
      });
    }

    function loadPasswdSettings() {
      Setting.getPasswordSettings().then(
        function(setting) {
          $scope.passwdCtx = setting;
        }
      );
    }

    init();
  });
