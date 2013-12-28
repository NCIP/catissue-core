Utility = {
  months: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],

  formatDate: function(timeInMs) {
    var input = new Date(timeInMs);
    var current = new Date();
    var dateDiff = current.getDate() - input.getDate();

    if (input.getFullYear() != current.getFullYear()) {
      return Utility.months[input.getMonth()] + " " + input.getDate() + ", " + input.getFullYear();
    } else if (input.getMonth() != current.getMonth() || dateDiff > 1) {
      return Utility.months[input.getMonth()] + " " + input.getDate();
    } else if (dateDiff == 1) {
      return "Yesterday";
    } else if (dateDiff == 0) {
      var suffix = "am";
      var hours = input.getHours();
      if (hours > 11) {
        hours = (hours == 12 ? hours : (hours - 12));
        suffix = "pm";
      }
      return hours + ":" + input.getMinutes() + " " + suffix;
    }
  },

  notify: function(notifDiv, message, type, fade) {
    fade = (typeof fade == 'undefined' || fade == null ) ? true : fade;
    notifDiv.removeClass("alert alert-success alert-info alert-danger hidden");
    notifDiv.html(message);
    var alertClass;
    if (type == 'success') {
      alertClass = "alert alert-success";
    } else if (type == 'error') {
      alertClass = "alert alert-danger";
    } else if (type == 'info') {
      alertClass = "alert alert-info";
    }

    notifDiv.addClass(alertClass).show();
    if (fade) {
      notifDiv.delay(3000).fadeOut(300);
    }
  }
};
