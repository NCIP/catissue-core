angular.module('openspecimen')
  .directive('osInfo', function() {
    return {
      restrict: 'E',
      replace: true,
      template:
        '<li class="dropdown">' +
          '<a class="dropdown-toggle">' +
            '<span class="fa fa-info-circle"></span>' +
          '</a>' +
          '<ul class="dropdown-menu dropdown-menu-right" role="menu">' +
            '<li>' +
              '<div ng-include src="\'modules/common/about.html\'"></div>' +
            '</li>' +
          '</ul>' +
        '</li>'
    };
  });
