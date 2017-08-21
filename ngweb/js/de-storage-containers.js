var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.fancy = openspecimen.ui.fancy || {};

openspecimen.ui.fancy.StorageContainers = edu.common.de.LookupSvc.extend({
  getApiUrl: function() {
    var containerSvc = angular.element($("#de-form")).injector().get('Container');
    return containerSvc.url();
  },

  searchRequest: function(searchTerm, searchFilters) {
    return $.extend({name: searchTerm}, searchFilters);
  },

  formatResults: function(containers, queryTerm) {
    var result = [];
  
    if (!queryTerm || "virtual".indexOf(queryTerm.toLowerCase()) >= 0) {
      result.push({id: null, text: 'Virtual'});
    }

    for (var i = 0; i < containers.length; ++i) {
      result.push({id: containers[i].id, text: containers[i].name});
    }

    return result;
  },

  formatResult: function(container) {
    return {id: container.id, text: container.name, actual: container};
  },

  getDefaultValue: function() {
    var deferred = $.Deferred();
    deferred.resolve({id: null, name: 'Virtual'});
    return deferred.promise();
  },

  getHeaders: function() {
    var $http = angular.element($("#de-form")).injector().get('$http');
    return {'X-OS-API-TOKEN': $http.defaults.headers.common['X-OS-API-TOKEN']};
  }
});

openspecimen.ui.fancy.StorageContainer = edu.common.de.LookupField.extend({
  svc: new openspecimen.ui.fancy.StorageContainers()
});

edu.common.de.FieldManager.getInstance()
  .register({
    name: "storageContainer", 
    displayName: "Storage Containers Dropdown",
    fieldCtor: openspecimen.ui.fancy.StorageContainer
  }); 
