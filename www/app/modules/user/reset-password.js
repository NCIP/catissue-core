angular.module('openspecimen')
  .controller('ResetPasswordCtrl', function($scope, $state, $location, $translate, User)  {
  
    function init() {
      if (!$location.search().token) {
        $state.go('login');
        return;
      }

      $scope.response = {};
      $scope.passwordDetail = {resetPasswordToken: $location.search().token};
    }

    function onResetPassword(result) {
      $scope.response.status = result.status;
      if (result.status == 'ok') {
        $scope.response.message = 'reset_password.password_updated';
      }
    }

    $scope.resetPassword = function() {
      User.resetPassword($scope.passwordDetail).then(onResetPassword);
    }

    init();
  });
