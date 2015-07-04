angular.module('openspecimen')
  .controller('WelcomeCtrl', function ($scope, $sce, $state, videoSettings) {
    function init() {
      if (videoSettings.welcome_video_source !== 'vimeo' && 
          videoSettings.welcome_video_source !== 'youtube') {
          $state.go('sign-up');
          return;
      }
      
      $scope.videoSettings = videoSettings;
      $scope.iframeUrl = $sce.trustAsResourceUrl(videoSettings.welcome_video_url);      
    }
    
    
    init();
  });
