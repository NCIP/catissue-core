angular.module('openspecimen')
  .factory('ForgotPasswordService', function($http, ApiUtil, ApiUrls) {
    var url = ApiUrls.getBaseUrl() +"users/";

    return {
      sendMail: function(username) {
        return $http.get(url + username + '/forgotPassword').then(ApiUtil.processResp);
      }
    }
  })
  .controller('ForgotPasswordCtrl', function($scope, $rootScope, ForgotPasswordService)  {
  
    $scope.user = {};
    $scope.response = {};

    var onSendMail = function(result) {

      if (result.status == 'ok') {
        $scope.response.status = result.status;
        $scope.response.message = "An email has been sent."        
      } else {
        $scope.response.status = result.status;
        $scope.response.message = "We couldn't find a user associated with " +  $scope.user.username; 
      }   
    }

    $scope.sendMail = function() {
      ForgotPasswordService.sendMail($scope.user.username).then(onSendMail);
    }

  });
