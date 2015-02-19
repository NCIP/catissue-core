
angular.module('openspecimen')
  .factory('AuthService', function($http, $window, ApiUtil, ApiUrls) {
    var url = function() {
      return ApiUrls.getUrl('sessions');
    };

    return {
      authenticate: function(loginData) {
        return $http.post(url(), loginData).then(ApiUtil.processResp);
      },

      logout: function() {
        return $http.delete(url()).then(ApiUtil.processResp);
      },

      saveToken: function(token) {
        $window.localStorage['osAuthToken'] = token;
        $http.defaults.headers.common['X-OS-API-TOKEN'] = token;
        $http.defaults.withCredentials = true;
      },

      removeToken: function() {
        delete $window.localStorage['osAuthToken'];
        delete $http.defaults.headers.common['X-OS-API-TOKEN'];
      }
    }
  })
  .controller('LoginCtrl', function($scope, $rootScope, $state, $http, $location, AuthService) {

    function init() {
      $scope.loginData = {domainName: 'openspecimen'};
      
      if ($location.search().logout) {
        $scope.logout();
        return;
      }
 
      if ($http.defaults.headers.common['X-OS-API-TOKEN']) {
        $state.go('cp-list');
      }
    }

    function onLogin(result) {
      $scope.loginError = false;

      if (result.status == "ok" && result.data) {
        $rootScope.currentUser = {
          firstName: result.data.firstName,
          lastName: result.data.lastName,
          loginName: result.data.loginName
        };
        $rootScope.loggedIn = true;
        AuthService.saveToken(result.data.token);
        $state.go('cp-list');
      } else {
        $rootScope.currentUser = {};
        $rootScope.loggedIn = false;
        AuthService.removeToken();
        $scope.loginError = true;
      }
    };

    function onLogout() {
      AuthService.removeToken();
    }

    $scope.login = function() {
      AuthService.authenticate($scope.loginData).then(onLogin);
    }

    $scope.logout = function() {
      AuthService.logout().then(onLogout); 
    }

    init();
  });
