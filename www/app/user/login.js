
angular.module('openspecimen')
  .factory('AuthService', function($http, $window, ApiUtil, ApiUrls) {
    var url = function() {
      return ApiUrls.getUrl('sessions');
    };

    return {
      authenticate: function(loginData) {
        return $http.post(url(), loginData).then(ApiUtil.processResp);
      },

      saveToken: function(token) {
        $window.localStorage['osAuthToken'] = token;
        $http.defaults.headers.common['X-OS-API-TOKEN'] = token;
        //$cookieStore.put('JSESSIONID', token);
      },

      removeToken: function(token) {
        delete $window.localStorage['osAuthToken'];
        delete $http.defaults.headers.common['X-OS-API-TOKEN'];
      }
    }
  })
  .controller('LoginCtrl', function($scope, $rootScope, $state, AuthService) {
    $scope.loginData = {};

    var onLogin = function(result) {
      $scope.loginError = false;

      if (result.status == "ok" && result.data) {
        $rootScope.currentUser = {
          firstName: result.data.firstName,
          lastName: result.data.lastName,
          loginName: result.data.loginName
        };
        $rootScope.loggedIn = true;
        AuthService.saveToken(result.data.token);
        $state.go('cp_home');
      } else {
        $rootScope.currentUser = {};
        $rootScope.loggedIn = false;
        AuthService.removeToken();
        $scope.loginError = true;
      }
    };

    $scope.login = function() {
      AuthService.authenticate($scope.loginData).then(onLogin);
    }
  });
