
angular.module('openspecimen')
  .directive('osSelect', function() {
    return {
      restrict: 'AE',

      scope: {
        ngModel: '=ngModel',
        list: '=list',
        placeholder: '@'
      },

      replace: true,

      link: function(scope, element, attrs, ctrl) {
        if (!scope.ngModel && angular.isDefined(attrs.multiple)) {
          scope.ngModel = [];
        }
      },

      template: function(tElem, tAttrs) {
        return angular.isDefined(tAttrs.multiple) ?
              '<div>' +
                '<ui-select multiple ng-model="$parent.ngModel">' +
                  '<ui-select-match placeholder="{{$parent.placeholder}}">' +
                    '{{$item}}' +
                  '</ui-select-match>' +
                  '<ui-select-choices repeat="item in $parent.list">' +
                    '<span ng-bind-html="item | highlight: $select.search"></span>' +
                  '</ui-select-choices>' +
                '</ui-select>' +
              '</div>'

              :

              '<div>' +
                '<ui-select ng-model="$parent.ngModel">' +
                  '<ui-select-match placeholder="{{$parent.placeholder}}">' +
                    '{{$select.selected}}' +
                  '</ui-select-match>' +
                  '<ui-select-choices repeat="item in $parent.list">' +
                    '<span ng-bind-html="item | highlight: $select.search"></span>' +
                  '</ui-select-choices>' + 
                '</ui-select>' +
              '</div>';
      }
    };
  });
