
angular.module('os.biospecimen.specimen.overview', ['os.biospecimen.models'])
  .controller('SpecimenOverviewCtrl', function($scope, $rootScope, specimen) {
    function init() {
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
