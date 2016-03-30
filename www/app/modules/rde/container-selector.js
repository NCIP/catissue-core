angular.module('os.rde')
  .controller('RdeContainerSelectorCtrl', function($scope, $state, visits) {
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

    $scope.toggleContainerSel = function(container) {
      $scope.ctx.container = container;
      $state.go('rde-assign-positions');
    }

    $scope.scanVirtualAliquots = function() {
      $scope.toggleContainerSel(undefined);
    }

    init();
  });
