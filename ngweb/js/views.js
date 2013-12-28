var FormViewManager = function(appData) {
  this.formsCollection = new FormCollection(appData);
  this.formsListView = new ViewListOfForms({collection: this.formsCollection});
  this.currentView = this.formsListView;

  this.displayForms = function() {
    if (this.currentView != this.formsListView) {
      this.currentView.destroy();
    }
         
    if (this.formsListView) {
      this.formsListView.show();
    } else {
      // TODO: Is this else needed?
      this.formsListView = new ViewListOfForms({collection: this.formsCollection});
    }

    this.currentView = this.formsListView;
  },

  this.displayFormDataSummary = function(formId) {
    this.currentView.destroy();

    var form = this.formsCollection.get(formId);
    if (!form) {
      form = new Form({id: formId});
      form.fetch({async: false});
      this.formsCollection.add(form);
    }

    this.currentView = new ViewListOfFormRecords({model: form, appData: appData});
    this.currentView.render();
  };

  this.displayForm = function(formId, recordId) {
    this.currentView.destroy();
    this.currentView = new ViewForm({formId: formId, recordId: recordId, appData: appData});
    this.currentView.render();
  };
}

var formViewMgr = null;

FormRouter = Backbone.Router.extend({
  routes : {
    "" : "displayForms",
    "displayFormDataSummary/:id"       : "displayFormDataSummary",
    "displayForm/:id(/data/:recordId)" : "displayForm",
    "*path": "hello"
  },

  hello: function(path) {
    alert(path);
  },

  displayForms: function() {
    formViewMgr.displayForms();
  },

  displayFormDataSummary: function(id) {
    formViewMgr.displayFormDataSummary(id);
  },

  displayForm: function(id, recordId) {
    formViewMgr.displayForm(parseInt(id), parseInt(recordId));
  }
});

var router = null;

ViewListOfForms = Backbone.View.extend({
  el: '#forms-summary',
 
  formsListEl: $('#forms-list'),

  initialize: function() {
    this.listenTo(this.collection, "add", this.add);
    this.collection.fetch({async: false});
  },

  add: function(form) {
    this.$el.append(new ViewFormSummary(form).render().el);
  },

  show: function() {
    if (this.collection.length == 1) {
      var form = this.collection.at(0);
      router.navigate("#/displayFormDataSummary/" + form.get('id'));
      return;
    }

    this.formsListEl.show();
  },

  hide: function() {
    this.formsListEl.hide();
  },

  destroy: function() {
    this.hide();
    this.$el.children().remove();
    this.undelegateEvents();
    this.stopListening();
    this.formsListEl.hide();
  }
});

   
ViewFormSummary = Backbone.View.extend({
  template: _.template($(".template_form_summary").html()),

  events: {
    "mouseenter": "toggleSelect",
    "mouseleave": "toggleSelect",
    "click .add-record":  "addRecord", 
    "click .edit-record": "editRecord", 
  },

  initialize: function(model) {
    this.model = model;
    this.model.on('change', this.render, this);
  },

  render: function() {
    this.$el.html(this.template(this.model.toJSON()));
    return this;
  },

  toggleSelect: function() {
    this.$(".form-summary").toggleClass("de-select");
  },

  editRecord: function() {
    router.navigate("#/displayFormDataSummary/" + this.model.get('id'));
  }
});

ViewListOfFormRecords = Backbone.View.extend({
  el: '#form-data-summary',

  panelHeadingTmpl: _.template($(".template_data_summary_panel_heading").html()),

  tableHeadingTmpl: _.template($(".template_data_summary_table_heading").html()),


  events: {
    "click #addFormRecord": "addRecord"
  },

  initialize: function(args) {
    this.appData = args.appData;
    this.records = new FormRecordCollection({formId: this.model.get('id'), appData: this.appData});
    this.records.fetch({async: false});
    this.model.set({noOfRecords: this.records.length});
  },

  render: function() {
    if (this.records.length == 0) {
      router.navigate("#/displayForm/" + this.model.get("id"));
      return;
    } else if (this.records.length == 1) {
      router.navigate("#/displayForm/" + this.model.get("id") + "/data/" + this.records.at(0).get('id'));
      return;
    }

    this.$(".panel-heading").append(this.panelHeadingTmpl({caption: this.model.get('caption')}));
    var panelBody = this.$(".panel-body");
    panelBody.append(this.tableHeadingTmpl());

    var that = this;
    this.records.each(function(record) {
      panelBody.append(new ViewFormRecordSummary({appData: that.appData, formId: that.model.get('id'), record: record}).render().el);
    });
    this.$el.show();
    this.$el.removeClass("hidden");
    return this;
  },

  addRecord: function() {
    router.navigate("#/displayForm/" + this.model.get("id"));
  },

  destroy: function() {
    this.$el.hide();
    this.$(".panel-heading").children().remove();
    this.$(".panel-body").children().remove();
    this.undelegateEvents();
    this.stopListening();
  }
});

ViewFormRecordSummary = Backbone.View.extend({
  tableRowTmpl: _.template($(".template_data_summary_table_row").html()),

  events: {
    "click": "editFormData"
  },

  initialize: function(args) {
    this.record = args.record;
    this.formId = args.formId;
    this.appData = args.appData;
  },

  render: function() {
    alert(JSON.stringify(this.record.toJSON()));
    this.$el.append(this.tableRowTmpl(this.record.toJSON()));
    return this;
  },

  editFormData: function() {
    router.navigate("#/displayForm/" + this.formId + "/data/" + this.record.get('id'));
  }
});

ViewForm = Backbone.View.extend({
  el: "#form-view",

  initialize: function(args) {
    this.appData = args.appData;
    this.formId = args.formId;
    this.recordId = args.recordId;
  },

  render: function() {
    var that = this;
    this.form = new edu.common.de.Form({
      id: this.formId, 
      formDefUrl: '/catissuecore/rest/forms/:formId', 
      recordId: this.recordId || null,
      formDataUrl: '/catissuecore/rest/forms/:formId/records/:recordId',
      appData: this.appData,
      formDiv: 'form-view',
      onSaveSuccess: function() {
        Utility.notify($("#notifications"), "Form Data Saved", "success", true);
      },
      onSaveError: function() {
        Utility.notify($("#notifications"), "Form Data Save Failed", "error", true);
      }
    });
    this.form.render();

    this.$el.removeClass("hidden");
    this.$el.show();
    return this;
  },

  destroy: function() {
    this.form.destroy();
    this.$el.hide();
  }
});
