var openspecimen = openspecimen || {}
openspecimen.ui = openspecimen.ui || {};
openspecimen.ui.fancy = openspecimen.ui.fancy || {};

openspecimen.ui.fancy.Sites = function() {
  var baseUrl = '../../rest/ng/sites/';

  var siteCacheMap = {};
 
  var defaultList = [];

  this.getSites = function(queryTerm, callback) {
    if (!queryTerm && defaultList.length > 0) {
      callback(defaultList);
      return;
    }

    var xhr;
    if (queryTerm) {
      xhr = $.ajax({type: 'GET', url: baseUrl, data: {searchString: queryTerm, sortBy:'name'}});
    } else if (this.getAllSitesXhr) {
      xhr = this.getAllSitesXhr;
    } else {
      xhr = this.getAllSitesXhr = $.ajax({type: 'GET', url: baseUrl, data: {sortBy:'name'}});
    }
   
    xhr.done(
      function(sites) {
        var result = [];
        for (var i = 0; i < sites.length; ++i) {
          result.push({id: sites[i].id, text: sites[i].name});
        }

        if (!queryTerm) {
          defaultList = result;
        }

        callback(result);
      }
    ).fail(
      function(data) {
        alert("Failed to load site list");
      }
    );
  };

  this.getSite = function(siteId, callback) {
    var site = siteCacheMap[siteId];
    if (site) {
      callback(site);
      return;
    }

    for (var i = 0; i < defaultList.length; ++i) {
      if (defaultList[i].id == siteId) { 
        callback(defaultList[i]);
        return;
      }
    }

    $.ajax({type: 'GET', url: baseUrl + siteId})
      .done(function(data) {
        var result = {id: data.id, text: data.name};
        siteCacheMap[siteId] = result;
        callback(result);
      })
      .fail(function(data) {
        alert("Failed to retrieve site")
      });
  };
};

var siteSvc = new openspecimen.ui.fancy.Sites();

openspecimen.ui.fancy.SiteField = function(params) {
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
        siteSvc.getSites(qTerm, qCallback); 
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

  var initSelectedSite = function(siteId, elem, callback) {
    if (!siteId) {
      return;
    }
    siteSvc.getSite(siteId, callback);
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
        initSelectedSite(that.value, elem, callback);
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
    name: "siteField", 
    displayName: "Site Dropdown",
    fieldCtor: openspecimen.ui.fancy.SiteField
  }); 
