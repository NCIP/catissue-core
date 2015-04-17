
angular.module('os.administrative.models.form', ['os.common.models'])
  .factory('Form', function(osModel, $http) {
    var Form =
      osModel(
        'forms',
        function(form) {
          form.FormContextModel =  osModel('forms/' + form.$id() + '/contexts');
          form.FormContextModel.prototype.$update = updateFormContext;
        }
      );

    var updateFormContext = function() {
      var form = this.form;
      var formCtxts = [];
      for (var i = 0; i < this.cpIds.length; ++i) {
        formCtxts.push({formId: form.$id(), collectionProtocol: {id: this.cpIds[i]}, level: this.entity, multiRecord: this.isMultiRecord});
      }
      
      return $http.put(form.FormContextModel.url(), formCtxts).then(form.FormContextModel.modelRespTransform);
    };

    Form.prototype.$id = function () {
      return this.formId;
    };

    Form.prototype.getType = function() {
      return 'form';
    }

    Form.prototype.getDisplayName = function() {
      return this.caption;
    }

    Form.prototype.getFormContexts = function() {
      return this.FormContextModel.query();
    };

    Form.prototype.newFormContext = function(formCtxt) {
      return new this.FormContextModel(formCtxt);
    };

    Form.prototype.$remove = function () {
      return $http['delete']( Form.url() + this.$id()).then(function(result) { return result.data; });
    };

    return Form;
  });

