angular.module('openspecimen')
  .controller('AboutOSCtrl', function($scope, $modal) {

    $scope.showAboutOS = function() {
      $modal.open({
        templateUrl: 'modules/common/about.html',
        windowClass: 'os-about-modal',
        size: 'sm'
      });
    };
  });
