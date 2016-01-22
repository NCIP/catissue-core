
var ui = ui || {};
ui.os = ui.os || {};
ui.os.server = {
  /*hostname: 'localhost', // testing purpose
  port: 8080,
  secure: false,
  app: '/openspecimen'*/
};

ui.os.appProps = {
  plugins: []
};

(function($) {
  var pluginScriptsCnt = 0;
  var pluginsCnt = 0;

  function init() {
    var server = ui.os.server;

    server.url = '';
    if (server.hostname) {
      var protocol = server.secure ? 'https://' : 'http://';
      server.url = protocol + server.hostname + ':' + server.port + server.app + '/';
    }

    $.get(server.url + 'rest/ng/config-settings/app-props')
      .success(
        function(appProps) {
          appProps = appProps || {};
          appProps.plugins = appProps.plugins || [];

          ui.os.appProps = appProps;
          if (appProps.plugins.length > 0) {
            appProps.plugins.forEach(loadPluginResources);
          } else {
            bootstrapApp();
          }
        }
      ).fail(
        function() {
          console.log("Failed to load app props. Initialisation might fail");
          bootstrapApp();
        }
      );
  }

  function loadPluginResources(plugin) {
    var url = 'plugin-ui-resources/' + plugin + '/def.json';
    $.get(url).success(
      function(def) {
        def.styles = def.styles || [];
        def.styles.forEach(loadCss);

        def.scripts = def.scripts || [];
        def.scripts.forEach(loadScript);

        --pluginsCnt;
        bootstrapAppIfAllResourcesLoaded();
      }
    ).fail(
      function() {
        console.log("Failed to load resources of plugin: " + plugin);

        --pluginsCnt;
        bootstrapAppIfAllResourcesLoaded();
      }
    );

    ++pluginsCnt;
  }

  function loadCss(src) {
    var link = document.createElement('link');
    link.rel = "stylesheet";
    link.type = "text/css";
    link.href = 'plugin-ui-resources/' + src
    document.head.appendChild(link);
  }

  function loadScript(src) {
    var script = document.createElement('script');
    script.src = 'plugin-ui-resources/' + src;
    document.body.appendChild(script);
    
    script.onload = /*script.onreadystatechange = */ onScriptLoadSuccess;
    script.onerror = onScriptLoadError;

    ++pluginScriptsCnt;
  }

  function onScriptLoadSuccess() {
    --pluginScriptsCnt;
    bootstrapAppIfAllResourcesLoaded();
  }

  function onScriptLoadError() {
    console.log("Failed to load: " + this.src);
    onScriptLoadSuccess();
  }

  function getPluginModules(plugins) {
    return plugins.map(pluginModuleName).filter(isModuleDefined);
  }

  function pluginModuleName(plugin) {
    return 'os.plugins.' + plugin;
  }

  function isModuleDefined(moduleName) {
    try {
      angular.module(moduleName);
      return true;
    } catch (error) {
      console.log("Module " + moduleName + " not defined");
      return false;
    }
  }

  function bootstrapAppIfAllResourcesLoaded() {
    if (pluginsCnt <= 0 && pluginScriptsCnt <= 0) {
      bootstrapApp();
    }
  }
    
  function bootstrapApp() {
    console.log("Bootstraping...");
    var definedModules = getPluginModules(ui.os.appProps.plugins);
    angular.bootstrap(document, ['openspecimen'].concat(definedModules));
  }

  init();
})(jQuery);
