
angular.module('openspecimen')
  .config(function($stateProvider) {
    $stateProvider
      .state('login', {
        url: '/?logout',
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
      .state('sign-up', {
        url: '/sign-up',
        templateUrl: 'modules/user/signup.html',
        resolve: {
          user: function(User) {
            return new User();
          }
        },
        controller: 'UserAddEditCtrl',
        parent: 'default'
      })
      .state('welcome', {
        url: '/welcome',
        templateUrl: 'modules/user/welcome.html',
        resolve: {
          videoSettings : function (Setting, $state) {
            return Setting.getWelcomeVideoSetting().then(function (data) {
              var result = {};
              for (i=0; i<data.length; i++) {
                if (data[i].name === 'welcome_video_source') {
                  if (data[i].value !== 'vimeo' && data[i].value !== 'youtube') {
                    $state.go('sign-up');
                    return;
                  }
                  else {
                    result['welcome_video_source'] = data[i].value;
                  }
                }
                else if (data[i].name === 'welcome_video_url') {
                  result['welcome_video_url'] = data[i].value;
                }
              }
              return result;
            });
          }
        },
        controller: 'WelcomeCtrl',
        parent: 'default'
      });
  });


