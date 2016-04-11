angular.module('os.rde')
  .controller('RdeContainerSelectorCtrl', function($scope, $state, session, visits) {
    function init() {
      $scope.input = {
        listOpts: {
          type: 'specimen',
          criteria: {
            storeSpecimensEnabled: true,
            cpShortTitle: getCpShortTitles(visits)
          }
        }
      }
    }

    function getCpShortTitles(visits) {
      return (visits || []).filter(
        function(visit) {
          return visit.specimens.length > 0;
        }
      ).map(
        function(visit) {
          return visit.cpShortTitle;
        }
      );
    }

    function saveSession(step) {
      angular.extend(session.data, {
        step: step || $state.$current.name,
        selectedContainer: !!$scope.ctx.container ? $scope.ctx.container.id : -1
      });

      return session.saveOrUpdate().then($scope.showSessionSaved(!step));
    }

    $scope.toggleContainerSel = function(container) {
      $scope.ctx.container = container;

      var nextStep = 'rde-assign-positions';
      saveSession(nextStep).then(
        function() {
          $state.go(nextStep, {sessionId: session.uid});
        }
      );
    }

    $scope.scanVirtualAliquots = function() {
      $scope.toggleContainerSel(undefined);
    }

    init();
  });
