
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
        controller: function($scope, cp, cpViewCtx) {
          $scope.cp = cp;
          $scope.cpViewCtx = cpViewCtx;

          var sites = cp.cpSites.map(function(cpSite) { return cpSite.siteName; });
          $scope.partRegOpts =        {cp: cp.shortTitle, sites: sites, resource: 'ParticipantPhi', operations: ['Create']};
          $scope.orderCreateOpts =    {cp: cp.shortTitle, sites: sites, resource: 'Order', operations: ['Create']};
          $scope.shipmentCreateOpts = {cp: cp.shortTitle, sites: sites, resource: 'ShippingAndTracking', operations: ['Create']};
          $scope.specimenUpdateOpts = {cp: cp.shortTitle, sites: sites, resource: 'VisitAndSpecimen', operations: ['Update']};
          $scope.specimenDeleteOpts = {cp: cp.shortTitle, sites: sites, resource: 'VisitAndSpecimen', operations: ['Delete']};
        },
        resolve: {
          cp: function($stateParams, CollectionProtocol) {
            return CollectionProtocol.getById($stateParams.cpId);
          },

          cpViewCtx: function(cp, AuthorizationService) {
            return {
              participantUpdateAllowed: AuthorizationService.isAllowed({
                resource: 'ParticipantPhi',
                operations: ['Create', 'Update'],
                cp: cp.shortTitle
              }),

              visitSpecimenUpdateAllowed: AuthorizationService.isAllowed({
                resource: 'VisitAndSpecimen',
                operations: ['Create', 'Update'],
                cp: cp.shortTitle
              })
            }
          },

          listView: function(cp, CpConfigSvc) {
            return CpConfigSvc.getListView(cp.id, 'participant-list');
          },

          reqBasedDistOrShip: function($injector) {
            if ($injector.has('spmnReqCfgUtil')) {
              return $injector.get('spmnReqCfgUtil').isReqBasedDistOrShippingEnabled();
            } else {
              return {value: false};
            }
          }
        },
        parent: 'signed-in',
        abstract: true
      })
      .state('cp-summary-view', {
        url: '/summary-view',
        controller: function($state, cp, summaryView) {
          $state.go(summaryView, {cpId: cp.id}, {location: 'replace'});
        },
        resolve: {
          summaryView: function(listView, CpConfigSvc) {
            var summaryView = listView;

            var summarySt = CpConfigSvc.getSummaryState();
            if (summarySt) {
              summaryView = summarySt;
            }

            return summaryView;
          }
        },
        parent: 'cp-view'
      })
      .state('cp-list-view', {
        url: '/list-view',
        controller: function($state, cp, listView) {
          $state.go(listView, {cpId: cp.id}, {location: 'replace'});
        },
        parent: 'cp-view'
      })
      .state('cp-list-view-root', {
        templateUrl: 'modules/biospecimen/participant/list-view.html',
        controller: function($scope, cp, defSopDoc, defSopUrl, spmnListCfg, Alerts) {
          var ctx = $scope.listViewCtx = {
            sopDocDownloadUrl: cp.getSopDocDownloadUrl(),
            spmnListCfg: spmnListCfg
          };

          ctx.sopDoc = cp.sopDocumentName;
          if (!ctx.sopDoc) {
            ctx.sopUrl = cp.sopDocumentUrl;
            if (!ctx.sopUrl) {
              ctx.sopDoc = defSopDoc.value;
              if (!ctx.sopDoc) {
                ctx.sopUrl = defSopUrl.value;
              }
            }
          }

          $scope.generateCpReport = function() {
            cp.generateReport().then(
              function() {
                Alerts.success('participant.report_gen_initiated', cp);
              }
            );
          }
        },
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
          },

          defSopDoc: function(cp, SettingUtil) {
            if (!!cp.sopDocumentName || !!cp.sopDocumentUrl) {
              return null;
            }

            return SettingUtil.getSetting('biospecimen', 'cp_sop_doc');
          },

          defSopUrl: function(cp, defSopDoc, SettingUtil) {
            if (!!cp.sopDocumentName || !!cp.sopDocumentUrl || !!defSopDoc.value) {
              return null;
            }

            return SettingUtil.getSetting('biospecimen', 'cp_sop_doc_url');
          },

          spmnListCfg: function(cp, CpConfigSvc) {
            return CpConfigSvc.getListConfig(cp, 'specimen-list-view');
          }
        },
        parent: 'cp-view',
        abstract: true
      })
      .state('participant-list', {
        url: '/participants',
        templateUrl: 'modules/biospecimen/participant/list.html',
        controller: 'ParticipantListCtrl',
        resolve: {
          participantListCfg: function(cp, CpConfigSvc) {
            return CpConfigSvc.getListConfig(cp, 'participant-list-view');
          }
        },
        metaInfo: {
          button: {
            icon: 'fa-group',
            label: 'cp.view_participants'
          }
        },
        parent: 'cp-list-view-root'
      })
      .state('cp-specimens', {
        url: '/specimens',
        templateUrl: 'modules/biospecimen/participant/specimens-list.html',
        controller: 'SpecimensListViewCtrl',
        metaInfo: {
          button: {
            icon: 'fa-flask',
            label: 'cp.view_specimens'
          }
        },
        parent: 'cp-list-view-root'
      })
      .state('import-cp-objs', {
        url: '/import-cp-objs',
        templateUrl: 'modules/common/import/add.html',
        controller: 'ImportObjectCtrl',
        resolve: {
          allowedEntityTypes: function(cpViewCtx) {
            var entityTypes = [];
            if (cpViewCtx.participantUpdateAllowed) {
              entityTypes.push('Participant');
            }

            if (cpViewCtx.visitSpecimenUpdateAllowed) {
              entityTypes = entityTypes.concat(['SpecimenCollectionGroup', 'Specimen', 'SpecimenEvent']);
            }

            return entityTypes;
          },

          forms: function(cp, allowedEntityTypes) {
            return allowedEntityTypes.length > 0 ? cp.getForms(allowedEntityTypes) : [];
          },

          importDetail: function(cp, allowedEntityTypes, forms, ImportUtil) {
            return ImportUtil.getImportDetail(cp, allowedEntityTypes, forms);
          }
        },
        parent: 'cp-view'
      })
      .state('import-cp-jobs', {
        url: '/import-cp-jobs',
        templateUrl: 'modules/common/import/list.html',
        controller: 'ImportJobsListCtrl',
        resolve: {
          importDetail: function(cp) {
            return {
              breadcrumbs: [
                {state: 'cp-list-view', title:  cp.shortTitle,     params: '{cpId:' + cp.id + '}'}
              ],
              title: 'bulk_imports.jobs_list',
              objectTypes: [
                'cpr', 'participant', 'consent', 'visit',
                'specimen', 'specimenDerivative', 'specimenAliquot',
                'masterSpecimen', 'extensions'
              ],
              objectParams: {cpId: cp.id}
            }
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
          },

          hasFieldsFn: function($injector, hasDict, sysDict, cpDict) {
            return function(inObjs, exObjs) {
              if (!hasDict) {
                return true;
              }

              return $injector.get('sdeFieldsSvc').commonFns()
                .hasFields(sysDict, cpDict, inObjs, exObjs);
            }
          },

          pendingSpmnsDispInterval: function(SettingUtil) {
            return SettingUtil.getSetting('biospecimen', 'pending_spmns_disp_interval');
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
          extensionCtxt: function(cp, Participant) {
            return Participant.getExtensionCtxt({cpId: cp.id});
          },
          twoStepReg: function(SettingUtil) {
            return SettingUtil.getSetting('biospecimen', 'two_step_patient_reg').then(
              function(setting) {
                return setting.value == 'true';
              }
            );
          },
          lockedFields: function(CpConfigSvc) {
            return CpConfigSvc.getLockedParticipantFields();
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
        templateProvider: function($q, PluginReg) {
          var defTmpl  = 'modules/biospecimen/participant/overview.html';
          return $q.when(PluginReg.getTmpls('participant-detail', 'overview', defTmpl)).then(
            function(tmpls) {
              return '<div ng-include src="\'' + tmpls[0] + '\'"></div>';
            }
          );
        },
        controller: 'ParticipantOverviewCtrl',
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
