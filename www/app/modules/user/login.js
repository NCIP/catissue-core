
angular.module('openspecimen')
  .factory('AuthService', function($http, $rootScope, $window, $cookieStore, $location, ApiUtil, ApiUrls) {
    var url = function() {
      return ApiUrls.getUrl('sessions');
    };

    var redirectToUrlAfterLogin = {url: "/home"};

    return {
      authenticate: function(loginData) {
        return $http.post(url(), loginData).then(ApiUtil.processResp);
      },

      logout: function() {
        var q = $http.delete(url());
        this.removeToken();
        delete $rootScope.currentUser;
        return q.then(ApiUtil.processResp);
      },

      saveToken: function(token) {
        $window.localStorage['osAuthToken'] = token;
        $cookieStore.put('osAuthToken', token);
        $http.defaults.headers.common['X-OS-API-TOKEN'] = token;
        $http.defaults.withCredentials = true;
      },

      removeToken: function() {
        delete $window.localStorage['osAuthToken'];
        $cookieStore.remove('osAuthToken');
        delete $http.defaults.headers.common['X-OS-API-TOKEN'];
        delete $http.defaults.headers.common['Authorization'];
      },

      saveAttemptUrl: function() {
        if($location.path() != "/" && $location.path() != "/?logout=true") {
          redirectToUrlAfterLogin.url = $location.path();
        }
        else {
          redirectToUrlAfterLogin.url = '/home';
        }
      },

      redirectToAttemptedUrl: function() {
        $location.path(redirectToUrlAfterLogin.url);
        redirectToUrlAfterLogin.url = '/home';
      }
    }
  })
  .controller('LoginCtrl', function($scope, $rootScope, $state, $http, $location, AuthDomain, AuthService) {

    function init() {
      $scope.loginData = {};
      
      if ($location.search().logout) {
        $scope.logout();
      }
 
      if ($http.defaults.headers.common['X-OS-API-TOKEN']) {
        $state.go('home');
      }

      loadPvs();
    }

    function loadPvs() {
      $scope.domains = [];
      AuthDomain.getDomainNames().then(
        function(domains) {
          $scope.domains = domains;
          if ($scope.domains.length == 1) {
            $scope.loginData.domainName = $scope.domains[0];
          }
        }
      );
    }

    function onLogin(result) {
      $scope.loginError = false;

      if (result.status == "ok" && result.data) {
        $rootScope.currentUser = {
          id: result.data.id,
          firstName: result.data.firstName,
          lastName: result.data.lastName,
          loginName: result.data.loginName,
          admin: result.data.admin
        };
        $rootScope.loggedIn = true;
        AuthService.saveToken(result.data.token);
        AuthService.redirectToAttemptedUrl();
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

    $scope.logout = function() {
      if ($http.defaults.headers.common['X-OS-API-TOKEN']) {
        AuthService.logout();
      }
    }

    init();
  });
