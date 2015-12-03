angular.module('os.biospecimen.specimenlist', 
  [ 
    'os.biospecimen.specimenlist.list',
    'os.biospecimen.specimenlist.addedit',
    'os.biospecimen.specimenlist.specimensholder',
    'os.biospecimen.specimenlist.assignto',
    'os.biospecimen.specimenlist.reqspmns'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('specimen-list', {
        url: '/specimen-list?listId',
        templateUrl: 'modules/biospecimen/specimen-list/list.html',
        controller: 'SpecimenListsCtrl',
        parent: 'signed-in'
      })
      .state('specimen-list-addedit', {
        url: '/specimen-list/:listId/addedit',
        templateUrl: 'modules/biospecimen/specimen-list/addedit.html',
        resolve: {
          list: function($stateParams, SpecimenList) {
            if ($stateParams.listId) {
              return SpecimenList.getById($stateParams.listId);
            }
            return new SpecimenList();
          }
        },
        controller: 'AddEditSpecimenListCtrl',
        parent: 'signed-in'
      })
      .state('specimen-list-request', {
        url: '/specimen-list/:listId/request-specimens',
        templateUrl: 'modules/biospecimen/specimen-list/request-specimens.html',
        resolve: {
          list: function($stateParams, SpecimenList) {
            return SpecimenList.getById($stateParams.listId);
          },

          reqFormIds: function($stateParams, SpecimenRequest) {
            return SpecimenRequest.getRequestFormIds($stateParams.listId);
          }
        },
        controller: 'RequestSpecimensCtrl',
        parent: 'signed-in'
      });
  });


