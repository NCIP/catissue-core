
angular.module('openspecimen')
  .controller('ParticipantListCtrl', function($scope, $state, $stateParams, $modal, cp, CollectionProtocolRegistration, AlertService) {
    $scope.cpId = $stateParams.cpId;

    var loadParticipants = function() {
      CollectionProtocolRegistration.listForCp(cp.id, true).then(
        function(cprList) {
          $scope.cprList = cprList;
        }
      )
    };

    $scope.showParticipantOverview = function(cpr) {
      $state.go('participant-detail.overview', {cprId: cpr.cprId});
    };

    loadParticipants();
  });
