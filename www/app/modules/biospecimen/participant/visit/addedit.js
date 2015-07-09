
angular.module('os.biospecimen.visit.addedit', [])
  .controller('AddEditVisitCtrl', function($scope, $state, cpr, visit, PvManager) {
    function loadPvs() {
      $scope.visitStatuses = PvManager.getPvs('visit-status');
      $scope.missedReasons = PvManager.getPvs('missed-visit-reason');
      $scope.sites = PvManager.getSites();
      $scope.clinicalStatuses = PvManager.getPvs('clinical-status');

      $scope.searchClinicalDiagnoses = function(searchTerm) {
        $scope.clinicalDiagnoses = PvManager.getPvs('clinical-diagnosis', searchTerm);
      };
    };

    function init() {
      loadPvs();

      var currVisit = $scope.currVisit = angular.copy(visit);
      angular.extend(currVisit, {cprId: cpr.id, cpTitle: cpr.cpTitle});
      
      if (!currVisit.id && currVisit.anticipatedVisitDate) {
        angular.extend(currVisit, {visitDate: currVisit.anticipatedVisitDate, status: 'Complete'});
        delete currVisit.anticipatedVisitDate;
      }
    }

    $scope.saveVisit = function() {
      $scope.currVisit.$saveOrUpdate().then(
        function(result) {
          angular.extend($scope.visit, result);

          var params = {visitId: result.id, eventId: result.eventId};
          $state.go('visit-detail.overview', params);
        }
      );
    };

    init();
  });
