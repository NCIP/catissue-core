
angular.module('os.biospecimen.participant.addvisit', ['os.biospecimen.participant.detail'])
  .controller('AddVisitCtrl', function($scope, $state, Visit, PvManager) {
    function loadPvs() {
      $scope.visitStatuses = PvManager.getPvs('visit-status');
      $scope.sites = PvManager.getSites();
      $scope.clinicalStatuses = PvManager.getPvs('clinical-status');
      $scope.clinicalDiagnoses = PvManager.getClinicalDiagnoses({cpId: $scope.cpr.cpId});
    };

    function init() {
      loadPvs();

      $scope.visit = new Visit({
        id: $scope.visitToAdd.id,
        cprId: $scope.cpr.id,
        cpTitle: $scope.cpr.cpTitle,
        eventId: $scope.visit.eventId
      });
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

