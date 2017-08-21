
angular.module('openspecimen')
  .factory('Alerts', function($interval, $translate) {
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
          return msg;
        }

        if (timeout === undefined || timeout === null) {
          timeout = 5000;
        }

        var promise = $interval(function() {
          self.remove(msg);
          $interval.cancel(promise);
        }, timeout, 1);

        return msg;
      },

      success: function(code, params, timeout) {
        return this.add($translate.instant(code, params), 'success', timeout);
      },

      info: function(code, params, timeout) {
        return this.add($translate.instant(code, params), 'info', timeout);
      },

      warn: function(code, params, timeout) {
        return this.add($translate.instant(code, params), 'warning', timeout);
      },

      error: function(code, params, timeout) {
        return this.add($translate.instant(code, params), 'danger', timeout);
      },

      errorText: function(text, timeout) {
        var msg = text;
        if (text instanceof Array) {
          msg = text.join(", ");
        }
        return this.add(msg, 'danger', timeout);
      },

      clear: function() {
        this.messages.length = 0;
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
