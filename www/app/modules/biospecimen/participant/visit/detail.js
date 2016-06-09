
angular.module('os.biospecimen.visit.detail', ['os.biospecimen.models'])
  .controller('VisitDetailCtrl', function(
    $scope, $state,
    cpr, visit, specimens, Specimen, VisitNamePrinter, DeleteUtil, ExtensionsUtil) {

    function init() {
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.specimens = specimens;
      ExtensionsUtil.createExtensionFieldMap(visit);
    }
          
    function onVisitDeletion() {
      $state.go('participant-detail.overview', {cprId: cpr.id, cpId: cpr.cpId});
    }

    $scope.deleteVisit = function() {
      DeleteUtil.delete($scope.visit, {onDeletion: onVisitDeletion});
    }

    $scope.reload = function() {
      var visitDetail = {
        visitId: visit.id,
        eventId: visit.eventId
      };

      return Specimen.listFor(cpr.id, visitDetail).then(
        function(specimens) {
          $scope.specimens = specimens;
        }
      );
    }

    $scope.printVisitName = function() {
      VisitNamePrinter.printNames({visitIds: [$scope.visit.id]});
    }

    init();
  });
