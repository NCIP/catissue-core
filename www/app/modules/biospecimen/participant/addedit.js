
angular.module('os.biospecimen.participant.addedit', ['os.biospecimen.models', 'os.administrative.models'])
  .controller('ParticipantAddEditCtrl', function(
    $scope, $state, $stateParams, cpr,
    CollectionProtocolRegistration, Participant,
    Site, PvManager) {

    function loadPvs() {
      var op = !!$scope.cpr.id ? 'Update' : 'Create';
      var opts = {resource:'ParticipantPhi', operation: op};
      $scope.sites = PvManager.getSites(opts);

      $scope.genders = PvManager.getPvs('gender');
      $scope.ethnicities = PvManager.getPvs('ethnicity');
      $scope.vitalStatuses = PvManager.getPvs('vital-status');
      $scope.races = PvManager.getPvs('race');
    };

    function init() {
      $scope.cpId = $stateParams.cpId;
      $scope.pid = undefined;

      $scope.cpr = cpr;
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

    $scope.pmiText = function(pmi) {
      return pmi.mrn + " (" + pmi.siteName + ")";
    }

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
      $scope.cpr.participant = new Participant({id: $scope.selectedParticipant.$id()});
      registerParticipant();
    };

    init();
  });
