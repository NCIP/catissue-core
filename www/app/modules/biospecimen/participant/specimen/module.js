
angular.module('os.biospecimen.specimen', 
  [
    'ui.router',
    'os.biospecimen.specimen.addedit',
    'os.biospecimen.specimen.detail',
    'os.biospecimen.specimen.overview',
    'os.biospecimen.specimen.close',
    'os.biospecimen.specimen.addaliquots',
    'os.biospecimen.specimen.addderivative',
    'os.biospecimen.specimen.bulkaddevent',
    'os.biospecimen.specimen.search'
  ])
  .config(function($stateProvider) {
    $stateProvider
      .state('specimen', {
        url: '/specimens/:specimenId',
        controller: function($state, params) {
          $state.go('specimen-detail.overview', params, {location: 'replace'});
        },
        resolve: {
          params: function($stateParams, Specimen) {
            return Specimen.getRouteIds($stateParams.specimenId);
          }
        },
        parent: 'signed-in'
      })
      .state('specimen-root', {
        url: '/specimens?specimenId&srId',
        template: '<div ui-view></div>',
        resolve: {
          specimen: function($stateParams, cpr, Specimen) {
            if ($stateParams.specimenId) {
              return Specimen.getById($stateParams.specimenId);
            } else if ($stateParams.srId) {
              return Specimen.getAnticipatedSpecimen($stateParams.srId);
            }
 
            return new Specimen({
              lineage: 'New', 
              visitId: $stateParams.visitId, 
              labelFmt: cpr.specimenLabelFmt
            });
          }
        },
        controller: function($scope, specimen) {
          $scope.specimen = $scope.object = specimen;
          $scope.entityType = 'Specimen';
          $scope.extnState = 'specimen-detail.extensions.';
        },
        abstract: true,
        parent: 'visit-root'
      })
      .state('specimen-addedit', {
        url: '/addedit-specimen',
        templateProvider: function(PluginReg, $q) {
          var defaultTmpl = "modules/biospecimen/participant/specimen/addedit.html";
          return $q.when(PluginReg.getTmpls("specimen-addedit", "page-body", defaultTmpl)).then(
            function(tmpls) {
              return '<div ng-include src="\'' + tmpls[0] + '\'"></div>';
            }
          );
        },
        resolve: {
          extensionCtxt: function(cp, specimen) {
            return specimen.getExtensionCtxt({cpId: cp.id});
          },

          reservedPosition: function(cp, specimen, Container) {
            if (!cp.containerSelectionStrategy) {
              return null;
            }

            if (specimen.storageType == 'Virtual' || (!!specimen.status && specimen.status != 'Pending')) {
              return null;
            }

            return Container.reservePositionForSpecimen(cp.id, specimen);
          }
        },
        controller: 'AddEditSpecimenCtrl',
        parent: 'specimen-root'
      })
      .state('specimen-detail', {
        url: '/detail',
        templateUrl: 'modules/biospecimen/participant/specimen/detail.html',
        controller: 'SpecimenDetailCtrl',
        parent: 'specimen-root'
      })
      .state('specimen-detail.overview', {
        url: '/overview',
        templateProvider: function(PluginReg, $q) {
          var defaultTmpl = "modules/biospecimen/participant/specimen/overview.html";
          return $q.when(PluginReg.getTmpls("specimen-detail", "overview", defaultTmpl)).then(
            function(tmpls) {
              return '<div ng-include src="\'' + tmpls[0] + '\'"></div>';
            }
          );
        },
        controller: 'SpecimenOverviewCtrl',
        parent: 'specimen-detail'
      })
      .state('specimen-detail.extensions', {
        url: '/extensions',
        template: '<div ui-view></div>',
        controller: function($scope, specimen) {
          $scope.extnOpts = {
            update: $scope.specimenResource.updateOpts,
            entity: specimen,
            isEntityActive: (specimen.activityStatus == 'Active')
          };
        },
        abstract: true,
        parent: 'specimen-detail'
      })
      .state('specimen-detail.extensions.list', {
        url: '/list',
        templateUrl: 'modules/biospecimen/extensions/list.html',
        controller: 'FormsListCtrl', 
        parent: 'specimen-detail.extensions'
      })
      .state('specimen-detail.extensions.addedit', {
        url: '/addedit?formId&recordId&formCtxId',
        templateUrl: 'modules/biospecimen/extensions/addedit.html',
        resolve: {
          formDef: function($stateParams, Form) {
            return Form.getDefinition($stateParams.formId);
          },
          postSaveFilters: function() {
            return [
              function(specimen, formName, formData) {
                if (formName == "SpecimenReceivedEvent") {
                  specimen.createdOn = formData.time
                } else if (formName == "SpecimenFrozenEvent" && formData.incrementFreezeThaw == 1) {
                  ++specimen.freezeThawCycles;
                } else if (formName == "SpecimenThawEvent" && formData.incrementFreezeThaw == 1) {
                  ++specimen.freezeThawCycles;
                }

                return formData
              }
            ];
          }
        },
        controller: 'FormRecordAddEditCtrl',
        parent: 'specimen-detail.extensions'
      })
      .state('specimen-detail.events', {
        url: '/events',
        templateUrl: 'modules/biospecimen/participant/specimen/events.html',
        controller: function($scope, specimen, ExtensionsUtil) {
          $scope.entityType = 'SpecimenEvent';
          $scope.extnState = 'specimen-detail.events';
          $scope.events = specimen.getEvents();
          $scope.eventForms = specimen.getForms({entityType: 'SpecimenEvent'});

          $scope.deleteEvent = function(event) {
            var record = {recordId: event.id, formId: event.formId, formCaption: event.name};
            ExtensionsUtil.deleteRecord(
              record, 
              function() {
                var idx = $scope.events.indexOf(event);
                $scope.events.splice(idx, 1);
              }
            );
          }
        },
        parent: 'specimen-detail'
      })
      .state('specimen-create-derivative', {
        url: '/derivative',
        templateProvider: function(PluginReg, $q) {
          var defaultTmpl = "modules/biospecimen/participant/specimen/add-derivative.html";
          return $q.when(PluginReg.getTmpls("specimen-create-derivative", "page-body", defaultTmpl)).then(
            function(tmpls) {
              return '<div ng-include src="\'' + tmpls[0] + '\'"></div>';
            }
          );
        },
        resolve: {
          extensionCtxt: function(cp, Specimen) {
            return Specimen.getExtensionCtxt({cpId: cp.id});
          }
        },
        controller: 'AddDerivativeCtrl',
        parent: 'specimen-root'
      })
      .state('specimen-create-aliquots', {
        url: '/aliquots',
        templateProvider: function(PluginReg, $q) {
          var defaultTmpl = "modules/biospecimen/participant/specimen/add-aliquots.html";
          return $q.when(PluginReg.getTmpls("specimen-create-aliquots", "page-body", defaultTmpl)).then(
            function(tmpls) {
              return '<div ng-include src="\'' + tmpls[0] + '\'"></div>';
            }
          );
        },
        resolve: {
          extensionCtxt: function(cp, Specimen) {
            return Specimen.getExtensionCtxt({cpId: cp.id});
          }
        },
        controller: 'AddAliquotsCtrl',
        parent: 'specimen-root'
      })
      .state('specimen-bulk-create-aliquots', {
        url: '/bulk-create-aliquots',
        templateUrl: 'modules/biospecimen/participant/specimen/bulk-create-aliquots.html',
        controller: 'BulkCreateAliquotsCtrl',
        resolve: {
          parentSpmns: function(SpecimensHolder) {
            var specimens = SpecimensHolder.getSpecimens();
            SpecimensHolder.setSpecimens([]);
            return specimens || [];
          },

          cp: function(parentSpmns, CollectionProtocol) {
            if (parentSpmns.length == 0) {
              return {};
            }

            var cpId = parentSpmns[0].cpId;
            if (parentSpmns.every(function(spmn) { return spmn.cpId == cpId })) {
              return CollectionProtocol.getById(cpId);
            } else {
              return {};
            }
          }
        },
        parent: 'signed-in'
      })
      .state('specimen-bulk-create-derivatives', {
        url: '/bulk-create-derivatives',
        templateUrl: 'modules/biospecimen/participant/specimen/bulk-create-derivatives.html',
        controller: 'BulkCreateDerivativesCtrl',
        resolve: {
          parentSpmns: function(SpecimensHolder) {
            var specimens = SpecimensHolder.getSpecimens();
            SpecimensHolder.setSpecimens([]);
            return specimens || [];
          }
        },
        parent: 'signed-in'
      })
      .state('bulk-add-event', {
        url: '/bulk-add-event',
        templateUrl: 'modules/biospecimen/participant/specimen/bulk-add-event.html',
        controller: 'BulkAddEventCtrl',
        parent: 'signed-in'
      })
      .state('specimen-search', {
        url: '/specimen-search',
        templateUrl: 'modules/biospecimen/participant/specimen/search-result.html',
        resolve: {
          specimens: function(SpecimenSearchSvc) {
            return SpecimenSearchSvc.getSpecimens();
          },

          searchKey: function(SpecimenSearchSvc) {
            return SpecimenSearchSvc.getSearchKey();
          }
        },
        controller: 'SpecimenResultsView',
        parent: 'signed-in'
      });
  })

  .run(function(QuickSearchSvc, SpecimenSearchSvc) {
    var opts = {
      template: 'modules/biospecimen/participant/specimen/quick-search.html',
      caption: 'entities.specimen',
      order: 3,
      search: SpecimenSearchSvc.search
    }

    QuickSearchSvc.register('specimen', opts);
  });
