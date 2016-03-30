angular.module('os.rde')
  .controller('RdeCollectVisitBarcodesCtrl', function($scope, $state) {
    function init() {
      $scope.ctx.workflow = 'process-visits-fast';
      $scope.input = {
        visits: [{visitDate: new Date()}]
      };
    }

    $scope.addVisit = function() {
      $scope.input.visits.push({visitDate: new Date()});
    }

    $scope.removeVisit = function(visit) {
      var idx = $scope.input.visits.indexOf(visit);
      $scope.input.visits.splice(idx, 1);
    }

    $scope.validate = function() {
      $scope.ctx.visits = $scope.input.visits;
      $state.go('rde-validate-visit-names');
    }

    init();
  });
