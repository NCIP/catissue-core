'use strict';

angular.module('openspecimen', [
  'os.common',
  'os.biospecimen',
  'os.administrative',

  'ngMessages',
  'ngSanitize', 
  'ui.router', 
  'ui.bootstrap', 
  'ui.mask', 
  'ui.keypress', 
  'ui.select',
  'mgcrea.ngStrap.popover',
  'angular-loading-bar',
  'pascalprecht.translate'])

  .config(function(
    $stateProvider, $urlRouterProvider, 
    $httpProvider, $translateProvider,
    uiSelectConfig, ApiUrlsProvider) {

    $translateProvider.useStaticFilesLoader({
      prefix: 'modules/i18n/',
      suffix: '.js'
    });

    $translateProvider.preferredLanguage('en_US');

    $stateProvider
      .state('login', {
        url: '/',
        templateUrl: 'modules/user/signin.html',
        controller: 'LoginCtrl'
      })
      .state('signed-in', {
        abstract: true,
        templateUrl: 'modules/common/appmenu.html',
        controller: function($scope, Alerts) {
          $scope.alerts = Alerts.messages;
        }
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
      .state('cp-addedit', {
        url: '/addedit?cpId',
        templateUrl: 'modules/biospecimen/cp/addedit.html',
        controller: 'CpAddEditCtrl',
        parent: 'cps'
      })
      .state('cp-detail', {
        url: '/:cpId',
        templateUrl: 'modules/biospecimen/cp/detail.html',
        parent: 'cps',
        resolve: {
          cp: function($stateParams, CollectionProtocol) {
            return CollectionProtocol.getById($stateParams.cpId);
          }
        },
        controller: 'CpDetailCtrl',
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
        parent: 'cp-detail',
        resolve: {
          consentTiers: function(cp) {
            return cp.getConsentTiers();
          }
        },
        controller: 'CpConsentsCtrl'
      })
      .state('cp-detail.events', {
        templateUrl: 'modules/biospecimen/cp/events.html',
        parent: 'cp-detail',
        resolve: {
          events: function($stateParams, cp, CollectionProtocolEvent) {
            return CollectionProtocolEvent.listFor(cp.id);
          }
        },
        controller: 'CpEventsCtrl'
      })
      .state('cp-detail.specimen-requirements', {
        url: '/specimen-requirements?eventId',
        templateUrl: 'modules/biospecimen/cp/specimens.html',
        parent: 'cp-detail.events',
        resolve: {
          specimenRequirements: function($stateParams, SpecimenRequirement) {
            var eventId = $stateParams.eventId;
            if (!eventId) {
              return [];
            }

            return SpecimenRequirement.listFor(eventId);
          }
        },
        controller: 'CpSpecimensCtrl'
      })
      .state('cp-detail.users', {
        url: '/users',
        templateUrl: 'modules/biospecimen/cp/users.html',
        parent: 'cp-detail'
      })
      .state('cp-detail.dashboard', {
        url: '/dashboard',
        templateUrl: 'modules/biospecimen/cp/dashboard.html',
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
          cpr: function($stateParams, CollectionProtocolRegistration) {
            return CollectionProtocolRegistration.getById($stateParams.cprId);
          },

          visits: function($stateParams, Visit) {
            return Visit.listFor($stateParams.cprId, true);
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
          specimens: function($stateParams, Specimen) {
            if (!$stateParams.visitId && !$stateParams.eventId) {
              return [];
            }

            var visitDetail = {
              visitId: $stateParams.visitId, 
              eventId: $stateParams.eventId
            };
            return Specimen.listFor($stateParams.cprId, visitDetail);
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

    $httpProvider.interceptors.push('httpRespInterceptor');

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
  .factory('httpRespInterceptor', function($q, $injector, Alerts, $window) {
    return {
      request: function(config) {
        return config || $q.when(config);
      },

      requestError: function(rejection) {
        $q.reject(rejection);
      },

      response: function(response) {
        return response || $q.when(response);
      },

      responseError: function(rejection) {
        if (rejection.status == 401) {
          $window.localStorage['osAuthToken'] = '';
          $injector.get('$state').go('login'); // using injector to get rid of circular dependencies
        } else if (rejection.status / 100 == 5) {
          Alerts.error("Internal server error. Please contact system administrator");
        } else if (rejection.status / 100 == 4) {
          Alerts.error("Bad user action: " + rejection.data.message);
        }

        return $q.reject(rejection);
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

        getBaseUrl: function() {
          var prefix = '';
          if (this.hostname) {
            var protocol = this.secure ? 'https://' : 'http://';
            prefix = protocol + this.hostname + ':' + this.port;
          }

          return prefix + this.app + '/rest/ng/';
        },

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
