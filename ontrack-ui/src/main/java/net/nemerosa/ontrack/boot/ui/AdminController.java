package net.nemerosa.ontrack.boot.ui;

import net.nemerosa.ontrack.job.JobScheduler;
import net.nemerosa.ontrack.job.JobStatus;
import net.nemerosa.ontrack.model.Ack;
import net.nemerosa.ontrack.model.security.ApplicationManagement;
import net.nemerosa.ontrack.model.security.SecurityService;
import net.nemerosa.ontrack.model.support.ApplicationLogEntry;
import net.nemerosa.ontrack.model.support.ApplicationLogService;
import net.nemerosa.ontrack.model.support.Page;
import net.nemerosa.ontrack.ui.controller.AbstractResourceController;
import net.nemerosa.ontrack.ui.resource.Pagination;
import net.nemerosa.ontrack.ui.resource.Resource;
import net.nemerosa.ontrack.ui.resource.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/admin")
public class AdminController extends AbstractResourceController {

    private final JobScheduler jobScheduler;
    private final ApplicationLogService applicationLogService;
    private final HealthEndpoint healthEndpoint;
    private final SecurityService securityService;

    @Autowired
    public AdminController(JobScheduler jobScheduler, ApplicationLogService applicationLogService, HealthEndpoint healthEndpoint, SecurityService securityService) {
        this.jobScheduler = jobScheduler;
        this.applicationLogService = applicationLogService;
        this.healthEndpoint = healthEndpoint;
        this.securityService = securityService;
    }

    /**
     * Gets the health status
     */
    @RequestMapping(value = "status", method = RequestMethod.GET)
    public Resource<Health> getStatus() {
        return Resource.of(
                healthEndpoint.invoke(),
                uri(on(getClass()).getStatus())
        );
    }

    /**
     * Gets the list of application log entries
     */
    @RequestMapping(value = "logs", method = RequestMethod.GET)
    public Resources<ApplicationLogEntry> getLogEntries(Page page) {
        // Gets the entries
        List<ApplicationLogEntry> entries = applicationLogService.getLogEntries(page);
        // Builds the resources
        Resources<ApplicationLogEntry> resources = Resources.of(
                entries,
                uri(on(getClass()).getLogEntries(page))
        );
        // Pagination information
        int offset = page.getOffset();
        int count = page.getCount();
        int actualCount = entries.size();
        int total = applicationLogService.getLogEntriesTotal();
        Pagination pagination = Pagination.of(offset, actualCount, total);
        // Previous page
        if (offset > 0) {
            pagination = pagination.withPrev(
                    uri(on(AdminController.class).getLogEntries(
                            new Page(
                                    Math.max(0, offset - count),
                                    count
                            )
                    ))
            );
        }
        // Next page
        if (offset + count < total) {
            pagination = pagination.withNext(
                    uri(on(AdminController.class).getLogEntries(
                            new Page(
                                    offset + count,
                                    count
                            )
                    ))
            );
        }
        // OK
        return resources.withPagination(pagination);
    }

    /**
     * Gets the list of jobs and their status
     */
    @RequestMapping(value = "jobs", method = RequestMethod.GET)
    public Resources<JobStatus> getJobs() {
        return Resources.of(
                jobScheduler.getJobStatuses(),
                uri(on(getClass()).getJobs())
        );
    }

    /**
     * Launches a job
     */
    @RequestMapping(value = "jobs/{id:\\d+}", method = RequestMethod.POST)
    public Ack launchJob(@PathVariable long id) {
        securityService.checkGlobalFunction(ApplicationManagement.class);
        return jobScheduler.getJobKey(id)
                .map(key -> Ack.validate(jobScheduler.fireImmediately(key) != null))
                .orElse(Ack.NOK);
    }

    /**
     * Pauses a job
     */
    @RequestMapping(value = "jobs/{id:\\d+}/pause", method = RequestMethod.POST)
    public Ack pauseJob(@PathVariable long id) {
        securityService.checkGlobalFunction(ApplicationManagement.class);
        return jobScheduler.getJobKey(id)
                .map(key -> Ack.validate(jobScheduler.pause(key)))
                .orElse(Ack.NOK);
    }

    /**
     * Resumes a job
     */
    @RequestMapping(value = "jobs/{id:\\d+}/resume", method = RequestMethod.POST)
    public Ack resumeJob(@PathVariable long id) {
        securityService.checkGlobalFunction(ApplicationManagement.class);
        return jobScheduler.getJobKey(id)
                .map(key -> Ack.validate(jobScheduler.resume(key)))
                .orElse(Ack.NOK);
    }

    /**
     * Deleting a job
     */
    @RequestMapping(value = "jobs/{id:\\d+}", method = RequestMethod.DELETE)
    public Ack deleteJob(@PathVariable long id) {
        securityService.checkGlobalFunction(ApplicationManagement.class);
        return jobScheduler.getJobKey(id)
                .filter(key -> !jobScheduler.getJobStatus(key).get().isValid())
                .map(key -> Ack.validate(jobScheduler.unschedule(key)))
                .orElse(Ack.NOK);
    }

}
