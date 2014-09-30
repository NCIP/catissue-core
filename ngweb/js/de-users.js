
var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.fancy = openspecimen.ui.fancy || {};

openspecimen.ui.fancy.Users = function() {
  var baseUrl = '/openspecimen/rest/ng/users/';

  var userCacheMap = {};
 
  var defaultList = [];

  var signedInUser;

  this.getUsers = function(queryTerm, callback) {
    if (!queryTerm && defaultList.length > 0) {
      callback(defaultList);
      return;
    }

    var xhr;
    if (queryTerm) {
      xhr = $.ajax({type: 'GET', url: baseUrl, data: {searchString: queryTerm, sortBy:'lastName,firstName'}});
    } else if (this.getAllUsersXhr) {
      xhr = this.getAllUsersXhr;
    } else {
      xhr = this.getAllUsersXhr = $.ajax({type: 'GET', url: baseUrl, data: {sortBy:'lastName,firstName'}});
    }
   
    xhr.done(
      function(data) {
        var result = [];
        var users = data.users;
        for (var i = 0; i < users.length; ++i) {
          result.push({id: users[i].id, text: users[i].lastName + ', ' + users[i].firstName});
        }

        if (!queryTerm) {
          defaultList = result;
        }

        callback(result);
      }
    ).fail(
      function(data) {
        alert("Failed to load users list");
      }
    );
  };

  this.getUser = function(userId, callback) {
    var user = userCacheMap[userId];
    if (user) {
      callback(user);
      return;
    }

    for (var i = 0; i < defaultList.length; ++i) {
      if (defaultList[i].id == userId) { 
        callback(defaultList[i]);
        return;
      }
    }

    $.ajax({type: 'GET', url: baseUrl + userId})
      .done(function(data) {
        var result = {id: data.id, text: data.lastName + ', ' + data.firstName};
        userCacheMap[userId] = result;
        callback(result);
      })
      .fail(function(data) {
        alert("Failed to retrieve user")
      });
  };

  this.getSignedInUser = function(callback) {
    if (signedInUser) {
      callback(signedInUser);
      return;
    }

    var that = this;
    $.ajax({type: 'GET', url: baseUrl + '/signed-in-user'})
      .done(function(data) {
        var result = {id: data.id, text: data.lastName + ', ' + data.firstName};
        userCacheMap[data.id] = result;
        signedInUser = result;
        callback(result);
      })
      .fail(function(data) {
        alert("Failed to retrieve user");
      });
  };
};

var usersSvc = new openspecimen.ui.fancy.Users();

openspecimen.ui.fancy.UserField = function(params) {
  this.inputEl = null;

  this.control = null;

  this.value = '';

  this.validator;

  var field = params.field;
  var id = params.id;
  var timeout = undefined;
  var that = this;

  var qFunc = function(qTerm, qCallback) {
    var timeInterval = 500;
    if (qTerm.length == 0) {
      timeInterval = 0;
    }

    if (timeout != undefined) {
      clearTimeout(timeout);
    }

    timeout = setTimeout(
      function() { 
        usersSvc.getUsers(qTerm, qCallback); 
      }, 
      timeInterval);
  };

  var onChange = function(selected) { 
    if (selected) {
      this.value = selected.id;
    } else {
      this.value = '';
    }
  };

  var initSelectedUser = function(userId, elem, callback) {
    if (!userId) {
      usersSvc.getSignedInUser(function(user) {
        that.value = user.id;
        that.control.setValue(user.id);
        callback(user);
      });
      return;
    }

    usersSvc.getUser(userId, callback);
  };

  this.render = function() {
    this.inputEl = $("<input/>")
      .prop({id: id, title: field.toolTip, value: field.defaultValue})
      .css("border", "0px").css("padding", "0px")
      .val("")
      .addClass("form-control");
    this.validator = new edu.common.de.FieldValidator(field.validationRules, this);
    return this.inputEl;
  };

  this.postRender = function() {
    this.control = new Select2Search(this.inputEl);
    this.control.onQuery(qFunc).onChange(onChange);
    this.control.setValue(this.value);

    this.control.onInitSelection(
      function(elem, callback) {
        initSelectedUser(that.value, elem, callback);
      }
    ).render();

  };

  this.getName = function() {
    return field.name;
  };

  this.getCaption = function() {
    return field.caption;
  };

  this.getTooltip = function() {
    return field.toolTip ? field.toolTip : field.caption;
  };

  this.getValue = function() {
    var val = this.control.getValue();
    if (val) {
      val = val.id;
    }

    return {name: field.name, value: val ? val : ''};
  };

  this.getDisplayValue = function() {
    if(!this.control) {
      this.postRender();
    }
    var val = this.control.getValue();
    if (val) {
      var displayValue = val.text;
    }
    return {name: field.name, value: displayValue ? displayValue : '' };
  }

  this.setValue = function(recId, value) {
    this.recId = recId;
    this.value = value ? value : '';
    if (this.control) {
      this.control.setValue(value);
    }
  };

  this.validate = function() {
    return this.validator.validate();
  };

  this.getPrintEl = function() {
    return edu.common.de.Utility.getPrintEl(this);
  };
};

edu.common.de.FieldManager.getInstance()
  .register({
    name: "userField", 
    displayName: "User Dropdown",
    fieldCtor: openspecimen.ui.fancy.UserField
  }); 
