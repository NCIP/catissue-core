angular.module('openspecimen')
  .directive('osAddItems', function($parse, $q) {
    return {
      restrict: 'E',

      replace: true,

      scope: {
        itemList: '=',
        onAdd: '&'
      },

      controller: function($scope) {
        $scope.addItems = function() {
          var labels =
            $scope.input.labelText.split(/,|\t|\n/)
              .map(function(label) { return label.trim(); })
              .filter(function(label) { return label.length != 0; });

          if (labels.length == 0) {
            return;
          }

          if ($scope.labelGetter) {
            angular.forEach($scope.itemList, function(item) {
              var idx = labels.indexOf($scope.labelGetter(item));
              if (idx != -1) {
                labels.splice(idx, 1);
              }
            });
          }

          if (labels.length == 0) {
            return;
          }

          $q.when($scope.onAdd({itemLabels: labels})).then(
            function(added) {
              if (added) {
                $scope.input.labelText = '';
              }
            }
          );
        }
      },

      link: function(scope, element, attrs, ctrl) {
        //
        // When item key is specified, the widget tries to remove items identified
        // by key that are already present in source list (itemList)
        //
        if (attrs.itemKey) {
          scope.labelGetter = $parse(attrs.itemKey);
        }
      },

      template: function(tElem, tAttrs) {
        return '<div class="input-group" style="margin-bottom:5px;" os-textarea-input-group> '+
               '  <textarea ng-model="input.labelText" class="form-control" ' +
               '    placeholder="' + tAttrs.placeholder + '" os-enable-tab rows="2"> ' +
               '  </textarea> '+
               '  <span class="input-group-btn"> ' +
               '    <button class="btn btn-primary" ng-click="addItems()" ng-disabled="!input.labelText"> ' +
               '      <span translate="common.buttons.add">Add</span> ' +
               '    </button> ' +
               '  </span> ' +
               '</div> ';
      }
    }
  });
