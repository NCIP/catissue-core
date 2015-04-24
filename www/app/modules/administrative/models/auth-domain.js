
angular.module('os.administrative.models.authdomain', ['os.common.models'])
  .factory('AuthDomain', function(osModel) {
    var AuthDomain = new osModel('auth-domains');

    AuthDomain.getDomainNames = function() {
      return AuthDomain.query().then(
        function(domains) {
          return domains.map(function(domain) { return domain.name; });
        }
      );
    };

    return AuthDomain;
  });
