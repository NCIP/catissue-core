
angular.module('openspecimen')
  .controller('CustomRegParticipantCtrl', 
    function($scope, $state, $stateParams, $http, 
             PvManager, ApiUrls, ApiUtil, Alerts) {

      $scope.cpId = $stateParams.cpId

      function loadPvs() {
        $scope.genders = PvManager.getPvs('gender');
        $scope.anatomicSites = PvManager.getPvs('anatomic-site');
        $scope.hospitals = PvManager.getSites();
        $scope.diagnoses = [];
      };

      function init() {
        $scope.cpr = {
          participant: {},
        };

        $scope.patientSmokingHistory = { };

        $scope.visit = { };

        $scope.blood = {collectionEvent: {}, receivedEvent: {}};

        $scope.bloodExtn = { };

        $scope.frozenTissue = {collectionEvent: {}, receivedEvent: {}};
 
        $scope.consent = {
          files: '',
          filename: ''
        };

        loadPvs();

        /*$scope.$watch('consent.files', function(newFile) {
          if (!newFile) {
            $scope.removeConsent();
            return;
          }

          uploadFile(newFile).then(
            function(result) {
              $scope.consent.filename = result.data.filename;
            }
          );
        });*/
      };

      /*function uploadFile(file) {
        return FileSvc.upload(ApiUrls.getUrl('form-files'), file);
      };*/

      $scope.removeConsent = function() {
        $scope.consent = {
          files: '',
          filename: ''
        };
      };


      function getPayload() {
        return {
          cpr: $scope.cpr,
          patientSmokingHistory: $scope.patientSmokingHistory,
          visit: $scope.visit,
          blood: $scope.blood,
          bloodExtn: $scope.bloodExtn,
          frozenTissue: $scope.frozenTissue
        };
      };

      $scope.collectSpecimens = function() {
        var url = ApiUrls.getUrl() + '/rest/ng/demo/participant-specimens';
        $http.post(url, getPayload()).then(
          function(result) {
            $state.go('participant-detail.overview', {cprId: result.data.cpr.id});
          }
        );
      };

      $scope.searchClinicalDiagnoses = function(searchTerm) {
        $scope.diagnoses = PvManager.getPvs('clinical-diagnosis', searchTerm);
      };

      init();
    }
  );
