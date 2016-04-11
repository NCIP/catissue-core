angular.module('os.rde')
  .controller('RdeCollectVisitBarcodesCtrl', function($scope, $state, session) {
    function init() {
      $scope.ctx.workflow = 'process_visits_fast';
      $scope.input = {visits: session.getVisitBarcodes()};
    }

    function saveSession(step) {
      var sessionData = {
        workflow: $scope.ctx.workflow,
        step:     step || $state.$current.name,
        visits:   $scope.input.visits.map(function(v) { return {name: v.barcode, visitDate: v.visitDate}; })
      }

      angular.extend(session.data, sessionData);
      return session.saveOrUpdate().then($scope.showSessionSaved(!step))
    }

    $scope.addVisit = function() {
      $scope.input.visits.push({visitDate: new Date()});
    }

    $scope.removeVisit = function(visit) {
      var idx = $scope.input.visits.indexOf(visit);
      $scope.input.visits.splice(idx, 1);
    }

    $scope.validate = function() {
      var nextStep = 'rde-validate-visit-names';
      saveSession(nextStep).then(
        function() {
          $scope.ctx.visits = $scope.input.visits;
          $state.go(nextStep, {sessionId: session.uid});
        }
      );
    }

    $scope.saveSession = saveSession;

    init();
  });
