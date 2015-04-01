
angular.module('os.administrative.models.authdomain', ['os.common.models'])
  .factory('AuthDomain', function(osModel) {
    var AuthDomain = new osModel('auth-domains');

    return AuthDomain;
  });
