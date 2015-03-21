
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
        var collection = undefined;
        if (response.data instanceof Array) {
          collection = response.data;
        } else if (response.data[modelName] instanceof Array) {
          collection = response.data[modelName];
        } else {
          collection = [];
        }

        return collection.map(function(item) {
          return new Model(item);
        });
      };

      Model.noTransform = function(response) {
        return response.data;
      };

      Model.query = function(reqParams, transformer) {
        var respTransformer = transformer || Model.modelArrayRespTransform;
        return $http.get(url, {params: reqParams}).then(respTransformer);
      };

      Model.getById = function (id) {
        return $http.get(url + id).then(Model.modelRespTransform);
      };

      Model.getDependencyStat = function(id) {
        return $http.get(url + id + '/dependency-stat').then(Model.noTransform);
      }

      Model._lazyCollectionInit = function(source, dest) {
        angular.forEach(source, function(item) {
          dest.push(item);
        });
      };

      Model._flatten = function(entities, childrenPropName, parent, depth) {
        var result = [];
        if (!entities) {
          return result;
        }

        depth = depth || 0;
        angular.forEach(entities, function(entity) {
          result.push(entity);
          entity.depth = depth || 0;
          entity.parent = parent;
          entity.hasChildren = (!!entity[childrenPropName] && entity[childrenPropName].length > 0);
          if (entity.hasChildren) {
            result = result.concat(Model._flatten(entity[childrenPropName], childrenPropName, entity, depth + 1));
          }
        });

        return result;
      };

      Model.prototype.$id = function () {
        return this.id;
      };

      Model.prototype.$save = function () {
        return $http.post(url, this.$saveProps()).then(Model.modelRespTransform);
      };

      Model.prototype.$update = function () {
        return $http.put(url + this.$id(), this.$saveProps()).then(Model.modelRespTransform);
      };

      Model.prototype.$saveOrUpdate = function () {
        return this.$id() ? this.$update() : this.$save();
      };

      Model.prototype.$remove = function () {
        return $http['delete'](url + this.$id()).then(Model.modelRespTransform);
      };

      Model.prototype.$saveProps = function() { 
        return this;
      };

      return Model;
    };

    return ModelFactory;
  });
