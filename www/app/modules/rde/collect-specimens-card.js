angular.module('os.rde')
  .controller('CollectSpecimensCardCtrl', function($scope, AuthorizationService) {
    function init() {
      var isAllowed = AuthorizationService.isAllowed;
      var isParticipantRegAllowed  = isAllowed({resource: 'ParticipantPhi', operations: ['Create']});
      var isVisitSpmnUpdateAllowed = isAllowed({resource: 'VisitAndSpecimen', operations: ['Create', 'Update']});
      $scope.bdeAllowed = isParticipantRegAllowed && isVisitSpmnUpdateAllowed;
    }

    init();
  });
