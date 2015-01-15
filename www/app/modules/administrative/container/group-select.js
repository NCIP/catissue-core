angular.module('os.administrative.container.groupSelect',[])
  .filter('groupSelect', function($filter) {
    return function(list, selectedList) {
      var newList = [];
      var valueAllItems = []

      angular.forEach(list, function(item, index) {
        if(item.value.indexOf('All') == 0) {
          valueAllItems.push(item);
        }
        newList.push(item);
      });

      angular.forEach(selectedList, function(item, index) {
        if (item.indexOf("All") == 0) {
            var allItem = $filter('filter')(valueAllItems, {value: item})[0];
            if(allItem) {
              newList = $filter('filter') (newList, {class: '!' + allItem.class});
            }
        }
      });
      return newList;
    }
  });


