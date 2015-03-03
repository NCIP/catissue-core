angular.module('openspecimen')
  .controller('ForgotPasswordCtrl', function($scope, User, $translate)  {
  
    $scope.user = {};
    $scope.response = {};

    var onSendMail = function(result) {
      $scope.response.status = result.status;
      if (result.status == 'ok') {
        $scope.response.message = 'forgot_password.reset_email_sent';
      } else {
        $scope.response.message = 'forgot_password.invalid_login_name';
      }   
    }

    $scope.sendPasswordResetLink = function() {
      User.sendPasswordResetLink($scope.user).then(onSendMail);
    }
  });
