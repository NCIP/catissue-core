
angular.module('os.query', 
  [
    'os.query.models',
    'os.query.globaldata',
    'os.query.addeditfolder',
    'os.query.delete',
    'os.query.importquery',
    'os.query.list',
    'os.query.save',
    'os.query.addedit',
    'os.query.addeditfilter',
    'os.query.expr',
    'os.query.util',
    'os.query.datepicker',
    'os.query.executor',
    'os.query.results',
    'os.query.defineview',
    'os.query.parameterized',
    'os.query.lookup'
  ]
).config(function($stateProvider) {
   $stateProvider
     .state('query-root', {
       url: '/queries',
       template: '<div ui-view></div>',
       controller: function($scope, queryGlobal, QueryUtil) {
         $scope.queryGlobal = queryGlobal;
         QueryUtil.initOpsDesc();
       },
       resolve: {
         queryGlobal: function(QueryGlobalData) {
           return new QueryGlobalData();
         }
       },
       abstract: true,
       parent: 'signed-in'
     })
     .state('query-list', {
       url: '/list',
       templateUrl: 'modules/query/list.html',
       controller: 'QueryListCtrl',
       parent: 'query-root'
     })
     .state('query-addedit', {
       url: '/addedit?queryId',
       templateUrl: 'modules/query/addedit.html',
       controller: 'QueryAddEditCtrl',
       resolve: {
         cps: function(queryGlobal) {
           return queryGlobal.getCps();
         },
         queryCtx: function($stateParams, queryGlobal) {
           return queryGlobal.getQueryCtx($stateParams.queryId);        
         }
       },
       parent: 'query-root'
     })  
     .state('query-results', {
       url: '/results?queryId&editMode&cpId',
       templateUrl: 'modules/query/results.html',
       controller: 'QueryResultsCtrl',
       resolve: {
         queryCtx: function($rootScope, $stateParams, queryGlobal, QueryCtxHolder) {
           var ctx = QueryCtxHolder.getCtx();
           if ($rootScope.stateChangeInfo && ctx && $rootScope.stateChangeInfo.fromState.name == ctx.fromState) {
             queryGlobal.setQueryCtx(ctx);
             $stateParams.editMode = true;
           }

           QueryCtxHolder.clearCtx();
           return queryGlobal.getQueryCtx($stateParams.queryId, $stateParams.cpId);
         },

         cps: function(queryGlobal) {
           return queryGlobal.getCps();
         }
       },
       parent: 'query-root'
     })
  });

