
angular.module('os.biospecimen.participant.consents', [])
  .controller('ParticipantConsentsCtrl', function($scope, $sce, cpr, consents,
    CollectionProtocol, DeleteUtil) {

    function init() {
      $scope.consentFormUploader = {};
      $scope.consentFormUrl = $sce.trustAsResourceUrl(cpr.getSignedConsentFormUrl());
      $scope.consent = consents;
      $scope.uploadMode = false;

      $scope.existingConsentTierResponses = angular.copy($scope.consent.consentTierResponses);
      CollectionProtocol.getConsentTiers(cpr.cpId).then(function(consentTiers) {
        $scope.consent.consentTierResponses = [];
        angular.forEach(consentTiers, function(consentTier) {
          $scope.consent.consentTierResponses.push({consentStatement: consentTier.statement,
            participantResponse: getConsentStatementResponse(consentTier.statement)});
        });
        $scope.consentExists = $scope.consent.consentTierResponses.length > 0;
      });
    }

    function getConsentStatementResponse(statement) {
      var participantResponse = 'None';
      for (var i=0; i < $scope.existingConsentTierResponses.length; i++) {
        if ($scope.existingConsentTierResponses[i].consentStatement == statement) {
          participantResponse = $scope.existingConsentTierResponses[i].participantResponse;
          break;
        }
      }
      return participantResponse;
    }

    function deleteConsentForm() {
      cpr.deleteSignedConsentForm().then(
        function(result) {
          if (result) {
                $scope.consent.consentDocumentName = undefined;
          }
        }
      );
    }

    $scope.showUploadMode = function() {
      $scope.uploadMode = true;
    }

    $scope.cancel = function() {
      $scope.uploadMode = false;
    }

    $scope.uploadConsentForm = function() {
      $scope.consentFormUploader.ctrl.submit().then(
        function(fileName) {
          $scope.consent.consentDocumentName = fileName;
          $scope.uploadMode = false;
        }
      );
    }

    $scope.confirmDeleteConsentForm = function() {
      DeleteUtil.confirmDelete({
        entity: $scope.consent,
        templateUrl: 'modules/biospecimen/participant/confirm-delete-consent-form.html',
        delete: deleteConsentForm
      });
    }

    $scope.showEditConsents = function() {
      $scope.editMode = true;
    }

    $scope.saveConsents = function() {
      cpr.saveConsentResponse($scope.consent).then(function(result) {
        $scope.consents = result;
        $scope.editMode = false;
      });
    }

    init();
  });
