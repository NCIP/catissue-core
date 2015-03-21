
angular.module('os.query', 
  [
    'os.query.models',
    'os.query.list'
  ]
).config(function($stateProvider) {
   $stateProvider
     .state('query-list', {
        url: '/queries',
        templateUrl: 'modules/query/list.html',
        controller: 'QueryListCtrl',
        parent: 'signed-in'
     })
  });

