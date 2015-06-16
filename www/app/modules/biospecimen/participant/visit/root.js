angular.module('os.biospecimen.visit.root', ['os.biospecimen.models'])
  .controller('VisitRootCtrl', function($scope, cpr, visit) {

    function init() {
      $scope.visit = $scope.object = visit;
      $scope.entityType = 'SpecimenCollectionGroup';
      $scope.extnState = 'visit-detail.extensions.';

      initAuthorizationOpts();
    }

    function initAuthorizationOpts() {
      var sites = cpr.getMrnSites();
      if (sites && sites.length == 0) {
        sites = undefined;
      }

      $scope.sprResource = {
        readOpts: {
          resource: 'SurgicalPathologyReport',
          operations: ['Read'],
          cp: cpr.cpShortTitle,
          sites: sites
        },

        updateOpts: {
          resource: 'SurgicalPathologyReport',
          operations: ['Update'],
          cp: cpr.cpShortTitle,
          sites: sites
        },

        deleteOpts: {
          resource: 'SurgicalPathologyReport',
          operations: ['Delete'],
          cp: cpr.cpShortTitle,
          sites: sites
        },

        lockOpts: {
          resource: 'SurgicalPathologyReport',
          operations: ['Lock'],
          cp: cpr.cpShortTitle,
          sites: sites
        },

        unlockOpts: {
          resource: 'SurgicalPathologyReport',
          operations: ['Unlock'],
          cp: cpr.cpShortTitle,
          sites: sites
        }
      }
    }

    init();
  });
