
angular.module('os.biospecimen.participant.root', ['os.biospecimen.models'])
  .controller('ParticipantRootCtrl', function($scope, cpr, AuthorizationService) {

    function init() {
      $scope.cpr = $scope.object = cpr;
      $scope.entityType = 'Participant';
      $scope.extnState = 'participant-detail.extensions.';

      initAuthorizationOpts();
    }

    function initAuthorizationOpts() {

      // Participant Authorization Options
      $scope.participantResource = {
        updateOpts: {resource: 'ParticipantPhi', operations: ['Update'], cp: $scope.cpr.cpShortTitle}
      }

      // Specimen and Visit Authorization Options
      $scope.specimenResource = {
        updateOpts: {resource: 'VisitAndSpecimen', operations: ['Update'], cp: $scope.cpr.cpShortTitle}
      }

      $scope.sites = $scope.cpr.participant.pmis.map(
        function(pmi) {
          return pmi.siteName;
        }
      );
      if ($scope.sites.length > 0) {
        angular.extend($scope.specimenResource.updateOpts, {sites: $scope.sites});
      }

      // Specimen Tree Authorization Options
      var update = AuthorizationService.isAllowed($scope.specimenResource.updateOpts);
      $scope.specimenTreeOpts = {update: update};
    }

    init();
  });