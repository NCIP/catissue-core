
angular.module('openspecimen')
  .factory('AuthService', function($http, $rootScope, $window, $cookieStore, ApiUtil, ApiUrls) {
    var url = function() {
      return ApiUrls.getUrl('sessions');
    };

    return {
      authenticate: function(loginData) {
        return $http.post(url(), loginData).then(ApiUtil.processResp);
      },

      logout: function() {
        var q = $http.delete(url());
        this.removeToken();
        $rootScope.loggedIn = false;
        delete $rootScope.reqState;
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
        if ($rootScope.reqState) {
          $state.go($rootScope.reqState.name, $rootScope.reqState.params);
        } else {
          $state.go('home');
        }
        //return;
      }

      loadDomains();
    }

    function loadDomains() {
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
        if ($rootScope.reqState) {
          $state.go($rootScope.reqState.name, $rootScope.reqState.params);
          $rootScope.reqState = undefined;
        } else {
          $state.go('cp-list');
        }
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
