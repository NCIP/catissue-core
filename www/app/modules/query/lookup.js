
angular.module('os.query.lookup', [])
  .directive('osLookup', function($http, ApiUrls) {
    function formatOptions(format, options) {
      return options.map(
        function(option) {
          return format.replace(
            /{{(\w+)}}/g,
            function(all, s) {
              if (option) {
                return option[s];
              }

              return "undefined";
            }
          );
        }
      );
    };

    return {
      restrict: 'AE',

      scope: {
        ngModel: '=',
        placeholder: '@',
        opts: '='
      },

      replace: true,

      controller: function($scope) {
        var opts = $scope.opts;
        var url = ApiUrls.getBaseUrl() + opts.apiUrl.substring(8);

        $scope.searchTerm = function(searchTerm) {
          var params = {};
          params[opts.searchTermName] = searchTerm;

          $http.get(url, {params: params}).then(
            function(result) {
              var options = result.data;
              if (opts.respField) {
                options = result.data[opts.respField];
                if (!options) {
                  options = result.data;
                }
              }

              $scope.options = formatOptions(opts.resultFormat, options);
            }
          );
        };
      },
  
      link: function(scope, element, attrs, ctrl) {
        if (!scope.ngModel && angular.isDefined(attrs.multiple)) {
          scope.ngModel = [];
        }
      },

      template: function(tElem, tAttrs) {
        return angular.isDefined(tAttrs.multiple) ?
              '<div>' +
                '<ui-select multiple ng-model="$parent.ngModel" reset-search-input="true" append-to-body="true">' +
                  '<ui-select-match placeholder="{{$parent.placeholder}}">' +
                    '{{$item}}' +
                  '</ui-select-match>' +
                  '<ui-select-choices repeat="option in options" refresh="searchTerm($select.search)" refresh-delay="750">' +
                    '<span ng-bind-html="option | highlight: $select.search"></span>' +
                  '</ui-select-choices>' +
                '</ui-select>' +
              '</div>'

              :

              '<div>' +
                '<ui-select ng-model="$parent.ngModel" reset-search-input="true" append-to-body="true">' +
                  '<ui-select-match placeholder="{{$parent.placeholder}}">' +
                    '{{$select.selected}}' +
                  '</ui-select-match>' +
                  '<ui-select-choices repeat="option in options" refresh="searchTerm($select.search)" refresh-delay="750">' +
                    '<span ng-bind-html="option | highlight: $select.search"></span>' +
                  '</ui-select-choices>' + 
                '</ui-select>' +
              '</div>';
      }
    };
  });
