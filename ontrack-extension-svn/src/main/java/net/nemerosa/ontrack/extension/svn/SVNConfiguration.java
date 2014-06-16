package net.nemerosa.ontrack.extension.svn;

import lombok.Data;
import net.nemerosa.ontrack.extension.issues.model.IssueServiceConfiguration;
import net.nemerosa.ontrack.extension.issues.model.IssueServiceConfigurationId;
import net.nemerosa.ontrack.extension.support.configurations.UserPasswordConfiguration;
import net.nemerosa.ontrack.model.form.*;
import net.nemerosa.ontrack.model.support.ConfigurationDescriptor;

import java.util.List;

import static net.nemerosa.ontrack.model.form.Form.defaultText;

@Data
public class SVNConfiguration implements UserPasswordConfiguration<SVNConfiguration> {

    private final String name;
    private final String url;
    private final String user;
    @SuppressWarnings("UnusedDeclaration")
    private final String password;
    private final String branchPattern;
    private final String tagPattern;
    private final String tagFilterPattern;
    private final String browserForPath;
    private final String browserForRevision;
    private final String browserForChange;
    private final int indexationInterval;
    private final long indexationStart;
    private final IssueServiceConfiguration issueServiceConfiguration;

    public static Form form(List<IssueServiceConfigurationId> availableIssueServiceConfigurations) {
        return Form.create()
                .with(defaultText())
                .url()
                .with(
                        Text.of("user")
                                .label("User")
                                .length(16)
                                .optional()
                )
                .with(
                        Password.of("password")
                                .label("Password")
                                .length(40)
                                .optional()
                )
                .with(
                        Text.of("branchPattern")
                                .label("Branch pattern")
                                .length(250)
                                .optional()
                                .help("Regular expression applied to SVN paths in order to detect the branches. " +
                                        "It defaults to .*/branches/.*")
                )
                .with(
                        Text.of("tagPattern")
                                .label("Tag pattern")
                                .length(250)
                                .optional()
                                .help("Regular expression applied to SVN paths in order to detect the tags. " +
                                        "It defaults to .*/tags/.*")
                )
                .with(
                        Text.of("tagFilterPattern")
                                .label("Tag filter pattern")
                                .length(100)
                                .optional()
                                .help("Regular expression applied to tag names. Any tag whose name matches " +
                                        "will be excluded from the tags. By default, no tag is excluded.")
                )
                .with(
                        Text.of("browserForPath")
                                .label("Browsing URL for a path")
                                .length(400)
                                .optional()
                                .help("URL that defines how to browse to a path. The path is relative to the " +
                                        "repository root and must be parameterized as ${path} in the URL.")
                )
                .with(
                        Text.of("browserForRevision")
                                .label("Browsing URL for a revision")
                                .length(400)
                                .optional()
                                .help("URL that defines how to browse to a revision. The revision must be " +
                                        "parameterized as ${revision} in the URL.")
                )
                .with(
                        Text.of("browserForChange")
                                .label("Browsing URL for a change")
                                .length(400)
                                .optional()
                                .help("URL that defines how to browse to the changes of a path at a given revision. " +
                                        "The revision must be parameterized as ${revision} in the URL and the path " +
                                        "as ${path}.")
                )
                .with(
                        Int.of("indexationInterval")
                                .label("Indexation interval")
                                .min(0)
                                .max(60 * 24)
                                .value(0)
                                .help("Interval (in minutes) between each indexation of the Subversion repository. A " +
                                        "zero value indicates that no indexation must take place automatically and they " +
                                        "have to be triggered manually.")
                )
                .with(
                        Int.of("indexationStart")
                                .label("Indexation start")
                                .min(1)
                                .value(1)
                                .help("Revision to start the indexation from.")
                )
                .with(
                        Selection.of("issueServiceConfiguration")
                                .label("Issue configuration")
                                .help("Select an issue service that is sued to associate tickets and issues to the source.")
                                .optional()
                                .items(availableIssueServiceConfigurations)
                );
    }

    @Override
    public SVNConfiguration obfuscate() {
        return new SVNConfiguration(
                name,
                url,
                user,
                "",
                branchPattern,
                tagPattern,
                tagFilterPattern,
                browserForPath,
                browserForRevision,
                browserForChange,
                indexationInterval,
                indexationStart,
                issueServiceConfiguration
        );
    }

    public Form asForm(List<IssueServiceConfigurationId> availableIssueServiceConfigurations) {
        return form(availableIssueServiceConfigurations)
                .with(defaultText().readOnly().value(name))
                .fill("url", url)
                .fill("user", user)
                .fill("password", "")
                .fill("branchPattern", branchPattern)
                .fill("tagPattern", tagPattern)
                .fill("tagFilterPattern", tagFilterPattern)
                .fill("browserForPath", browserForPath)
                .fill("browserForRevision", browserForRevision)
                .fill("browserForChange", browserForChange)
                .fill("indexationInterval", indexationInterval)
                .fill("indexationStart", indexationStart)
                .fill("issueServiceConfiguration", issueServiceConfiguration != null ?
                       issueServiceConfiguration.toId() : null)
                ;
    }

    @Override
    public SVNConfiguration withPassword(String password) {
        return new SVNConfiguration(
                name,
                url,
                user,
                password,
                branchPattern,
                tagPattern,
                tagFilterPattern,
                browserForPath,
                browserForRevision,
                browserForChange,
                indexationInterval,
                indexationStart,
                issueServiceConfiguration
        );
    }

    @Override
    public ConfigurationDescriptor getDescriptor() {
        return new ConfigurationDescriptor(name, name);
    }
}
