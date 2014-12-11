
angular.module('openspecimen')
  .controller('ParticipantVisitsTreeCtrl', function($scope, $state, $stateParams, cpr, visits, specimens, Specimen) {
    if (!$stateParams.visitId && !$stateParams.eventId && visits && visits.length > 0) {
      $state.go(
        'participant-detail.visits', 
        {visitId: visits[0].id, eventId: visits[0].eventId}
      );
      return;
    }

    $scope.cpr = cpr;
    $scope.visits = visits;
    $scope.specimens = Specimen.flatten(specimens);

    $scope.openSpecimenNode = function(specimen) {
      specimen.isOpened = true;
    };

    $scope.closeSpecimenNode = function(specimen) {
      specimen.isOpened = false;
    };

  })
  .filter('openedSpecimenNodes', function() {
    var showSpecimen = function(specimen) {
      if (specimen.depth == 0) {
        return true;
      }

      var show = true;
      while (!!specimen.parent) {
        if (!specimen.parent.isOpened) {
          show = false;
          break;
        }

        specimen = specimen.parent;
      }

      return show;
    };

    return function(input) {
      if (!input) {
        return [];
      }

      var result = [];
      angular.forEach(input, function(specimen) {
        if (showSpecimen(specimen)) {
          result.push(specimen);
        }
      });

      return result;
    };
  });
