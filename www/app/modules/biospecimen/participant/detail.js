
angular.module('os.biospecimen.participant.detail', ['os.biospecimen.models'])
  .controller('ParticipantDetailCtrl', function(
    $scope, $q, cpr, visits, 
    CollectionProtocol, SpecimenLabelPrinter, DeleteUtil) {

    function init() {
      $scope.cpr = cpr;
      $scope.showRegToAnother = false;
      loadPvs();
    }

    function loadPvs() {
      var registeredCps = [];
      angular.forEach(cpr.participant.registeredCps, function(cp) {
        registeredCps.push(cp.cpShortTitle);
      });

      var siteNames = cpr.getMrnSites();
      CollectionProtocol.listForRegistrations(siteNames).then(
        function(cps) {
          $scope.showRegToAnother = cps.length > registeredCps.length;
        }
      );
    }

    $scope.editCpr = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.pmiText = function(pmi) {
      return pmi.siteName + (pmi.mrn ? " (" + pmi.mrn + ")" : "");
    }

    $scope.printSpecimenLabels = function(visitDetail) {
      SpecimenLabelPrinter.printLabels(visitDetail);
    }

    $scope.deleteReg = function() {
      DeleteUtil.delete($scope.cpr, {onDeleteState: 'participant-list'});
    }

    init();
  });
