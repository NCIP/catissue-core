
angular.module('os.biospecimen.participant.root', ['os.biospecimen.models'])
  .controller('ParticipantRootCtrl', function($scope, cpr, AuthorizationService) {

    function init() {
      $scope.cpr = $scope.object = cpr;
      $scope.entityType = 'Participant';
      $scope.extnState = 'participant-detail.extensions.';

      initAuthorizationOpts();
    }

    function initAuthorizationOpts() {
      var sites = undefined;
      if ($scope.cpr.participant.pmis) {
        sites = $scope.cpr.participant.pmis.map(function(pmi) { return pmi.siteName; });
        sites = sites.length > 0 ? sites : undefined;
      }

      // Participant Authorization Options
      $scope.participantResource = {
        updateOpts: {
          resource: 'ParticipantPhi',
          operations: ['Update'],
          cp: $scope.cpr.cpShortTitle,
          sites: sites
        }
      };

      // Specimen and Visit Authorization Options
      $scope.specimenResource = {
        updateOpts: {
          resource: 'VisitAndSpecimen',
          operations: ['Update'],
          cp: $scope.cpr.cpShortTitle,
          sites: sites
        }
      }

      // Specimen Tree Authorization Options
      var update = AuthorizationService.isAllowed($scope.specimenResource.updateOpts);
      $scope.specimenAllowedOps = {update: update};
    }

    init();
  });
