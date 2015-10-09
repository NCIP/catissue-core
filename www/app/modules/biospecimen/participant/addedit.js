
angular.module('os.biospecimen.participant.addedit', ['os.biospecimen.models', 'os.administrative.models'])
  .controller('ParticipantAddEditCtrl', function(
    $scope, $state, $stateParams, $translate, cp, cpr, extensionCtxt,
    CollectionProtocolRegistration, Participant,
    Site, PvManager, Util) {

    var availableSites = [];

    function init() {
      $scope.cpId = $stateParams.cpId;
      $scope.pid = undefined;
      $scope.allowIgnoreMatches = true;
      $scope.deFormCtrl = {};

      $scope.cp = cp;
      $scope.cpr = angular.copy(cpr);
      $scope.cpr.participant.addPmi($scope.cpr.participant.newPmi());
      $scope.extnOpts = Util.getExtnOpts($scope.cpr.participant, extensionCtxt); 

      loadPvs();
    };

    function loadPvs() {
      var op = !!$scope.cpr.id ? 'Update' : 'Create';

      $scope.sites = [];
      Site.listForParticipants(op, true).then(function(sites) {
        availableSites = sites;
        filterAvailableSites();
      });

      $scope.genders = PvManager.getPvs('gender');
      $scope.ethnicities = PvManager.getPvs('ethnicity');
      $scope.vitalStatuses = PvManager.getPvs('vital-status');
      $scope.races = PvManager.getPvs('race');
    };

    function filterAvailableSites() {
      var siteNames = $scope.cpr.getMrnSites();
      $scope.sites = availableSites.filter(
        function(site) {
          return siteNames.indexOf(site) == -1;
        }
      );
    }

    function registerParticipant() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      var cprToSave = angular.copy($scope.cpr);
      cprToSave.cpId = $scope.cpId;

      if (formCtrl) {
        cprToSave.participant.extensionDetail = formCtrl.getFormData();
      }

      cprToSave.$saveOrUpdate().then(
        function(savedCpr) {
          angular.extend(cpr, savedCpr);
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
      filterAvailableSites();    

      if (participant.pmis.length == 0) {
        participant.addPmi(participant.newPmi());
      }
    };

    $scope.onSiteSelect = filterAvailableSites;

    $scope.register = function() {
      var participant = $scope.cpr.participant;
      if (participant.$id() || !participant.isMatchingInfoPresent()) {
        registerParticipant();
      } else {
        participant.getMatchingParticipants().then(
          function(result) {
            if (!result || result.length == 0) {
              registerParticipant();
            }

            $scope.allowIgnoreMatches = true;
            for (var i = 0; i < result.length; ++i) {
              var matchedAttrs = result[i].matchedAttrs;
              if (matchedAttrs.length > 1 || (matchedAttrs[0] != 'lnameAndDob')) {
                $scope.allowIgnoreMatches = false;
                break;
              }
            } 
            $scope.matchedParticipants = result;
          }
        );
      }
    };

    $scope.matchedAttrText = function(attr) {
      return $translate.instant("participant.matching_attr." + attr);
    }

    $scope.selectParticipant = function(participant) {
      $scope.selectedParticipant = participant;
    };

    $scope.lookupAgain = function() {
      $scope.matchedParticipants = undefined;
      $scope.selectedParticipant = undefined;
      $scope.allowIgnoreMatches = true;
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
