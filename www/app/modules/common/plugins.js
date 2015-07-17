
angular.module('openspecimen')
  .factory('PluginReg', function() {
    /**
     * Structure
     * {
     *   plugin_name: {
     *     view_name: {
     *       section_name: {
     *         template: template.html,
     *         // there could be other props in future 
     *       },
     *
     *       // more sections within same view
     *     },
     *
     *     // more views within same plugin
     *   },
     * }
     */

     var pluginViews = {};

     var activePlugins = [];

     function registerViews(pluginName, views) {
       pluginViews[pluginName] = views;
     }

     function getViews(pluginName) {
       return pluginViews[pluginName] || {};
     }

     function getTmpls(viewName, secName) {
       var tmpls = [];
       angular.forEach(pluginViews, function(pViews, pName) {
         if (activePlugins.indexOf(pName) == -1) {
           //
           // plugin is not used
           //
           return; 
         }

         angular.forEach(pViews, function(pViewTmpls, pViewName) {
           if (viewName !== pViewName) {
             return;
           }

           angular.forEach(pViewTmpls, function(pViewSecTmpl, pViewSecName) {
             if (pViewSecName !== secName) {
               return;
             }

             tmpls.push(pViewSecTmpl.template);
           });
         });
       });

       return tmpls;
     }

     function usePlugins(pluginNames) {
       activePlugins = [].concat(pluginNames); 
     }

     return {
       registerViews: registerViews,

       getViews: getViews,

       getTmpls: getTmpls,

       usePlugins: usePlugins
     };
  })

  .directive('osPluginHooks', function(PluginReg) {
    return {
      restrict: 'AE',

      replace: true,

      scope: {
        viewName: '=',

        secName: '='     
      },

      template: '<ng-include src="tmpl" ng-repeat="tmpl in hookTmpls"></ng-include>',

      link: function(scope, element, attrs) {
        scope.hookTmpls = [];
        scope.$watchGroup(['viewName', 'secName'], function(newVals) {
          scope.hookTmpls = PluginReg.getTmpls(scope.viewName, scope.secName);
        });
      }
    };
  });
