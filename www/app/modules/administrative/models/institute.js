angular.module('os.administrative.models.institute', ['os.common.models'])
  .factory('Institute', function(osModel, $http, $q) {
    var Institute = osModel('institutes');

    Institute.prototype.getType = function() {
      return 'institute';
    }

    Institute.prototype.getDisplayName = function() {
      return this.name;
    }

    Institute.getByName = function (name) {
      return $http.get(Institute.url() + 'byname', {params: {name: name}}).then(
        function(result) {
          return new Institute(result.data);
        }
      );
    }
    
    Institute.getNames = function (institutes) {
      return institutes.map(
        function (institute) {
          return institute.name;
        }
      );
    };

    Institute.getSites = function (instituteName, siteName) {
      var params = {name: instituteName};
      if (siteName) {
        params.siteName = siteName;
      }

      return $http.get(Institute.url() + 'sites', {params: params}).then(
        function(result) {
          return result.data;
        }
      );
    }

    return Institute;
  });

