
var cpCollection = new CpCollection();
var cpCollectionFetched = false;
cpCollection.on('sync', function() {
  cpCollectionFetched = true;
});

ViewListOfContainers = Backbone.View.extend({
  el: '#containers-summary',

  containersListEl: $('#containers-list'),

  initialize: function() {
    this.listenTo(this.collection, "add", this.add);
    this.collection.fetch();
    this.show();
  },

  show: function() {
    this.containersListEl.show();
  },

  add: function(container) {
    this.$el.append(new ViewContainerSummary(container).render().el);
  },

  destroy: function() {
    this.$el.children().remove();
    this.undelegateEvents();
    this.stopListening();
    this.formsListEl.hide();
  }
});


ViewContainerSummary = Backbone.View.extend({
  template: _.template($(".template_container_summary").html()),

  events: {
    "mouseenter": "toggleSelect",
    "mouseleave": "toggleSelect",
    "click .container-summary .col-xs-10": "edit",
    "click .container-attach": "attachForm"
  },

  initialize: function(container) {
    this.container = container;
    this.container.on('change', this.render, this);
  },

  render: function() {
    this.$el.html(this.template(this.container.toJSON()));
    return this;
  },

  toggleSelect: function() {
    this.$(".container-summary").toggleClass("de-select");
  },

  edit: function() {
    window.location = "/catissuecore/loadcsd.do#loadCachedForm/" + this.container.get("id") + "/true";
  },

  attachForm: function() {
    new ViewFormContextDialog(this.container);
  },

  editRecord: function() {
    router.navigate("#/displayFormDataSummary/" + this.container.get('id'));
  }
});

ViewFormContextDialog = Backbone.View.extend({
  template: _.template($(".template_container_attach").html()),

  initialize: function(container) {
    this.container = container;
    if (!cpCollectionFetched) {
      cpCollection.fetch({async: false});
    }

    this.$el.html(this.template({cpCollection: cpCollection.toJSON()}));

    var that = this;
    bootbox.dialog({
      title: 'Form Association',
      message: this.$el.html(), 
      buttons: {
        main: {
          label: "Attach",
          className: 'btn-primary',
          callback: function() {
            var cp = $("#collectionProtocol");
            var entity = $("#entity");
            var formCtx = new Form({containerId: that.container.get('id'), entityType: entity.val(), cpId: cp.val()});
            formCtx.save({}, {
              success: function() { 
                Utility.notify($("#notifications"), "Form Successfully Attached!", "success");
              },

              error: function() {
                Utility.notify($("#notifications"), "Failed to attach the form!", "error");
              }
            });
          }
        },

        cancel: {
          label: 'Cancel',
          className: 'btn-default',
          callback: function() {
            alert("Cancel button");
          }
        }
      }
    });

    $("#collectionProtocol").chosen({width: "100%"});
    $("#entity").chosen({width: "100%"});
  }
});
