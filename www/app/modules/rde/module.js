
angular.module('os.rde', [])
  .config(function($stateProvider) {
    $stateProvider
      .state('cp-detail.rde-config', {
        url: '/rde-config',
        templateUrl: 'modules/rde/cp-cfg.html',
        controller: 'RdeConfigCtrl',
        parent: 'cp-detail'
      })

      .state('rde-workflow', {
        url: '/rde-workflow',
        template: '<div ui-view></div>',
        controller: function($scope, ctx) {
          $scope.ctx = ctx;
        },
        resolve: {
          ctx: function() {
            return {};
          }
        },
        abstract: true,
        parent: 'signed-in'
      })

      .state('rde-select-workflow', {
        url: '/select-workflow',
        templateUrl: 'modules/rde/select-workflow.html',
        controller: function() { },
        parent: 'rde-workflow'
      })

      .state('rde-collect-visit-names', {
        url: '/collect-visit-names',
        templateUrl: 'modules/rde/collect-visit-barcodes.html',
        controller: 'RdeCollectVisitBarcodesCtrl',
        parent: 'rde-workflow'
      })

      .state('rde-validate-visit-names', {
        url: '/validate-visit-names',
        templateUrl: 'modules/rde/visit-barcode-details.html',
        controller: 'RdeVisitBarcodeDetailsCtrl',
        resolve: {
          visitDetails: function(ctx, RdeApis) {
            var barcodes = ctx.visits.map(function(v) { return v.barcode; });
            return RdeApis.getBarcodeDetails(barcodes);
          }
        },
        parent: 'rde-workflow'
      })

      .state('rde-collect-participant-details', {
        url: '/collect-participant-details',
        templateUrl: 'modules/rde/collect-participant-details.html',
        controller: 'RdeCollectParticipantDetailsCtrl',
        resolve: {
          cps: function(CollectionProtocol) {
            return CollectionProtocol.listForRegistrations();
          }
        },
        parent: 'rde-workflow'
      })

      .state('rde-collect-visit-details', {
        url: '/collect-visit-details',
        templateUrl: 'modules/rde/collect-visit-details.html',
        controller: 'RdeCollectVisitDetailsCtrl',
        resolve: {
          participants: function(ctx) {
            return angular.copy(ctx.participants);
          }
        },
        parent: 'rde-workflow'
      })

      .state('rde-process-aliquots', {
        controller: function($scope, $state, ctx) {
          ctx.workflow = 'process-aliquots',
          $state.go('rde-select-container')
        },
        parent: 'rde-workflow'
      })

      .state('rde-collect-primary-specimens', {
        url: '/collect-primary-specimens',
        templateUrl: 'modules/rde/collect-primary-specimens.html',
        controller: 'RdeCollectPrimarySpmnsCtrl',
        resolve: {
          visits: function(ctx) {
            return angular.copy(ctx.visitsSpmns);
          },

          cpRdeWfCfg: function($q, ctx, CollectionProtocol) {
            var cpIds = [];
            angular.forEach(ctx.visitsSpmns, function(visit) {
              if (cpIds.indexOf(visit.cpId) == -1) {
                cpIds.push(visit.cpId);
              }
            });

            var cfg = {};
            var promises = cpIds.map(
              function(cpId) {
                return CollectionProtocol.getWorkflows(cpId).then(
                  function(cpWfs) {
                    cfg[cpWfs.shortTitle] = cpWfs.workflows.bde || {};
                  }
                );
              }
            );

            return $q.all(promises).then(
              function() {
                return cfg
              }
            );
          }
        },
        parent: 'rde-workflow'
      })

      .state('rde-print-labels', {
        url: '/print-labels',
        templateUrl: 'modules/rde/print-specimen-labels.html',
        controller: 'RdePrintSpecimenLabelsCtrl',
        resolve: {
          visits: function(ctx) {
            return ctx.visitsSpmns;
          }
        },
        parent: 'rde-workflow'
      })

      .state('rde-select-container', {
        url: '/select-container',
        templateUrl: 'modules/rde/container-selector.html',
        controller: 'RdeContainerSelectorCtrl',
        resolve: {
          visits: function(ctx) {
            return ctx.visitsSpmns;
          }
        },
        parent: 'rde-workflow'
      })

      .state('rde-assign-positions', {
        url: '/assign-positions',
        templateUrl: 'modules/rde/assign-positions.html',
        controller: 'RdeAssignPositionsCtrl',
        resolve: {
          container: function(ctx, Container) {
            if (ctx.container) {
              return Container.getById(ctx.container.id);
            } else {
              return undefined;
            }
          },

          occupancyMap: function(ctx, Container) {
            if (ctx.container) {
              return ctx.container.getOccupiedPositions();
            } else {
              return undefined;
            }
          },

          visits: function(ctx) {
            return angular.copy(ctx.visitsSpmns || []);
          }
        },
        parent: 'rde-workflow'
      })

      .state('rde-collect-child-specimens', {
        url: '/collect-child-specimens',
        templateUrl: 'modules/rde/collect-child-specimens.html',
        controller: 'RdeCollectChildSpecimensCtrl',
        resolve: {
          visits: function(ctx) {
            return angular.copy(ctx.visitsSpmns || []);
          }
        },
        parent: 'rde-workflow'
      })
  });
