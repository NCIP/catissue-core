angular.module('os.administrative.user.siteCP', ['os.administrative.models'])
  .filter('siteCP', function($filter, $parse) {
    return function(cpList, userCPRoles, userCPRole) {
      if(cpList == undefined || userCPRoles == undefined || userCPRole.cp == 'All') {
        return cpList;
      }

      for(var i = 0; i < userCPRoles.length; i++) {
        if(userCPRoles[i].cp == 'All') {
          return [];
        }

        if(userCPRole == undefined || userCPRole.cp != userCPRoles[i].cp) {
          cpList = $filter('filter')(cpList, {shortTitle: '!' + userCPRoles[i].cp});
        }
      }

      return cpList;
    }
  })
