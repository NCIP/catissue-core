'use strict';

angular.module('openspecimen', [
  'ngMessages',
  'ngSanitize', 
  'ui.router', 
  'ui.bootstrap', 
  'ui.mask', 
  'ui.select',
  'mgcrea.ngStrap.popover',
  'angular-loading-bar'])

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
        controller: function($scope, $state, cp) {
          $scope.cp = cp;
        },
        breadcrumb: {
          title: '{{cp.title}}',
          state: 'cp-detail.overview'
        }
      })
      .state('cp-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/biospecimen/cp/overview.html',
        parent: 'cp-detail'
      })
      .state('cp-detail.consents', {
        url: '/consents',
        templateUrl: 'modules/biospecimen/cp/consents.html',
        parent: 'cp-detail'
      })
      .state('cp-detail.events', {
        url: '/events',
        templateUrl: 'modules/biospecimen/cp/events.html',
        parent: 'cp-detail'
      })
      .state('participant-list', {
        url: '/participants',
        templateUrl: 'modules/biospecimen/participant/list.html',
        controller: 'ParticipantListCtrl',
        parent: 'cp-detail'
      })
      .state('participant-new', {
        url: '/new-participant',
        templateProvider: function($stateParams, CpConfigSvc) {
          var tmpl = CpConfigSvc.getRegParticipantTmpl($stateParams.cpId);
          return '<div ng-include src="\'' + tmpl + '\'"></div>';
        },
        controllerProvider: function($stateParams, CpConfigSvc) {
          return CpConfigSvc.getRegParticipantCtrl($stateParams.cpId);
        },
        parent: 'cp-detail'
      })
      .state('participant-detail', {
        url: '/participants/:cprId',
        templateUrl: 'modules/biospecimen/participant/detail.html',
        resolve: {
          cpr: function($stateParams, CprService) {
            return CprService.getRegistration($stateParams.cprId).then(
              function(result) {
                return result.data;
              }
            );   
          },

          visits: function($stateParams, CprService) {
            return CprService.getVisits($stateParams.cprId, true).then(
              function(result) {
                return result.data;
              }
            );
          }
        },
        controller: 'ParticipantDetailCtrl',
        parent: 'signed-in'
      })
      .state('participant-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/biospecimen/participant/overview.html',
        controller: function() {
        },
        parent: 'participant-detail'
      })
      .state('participant-detail.consents', {
        url: '/consents',
        templateUrl: 'modules/biospecimen/participant/consents.html',
        controller: function() {
        },
        parent: 'participant-detail'
      })
      .state('participant-detail.visits', {
        url: '/visits?eventId&visitId',
        templateUrl: 'modules/biospecimen/participant/visits.html',
        controller: 'ParticipantVisitsTreeCtrl',
        resolve: {
          specimens: function($stateParams, CprService) {
            if (!$stateParams.visitId && !$stateParams.eventId) {
              return [];
            }

            var visitDetail = {visitId: $stateParams.visitId, eventId: $stateParams.eventId};
            return CprService.getVisitSpecimensTree($stateParams.cprId, visitDetail).then(
              function(result) {
                return result.data;
              }
            );
          }
        },
        parent: 'participant-detail'
      })
      .state('participant-detail.annotations', {
        url: '/annotations',
        templateUrl: 'modules/biospecimen/participant/annotations.html',
        controller: function() {
        },
        parent: 'participant-detail'
      });

    $urlRouterProvider.otherwise('/');

    //$httpProvider.interceptors.push('httpRespInterceptor');

    ApiUrlsProvider.hostname = "localhost"; // used for testing purpose
    ApiUrlsProvider.port = 9090;
    ApiUrlsProvider.secure = false;
    ApiUrlsProvider.app = "/openspecimen";
    ApiUrlsProvider.urls = {
      'sessions': '/rest/ng/sessions',
      'collection-protocols': '/rest/ng/collection-protocols',
      'cprs': '/rest/ng/collection-protocol-registrations',
      'participants': '/rest/ng/participants',
      'sites': '/rest/ng/sites',
      'form-files': '/rest/ng/form-files'
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
          var url = '';
          if (key) {
            url = this.urls[key];
          }

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

    $rootScope.$on('$stateChangeSuccess', 
      function(event, toState, toParams, fromState, fromParams) { 
        $rootScope.state = toState;
      });
  });
