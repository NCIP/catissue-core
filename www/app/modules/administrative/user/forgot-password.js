angular.module('openspecimen')
  .controller('ForgotPasswordCtrl', function($scope, $rootScope, User)  {
  
    $scope.user = {};
    $scope.response = {};

    var onSendMail = function(result) {
      if (result.status == 'ok') {
        $scope.response.status = result.status;
        $scope.response.message = "An email has been sent."        
      } else {
        $scope.response.status = result.status;
        $scope.response.message = "We couldn't find a user associated with " +  $scope.user.loginName; 
      }   
    }

    $scope.sendMail = function() {
      User.sendPasswordResetLink($scope.user).then(onSendMail);
    }
  });
