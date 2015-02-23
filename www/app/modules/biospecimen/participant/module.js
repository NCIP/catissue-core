
angular.module('os.biospecimen.participant', 
  [ 
    'ui.router',
    'os.biospecimen.participant.list',
    'os.biospecimen.participant.detail',
    'os.biospecimen.participant.overview',
    'os.biospecimen.participant.visits',
    'os.biospecimen.participant.addedit',
    'os.biospecimen.participant.addvisit',
    'os.biospecimen.visit',
    'os.biospecimen.specimen'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('participant-list', {
        url: '/participants?cpId',
        templateUrl: 'modules/biospecimen/participant/list.html',
        controller: 'ParticipantListCtrl',
        parent: 'signed-in'
      })
      .state('participant-new', {
        url: '/new-participant?cpId',
        templateProvider: function($stateParams, CpConfigSvc) {
          var tmpl = CpConfigSvc.getRegParticipantTmpl($stateParams.cpId);
          return '<div ng-include src="\'' + tmpl + '\'"></div>';
        },
        controllerProvider: function($stateParams, CpConfigSvc) {
          return CpConfigSvc.getRegParticipantCtrl($stateParams.cpId);
        },
        parent: 'signed-in'
      })
      .state('participant-root', {
        url: '/participants/:cprId',
        template: '<div ui-view></div>',
        resolve: {
          cpr: function($stateParams, CollectionProtocolRegistration) {
            return CollectionProtocolRegistration.getById($stateParams.cprId);
          }
        },
        controller: function() {
        },
        parent: 'signed-in',
        abstract: true
      })
      .state('participant-detail', {
        url: '/detail',
        templateUrl: 'modules/biospecimen/participant/detail.html',
        resolve: {
          visits: function($stateParams, Visit) {
            return Visit.listFor($stateParams.cprId, true);
          }
        },
        controller: 'ParticipantDetailCtrl',
        parent: 'participant-root'
      })
      .state('participant-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/biospecimen/participant/overview.html',
        controller: 'ParticipantOverviewCtrl',
        parent: 'participant-detail'
      })
      .state('participant-detail.consents', {
        url: '/consents',
        templateUrl: 'modules/biospecimen/participant/consents.html',
        controller: function() {
        },
        parent: 'participant-detail'
      })
      .state('participant-detail.visits', {
        url: '/visits-summary?eventId&visitId',
        templateUrl: 'modules/biospecimen/participant/visits.html',
        controller: 'ParticipantVisitsTreeCtrl',
        resolve: {
          specimens: function($stateParams, Specimen) {
            if (!$stateParams.visitId && !$stateParams.eventId) {
              return [];
            }

            var visitDetail = {
              visitId: $stateParams.visitId, 
              eventId: $stateParams.eventId
            };
            return Specimen.listFor($stateParams.cprId, visitDetail);
          }
        },
        parent: 'participant-detail'
      })
      .state('participant-detail.annotations', {
        url: '/annotations',
        templateUrl: 'modules/biospecimen/participant/annotations.html',
        controller: function(cpr, $scope) {
          $scope.objectId = cpr.id;
          $scope.entityType = 'Participant';
        },
        parent: 'participant-detail'
      });
  });
