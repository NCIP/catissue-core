
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

      Model.getById = function (id, params) {
        params = !params ? undefined: {params: params};
        return $http.get(url + id, params).then(Model.modelRespTransform);
      };

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
        return (!!this.$id() || this.$id() === 0) ? this.$update() : this.$save();
      };

      Model.prototype.getDependentEntities = function() {
        return $http.get(url + this.$id() + '/dependent-entities').then(Model.noTransform);
      }

      Model.prototype.$close = function(comments) {
        var payload = {status: 'Closed', reason: comments};
        return $http.put(url + this.$id() + '/status', payload).then(Model.modelRespTransform);
      }

      Model.prototype.$remove = function (forceDelete) {
        var params = !!forceDelete ? '?forceDelete=' + forceDelete : '';
        return $http['delete'](url + this.$id() + params).then(Model.modelRespTransform);
      };

      Model.prototype.$patch = function(modifiedProps) {
        var modelObj = this;
        return $http.patch(url + this.$id(), modifiedProps).then(
          function(resp) {
            return angular.extend(modelObj, modifiedProps);
          }
        ); 
      }

      Model.prototype.$saveProps = function() { 
        return this;
      };

      Model.prototype.copyAttrsIfNotPresent = function(src) {
        var that = this;
        angular.forEach(src, function(value, attr) {
          if (!that[attr]) {
            that[attr] = value;
          }
        })
      }

      Model.prototype.attrValue = function(attrName) {
        var nameParts = attrName.split(".");

        var value = this;
        for (var i = 0; i < nameParts.length; ++i) {
          if (value === undefined || value === null || typeof value != 'object') {
            return undefined;
          }

          if (value instanceof Array) {
            var mv = undefined;
            if (i == nameParts.length - 1) {
              mv = [];
              angular.forEach(value, function(val) {
                if (val instanceof Array) {
                  mv = mv.concat(val);
                } else {
                  mv.push(val[nameParts[i]]);
                }
              });
            }

            value = mv;
            break;
          }

          value = value[nameParts[i]];
        }

        return value;
      }

      Model.getCount = function(reqParams) {
        return $http.get(url + 'count', {params: reqParams}).then(Model.noTransform);
      }

      Model.getExtensionCtxt = function(params) {
        return $http.get(url + "extension-form", {params: params}).then(function(result) { return result.data; });
      }

      return Model;
    };

    return ModelFactory;
  });
