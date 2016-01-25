var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.fancy = openspecimen.ui.fancy || {};

openspecimen.ui.fancy.Sites = edu.common.de.LookupSvc.extend({
  getApiUrl: function() {
    var siteSvc = angular.element($("#de-form")).injector().get('Site');
    return siteSvc.url();
  },

  searchRequest: function(searchTerm) {
    return {searchString: searchTerm, sortBy: 'name'};
  },

  formatResults: function(sites) {
    var result = [];
    for (var i = 0; i < sites.length; ++i) {
      result.push({id: sites[i].id, text: sites[i].name});
    }

    return result;
  },

  formatResult: function(data) {
    return !data ? "" : {id: data.id, text: data.name};
  },

  getDefaultValue: function() {
    var deferred = $.Deferred();
    deferred.resolve(undefined);
    return deferred.promise();
  },

  getHeaders: function() {
    var $http = angular.element($("#de-form")).injector().get('$http');
    return {'X-OS-API-TOKEN': $http.defaults.headers.common['X-OS-API-TOKEN']};
  }
});

openspecimen.ui.fancy.SiteField = edu.common.de.LookupField.extend({
  svc: new openspecimen.ui.fancy.Sites()
});

edu.common.de.FieldManager.getInstance()
  .register({
    name: "siteField", 
    displayName: "Site Dropdown",
    fieldCtor: openspecimen.ui.fancy.SiteField
  }); 
