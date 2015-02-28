
angular.module('os.biospecimen.participant.addvisit', ['os.biospecimen.participant.detail'])
  .controller('AddVisitCtrl', function($scope, $state, Visit, PvManager, CollectionProtocolEvent) {
    function loadPvs() {
      $scope.visitStatuses = PvManager.getPvs('visit-status');
      $scope.sites = PvManager.getSites();
      $scope.clinicalStatuses = PvManager.getPvs('clinical-status');

      $scope.searchClinicalDiagnoses = function(searchTerm) {
        $scope.clinicalDiagnoses = PvManager.getPvs('clinical-diagnosis', searchTerm);
      };
    };

    function getVisit() {
      var visit = new Visit({
        /*id: $scope.visitToAdd.id,*/
        cprId: $scope.cpr.id,
        cpTitle: $scope.cpr.cpTitle,
        eventId: $scope.visit.eventId
      });

      CollectionProtocolEvent.getById($scope.visit.eventId).then(
        function(cpe) {
          $scope.visit.clinicalDiagnosis = cpe.clinicalDiagnosis;
          $scope.visit.clinicalStatus = cpe.clinicalStatus;
          $scope.visit.site = cpe.defaultSite;
          $scope.visit.visitDate = new Date($scope.visitToAdd.anticipatedVisitDate).toISOString();
          $scope.visit.status = 'Complete';
        }
      );

      return visit;
    }

    function init() {
      loadPvs();
      $scope.visit = getVisit();
    }

    $scope.addVisit = function() {
      $scope.visit.$saveOrUpdate().then(
        function(result) {
          angular.extend($scope.visitToAdd, result);
          $scope.revertAddVisit();

          var params = {visitId: result.id, eventId: result.eventId};
          $state.go('participant-detail.visits', params);
        }
      );
    };

    init();
  });

