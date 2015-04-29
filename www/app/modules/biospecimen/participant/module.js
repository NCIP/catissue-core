
angular.module('os.biospecimen.participant', 
  [ 
    'ui.router',
    'os.biospecimen.common',
    'os.biospecimen.participant.list',
    'os.biospecimen.participant.detail',
    'os.biospecimen.participant.overview',
    'os.biospecimen.participant.visits',
    'os.biospecimen.participant.addedit',
    'os.biospecimen.participant.addvisit',
    'os.biospecimen.participant.collect-specimens',
    'os.biospecimen.participant.specimen-position',
    'os.biospecimen.visit',
    'os.biospecimen.specimen',
    'os.biospecimen.extensions.list',
    'os.biospecimen.extensions.addedit-record'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('participant-root', {
        template:'<div ui-view></div>',
        controller: function($scope) {
          $scope.participantResource = {
            registerOpts: {resource: 'ParticipantPhi', operations: ['Create']},
            updateOpts: {resource: 'ParticipantPhi', operations: ['Update']},
          },

          $scope.visitAndSpecimenResource = {
            createOpts: {resource: 'VisitAndSpecimen', operations: ['Create']},
            updateOpts: {resource: 'VisitAndSpecimen', operations: ['Update']},
            createOrUpdateOpts: {resource: 'VisitAndSpecimen', operations: ['Create', 'Update']}
          }
        },
        parent: 'signed-in',
        abstract: true
      })
      .state('participant-list', {
        url: '/participants?cpId',
        templateUrl: 'modules/biospecimen/participant/list.html',
        controller: 'ParticipantListCtrl',
        resolve: {
          cp: function($stateParams, CollectionProtocol) {
              return CollectionProtocol.getById($stateParams.cpId);
          }
        },
        parent: 'participant-root'
      })
      .state('participants', {
        url: '/participants/:cprId',
        template: '<div ui-view></div>',
        resolve: {
          cpr: function($stateParams, CollectionProtocolRegistration) {
            if (!!$stateParams.cprId && $stateParams.cprId > 0) {
              return CollectionProtocolRegistration.getById($stateParams.cprId);
            } 

            return new CollectionProtocolRegistration({registrationDate: new Date()});
          }
        },
        controller: function($scope, cpr) {
          $scope.cpr = $scope.object = cpr;
          $scope.entityType = 'Participant';
          $scope.extnState = 'participant-detail.extensions.'
        },
        parent: 'participant-root',
        abstract: true
      })
      .state('participant-addedit', {
        url: '/addedit-participant?cpId',
        templateProvider: function($stateParams, $q, CpConfigSvc) {
          return $q.when(CpConfigSvc.getRegParticipantTmpl($stateParams.cpId, $stateParams.cprId)).then(
            function(tmpl) {
              return '<div ng-include src="\'' + tmpl + '\'"></div>';
            }
          );
        },
        controllerProvider: function($stateParams, CpConfigSvc) {
          return CpConfigSvc.getRegParticipantCtrl($stateParams.cpId, $stateParams.cprId);
        },
        resolve: {
        },
        parent: 'participants'
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
        parent: 'participants'
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
      .state('participant-detail.collect-specimens', {
        url: '/collect-specimens?visitId&eventId',
        templateUrl: 'modules/biospecimen/participant/collect-specimens.html',
        controller: 'CollectSpecimensCtrl',
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
        parent: 'participants'
      })
      .state('participant-detail.extensions', {
        url: '/extensions',
        template: '<div ui-view></div>',
        controller: function($scope) {
          var opts = {cp: $scope.cpr.cpShortTitle};
          angular.extend($scope.participantResource.updateOpts, opts);
          $scope.extensionUpdateOpts = $scope.participantResource.updateOpts;
        },
        abstract: true,
        parent: 'participant-detail'
      })
      .state('participant-detail.extensions.list', {
        url: '/list',
        templateUrl: 'modules/biospecimen/extensions/list.html',
        controller: 'FormsListCtrl', 
        parent: 'participant-detail.extensions'
      })
      .state('participant-detail.extensions.addedit', {
        url: '/addedit?formId&recordId&formCtxId',
        templateUrl: 'modules/biospecimen/extensions/addedit.html',
        resolve: {
          formDef: function($stateParams, Form) {
            return Form.getDefinition($stateParams.formId);
          }
        },
        controller: 'FormRecordAddEditCtrl',
        parent: 'participant-detail.extensions'
      });
  });
