angular.module('openspecimen')
  .factory('QuickSearchSvc', function($translate) {
    var entitySearchMap = {}

    function register(entityName, searchOpts) {
      if (entitySearchMap[entityName]) {
        return;
      }

      entitySearchMap[entityName] = searchOpts;
    }

    function getTemplate(entity) {
      var opts = entitySearchMap[entity];
      return !!opts ? opts.template : null;
    }

    function search(entity, searchData) {
      var opts = entitySearchMap[entity];
      opts.search(searchData);
    }

    function getEntities() {
      var results = [];
      angular.forEach(entitySearchMap, function(value, key) {
        results.push({name: key, caption: value.caption});
      });

      return results;
    }

    return {
      register: register,

      getEntities: getEntities,

      getTemplate: getTemplate,

      search: search
    };
  })
