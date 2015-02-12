angular.module('openspecimen')
  .controller('ResetPasswordCtrl', function($scope, $state, $location, $translate, User)  {
  
    $scope.response = {};
    $scope.passwordDetail = {resetPasswordToken: $location.search().token};

    var onResetPassword = function(result) {
      if (result.status == 'ok') {
        $scope.response.status = result.status;
        $translate('reset_password.password_updated').then(function(msg) {
          $scope.response.message = msg;
        })
      } else {
        $scope.response.status = result.status;
      }   
    }

    $scope.resetPassword = function() {
      User.resetPassword($scope.passwordDetail).then(onResetPassword);
    }

    var init = function() {
      if (!$location.search().token) {
        $state.go('login');
      }
    }

    init();
  });
