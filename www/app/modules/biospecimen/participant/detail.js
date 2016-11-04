
angular.module('os.biospecimen.participant.detail', ['os.biospecimen.models'])
  .controller('ParticipantDetailCtrl', function(
    $scope, $q, cpr, visits, 
    CollectionProtocol, SpecimenLabelPrinter, RegisterToNewCpsHolder, DeleteUtil) {

    function init() {
      $scope.cpr = cpr;
      loadPvs();
    }

    function loadPvs() {
      var registeredCps = [];
      angular.forEach(cpr.participant.registeredCps, function(cp) {
        registeredCps.push(cp.cpShortTitle);
      });

      $scope.cpsForReg = [];
      CollectionProtocol.listForRegistrations(cpr.getMrnSites()).then(
        function(cps) {
          for (var i = 0; i < cps.length; ++i) {
            if (registeredCps.indexOf(cps[i].shortTitle) == -1) {
              $scope.cpsForReg.push(cps[i]);
            }
          }
        }
      );

      RegisterToNewCpsHolder.setCpList($scope.cpsForReg);
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
      DeleteUtil.delete($scope.cpr, {onDeleteState: 'participant-list', forceDelete: true});
    }

    init();
  });
