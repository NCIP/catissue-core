'use strict';

angular.module('openspecimen', ['ngSanitize', 'ui.router', 'ui.bootstrap', 'ui.mask', 'ui.select'])
  .config(function($stateProvider, $urlRouterProvider, $httpProvider, uiSelectConfig, ApiUrlsProvider) {
    $stateProvider
      .state('login', {
        url: '/',
        templateUrl: 'modules/user/signin.html',
        controller: 'LoginCtrl'
      })
      .state('signed-in', {
        abstract: true,
        templateUrl: 'modules/common/appmenu.html'
      })
      .state('cps', {
        url: '/cps',
        abstract: true,
        template: '<div ui-view></div>',
        parent: 'signed-in'
      })
      .state('cp-list', {
        url: '', 
        templateUrl: 'modules/biospecimen/cp/list.html',
        controller: 'CpListCtrl',
        parent: 'cps'
      })
      .state('cp-detail', {
        url: '/:cpId',
        templateUrl: 'modules/biospecimen/cp/detail.html',
        parent: 'cps',
        resolve: {
          cp: function($stateParams, CollectionProtocolService) {
            return CollectionProtocolService.getCp($stateParams.cpId).then(
              function(result) {
                return result.data;
              }
            );
          }
        },
        controller: function($scope, cp) {
          $scope.cp = cp;
        },
        breadcrumb: {
          title: '{{cp.title}}'
        }
      })
      .state('participant-list', {
        url: '/participants',
        templateUrl: 'modules/biospecimen/participant/list.html',
        controller: 'ParticipantListCtrl',
        parent: 'cp-detail'
      })
      .state('institute-list', {
        url: '/institution',
        templateUrl: 'modules/administrative/institution/list.html',
        controller: 'InstituteCtrl',
        parent: 'signed-in'
      });

    $urlRouterProvider.otherwise('/');

    //$httpProvider.interceptors.push('httpRespInterceptor');

    ApiUrlsProvider.hostname = "localhost"; // used for testing purpose
    ApiUrlsProvider.port = 8180;
    ApiUrlsProvider.secure = false;
    ApiUrlsProvider.app = "/openspecimen";
    ApiUrlsProvider.urls = {
      'sessions': '/rest/ng/sessions',
      'collection-protocols': '/rest/ng/collection-protocols',
      'participants': '/rest/ng/participants',
      'users': '/rest/ng/users',
      'sites': '/rest/ng/sites',
      'institutions': '/rest/ng/institutes'
    };

    uiSelectConfig.theme = 'bootstrap';
  })
  .factory('httpRespInterceptor', function($q, $injector, $window) {
    return {
      request: function(result) {
        return result;
      },

      requestError: function(result) {
        $q.reject(result);
      },

      response: function(result) {
        return result;
      },

      responseError: function(result) {
        if (result.status == 401) {
          $window.localStorage['osAuthToken'] = '';
          $injector.get('$state').go('login'); // using injector to get rid of circular dependencies
        }

        $q.reject(result);
      }
    };
  })
  .factory('ApiUtil', function($window, $http) {
    return {
      processResp: function(result) {
        var response = {};
        if (result.status / 100 == 2) {
          response.status = "ok";
        } else if (result.status / 100 == 4) {
          response.status = "user_error";
        } else if (result.status / 100 == 5) {
          response.status = "server_error";
        }

        response.data = result.data;
        return response;
      },

      initialize: function(token) {
        if (!token) {
          token = $window.localStorage['osAuthToken'];
          if (!token) {
            return;
          }

          //token = JSON.parse(token);
        }

        $http.defaults.headers.common['X-OS-API-TOKEN'] = token;
        $http.defaults.withCredentials = true;
        //$cookieStore.put('JESSIONID', token);
        //$http.defaults.headers.common['Cookie'] = 'JSESSIONID=' + token;
      }
    };
  })
  .provider('ApiUrls', function() {
    var that = this;

    this.hostname = "";
    this.port = "";
    this.secure = false;
    this.app = "";
    this.urls = {};

    this.$get = function() {
      return {
        hostname: that.hostname,
        port    : that.port,
        secure  : that.secure,
        app     : that.app,
        urls    : that.urls,

        getUrl  : function(key) {
          var url = this.urls[key];
          var prefix = "";
          if (this.hostname) {
            var protocol = this.secure ? 'https://' : 'http://';
            prefix = protocol + this.hostname + ":" + this.port;
          }

          return prefix + this.app + url;
        }
      };
    }
  })
  .run(function($rootScope, $window, ApiUtil) {
    if ($window.localStorage['osAuthToken']) {
      $rootScope.loggedIn = true;
    }

    ApiUtil.initialize();
  });
