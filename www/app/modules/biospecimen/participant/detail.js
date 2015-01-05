
angular.module('os.biospecimen.participant.detail', ['os.biospecimen.models'])
  .controller('ParticipantDetailCtrl', function($scope, $q, cpr, visits, Visit, PvManager) {

    function loadPvs() {
      $scope.genders = PvManager.getPvs('gender');
      $scope.ethnicities = PvManager.getPvs('ethnicity');
      $scope.vitalStatuses = PvManager.getPvs('vital-status');
      $scope.races = PvManager.getPvs('race');
    }

    function init() {
      $scope.cpr = cpr;
      $scope.occurredVisits = Visit.completedVisits(visits);
      $scope.anticipatedVisits = Visit.anticipatedVisits(visits);

      loadPvs();
    }

    $scope.editCpr = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.getMrnDisplayText = function(pmi) {
      return pmi.mrn + " (" + pmi.siteName + ")";
    }

    init();
  });
