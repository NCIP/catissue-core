
angular.module('os.biospecimen.specimensrequest', 
  [
    'ui.router',
    'os.biospecimen.specimensrequest.list'
  ])
  .config(function($stateProvider) {
    $stateProvider
      .state('specimens-request-list', {
        url: '/specimen-requests',
        templateUrl: 'modules/biospecimen/specimens-request/list.html',
        controller: 'SpecimensRequestListCtrl',
        resolve: {
          specimensRequestList: function(cp, SpecimenRequest) {
            return SpecimenRequest.listForCp(cp.id, true); 
          }
        },
        parent: 'cp-view'
      })
  });
