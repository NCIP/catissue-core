
angular.module('os.administrative.models.authdomain', ['os.common.models'])
  .factory('AuthDomain', function(osModel, $http) {
    var AuthDomain = new osModel('auth-domains');

    AuthDomain.getDomainNames = function() {
      return $http.get(AuthDomain.url()).then(function(result) {
        var domains = [];
        angular.forEach(result.data, function(domain) {
          domains.push(domain.name);
        });
        return domains;
      });
    }

    return AuthDomain;
  });
