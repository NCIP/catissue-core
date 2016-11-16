
angular.module('os.common')
  .config(function($stateProvider) {
    $stateProvider.state('file-download', {
      url: '/file-download?downloadUrl',
      controller: 'FileDownloadCtrl',
      parent: 'signed-in'
    })
  })
  .controller('FileDownloadCtrl',
    function($timeout, $stateParams, $state, $window, Alerts) {
      $state.go('home');
      Alerts.info('common.download_initiated');
      $timeout(function() { $window.open($stateParams.downloadUrl, '_self') }, 500);
    }
  );
