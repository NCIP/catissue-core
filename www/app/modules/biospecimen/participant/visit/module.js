
angular.module('os.biospecimen.visit', [ 
    'ui.router',
    'os.biospecimen.participant.specimen-tree',
    'os.biospecimen.extensions',
    'os.biospecimen.visit.addedit',
    'os.biospecimen.visit.spr',
    'os.biospecimen.visit.detail'
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
        controller: function($scope, cpr, visit) {
          $scope.visit = $scope.object = visit;
          $scope.entityType = 'SpecimenCollectionGroup';
          $scope.extnState = 'visit-detail.extensions.'
        },
        abstract: true,
        parent: 'participant-root'
      })
      .state('visit-addedit', {
        url: '/addedit-visit',
        templateUrl: 'modules/biospecimen/participant/visit/addedit.html',
        controller: 'AddEditVisitCtrl',
        parent: 'visit-root'
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
        controller: 'VisitDetailCtrl',
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
      .state('visit-detail.extensions', {
        url: '/extensions',
        template: '<div ui-view></div>',
        controller: function($scope, visit) {
          $scope.extnOpts = {
            update: $scope.specimenResource.updateOpts,
            entity: visit,
            isEntityActive: visit.activityStatus == 'Active'
          }
        },
        abstract: true,
        parent: 'visit-detail'
      })
      .state('visit-detail.extensions.list', {
        url: '/list',
        templateUrl: 'modules/biospecimen/extensions/list.html',
        controller: 'FormsListCtrl',
        parent: 'visit-detail.extensions'
      })
      .state('visit-detail.extensions.addedit', {
        url: '/addedit?formId&recordId&formCtxId',
        templateUrl: 'modules/biospecimen/extensions/addedit.html',
        resolve: {
          formDef: function($stateParams, Form) {
            return Form.getDefinition($stateParams.formId);
          }
        },
        controller: 'FormRecordAddEditCtrl',
        parent: 'visit-detail.extensions'
      })
      .state('visit-detail.spr', {
        url: '/spr',
        templateUrl: 'modules/biospecimen/participant/visit/spr.html',
        controller: 'VisitSprCtrl',
        parent: 'visit-detail'
      });
  });
