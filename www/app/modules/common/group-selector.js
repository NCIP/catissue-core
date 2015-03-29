
angular.module('openspecimen')
  .directive('osGroupSelector', function($timeout) {
    function flattenMap(inputMap, allowAll) {
      var result = [];
      angular.forEach(inputMap, function(category) {
        if (!allowAll || allowAll.indexOf(category.all.category) >= 0) {
          result.push(category.all);
        }

        if (!category.all.selected) {
          result = result.concat(category.items);
        }
      });

      return result;
    };

    function generatePickList(scope, opts) {
      var itemsMap = {};
      var selectedItems = [];

      var modelCategories = scope.selectedCategories;
      var modelCategoryItems = scope.selectedCategoryItems;

      angular.forEach(opts.items, function(item) {
        var category = item[opts.categoryAttr];

        var categoryItems = itemsMap[category];
        if (!categoryItems) {
          var all = {selected: false, text: 'All ' + category, category: category, all: true};
          if (modelCategories.indexOf(all.category) != -1) {
            all.selected = true;
            selectedItems.push(all);
          }

          categoryItems = itemsMap[category] = {all: all, items: []};
        }

        var item = {selected: false, text: item[opts.valueAttr], category: category};
        if (modelCategoryItems.indexOf(item.text) != -1) {
          item.selected = true;
          selectedItems.push(item);     
        }

        categoryItems.items.push(item);
      });
    
      scope.selectedItems = selectedItems;
      scope.itemsMap = itemsMap;
      scope.itemsList = flattenMap(itemsMap, opts.allowAll);
    };

    function linker(scope, element, attrs) {
      scope.items = [];
      scope.selectedItems = [];

      var pickListGenerated = false;
      scope.$watch('opts', function(newVal, oldVal) {
        if (pickListGenerated && newVal === oldVal) {
          return;
        }

        generatePickList(scope, newVal); // scope.itemsMap, scop.selectedItems
        pickListGenerated = true;
      }, true);

      scope.onSelect = function(item) {
        $timeout(function() {
          item.selected = true;

          if (scope.itemsMap[item.category].all != item) { // if not all of category
            scope.selectedCategoryItems.push(item.text);
            return;
          }

          scope.selectedCategories.push(item.category);
          var selectedItems = scope.selectedItems;
          var modelItems = scope.selectedCategoryItems;
          for (var idx = selectedItems.length - 1; idx >= 0; idx--) {
            if (selectedItems[idx] != item && selectedItems[idx].category == item.category) {
              selectedItems[idx].selected = false;

              modelItems.splice(modelItems.indexOf(selectedItems[idx].text), 1);
              selectedItems.splice(idx, 1);
            }
          }

          scope.itemsList = flattenMap(scope.itemsMap, scope.opts.allowAll);
        }, 0); 
      };

      scope.onRemove = function(item) {
        $timeout(function() {
          item.selected = false;

          var modelItems = [];
          if (scope.itemsMap[item.category].all != item) {
            modelItems = scope.selectedCategoryItems;        
            modelItems.splice(modelItems.indexOf(item.text), 1);
          } else {
            modelItems = scope.selectedCategories;
            modelItems.splice(modelItems.indexOf(item.category), 1);
          }

          scope.itemsList = flattenMap(scope.itemsMap, scope.opts.allowAll);
        }, 0);
      };
    };

    return {
      restrict: 'E',

      scope: {
        opts: '=',
        selectedCategories: '=',
        selectedCategoryItems: '='
      },

      template: 
        '<div>' +
          '<ui-select multiple ng-model="$parent.selectedItems" on-select="$parent.onSelect($item)" on-remove="$parent.onRemove($item)">' +
            '<ui-select-match placeholder="{{$parent.placeholder}}">' +
              '{{$item.text}}' +
            '</ui-select-match>' +
            '<ui-select-choices repeat="item in $parent.itemsList | filter: $select.search" ' +
              ' group-by="\'category\'">' +
              '<span ng-bind-html="item.text | highlight: $select.search"></span>' +
            '</ui-select-choices>' +
          '</ui-select>' +
        '</div>',

      link: linker
    };
  });
