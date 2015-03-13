
angular.module('os.biospecimen.specimen.overview', ['os.biospecimen.models'])
  .controller('SpecimenOverviewCtrl', function($scope, specimen) {
    function loadActivities() {
      $scope.activities = [];
      specimen.getActivities().then(
        function(activities) {
          $scope.activities = activities;
        }
      ); 
    };

    loadActivities();
  });
