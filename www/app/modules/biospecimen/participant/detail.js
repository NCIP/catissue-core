
angular.module('os.biospecimen.participant.detail', ['os.biospecimen.models'])
  .controller('ParticipantDetailCtrl', function($scope, $q, cpr, visits, SpecimenLabelPrinter, PvManager) {

    function loadPvs() {
      $scope.genders = PvManager.getPvs('gender');
      $scope.ethnicities = PvManager.getPvs('ethnicity');
      $scope.vitalStatuses = PvManager.getPvs('vital-status');
      $scope.races = PvManager.getPvs('race');
    }

    function init() {
      $scope.cpr = cpr;
      loadPvs();
    }

    $scope.editCpr = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.pmiText = function(pmi) {
      return pmi.mrn + " (" + pmi.siteName + ")";
    }

    $scope.printSpecimenLabels = function(visitDetail) {
      SpecimenLabelPrinter.printLabels(visitDetail);
    }

    /**
     * Add visit logic
     */
    $scope.addVisitIdx = -1;
    $scope.rptVisitIdx = -1;

    $scope.showAddVisit = function(visit, index) {
      $scope.addVisitIdx = index;
      $scope.rptVisitIdx = -1;
      $scope.visitToAdd = visit;
    };

    $scope.revertAddVisit = function() {
      $scope.addVisitIdx = $scope.rptVisitIdx = -1;
      $scope.visitToAdd = {};
    };

    $scope.showRepeatVisit = function(visit, index) {
      $scope.addVisitIdx = -1;
      $scope.rptVisitIdx = index;
      $scope.visitToAdd = angular.copy(visit);
    };

    init();
  });
