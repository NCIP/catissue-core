angular.module('os.rde')
  .controller('RdeVisitBarcodeDetailsCtrl', function($scope, $state, ctx, visitDetails, session, RdeApis) {
    function init() {
      $scope.input = getInputCtx(ctx.visits, visitDetails);
    }

    function getInputCtx(visits, visitDetails) {
      var input = {
        visits: [],
        columnWidth: 85 / visitDetails.headers.length,
        headers: visitDetails.headers,
        error: false
      };

      angular.forEach(visits, function(visit) {
        input.visits.push({
          barcode  : visit.barcode,
          visitDate: visit.visitDate,
          detail   : visitDetails.visits[visit.barcode].detail
        });

        if (visitDetails.visits[visit.barcode].error) {
          input.error = true;
        }
      }); 

      return input;
    };

    function saveSession(step) {
      var sessionData = {
        step:   step || $state.$current.name,
        visits: $scope.input.visits.map(function(v) { return {name: v.barcode, visitDate: v.visitDate}; })
      }

      angular.extend(session.data, sessionData);
      return session.saveOrUpdate().then($scope.showSessionSaved(!step))
    }

    $scope.validate = function(form) {
      var barcodes = $scope.input.visits.map(function(v) { return v.barcode; });
      RdeApis.getBarcodeDetails(barcodes).then(
        function(visitDetails) {
          $scope.input = getInputCtx($scope.input.visits, visitDetails);
          form.$setPristine();
        }
      );
    }

    $scope.addOrUpdateVisits = function(form) {
      RdeApis.saveVisitBarcodes($scope.input.visits).then(
        function(visitsSpmns) {
          var nextState = 'rde-collect-primary-specimens';
          saveSession(nextState).then(
            function() {
              ctx.visitsSpmns = visitsSpmns;
              $state.go(nextState, {sessionId: session.uid});
            }
          );
        }
      );
    }

    $scope.saveSession = saveSession;

    init();
  });
