
angular.module('openspecimen')
  .directive('osFileUpload', function($timeout, $q, $http) {
    return {
      restrict: 'A',
      replace: true,
      scope: {
        ctrl: '='
      },      
      controller: function() {
        this.data = null;
        this.q = null;

        this.submit = function() {
          this.q = $q.defer();
          if (this.data) {
            this.data.submit();
          } else {
            this.q.reject();
          }

          return this.q.promise;
        };

        this.done = function(data) {
          this.q.resolve(data);
        };

        this.fail = function() {
          this.q.reject();
        }
      },
      link: function(scope, element, attrs, ctrl) {
        $timeout(function() {
          scope.ctrl = ctrl;

          element.find('input').fileupload({
            dataType: 'json',
            beforeSend: function(xhr) {
              xhr.setRequestHeader('X-OS-API-TOKEN', $http.defaults.headers.common['X-OS-API-TOKEN']);
            },
            add: function (e, data) {
              element.find('span').text(data.files[0].name);
              ctrl.data = data;
            },
            done: function(e, data) {
              ctrl.done(data);
            },
            fail: function(e, data) {
              ctrl.fail();
            }
          })
        });
      },
      template: 
        '<div class="os-file-upload">' +
          '<input class="form-control" name="file" type="file">' +
          '<span class="name" translate="common.no_file_selected">' +
             'No File Selected' +
          '</span>' +
        '</div>'
    };
  });
