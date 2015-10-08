
angular.module('os.biospecimen.specimen.overview', ['os.biospecimen.models'])
  .controller('SpecimenOverviewCtrl', function($scope, $rootScope, $state, specimen) {
    function init() {
      loadActivities();

      //
      // On changing received event detail page should be reloaded, so that field changed in event are reflected.
      //
      if ($rootScope.stateChangeInfo.fromState.url.indexOf("formCtxId") >= 1) {
        var params = {specimenId:  $scope.specimen.id, srId:  $scope.specimen.reqId};
        $state.go('specimen-detail.overview', params, {reload: true});
      }

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
