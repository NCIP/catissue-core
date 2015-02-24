
angular.module('openspecimen')
  .config(function($stateProvider) {
    $stateProvider
      .state('login', {
        url: '/',
        templateUrl: 'modules/user/signin.html',
        controller: 'LoginCtrl',
        parent: 'default'
      })
      .state('forgot-password', {
        url: '/forgot-password',
        templateUrl: 'modules/user/forgot-password.html',
        controller: 'ForgotPasswordCtrl',
        parent: 'default'
      })
      .state('reset-password', {
        url: '/reset-password',
        templateUrl: 'modules/user/reset-password.html',
        controller: 'ResetPasswordCtrl',
        parent: 'default'
      })
  });


