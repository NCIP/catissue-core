angular.module('openspecimen')
  .filter('osGroupOptions', function($filter, $parse) {
    return function(list, key, selectedList, include, exclude) {
      var newList = [];
      if(list == undefined || selectedList == undefined) {
        return list;
      } 
      
      var getter = $parse(key);
      
      angular.forEach(list, function(item) {
        newList.push(item);
      });
      
      for(var i = 0; i < selectedList.length; i++) {
        if(include == undefined || getter(include) != getter(selectedList[i])) {
          newList = newList.filter(function(item) { 
            return !angular.equals(item.shortTitle, getter(selectedList[i])) 
          });
        }
      }
      
      if(exclude != undefined && getter(exclude) != undefined) {
        newList = newList.filter(function(item) { 
          return !angular.equals(item.shortTitle, getter(exclude)) 
        });
      }
    
      return newList;
    }
  })
  
  .filter('checkAll', function($filter, $parse) {
    return function(list, key, selectedList, include) {
      var getter = $parse(key);
  
      if(list == undefined || selectedList == undefined || getter(include) == 'All') {
        return list;
      } 
      
      for(var i = 0; i < selectedList.length; i++) {
        if(selectedList[i].cpTitle == 'All') {
          return [];
        }    
      }
      
      return list;
    }
  });
  
