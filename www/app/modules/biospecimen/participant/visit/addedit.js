
angular.module('os.biospecimen.visit.addedit', [])
  .controller('AddEditVisitCtrl', function(
    $scope, $state, $stateParams, cp, cpr, visit, latestVisit, extensionCtxt, hasDict,
    PvManager, ExtensionsUtil) {

    function loadPvs() {
      $scope.visitStatuses = PvManager.getPvs('visit-status');
      $scope.cohorts = PvManager.getPvs('cohort');
    };

    function init() {
      var currVisit = $scope.currVisit = angular.copy(visit);
      angular.extend(currVisit, {cprId: cpr.id, cpTitle: cpr.cpTitle});

      $scope.visitCtx = {
        obj: {visit: $scope.currVisit, cp: cp}, inObjs: ['visit']
      }

      if (!currVisit.id) {
        angular.extend(currVisit, {
          visitDate: currVisit.anticipatedVisitDate || new Date(),
          status: 'Complete',
          clinicalDiagnosis: latestVisit ? latestVisit.clinicalDiagnosis : currVisit.clinicalDiagnosis
        });
        delete currVisit.anticipatedVisitDate;
      }

      if ($stateParams.missedVisit == 'true') {
        angular.extend(currVisit, {status: 'Missed Collection'});
      } else if ($stateParams.newVisit == 'true') {
        angular.extend(currVisit, {id: undefined, name: undefined, status: 'Complete', visitDate: new Date()});
      }

      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(currVisit, extensionCtxt);

      if (!hasDict) {
        loadPvs();
      }
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
          $state.go('visit-detail.overview', {visitId: result.id, eventId: result.eventId});
        }
      );
    };

    init();
  });
