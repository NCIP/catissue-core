angular.module('os.common.search.service', [])
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
        results.push({name: key, caption: $translate.instant(value.caption), order: value.order});
      });

      results = results.sort(function(a, b) {return (a.order > b.order) - (b.order > a.order);});

      return results;
    }

    return {
      register: register,

      getEntities: getEntities,

      getTemplate: getTemplate,

      search: search
    };
  })
