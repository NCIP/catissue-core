
angular.module('os.biospecimen.specimen.overview', ['os.biospecimen.models'])
  .controller('SpecimenOverviewCtrl', function($scope, $rootScope, hasSde, sysDict, cpDict, specimen) {
    function init() {
      $scope.ctx = {
        hasDict: hasSde && (cpDict.length > 0 || sysDict.length > 0),
        sysDict: sysDict,
        cpDict: cpDict,
        obj: {specimen: $scope.specimen},
        inObjs: ['specimen'],
        exObjs: ['specimen.events', 'specimen.collectionEvent', 'specimen.receivedEvent']
      }

      loadActivities();

      $scope.$watch('specimen.activityStatus', function(newVal, oldVal) {
        if (newVal == oldVal) {
          return;
        }

        if (newVal == 'Closed') {
          loadActivities();
        }
      });
    }

    function loadActivities() {
      $scope.activities = [];
      specimen.getEvents(
        function(activities) {
          $scope.activities = activities.map(
            function(activity) {
              return angular.extend({global: $rootScope.global}, activity);
            }
          );
        }
      );
    };

    init();
  });
