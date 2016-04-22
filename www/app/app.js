'use strict';

var osApp = angular.module('openspecimen', [
  'os.common',
  'os.biospecimen',
  'os.administrative',
  'os.query',
  'os.fields',

  'ngMessages',
  'ngCookies',
  'ngSanitize', 
  'ngGrid',
  'ui.router', 
  'ui.bootstrap', 
  'ui.mask', 
  'ui.keypress', 
  'ui.select',
  'ui.sortable',
  'ui.autocomplete',
  'mgcrea.ngStrap.popover',
  'angular-loading-bar',
  'pascalprecht.translate'
  ]);

osApp.config(function(
    $stateProvider, $urlRouterProvider, $httpProvider, $controllerProvider,
    $compileProvider, $filterProvider, $provide,
    $translateProvider, $translatePartialLoaderProvider,
    uiSelectConfig, ApiUrlsProvider) {

    osApp.providers = {
      controller : $controllerProvider.register,
      state      : $stateProvider.state,
      directive  : $compileProvider.directive,
      filter     : $filterProvider.register,
      factory    : $provide.factory,
      service    : $provide.service
    };

    $translatePartialLoaderProvider.addPart('modules');
    $translateProvider.useSanitizeValueStrategy('escapeParameters');
    $translateProvider.useLoader('$translatePartialLoader', {  
      urlTemplate: '{part}/i18n/{lang}.js',
      loadFailureHandler: 'i18nErrHandler'
    });
 
    $stateProvider
      .state('default', {
        abstract: true,
        templateUrl: 'modules/common/default.html',
        controller: function($scope, Alerts) {
          $scope.alerts = Alerts.messages;
        }
      })
      .state('signed-in', {
        abstract: true,
        templateUrl: 'modules/common/appmenu.html',
        resolve: {
          currentUser: function(User) {
            return User.getCurrentUser();
          },
          authInit: function(AuthorizationService) {
            return AuthorizationService.initializeUserRights();
          }
        },
        controller: 'SignedInCtrl'
      })
      .state('home', {
        url: '/home',
        templateUrl: 'modules/common/home.html',
        controller: function() {
        },
        parent: 'signed-in'
      })
      .state('admin-view', {
        abstract: true,
        template: '<div ui-view></div>',
        resolve: {
          isAdmin: function(currentUser, Util) {
            return Util.booleanPromise(currentUser.admin);
          }
        },
        parent: 'signed-in'
      });

    $urlRouterProvider.otherwise('/');

    $httpProvider.interceptors.push('httpRespInterceptor');

    ApiUrlsProvider.urls = {
      'sessions': 'rest/ng/sessions',
      'sites': 'rest/ng/sites',
      'form-files': 'rest/ng/form-files'
    };

    uiSelectConfig.theme = 'bootstrap';
  })
  .factory('i18nErrHandler', function($q) {
    return function(part, lang) {
      return $q.when({});
    }
  })
  .factory('httpRespInterceptor', function($rootScope, $q, $injector, $window, Alerts, LocationChangeListener) {
    function displayErrMsgs(errors) {
      var errMsgs = errors.map(
        function(err) {
          return err.message + " (" + err.code + ")";
        }
      );
      Alerts.errorText(errMsgs);
    }

    return {
      request: function(config) {
        return config || $q.when(config);
      },

      requestError: function(rejection) {
        $q.reject(rejection);
      },

      response: function(response) {
        var httpMethods = ['POST', 'PUT', 'PATCH'];
        if (response.status == 200 && httpMethods.indexOf(response.config.method) != -1) {
          LocationChangeListener.allowChange();
        }
        return response || $q.when(response);
      },

      responseError: function(rejection) {
        if (rejection.status == 0) {
          Alerts.error("common.server_connect_error");
        } else if (rejection.status == 401) {
          $rootScope.loggedIn = false;
          delete $window.localStorage['osAuthToken'];
          delete $injector.get("$http").defaults.headers.common['X-OS-API-TOKEN'];
          $injector.get('$state').go('login'); // using injector to get rid of circular dependencies
        } else if (rejection.status / 100 == 5) {
          if (rejection.data instanceof Array) {
            displayErrMsgs(rejection.data);
          } else {
            Alerts.error("common.server_error");
          }
        } else if (rejection.status / 100 == 4) {
          if (rejection.data instanceof Array) {
            displayErrMsgs(rejection.data);
          } else if (rejection.config.method != 'HEAD') {
            Alerts.error('common.ui_error');
          }
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
        $http.defaults.headers.common['X-OS-API-CLIENT'] = "webui";

        if (!token) {
          token = $window.localStorage['osAuthToken'];
          if (!token) {
            return;
          }
        }

        $http.defaults.headers.common['X-OS-API-TOKEN'] = token;
        $http.defaults.withCredentials = true;
      }
    };
  })
  .provider('ApiUrls', function() {
    var that = this;
    var server = ui.os.server;

    this.urls = {};

    this.$get = function() {
      return {
        hostname: server.hostname,
        port    : server.port,
        secure  : server.secure,
        app     : server.app,
        urls    : that.urls,

        getBaseUrl: function() {
          return server.url + 'rest/ng/';
        },

        getUrl: function(key) {
          var url = '';
          if (key) {
            url = this.urls[key];
          }
          return server.url + url;
        }
      };
    }
  })
  .run(function(
    $rootScope, $window, $document, $cookieStore, $q,  $state, $translate, $translatePartialLoader,
    LocationChangeListener, ApiUtil, Setting, PluginReg) {

    $document.on('click', '.dropdown-menu.dropdown-menu-form', function(e) {
      e.stopPropagation();
    });

    if ($window.localStorage['osAuthToken']) {
      $cookieStore.put('osAuthToken', $window.localStorage['osAuthToken']);
      $rootScope.loggedIn = true;
    }

    ApiUtil.initialize();

    $rootScope.$on('$stateChangeSuccess',
      function(event, toState, toParams, fromState, fromParams) {
        $rootScope.state = toState;
      });

    $rootScope.$on('$stateChangeStart',
      function(event, toState, toParams, fromState, fromParams) {
        LocationChangeListener.onChange(event);

        if (toState.parent != 'default') {
          $rootScope.reqState = {
            name: toState.name,
            params: toParams
          };
        } else if ($rootScope.loggedIn && toState.name != "login") {
          event.preventDefault();
          if ($rootScope.reqState) {
            $state.go($rootScope.reqState.name, $rootScope.reqState.params);
          } else {
            $state.go("home");
          }
        }

        $rootScope.stateChangeInfo = {
          toState  : toState,   toParams  : toParams,
          fromState: fromState, fromParams: fromParams
        };
      });
      
    $rootScope.$on('$stateChangeError',
      function(event, toState, toParams, fromState, fromParams) {
        if (fromState.name == "") {
          $state.go('home');
        } else {
          $state.go(fromState.name);
        }
      });

    $rootScope.includesState = function(stateName, params, options) {
      return $state.includes(stateName, params, options);
    }

    $rootScope.back = function() {
      LocationChangeListener.allowChange();
      $window.history.back();
    };

    $rootScope.global = {
      defaultDomain: 'openspecimen',	
      filterWaitInterval: 500,
      appProps: ui.os.appProps
    };

    Setting.getLocale().then(
      function(localeSettings) {
        angular.extend(
          $rootScope.global,
          {
            dateFmt: localeSettings.dateFmt,
            shortDateFmt: localeSettings.deBeDateFmt,
            timeFmt: localeSettings.timeFmt,
            queryDateFmt: {format: localeSettings.deFeDateFmt},
            dateTimeFmt: localeSettings.dateFmt + ' ' + localeSettings.timeFmt,
            utcOffset: localeSettings.utcOffset
          }
        );

        var plugins = ui.os.appProps.plugins;
        PluginReg.usePlugins(plugins);
        angular.forEach(plugins, function(plugin) {
          $translatePartialLoader.addPart('plugin-ui-resources/'+ plugin + '/');
        });
        
        var locale = localeSettings.locale;
        $translate.use(locale);

        var idx = locale.indexOf('_');
        var localeLang = (idx != -1) ? locale.substring(0, idx) : locale;

        var langs = [locale];
        if (locale !== localeLang) {
          langs.push(localeLang);
        }

        if (localeLang != 'en') {
          langs.push('en');
        }

        $translate.fallbackLanguage(langs);
        $translate.refresh();
      }
    );
  });
