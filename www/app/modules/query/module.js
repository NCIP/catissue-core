
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
    'os.query.results'
  ]
).config(function($stateProvider) {
   $stateProvider
     .state('query-root', {
       url: '/queries',
       template: '<div ui-view></div>',
       controller: function($scope, queryGlobal) {
         $scope.queryGlobal = queryGlobal;
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
         query: function($stateParams, SavedQuery) {
           if (!!$stateParams.queryId) {
             return SavedQuery.getById($stateParams.queryId);
           }

           return new SavedQuery();
         }
       },
       parent: 'query-root'
     })  
     .state('query-results', {
       url: '/results?querId',
       templateUrl: 'modules/query/results.html',
       controller: 'QueryResultsCtrl',
       resolve: {
       },
       parent: 'query-root'
     })
  });

