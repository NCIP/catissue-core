angular.module('os.biospecimen.specimenlist', 
  [ 
    'os.biospecimen.specimenlist.name',
    'os.biospecimen.specimenlist.list',
    'os.biospecimen.specimenlist.addedit',
    'os.biospecimen.specimenlist.specimensholder',
    'os.biospecimen.specimenlist.assignto'
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
        url: '/specimen-list-addedit/:listId',
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
      });
  });


