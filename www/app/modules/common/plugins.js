
angular.module('openspecimen')
  .factory('PluginReg', function($rootScope) {
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

     function subscribe(scope, callback) {
       var handler = $rootScope.$on('os-plugin-views-updated', callback);
       scope.$on('$destroy', handler);
     }

     function registerViews(pluginName, views) {
       pluginViews[pluginName] = views;
       $rootScope.$emit('os-plugin-views-updated');
     }

     function getViews(pluginName) {
       return pluginViews[pluginName] || {};
     }

     function getTmpls(viewName, secName, defaultTmpl) {
       var tmpls = [];
       angular.forEach(pluginViews, function(pViews, pName) {
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

       if (tmpls.length == 0 && !!defaultTmpl) {
         tmpls.push(defaultTmpl);
       }

       return tmpls;
     }

     function usePlugins(pluginNames) {
       activePlugins = [].concat(pluginNames); 
     }

     return {
       subscribe: subscribe,

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

        secName: '=',

        defaultTmpl: '='
      },

      template: '<ng-include os-replace-with-children src="tmpl" ng-repeat="tmpl in hookTmpls"></ng-include>',

      link: function(scope, element, attrs) {
        scope.hookTmpls = [];
        scope.$watchGroup(['viewName', 'secName', 'defaultTmpl'], function(newVals) {
          scope.hookTmpls =  PluginReg.getTmpls(scope.viewName, scope.secName, scope.defaultTmpl)
        });

        PluginReg.subscribe(
          scope,
          function() {
            scope.hookTmpls = PluginReg.getTmpls(scope.viewName, scope.secName, scope.defaultTmpl);
          }
        );
      }
    };
  })

  .directive('osReplaceWithChildren', function() {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        element.replaceWith(element.children());
      }
    };
  });
