
angular.module('os.biospecimen.participant.consents', [])
  .controller('ParticipantConsentsCtrl', function($scope, $sce, cpr, consent,
    PvManager,  DeleteUtil) {

    function init() {
      $scope.consentFormUploader = {};
      $scope.consentFormUrl = $sce.trustAsResourceUrl(cpr.getSignedConsentFormUrl());
      $scope.consent = consent;
      $scope.uploadMode = false;
      $scope.editMode = false;
      $scope.consentExists = $scope.consent.consentTierResponses.length > 0;

      loadPvs();
    }

    function loadPvs() {
      /**
       * TODO:
       * Instead of below hardcoded values, use PvManager.getPvs('consent_response');
       * Current Permissible Values REST API only return alphabetically sorted values
       * here needs pvs sorted on basis of value in SORTORDER column of catissue_permissible_value table
      **/
      //$scope.consentResponses = PvManager.getPvs('consent_response');
      $scope.consentResponses = ['Yes', 'No', 'Not Specified'];
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
        $scope.editMode = $scope.consentResponseAdd = false;

      });
    }

    init();
  });
