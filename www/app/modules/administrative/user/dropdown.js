
angular.module('os.administrative.user.dropdown', ['os.administrative.models'])
  .directive('osUsers', function(User) {
    return {
      restrict: 'AE',

      scope: {
        ngModel: '=ngModel',
        placeholder: '@'
      },

      replace: true,

      controller: function($scope) {
        $scope.searchUsers = function(searchTerm) {
          User.query({searchString: searchTerm}).then(
            function(result) {
              $scope.users = result;
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
                '<ui-select multiple ng-model="$parent.ngModel">' +
                  '<ui-select-match placeholder="{{$parent.placeholder}}">' +
                    '{{$item.lastName}}, {{$item.firstName}}' +
                  '</ui-select-match>' +
                  '<ui-select-choices repeat="user in users" refresh="searchUsers($select.search)" refresh-delay="750">' +
                    '<span ng-bind-html="user.lastName + \', \' + user.firstName | highlight: $select.search"></span>' +
                  '</ui-select-choices>' +
                '</ui-select>' +
              '</div>'

              :

              '<div>' +
                '<ui-select ng-model="$parent.ngModel">' +
                  '<ui-select-match placeholder="{{$parent.placeholder}}">' +
                    '{{$select.selected.lastName}}, {{$select.selected.firstName}}' +
                  '</ui-select-match>' +
                  '<ui-select-choices repeat="user in users" refresh="searchUsers($select.search)" refresh-delay="750">' +
                    '<span ng-bind-html="user.lastName + \', \' + user.firstName | highlight: $select.search"></span>' +
                  '</ui-select-choices>' + 
                '</ui-select>' +
              '</div>';
      }
    };
  });
