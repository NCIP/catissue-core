angular.module('os.biospecimen.specimen.detail', [])
  .controller('SpecimenDetailCtrl', function(
    $scope, $state, $modal, $stateParams, currentUser,
    cpr, visit, specimen, Specimen, SpecimenLabelPrinter, SpecimensHolder, DeleteUtil, Alerts) {

    function init() {
      $scope.cpr = cpr;
      $scope.visit = visit;
      $scope.specimen = specimen;
      $scope.treeSpecimens = getTreeSpecimens(specimen);
    }

    function getTreeSpecimens(specimen) {
      var result = [];
      angular.forEach(specimen.specimensPool,
        function(poolSpecimen) {
          poolSpecimen.pooledSpecimen = specimen;
          result.push(poolSpecimen);
        }
      );

      return result.concat($scope.specimen.children);
    }

    $scope.reload = function() {
      var promise = null;
      if ($stateParams.specimenId) {
        promise = Specimen.getById($stateParams.specimenId);
      } else if ($stateParams.srId) {
        promise = Specimen.getAnticipatedSpecimen($stateParams.srId);
      }

      return promise.then(
        function(specimen) {
          $scope.treeSpecimens = getTreeSpecimens(specimen);
        }
      );
    }

    $scope.reopen = function() {
      specimen.reopen();
    }

    $scope.deleteSpecimen = function() {
      var specimen = new Specimen({
        id: $scope.specimen.id,
        label: $scope.specimen.label
      });
      var params = {cpId: cpr.cpId, cprId: cpr.id, visitId: visit.id};
      var parentId = $scope.specimen.parentId;

      DeleteUtil.delete(
        specimen,
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
          specimens: function() {
            return [specimen];
          }
        }
      });
    }

    $scope.printSpecimenLabels = function() {
      SpecimenLabelPrinter.printLabels({specimenIds: [specimen.id]});
    };

    $scope.addSpecimensToSpecimenList = function(list) {
      var selectedSpecimens = [{id: $scope.specimen.id}];

      if (!!list) {
        list.addSpecimens(selectedSpecimens).then(function(specimens) {
          var listType = list.getListType(currentUser);
          Alerts.success('specimen_list.specimens_added_to_' + listType, list);
        })
      } else {
        SpecimensHolder.setSpecimens(selectedSpecimens);
        $state.go('specimen-list-addedit', {listId: ''});
      }
    }

    init();
  });
