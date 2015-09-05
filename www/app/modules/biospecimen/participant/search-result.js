
angular.module('os.biospecimen.participant.searchresult', ['os.biospecimen.models'])
  .controller('ParticipantSearch', function($rootScope, $scope, $state) {

    function init() {
      $scope.cprs = $rootScope.quickSearch.cprs;
    }

    $scope.participantDetail = function(cprId) {
      $state.go('participant-detail.overview', {cprId: cprId});
    }

    init();
  });
