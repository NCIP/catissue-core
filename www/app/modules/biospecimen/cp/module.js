
angular.module('os.biospecimen.cp', 
  [
    'ui.router',
    'os.biospecimen.cp.list',
    'os.biospecimen.cp.addedit',
    'os.biospecimen.cp.import',
    'os.biospecimen.cp.detail',
    'os.biospecimen.cp.consents',
    'os.biospecimen.cp.events',
    'os.biospecimen.cp.specimens',
    'os.biospecimen.cp.catalog'
  ])

  .config(function($stateProvider) {
    $stateProvider
      .state('cps', {
        url: '/cps',
        abstract: true,
        template: '<div ui-view></div>',
        controller: function($scope, cpsCtx) {
          $scope.cpsCtx = cpsCtx;

          // Collection Protocol Authorization Options
          $scope.cpResource = {
            createOpts: {resource: 'CollectionProtocol', operations: ['Create']},
            updateOpts: {resource: 'CollectionProtocol', operations: ['Update']},
            deleteOpts: {resource: 'CollectionProtocol', operations: ['Delete']}
          }
          
          $scope.participantResource = {
            createOpts: {resource: 'ParticipantPhi', operations: ['Create']},
            updateOpts: {resource: 'ParticipantPhi', operations: ['Update']}
          }
          
          $scope.specimenResource = {
            updateOpts: {resource: 'VisitAndSpecimen', operations: ['Create', 'Update']}
          }
          
          $scope.codingEnabled = $scope.global.appProps.cp_coding_enabled;
        },
        resolve: {
          cpsCtx: function(currentUser, AuthorizationService) {
            return {
              participantUpdateAllowed: AuthorizationService.isAllowed({
                resource: 'ParticipantPhi',
                operations: ['Create', 'Update']
              }),

              visitSpecimenUpdateAllowed: AuthorizationService.isAllowed({
                resource: 'VisitAndSpecimen',
                operations: ['Create', 'Update']
              })
            }
          }
        },
        parent: 'signed-in'
      })
      .state('cp-list', {
        url: '', 
        templateUrl: 'modules/biospecimen/cp/list.html',
        controller: 'CpListCtrl',
        parent: 'cps',
        resolve: {
          cpList: function(CollectionProtocol, ListPagerOpts) {
            return CollectionProtocol.list({maxResults: ListPagerOpts.MAX_PAGE_RECS + 1});
          },
          
          view: function($rootScope, $state, cpList) {
            if ($rootScope.stateChangeInfo.fromState.name == 'login') {
              if (cpList.length == 1) {
                $state.go('participant-list', {cpId: cpList[0].id});
              } else if (cpList.length == 0) {
                $state.go('home');
              }
            }
          }
        }
      })
      .state('cp-addedit', {
        url: '/addedit/:cpId?mode',
        templateUrl: 'modules/biospecimen/cp/addedit.html',
        resolve: {
          cp: function($stateParams, CollectionProtocol) {
            if ($stateParams.cpId) {
              return CollectionProtocol.getById($stateParams.cpId);
            }
            return new CollectionProtocol();
          },
          extensionCtxt: function(CollectionProtocol) {
            return CollectionProtocol.getExtensionCtxt();
          }
        },
        controller: 'CpAddEditCtrl',
        parent: 'cps'
      })
      .state('cp-import', {
        url: '/import',
        templateUrl: 'modules/biospecimen/cp/import.html',
        controller: 'CpImportCtrl',
        parent: 'cps'
      })
      .state('import-multi-cp-objs', {
        url: '/import-multi-cp-objs',
        templateUrl: 'modules/common/import/add.html',
        controller: 'ImportObjectCtrl',
        resolve: {
          cp: function(CollectionProtocol) {
            return new CollectionProtocol({id: -1});
          },

          allowedEntityTypes: function(cpsCtx) {
            var entityTypes = [];
            if (cpsCtx.participantUpdateAllowed) {
              entityTypes.push('Participant');
            }

            if (cpsCtx.visitSpecimenUpdateAllowed) {
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
        parent: 'cps'
      })
      .state('import-multi-cp-jobs', {
        url: '/import-multi-cp-jobs',
        templateUrl: 'modules/common/import/list.html',
        controller: 'ImportJobsListCtrl',
        resolve: {
          importDetail: function() {
            return {
              breadcrumbs: [
                {state: 'cp-list', title: "cp.list"}
              ],
              title: 'bulk_imports.jobs_list',
              objectTypes: [
                "cprMultiple", 'otherCpr', 'cpr', 'participant', 'consent', 'visit',
                'specimen', 'specimenDerivative', 'specimenAliquot',
                'masterSpecimen', 'extensions'
              ],
              objectParams: {cpId: -1}
            }
          }
        },
        parent: 'cps'
      })
      .state('cp-detail', {
        url: '/:cpId',
        templateUrl: 'modules/biospecimen/cp/detail.html',
        parent: 'cps',
        resolve: {
          cp: function($stateParams, CollectionProtocol) {
            return CollectionProtocol.getById($stateParams.cpId);
          }
        },
        controller: 'CpDetailCtrl',
        breadcrumb: {
          title: '{{cp.title}}',
          state: 'cp-detail.overview'
        }
      })
      .state('cp-detail.overview', {
        url: '/overview',
        templateUrl: 'modules/biospecimen/cp/overview.html',
        parent: 'cp-detail'
      })
      .state('cp-detail.consents', {
        url: '/consents',
        templateUrl: 'modules/biospecimen/cp/consents.html',
        parent: 'cp-detail',
        resolve: {
          consentTiers: function(cp) {
            return cp.getConsentTiers();
          }
        },
        controller: 'CpConsentsCtrl'
      })
      .state('cp-detail.events', {
        templateUrl: 'modules/biospecimen/cp/events.html',
        parent: 'cp-detail',
        resolve: {
          events: function($stateParams, cp, CollectionProtocolEvent) {
            return CollectionProtocolEvent.listFor(cp.id);
          }
        },
        controller: 'CpEventsCtrl'
      })
      .state('cp-detail.specimen-requirements', {
        url: '/specimen-requirements?eventId',
        templateUrl: 'modules/biospecimen/cp/specimens.html',
        parent: 'cp-detail.events',
        resolve: {
          specimenRequirements: function($stateParams, SpecimenRequirement) {
            var eventId = $stateParams.eventId;
            if (!eventId) {
              return [];
            }

            return SpecimenRequirement.listFor(eventId);
          }
        },
        controller: 'CpSpecimensCtrl'
      })
      .state('cp-detail.settings', {
        url: '/settings',
        abstract: true,
        template: '<div class="clearfix">' +
                  '  <div class="col-xs-12">' +
                  '    <div ui-view></div>' +
                  '  </div>' +
                  '</div>',
        parent: 'cp-detail'
      })
      .state('cp-detail.settings.labels', {
        url: '/label',
        templateUrl: 'modules/biospecimen/cp/label-settings.html',
        parent: 'cp-detail.settings',
        controller: 'CpLabelSettingsCtrl'
      })
      .state('cp-detail.settings.catalog', {
        url: '/catalog',
        templateUrl: 'modules/biospecimen/cp/catalog-settings.html',
        parent: 'cp-detail.settings',
        resolve: {
          catalogSetting: function(cp) {
            if (cp.catalogSetting) {
              return cp.catalogSetting;
            }

            return cp.getCatalogSetting().then(
              function(setting) {
                cp.catalogSetting = setting || {};
              }
            );
          }
        },
        controller: 'CpCatalogSettingsCtrl'
      })
      .state('cp-detail.settings.container', {
        url: '/container',
        templateUrl: 'modules/biospecimen/cp/container-settings.html',
        parent: 'cp-detail.settings',
        controller: 'CpContainerSettingsCtrl'
      })
      .state('cp-detail.settings.reporting', {
        url: '/reporting',
        templateUrl: 'modules/biospecimen/cp/report-settings.html',
        parent: 'cp-detail.settings',
        resolve: {
          reportSettings: function(cp) {
            if (cp.reportSettings) {
              return cp.reportSettings;
            }

            return cp.getReportSettings().then(
              function(settings) {
                cp.reportSettings = settings || {enabled: true};
              }
            );
          }
        },
        controller: 'CpReportSettingsCtrl'
      })
    });
  
