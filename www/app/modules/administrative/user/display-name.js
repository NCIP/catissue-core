angular.module('os.administrative.user.displayname', ['os.administrative.models'])
  .filter('osUserDisplayName', function() {
    function getDisplayName(user) {
      if (!!user.lastName && !!user.firstName) {
        return user.firstName + ' ' + user.lastName;
      } else if (!!user.firstName) {
        return user.firstName;
      } else if (!!user.lastName) {
        return user.lastName;
      } else {
        return undefined;
      }
    }

    return function(input) {
      if (!input) {
        return undefined;
      }

      return input instanceof Array ?
        input.map(getDisplayName).join(', ') : getDisplayName(input);
    }
  });
