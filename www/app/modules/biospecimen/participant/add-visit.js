angular.module('os.biospecimen.participant.addvisit', ['os.biospecimen.participant.detail'])
  .controller('AddVisitCtrl', function($scope, $state, Visit, PvManager, CollectionProtocolEvent) {
    function loadPvs() {
      $scope.visitStatuses = PvManager.getPvs('visit-status');
      $scope.sites = PvManager.getSites();
      $scope.missedReasons = PvManager.getPvs('missed-visit-reason');
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

      var visitStatus = $scope.visitToAdd.status;
      if (!visitStatus) {
        visitStatus = 'Complete';
      }

      if (!!$scope.visitToAdd.id) {
        Visit.getById($scope.visitToAdd.id).then(
          function(result) {
            angular.extend(visit, result);
            angular.extend(visit, {id: undefined, name: undefined, status: visitStatus});
          }
        );
      } else {
        CollectionProtocolEvent.getById($scope.visit.eventId).then(
          function(cpe) {
            visit.clinicalDiagnosis = cpe.clinicalDiagnosis;
            visit.clinicalStatus = cpe.clinicalStatus;
            visit.site = cpe.defaultSite;
            visit.visitDate = $scope.visitToAdd.anticipatedVisitDate;
            visit.status = visitStatus;
          }
        );
      }

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
          $state.go('participant-detail.visits', params, {reload: true});
        }
      );
    };

    init();
  });

