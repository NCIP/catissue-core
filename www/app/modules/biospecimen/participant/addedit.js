
angular.module('os.biospecimen.participant.addedit', ['os.biospecimen.models'])
  .controller('ParticipantAddEditCtrl', function(
    $scope, $state, $stateParams, 
    AlertService, CollectionProtocolRegistration, Participant,
    SiteService, PvManager) {

    function loadPvs() {
      SiteService.getSites().then(
        function(result) {
          if (result.status != "ok") {
            alert("Failed to load sites information");
          }
          $scope.sites = result.data.map(function(site) {
            return site.name;
          });
        }
      );

      PvManager.loadPvs($scope, 'gender');
      PvManager.loadPvs($scope, 'ethnicity');
      PvManager.loadPvs($scope, 'vitalStatus');
      PvManager.loadPvs($scope, 'race');
    };

    function init() {
      $scope.cpId = $stateParams.cpId;
      $scope.pid = undefined;

      $scope.cpr = new CollectionProtocolRegistration({registrationDate: new Date()});
      $scope.cpr.participant.addPmi($scope.cpr.participant.newPmi());
      
      loadPvs();
    };

    function registerParticipant() {
      var cpr = angular.copy($scope.cpr);
      cpr.cpId = $scope.cpId;
      cpr.$saveOrUpdate().then(
        function(savedCpr) {
          $state.go('participant-detail.overview', {cprId: savedCpr.id});
        }
      );
    };

    $scope.addPmiIfLast = function(idx) {
      var participant = $scope.cpr.participant;
      if (idx == participant.pmis.length - 1) {
        participant.addPmi(participant.newPmi());
      }
    };

    $scope.removePmi = function(pmi) {
      var participant = $scope.cpr.participant;
      participant.removePmi(pmi);
      if (participant.pmis.length == 0) {
        participant.addPmi(participant.newPmi());
      }
    };

    $scope.register = function() {
      var participant = $scope.cpr.participant;
      if (participant.$id() || !participant.isMatchingInfoPresent()) {
        registerParticipant();
      } else {
        participant.getMatchingParticipants().then(
          function(result) {
            if (result.matchedAttr == 'none') {
              registerParticipant();
            }

            $scope.matchedResults = result;
          }
        );
      }
    };

    $scope.selectParticipant = function(participant) {
      $scope.selectedParticipant = participant;
    };

    $scope.lookupAgain = function() {
      $scope.matchedResults = undefined;
      $scope.selectedParticipant = undefined;
    };

    $scope.ignoreMatchesAndRegister = function() {
      registerParticipant();
    };

    $scope.registerUsingSelectedParticipant = function() {
      $scope.cpr.participant = $scope.selectedParticipant;
      registerParticipant();
    };

    init();
  });
