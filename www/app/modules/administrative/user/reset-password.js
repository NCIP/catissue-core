angular.module('openspecimen')
  .factory('ResetPasswordService', function($http, ApiUtil, ApiUrls) {
    var url = ApiUrls.getBaseUrl() +"users/";

    return {
      resetPassword: function(passowrdDetail, loginName) {
        return $http.post(url + loginName + "/password", passowrdDetail).then(ApiUtil.processResp); 
      }
    }
  })
  .controller('ResetPasswordCtrl', function($scope, $rootScope, $stateParams, ResetPasswordService, Alerts)  {
  
    $scope.passwordDetail = {forgotPasswordToken: $stateParams.forgotPasswordToken};
    $scope.response = {};
    $scope.alerts = Alerts.messages;

    var onResetPassword = function(result) {

      if (result.status == 'ok') {
        $scope.response.status = result.status;
        $scope.response.message = "Password has been set." 
      } else {
        $scope.response.status = result.status;
        //$scope.response.message = "We couldn't find a user associated with" +  $scope.user.username; 
      }   
    }

    $scope.resetPassword = function() {
      ResetPasswordService.resetPassword($scope.passwordDetail, $scope.passwordDetail.loginName).then(onResetPassword);
    }

  });
