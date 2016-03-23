angular.module('os.administrative.setting.util', [])
  .factory('SettingUtil', function(Setting) {
    var settings = {};

    function getSetting(module, property) {
      var setting = settings[module + ":" + property];
      if (!setting || (new Date().getTime() - setting.time) / (60 * 60 * 1000) >= 1) {
        setting = {
          promise: Setting.getByProp(module, property),
          time: new Date().getTime()
        };
        settings[module + ":" + property] = setting;
      }

      return setting.promise;
    }

    function clearSetting(module, property) {
      var setting = settings[module + ":" + property];
      if (setting) {
        delete settings[module + ":" + property];
      }
    }

    return {
      getSetting: getSetting,

      clearSetting: clearSetting
    }
  });
