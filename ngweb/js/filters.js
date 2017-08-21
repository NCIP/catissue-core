angular.module("ui.app", ["ui.app.filters"]);

angular.module('ui.app.filters', [])
 .filter('formatDate', function() {
  return function(timeInMs) {
    return Utility.formatDate(timeInMs);
  }
})

.filter("formatUsername", function() {
  return function(user) {
    var username = user.lastName;
    if (username && user.firstName) {
      username += ", ";
    }

    if (user.firstName) {
      username += user.firstName;
    }

    return username;
  }
})

.filter("formatDuration", function() {
  return function(time) {
    if (time < 1000) {
      return time + " ms";
    } else {
      var seconds = Math.floor(time / 1000);
      var ms = time % 1000;
      if (seconds < 60) {
        return seconds + "." + ms + " s";
      } else {
        var minutes = Math.floor(seconds / 60);
        seconds = seconds % 60;
        if (minutes < 60) {
          return minutes + (minutes == 1 ? " min " : " mins ") + seconds + (seconds == 1 ? " sec " : " secs ");
        } else {
          var hours = Math.floor(minutes / 60);
          minutes = minutes % 60;
          return hours + (hours == 1 ? " hr " : " hrs ") + minutes + (minutes == 1 ? " min " : " mins ");
        }
      }
    }
  }
});
