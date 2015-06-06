package net.nemerosa.ontrack.extension.issues.combined;

import com.google.common.collect.Sets;
import net.nemerosa.ontrack.extension.api.model.IssueChangeLogExportRequest;
import net.nemerosa.ontrack.extension.issues.IssueServiceExtension;
import net.nemerosa.ontrack.extension.issues.IssueServiceRegistry;
import net.nemerosa.ontrack.extension.issues.export.ExportFormat;
import net.nemerosa.ontrack.extension.issues.export.ExportedIssues;
import net.nemerosa.ontrack.extension.issues.export.IssueExportServiceFactory;
import net.nemerosa.ontrack.extension.issues.model.ConfiguredIssueService;
import net.nemerosa.ontrack.extension.issues.model.Issue;
import net.nemerosa.ontrack.extension.issues.model.IssueServiceConfiguration;
import net.nemerosa.ontrack.extension.support.AbstractExtension;
import net.nemerosa.ontrack.model.support.MessageAnnotator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CombinedIssueServiceExtension extends AbstractExtension implements IssueServiceExtension {

    public static final String SERVICE = "combined";
    private final IssueServiceRegistry issueServiceRegistry;

    @Autowired
    public CombinedIssueServiceExtension(
            CombinedIssueServiceExtensionFeature extensionFeature,
            IssueServiceRegistry issueServiceRegistry,
            IssueExportServiceFactory issueExportServiceFactory) {
        super(extensionFeature);
        this.issueServiceRegistry = issueServiceRegistry;
    }

    /**
     * Gets the list of attached configured issue services.
     *
     * @param issueServiceConfiguration Configuration of the combined issue service
     * @return List of associated configured issue services
     */
    protected Collection<ConfiguredIssueService> getConfiguredIssueServices(IssueServiceConfiguration issueServiceConfiguration) {
        CombinedIssueServiceConfiguration combinedIssueServiceConfiguration = (CombinedIssueServiceConfiguration) issueServiceConfiguration;
        return combinedIssueServiceConfiguration.getIssueServiceConfigurationIdentifiers().stream()
                .map(issueServiceRegistry::getConfiguredIssueService)
                .collect(Collectors.toList());
    }

    @Override
    public String getId() {
        return SERVICE;
    }

    @Override
    public String getName() {
        return "Combined issue service";
    }

    @Override
    public List<? extends IssueServiceConfiguration> getConfigurationList() {
        // FIXME Method net.nemerosa.ontrack.extension.issues.combined.CombinedIssueServiceExtension.getConfigurationList
        return null;
    }

    @Override
    public IssueServiceConfiguration getConfigurationByName(String name) {
        // FIXME Method net.nemerosa.ontrack.extension.issues.combined.CombinedIssueServiceExtension.getConfigurationByName
        return null;
    }

    /**
     * Without any specific configuration, we have to assume the token is valid.
     */
    @Override
    public boolean validIssueToken(String token) {
        return true;
    }

    @Override
    public Set<String> extractIssueKeysFromMessage(IssueServiceConfiguration issueServiceConfiguration, String message) {
        return getConfiguredIssueServices(issueServiceConfiguration).stream()
                .map(
                        configuredIssueService ->
                                configuredIssueService.getIssueServiceExtension().extractIssueKeysFromMessage(
                                        configuredIssueService.getIssueServiceConfiguration(),
                                        message
                                )
                )
                .collect(
                        Collectors.reducing(
                                Collections.<String>emptySet(),
                                Sets::union
                        )
                );
    }

    @Override
    public Optional<MessageAnnotator> getMessageAnnotator(IssueServiceConfiguration issueServiceConfiguration) {
        // FIXME Method net.nemerosa.ontrack.extension.issues.combined.CombinedIssueServiceExtension.getMessageAnnotator
        return null;
    }

    @Override
    public String getLinkForAllIssues(IssueServiceConfiguration issueServiceConfiguration, List<Issue> issues) {
        return null;
    }

    @Override
    public Issue getIssue(IssueServiceConfiguration issueServiceConfiguration, String issueKey) {
        return getConfiguredIssueServices(issueServiceConfiguration).stream()
                .map(
                        configuredIssueService ->
                                configuredIssueService.getIssueServiceExtension().getIssue(
                                        configuredIssueService.getIssueServiceConfiguration(),
                                        issueKey
                                )
                )
                .filter(issue -> issue != null)
                .findFirst()
                .orElse(null)
                ;
    }

    @Override
    public boolean containsIssueKey(IssueServiceConfiguration issueServiceConfiguration, String key, Set<String> keys) {
        return getConfiguredIssueServices(issueServiceConfiguration).stream()
                .anyMatch(
                        configuredIssueService ->
                                configuredIssueService.getIssueServiceExtension().containsIssueKey(
                                        configuredIssueService.getIssueServiceConfiguration(),
                                        key,
                                        keys
                                )
                );
    }

    @Override
    public List<ExportFormat> exportFormats() {
        // FIXME Method net.nemerosa.ontrack.extension.issues.combined.CombinedIssueServiceExtension.exportFormats
        return null;
    }

    @Override
    public ExportedIssues exportIssues(IssueServiceConfiguration issueServiceConfiguration, List<? extends Issue> issues, IssueChangeLogExportRequest request) {
        // FIXME Method net.nemerosa.ontrack.extension.issues.combined.CombinedIssueServiceExtension.exportIssues
        return null;
    }

    @Override
    public Optional<String> getIssueId(IssueServiceConfiguration issueServiceConfiguration, String token) {
        return getConfiguredIssueServices(issueServiceConfiguration).stream()
                .map(
                        configuredIssueService ->
                                configuredIssueService.getIssueServiceExtension().getIssueId(
                                        configuredIssueService.getIssueServiceConfiguration(),
                                        token
                                )
                )
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.<String>empty());
    }
}
