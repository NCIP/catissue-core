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
      var minutes = input.getMinutes();
      minutes = minutes < 10 ? "0" + minutes : minutes;
      hours = hours < 10 ? "0" + hours : hours;
      return hours + ":" + minutes + " " + suffix;
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
  },
  
  printHtml: function(html, title) {
    var height = $(window).height();
    var width = $(window).width();
    
    var myWindow = window.open("", "MsgWindow");
    myWindow.document.write('<html><head><title>' + title + '</title>');
    
    myWindow.document.write('<link rel="stylesheet" href="ngweb/external/bootstrap/css/bootstrap.min.css" type="text/css" />');
    myWindow.document.write('<link rel="stylesheet" href="ngweb/external/de/css/de.css" type="text/css" />');
    myWindow.document.write('<link rel="stylesheet" href="ngweb/css/app.css" type="text/css" />');

    myWindow.document.write('</head><body>');
    myWindow.document.write(html);
    myWindow.document.write('</body></html>');

    myWindow.print();
    
    //myWindow.close();
    return true;
  },
  
  get: function(http, url, successfn, params) {
    if (params == undefined) {
      params = {
        '_reqTime' : new Date().getTime()
      }
    } else {
    	params['_reqTime'] = new Date().getTime();
    }
	if (successfn == undefined){
	  return http({method: 'GET', url: url, params: params});
	}
	else {
      return http({method: 'GET', url: url, params: params}).then(successfn);
	}
  }
};
