angular.module('os.administrative.user.displayname', ['os.administrative.models'])
  .filter('osUserDisplayName', function() {
    return function(user) {
      // Last name not exist only for 'System' user.
      if (!user.lastName) {
        return user.firstName;
      }

      return user.firstName + ' ' + user.lastName;
    }
  });
