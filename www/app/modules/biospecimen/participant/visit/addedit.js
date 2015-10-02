
angular.module('os.biospecimen.visit.addedit', [])
  .controller('AddEditVisitCtrl', function(
    $scope, $state, cpr, visit, extensionCtxt,
    PvManager, Util, ExtensionsUtil) {

    function loadPvs() {
      $scope.visitStatuses = PvManager.getPvs('visit-status');
      $scope.missedReasons = PvManager.getPvs('missed-visit-reason');
      $scope.sites = PvManager.getSites();
      $scope.clinicalStatuses = PvManager.getPvs('clinical-status');
      $scope.cohorts = PvManager.getPvs('cohort');

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

      $scope.deFormCtrl = {};
      $scope.extnOpts = Util.getExtnOpts(currVisit, extensionCtxt);
      ExtensionsUtil.createExtensionFieldMap(currVisit);
    }

    $scope.saveVisit = function() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      if (formCtrl) {
        $scope.currVisit.extensionDetail = formCtrl.getFormData();
      }

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
