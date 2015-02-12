angular.module('openspecimen')
  .controller('ForgotPasswordCtrl', function($scope, User, $translate)  {
  
    $scope.user = {};
    $scope.response = {};

    var onSendMail = function(result) {
      if (result.status == 'ok') {
        $scope.response.status = result.status;
        $translate('forgot_password.reset_email_sent').then(function(msg) {
          $scope.response.message = msg;
        }); 
      } else {
        $scope.response.status = result.status;
        $translate('forgot_password.invalid_login_name', {loginName: $scope.user.loginName}).then(function(msg){
          $scope.response.message = msg;
        });
      }   
    }

    $scope.sendMail = function() {
      User.sendPasswordResetLink($scope.user).then(onSendMail);
    }
  });
