
angular.module('os.common.models', [])
  .factory('osModel', function(ApiUrls, $http, $q) {
    function ModelFactory(modelName, initCb) {
      var url = ApiUrls.getBaseUrl() + modelName + '/';
      
    
      var Model = function(data) {
        angular.extend(this, data);
        if (typeof initCb == 'function') {
          initCb(this);
        }
      };

      Model.url = function() {
        return url;
      };

      Model.modelRespTransform = function(response) {
        return new Model(response.data);
      };

      Model.modelArrayRespTransform = function(response) {
        return response.data.map(function(item) {
          return new Model(item);
        });
      };

      Model.noTransform = function(response) {
        return response.date;
      };

      Model.query = function(reqParams) {
        return $http.get(url, {params: reqParams}).then(Model.modelArrayRespTransform);
      };

      Model.getById = function (id) {
        return $http.get(url + id).then(Model.modelRespTransform);
      };

      Model.prototype.$id = function () {
        return this.id;
      };

      Model.prototype.$save = function () {
        return $http.post(url, this).then(Model.modelRespTransform);
      };

      Model.prototype.$update = function () {
        return $http.put(url + this.$id(), this).then(Model.modelRespTransform);
      };

      Model.prototype.$saveOrUpdate = function () {
        return this.$id() ? this.$update() : this.$save();
      };

      Model.prototype.$remove = function () {
        return $http['delete'](url + this.$id()).then(Model.modelRespTransform);
      };

      return Model;
    };

    return ModelFactory;
  });
