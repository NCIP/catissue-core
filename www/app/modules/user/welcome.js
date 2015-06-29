angular.module('openspecimen')
  .controller('WelcomeCtrl', function ($scope, $sce, videoSettings) {
    $scope.videoSettings = videoSettings;
    $scope.iframeUrl = $sce.trustAsResourceUrl(videoSettings.welcome_video_url);
  });
