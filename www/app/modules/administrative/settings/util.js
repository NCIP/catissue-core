angular.module('os.administrative.setting.util', [])
  .factory('SettingUtil', function(Setting) {
    var settings = {};

    function getSetting(module, property) {
      var key = module + ':' + property;

      var setting = settings[key];
      if (!setting || (new Date().getTime() - setting.time) / (60 * 60 * 1000) >= 1) {
        setting = {
          promise: Setting.getByProp(module, property),
          time: new Date().getTime()
        };
        settings[key] = setting;
      }

      setting.promise.catch(
        function() {
          //
          // rejected promise, remove it from cache so that it can
          // be loaded again in subsequent invocations...
          //
          delete settings[key];
        }
      );

      return setting.promise;
    }

    function clearSetting(module, property) {
      var key = module + ':' + property;
      if (settings[key]) {
        delete settings[key];
      }
    }

    return {
      getSetting: getSetting,

      clearSetting: clearSetting
    }
  });
