
angular.module('os.biospecimen.participant', 
  [ 
    'ui.router',
    'os.biospecimen.common',
    'os.biospecimen.participant.root',
    'os.biospecimen.participant.list',
    'os.biospecimen.participant.detail',
    'os.biospecimen.participant.overview',
    'os.biospecimen.participant.visits',
    'os.biospecimen.participant.addedit',
    'os.biospecimen.participant.newreg',
    'os.biospecimen.participant.collect-specimens',
    'os.biospecimen.participant.consents',
    'os.biospecimen.participant.search',
    'os.biospecimen.visit',
    'os.biospecimen.specimen',
    'os.biospecimen.extensions.list',
    'os.biospecimen.extensions.addedit-record'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('cp-view', {
        url: '/cp-view/:cpId',
        template: '<div ui-view></div>',
        controller: function($scope, cp) {
          $scope.cp = cp;
        },
        resolve: {
          cp: function($stateParams, CollectionProtocol) {
            return CollectionProtocol.getById($stateParams.cpId);
          }
        },
        parent: 'signed-in',
        abstract: true
      })
      .state('participant-list', {
        url: '/participants',
        templateUrl: 'modules/biospecimen/participant/list.html',
        controller: 'ParticipantListCtrl',
        resolve: {
          catalogQuery: function(cp) {
            if (cp.catalogQuery) {
              return cp.catalogQuery;
            }

            return cp.getCatalogQuery().then(
              function(query) {
                cp.catalogQuery = query;
              }
            );
          }
        },
        parent: 'cp-view'
      })
      .state('participant-root', {
        url: '/participants/:cprId',
        template: '<div ui-view></div>',
        resolve: {
          cpr: function($stateParams, CollectionProtocolRegistration) {
            if (!!$stateParams.cprId && $stateParams.cprId > 0) {
              return CollectionProtocolRegistration.getById($stateParams.cprId);
            } 

            return new CollectionProtocolRegistration({registrationDate: new Date()});
          },

          hasSde: function($injector) {
            return $injector.has('sdeFieldsSvc');
          },

          sysDict: function($stateParams, hasSde, CpConfigSvc) {
            if (!hasSde) {
              return [];
            }

            return undefined || []; // CpConfigSvc.getDictionary(null);
          },

          cpDict: function($stateParams, hasSde, CpConfigSvc) {
            if (!hasSde) {
              return [];
            }

            return CpConfigSvc.getDictionary($stateParams.cpId, []);
          },

          hasDict: function(hasSde, sysDict, cpDict) {
            return hasSde && (cpDict.length > 0 || sysDict.length > 0);
          }
        },
        controller: 'ParticipantRootCtrl',
        parent: 'cp-view',
        abstract: true
      })
      .state('participant-addedit', {
        url: '/addedit-participant',
        templateProvider: function($stateParams, $q, CpConfigSvc, PluginReg) {
          return $q.when(CpConfigSvc.getRegParticipantTmpl($stateParams.cpId, $stateParams.cprId)).then(
            function(tmpl) {
              var tmpls = PluginReg.getTmpls("participant-addedit", "page-body", tmpl); 
              return '<div ng-include src="\'' + tmpls[0] + '\'"></div>';
            }
          );
        },
        controllerProvider: function($stateParams, CpConfigSvc) {
          return CpConfigSvc.getRegParticipantCtrl($stateParams.cpId, $stateParams.cprId);
        },
        resolve: {
          extensionCtxt: function(Participant) {
            return Participant.getExtensionCtxt();
          }
        },
        parent: 'participant-root'
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
      .state('participant-newreg', {
        url: '/newreg',
        templateUrl: 'modules/biospecimen/participant/register-new.html',
        controller: 'RegisterNewCtrl',
        parent: 'participant-detail'
      })
      .state('participant-detail.overview', {
        url: '/overview',
        templateProvider: function(sdeView, $q, PluginReg) {
          var viewName = 'participant-detail',
              secName  = 'overview',
              defTmpl  = 'modules/biospecimen/participant/overview.html';

          if (sdeView) {
            viewName = 'sde-participant-detail',
            defTmpl = 'plugin-ui-resources/sde/samples-overview.html'
          }

          return $q.when(PluginReg.getTmpls(viewName, secName, defTmpl)).then(
            function(tmpls) {
              return '<div ng-include src="\'' + tmpls[0] + '\'"></div>';
            }
          );
        },
        controllerProvider: function(sdeView) {
          return sdeView ? 'sdeSamplesOverviewCtrl' : 'ParticipantOverviewCtrl';
        },
        resolve: {
          sdeView: function($stateParams, visits, hasSde, CpConfigSvc) {
            if (!hasSde || visits.length > 1) {
              return false;
            }

            return CpConfigSvc.getWorkflowData($stateParams.cpId, 'sde').then(
              function(data) {
                var fields = data.singlePatientSamples;
                return angular.isArray(fields) && fields.length > 0;
              }
            );
          }
        },
        parent: 'participant-detail'
      })
      .state('participant-detail.consents', {
        url: '/consents',
        templateUrl: 'modules/biospecimen/participant/consents.html',
        resolve: {
          consent: function(cpr) {
            return cpr.getConsents();
          }
        },
        controller: 'ParticipantConsentsCtrl',
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
        parent: 'participant-root'
      })
      .state('participant-detail.extensions', {
        url: '/extensions',
        template: '<div ui-view></div>',
        controller: function($scope, cpr) {
          $scope.extnOpts = {
            update: $scope.participantResource.updateOpts,
            isEntityActive: cpr.activityStatus == 'Active',
            entity: cpr
          }
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
          },
          postSaveFilters: function() {
            return [];
          }
        },
        controller: 'FormRecordAddEditCtrl',
        parent: 'participant-detail.extensions'
      })
      .state('bulk-registrations', {
        url: '/bulk-registrations',
        templateProvider: function($stateParams, $q, CpConfigSvc) {
          return $q.when(CpConfigSvc.getBulkRegParticipantTmpl($stateParams.cpId, $stateParams.cprId)).then(
            function(tmpl) {
              return '<div ng-include src="\'' + tmpl + '\'"></div>';
            }
          );
        },
        controllerProvider: function($stateParams, CpConfigSvc) {
          return CpConfigSvc.getBulkRegParticipantCtrl($stateParams.cpId, $stateParams.cprId);
        },
        parent: 'participant-root'
      })
      .state('participant-search', {
        url: '/participant-search',
        templateUrl: 'modules/biospecimen/participant/search-result.html',
        resolve: {
          participants: function(ParticipantSearchSvc) {
            return ParticipantSearchSvc.getParticipants();
          },

          searchKey: function(ParticipantSearchSvc) {
            return ParticipantSearchSvc.getSearchKey();
          }
        },
        controller: 'ParticipantResultsView',
        parent: 'signed-in'
      });
  })

  .run(function(QuickSearchSvc, ParticipantSearchSvc) {
    var opts = {
      template: 'modules/biospecimen/participant/quick-search.html',
      caption: 'entities.participant',
      order : 1,
      search: ParticipantSearchSvc.search
    };

    QuickSearchSvc.register('participant', opts);
  });
