angular.module('openspecimen')
  .factory('ParticipantSearchSvc', function($state, CollectionProtocolRegistration) {
    var participants =[];

    function search(searchData) {
      var filterOpts = {
        ppid: searchData.ppid,
        uid:searchData.uid,
        exactMatch: true
      };

      CollectionProtocolRegistration.listForCp(-1, false, filterOpts).then(
        function(participant) {
          if (participant == undefined || participant.length == 0) {
            Alerts.error('search.error', {component: 'Participant', id: searchData.ppid || searchData.uid});
            return;
          } else if (participant.length > 1) {
            participants = participant;
            $state.go('participant-search');
          } else {
            $state.go('participant-detail.overview', {cprId: participant[0].cprId});
          }
        }
      );
    }

    function getParticipants() {
      return participants;
    }

    return {
      getParticipants : getParticipants,

      search: search
    };
  })
