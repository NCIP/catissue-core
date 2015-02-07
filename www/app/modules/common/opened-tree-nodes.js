angular.module('openspecimen')
  .filter('openedTreeNodes', function() {
    var showNode = function(node) {
      if (node.depth == 0) {
        return true;
      }

      var show = true;
      while (!!node.parent) {
        if (!node.parent.isOpened) {
          show = false;
          break;
        }

        node = node.parent;
      }

      return show;
    };

    return function(input) {
      if (!input) {
        return [];
      }

      var result = [];
      angular.forEach(input, function(node) {
        if (showNode(node)) {
          result.push(node);
        }
      });

      return result;
    };
  });
