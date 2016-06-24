angular.module('os.biospecimen.specimenlist', 
  [ 
    'os.biospecimen.specimenlist.name',
    'os.biospecimen.specimenlist.addedit',
    'os.biospecimen.specimenlist.specimensholder',
    'os.biospecimen.specimenlist.assignto'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('specimen-lists', {
        url: '/specimen-lists',
        templateUrl: 'modules/biospecimen/specimen-list/lists.html',
        controller: 'SpecimenListsCtrl',
        resolve: {
          lists: function(SpecimenList) {
            return SpecimenList.query({includeStats: true});
          }
        },
        parent: 'signed-in'
      })
      .state('specimen-list-root', {
        url: '/specimen-lists/:listId',
        template: '<div ui-view></div>',
        resolve: {
          list: function($stateParams, SpecimenList) {
            if (!!$stateParams.listId || $stateParams.listId == 0) {
              return SpecimenList.getById($stateParams.listId);
            }

            return new SpecimenList();
          }
        },
        abstract: true,
        parent: 'signed-in'
      })
      .state('specimen-list', {
        url: '/',
        params: {
          breadcrumbs: [
            {state: 'specimen-lists', params: {}, captionKey: 'specimen_list.lists'}
          ]
        },
        templateUrl: 'modules/biospecimen/specimen-list/specimens.html',
        controller: 'SpecimenListSpecimensCtrl',
        resolve: {
          reqBasedDistOrShip: function($injector) {
            if ($injector.has('spmnReqCfgUtil')) {
              return $injector.get('spmnReqCfgUtil').isReqBasedDistOrShippingEnabled();
            } else {
              return {value: false};
            }
          }
        },
        parent: 'specimen-list-root'
      })
      .state('specimen-list-addedit', {
        url: '/specimen-list/:listId/addedit',
        templateUrl: 'modules/biospecimen/specimen-list/addedit.html',
        controller: 'AddEditSpecimenListCtrl',
        parent: 'specimen-list-root'
      });
  });
