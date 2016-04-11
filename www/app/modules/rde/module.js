
angular.module('os.rde', [])
  .config(function($stateProvider) {
    $stateProvider
      .state('cp-detail.rde-config', {
        url: '/rde-config',
        templateUrl: 'modules/rde/cp-config.html',
        controller: 'RdeConfigCtrl',
        parent: 'cp-detail'
      })

      .state('rde-sessions', {
        url: '/rde-sessions',
        templateUrl: 'modules/rde/list-sessions.html',
        controller: 'RdeListSessionsCtrl',
        resolve: {
          sessions: function(RdeSession) {
            return RdeSession.query();
          }
        },
        parent: 'signed-in'
      })

      .state('rde', {
        url: '/rde',
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
        controller: function($scope, uid) {
          $scope.ctx = { sessionId: uid }
        },
        resolve: {
          uid: function(RdeSession) {
            return RdeSession.generateSessionId();
          }
        },
        parent: 'rde'
      })

      .state('rde-session', {
        url: '/:sessionId',
        template: '<div ui-view></div>',
        controller: function($scope, session, Alerts) {
          $scope.ctx.workflow = session.data.workflow;
          $scope.ctx.sessionId = session.uid;

          $scope.showSessionSaved = function(showMsg) {
            return function() {
              if (showMsg) {
                Alerts.success('rde.session_saved');
              }
            }
          }
        },
        resolve: {
          session: function($stateParams, RdeSession) {
            return RdeSession.getByUid($stateParams.sessionId).then(
              function(session) {
                if (session.uid != $stateParams.sessionId) {
                  session = new RdeSession({uid: $stateParams.sessionId, data: {}});
                }
                return session;
              }
            );
          }
        },
        abstract: true,
        parent: 'rde'
      })

      .state('rde-collect-visit-names', {
        url: '/collect-visit-names',
        templateUrl: 'modules/rde/collect-visit-barcodes.html',
        controller: 'RdeCollectVisitBarcodesCtrl',
        parent: 'rde-session'
      })

      .state('rde-validate-visit-names', {
        url: '/validate-visit-names',
        templateUrl: 'modules/rde/visit-barcode-details.html',
        controller: 'RdeVisitBarcodeDetailsCtrl',
        resolve: {
          visitDetails: function(ctx, session, RdeApis) {
            if (!ctx.visits || ctx.visits.length == 0) {
              ctx.visits = session.getVisitBarcodes();
            }

            var barcodes = ctx.visits.map(function(v) { return v.barcode });
            return RdeApis.getBarcodeDetails(barcodes);
          }
        },
        parent: 'rde-session'
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
        parent: 'rde-session'
      })

      .state('rde-collect-visit-details', {
        url: '/collect-visit-details',
        templateUrl: 'modules/rde/collect-visit-details.html',
        controller: 'RdeCollectVisitDetailsCtrl',
        resolve: {
          participants: function(ctx, session) {
            if (!ctx.participants || ctx.participants.length == 0) {
              ctx.participants = session.data.participants;
            }

            return angular.copy(ctx.participants);
          }
        },
        parent: 'rde-session'
      })

      .state('rde-process-aliquots', {
        url: '/process-aliquots',
        controller: function($scope, $state, ctx, session) {
          ctx.workflow = 'process_aliquots';
          angular.extend(session.data, {
            workflow: ctx.workflow,
            step: 'rde-select-container'
          });

          session.saveOrUpdate().then(
            function() {
              $state.go('rde-select-container', {sessionId: session.uid}, {location: 'replace'})
            }
          );
        },
        parent: 'rde-session'
      })

      .state('rde-collect-primary-specimens', {
        url: '/collect-primary-specimens',
        templateUrl: 'modules/rde/collect-primary-specimens.html',
        controller: 'RdeCollectPrimarySpmnsCtrl',
        resolve: {
          visits: function(ctx, session) {
            return session.loadVisitsSpmns(ctx);
          },

          cpRdeWfCfg: function($q, visits, CollectionProtocol) {
            var cpIds = [];
            angular.forEach(visits, function(visit) {
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
        parent: 'rde-session'
      })

      .state('rde-print-labels', {
        url: '/print-labels',
        templateUrl: 'modules/rde/print-specimen-labels.html',
        controller: 'RdePrintSpecimenLabelsCtrl',
        resolve: {
          visits: function(ctx, session) {
            return session.loadVisitsSpmns(ctx);
          }
        },
        parent: 'rde-session'
      })

      .state('rde-select-container', {
        url: '/select-container',
        templateUrl: 'modules/rde/container-selector.html',
        controller: 'RdeContainerSelectorCtrl',
        resolve: {
          visits: function(ctx, session) {
            return session.loadVisitsSpmns(ctx);
          }
        },
        parent: 'rde-session'
      })

      .state('rde-assign-positions', {
        url: '/assign-positions',
        templateUrl: 'modules/rde/assign-positions.html',
        controller: 'RdeAssignPositionsCtrl',
        resolve: {
          container: function(ctx, session, Container) {
            if (ctx.container) {
              return Container.getById(ctx.container.id);
            } else if (session.data.selectedContainer == -1) {
              return undefined;
            } else if (angular.isDefined(session.data.selectedContainer)) {
              return Container.getById(session.data.selectedContainer);
            } else {
              return undefined;
            }
          },

          occupancyMap: function(ctx, container, Container) {
            ctx.container = container;
            if (container) {
              return container.getOccupiedPositions();
            } else {
              return undefined;
            }
          },

          visits: function(ctx, session) {
            return session.loadVisitsSpmns(ctx);
          }
        },
        parent: 'rde-session'
      })

      .state('rde-collect-child-specimens', {
        url: '/collect-child-specimens',
        templateUrl: 'modules/rde/collect-child-specimens.html',
        controller: 'RdeCollectChildSpecimensCtrl',
        resolve: {
          visits: function(ctx, session) {
            return session.loadVisitsSpmns(ctx);
          }
        },
        parent: 'rde-session'
      })
  });
