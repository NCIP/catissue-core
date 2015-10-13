
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
      if ($scope.global.appProps.mrn_restriction_enabled) {
        sites = $scope.cpr.getMrnSites();
        if (sites && sites.length == 0) {
          sites = undefined;
        }
      }

      // Participant Authorization Options
      $scope.participantResource = {
        updateOpts: {
          resource: 'ParticipantPhi',
          operations: ['Update'],
          cp: $scope.cpr.cpShortTitle,
          sites: sites
        },

        deleteOpts: {
          resource: 'ParticipantPhi',
          operations: ['Delete'],
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
        },

        deleteOpts: {
          resource: 'VisitAndSpecimen',
          operations: ['Delete'],
          cp: $scope.cpr.cpShortTitle,
          sites: sites
        }
      }

      // Specimen Tree Authorization Options
      var update = AuthorizationService.isAllowed($scope.specimenResource.updateOpts);
      var del = AuthorizationService.isAllowed($scope.specimenResource.deleteOpts);
      $scope.specimenAllowedOps = {update: update, delete: del};

      // Surgical Pathology Report Authorization Options
      $scope.sprResource = {
        readOpts: {
          resource: 'SurgicalPathologyReport',
          operations: ['Read'],
          cp: $scope.cpr.cpShortTitle,
          sites: sites
        },

        updateOpts: {
          resource: 'SurgicalPathologyReport',
          operations: ['Update'],
          cp: $scope.cpr.cpShortTitle,
          sites: sites
        },

        deleteOpts: {
          resource: 'SurgicalPathologyReport',
          operations: ['Delete'],
          cp: $scope.cpr.cpShortTitle,
          sites: sites
        },

        lockOpts: {
          resource: 'SurgicalPathologyReport',
          operations: ['Lock'],
          cp: $scope.cpr.cpShortTitle,
          sites: sites
        },

        unlockOpts: {
          resource: 'SurgicalPathologyReport',
          operations: ['Unlock'],
          cp: $scope.cpr.cpShortTitle,
          sites: sites
        }
      }
    }

    init();
  });
