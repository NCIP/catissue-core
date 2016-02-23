angular.module('os.biospecimen.participant.search', ['os.biospecimen.models'])
  .factory('ParticipantSearchSvc', function($state, CollectionProtocolRegistration, Alerts) {
    var matchingParticipants =[];
    var searchKey = undefined;

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
            searchKey = searchData.ppid || searchData.uid;
            $state.go('participant-search', {}, {reload: true});
          } else {
            $state.go('participant-detail.overview', {cpId: participants[0].cpId, cprId: participants[0].cprId});
          }
        }
      );
    }

    function getParticipants() {
      return matchingParticipants;
    }

    function getSearchKey() {
      return searchKey;
    }

    return {
      getParticipants :getParticipants,

      getSearchKey: getSearchKey,

      search: search
    };
  })
  .controller('ParticipantResultsView', function($scope, $state, participants, searchKey) {
    function init() {
      $scope.cprs = participants;
      $scope.searchKey = searchKey;
    }

    $scope.participantDetail = function(cpr) {
      $state.go('participant-detail.overview', {cpId: cpr.cpId, cprId: cpr.cprId});
    }

    init();
  });

