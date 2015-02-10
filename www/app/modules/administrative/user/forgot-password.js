angular.module('openspecimen')
  .controller('ForgotPasswordCtrl', function($scope, $rootScope, User, $translate)  {
  
    $scope.user = {};
    $scope.response = {};

    var onSendMail = function(result) {
      if (result.status == 'ok') {
        $scope.response.status = result.status;
        $translate('user.forgot_password.success_msg').then(function(msg) {
          $scope.response.message = msg;
        }); 
      } else {
        $scope.response.status = result.status;
        $translate('user.forgot_password.error_msg', {loginName: $scope.user.loginName}).then(function(msg){
          $scope.response.message = msg;
        });
      }   
    }

    $scope.sendMail = function() {
      User.sendPasswordResetLink($scope.user).then(onSendMail);
    }
  });
