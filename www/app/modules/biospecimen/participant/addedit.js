
angular.module('os.biospecimen.participant.addedit', ['os.biospecimen.models'])
  .controller('RegParticipantCtrl', function(
    $state, $stateParams, $modal, AlertService) {
    
    var modalInstance = $modal.open({
      templateUrl: 'modules/biospecimen/participant/addedit.html',
      controller: 'ParticipantAddEditCtrl',
      resolve: {
        cpId: $stateParams.cpId
      },
      windowClass: 'os-modal-800'
    });

    modalInstance.result.then(
      function(result) {
        $state.go('participant-detail.overview', {cprId: result.id});
      },

      function() {
        $state.go('participant-list');
      }
    );
  })

  .controller('ParticipantAddEditCtrl', function(
    $scope, $modalInstance, $stateParams, 
    AlertService, CollectionProtocolRegistration, Participant,
    SiteService, PvManager) {

    $scope.cpId = $stateParams.cpId;
    $scope.pid = undefined;

    $scope.cpr = new CollectionProtocolRegistration({registrationDate: Date.now()});


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


    var handleMatchedResults = function() {
      if (!$scope.showMatchingParticipants) {
        return false;
      }

      if (!$scope.selectedParticipant) {
        AlertService.display($scope, "Select Participant before moving forward", "danger");
        return false;
      }

      $scope.matchedResults = undefined;
      $scope.showMatchingParticipants = false;
      return true;
    };

    $scope.validateBasicInfo = function() {
      if ($scope.matchedResults) {
        return handleMatchedResults();
      }   

      var participant = $scope.cpr.participant;
      if (participant.$id() || !participant.isMatchingInfoPresent()) {
        return true;
      }

      var criteria = participant.getMatchingCriteria();
      if (angular.equals($scope.ignoredCrit, criteria)) {
        return true;
      }

      $scope.matchedResults = undefined;
      return participant.getMatchingParticipants().then(
        function(result) {
          if (result.matchedAttr == 'none') {
            $scope.ignoredCrit = undefined;
            return true;
          }

          $scope.matchedResults = result;
          $scope.showMatchingParticipants = true;
          $scope.origParticipant = $scope.cpr.participant;
          return false;
        }
      );
    };

    $scope.selectParticipant = function(participant) {
      $scope.selectedParticipant = true;
      $scope.cpr.participant = participant;
    };

    $scope.lookupAgain = function() {
      $scope.matchedResults = undefined;
      $scope.showMatchingParticipants = false;
      $scope.cpr.participant = $scope.origParticipant;
      $scope.origParticipant = undefined;
      $scope.selectedParticipant = false;
      $scope.ignoredCrit = undefined;
    };

    $scope.ignoreMatches = function(wizard) {
      $scope.matchedResults = undefined;
      $scope.showMatchingParticipants = false;
      $scope.ignoredCrit = $scope.origParticipant.getMatchingCriteria();
      wizard.next(false);
    };

    $scope.register = function() {
      var cpr = angular.copy($scope.cpr);
      cpr.cpId = $scope.cpId;
      cpr.participant.ssn = cpr.participant.formatSsn();
      cpr.$saveOrUpdate().then(
        function(savedCpr) {
          $modalInstance.close(savedCpr);
        }
      );
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };
  });
