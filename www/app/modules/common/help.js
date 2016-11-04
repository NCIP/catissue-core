
angular.module('openspecimen')
  .directive('osHelp', function(SettingUtil) {
    function linker(scope, element, attrs) {
      scope.link = '';

      var module = attrs.module ? attrs.module : 'training';
      if (attrs.link) {
        loadLink(scope, module, attrs.link);
      }


    }

    function loadLink(scope, module, property) {
      SettingUtil.getSetting(module, property).then(
        function(setting) {
          scope.link = setting.value;
        }
      );
    }

    return {
      restrict: 'E',
      replace: true,
      link : linker,
      template: '<a ng-if="link" ng-href="{{link}}" target="_blank">' +
                '  <span class="fa fa-question-circle"></span>' +
                '  <span translate="common.buttons.help">Help</span>' +
                '</a>'
    };
  });
