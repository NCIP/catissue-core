
angular.module('os.biospecimen.participant.visits', ['os.biospecimen.models'])
  .controller('ParticipantVisitsTreeCtrl', function(
    $scope, $state, $stateParams, $timeout,
    cpr, visits, specimens, 
    Specimen, PvManager) {

    if (!$stateParams.visitId && !$stateParams.eventId && visits && visits.length > 0) {
      $state.go(
        'participant-detail.visits', 
        {visitId: visits[0].id, eventId: visits[0].eventId}
      );
      return;
    }

    $scope.cpr = cpr;
    $scope.visits = visits;
    $scope.errorCode = '';
    $scope.visit = {};
    for (var i = 0; i < visits.length; ++i) {
      if ((!!$stateParams.visitId && $stateParams.visitId == visits[i].id) ||
          (!!$stateParams.eventId && $stateParams.eventId == visits[i].eventId)) {
        $scope.visit = visits[i];
        break;
      } 
    }

    $scope.showVisitDetail = function(event, visitId, eventId) {
      event.preventDefault();
      event.stopPropagation();
      $state.go('visit-detail.overview', {visitId: visitId, eventId: eventId});
    };

    $scope.specimens = Specimen.flatten(specimens);
    $scope.view = $stateParams.view;
    $scope.storageTypes = PvManager.getPvs('storage-type');

    $scope.openSpecimenNode = function(specimen) {
      specimen.isOpened = true;
    };

    $scope.closeSpecimenNode = function(specimen) {
      specimen.isOpened = false;
    };

    $scope.selection = {all: false, any: false};
    $scope.toggleAllSpecimenSelect = function() {
      angular.forEach($scope.specimens, function(specimen) {
        specimen.selected = $scope.selection.all;
      });

      $scope.selection.any = $scope.selection.all;
    };

    $scope.toggleSpecimenSelect = function(specimen) {
      selectChildrenSpecimens(specimen);
      selectParentSpecimen(specimen);  
      toggleAllSelected(specimen);

      $scope.selection.any = specimen.selected ? true : isAnySelected();
    };

    $scope.collectSpecimens = function() {
      $scope.view = 'collect_specimens';

      var specimensToCollect = [];
      angular.forEach($scope.specimens, function(specimen) {
        if (specimen.selected) {
          specimen.isOpened = true;
          specimensToCollect.push(specimen);
        } else if (isAnyChildSpecimenSelected(specimen)) {
          specimen.isOpened = true;
          specimensToCollect.push(specimen);
        }
      });

      $scope.specimensToCollect = specimensToCollect;
    };

    $scope.saveSpecimens = function() {
      if (areDuplicateLabelsPresent($scope.specimensToCollect)) {
        $scope.errorCode = 'One or more specimens are using same label';
        $timeout(function() { $scope.errorCode = '' }, 3000);
        return;
      }

      var specimensToSave = getSpecimensToSave($scope.specimensToCollect, []);
      Specimen.save(specimensToSave).then(
        function(result) {
          if (result && result.length > 0) {
            //
            // TODO: a better way could be to merge results with existing data
            //
            $state.go('participant-detail.visits', {}, {reload: true});
          }
        }
      );
    };

    function getSpecimensToSave(uiSpecimens, visited) {
      var result = [];
      angular.forEach(uiSpecimens, function(specimen) {
        if (visited.indexOf(specimen) > 0) {
          return;
        }

        visited.push(specimen);
        result.push({
          initialQty: specimen.initialQty,
          label: specimen.label,
          reqId: specimen.reqId,
          visitId: $scope.visit.id,
          children: getSpecimensToSave(specimen.children, visited)
        });  
      });

      return result;
    };

    function toggleAllSelected(specimen) {
      if (!specimen.selected) {
        $scope.selection.all = false;
        return;
      }

      for (var i = 0; i < $scope.specimens.length; ++i) {
        if (!$scope.specimens[i].selected) {
          $scope.selection.all = false;
          return;
        }
      }

      $scope.selection.all = true;
    };

    function selectChildrenSpecimens(specimen) {
      if (!specimen.selected) {
        return;
      }

      angular.forEach(specimen.children, function(child) {
        child.selected = true;
        selectChildrenSpecimens(child);
      });
    };

    function selectParentSpecimen(specimen) {
      if (!specimen.selected) {
        return false;
      }

      var parent = specimen.parent;
      while (parent) {
        parent.selected = true;
        parent = parent.parent;
      }
    };

    function isAnySelected() {
      for (var i = 0; i < $scope.specimens.length; ++i) {
        if ($scope.specimens[i].selected) {
          return true;
        }
      }

      return false;
    }

    function isAnyChildSpecimenSelected(specimen) {
      if (!specimen.children) {
        return false;
      }

      for (var i = 0; i < specimen.children.length; ++i) {
        if (specimen.children[i].selected) {
          return true;
        }

        if (isAnyChildSpecimenSelected(specimen.children[i])) {
          return true;
        }
      }

      return false;
    };

    function areDuplicateLabelsPresent(input) {
      var labels = [];
      for (var i = 0; i < input.length; ++i) {
        if (labels.indexOf(input[i].label) != -1) {
          return true;
        }

        labels.push(input[i].label);
      }

      return false;
    }
  });
