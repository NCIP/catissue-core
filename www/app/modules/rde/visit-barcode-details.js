angular.module('os.rde')
  .controller('RdeVisitBarcodeDetailsCtrl', function($scope, $state, ctx, visitDetails, RdeApis) {
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
          barcode: visit.barcode,
          visitDate: visit.visitDate,
          detail: visitDetails.visits[visit.barcode].detail
        });

        if (visitDetails.visits[visit.barcode].error) {
          input.error = true;
        }
      }); 

      return input;
    };

    function getBarcodes(visits) {
      return visits.map(function(v) { return v.barcode; });
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
      var visits = $scope.input.visits.map(
        function(visit) {
          return {barcode: visit.barcode, visitDate: visit.visitDate}
        }
      );

      RdeApis.saveVisitBarcodes(visits).then(
        function(visitsSpmns) {
          ctx.visitsSpmns = visitsSpmns;
          $state.go('rde-collect-primary-specimens');
        }
      );
    }

    init();
  });
