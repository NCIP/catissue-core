Form = Backbone.Model.extend({
  defaults: {
    id: null,
    name: null,
    caption: null,
    noOfRecords: null,

    containerId: null,
    cpId: null,
    entityType: null
  },

  url: '/catissuecore/rest/forms'
});

FormRecord = Backbone.Model.extend({
  defaults: {
    id: null,
    updatedBy: null,
    updateTime: null
  },

  parse: function(response) {
    if (response.recordId) {
      response.id = response.recordId;
    }
    return response;
  }
});


Container = Backbone.Model.extend({
  defaults: {
    id: null,
    name: null,
    caption: null,
    creationTime: null,
    createdBy: null
  }
});

CollectionProtocol = Backbone.Model.extend({
  defaults: {
    id: null,
    shortTitle: null,
    title: null
  }
});

FormCollection = Backbone.Collection.extend({
  model: Form,

  initialize: function(appData) {
    this.appData = appData;
  },

  url: function() {
    var queryParams = "";
    for (var key in this.appData) {
      queryParams += key + "=" + this.appData[key] + "&";
    }
    return "/catissuecore/rest/forms?" + queryParams;
  }
});

FormRecordCollection = Backbone.Collection.extend({
  model: FormRecord,

  initialize: function(args) {
    this.formId = args.formId;
    this.appData = args.appData;
  },

  url: function() {
    var queryParams = "";
    for (var key in this.appData) {
      queryParams += key + "=" + this.appData[key] + "&";
    }

    return "/catissuecore/rest/forms/" + this.formId + "/records?" + queryParams;
  },

  parse: function(response) {
    return response.records;
  }
});

ContainerCollection = Backbone.Collection.extend({
  model: Container,

  url: function() {
    return "/catissuecore/rest/containers";
  }
});

CpCollection = Backbone.Collection.extend({
  model: CollectionProtocol,
  
  url: function() {
    return '/catissuecore/rest/collection-protocols';
  }
});
