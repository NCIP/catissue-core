
angular.module('os.biospecimen.participant.consents', [])
  .controller('ParticipantConsentsCtrl', function($scope, $sce, cpr, CollectionProtocol,
     DeleteUtil, AuthorizationService) {

    function init() {
      $scope.consentFormUploader = {};
      $scope.consentFormUrl = $sce.trustAsResourceUrl(cpr.getSignedConsentFormUrl());
      $scope.consent = angular.copy(cpr.consentDetails);
      $scope.uploadMode = false;
      $scope.consentEditMode = true;
      $scope.consentUpdateRight = AuthorizationService.isAllowed($scope.participantResource.updateOpts);

      if ($scope.consent.consentTierResponses.length == 0) {
        $scope.consentEditMode = false;
        CollectionProtocol.getConsentTiers(cpr.cpId).then(function(consents) {
          angular.forEach(consents, function(consent) {
            $scope.consent.consentTierResponses.push({consentStatement: consent.statement});
          });
        });
      }
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

    $scope.saveConsentTierResponses = function() {
      cpr.saveConsentResponse().then(function(result) {
      })
    }

    init();
  });
