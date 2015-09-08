angular.module('os.common.search.service', [])
  .factory('QuickSearchSvc', function($translate) {
    var entitySearchMap = {}

    function sortByName(a, b) {
      if (a.name == b.name) {
        return 0;
      } else {
        return (a.name < b.name) ? -1 : 1;
      }
    }

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
        results.push({name: key, caption: $translate.instant(value.caption)});
      });

      results.sort(sortByName);
      return results;
    }

    return {
      register: register,

      getEntities: getEntities,

      getTemplate: getTemplate,

      search: search
    };
  })
