
angular.module('os.biospecimen.participant.overview', ['os.biospecimen.models'])
  .controller('ParticipantOverviewCtrl', function($scope, hasSde, sysDict, cpDict, visits, Visit) {

    function init() {
      $scope.occurredVisits    = Visit.completedVisits(visits);
      $scope.anticipatedVisits = Visit.anticipatedVisits(visits);
      $scope.missedVisits      = Visit.missedVisits(visits);

      $scope.ctx = {
        hasDict: hasSde && (cpDict.length > 0 || sysDict.length > 0),
        sysDict: sysDict,
        cpDict: cpDict,
        obj: {cpr: $scope.cpr},
        inObjs: ['cpr']
      }
    }

    $scope.isOtherProtocol = function(other) {
      return other.cpShortTitle != $scope.cpr.cpShortTitle;
    }

    init();
  });
