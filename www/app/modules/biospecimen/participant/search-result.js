
angular.module('os.biospecimen.participant.searchresult', ['os.biospecimen.models'])
  .controller('ParticipantSearch', function($scope, $state, participants) {

    function init() {
      $scope.cprs = participants;
    }

    $scope.participantDetail = function(cprId) {
      $state.go('participant-detail.overview', {cprId: cprId});
    }

    init();
  });
