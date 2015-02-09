angular.module('openspecimen')
  .controller('ResetPasswordCtrl', function($scope, $rootScope, $location, User, Alerts)  {
  
    $scope.response = {};
    $scope.alerts = Alerts.messages;
    $scope.passwordDetail = {resetPasswordToken: $location.search().token};

    var onResetPassword = function(result) {
      if (result.status == 'ok') {
        $scope.response.status = result.status;
        $scope.response.message = "Password has been set." 
      } else {
        $scope.response.status = result.status;
      }   
    }

    $scope.resetPassword = function() {
      User.resetPassword($scope.passwordDetail).then(onResetPassword);
    }
  });
