
angular.module('os.biospecimen.participant.consents', [])
  .controller('ParticipantConsentsCtrl', function($scope, $sce, cpr, DeleteUtil) {


    function init() {
      $scope.consentFormUploader = {};
      $scope.consentFormUrl = $sce.trustAsResourceUrl(cpr.getSignedConsentFormUrl());
      $scope.consent = cpr.consentDetails;
      $scope.uploadMode = false;
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

    $scope.confirmDeleteConsentForm = function () {
      DeleteUtil.confirmDelete({
        entity: $scope.consent,
        templateUrl: 'modules/biospecimen/participant/confirm-delete-consent-form.html',
        delete: deleteConsentForm
      });
    }

    init();
  });
