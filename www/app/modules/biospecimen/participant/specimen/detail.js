angular.module('os.biospecimen.specimen.detail', [])
  .controller('SpecimenDetailCtrl', function(
    $scope, $state, $modal, $stateParams,
    cpr, visit, specimen, Specimen, DeleteUtil) {

    function init() {
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.specimen = specimen;
      $scope.childSpecimens = $scope.specimen.children; 
    }

    $scope.reload = function() {
      return reloadSpecimen();
    }


    function reloadSpecimen() {
      if ($stateParams.specimenId) {
        return Specimen.getById($stateParams.specimenId);
      } else if ($stateParams.srId) {
        return Specimen.getAnticipatedSpecimen($stateParams.srId);
      }
    }

    $scope.reopen = function() {
      specimen.reopen();
    }

    $scope.deleteSpecimen = function() {
      var params = {cpId: cpr.cpId, cprId: cpr.id, visitId: visit.id};
      var parentId = $scope.specimen.parentId;

      DeleteUtil.delete(
        $scope.specimen, 
        {
          onDeletion: function() {
            if (!parentId) {
              $state.go('visit-detail.overview', params);
            } else {
              $state.go('specimen-detail.overview', angular.extend({specimenId: parentId}, params));
            }
          }     
        }
      );
    }

    $scope.closeSpecimen = function() {
      var modalInstance = $modal.open({
        templateUrl: 'modules/biospecimen/participant/specimen/close.html',
        controller: 'SpecimenCloseCtrl',
        resolve: {
          props: function() {
            return {specimen: specimen};
          }
        }
      });
    }

    init();
  });
