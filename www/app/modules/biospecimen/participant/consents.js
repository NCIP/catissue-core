
angular.module('os.biospecimen.participant.consents', ['os.biospecimen.models'])
  .controller('ConsentsCtrl', function(
    $scope, $state, $stateParams, $sce, cpr,
    CollectionProtocolRegistration, DeleteUtil) {

    $scope.consentDocUploader = {};
    $scope.consentDocUrl = !!cpr ? $sce.trustAsResourceUrl(cpr.getSignedConsentDocUrl()) : "";

    function init() {
      $scope.uploadMode = false;
    }

    function deleteConsentDoc() {
      cpr.deleteSignedConsentDoc().then(
        function(result) {
          if (result) {
            cpr.consentDetails.consentDocumentName = undefined;
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

    $scope.uploadConsentDoc = function() {
      $scope.consentDocUploader.submit().then(
        function(fileName) {
          cpr.consentDetails.consentDocumentName = fileName;
          $scope.uploadMode = false;
        }
      );
      return false;
    }

    $scope.confirmDeleteConsentDoc = function () {
      DeleteUtil.confirmDelete({
        entity: cpr.consentDetails,
        templateUrl: 'modules/biospecimen/participant/confirm-delete-consent-doc.html',
        delete: deleteConsentDoc
      });
    }
   
  });
