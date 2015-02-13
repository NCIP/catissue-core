
angular.module('os.biospecimen.participant.list', ['os.biospecimen.models'])
  .controller('ParticipantListCtrl', function($scope, $state, $stateParams, $modal, CollectionProtocol, CollectionProtocolRegistration) {
    $scope.cpId = $stateParams.cpId;
    
    var loadParticipants = function() {
      CollectionProtocol.getById($scope.cpId).then(
        function(cp) {
          $scope.cp = cp;
        }
      );
     
      CollectionProtocolRegistration.listForCp($scope.cpId, true).then(
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
