angular.module('os.common')
  .factory('osDisplayList', function($translate) {
    function DisplayListFactory() {
      var list = {
        items: [],
        inited: false
      };

      list.getItems = function() {
        return $translate('common.none').then(
          function() {
            if (list.inited) {
              return list.items;
            }

            angular.forEach(list.items,
              function(item) {
                item.caption = $translate.instant(item.key);
              }
            );

            list.inited = true;
            return list.items;
          }
        );
      }

      list.addItem = function(item) {
        var existing = undefined;
        for (var i = 0; i < list.items.length; ++i) {
          if (list.items[i].name == item.name) {
            existing = list.items[i];
            break;
          }
        }

        if (!existing) {
          list.items.push(item);
        }
      }
    
      return list;
    };

    return DisplayListFactory;
  });
