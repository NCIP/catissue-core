angular.module('os.administrative.user.displayname', ['os.administrative.models'])
  .filter('osUserDisplayName', function() {
    return function(user) {
      if (!user.lastName) {
        return user.firstName;
      }

      return user.firstName + ' ' + user.lastName;
    }
  });
