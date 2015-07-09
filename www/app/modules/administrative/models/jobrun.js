angular.module('os.administrative.models.jobrun', ['os.common.models'])
  .factory('ScheduledJobRuns', function(osModel, $http) {
    var ScheduledJobRuns = osModel('scheduled-job-runs');

    ScheduledJobRuns.prototype.getType = function() {
      return 'scheduled-job-runs';
    }

    ScheduledJobRuns.prototype.getDisplayName = function() {
      return this.name;
    }

    return ScheduledJobRuns;
  });
