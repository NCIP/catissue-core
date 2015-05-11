angular.module('os.biospecimen.specimenlist', 
  [ 
    'os.biospecimen.specimenlist.list',
    'os.biospecimen.specimenlist.addedit'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('specimen-list-root', {
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope) {
          // Specimen list Authorization Options
        },
        parent: 'signed-in'
      })
      .state('specimen-list', {
        url: '/specimen-list',
        templateUrl: 'modules/biospecimen/specimen-list/list.html',
        controller: 'SpecimenListsCtrl',
        parent: 'signed-in'
      });
  });


