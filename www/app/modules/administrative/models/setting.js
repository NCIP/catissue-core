angular.module('os.administrative.models.setting', ['os.common.models'])
  .factory('Setting', function(osModel, $http) {
    var Setting = new osModel('config-settings');

    Setting.getLocale = function() {
      return $http.get(Setting.url() + 'locale').then(
        function(resp) {
          return resp.data;
        }
      );
    }
    
    Setting.getWelcomeVideoSetting = function () {
      return $http.get(Setting.url() + 'welcome-video').then(
        function (resp) {
          return resp.data;
        }
      );
    };

    Setting.getAppProps = function() {
      return $http.get(Setting.url() + 'app-props').then(
        function(resp) {
          return resp.data;
        }
      );
    };

    Setting.updateSetting = function(setting) {
      return $http.put(Setting.url(), setting).then(
        function(resp) {
          return new Setting(resp.data);
        }
      );
    }

    return Setting;
  });
