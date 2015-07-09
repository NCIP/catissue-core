
angular.module('os.administrative.models.job', ['os.common.models'])
  .factory('ScheduledJobs', function(osModel, $http) {
    var ScheduledJobs = osModel('scheduled-jobs');

    ScheduledJobs.prototype.getType = function() {
      return 'scheduled-jobs';
    }

    ScheduledJobs.prototype.getDisplayName = function() {
      return this.name;
    }

    ScheduledJobs.executeJob = function(jobId) {
      return $http.post(ScheduledJobs.url()  + jobId);
    }
    return ScheduledJobs;
  });
