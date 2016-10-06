
angular.module('os.biospecimen.participant.overview', ['os.biospecimen.models'])
  .controller('ParticipantOverviewCtrl', function($scope, hasFieldsFn, visits, Visit, ExtensionsUtil) {
    function init() {
      $scope.occurredVisits    = Visit.completedVisits(visits);
      $scope.anticipatedVisits = Visit.anticipatedVisits(visits);
      $scope.missedVisits      = Visit.missedVisits(visits);

      ExtensionsUtil.createExtensionFieldMap($scope.cpr.participant);
      $scope.partCtx = {
        obj: {cpr: $scope.cpr},
        inObjs: ['cpr'],
        showEdit: hasFieldsFn(['cpr'], [])
      }
    }

    $scope.isOtherProtocol = function(other) {
      return other.cpShortTitle != $scope.cpr.cpShortTitle;
    }

    init();
  });
