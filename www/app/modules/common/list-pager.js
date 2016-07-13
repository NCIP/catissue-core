angular.module('openspecimen')
  .factory('ListPagerOpts', function() {
    var maxRecords = 100;

    var Opts = function(options) {
      angular.extend(this, options);
      if (!angular.isDefined(this.recordsPerPage)) {
        this.recordsPerPage = maxRecords;
      }

      this.modifCnt = 0;
    }

    Opts.prototype.refreshOpts = function(list) {
      this.modifCnt++;
      this.currentPageRecs = list.length;

      if (list.length > this.recordsPerPage) {
        list.splice(this.recordsPerPage, list.length);
      }
    }

    Opts.MAX_PAGE_RECS = maxRecords;

    return Opts;
  })
  .directive('osListPager', function() {

    function showListSize(pagerCtx, opts) {
      pagerCtx.listSize = -1;
      pagerCtx.showListSize = true;

      opts.listSizeGetter().then(
        function(resp) {
          pagerCtx.listSize = resp.count;
        }
      );
    }

    return {
      restrict: 'E',

      replace: true,

      scope: {
        opts: '='
      },

      link: function(scope, element, attrs) {
        var pagerCtx = scope.pagerCtx  = {
          showListSize: false,
          listSize: -1,
          viewSize: 0
        };

        scope.$watch('opts',
          function(opts) {
            if (!opts) {
              return;
            }

            if (opts.currentPageRecs > opts.recordsPerPage) {
              angular.extend(pagerCtx, {showMore: true, viewSize: opts.recordsPerPage});
              if (pagerCtx.showListSize) {
                showListSize(pagerCtx, opts);
              }
            } else {
              angular.extend(pagerCtx, {showMore: false, viewSize: opts.currentPageRecs});
            }
          }, true);

        scope.showListSize = function() {
          showListSize(pagerCtx, scope.opts);
        }
      },

      templateUrl: 'modules/common/list-pager.html'
    }
  });
