
angular.module('openspecimen')
  .factory('Alerts', function($timeout, $translate) {
    return {
      messages: [],

      add: function(text, type, timeout) {
        var self = this;

        var msg = {
          text: text,
          type: type, 
          timeout: timeout,
          close: function() {
            self.remove(this);
          }           
        };

        this.messages.push(msg);

        if (timeout === false) {
          return;
        }

        if (timeout === undefined || timeout === null) {
          timeout = 5000;
        }

        $timeout(function() { self.remove(msg); }, timeout);
      },

      success: function(code, timeout) {
        this.add($translate.instant(code), 'success', timeout);
      },

      error: function(code, timeout) {
        this.add($translate.instant(code), 'danger', timeout);
      },

      errorText: function(text, timeout) {
        var msg = text;
        if (text instanceof Array) {
          msg = text.join(", ");
        }
        this.add(msg, 'danger', timeout);
      },

      remove: function(msg) {
        var idx = this.messages.indexOf(msg);
        if (idx == -1) {
          return;
        }
   
        this.messages.splice(idx, 1);
      }
    };
  });
