
angular.module('os.biospecimen.specimen', ['ui.router'])
  .config(function($stateProvider) {
    $stateProvider
      .state('specimen-root', {
        url: '/specimens?specimenId&srId',
        template: '<div ui-view></div>',
        resolve: {
          specimen: function($stateParams, Specimen) {
            if ($stateParams.specimenId) {
              return Specimen.getById($stateParams.specimenId);
            } else if ($stateParams.srId) {
              return Specimen.getAnticipatedSpecimen($stateParams.srId);
            }
 
            return null;
          }
        },
        abstract: true,
        parent: 'visit-root'
      })
      .state('specimen-detail', {
        url: '/detail',
        templateUrl: 'modules/biospecimen/participant/specimen/detail.html',
        resolve: {
        },
        controller: function($scope, cpr, visit, specimen, Specimen) {
          $scope.cpr = cpr;
          $scope.visit = visit;
          $scope.specimen = specimen;

          $scope.childSpecimens = Specimen.flatten($scope.specimen.children);
          // 'SpecimenDetailCtrl'
        },
        parent: 'specimen-root'
      })
      .state('specimen-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/biospecimen/participant/specimen/overview.html',
        controller: function() {
          // 'SpecimenOverviewCtrl',
        },
        parent: 'specimen-detail'
      })
      .state('specimen-detail.annotations', {
        url: '/annotations',
        templateUrl: 'modules/biospecimen/participant/annotations.html',
        controller: function(specimen, $scope) {
          $scope.objectId = specimen.id;
          $scope.entityType = 'Specimen';
        },
        parent: 'specimen-detail'
      });
  });
