angular.module('os.biospecimen.participant.search', ['os.biospecimen.models'])
  .factory('ParticipantSearchSvc', function($state, CollectionProtocolRegistration, Alerts) {
    var matchingParticipants =[];

    function search(searchData) {
      var filterOpts = {
        ppid: searchData.ppid,
        uid:searchData.uid,
        exactMatch: true
      };

      CollectionProtocolRegistration.listForCp(-1, false, filterOpts).then(
        function(participants) {
          if (participants == undefined || participants.length == 0) {
            Alerts.error('search.error', {entity: 'Participant', key: searchData.ppid || searchData.uid});
            return;
          } else if (participants.length > 1) {
            matchingParticipants = participants;
            $state.go('participant-search', {}, {reload: true});
          } else {
            $state.go('participant-detail.overview', {cprId: participants[0].cprId});
          }
        }
      );
    }

    function getParticipants() {
      return matchingParticipants;
    }

    return {
      getParticipants : getParticipants,

      search: search
    };
  })
  .controller('ParticipantResultsView', function($scope, $state, participants) {
    function init() {
      $scope.cprs = participants;
    }

    init();
  });

