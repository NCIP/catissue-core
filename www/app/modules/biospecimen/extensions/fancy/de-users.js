var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.fancy = openspecimen.ui.fancy || {};

openspecimen.ui.fancy.Users = edu.common.de.LookupSvc.extend({
  getApiUrl: function() {
    var userSvc = angular.element($("#de-form")).injector().get('User');
    return userSvc.url();
  },

  searchRequest: function(searchTerm) {
    return {searchString: searchTerm, sortBy: 'lastName,firstName'};
  },

  formatResults: function(users) {
    var result = [];
    for (var i = 0; i < users.length; ++i) {
      result.push({id: users[i].id, text: users[i].lastName + ', ' + users[i].firstName});
    }

    return result;
  },

  formatResult: function(data) {
    return {id: data.id, text: data.lastName + ', ' + data.firstName};
  },

  getDefaultValue: function() {
    var currentUser = angular.element($("#de-form")).scope().currentUser;
    var deferred = $.Deferred();
    deferred.resolve(currentUser);
    return deferred.promise();
  },

  getHeaders: function() {
    var $http = angular.element($("#de-form")).injector().get('$http');
    return {'X-OS-API-TOKEN': $http.defaults.headers.common['X-OS-API-TOKEN']};
  }
});

openspecimen.ui.fancy.UserField = edu.common.de.LookupField.extend({
  svc: new openspecimen.ui.fancy.Users()
});

edu.common.de.FieldManager.getInstance()
  .register({
    name: "userField", 
    displayName: "User Dropdown",
    fieldCtor: openspecimen.ui.fancy.UserField
  }); 
