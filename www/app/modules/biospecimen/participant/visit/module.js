
angular.module('os.biospecimen.visit', [ 
    'ui.router',
    'os.biospecimen.participant.specimen-tree'
  ])
  .config(function($stateProvider) {
    $stateProvider
      .state('visit-root', {
        url: '/visits?visitId&eventId',
        template: '<div ui-view></div>',
        resolve: {
          visit: function($stateParams, cpr, Visit) {
            if (!!$stateParams.visitId) {
              return Visit.getById($stateParams.visitId);
            } else if (!!$stateParams.eventId) {
              return Visit.getAnticipatedVisit($stateParams.eventId, cpr.registrationDate);
            }

            return null;           
          }
        },
        abstract: true,
        parent: 'participant-root'
      })
      .state('visit-detail', {
        url: '/detail',
        templateUrl: 'modules/biospecimen/participant/visit/detail.html',
        resolve: {
          specimens: function(cpr, visit, Specimen) {
            var criteria = { visitId: visit.id, eventId: visit.eventId };
            return Specimen.listFor(cpr.id, criteria);
          }
        },
        controller: function($scope, cpr, visit, specimens, Specimen) {
          $scope.cpr = cpr;
          $scope.visit = visit;
          $scope.specimens = Specimen.flatten(specimens);

          // 'VisitDetailCtrl'
        },
        parent: 'visit-root'
      })
      .state('visit-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/biospecimen/participant/visit/overview.html',
        controller: function() {
          // 'ParticipantOverviewCtrl',
        },
        parent: 'visit-detail'
      })
      .state('visit-detail.annotations', {
        url: '/annotations',
        templateUrl: 'modules/biospecimen/participant/annotations.html',
        controller: function(cpr, $scope) {
          $scope.objectId = cpr.id;
          $scope.entityType = 'Visit';
        },
        parent: 'visit-detail'
      });
  });
