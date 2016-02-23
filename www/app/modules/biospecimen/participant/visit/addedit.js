
angular.module('os.biospecimen.visit.addedit', [])
  .controller('AddEditVisitCtrl', function(
    $scope, $state, $stateParams, cpr, visit, extensionCtxt,
    PvManager, ExtensionsUtil) {

    function loadPvs() {
      $scope.visitStatuses = PvManager.getPvs('visit-status');
      $scope.missedReasons = PvManager.getPvs('missed-visit-reason');
      $scope.sites = PvManager.getSites({listAll: true});
      $scope.clinicalStatuses = PvManager.getPvs('clinical-status');
      $scope.cohorts = PvManager.getPvs('cohort');
    };

    function init() {
      loadPvs();

      var currVisit = $scope.currVisit = angular.copy(visit);
      angular.extend(currVisit, {cprId: cpr.id, cpTitle: cpr.cpTitle});

      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(currVisit, extensionCtxt);
      
      if (!currVisit.id) {
        angular.extend(currVisit, {visitDate: currVisit.anticipatedVisitDate || new Date(), status: 'Complete'});
        delete currVisit.anticipatedVisitDate;
      }

      if ($stateParams.missedVisit == 'true') {
        angular.extend(currVisit, {status: 'Missed Collection'});
      } else if ($stateParams.newVisit == 'true') {
        angular.extend(currVisit, {id: undefined, name: undefined, status: 'Complete'});
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

          var params = {visitId: result.id, eventId: result.eventId};
          //
          // For now, redirectTo works to participant child states
          //
          if (!!$stateParams.redirectTo) {
            $state.go($stateParams.redirectTo, params);
          } else {
            $state.go('visit-detail.overview', params);
          }
        }
      );
    };

    init();
  });
